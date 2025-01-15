package chumbanotz.mutantbeasts.util;

import chumbanotz.mutantbeasts.entity.ai.EntityAICopySummonerTarget;
import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class ZombieResurrection extends BlockPos {
    private final World world;
    private int tick;

    public ZombieResurrection(World world, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
        this.tick = 100 + world.rand.nextInt(40);
    }

    public ZombieResurrection(World world, BlockPos pos, int tick) {
        super(pos);
        this.world = world;
        this.tick = tick;
    }

    public static int getSuitableGround(World world, int x, int y, int z) {
        return getSuitableGround(world, x, y, z, 4, true);
    }

    public static int getSuitableGround(World world, int x, int y, int z, int range, boolean checkDay) {
        int i = y;
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(x, y, z);
        BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos(checkPos);
        while (Math.abs(y - i) <= range) {
            checkPos.setY(i);
            abovePos.setY(i + 1);
            IBlockState blockState = world.getBlockState(checkPos);
            IBlockState blockStateUp = world.getBlockState(abovePos);
            if (blockState.getBlock() != Blocks.FIRE) {
                if (blockState.getBlock() != Blocks.LAVA) {
                    if (world.isAirBlock(checkPos)) {
                        i--;
                        continue;
                    }
                    if (!world.isAirBlock(checkPos) && world.isAirBlock(abovePos) && blockState.getBlock().isPassable(world, checkPos)) {
                        i--;
                    } else if (!world.isAirBlock(checkPos) && !world.isAirBlock(abovePos) && !blockStateUp.getBlock().isPassable(world, abovePos)) {
                        i++;
                        continue;
                    }
                }
                if (checkDay && world.isDaytime()) {
                    BlockPos pos1 = new BlockPos(x, y + 1, z);
                    float f = world.getLightBrightness(pos1);
                    if (f > 0.5F && world.canSeeSky(pos1) && world.rand.nextInt(3) != 0)
                        return -1;
                }
                return i;
            }
            return -1;
        }
        return -1;
    }

    public static boolean canBeResurrected(Class<? extends Entity> entityClass) {
        return (entityClass == EntityZombie.class || entityClass == EntityZombieVillager.class || entityClass == EntityHusk.class);
    }

    public static Class<? extends EntityLiving> getZombieByLocation(World world, BlockPos pos) {
        List<Biome.SpawnListEntry> zombieEntries = null;
        for (Biome.SpawnListEntry entry : world.getBiome(pos).getSpawnableList(EnumCreatureType.MONSTER)) {
            if (canBeResurrected(entry.entityClass)) {
                if (zombieEntries == null)
                    zombieEntries = new ArrayList<>();
                zombieEntries.add(entry);
            }
        }
        return (zombieEntries == null || zombieEntries.isEmpty()) ? EntityZombie.class : WeightedRandom.getRandomItem(world.rand, zombieEntries).entityClass;
    }

    public int getTick() {
        return this.tick;
    }

    public boolean update(MutantZombieEntity mutantZombie) {
        if (this.world.isAirBlock(this) || !this.world.getBlockState(this).getMaterial().blocksMovement())
            return false;
        if (mutantZombie.getRNG().nextInt(15) == 0)
            this.world.playEvent(2001, up(), Block.getStateId(this.world.getBlockState(this)));
        if (--this.tick <= 0) {
            BlockPos posUp = up();
            EntityZombie zombie = (EntityZombie) EntityList.newEntity(getZombieByLocation(this.world, posUp), this.world);
            IEntityLivingData entityLivingData = null;
            entityLivingData = zombie.onInitialSpawn(this.world.getDifficultyForLocation(this), entityLivingData);
            zombie.setHealth(zombie.getMaxHealth() * (0.6F + 0.4F * zombie.getRNG().nextFloat()));
            zombie.playLivingSound();
            zombie.dismountRidingEntity();
            this.world.playEvent(2001, posUp, Block.getStateId(this.world.getBlockState(this)));
            if (!this.world.isRemote) {
                zombie.targetTasks.addTask(0, new EntityAICopySummonerTarget(zombie, mutantZombie));
                zombie.moveToBlockPosAndAngles(posUp, mutantZombie.rotationYaw, 0.0F);
                this.world.spawnEntity(zombie);
            }
            return false;
        }
        return true;
    }
}

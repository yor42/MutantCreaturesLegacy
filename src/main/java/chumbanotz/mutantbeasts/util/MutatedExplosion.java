package chumbanotz.mutantbeasts.util;

import chumbanotz.mutantbeasts.entity.SkullSpiritEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import chumbanotz.mutantbeasts.entity.projectile.ChemicalXEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MutatedExplosion extends Explosion {
    private final Entity exploder;

    private final World world;

    private final float size;

    private MutatedExplosion(World world, Entity exploder, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain) {
        super(world, exploder, x, y, z, size, causesFire, damagesTerrain);
        this.exploder = exploder;
        this.world = world;
        this.size = size;
    }

    public static MutatedExplosion create(@Nonnull Entity exploder, float size, boolean causesFire, boolean damagesTerrain) {
        return create(exploder.world, exploder, exploder.posX, exploder.posY, exploder.posZ, size, causesFire, damagesTerrain);
    }

    public static MutatedExplosion create(World world, @Nullable Entity exploder, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain) {
        if (damagesTerrain && exploder instanceof net.minecraft.entity.EntityLiving && !ForgeEventFactory.getMobGriefingEvent(world, exploder))
            damagesTerrain = false;
        MutatedExplosion explosion = new MutatedExplosion(world, exploder, x, y, z, size, causesFire, damagesTerrain);
        if (ForgeEventFactory.onExplosionStart(world, explosion))
            return explosion;
        if (world instanceof net.minecraft.world.WorldServer) {
            explosion.doExplosionA();
            explosion.doExplosionB(false);
            if (!damagesTerrain)
                explosion.clearAffectedBlockPositions();
            for (EntityPlayer entityplayer : world.playerEntities) {
                if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
                    ((EntityPlayerMP) entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, size, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
            }
        }
        return explosion;
    }

    private static float getBlockDensity(Vec3d vec, Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int j2 = 0;
            int k2 = 0;
            float f;
            for (f = 0.0F; f <= 1.0F; f = (float) (f + d0)) {
                float f1;
                for (f1 = 0.0F; f1 <= 1.0F; f1 = (float) (f1 + d1)) {
                    float f2;
                    for (f2 = 0.0F; f2 <= 1.0F; f2 = (float) (f2 + d2)) {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * f;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * f1;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * f2;
                        RayTraceResult result = entity.world.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec, false, true, false);
                        if (result == null || result.typeOfHit == RayTraceResult.Type.MISS || (result.typeOfHit == RayTraceResult.Type.BLOCK && entity.world.getBlockState(result.getBlockPos()).getBoundingBox(entity.world, result.getBlockPos()) != Block.FULL_BLOCK_AABB))
                            j2++;
                        k2++;
                    }
                }
            }
            return (float) j2 / k2;
        }
        return 0.0F;
    }

    public void doExplosionA() {
        if (this.size <= 0.0F)
            return;
        Set<BlockPos> set = new HashSet<>();
        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                for (int l = 0; l < 16; l++) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (j / 15.0F * 2.0F - 1.0F);
                        double d1 = (k / 15.0F * 2.0F - 1.0F);
                        double d2 = (l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float intensity = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double x = (getPosition()).x;
                        double y = (getPosition()).y;
                        double z = (getPosition()).z;
                        for (float attenuation = 0.3F; intensity > 0.0F; intensity -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(x, y, z);
                            IBlockState iblockstate = this.world.getBlockState(blockpos);
                            if (!this.world.isAirBlock(blockpos) && !iblockstate.getMaterial().isLiquid()) {
                                float resistance = (this.exploder != null) ? this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(this.world, blockpos, this.exploder, this);
                                intensity -= (resistance + attenuation) * attenuation;
                            }
                            if (intensity > 0.0F && !iblockstate.getMaterial().isLiquid() && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, intensity)))
                                set.add(blockpos);
                            x += d0 * 0.30000001192092896D;
                            y += d1 * 0.30000001192092896D;
                            z += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }
        getAffectedBlockPositions().addAll(set);
        float diameter = this.size * 2.0F;
        int minX = MathHelper.floor((getPosition()).x - diameter - 1.0D);
        int maxX = MathHelper.floor((getPosition()).x + diameter + 1.0D);
        int minY = MathHelper.floor((getPosition()).y - diameter - 1.0D);
        int maxY = MathHelper.floor((getPosition()).y + diameter + 1.0D);
        int minZ = MathHelper.floor((getPosition()).z - diameter - 1.0D);
        int maxZ = MathHelper.floor((getPosition()).z + diameter + 1.0D);
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this.exploder, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), entity -> !entity.isImmuneToExplosions() && (!(this.exploder instanceof SkullSpiritEntity) || ((entity == ((SkullSpiritEntity) this.exploder).getTarget()) ? (!((SkullSpiritEntity) this.exploder).isAttached()) : ChemicalXEntity.IS_APPLICABLE.test(entity))));
        ForgeEventFactory.onExplosionDetonate(this.world, this, list, diameter);
        DamageSource damageSource = DamageSource.causeExplosionDamage(this);
        for (Entity entity : list) {
            double distance = entity.getDistance((getPosition()).x, (getPosition()).y, (getPosition()).z) / diameter;
            if (distance <= 1.0D) {
                double x = entity.posX - (getPosition()).x;
                double y = entity.posY + entity.getEyeHeight() - (getPosition()).y;
                double z = entity.posZ - (getPosition()).z;
                double sqrt = MathHelper.sqrt(x * x + y * y + z * z);
                if (sqrt != 0.0D) {
                    x /= sqrt;
                    y /= sqrt;
                    z /= sqrt;
                    double intensity = (1.0D - distance) * getBlockDensity(getPosition(), entity);
                    float damage = (int) ((intensity * intensity + intensity) / 2.0D * 6.0D * diameter + 1.0D);
                    if (!entity.attackEntityFrom(damageSource, damage) && this.exploder instanceof MutantCreeperEntity && entity instanceof EntityPlayer && ((EntityPlayer) entity).isActiveItemStackBlocking()) {
                        MutantCreeperEntity mutantCreeper = (MutantCreeperEntity) this.exploder;
                        EntityPlayer player = (EntityPlayer) entity;
                        player.velocityChanged = true;
                        if (mutantCreeper.isJumpAttacking()) {
                            EntityUtil.disableShield(player, mutantCreeper.getPowered() ? 200 : 100);
                            player.attackEntityFrom(damageSource, damage * 0.5F);
                        } else {
                            player.getActiveItemStack().damageItem((int) damage * 2, player);
                            player.attackEntityFrom(damageSource, damage * 0.5F);
                        }
                    }
                    double scale = intensity;
                    if (entity instanceof EntityLivingBase)
                        scale = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, intensity);
                    if (!(entity instanceof MutantCreeperEntity)) {
                        entity.motionX += x * scale;
                        entity.motionY += y * scale;
                        entity.motionZ += z * scale;
                    }
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer entityplayer = (EntityPlayer) entity;
                        if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying))
                            getPlayerKnockbackMap().put(entityplayer, new Vec3d(x * intensity, y * intensity, z * intensity));
                    }
                }
            }
        }
    }
}

package chumbanotz.mutantbeasts.entity.projectile;

import chumbanotz.mutantbeasts.entity.EndersoulFragmentEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.util.EntityUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class ThrowableBlockEntity extends EntityThrowable implements IEntityAdditionalSpawnData, IThrowableEntity {
    private static final DataParameter<Boolean> HELD = EntityDataManager.createKey(ThrowableBlockEntity.class, DataSerializers.BOOLEAN);

    private IBlockState blockState = Blocks.GRASS.getDefaultState();

    private UUID ownerUUID;

    public ThrowableBlockEntity(World worldIn) {
        super(worldIn);
        setSize(1.0F, 1.0F);
    }

    public ThrowableBlockEntity(World worldIn, MutantSnowGolemEntity mutantSnowGolem) {
        super(worldIn, mutantSnowGolem.posX, mutantSnowGolem.posY + 1.954D, mutantSnowGolem.posZ);
        this.rotationYaw = mutantSnowGolem.rotationYaw;
        setThrower(mutantSnowGolem);
        this.blockState = Blocks.ICE.getDefaultState();
    }

    public ThrowableBlockEntity(World world, MutantEndermanEntity enderman, int armID) {
        super(world, enderman.posX, enderman.posY + 4.7D, enderman.posZ);
        setThrower(enderman);
        this.blockState = Block.getStateById(enderman.heldBlock[armID]);
        boolean outer = (armID <= 2);
        boolean right = ((armID & 0x1) == 1);
        EntityLivingBase living = enderman.getAttackTarget();
        Vec3d forward = EntityUtil.getDirVector(this.rotationYaw, outer ? 2.7F : 1.4F);
        Vec3d strafe = EntityUtil.getDirVector(this.rotationYaw + (right ? 90.0F : -90.0F), outer ? 2.2F : 2.0F);
        this.posX += forward.x + strafe.x;
        this.posY += ((outer ? 2.8F : 1.1F) - 4.8F);
        this.posZ += forward.z + strafe.z;
        if (living != null) {
            shoot(living.posX - this.posX, living.posY + living.getEyeHeight() - this.posY, living.posZ - this.posZ, 1.4F, 1.0F);
        } else {
            throwBlock();
        }
    }

    public ThrowableBlockEntity(World world, EntityPlayer player, IBlockState blockState, BlockPos pos) {
        super(world, player);
        setThrower(player);
        this.blockState = blockState;
        setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        setHeld(true);
    }

    protected void entityInit() {
        this.dataManager.register(HELD, false);
    }

    public IBlockState getBlockState() {
        return this.blockState;
    }

    @Nullable
    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.ownerUUID != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.ownerUUID);
            if (entity instanceof EntityLivingBase)
                setThrower(entity);
        }
        return this.thrower;
    }

    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.thrower = (EntityLivingBase) entity;
            this.ownerUUID = entity.getUniqueID();
        }
    }

    public boolean isHeld() {
        return this.dataManager.get(HELD);
    }

    private void setHeld(boolean held) {
        this.dataManager.set(HELD, held);
    }

    protected float getGravityVelocity() {
        if (this.thrower instanceof MutantSnowGolemEntity)
            return 0.06F;
        if (this.thrower instanceof EntityPlayer)
            return 0.04F;
        return 0.01F;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return (isHeld() && !this.isDead);
    }

    public boolean canBePushed() {
        return (isHeld() && !this.isDead);
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    public void applyEntityCollision(Entity entityIn) {
        if (entityIn != this.thrower)
            super.applyEntityCollision(entityIn);
    }

    public void handleStatusUpdate(byte id) {
        if (id == 3)
            for (int i = 0; i < 60; i++) {
                double x = this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width;
                double y = this.posY + 0.5D + (this.rand.nextFloat() * this.height);
                double z = this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width;
                double motx = ((this.rand.nextFloat() - this.rand.nextFloat()) * 3.0F);
                double moty = (0.5F + this.rand.nextFloat() * 2.0F);
                double motz = ((this.rand.nextFloat() - this.rand.nextFloat()) * 3.0F);
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x, y, z, motx, moty, motz, Block.getStateId(this.blockState));
            }
    }

    public void onUpdate() {
        if (isHeld()) {
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            if (!this.world.isRemote)
                setFlag(6, isGlowing());
            onEntityUpdate();
            if (this.thrower == null || !this.thrower.isEntityAlive() || !EntitySelectors.NOT_SPECTATING.apply(this.thrower) || !EndersoulFragmentEntity.isProtected(this.thrower)) {
                setHeld(false);
            } else {
                Vec3d vec = this.thrower.getLookVec();
                double x = this.thrower.posX + vec.x * 1.6D - this.posX;
                double y = this.thrower.posY + this.thrower.getEyeHeight() + vec.y * 1.6D - this.posY;
                double z = this.thrower.posZ + vec.z * 1.6D - this.posZ;
                float offset = 0.6F;
                this.motionX = x * offset;
                this.motionY = y * offset;
                this.motionZ = z * offset;
                move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            }
        } else {
            this.ignoreEntity = this.thrower;
            super.onUpdate();
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player.isSneaking() || itemStack.getItem() != MBItems.ENDERSOUL_HAND)
            return false;
        if (isHeld() && this.thrower == player) {
            if (!this.world.isRemote) {
                setHeld(false);
                throwBlock();
            }
            player.swingArm(hand);
            itemStack.damageItem(1, player);
            return true;
        }
        return false;
    }

    private void throwBlock() {
        this.rotationYaw = this.thrower.rotationYaw;
        this.rotationPitch = this.thrower.rotationPitch;
        float f = 0.4F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        shoot(this.motionX, this.motionY, this.motionZ, 1.4F, 1.0F);
    }

    protected void onImpact(RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(result.getBlockPos()).getCollisionBoundingBox(this.world, result.getBlockPos()) == Block.NULL_AABB)
            return;
        if (this.thrower instanceof MutantSnowGolemEntity) {
            if (result.typeOfHit == RayTraceResult.Type.ENTITY && (
                    this.thrower.isOnSameTeam(result.entityHit) || (result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.thrower), 4.0F) && result.entityHit instanceof net.minecraft.entity.monster.EntityEnderman)))
                return;
            for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(2.5D, 2.0D, 2.5D))) {
                if (entity.canBeCollidedWith() && !this.thrower.isOnSameTeam(entity) && getDistanceSq(entity) <= 6.25D)
                    entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), 4.0F + this.rand.nextInt(3));
            }
            if (!this.world.isRemote) {
                playSound(this.blockState.getBlock().getSoundType(this.blockState, this.world, getPosition(), this).getBreakSound(), 0.8F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F);
                this.world.setEntityState(this, (byte) 3);
                setDead();
            }
        } else if (!this.world.isRemote) {
            BlockPos pos = new BlockPos(this);
            boolean canOwnerGrief = (!(this.thrower instanceof net.minecraft.entity.EntityLiving) || ForgeEventFactory.getMobGriefingEvent(this.world, this.thrower));
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                if (canOwnerGrief && canPlaceBlock(result.getBlockPos(), result.sideHit)) {
                    SoundType soundType = this.blockState.getBlock().getSoundType(this.blockState, this.world, pos, this.thrower);
                    playSound(soundType.getPlaceSound(), (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                } else {
                    this.world.playEvent(2001, pos, Block.getStateId(this.blockState));
                    if (this.world.getGameRules().getBoolean("doEntityDrops") && canOwnerGrief) {
                        Block block = this.blockState.getBlock();
                        entityDropItem(new ItemStack(block, 1, block.damageDropped(this.blockState)), 0.0F);
                    }
                }
            } else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
                if (result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.thrower), 4.0F) && result.entityHit instanceof net.minecraft.entity.monster.EntityEnderman)
                    return;
                this.world.playEvent(2001, pos, Block.getStateId(this.blockState));
                if (this.world.getGameRules().getBoolean("doEntityDrops") && canOwnerGrief) {
                    Block block = this.blockState.getBlock();
                    entityDropItem(new ItemStack(block, 1, block.damageDropped(this.blockState)), 0.0F);
                }
            }
            for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(2.0D))) {
                if (entity.canBeCollidedWith() && !entity.isEntityEqual(this.thrower) && getDistanceSq(entity) <= 4.0D)
                    entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), (6 + this.rand.nextInt(3)));
            }
            setDead();
        }
    }

    private boolean canPlaceBlock(BlockPos pos, EnumFacing facing) {
        if (this.thrower instanceof EntityPlayer && !((EntityPlayer) this.thrower).isAllowEdit())
            return false;
        if (!this.blockState.getBlock().isReplaceable(this.world, pos))
            pos = pos.offset(facing);
        return (this.world.mayPlace(this.blockState.getBlock(), pos, false, facing, this.thrower) && this.world.setBlockState(pos, this.blockState, 11));
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("Held", isHeld());
        compound.setTag("BlockState", NBTUtil.writeBlockState(new NBTTagCompound(), this.blockState));
        if (this.ownerUUID != null)
            compound.setUniqueId("OwnerUUID", this.ownerUUID);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        setHeld(compound.getBoolean("Held"));
        if (compound.hasKey("BlockState", 10))
            this.blockState = NBTUtil.readBlockState(compound.getCompoundTag("BlockState"));
        if (compound.hasUniqueId("OwnerUUID"))
            this.ownerUUID = compound.getUniqueId("OwnerUUID");
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(Block.getStateId(this.blockState));
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.blockState = Block.getStateById(additionalData.readInt());
    }
}

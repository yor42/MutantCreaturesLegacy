package chumbanotz.mutantbeasts.entity;

import chumbanotz.mutantbeasts.entity.projectile.ChemicalXEntity;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBParticles;
import chumbanotz.mutantbeasts.util.MutatedExplosion;
import io.netty.buffer.ByteBuf;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.UUID;

public class SkullSpiritEntity extends Entity implements IEntityAdditionalSpawnData {
    private static final DataParameter<Boolean> ATTACHED = EntityDataManager.createKey(SkullSpiritEntity.class, DataSerializers.BOOLEAN);

    private int startTick = 15;

    private int attachedTick = 80 + this.rand.nextInt(40);

    private int targetId;

    private UUID targetUUID;

    public SkullSpiritEntity(World world) {
        super(world);
        this.noClip = true;
        setSize(0.1F, 0.1F);
    }

    public SkullSpiritEntity(World world, EntityLiving target) {
        this(world);
        this.targetId = target.getEntityId();
        this.targetUUID = target.getUniqueID();
    }

    protected void entityInit() {
        this.dataManager.register(ATTACHED, false);
    }

    public boolean isAttached() {
        return this.dataManager.get(ATTACHED);
    }

    private void setAttached(boolean attached) {
        this.dataManager.set(ATTACHED, attached);
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    public Entity getTarget() {
        if (this.targetUUID != null && this.world instanceof WorldServer)
            return ((WorldServer) this.world).getEntityFromUuid(this.targetUUID);
        return this.world.getEntityByID(this.targetId);
    }

    public void onUpdate() {
        Entity target = getTarget();
        if (target != null && target.isEntityAlive()) {
            if (isAttached()) {
                target.motionX = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                target.motionZ = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                if (!this.world.isRemote && --this.attachedTick <= 0 && target instanceof EntityLiving) {
                    EntityLiving mob = (EntityLiving) target;
                    Class<? extends EntityLiving> mutantClass = ChemicalXEntity.getMutantOf(mob);
                    if (mutantClass != null && this.rand.nextFloat() < 0.75F) {
                        MutatedExplosion.create(this, 2.0F, false, false);
                        EntityLiving mutant = EntityUtil.convertMobWithNBT(mob, (EntityLiving) EntityList.newEntity(mutantClass, this.world), true);
                        mutant.enablePersistence();
                        AxisAlignedBB bb = mutant.getEntityBoundingBox();
                        for (BlockPos pos : BlockPos.getAllInBox(MathHelper.floor(bb.minX), MathHelper.floor(bb.minY), MathHelper.floor(bb.minZ), MathHelper.floor(bb.maxX), MathHelper.floor(bb.maxY), MathHelper.floor(bb.maxZ))) {
                            if (this.world.getBlockState(pos).getBlockHardness(this.world, pos) >= 0.0F)
                                this.world.destroyBlock(pos, true);
                        }
                        for (EntityPlayerMP entityplayermp : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, bb.grow(5.0D)))
                            CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, mutant);
                    } else {
                        setAttached(false);
                        MutatedExplosion.create(this, 2.0F, false, false);
                    }
                    setDead();
                }
                setPosition(target.posX, target.posY, target.posZ);
                if (this.rand.nextInt(8) == 0) target.attackEntityFrom(DamageSource.MAGIC, 0.0F);
                for (int i = 0; i < 3; i++) {
                    double posX = target.posX + (this.rand.nextFloat() * target.width * 2.0F) - target.width;
                    double posY = target.posY + 0.5D + (this.rand.nextFloat() * target.height);
                    double posZ = target.posZ + (this.rand.nextFloat() * target.width * 2.0F) - target.width;
                    double x = this.rand.nextGaussian() * 0.02D;
                    double y = this.rand.nextGaussian() * 0.02D;
                    double z = this.rand.nextGaussian() * 0.02D;
                    this.world.spawnParticle(MBParticles.SKULL_SPIRIT, posX, posY, posZ, x, y, z);
                }
            } else {
                this.prevPosX = this.posX;
                this.prevPosY = this.posY;
                this.prevPosZ = this.posZ;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                if (this.startTick-- >= 0) this.motionY += (0.3F * this.startTick / 15.0F);
                double x = target.posX - this.posX;
                double y = target.posY - this.posY;
                double z = target.posZ - this.posZ;
                double d = Math.sqrt(x * x + y * y + z * z);
                this.motionX += x / d * 0.20000000298023224D;
                this.motionY += y / d * 0.20000000298023224D;
                this.motionZ += z / d * 0.20000000298023224D;
                move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                if (!this.world.isRemote && getDistanceSq(target) < 1.0D) setAttached(true);
                for (int i = 0; i < 16; i++) {
                    float xx = (this.rand.nextFloat() - 0.5F) * 1.2F;
                    float yy = (this.rand.nextFloat() - 0.5F) * 1.2F;
                    float zz = (this.rand.nextFloat() - 0.5F) * 1.2F;
                    this.world.spawnParticle(MBParticles.SKULL_SPIRIT, this.posX + xx, this.posY + yy, this.posZ + zz, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            setDead();
        }
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("Attached", isAttached());
        compound.setInteger("AttachedTick", this.attachedTick);
        if (this.targetUUID != null) compound.setUniqueId("Target", this.targetUUID);
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        setAttached(compound.getBoolean("Attached"));
        this.attachedTick = compound.getInteger("AttachedTick");
        if (compound.hasUniqueId("Target")) this.targetUUID = compound.getUniqueId("Target");
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.targetId);
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.targetId = additionalData.readInt();
    }
}

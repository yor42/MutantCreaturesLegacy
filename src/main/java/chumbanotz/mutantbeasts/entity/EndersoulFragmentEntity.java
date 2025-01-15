package chumbanotz.mutantbeasts.entity;

import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EndersoulFragmentEntity extends Entity {
    public static final Predicate<Entity> IS_VALID_TARGET;
    private static final DataParameter<Boolean> TAMED = EntityDataManager.createKey(EndersoulFragmentEntity.class, DataSerializers.BOOLEAN);

    static {
        IS_VALID_TARGET = (entity -> {
            Class<?> entityClass = entity.getClass();
            return (EntitySelectors.CAN_AI_TARGET.apply(entity) && entityClass != EntityItem.class && entityClass != EntityXPOrb.class && entityClass != EntityEnderCrystal.class && entityClass != EndersoulCloneEntity.class && entityClass != EndersoulFragmentEntity.class && entityClass != MutantEndermanEntity.class && entityClass != EntityDragon.class && entityClass != EntityEnderman.class);
        });
    }

    public final float[][] stickRotations = new float[8][3];
    private int explodeTick = 20 + this.rand.nextInt(20);
    private EntityPlayer owner;

    public EndersoulFragmentEntity(World world) {
        super(world);
        this.preventEntitySpawning = true;
        for (int i = 0; i < this.stickRotations.length; i++) {
            for (int j = 0; j < (this.stickRotations[i]).length; j++)
                this.stickRotations[i][j] = this.rand.nextFloat() * 2.0F * 3.1415927F;
        }
        setSize(0.75F, 0.75F);
    }

    public static boolean isProtected(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) return false;
        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
        return (entityLivingBase.getHeldItemMainhand().getItem() == MBItems.ENDERSOUL_HAND || entityLivingBase.getHeldItemOffhand().getItem() == MBItems.ENDERSOUL_HAND);
    }

    protected void entityInit() {
        this.dataManager.register(TAMED, false);
    }

    public EntityPlayer getOwner() {
        return this.owner;
    }

    public boolean isTamed() {
        return this.dataManager.get(TAMED);
    }

    public void setTamed(boolean tamed) {
        this.dataManager.set(TAMED, tamed);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return isEntityAlive();
    }

    public boolean canBePushed() {
        return isEntityAlive();
    }

    public void handleStatusUpdate(byte id) {
        if (id == 3) EntityUtil.spawnEndersoulParticles(this, 64, 0.8F);
    }

    public void onUpdate() {
        super.onUpdate();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.owner == null && this.motionY > -0.05000000074505806D && !hasNoGravity())
            this.motionY = Math.max(-0.05000000074505806D, this.motionY - 0.10000000149011612D);
        if (this.owner != null && (!this.owner.isEntityAlive() || !this.owner.isAddedToWorld() || this.world != this.owner.world))
            this.owner = null;
        move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9D;
        this.motionY *= 0.9D;
        this.motionZ *= 0.9D;
        if (!this.world.isRemote) {
            if (!isTamed() && --this.explodeTick == 0) explode();
            if (this.owner != null && !this.owner.isSpectator() && getDistanceSq(this.owner) > 9.0D) {
                float scale = 0.05F;
                addVelocity((this.owner.posX - this.posX) * scale, (this.owner.posY + (this.owner.height / 3.0F) - this.posY) * scale, (this.owner.posZ - this.posZ) * scale);
            }
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (isTamed()) {
            if (this.owner == null && !player.isSneaking()) {
                this.owner = player;
                playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                return true;
            }
            if (this.owner == player && player.isSneaking()) {
                this.owner = null;
                playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
                return true;
            }
            return false;
        }
        if (!this.world.isRemote) setTamed(true);
        this.owner = player;
        playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) return false;
        if (!this.world.isRemote && !this.isDead && this.ticksExisted > 0) explode();
        return true;
    }

    private void explode() {
        playSound(MBSoundEvents.ENTITY_ENDERSOUL_FRAGMENT_EXPLODE, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.world.setEntityState(this, (byte) 3);
        for (Entity entity : this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().grow(5.0D), IS_VALID_TARGET)) {
            if (getDistanceSq(entity) <= 25.0D) {
                boolean protectedEntity = isProtected(entity);
                boolean causeDamage = true;
                if (protectedEntity) causeDamage = (this.rand.nextInt(3) == 0);
                if (causeDamage) entity.attackEntityFrom(DamageSource.MAGIC, 1.0F);
                if (!protectedEntity) {
                    double x = entity.posX - this.posX;
                    double z = entity.posZ - this.posZ;
                    double d = Math.sqrt(x * x + z * z);
                    entity.motionX = 0.800000011920929D * x / d;
                    entity.motionY = (this.rand.nextFloat() * 0.6F - 0.1F);
                    entity.motionZ = 0.800000011920929D * z / d;
                    EntityUtil.sendPlayerVelocityPacket(entity);
                }
            }
        }
        setDead();
    }

    public SoundCategory getSoundCategory() {
        return isTamed() ? SoundCategory.NEUTRAL : SoundCategory.HOSTILE;
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("Tamed", isTamed());
        compound.setInteger("ExplodeTick", this.explodeTick);
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        setTamed((compound.getBoolean("Collected") || compound.getBoolean("Tamed")));
        if (compound.hasKey("ExplodeTick")) this.explodeTick = compound.getInteger("ExplodeTick");
    }
}

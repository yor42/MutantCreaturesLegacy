package chumbanotz.mutantbeasts.entity;

import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import chumbanotz.mutantbeasts.util.MutatedExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.UUID;

public class CreeperMinionEggEntity extends Entity implements IEntityOwnable {
    private static final DataParameter<Boolean> CHARGED = EntityDataManager.createKey(CreeperMinionEggEntity.class, DataSerializers.BOOLEAN);

    private int health = 8;

    private int age = (60 + this.rand.nextInt(40)) * 1200;

    private int recentlyHit;

    private double velocityX;

    private double velocityY;

    private double velocityZ;

    private UUID ownerUUID;

    public CreeperMinionEggEntity(World world) {
        super(world);
        this.preventEntitySpawning = true;
        setSize(0.5625F, 0.75F);
    }

    public CreeperMinionEggEntity(MutantCreeperEntity spawner, EntityPlayer owner) {
        this(spawner.world);
        this.ownerUUID = owner.getUniqueID();
        setPosition(spawner.posX, spawner.posY, spawner.posZ);
        if (spawner.getPowered()) setCharged(true);
    }

    protected void entityInit() {
        this.dataManager.register(CHARGED, false);
    }

    public boolean isCharged() {
        return this.dataManager.get(CHARGED);
    }

    private void setCharged(boolean charged) {
        this.dataManager.set(CHARGED, charged);
    }

    public EntityPlayer getOwner() {
        return (this.ownerUUID == null) ? null : this.world.getPlayerEntityByUUID(this.ownerUUID);
    }

    public UUID getOwnerId() {
        return this.ownerUUID;
    }

    public double getYOffset() {
        if (getRidingEntity() instanceof EntityPlayer)
            return this.height - (getRidingEntity().isSneaking() ? 0.3D : 0.2D);
        return 0.0D;
    }

    public double getMountedYOffset() {
        return this.height;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.canBePushed() ? entity.getEntityBoundingBox() : null;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public boolean canBePushed() {
        return !this.isDead;
    }

    public boolean canRiderInteract() {
        return true;
    }

    public boolean hitByEntity(Entity entityIn) {
        return (entityIn == getRidingEntity());
    }

    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    public void setVelocity(double x, double y, double z) {
        super.setVelocity(x, y, z);
        this.velocityX = x;
        this.velocityY = y;
        this.velocityZ = z;
    }

    private void hatch() {
        CreeperMinionEntity minion = new CreeperMinionEntity(this.world);
        if (this.ownerUUID != null) {
            EntityPlayer player = getOwner();
            if (player != null && !ForgeEventFactory.onAnimalTame(minion, player)) {
                minion.setTamedBy(player);
                minion.getAISit().setSitting(true);
                player.setLastAttackedEntity(null);
            }
        }
        if (isCharged()) minion.setPowered(true);
        minion.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(minion);
        playSound(MBSoundEvents.ENTITY_CREEPER_MINION_EGG_HATCH, 0.7F, 0.9F + this.rand.nextFloat() * 0.1F);
        setDead();
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        super.onStruckByLightning(lightningBolt);
        setCharged(true);
    }

    public void onUpdate() {
        super.onUpdate();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!hasNoGravity()) this.motionY -= 0.03999999910593033D;
        move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
        if (isRiding() && (isEntityInsideOpaqueBlock() || (getRidingEntity() instanceof EntityPlayer && (((EntityPlayer) getRidingEntity()).isElytraFlying() || ((EntityPlayer) getRidingEntity()).isPlayerSleeping())))) {
            playMountSound(false);
            dismountRidingEntity();
        }
        if (!this.world.isRemote) {
            if (this.health < 8 && this.ticksExisted - this.recentlyHit > 80 && this.ticksExisted % 20 == 0)
                this.health++;
            if (--this.age <= 0) hatch();
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (isRiding() && player == getRidingEntity()) {
            playMountSound(false);
            getEntityToRide(player).dismountRidingEntity();
            return true;
        }
        if (!player.isElytraFlying()) {
            startRiding(getEntityToRide(player), true);
            playMountSound(true);
            return true;
        }
        return false;
    }

    private Entity getEntityToRide(Entity entity) {
        List<Entity> passengers = entity.getPassengers();
        return !passengers.isEmpty() ? getEntityToRide(passengers.get(0)) : entity;
    }

    private void playMountSound(boolean mount) {
        playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.7F, (mount ? 0.6F : 0.3F) + this.rand.nextFloat() * 0.1F);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source) || source.getTrueSource() == getRidingEntity()) return false;
        if (!this.world.isRemote && !this.isDead && this.ticksExisted > 0) {
            markVelocityChanged();
            if (source.isExplosion()) {
                this.age -= (int) (amount * 80.0F);
                EntityUtil.sendParticlePacket(this, EnumParticleTypes.HEART, (int) (amount / 2.0F));
                return false;
            }
            this.recentlyHit = this.ticksExisted;
            this.motionY = 0.20000000298023224D;
            this.health -= (int) amount;
            if (this.health <= 0) {
                MutatedExplosion.create(this, isCharged() ? 2.0F : 0.0F, false, true);
                if (this.world.getGameRules().getBoolean("doEntityDrops"))
                    if (isCharged() || this.rand.nextInt(3) == 0) {
                        dropItem(MBItems.CREEPER_SHARD, 1);
                    } else {
                        for (int j = 5 + this.rand.nextInt(6); j > 0; j--)
                            dropItem(Items.GUNPOWDER, 1);
                    }
                setDead();
            }
            return true;
        }
        return false;
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Health", this.health);
        compound.setInteger("Age", this.age);
        if (isCharged()) compound.setBoolean("Charged", true);
        if (this.ownerUUID != null) compound.setUniqueId("OwnerUUID", this.ownerUUID);
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.health = compound.getInteger("Health");
        if (compound.hasKey("Age")) this.age = compound.getInteger("Age");
        setCharged(compound.getBoolean("Charged"));
        if (compound.hasUniqueId("OwnerUUID")) this.ownerUUID = compound.getUniqueId("OwnerUUID");
    }
}

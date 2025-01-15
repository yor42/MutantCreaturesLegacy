package chumbanotz.mutantbeasts.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.config.MBConfig;
import chumbanotz.mutantbeasts.entity.ai.EntityAIAvoidDamage;
import chumbanotz.mutantbeasts.entity.ai.EntityAIHurtByNearestTarget;
import chumbanotz.mutantbeasts.entity.ai.MBEntityAIAttackMelee;
import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.pathfinding.MBGroundPathNavigator;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import chumbanotz.mutantbeasts.util.MutatedExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.UUID;

public class CreeperMinionEntity extends EntityShoulderRiding {
    private static final DataParameter<Byte> CREEPER_MINION_FLAGS = EntityDataManager.createKey(CreeperMinionEntity.class, DataSerializers.BYTE);

    private static final DataParameter<Integer> EXPLODE_STATE = EntityDataManager.createKey(CreeperMinionEntity.class, DataSerializers.VARINT);

    private static final DataParameter<Float> EXPLOSION_RADIUS = EntityDataManager.createKey(CreeperMinionEntity.class, DataSerializers.FLOAT);
    private final int fuseTime = 26;
    private int lastActiveTime;
    private int timeSinceIgnited;

    public CreeperMinionEntity(World worldIn) {
        super(worldIn);
        setDestroyBlocks(true);
        setSize(0.3F, 0.84F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(2, new AISwell());
        this.tasks.addTask(3, new EntityAIAvoidEntity<EntityOcelot>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D) {
            public boolean shouldExecute() {
                return (!CreeperMinionEntity.this.isTamed() && super.shouldExecute());
            }
        });
        this.tasks.addTask(4, new MBEntityAIAttackMelee(this, 1.2D));
        this.tasks.addTask(5, new EntityAIAvoidDamage(this, 1.2D));
        this.tasks.addTask(6, new FollowOwnerGoal());
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAILandOnOwnersShoulder(this) {
            public boolean shouldExecute() {
                return (MBConfig.creeperMinionOnShoulder && CreeperMinionEntity.this.isTamed() && CreeperMinionEntity.this.getOwner() instanceof EntityPlayer && super.shouldExecute());
            }
        });
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(2, new EntityAIHurtByNearestTarget(this));
        this.targetTasks.addTask(3, new EntityAITargetNonTamed(this, EntityPlayer.class, true, null));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CREEPER_MINION_FLAGS, (byte) 0);
        this.dataManager.register(EXPLODE_STATE, -1);
        this.dataManager.register(EXPLOSION_RADIUS, 20.0F);
    }

    @Nullable
    public EntityLivingBase getOwner() {
        Entity entity = null;
        UUID uuid = getOwnerId();
        if (uuid == null) return null;
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uuid);
        if (entityPlayer == null && this.world instanceof WorldServer)
            entity = ((WorldServer) this.world).getEntityFromUuid(uuid);
        return (entity instanceof EntityLivingBase) ? (EntityLivingBase) entity : null;
    }

    public int getExplodeState() {
        return this.dataManager.get(EXPLODE_STATE);
    }

    public void setExplodeState(int state) {
        this.dataManager.set(EXPLODE_STATE, state);
    }

    public boolean getPowered() {
        return ((this.dataManager.get(CREEPER_MINION_FLAGS) & 0x1) != 0);
    }

    public void setPowered(boolean powered) {
        byte b0 = this.dataManager.get(CREEPER_MINION_FLAGS);
        this.dataManager.set(CREEPER_MINION_FLAGS, powered ? (byte) (b0 | 0x1) : (byte) (b0 & 0xFFFFFFFE));
    }

    public boolean hasIgnited() {
        return ((this.dataManager.get(CREEPER_MINION_FLAGS) & 0x4) != 0);
    }

    public void ignite() {
        byte b0 = this.dataManager.get(CREEPER_MINION_FLAGS);
        this.dataManager.set(CREEPER_MINION_FLAGS, (byte) (b0 | 0x4));
    }

    public boolean canExplodeContinuously() {
        return ((this.dataManager.get(CREEPER_MINION_FLAGS) & 0x8) != 0);
    }

    public void setCanExplodeContinuously(boolean continuously) {
        byte b0 = this.dataManager.get(CREEPER_MINION_FLAGS);
        this.dataManager.set(CREEPER_MINION_FLAGS, continuously ? (byte) (b0 | 0x8) : (byte) (b0 & 0xFFFFFFF7));
    }

    public boolean canDestroyBlocks() {
        return ((this.dataManager.get(CREEPER_MINION_FLAGS) & 0x10) != 0);
    }

    public void setDestroyBlocks(boolean destroy) {
        byte b0 = this.dataManager.get(CREEPER_MINION_FLAGS);
        this.dataManager.set(CREEPER_MINION_FLAGS, destroy ? (byte) (b0 | 0x10) : (byte) (b0 & 0xFFFFFFEF));
    }

    public boolean canRideOnShoulder() {
        return ((this.dataManager.get(CREEPER_MINION_FLAGS) & 0x20) != 0);
    }

    public void setCanRideOnShoulder(boolean canRide) {
        byte b0 = this.dataManager.get(CREEPER_MINION_FLAGS);
        this.dataManager.set(CREEPER_MINION_FLAGS, canRide ? (byte) (b0 | 0x20) : (byte) (b0 & 0xFFFFFFDF));
    }

    public float getExplosionRadius() {
        return this.dataManager.get(EXPLOSION_RADIUS) / 10.0F;
    }

    public void setExplosionRadius(float radius) {
        this.dataManager.set(EXPLOSION_RADIUS, radius * 10.0F);
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new MBGroundPathNavigator(this, worldIn);
    }

    public boolean isChild() {
        return false;
    }

    public boolean canSitOnShoulder() {
        return (super.canSitOnShoulder() && canRideOnShoulder() && getAttackTarget() == null && getExplodeState() < 0);
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        super.onStruckByLightning(lightningBolt);
        setPowered(true);
    }

    public EntityLivingBase getAttackTarget() {
        if (!isTamed()) {
            EntityLivingBase owner = getOwner();
            return (owner instanceof MutantCreeperEntity) ? ((MutantCreeperEntity) owner).getAttackTarget() : super.getAttackTarget();
        }
        return super.getAttackTarget();
    }

    public void onUpdate() {
        if (!this.world.isRemote && !isTamed() && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) setDead();
        if (isEntityAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (hasIgnited()) setExplodeState(1);
            int i = getExplodeState();
            if (i > 0 && this.timeSinceIgnited == 0)
                playSound(MBSoundEvents.ENTITY_CREEPER_MINION_PRIMED, 1.0F, getSoundPitch());
            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) this.timeSinceIgnited = 0;
            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = 0;
                if (!this.world.isRemote) {
                    MutatedExplosion.create(this, getExplosionRadius() + (getPowered() ? 2.0F : 0.0F), false, canDestroyBlocks());
                    if (!canExplodeContinuously()) {
                        if (this.world.getGameRules().getBoolean("showDeathMessages") && getOwner() instanceof net.minecraft.entity.player.EntityPlayerMP)
                            getOwner().sendMessage(new TextComponentTranslation("death.attack.explosion", getName()));
                        this.dead = true;
                        setDead();
                        EntityUtil.spawnLingeringCloud(this);
                    }
                }
                setExplodeState(-this.fuseTime);
            }
            if (this.motionX * this.motionY * this.motionZ > 0.800000011920929D && getAttackTarget() != null && getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(0.5D).intersects(getAttackTarget().getEntityBoundingBox()))
                this.timeSinceIgnited = this.fuseTime;
        }
        super.onUpdate();
    }

    public float getCreeperFlashIntensity(float partialTicks) {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * partialTicks) / (this.fuseTime - 2);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.interactWithEntity(player, this, hand)) return true;
        if (isTamed()) {
            if (itemstack.getItem() == MBItems.CREEPER_MINION_TRACKER) {
                player.openGui(MutantBeasts.INSTANCE, 0, this.world, getEntityId(), 0, 0);
                return true;
            }
            if (isOwner(player)) if (itemstack.getItem() == Items.GUNPOWDER) {
                if (getHealth() < getMaxHealth()) {
                    heal(1.0F);
                    EntityUtil.spawnParticleAtEntity(this, EnumParticleTypes.HEART, 1);
                    itemstack.shrink(1);
                    return true;
                }
                if (getMaxHealth() < 20.0F) {
                    EntityUtil.spawnParticleAtEntity(this, EnumParticleTypes.HEART, 1);
                    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((getMaxHealth() + 1.0F));
                    itemstack.shrink(1);
                    return true;
                }
            } else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.TNT)) {
                if (canExplodeContinuously()) {
                    float explosionRadius = getExplosionRadius();
                    if (explosionRadius < 4.0F) {
                        this.forcedAgeTimer += 5;
                        setExplosionRadius(explosionRadius + 0.11F);
                        itemstack.shrink(1);
                        return true;
                    }
                } else {
                    this.forcedAgeTimer += 15;
                    setCanExplodeContinuously(true);
                    itemstack.shrink(1);
                    return true;
                }
            } else {
                if (!this.world.isRemote) {
                    this.aiSit.setSitting(!isSitting());
                    this.navigator.clearPath();
                    setAttackTarget(null);
                }
                return true;
            }
            return false;
        }
        if (itemstack.getItem() == Items.FLINT_AND_STEEL && !hasIgnited()) {
            this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            player.swingArm(hand);
            player.addStat(StatList.getObjectUseStats(itemstack.getItem()));
            if (!this.world.isRemote) {
                ignite();
                itemstack.damageItem(1, player);
            }
            return true;
        }
        if (player.isCreative() && getOwner() == null && itemstack.getItem() == MBItems.CREEPER_MINION_TRACKER) {
            if (!this.world.isRemote) {
                setTamed(true);
                setOwnerId(player.getUniqueID());
                player.setLastAttackedEntity(null);
                player.sendMessage(new TextComponentTranslation(MBItems.CREEPER_MINION_TRACKER.getTranslationKey() + ".tame_success", getName(), player.getName()));
            }
            return true;
        }
        return false;
    }

    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        return EntityUtil.shouldAttackEntity(target, owner, true);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isExplosion()) {
            if (isTamed()) return false;
            if (amount >= 2.0F) amount = 2.0F;
        }
        if (this.aiSit != null) this.aiSit.setSitting(false);
        return super.attackEntityFrom(source, amount);
    }

    public boolean isImmuneToExplosions() {
        return isTamed();
    }

    public boolean canBeLeashedTo(EntityPlayer player) {
        return (!getLeashed() && isTamed());
    }

    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        return (super.canAttackClass(cls) && cls != MutantCreeperEntity.class);
    }

    protected boolean canDespawn() {
        return !isTamed();
    }

    @Nullable
    public Team getTeam() {
        EntityLivingBase owner = getOwner();
        return (owner != null) ? owner.getTeam() : super.getTeam();
    }

    public boolean isOnSameTeam(Entity entityIn) {
        EntityLivingBase owner = getOwner();
        return ((owner != null && (entityIn == owner || owner.isOnSameTeam(entityIn))) || super.isOnSameTeam(entityIn));
    }

    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    public void playLivingSound() {
        if (getAttackTarget() == null && getExplodeState() <= 0) super.playLivingSound();
    }

    protected float getSoundPitch() {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
    }

    protected SoundEvent getAmbientSound() {
        return MBSoundEvents.ENTITY_CREEPER_MINION_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return MBSoundEvents.ENTITY_CREEPER_MINION_HURT;
    }

    protected SoundEvent getDeathSound() {
        return MBSoundEvents.ENTITY_CREEPER_MINION_DEATH;
    }

    public SoundCategory getSoundCategory() {
        return isTamed() ? SoundCategory.NEUTRAL : SoundCategory.HOSTILE;
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Tamed", isTamed());
        compound.setBoolean("ExplodesContinuously", canExplodeContinuously());
        compound.setBoolean("DestroysBlocks", canDestroyBlocks());
        compound.setBoolean("CanRideOnShoulder", canRideOnShoulder());
        compound.setBoolean("Ignited", hasIgnited());
        compound.setFloat("ExplosionRadius", getExplosionRadius());
        if (getPowered()) compound.setBoolean("Powered", true);
        for (String unusedNBT : new String[]{"Age", "ForcedAge", "InLove", "LoveCause"})
            compound.removeTag(unusedNBT);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setTamed(compound.getBoolean("Tamed"));
        setCanExplodeContinuously(compound.getBoolean("ExplodesContinuously"));
        setDestroyBlocks(compound.getBoolean("DestroysBlocks"));
        setCanRideOnShoulder(compound.getBoolean("CanRideOnShoulder"));
        setPowered(compound.getBoolean("Powered"));
        if (compound.hasKey("ExplosionRadius", 99)) setExplosionRadius(compound.getFloat("ExplosionRadius"));
        if (compound.getBoolean("Ignited")) ignite();
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }

    class AISwell extends EntityAIBase {
        public AISwell() {
            setMutexBits(1);
        }

        public boolean shouldExecute() {
            EntityLivingBase attackTarget = CreeperMinionEntity.this.getAttackTarget();
            return (!CreeperMinionEntity.this.isSitting() && (CreeperMinionEntity.this.getExplodeState() > 0 || (attackTarget != null && CreeperMinionEntity.this.getDistanceSq(attackTarget) < 9.0D && CreeperMinionEntity.this.getEntitySenses().canSee(attackTarget))));
        }

        public void startExecuting() {
            CreeperMinionEntity.this.navigator.clearPath();
        }

        public void updateTask() {
            CreeperMinionEntity.this.setExplodeState((CreeperMinionEntity.this.getAttackTarget() == null || CreeperMinionEntity.this.getDistanceSq(CreeperMinionEntity.this.getAttackTarget()) > 36.0D || !CreeperMinionEntity.this.getEntitySenses().canSee(CreeperMinionEntity.this.getAttackTarget())) ? -1 : 1);
        }
    }

    class FollowOwnerGoal extends EntityAIFollowOwner {
        public FollowOwnerGoal() {
            super(CreeperMinionEntity.this, 1.2D, 10.0F, 5.0F);
        }

        public boolean shouldExecute() {
            return (CreeperMinionEntity.this.getAttackTarget() == null && super.shouldExecute());
        }

        public void updateTask() {
            if (!CreeperMinionEntity.this.isTamed()) {
                if (CreeperMinionEntity.this.getOwner() != null)
                    CreeperMinionEntity.this.navigator.tryMoveToEntityLiving(CreeperMinionEntity.this.getOwner(), 1.2D);
            } else {
                super.updateTask();
            }
        }
    }
}

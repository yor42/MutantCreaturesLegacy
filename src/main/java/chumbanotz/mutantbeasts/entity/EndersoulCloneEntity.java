package chumbanotz.mutantbeasts.entity;

import chumbanotz.mutantbeasts.entity.ai.MBEntityAIAttackMelee;
import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import chumbanotz.mutantbeasts.pathfinding.MBGroundPathNavigator;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class EndersoulCloneEntity extends EntityMob {
    private MutantEndermanEntity cloner;

    public EndersoulCloneEntity(World worldIn) {
        super(worldIn);
        this.stepHeight = 1.0F;
        this.experienceValue = this.rand.nextInt(2);
        setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        setSize(0.6F, 2.9F);
    }

    public EndersoulCloneEntity(MutantEndermanEntity cloner, double x, double y, double z) {
        this(cloner.world);
        this.cloner = cloner;
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(cloner.getMaxHealth());
        setHealth(cloner.getHealth());
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(cloner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue());
        if (!EntityUtil.teleportTo(this, x, y, z)) copyLocationAndAnglesFrom(cloner);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new MBEntityAIAttackMelee(this, 1.2D));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    public float getEyeHeight() {
        return 2.55F;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new MBGroundPathNavigator(this, worldIn);
    }

    public int getMaxFallHeight() {
        return 3;
    }

    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        setFlag(2, (entitylivingbaseIn != null));
    }

    public boolean isAggressive() {
        return getFlag(2);
    }

    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == 0) EntityUtil.spawnEndersoulParticles(this, 256, 1.8F);
    }

    public void onLivingUpdate() {
        this.isJumping = false;
        super.onLivingUpdate();
        if (this.cloner != null && (!this.cloner.isEntityAlive() || this.cloner.isAIDisabled() || this.cloner.world != this.world))
            setDead();
    }

    protected void updateAITasks() {
        EntityLivingBase entityLivingBase = getAttackTarget();
        if (this.rand.nextInt(10) == 0 && entityLivingBase != null && (isInWater() || this.fallDistance > 3.0F || isRidingSameEntity(entityLivingBase) || getDistanceSq(entityLivingBase) > 1024.0D || !hasPath()))
            teleportToEntity(entityLivingBase);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (!this.world.isRemote && this.rand.nextInt(3) != 0) teleportToEntity(entityIn);
        if (flag) heal(2.0F);
        swingArm(EnumHand.MAIN_HAND);
        return flag;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source) || source.getTrueSource() instanceof net.minecraft.entity.boss.EntityDragon || source.getTrueSource() instanceof MutantEndermanEntity)
            return false;
        boolean flag = (!this.world.isRemote && !this.isDead && this.ticksExisted > 0);
        if (flag) {
            if (source.getTrueSource() instanceof EntityPlayer) {
                this.attackingPlayer = (EntityPlayer) source.getTrueSource();
                this.recentlyHit = 100;
            }
            if (this.world.getGameRules().getBoolean("doMobLoot")) {
                EntityUtil.dropExperience(this, this.recentlyHit, this::getExperiencePoints, this.attackingPlayer);
                dropLoot((this.recentlyHit > 0), ForgeHooks.getLootingLevel(this, source.getTrueSource(), source), source);
            }
            setDead();
        }
        return flag;
    }

    private boolean teleportToEntity(Entity entity) {
        double x = entity.posX + ((this.rand.nextFloat() - 0.5F) * 24.0F);
        double y = entity.posY + this.rand.nextInt(5) + 4.0D;
        double z = entity.posZ + ((this.rand.nextFloat() - 0.5F) * 24.0F);
        boolean flag = EntityUtil.teleportTo(this, x, y, z);
        if (flag) {
            dismountRidingEntity();
            if (!isSilent()) {
                this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, MBSoundEvents.ENTITY_ENDERSOUL_CLONE_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
                playSound(MBSoundEvents.ENTITY_ENDERSOUL_CLONE_TELEPORT, 1.0F, 1.0F);
            }
        }
        return flag;
    }

    protected void collideWithNearbyEntities() {
    }

    public boolean canBeHitWithPotion() {
        return false;
    }

    public boolean isNoDespawnRequired() {
        return (super.isNoDespawnRequired() || this.cloner != null);
    }

    public void setDead() {
        super.setDead();
        this.world.setEntityState(this, (byte) 0);
        playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return MBSoundEvents.ENTITY_ENDERSOUL_CLONE_DEATH;
    }

    public boolean isEntityEqual(Entity entityIn) {
        return (super.isEntityEqual(entityIn) || entityIn == this.cloner);
    }

    public Team getTeam() {
        return (this.cloner != null) ? this.cloner.getTeam() : super.getTeam();
    }

    public boolean isOnSameTeam(Entity entityIn) {
        return ((this.cloner != null && (this.cloner == entityIn || this.cloner.isOnSameTeam(entityIn))) || super.isOnSameTeam(entityIn));
    }

    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return (this.cloner == null && super.writeToNBTOptional(compound));
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }
}

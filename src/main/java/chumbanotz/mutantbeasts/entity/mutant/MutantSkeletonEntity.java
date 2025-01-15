package chumbanotz.mutantbeasts.entity.mutant;

import chumbanotz.mutantbeasts.client.animationapi.IAnimatedEntity;
import chumbanotz.mutantbeasts.entity.BodyPartEntity;
import chumbanotz.mutantbeasts.entity.ai.EntityAIAvoidDamage;
import chumbanotz.mutantbeasts.entity.ai.EntityAIHurtByNearestTarget;
import chumbanotz.mutantbeasts.entity.ai.MBEntityAIAttackMelee;
import chumbanotz.mutantbeasts.entity.projectile.MutantArrowEntity;
import chumbanotz.mutantbeasts.pathfinding.MBGroundPathNavigator;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MutantSkeletonEntity extends EntityMob implements IAnimatedEntity {
    public static final byte MELEE_ATTACK = 1;

    public static final byte SHOOT_ATTACK = 2;

    public static final byte MULTI_SHOT_ATTACK = 3;

    public static final byte CONSTRICT_RIBS_ATTACK = 4;

    private int attackID;

    private int attackTick;

    public MutantSkeletonEntity(World worldIn) {
        super(worldIn);
        this.stepHeight = 1.0F;
        this.experienceValue = 30;
        setSize(1.2F, 3.6F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new MeleeGoal());
        this.tasks.addTask(1, new ShootGoal());
        this.tasks.addTask(1, new MultiShotGoal());
        this.tasks.addTask(1, new ConstrictRibsAttackGoal());
        this.tasks.addTask(2, (new MBEntityAIAttackMelee(this, 1.1D)).setMaxAttackTick(10));
        this.tasks.addTask(3, new EntityAIAvoidDamage(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIHurtByNearestTarget(this));
        this.targetTasks.addTask(1, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
        getEntityAttribute(SWIM_SPEED).setBaseValue(5.0D);
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new MBGroundPathNavigator(this, worldIn);
    }

    public float getEyeHeight() {
        return 3.25F;
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void handleStatusUpdate(byte id) {
        if (id <= 0) {
            this.attackID = Math.abs(id);
            this.attackTick = 0;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.attackID != 0) this.attackTick++;
        if (!this.world.isDaytime() && this.ticksExisted % 100 == 0 && getHealth() < getMaxHealth()) heal(2.0F);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (!this.world.isRemote && this.attackID == 0) if (this.rand.nextInt(4) != 0) {
            this.attackID = 1;
        } else {
            this.attackID = 4;
        }
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (!(source.getTrueSource() instanceof MutantSkeletonEntity) && super.attackEntityFrom(source, amount));
    }

    protected boolean canBeRidden(Entity entityIn) {
        return (super.canBeRidden(entityIn) && entityIn instanceof EntityLivingBase);
    }

    public boolean isPushedByWater() {
        return false;
    }

    public int getAnimationID() {
        return this.attackID;
    }

    public void setAnimationID(int id) {
        this.attackID = id;
    }

    public int getAnimationTick() {
        return this.attackTick;
    }

    public void setAnimationTick(int tick) {
        this.attackTick = tick;
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote) {
            for (EntityLivingBase entityLivingBase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(3.0D, 2.0D, 3.0D)))
                entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this), 7.0F);
            for (int i = 0; i < 18; i++) {
                int j = i;
                if (i >= 3) j = i + 1;
                if (j >= 4) j++;
                if (j >= 5) j++;
                if (j >= 6) j++;
                if (j >= 9) j++;
                if (j >= 10) j++;
                if (j >= 11) j++;
                if (j >= 12) j++;
                if (j >= 15) j++;
                if (j >= 16) j++;
                if (j >= 17) j++;
                if (j >= 18) j++;
                if (j >= 20) j++;
                BodyPartEntity part = new BodyPartEntity(this.world, this, j);
                part.motionX += (this.rand.nextFloat() * 0.8F * 2.0F - 0.8F);
                part.motionY += (this.rand.nextFloat() * 0.25F + 0.1F);
                part.motionZ += (this.rand.nextFloat() * 0.8F * 2.0F - 0.8F);
                this.world.spawnEntity(part);
            }
        }
        this.deathTime = 19;
    }

    protected void handleJumpWater() {
        this.motionY += 0.03999999910593033D;
    }

    protected void handleJumpLava() {
        handleJumpWater();
    }

    protected SoundEvent getAmbientSound() {
        return MBSoundEvents.ENTITY_MUTANT_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return MBSoundEvents.ENTITY_MUTANT_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return MBSoundEvents.ENTITY_MUTANT_SKELETON_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(MBSoundEvents.ENTITY_MUTANT_SKELETON_STEP, 0.15F, 1.0F);
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }

    private void setAttackID(int id) {
        this.attackID = id;
        this.attackTick = 0;
        this.world.setEntityState(this, (byte) -id);
    }

    class MeleeGoal extends EntityAIBase {
        public MeleeGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            return (MutantSkeletonEntity.this.attackID == 1);
        }

        public boolean shouldContinueExecuting() {
            return (MutantSkeletonEntity.this.attackTick < 14);
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(1);
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            if (MutantSkeletonEntity.this.getAttackTarget() != null && MutantSkeletonEntity.this.getAttackTarget().isEntityAlive())
                MutantSkeletonEntity.this.getLookHelper().setLookPositionWithEntity(MutantSkeletonEntity.this.getAttackTarget(), 30.0F, 30.0F);
            if (MutantSkeletonEntity.this.attackTick == 3) {
                DamageSource damageSource = DamageSource.causeMobDamage(MutantSkeletonEntity.this);
                for (Entity entity : MutantSkeletonEntity.this.world.getEntitiesWithinAABBExcludingEntity(MutantSkeletonEntity.this, MutantSkeletonEntity.this.getEntityBoundingBox().grow(4.0D))) {
                    if (!entity.canBeCollidedWith() || entity instanceof MutantSkeletonEntity) continue;
                    double dist = MutantSkeletonEntity.this.getDistance(entity);
                    double x = MutantSkeletonEntity.this.posX - entity.posX;
                    double z = MutantSkeletonEntity.this.posZ - entity.posZ;
                    if (dist <= (2.3F + MutantSkeletonEntity.this.rand.nextFloat() * 0.3F) && EntityUtil.getHeadAngle(MutantSkeletonEntity.this, x, z) < 60.0F) {
                        float power = 1.8F + MutantSkeletonEntity.this.rand.nextInt(5) * 0.15F;
                        if (!entity.attackEntityFrom(damageSource, (float) MutantSkeletonEntity.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()))
                            EntityUtil.knockBackBlockingPlayer(entity);
                        entity.motionX = -x / dist * power;
                        entity.motionY = Math.max(0.2800000011920929D, entity.motionY);
                        entity.motionZ = -z / dist * power;
                    }
                }
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.0F, 1.0F / (MutantSkeletonEntity.this.rand.nextFloat() * 0.4F + 1.2F));
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
        }
    }

    class ConstrictRibsAttackGoal extends EntityAIBase {
        private EntityLivingBase attackTarget;

        public ConstrictRibsAttackGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantSkeletonEntity.this.getAttackTarget();
            return (this.attackTarget != null && MutantSkeletonEntity.this.attackID == 4);
        }

        public boolean shouldContinueExecuting() {
            return (MutantSkeletonEntity.this.attackTick < 20);
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(4);
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            if (MutantSkeletonEntity.this.attackTick == 5) this.attackTarget.dismountRidingEntity();
            if (MutantSkeletonEntity.this.attackTick == 6) {
                float damage = (float) MutantSkeletonEntity.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(MutantSkeletonEntity.this), (damage > 0.0F) ? (damage + 5.0F) : 0.0F);
                this.attackTarget.motionX = (1.0F + MutantSkeletonEntity.this.getRNG().nextFloat() * 0.4F) * (MutantSkeletonEntity.this.getRNG().nextBoolean() ? 1 : -1);
                this.attackTarget.motionY = 0.4F + MutantSkeletonEntity.this.getRNG().nextFloat() * 0.8F;
                this.attackTarget.motionZ = (1.0F + MutantSkeletonEntity.this.getRNG().nextFloat() * 0.4F) * (MutantSkeletonEntity.this.getRNG().nextBoolean() ? 1 : -1);
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 0.8F + MutantSkeletonEntity.this.rand.nextFloat() * 0.4F);
                EntityUtil.sendPlayerVelocityPacket(this.attackTarget);
                EntityUtil.disableShield(this.attackTarget, 100);
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
            this.attackTarget = null;
        }
    }

    class ShootGoal extends EntityAIBase {
        private EntityLivingBase attackTarget;

        public ShootGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantSkeletonEntity.this.getAttackTarget();
            return (this.attackTarget != null && MutantSkeletonEntity.this.attackID == 0 && MutantSkeletonEntity.this.rand.nextInt(12) == 0 && MutantSkeletonEntity.this.getDistanceSq(this.attackTarget) > 4.0D && MutantSkeletonEntity.this.getEntitySenses().canSee(this.attackTarget));
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(2);
        }

        public boolean shouldContinueExecuting() {
            return (MutantSkeletonEntity.this.attackTick < 32);
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            MutantSkeletonEntity.this.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
            if (MutantSkeletonEntity.this.attackTick == 26 && this.attackTarget.isEntityAlive()) {
                MutantArrowEntity arrowEntity = new MutantArrowEntity(MutantSkeletonEntity.this.world, MutantSkeletonEntity.this, this.attackTarget);
                if (MutantSkeletonEntity.this.hurtTime > 0) {
                    arrowEntity.randomize(MutantSkeletonEntity.this.hurtTime / 2.0F);
                } else if (!MutantSkeletonEntity.this.getEntitySenses().canSee(this.attackTarget)) {
                    arrowEntity.randomize((float) MutantSkeletonEntity.this.getDistanceSq(this.attackTarget));
                }
                if (MutantSkeletonEntity.this.rand.nextInt(4) == 0)
                    arrowEntity.setPotionEffect(new PotionEffect(MobEffects.POISON, 80 + MutantSkeletonEntity.this.rand.nextInt(60), 0));
                if (MutantSkeletonEntity.this.rand.nextInt(4) == 0)
                    arrowEntity.setPotionEffect(new PotionEffect(MobEffects.HUNGER, 120 + MutantSkeletonEntity.this.rand.nextInt(60), 1));
                if (MutantSkeletonEntity.this.rand.nextInt(4) == 0)
                    arrowEntity.setPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 120 + MutantSkeletonEntity.this.rand.nextInt(60), 1));
                MutantSkeletonEntity.this.world.spawnEntity(arrowEntity);
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (MutantSkeletonEntity.this.rand.nextFloat() * 0.4F + 1.2F) + 0.25F);
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
            this.attackTarget = null;
        }
    }

    class MultiShotGoal extends EntityAIBase {
        private final List<MutantArrowEntity> shots = new ArrayList<>();
        private EntityLivingBase attackTarget;

        public MultiShotGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantSkeletonEntity.this.getAttackTarget();
            return (this.attackTarget != null && MutantSkeletonEntity.this.attackID == 0 && ((MutantSkeletonEntity.this.onGround && MutantSkeletonEntity.this.rand.nextInt(26) == 0 && MutantSkeletonEntity.this.getEntitySenses().canSee(this.attackTarget)) || MutantSkeletonEntity.this.getRidingEntity() == this.attackTarget));
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(3);
        }

        public boolean shouldContinueExecuting() {
            return (MutantSkeletonEntity.this.attackTick < 30);
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            MutantSkeletonEntity.this.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
            if (MutantSkeletonEntity.this.attackTick == 10) {
                MutantSkeletonEntity.this.dismountRidingEntity();
                double x = this.attackTarget.posX - MutantSkeletonEntity.this.posX;
                double z = this.attackTarget.posZ - MutantSkeletonEntity.this.posZ;
                float scale = 0.06F + MutantSkeletonEntity.this.rand.nextFloat() * 0.03F;
                if (MutantSkeletonEntity.this.getDistanceSq(this.attackTarget) < 16.0D) {
                    x *= -1.0D;
                    z *= -1.0D;
                    scale *= 5.0F;
                }
                MutantSkeletonEntity.this.isInWeb = false;
                MutantSkeletonEntity.this.motionX = x * scale;
                MutantSkeletonEntity.this.motionY = 1.100000023841858D;
                MutantSkeletonEntity.this.motionZ = z * scale;
            }
            if (MutantSkeletonEntity.this.attackTick >= 24 && MutantSkeletonEntity.this.attackTick < 28) {
                if (!this.shots.isEmpty()) {
                    for (MutantArrowEntity arrowEntity : this.shots) {
                        MutantSkeletonEntity.this.world.spawnEntity(arrowEntity);
                    }
                    this.shots.clear();
                }
                for (int i = 0; i < 6; i++) {
                    MutantArrowEntity shot = new MutantArrowEntity(MutantSkeletonEntity.this.world, MutantSkeletonEntity.this, this.attackTarget);
                    shot.setSpeed(1.2F - MutantSkeletonEntity.this.rand.nextFloat() * 0.1F);
                    shot.setClones(2);
                    shot.randomize(3.0F);
                    shot.setDamage(5 + MutantSkeletonEntity.this.rand.nextInt(5));
                    this.shots.add(shot);
                }
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (MutantSkeletonEntity.this.rand.nextFloat() * 0.4F + 1.2F) + 0.25F);
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
            this.shots.clear();
            this.attackTarget = null;
        }
    }
}

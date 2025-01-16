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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
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

public class MutantSkeletonEntity
extends EntityMob
implements IAnimatedEntity {
    public static final byte MELEE_ATTACK = 1;
    public static final byte SHOOT_ATTACK = 2;
    public static final byte MULTI_SHOT_ATTACK = 3;
    public static final byte CONSTRICT_RIBS_ATTACK = 4;
    private int attackID;
    private int attackTick;

    public MutantSkeletonEntity(World worldIn) {
        super(worldIn);
        this.stepHeight = 1.0f;
        this.experienceValue = 30;
        this.setSize(1.2f, 3.6f);
    }

    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.canSeeSky(this.getPosition()) && EntityUtil.getRandomSpawnChance(this.rand);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new MeleeGoal());
        this.tasks.addTask(1, new ShootGoal());
        this.tasks.addTask(1, new MultiShotGoal());
        this.tasks.addTask(1, new ConstrictRibsAttackGoal());
        this.tasks.addTask(2, new MBEntityAIAttackMelee(this, 1.1).setMaxAttackTick(10));
        this.tasks.addTask(3, new EntityAIAvoidDamage(this, 1.0));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIHurtByNearestTarget(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0);
        this.getEntityAttribute(SWIM_SPEED).setBaseValue(5.0);
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new MBGroundPathNavigator(this, worldIn);
    }

    public float getEyeHeight() {
        return 3.25f;
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
        if (this.attackID != 0) {
            ++this.attackTick;
        }
        if (!this.world.isDaytime() && this.ticksExisted % 100 == 0 && this.getHealth() < this.getMaxHealth()) {
            this.heal(2.0f);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (!this.world.isRemote && this.attackID == 0) {
            this.attackID = this.rand.nextInt(4) != 0 ? 1 : 4;
        }
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !(source.getTrueSource() instanceof MutantSkeletonEntity) && super.attackEntityFrom(source, amount);
    }

    protected boolean canBeRidden(Entity entityIn) {
        return super.canBeRidden(entityIn) && entityIn instanceof EntityLivingBase;
    }

    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public int getAnimationID() {
        return this.attackID;
    }

    @Override
    public void setAnimationID(int id) {
        this.attackID = id;
    }

    @Override
    public int getAnimationTick() {
        return this.attackTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.attackTick = tick;
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote) {
            for (EntityLivingBase entityLivingBase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(3.0, 2.0, 3.0))) {
                entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 7.0f);
            }
            for (int i = 0; i < 18; ++i) {
                int j = i;
                if (i >= 3) {
                    j = i + 1;
                }
                if (j >= 4) {
                    ++j;
                }
                if (j >= 5) {
                    ++j;
                }
                if (j >= 6) {
                    ++j;
                }
                if (j >= 9) {
                    ++j;
                }
                if (j >= 10) {
                    ++j;
                }
                if (j >= 11) {
                    ++j;
                }
                if (j >= 12) {
                    ++j;
                }
                if (j >= 15) {
                    ++j;
                }
                if (j >= 16) {
                    ++j;
                }
                if (j >= 17) {
                    ++j;
                }
                if (j >= 18) {
                    ++j;
                }
                if (j >= 20) {
                    ++j;
                }
                BodyPartEntity part = new BodyPartEntity(this.world, this, j);
                part.motionX += (double)(this.rand.nextFloat() * 0.8f * 2.0f - 0.8f);
                part.motionY += (double)(this.rand.nextFloat() * 0.25f + 0.1f);
                part.motionZ += (double)(this.rand.nextFloat() * 0.8f * 2.0f - 0.8f);
                this.world.spawnEntity(part);
            }
        }
        this.deathTime = 19;
    }

    protected void handleJumpWater() {
        this.motionY += (double)0.04f;
    }

    protected void handleJumpLava() {
        this.handleJumpWater();
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
        this.playSound(MBSoundEvents.ENTITY_MUTANT_SKELETON_STEP, 0.15f, 1.0f);
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }

    private void setAttackID(int id) {
        this.attackID = id;
        this.attackTick = 0;
        this.world.setEntityState(this, (byte)(-id));
    }

    class MultiShotGoal
    extends EntityAIBase {
        private EntityLivingBase attackTarget;
        private final List<MutantArrowEntity> shots = new ArrayList<MutantArrowEntity>();

        public MultiShotGoal() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantSkeletonEntity.this.getAttackTarget();
            return this.attackTarget != null && MutantSkeletonEntity.this.attackID == 0 && (MutantSkeletonEntity.this.onGround && MutantSkeletonEntity.this.rand.nextInt(26) == 0 && MutantSkeletonEntity.this.getEntitySenses().canSee(this.attackTarget) || MutantSkeletonEntity.this.getRidingEntity() == this.attackTarget);
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(3);
        }

        public boolean shouldContinueExecuting() {
            return MutantSkeletonEntity.this.attackTick < 30;
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            MutantSkeletonEntity.this.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
            if (MutantSkeletonEntity.this.attackTick == 10) {
                MutantSkeletonEntity.this.dismountRidingEntity();
                double x = this.attackTarget.posX - MutantSkeletonEntity.this.posX;
                double z = this.attackTarget.posZ - MutantSkeletonEntity.this.posZ;
                float scale = 0.06f + MutantSkeletonEntity.this.rand.nextFloat() * 0.03f;
                if (MutantSkeletonEntity.this.getDistanceSq(this.attackTarget) < 16.0) {
                    x *= -1.0;
                    z *= -1.0;
                    scale *= 5.0f;
                }
                MutantSkeletonEntity.this.isInWeb = false;
                MutantSkeletonEntity.this.motionX = x * (double)scale;
                MutantSkeletonEntity.this.motionY = 1.1f;
                MutantSkeletonEntity.this.motionZ = z * (double)scale;
            }
            if (MutantSkeletonEntity.this.attackTick >= 24 && MutantSkeletonEntity.this.attackTick < 28) {
                if (!this.shots.isEmpty()) {
                    Iterator<MutantArrowEntity> iterator = this.shots.iterator();
                    while (iterator.hasNext()) {
                        MutantArrowEntity arrowEntity;
                        MutantArrowEntity shot = arrowEntity = iterator.next();
                        MutantSkeletonEntity.this.world.spawnEntity(arrowEntity);
                    }
                    this.shots.clear();
                }
                for (int i = 0; i < 6; ++i) {
                    MutantArrowEntity shot = new MutantArrowEntity(MutantSkeletonEntity.this.world, MutantSkeletonEntity.this, this.attackTarget);
                    shot.setSpeed(1.2f - MutantSkeletonEntity.this.rand.nextFloat() * 0.1f);
                    shot.setClones(2);
                    shot.randomize(3.0f);
                    shot.setDamage(5 + MutantSkeletonEntity.this.rand.nextInt(5));
                    this.shots.add(shot);
                }
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0f, 1.0f / (MutantSkeletonEntity.this.rand.nextFloat() * 0.4f + 1.2f) + 0.25f);
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
            this.shots.clear();
            this.attackTarget = null;
        }
    }

    class ShootGoal
    extends EntityAIBase {
        private EntityLivingBase attackTarget;

        public ShootGoal() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantSkeletonEntity.this.getAttackTarget();
            return this.attackTarget != null && MutantSkeletonEntity.this.attackID == 0 && MutantSkeletonEntity.this.rand.nextInt(12) == 0 && MutantSkeletonEntity.this.getDistanceSq(this.attackTarget) > 4.0 && MutantSkeletonEntity.this.getEntitySenses().canSee(this.attackTarget);
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(2);
        }

        public boolean shouldContinueExecuting() {
            return MutantSkeletonEntity.this.attackTick < 32;
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            MutantSkeletonEntity.this.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
            if (MutantSkeletonEntity.this.attackTick == 26 && this.attackTarget.isEntityAlive()) {
                MutantArrowEntity arrowEntity = new MutantArrowEntity(MutantSkeletonEntity.this.world, MutantSkeletonEntity.this, this.attackTarget);
                if (MutantSkeletonEntity.this.hurtTime > 0) {
                    arrowEntity.randomize((float)MutantSkeletonEntity.this.hurtTime / 2.0f);
                } else if (!MutantSkeletonEntity.this.getEntitySenses().canSee(this.attackTarget)) {
                    arrowEntity.randomize((float)MutantSkeletonEntity.this.getDistanceSq(this.attackTarget));
                }
                if (MutantSkeletonEntity.this.rand.nextInt(4) == 0) {
                    arrowEntity.setPotionEffect(new PotionEffect(MobEffects.POISON, 80 + MutantSkeletonEntity.this.rand.nextInt(60), 0));
                }
                if (MutantSkeletonEntity.this.rand.nextInt(4) == 0) {
                    arrowEntity.setPotionEffect(new PotionEffect(MobEffects.HUNGER, 120 + MutantSkeletonEntity.this.rand.nextInt(60), 1));
                }
                if (MutantSkeletonEntity.this.rand.nextInt(4) == 0) {
                    arrowEntity.setPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 120 + MutantSkeletonEntity.this.rand.nextInt(60), 1));
                }
                MutantSkeletonEntity.this.world.spawnEntity(arrowEntity);
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0f, 1.0f / (MutantSkeletonEntity.this.rand.nextFloat() * 0.4f + 1.2f) + 0.25f);
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
            this.attackTarget = null;
        }
    }

    class ConstrictRibsAttackGoal
    extends EntityAIBase {
        private EntityLivingBase attackTarget;

        public ConstrictRibsAttackGoal() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantSkeletonEntity.this.getAttackTarget();
            return this.attackTarget != null && MutantSkeletonEntity.this.attackID == 4;
        }

        public boolean shouldContinueExecuting() {
            return MutantSkeletonEntity.this.attackTick < 20;
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(4);
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            if (MutantSkeletonEntity.this.attackTick == 5) {
                this.attackTarget.dismountRidingEntity();
            }
            if (MutantSkeletonEntity.this.attackTick == 6) {
                float damage = (float)MutantSkeletonEntity.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)MutantSkeletonEntity.this), damage > 0.0f ? damage + 5.0f : 0.0f);
                this.attackTarget.motionX = (1.0f + MutantSkeletonEntity.this.getRNG().nextFloat() * 0.4f) * (float)(MutantSkeletonEntity.this.getRNG().nextBoolean() ? 1 : -1);
                this.attackTarget.motionY = 0.4f + MutantSkeletonEntity.this.getRNG().nextFloat() * 0.8f;
                this.attackTarget.motionZ = (1.0f + MutantSkeletonEntity.this.getRNG().nextFloat() * 0.4f) * (float)(MutantSkeletonEntity.this.getRNG().nextBoolean() ? 1 : -1);
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 0.8f + MutantSkeletonEntity.this.rand.nextFloat() * 0.4f);
                EntityUtil.sendPlayerVelocityPacket(this.attackTarget);
                EntityUtil.disableShield(this.attackTarget, 100);
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
            this.attackTarget = null;
        }
    }

    class MeleeGoal
    extends EntityAIBase {
        public MeleeGoal() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            return MutantSkeletonEntity.this.attackID == 1;
        }

        public boolean shouldContinueExecuting() {
            return MutantSkeletonEntity.this.attackTick < 14;
        }

        public void startExecuting() {
            MutantSkeletonEntity.this.setAttackID(1);
        }

        public void updateTask() {
            MutantSkeletonEntity.this.getNavigator().clearPath();
            if (MutantSkeletonEntity.this.getAttackTarget() != null && MutantSkeletonEntity.this.getAttackTarget().isEntityAlive()) {
                MutantSkeletonEntity.this.getLookHelper().setLookPositionWithEntity(MutantSkeletonEntity.this.getAttackTarget(), 30.0f, 30.0f);
            }
            if (MutantSkeletonEntity.this.attackTick == 3) {
                DamageSource damageSource = DamageSource.causeMobDamage((EntityLivingBase)MutantSkeletonEntity.this);
                for (Entity entity : MutantSkeletonEntity.this.world.getEntitiesWithinAABBExcludingEntity(MutantSkeletonEntity.this, MutantSkeletonEntity.this.getEntityBoundingBox().grow(4.0))) {
                    if (!entity.canBeCollidedWith() || entity instanceof MutantSkeletonEntity) continue;
                    double dist = MutantSkeletonEntity.this.getDistance(entity);
                    double x = MutantSkeletonEntity.this.posX - entity.posX;
                    double z = MutantSkeletonEntity.this.posZ - entity.posZ;
                    if (!(dist <= (double)(2.3f + MutantSkeletonEntity.this.rand.nextFloat() * 0.3f)) || !(EntityUtil.getHeadAngle(MutantSkeletonEntity.this, x, z) < 60.0f)) continue;
                    float power = 1.8f + (float)MutantSkeletonEntity.this.rand.nextInt(5) * 0.15f;
                    if (!entity.attackEntityFrom(damageSource, (float)MutantSkeletonEntity.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue())) {
                        EntityUtil.knockBackBlockingPlayer(entity);
                    }
                    entity.motionX = -x / dist * (double)power;
                    entity.motionY = Math.max((double)0.28f, entity.motionY);
                    entity.motionZ = -z / dist * (double)power;
                }
                MutantSkeletonEntity.this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.0f, 1.0f / (MutantSkeletonEntity.this.rand.nextFloat() * 0.4f + 1.2f));
            }
        }

        public void resetTask() {
            MutantSkeletonEntity.this.setAttackID(0);
        }
    }
}

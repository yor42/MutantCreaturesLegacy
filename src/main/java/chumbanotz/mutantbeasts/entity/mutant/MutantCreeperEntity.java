package chumbanotz.mutantbeasts.entity.mutant;

import chumbanotz.mutantbeasts.entity.CreeperMinionEggEntity;
import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import chumbanotz.mutantbeasts.entity.ai.EntityAIAvoidDamage;
import chumbanotz.mutantbeasts.entity.ai.EntityAIHurtByNearestTarget;
import chumbanotz.mutantbeasts.entity.ai.MBEntityAIAttackMelee;
import chumbanotz.mutantbeasts.pathfinding.MBGroundPathNavigator;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import chumbanotz.mutantbeasts.util.MutatedExplosion;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class MutantCreeperEntity extends EntityCreeper implements IEntityAdditionalSpawnData {
    public static final int MAX_CHARGE_TIME = 100;
    public static final int MAX_DEATH_TIME = 100;
    private static final DataParameter<Byte> STATUS = EntityDataManager.createKey(MutantCreeperEntity.class, DataSerializers.BYTE);
    public int deathTime;
    private int chargeTime;
    private int chargeHits = 3 + this.rand.nextInt(3);
    private int lastFlashTick;
    private int flashTick;
    private boolean summonLightning;
    private DamageSource deathCause;

    public MutantCreeperEntity(World worldIn) {
        super(worldIn);
        this.stepHeight = 1.0F;
        this.experienceValue = 30;
        setSize(1.98F, 2.8F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new JumpAttackGoal());
        this.tasks.addTask(1, new SpawnMinionsGoal());
        this.tasks.addTask(1, new ChargeAttackGoal());
        this.tasks.addTask(2, new MBEntityAIAttackMelee(this, 1.3D));
        this.tasks.addTask(3, new EntityAIAvoidDamage(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIHurtByNearestTarget(this));
        this.targetTasks.addTask(1, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(100));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SWIM_SPEED).setBaseValue(4.5D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(STATUS, (byte) 0);
    }

    public boolean getPowered() {
        return ((this.dataManager.get(STATUS) & 0x1) != 0);
    }

    private void setPowered(boolean powered) {
        this.isImmuneToFire = powered;
        this.experienceValue = powered ? 40 : 30;
        byte b0 = this.dataManager.get(STATUS);
        this.dataManager.set(STATUS, powered ? (byte) (b0 | 0x1) : (byte) (b0 & 0xFFFFFFFE));
    }

    public boolean isJumpAttacking() {
        return ((this.dataManager.get(STATUS) & 0x2) != 0);
    }

    private void setJumpAttacking(boolean jumping) {
        byte b0 = this.dataManager.get(STATUS);
        this.dataManager.set(STATUS, jumping ? (byte) (b0 | 0x2) : (byte) (b0 & 0xFFFFFFFD));
    }

    public boolean isCharging() {
        return ((this.dataManager.get(STATUS) & 0x4) != 0);
    }

    private void setCharging(boolean flag) {
        byte b0 = this.dataManager.get(STATUS);
        this.dataManager.set(STATUS, flag ? (byte) (b0 | 0x4) : (byte) (b0 & 0xFFFFFFFB));
    }

    public float getEyeHeight() {
        return 2.6F;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new MBGroundPathNavigator(this, worldIn);
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        super.updateFallState(y, onGroundIn, state, pos);
        if (!this.world.isRemote && isJumpAttacking() && (onGroundIn || state.getMaterial().isLiquid() || state.getMaterial() == Material.WEB)) {
            MutatedExplosion.create(this, getPowered() ? 6.0F : 4.0F, false, true);
            setJumpAttacking(false);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        double x = entityIn.posX - this.posX;
        double y = entityIn.posY - this.posY;
        double z = entityIn.posZ - this.posZ;
        double d = Math.sqrt(x * x + y * y + z * z);
        entityIn.motionX = x / d * 0.5D;
        entityIn.motionY = y / d * 0.05000000074505806D + 0.15000000596046448D;
        entityIn.motionZ = z / d * 0.5D;
        entityIn.velocityChanged = true;
        if (flag) applyEnchantments(this, entityIn);
        swingArm(EnumHand.MAIN_HAND);
        return flag;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) return false;
        if (source.isExplosion()) {
            float healAmount = amount / 2.0F;
            if (isEntityAlive() && getHealth() < getMaxHealth() && !(source.getTrueSource() instanceof MutantCreeperEntity)) {
                heal(healAmount);
                EntityUtil.sendParticlePacket(this, EnumParticleTypes.HEART, (int) (healAmount / 2.0F));
            }
            return false;
        }
        boolean flag = (!(source.getTrueSource() instanceof EntityCreeper) && super.attackEntityFrom(source, amount));
        if (isCharging() && flag && amount > 0.0F) this.chargeHits--;
        return flag;
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        setPowered(true);
    }

    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        return false;
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public void handleStatusUpdate(byte id) {
        if (id == 6) {
            EntityUtil.spawnParticleAtEntity(this, EnumParticleTypes.HEART, 15);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public void onUpdate() {
        super.onUpdate();
        this.lastFlashTick = this.flashTick;
        if (isJumpAttacking()) {
            if (this.flashTick == 0) playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 2.0F, getSoundPitch() * 0.5F);
            this.flashTick++;
        } else if (this.flashTick > 0) {
            this.flashTick = 0;
        }
    }

    protected boolean canBeRidden(Entity entityIn) {
        return (super.canBeRidden(entityIn) && entityIn instanceof EntityLivingBase);
    }

    public boolean isPushedByWater() {
        return false;
    }

    public float getCreeperFlashIntensity(float partialTick) {
        if (this.deathTime > 0) return this.deathTime / 100.0F * 255.0F;
        if (isCharging()) return ((this.ticksExisted % 20 < 10) ? 0.6F : 0.0F) * 255.0F;
        return (this.lastFlashTick + (this.flashTick - this.lastFlashTick) * partialTick) / 28.0F;
    }

    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote) {
            this.deathCause = cause;
            setCharging(false);
            playSound(MBSoundEvents.ENTITY_MUTANT_CREEPER_DEATH, 2.0F, 1.0F);
            this.world.setEntityState(this, (byte) 3);
            if (this.recentlyHit > 0) this.recentlyHit += 100;
        }
    }

    protected void onDeathUpdate() {
        this.deathTime++;
        float explosionPower = getPowered() ? 12.0F : 8.0F;
        float radius = explosionPower * 1.5F;
        for (Entity entity : this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().grow(radius), EntitySelectors.CAN_AI_TARGET)) {
            double x = this.posX - entity.posX;
            double y = this.posY - entity.posY;
            double z = this.posZ - entity.posZ;
            double d = Math.sqrt(x * x + y * y + z * z);
            float f2 = this.deathTime / 100.0F;
            entity.motionX += x / d * f2 * 0.09D;
            entity.motionY += y / d * f2 * 0.09D;
            entity.motionZ += z / d * f2 * 0.09D;
        }
        this.posX += (this.rand.nextFloat() * 0.2F) - 0.10000000149011612D;
        this.posZ += (this.rand.nextFloat() * 0.2F) - 0.10000000149011612D;
        if (this.deathTime >= 100) {
            if (!this.world.isRemote) {
                MutatedExplosion.create(this, explosionPower, isBurning(), true);
                EntityUtil.spawnLingeringCloud(this);
                EntityUtil.dropExperience(this, this.recentlyHit, this::getExperiencePoints, this.attackingPlayer);
                super.onDeath((this.deathCause != null) ? this.deathCause : DamageSource.GENERIC);
                if (this.world.getGameRules().getBoolean("doMobLoot") && this.attackingPlayer != null)
                    this.world.spawnEntity(new CreeperMinionEggEntity(this, this.attackingPlayer));
            }
            setDead();
        }
    }

    public boolean ableToCauseSkullDrop() {
        return this.world.getGameRules().getBoolean("doMobLoot");
    }

    @Deprecated
    public boolean hasIgnited() {
        return false;
    }

    @Deprecated
    public int getCreeperState() {
        return -1;
    }

    protected void handleJumpWater() {
        this.motionY += 0.03999999910593033D;
    }

    protected void handleJumpLava() {
        handleJumpWater();
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        return (getPowered() && blockStateIn.getBlockHardness(worldIn, pos) > -1.0F && ForgeEventFactory.onEntityDestroyBlock(this, pos, blockStateIn)) ? Math.min(0.8F, f) : f;
    }

    public void playLivingSound() {
        if (getAttackTarget() == null) super.playLivingSound();
    }

    protected SoundEvent getAmbientSound() {
        return MBSoundEvents.ENTITY_MUTANT_CREEPER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return MBSoundEvents.ENTITY_MUTANT_CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return MBSoundEvents.ENTITY_MUTANT_CREEPER_HURT;
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("JumpAttacking", isJumpAttacking());
        compound.setBoolean("Charging", isCharging());
        compound.setInteger("ChargeTime", this.chargeTime);
        compound.setInteger("ChargeHits", this.chargeHits);
        compound.setBoolean("SummonLightning", this.summonLightning);
        compound.setShort("DeathTime", (short) this.deathTime);
        if (getPowered()) compound.setBoolean("Powered", true);
        for (String unusedNBT : new String[]{"powered", "Fuse", "ExplosionRadius", "ignited"})
            compound.removeTag(unusedNBT);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setPowered(compound.getBoolean("Powered"));
        setJumpAttacking(compound.getBoolean("JumpAttacking"));
        setCharging(compound.getBoolean("Charging"));
        this.chargeTime = compound.getInteger("ChargeTime");
        this.chargeHits = compound.getInteger("ChargeHits");
        this.summonLightning = compound.getBoolean("SummonLightning");
        this.deathTime = super.deathTime;
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.flashTick);
        buffer.writeInt(this.deathTime);
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.flashTick = additionalData.readInt();
        this.deathTime = additionalData.readInt();
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }

    class SpawnMinionsGoal extends EntityAIBase {
        public boolean shouldExecute() {
            return (MutantCreeperEntity.this.getAttackTarget() != null && MutantCreeperEntity.this.getDistanceSq(MutantCreeperEntity.this.getAttackTarget()) <= 1024.0D && !MutantCreeperEntity.this.isCharging() && !MutantCreeperEntity.this.isJumpAttacking() && MutantCreeperEntity.this.rand.nextFloat() * 100.0F < 0.6F);
        }

        public void startExecuting() {
            for (int i = (int) Math.ceil((MutantCreeperEntity.this.getHealth() / MutantCreeperEntity.this.getMaxHealth() * 4.0F)); i > 0; i--) {
                CreeperMinionEntity creeper = new CreeperMinionEntity(MutantCreeperEntity.this.world);
                double x = MutantCreeperEntity.this.posX + (MutantCreeperEntity.this.rand.nextFloat() - MutantCreeperEntity.this.rand.nextFloat());
                double y = MutantCreeperEntity.this.posY + (MutantCreeperEntity.this.rand.nextFloat() * 0.5F);
                double z = MutantCreeperEntity.this.posZ + (MutantCreeperEntity.this.rand.nextFloat() - MutantCreeperEntity.this.rand.nextFloat());
                double xx = (MutantCreeperEntity.this.getAttackTarget()).posX - MutantCreeperEntity.this.posX;
                double yy = (MutantCreeperEntity.this.getAttackTarget()).posY - MutantCreeperEntity.this.posY;
                double zz = (MutantCreeperEntity.this.getAttackTarget()).posZ - MutantCreeperEntity.this.posZ;
                creeper.motionX = xx * 0.15000000596046448D + (MutantCreeperEntity.this.rand.nextFloat() * 0.05F);
                creeper.motionY = yy * 0.15000000596046448D + (MutantCreeperEntity.this.rand.nextFloat() * 0.05F);
                creeper.motionZ = zz * 0.15000000596046448D + (MutantCreeperEntity.this.rand.nextFloat() * 0.05F);
                creeper.setLocationAndAngles(x, y, z, MutantCreeperEntity.this.rotationYaw, 0.0F);
                creeper.setOwnerId(MutantCreeperEntity.this.entityUniqueID);
                if (MutantCreeperEntity.this.getPowered()) creeper.setPowered(true);
                MutantCreeperEntity.this.world.spawnEntity(creeper);
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }
    }

    class ChargeAttackGoal extends EntityAIBase {
        public ChargeAttackGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase target = MutantCreeperEntity.this.getAttackTarget();
            return ((target != null && MutantCreeperEntity.this.onGround && MutantCreeperEntity.this.getMaxHealth() - MutantCreeperEntity.this.getHealth() >= MutantCreeperEntity.this.getMaxHealth() / 6.0F && MutantCreeperEntity.this.getDistanceSq(target) >= 25.0D && MutantCreeperEntity.this.getDistanceSq(target) <= 1024.0D && MutantCreeperEntity.this.rand.nextFloat() * 100.0F < 0.7F) || MutantCreeperEntity.this.isCharging());
        }

        public boolean shouldContinueExecuting() {
            if (MutantCreeperEntity.this.summonLightning && MutantCreeperEntity.this.getAttackTarget() != null && MutantCreeperEntity.this.getDistanceSq(MutantCreeperEntity.this.getAttackTarget()) < 25.0D && MutantCreeperEntity.this.world.canSeeSky(MutantCreeperEntity.this.getPosition())) {
                MutantCreeperEntity.this.world.addWeatherEffect(new EntityLightningBolt(MutantCreeperEntity.this.world, MutantCreeperEntity.this.posX, MutantCreeperEntity.this.posY, MutantCreeperEntity.this.posZ, false));
                return false;
            }
            return (MutantCreeperEntity.this.chargeTime < 100 && MutantCreeperEntity.this.chargeHits > 0);
        }

        public void startExecuting() {
            MutantCreeperEntity.this.setCharging(true);
            if (MutantCreeperEntity.this.rand.nextInt(MutantCreeperEntity.this.world.isThundering() ? 2 : 6) == 0 && !MutantCreeperEntity.this.getPowered())
                MutantCreeperEntity.this.summonLightning = true;
        }

        public void updateTask() {
            MutantCreeperEntity.this.getNavigator().clearPath();
            int i = MutantCreeperEntity.this.chargeTime % 20;
            if (i == 0 || i == 20)
                MutantCreeperEntity.this.playSound(MBSoundEvents.ENTITY_MUTANT_CREEPER_CHARGE, 0.6F, 0.7F + MutantCreeperEntity.this.rand.nextFloat() * 0.6F);
            ++MutantCreeperEntity.this.chargeTime;
        }

        public void resetTask() {
            if (MutantCreeperEntity.this.chargeTime >= 100) {
                MutantCreeperEntity.this.heal(MutantCreeperEntity.this.getMaxHealth() / 4.0F);
                MutantCreeperEntity.this.world.setEntityState(MutantCreeperEntity.this, (byte) 6);
            }
            MutantCreeperEntity.this.chargeTime = 0;
            MutantCreeperEntity.this.chargeHits = 4 + MutantCreeperEntity.this.rand.nextInt(3);
            MutantCreeperEntity.this.setCharging(false);
            MutantCreeperEntity.this.summonLightning = false;
        }
    }

    class JumpAttackGoal extends EntityAIBase {
        public boolean shouldExecute() {
            return (MutantCreeperEntity.this.onGround && MutantCreeperEntity.this.getAttackTarget() != null && !MutantCreeperEntity.this.isCharging() && MutantCreeperEntity.this.getDistance(MutantCreeperEntity.this.getAttackTarget()) <= 1024.0D && MutantCreeperEntity.this.rand.nextFloat() * 100.0F < 0.9F);
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            MutantCreeperEntity.this.isInWeb = false;
            MutantCreeperEntity.this.setJumpAttacking(true);
            MutantCreeperEntity.this.motionX = ((MutantCreeperEntity.this.getAttackTarget()).posX - MutantCreeperEntity.this.posX) * 0.2D;
            MutantCreeperEntity.this.motionY = 1.4D;
            MutantCreeperEntity.this.motionZ = ((MutantCreeperEntity.this.getAttackTarget()).posZ - MutantCreeperEntity.this.posZ) * 0.2D;
        }
    }
}

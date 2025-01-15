package chumbanotz.mutantbeasts.entity.mutant;

import chumbanotz.mutantbeasts.entity.EndersoulCloneEntity;
import chumbanotz.mutantbeasts.entity.EndersoulFragmentEntity;
import chumbanotz.mutantbeasts.entity.ai.EntityAIAvoidDamage;
import chumbanotz.mutantbeasts.entity.ai.EntityAIHurtByNearestTarget;
import chumbanotz.mutantbeasts.entity.ai.MBEntityAIAttackMelee;
import chumbanotz.mutantbeasts.entity.projectile.ThrowableBlockEntity;
import chumbanotz.mutantbeasts.item.EndersoulHandItem;
import chumbanotz.mutantbeasts.packet.HeldBlockPacket;
import chumbanotz.mutantbeasts.packet.MBPacketHandler;
import chumbanotz.mutantbeasts.packet.TeleportPacket;
import chumbanotz.mutantbeasts.pathfinding.MBGroundPathNavigator;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBParticles;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import com.google.common.base.Predicate;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MutantEndermanEntity extends EntityMob implements IEntityAdditionalSpawnData {
    public static final int MAX_DEATH_TIME = 280;
    public static final byte MELEE_ATTACK = 1;
    public static final byte THROW_ATTACK = 2;
    public static final byte STARE_ATTACK = 3;
    public static final byte TELEPORT_ATTACK = 4;
    public static final byte SCREAM_ATTACK = 5;
    public static final byte CLONE_ATTACK = 6;
    public static final byte TELESMASH_ATTACK = 7;
    public static final byte DEATH_ATTACK = 8;
    private static final DataParameter<Byte> ACTIVE_ARM = EntityDataManager.createKey(MutantEndermanEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> CLONE = EntityDataManager.createKey(MutantEndermanEntity.class, DataSerializers.BOOLEAN);
    public final int[] heldBlock = new int[5];
    public final int[] heldBlockTick = new int[5];
    public int hasTarget;
    public int deathTime;
    private int attackID;
    private int attackTick;
    private int prevArmScale;
    private int armScale;
    private BlockPos teleportPosition = BlockPos.ORIGIN;
    private int screamDelayTick;
    private boolean triggerThrowBlock;
    private int blockFrenzy;
    private List<Entity> capturedEntities;
    private int dirty = -1;
    private DamageSource deathCause;

    public MutantEndermanEntity(World worldIn) {
        super(worldIn);
        this.experienceValue = 40;
        this.stepHeight = 1.5F;
        setSize(1.2F, 4.2F);
    }

    private static boolean canPlaceBlock(World world, BlockPos placePos, BlockPos downPos, Block heldBlock) {
        return (heldBlock.canPlaceBlockAt(world, placePos) && world.isAirBlock(placePos) && !world.isAirBlock(downPos) && world.getBlockState(downPos).isFullCube());
    }

    public static void teleportAttack(EntityLivingBase attacker) {
        double radius = 3.0D;
        int duration = 140 + attacker.getRNG().nextInt(60);
        DamageSource damageSource = DamageSource.causeMobDamage(attacker);
        if (attacker instanceof EntityPlayer) {
            radius = 2.0D;
            duration = 100;
            damageSource = DamageSource.causePlayerDamage((EntityPlayer) attacker);
        }
        for (Entity entity : attacker.world.getEntitiesInAABBexcluding(attacker, attacker.getEntityBoundingBox().grow(radius), EndersoulFragmentEntity.IS_VALID_TARGET)) {
            if (entity instanceof EntityLivingBase && entity.attackEntityFrom(damageSource, 4.0F) && attacker.getRNG().nextInt(3) == 0)
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, duration));
            double x = entity.posX - attacker.posX;
            double z = entity.posZ - attacker.posZ;
            double signX = x / Math.abs(x);
            double signZ = z / Math.abs(z);
            if (Double.isNaN(signX) || Double.isNaN(signZ)) return;
            entity.motionX = (radius * signX * 2.0D - x) * 0.20000000298023224D;
            entity.motionY = 0.20000000298023224D;
            entity.motionZ = (radius * signZ * 2.0D - z) * 0.20000000298023224D;
        }
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new MeleeGoal());
        this.tasks.addTask(1, new ThrowBlockGoal());
        this.tasks.addTask(1, new StareGoal());
        this.tasks.addTask(1, new TeleportGoal());
        this.tasks.addTask(1, new ScreamGoal());
        this.tasks.addTask(1, new CloneGoal());
        this.tasks.addTask(1, new TeleSmashGoal());
        this.tasks.addTask(2, (new MBEntityAIAttackMelee(this, 1.2D)).setMaxAttackTick(10));
        this.tasks.addTask(3, new EntityAIAvoidDamage(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIHurtByNearestTarget(this));
        this.targetTasks.addTask(1, new FindTargetGoal(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, (Predicate<EntityEndermite>) p_apply_1_ -> p_apply_1_.isSpawnedByPlayer()));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(96.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ACTIVE_ARM, (byte) 0);
        this.dataManager.register(CLONE, false);
    }

    public BlockPos getTeleportPosition() {
        return this.teleportPosition;
    }

    public void setTeleportPosition(BlockPos pos) {
        this.teleportPosition = pos;
        this.attackID = 4;
        if (this.world.isRemote) {
            spawnTeleportParticles();
        } else {
            MBPacketHandler.INSTANCE.sendToAllTracking(new TeleportPacket(this, pos), this);
        }
    }

    public int getActiveArm() {
        return this.dataManager.get(ACTIVE_ARM);
    }

    private void setActiveArm(int armID) {
        this.dataManager.set(ACTIVE_ARM, (byte) armID);
    }

    public boolean isClone() {
        return this.dataManager.get(CLONE);
    }

    private void setClone(boolean clone) {
        this.dataManager.set(CLONE, clone);
        playSound(MBSoundEvents.ENTITY_MUTANT_ENDERMAN_MORPH, 2.0F, getSoundPitch());
    }

    public int getAttackID() {
        return this.attackID;
    }

    private void setAttackID(int attackID) {
        this.attackID = attackID;
        this.attackTick = 0;
        if (!this.world.isRemote) this.world.setEntityState(this, (byte) -attackID);
    }

    public int getAttackTick() {
        return this.attackTick;
    }

    public float getEyeHeight() {
        return isClone() ? 2.55F : 3.9F;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return (new MBGroundPathNavigator(this, worldIn)).setAvoidRain(true);
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public int getMaxFallHeight() {
        return isClone() ? 3 : super.getMaxFallHeight();
    }

    public boolean canBeCollidedWith() {
        return (super.canBeCollidedWith() && this.attackID != 4);
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (CLONE.equals(key)) if (isClone()) {
            setSize(0.6F, 2.9F);
        } else {
            setSize(1.2F, 4.2F);
        }
    }

    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        setFlag(2, (entitylivingbaseIn != null));
    }

    public boolean isAggressive() {
        return getFlag(2);
    }

    public float getArmScale(float partialTicks) {
        return (this.prevArmScale + (this.armScale - this.prevArmScale) * partialTicks) / 10.0F;
    }

    private void updateTargetTick() {
        this.prevArmScale = this.armScale;
        if (isAggressive()) this.hasTarget = 20;
        boolean emptyHanded = true;
        for (int i = 1; i < this.heldBlock.length; i++) {
            if (this.heldBlock[i] > 0) emptyHanded = false;
            if (this.hasTarget > 0) {
                if (this.heldBlock[i] > 0) this.heldBlockTick[i] = Math.min(10, this.heldBlockTick[i] + 1);
            } else {
                this.heldBlockTick[i] = Math.max(0, this.heldBlockTick[i] - 1);
            }
        }
        if (this.hasTarget > 0) {
            this.armScale = Math.min(10, this.armScale + 1);
        } else if (emptyHanded) {
            this.armScale = Math.max(0, this.armScale - 1);
        } else if (!this.world.isRemote) {
            boolean mobGriefing = ForgeEventFactory.getMobGriefingEvent(this.world, this);
            for (int j = 1; j < this.heldBlock.length; j++) {
                if (this.heldBlock[j] != 0 && this.heldBlockTick[j] == 0) {
                    BlockPos placePos = new BlockPos(this.posX - 1.5D + this.rand.nextDouble() * 4.0D, this.posY - 0.5D + this.rand.nextDouble() * 2.5D, this.posZ - 1.5D + this.rand.nextDouble() * 4.0D);
                    Block heldBlock = Block.getBlockById(this.heldBlock[j]);
                    if (canPlaceBlock(this.world, placePos, placePos.down(), heldBlock) && mobGriefing) {
                        this.world.setBlockState(placePos, Block.getStateById(this.heldBlock[j]));
                        SoundType soundType = heldBlock.getSoundType(heldBlock.getDefaultState(), this.world, placePos, this);
                        playSound(soundType.getPlaceSound(), (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                        sendHoldBlock(j, 0);
                    } else if (!mobGriefing || this.rand.nextInt(50) == 0) {
                        this.triggerThrowBlock = true;
                    }
                }
            }
        }
        this.hasTarget = Math.max(0, this.hasTarget - 1);
    }

    private void updateScreamEntities() {
        this.screamDelayTick = Math.max(0, this.screamDelayTick - 1);
        if (this.attackID == 5 && this.attackTick >= 40 && this.attackTick <= 160) {
            if (this.attackTick == 160) {
                this.capturedEntities = null;
            } else if (this.capturedEntities == null) {
                this.capturedEntities = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().grow(20.0D, 12.0D, 20.0D), EndersoulFragmentEntity.IS_VALID_TARGET);
            }
            for (int i = 0; this.capturedEntities != null && i < this.capturedEntities.size(); i++) {
                Entity entity = this.capturedEntities.get(i);
                if (getDistanceSq(entity) > 400.0D || !EntitySelectors.NOT_SPECTATING.apply(entity) || !entity.isAddedToWorld()) {
                    this.capturedEntities.remove(i);
                    i--;
                } else {
                    if (this.attackTick == 40) {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this).setDamageBypassesArmor().setMagicDamage(), 4.0F);
                        if (entity instanceof EntityLiving) {
                            EntityLiving living = (EntityLiving) entity;
                            living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 120, 3));
                            if (this.rand.nextInt(2) != 0)
                                living.addPotionEffect(new PotionEffect(MobEffects.POISON, 120 + this.rand.nextInt(180), this.rand.nextInt(2)));
                            if (this.rand.nextInt(4) != 0)
                                living.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 300 + this.rand.nextInt(300), this.rand.nextInt(2)));
                            if (this.rand.nextInt(3) != 0)
                                living.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 120 + this.rand.nextInt(60), 10 + this.rand.nextInt(2)));
                            if (this.rand.nextInt(4) != 0)
                                living.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120 + this.rand.nextInt(400), 0));
                        }
                    }
                    entity.rotationPitch += (this.rand.nextFloat() - 0.3F) * 6.0F;
                }
            }
        }
    }

    public void handleStatusUpdate(byte id) {
        if (id <= 0) {
            setAttackID(Math.abs(id));
            if (this.attackID == 6) spawnTeleportParticles();
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public void onLivingUpdate() {
        this.isJumping = false;
        super.onLivingUpdate();
        if (this.attackID != 0) this.attackTick++;
        if (this.world.isRemote && this.attackID == 5 && this.attackTick == 40) spawnTeleportParticles();
        if (this.attackID == 8) this.deathTime = this.attackTick;
        updateTargetTick();
        updateScreamEntities();
        if (this.world.isRemote && !isClone()) {
            double h = (this.attackID != 8) ? this.height : (this.height + 1.0F);
            double w = (this.attackID != 8) ? this.width : (this.width * 1.5F);
            for (int i = 0; i < 3; i++) {
                double x = this.posX + (this.rand.nextDouble() - 0.5D) * w;
                double y = this.posY + this.rand.nextDouble() * h - 0.25D;
                double z = this.posZ + (this.rand.nextDouble() - 0.5D) * w;
                this.world.spawnParticle(EnumParticleTypes.PORTAL, x, y, z, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    private void updateBlockFrenzy() {
        this.blockFrenzy = Math.max(0, this.blockFrenzy - 1);
        if (getAttackTarget() != null && this.attackID == 0) {
            if (this.blockFrenzy == 0 && (getLastDamageSource() instanceof net.minecraft.util.EntityDamageSourceIndirect || this.rand.nextInt(!hasPath() ? 200 : 600) == 0))
                this.blockFrenzy = 200 + this.rand.nextInt(80);
            if (this.blockFrenzy > 0 && this.rand.nextInt(8) == 0) {
                int index = getFavorableHand();
                if (index == -1) return;
                BlockPos pos = new BlockPos(this.posX - 2.5D + this.rand.nextDouble() * 5.0D, this.posY - 0.5D + this.rand.nextDouble() * 3.0D, this.posZ - 2.5D + this.rand.nextDouble() * 5.0D);
                IBlockState blockState = this.world.getBlockState(pos);
                if (index != -1 && EndersoulHandItem.canCarry(this.world, pos, blockState)) {
                    sendHoldBlock(index, Block.getStateId(blockState));
                    if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) this.world.setBlockToAir(pos);
                }
            }
        }
    }

    private void updateTeleport() {
        EntityLivingBase entityLivingBase = getAttackTarget();
        teleportByChance((entityLivingBase == null) ? 1600 : 800, entityLivingBase);
        if (isInWater() || this.fallDistance > 3.0F || (entityLivingBase != null && (isRidingSameEntity(entityLivingBase) || getDistanceSq(entityLivingBase) > 1024.0D || (!hasPath() && !getEntitySenses().canSee(entityLivingBase)))))
            teleportByChance(10, entityLivingBase);
    }

    protected void updateAITasks() {
        if (this.ticksExisted % 100 == 0 && !isClone() && isWet()) attackEntityFrom(DamageSource.DROWN, 1.0F);
        if (this.dirty >= 0) this.dirty++;
        if (this.dirty >= 8) {
            this.dirty = -1;
            for (int i = 1; i < this.heldBlock.length; i++) {
                if (this.heldBlock[i] > 0) sendHoldBlock(i, this.heldBlock[i]);
            }
        }
        updateBlockFrenzy();
        updateTeleport();
    }

    protected void collideWithNearbyEntities() {
        if (!isClone()) super.collideWithNearbyEntities();
    }

    private int getAvailableHand() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < this.heldBlock.length; i++) {
            if (this.heldBlock[i] == 0) list.add(i);
        }
        if (list.isEmpty()) return -1;
        return list.get(this.rand.nextInt(list.size()));
    }

    private int getFavorableHand() {
        List<Integer> outer = new ArrayList<>();
        List<Integer> inner = new ArrayList<>();
        for (int i = 1; i < this.heldBlock.length; i++) {
            if (this.heldBlock[i] == 0) if (i <= 2) {
                outer.add(i);
            } else {
                inner.add(i);
            }
        }
        if (outer.isEmpty() && inner.isEmpty()) return -1;
        if (!outer.isEmpty()) return outer.get(this.rand.nextInt(outer.size()));
        return inner.get(this.rand.nextInt(inner.size()));
    }

    private int getThrowingHand() {
        List<Integer> outer = new ArrayList<>();
        List<Integer> inner = new ArrayList<>();
        for (int i = 1; i < this.heldBlock.length; i++) {
            if (this.heldBlock[i] != 0) if (i <= 2) {
                outer.add(i);
            } else {
                inner.add(i);
            }
        }
        if (outer.isEmpty() && inner.isEmpty()) return -1;
        if (!inner.isEmpty()) return inner.get(this.rand.nextInt(inner.size()));
        return outer.get(this.rand.nextInt(outer.size()));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (!this.world.isRemote && this.attackID == 0) {
            int i = getAvailableHand();
            if (!teleportByChance(6, entityIn)) if (i != -1) {
                boolean allHandsFree = (this.heldBlock[1] == 0 && this.heldBlock[2] == 0);
                if (allHandsFree && this.rand.nextInt(10) == 0) {
                    this.attackID = 6;
                } else if (allHandsFree && this.rand.nextInt(7) == 0) {
                    this.attackID = 7;
                } else {
                    setActiveArm(i);
                    this.attackID = 1;
                }
            } else {
                this.triggerThrowBlock = true;
            }
        }
        if (isClone()) {
            boolean flag = super.attackEntityAsMob(entityIn);
            if (!this.world.isRemote && this.rand.nextInt(2) == 0) {
                double x = entityIn.posX + (this.rand.nextDouble() - 0.5D) * 24.0D;
                double y = entityIn.posY + this.rand.nextInt(5) + 4.0D;
                double z = entityIn.posZ + (this.rand.nextDouble() - 0.5D) * 24.0D;
                teleportTo(x, y, z);
            }
            if (flag) heal(2.0F);
            swingArm(EnumHand.MAIN_HAND);
            return flag;
        }
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) return false;
        if (source.getTrueSource() instanceof net.minecraft.entity.boss.EntityDragon || source.getTrueSource() instanceof MutantEndermanEntity)
            return false;
        if ((this.attackID == 4 || this.attackID == 5) && source != DamageSource.OUT_OF_WORLD) return false;
        boolean damaged = super.attackEntityFrom(source, amount);
        if (damaged && (this.attackID == 3 || this.attackID == 6)) {
            this.attackID = 0;
            return true;
        }
        if (!this.world.isRemote && isEntityAlive() && (getAttackTarget() == null || (source != DamageSource.LAVA && source.getImmediateSource() != null))) {
            Entity entity = source.getTrueSource();
            boolean betterDodge = (entity == null);
            if (source.isProjectile() || source.isExplosion() || source == DamageSource.FALL) betterDodge = true;
            if (teleportByChance(betterDodge ? 3 : 6, entity) && source != DamageSource.OUT_OF_WORLD) {
                if (entity != null && entity instanceof EntityLivingBase) setRevengeTarget((EntityLivingBase) entity);
                return false;
            }
            teleportByChance((source == DamageSource.DROWN || source == DamageSource.IN_WALL) ? 3 : 5, entity);
        }
        return damaged;
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return (!isClone() && super.isPotionApplicable(potioneffectIn));
    }

    private boolean teleportByChance(int chance, @Nullable Entity entity) {
        if (this.attackID != 0 && !isClone()) return false;
        if (this.rand.nextInt(Math.max(1, chance)) == 0)
            return (entity == null) ? teleportRandomly() : teleportToEntity(entity);
        return false;
    }

    private boolean teleportRandomly() {
        if (this.attackID != 0 && !isClone()) return false;
        double radius = 24.0D;
        double x = this.posX + (this.rand.nextDouble() - 0.5D) * 2.0D * radius;
        double y = this.posY + this.rand.nextInt((int) radius * 2) - radius;
        double z = this.posZ + (this.rand.nextDouble() - 0.5D) * 2.0D * radius;
        return teleportTo(x, y, z);
    }

    private boolean teleportToEntity(Entity entity) {
        if (this.attackID != 0 && !isClone()) return false;
        double d = getDistanceSq(entity);
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        double radius = 16.0D;
        if (d < 100.0D) {
            x = entity.posX + (this.rand.nextDouble() - 0.5D) * 2.0D * radius;
            y = entity.posY + this.rand.nextDouble() * radius;
            z = entity.posZ + (this.rand.nextDouble() - 0.5D) * 2.0D * radius;
        } else {
            Vec3d vec = new Vec3d(this.posX - entity.posX, (getEntityBoundingBox()).minY + this.height / 2.0D - entity.posY + entity.getEyeHeight(), this.posZ - entity.posZ);
            vec = vec.normalize();
            x = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec.x * radius;
            y = this.posY + this.rand.nextInt(8) - vec.y * radius;
            z = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec.z * radius;
        }
        return teleportTo(x, y, z);
    }

    private boolean teleportTo(double targetX, double targetY, double targetZ) {
        if (!isServerWorld()) return false;
        if (isClone()) {
            boolean flag = EntityUtil.teleportTo(this, targetX, targetY, targetZ);
            if (flag) {
                dismountRidingEntity();
                if (!isSilent()) {
                    this.world.playSound(null, this.prevPosX, this.prevPosY + this.height / 2.0D, this.prevPosZ, MBSoundEvents.ENTITY_ENDERSOUL_CLONE_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
                    playSound(MBSoundEvents.ENTITY_ENDERSOUL_CLONE_TELEPORT, 1.0F, 1.0F);
                }
            }
            return flag;
        }
        if (this.attackID == 0) {
            this.attackID = 4;
            BlockPos.MutableBlockPos pos = (new BlockPos.MutableBlockPos()).setPos(targetX, targetY, targetZ);
            if (this.world.isBlockLoaded(pos)) {
                do {
                    pos.move(EnumFacing.DOWN);
                } while (pos.getY() > 0 && !this.world.getBlockState(pos).getMaterial().blocksMovement());
                pos.move(EnumFacing.UP);
            }
            if (!isOffsetPositionInLiquid((pos.getX() - MathHelper.floor(this.posX)), (pos.getY() - MathHelper.floor(this.posY)), (pos.getZ() - MathHelper.floor(this.posZ)))) {
                this.attackID = 0;
                return false;
            }
            setTeleportPosition(pos.toImmutable());
            return true;
        }
        return false;
    }

    private void spawnTeleportParticles() {
        int temp = (this.attackID == 4) ? 512 : 256;
        for (int i = 0; i < temp; i++) {
            float f = (this.rand.nextFloat() - 0.5F) * 1.8F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 1.8F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 1.8F;
            boolean useCurrentPos = (this.attackID != 4 || i < temp / 2);
            double tempX = (useCurrentPos ? this.posX : getTeleportPosition().getX()) + (this.rand.nextDouble() - 0.5D) * this.width;
            double tempY = (useCurrentPos ? this.posY : getTeleportPosition().getY()) + (this.rand.nextDouble() - 0.5D) * this.height + 1.5D;
            double tempZ = (useCurrentPos ? this.posZ : getTeleportPosition().getZ()) + (this.rand.nextDouble() - 0.5D) * this.width;
            this.world.spawnParticle(MBParticles.ENDERSOUL, tempX, tempY, tempZ, f, f1, f2);
        }
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return getEntityBoundingBox().grow(3.5D);
    }

    protected boolean canBeRidden(Entity entityIn) {
        return (super.canBeRidden(entityIn) && entityIn instanceof EntityLivingBase);
    }

    public boolean canEntityBeSeen(Entity entityIn) {
        return (!entityIn.getClass().getSimpleName().equals("EntityGorgon") && super.canEntityBeSeen(entityIn));
    }

    public boolean isPushedByWater() {
        return false;
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        this.capturedEntities = null;
        if (!this.world.isRemote) {
            this.deathCause = cause;
            if (isClone()) setClone(false);
            setAttackID(8);
            if (this.world.getGameRules().getBoolean("doMobLoot"))
                super.dropEquipment((this.recentlyHit > 0), ForgeHooks.getLootingLevel(this, cause.getTrueSource(), cause));
            if (this.recentlyHit > 0) this.recentlyHit += 280;
        }
    }

    protected void onDeathUpdate() {
        this.motionX = 0.0D;
        this.motionY = Math.min(this.motionY, 0.0D);
        this.motionZ = 0.0D;
        if (this.deathTime == 80) playSound(MBSoundEvents.ENTITY_MUTANT_ENDERMAN_DEATH, 5.0F, getSoundPitch());
        if (this.deathTime >= 60) {
            if (this.deathTime < 80 && this.capturedEntities == null)
                this.capturedEntities = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().grow(10.0D, 8.0D, 10.0D), EndersoulFragmentEntity.IS_VALID_TARGET);
            if (!this.world.isRemote && this.rand.nextInt(3) != 0) {
                EndersoulFragmentEntity orb = new EndersoulFragmentEntity(this.world);
                orb.setPosition(this.posX, this.posY + 3.8D, this.posZ);
                orb.motionX = ((this.rand.nextFloat() - 0.5F) * 1.5F);
                orb.motionY = ((this.rand.nextFloat() - 0.5F) * 1.5F);
                orb.motionZ = ((this.rand.nextFloat() - 0.5F) * 1.5F);
                this.world.spawnEntity(orb);
            }
        }
        if (this.deathTime >= 80 && this.deathTime < 260 && this.capturedEntities != null)
            for (int i = 0; i < this.capturedEntities.size(); i++) {
                Entity entity = this.capturedEntities.get(i);
                if (EndersoulFragmentEntity.isProtected(entity) || !EntitySelectors.NOT_SPECTATING.apply(entity) || !entity.isAddedToWorld()) {
                    this.capturedEntities.remove(i);
                    i--;
                } else {
                    if (entity.fallDistance > 4.5F) entity.fallDistance = 4.5F;
                    if (getDistanceSq(entity) > 64.0D) {
                        double x = this.posX - entity.posX;
                        double z = this.posZ - entity.posZ;
                        double d = Math.sqrt(x * x + z * z);
                        entity.motionX = 0.800000011920929D * x / d;
                        entity.motionZ = 0.800000011920929D * z / d;
                        if (this.posY + 4.0D > entity.posY)
                            entity.motionY = Math.max(entity.motionY, 0.4000000059604645D);
                    }
                }
            }
        if (!this.world.isRemote && this.deathTime >= 100 && this.deathTime < 150 && this.deathTime % 6 == 0 && this.world.getGameRules().getBoolean("doMobLoot")) {
            DamageSource source = (this.deathCause != null) ? this.deathCause : DamageSource.GENERIC;
            dropLoot((this.recentlyHit > 0), ForgeHooks.getLootingLevel(this, source.getTrueSource(), source), source);
        }
        if (this.deathTime >= 280) {
            EntityUtil.dropExperience(this, this.recentlyHit, this::getExperiencePoints, this.attackingPlayer);
            setDead();
        }
    }

    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
    }

    protected boolean canDropLoot() {
        return false;
    }

    public EntityItem entityDropItem(ItemStack stack, float offsetY) {
        return super.entityDropItem(stack, (this.deathTime > 0) ? 3.84F : offsetY);
    }

    public String getName() {
        return isClone() ? I18n.translateToLocal("entity.mutantbeasts.endersoul_clone.name") : super.getName();
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ScreamDelay", this.screamDelayTick);
        compound.setInteger("BlockFrenzy", this.blockFrenzy);
        compound.setShort("DeathTime", (short) this.deathTime);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.screamDelayTick = compound.getInteger("ScreamDelay");
        this.blockFrenzy = compound.getInteger("BlockFrenzy");
        this.deathTime = super.deathTime;
        if (this.deathTime > 0) {
            this.attackID = 8;
            this.attackTick = this.deathTime;
            super.deathTime = 0;
        }
    }

    public int getTalkInterval() {
        return 200;
    }

    public void playLivingSound() {
        if (!isClone()) super.playLivingSound();
    }

    protected SoundEvent getAmbientSound() {
        return MBSoundEvents.ENTITY_MUTANT_ENDERMAN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return MBSoundEvents.ENTITY_MUTANT_ENDERMAN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return MBSoundEvents.ENTITY_MUTANT_ENDERMAN_HURT;
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.attackID);
        buffer.writeInt(this.attackTick);
        buffer.writeInt(this.deathTime);
        buffer.writeInt(this.armScale);
        buffer.writeInt(this.hasTarget);
        buffer.writeLong(this.teleportPosition.toLong());
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.attackID = additionalData.readInt();
        this.attackTick = additionalData.readInt();
        this.deathTime = additionalData.readInt();
        this.armScale = additionalData.readInt();
        this.hasTarget = additionalData.readInt();
        this.teleportPosition = BlockPos.fromLong(additionalData.readLong());
    }

    public void sendHoldBlock(int blockIndex, int blockId) {
        this.heldBlock[blockIndex] = blockId;
        this.heldBlockTick[blockIndex] = 0;
        if (!this.world.isRemote)
            MBPacketHandler.INSTANCE.sendToAllTracking(new HeldBlockPacket(this, blockId, blockIndex), this);
    }

    private boolean isBeingLookedAtBy(EntityLivingBase target) {
        if (target instanceof EntityLiving)
            return (((EntityLiving) target).getAttackTarget() == this && target.canEntityBeSeen(this));
        Vec3d playerVec = target.getLook(1.0F).normalize();
        Vec3d targetVec = new Vec3d(this.posX - target.posX, (getEntityBoundingBox()).minY + getEyeHeight() - target.posY + target.getEyeHeight(), this.posZ - target.posZ);
        double length = targetVec.length();
        targetVec = targetVec.normalize();
        double d = playerVec.dotProduct(targetVec);
        return d > 1.0D - 0.08D / length && target.canEntityBeSeen(this);
    }

    static class FindTargetGoal extends EntityAINearestAttackableTarget<EntityPlayer> {
        public FindTargetGoal(MutantEndermanEntity mutantEnderman) {
            super(mutantEnderman, EntityPlayer.class, 0, false, false, target -> {
                if (mutantEnderman.isBeingLookedAtBy(target)) {
                    mutantEnderman.attackID = 3;
                    return true;
                }
                return EndersoulFragmentEntity.isProtected(target);
            });
        }

        public boolean shouldExecute() {
            return (((MutantEndermanEntity) this.taskOwner).attackID == 0 && super.shouldExecute());
        }

        public void resetTask() {
            super.resetTask();
            this.targetEntity = null;
        }
    }

    class StareGoal extends EntityAIBase {
        private EntityLivingBase attackTarget;

        public StareGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantEndermanEntity.this.getAttackTarget();
            MutantEndermanEntity.this.livingSoundTime = -MutantEndermanEntity.this.getTalkInterval();
            return (MutantEndermanEntity.this.attackID == 3 && this.attackTarget != null);
        }

        public void startExecuting() {
            MutantEndermanEntity.this.setAttackID(3);
            MutantEndermanEntity.this.playSound(MBSoundEvents.ENTITY_MUTANT_ENDERMAN_STARE, 2.5F, 0.7F + MutantEndermanEntity.this.rand.nextFloat() * 0.2F);
        }

        public boolean shouldContinueExecuting() {
            if (this.attackTarget instanceof EntityLiving) return false;
            return (MutantEndermanEntity.this.attackID == 3 && this.attackTarget.isEntityAlive() && MutantEndermanEntity.this.attackTick <= 100 && MutantEndermanEntity.this.isBeingLookedAtBy(this.attackTarget));
        }

        public void updateTask() {
            MutantEndermanEntity.this.getNavigator().clearPath();
            MutantEndermanEntity.this.getLookHelper().setLookPositionWithEntity(this.attackTarget, 45.0F, 45.0F);
        }

        public void resetTask() {
            MutantEndermanEntity.this.setAttackID(0);
            this.attackTarget.dismountRidingEntity();
            this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(MutantEndermanEntity.this).setDamageBypassesArmor().setMagicDamage(), 2.0F);
            this.attackTarget.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 160 + MutantEndermanEntity.this.rand.nextInt(140)));
            double x = MutantEndermanEntity.this.posX - this.attackTarget.posX;
            double z = MutantEndermanEntity.this.posZ - this.attackTarget.posZ;
            this.attackTarget.motionX = x * 0.10000000149011612D;
            this.attackTarget.motionY = 0.30000001192092896D;
            this.attackTarget.motionZ = z * 0.10000000149011612D;
            EntityUtil.sendPlayerVelocityPacket(this.attackTarget);
            this.attackTarget = null;
        }
    }

    class MeleeGoal extends EntityAIBase {
        public boolean shouldExecute() {
            return (MutantEndermanEntity.this.attackID == 1);
        }

        public void startExecuting() {
            MutantEndermanEntity.this.setAttackID(1);
        }

        public boolean shouldContinueExecuting() {
            return (shouldExecute() && MutantEndermanEntity.this.attackTick < 10);
        }

        public void updateTask() {
            if (MutantEndermanEntity.this.attackTick == 3) {
                MutantEndermanEntity.this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, MutantEndermanEntity.this.getSoundPitch());
                DamageSource damageSource = DamageSource.causeMobDamage(MutantEndermanEntity.this);
                boolean lower = (MutantEndermanEntity.this.getActiveArm() >= 3);
                float attackDamage = (float) MutantEndermanEntity.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                for (Entity entity : MutantEndermanEntity.this.world.getEntitiesWithinAABBExcludingEntity(MutantEndermanEntity.this, MutantEndermanEntity.this.getEntityBoundingBox().grow(4.0D))) {
                    if (!entity.canBeCollidedWith() || entity instanceof MutantEndermanEntity) continue;
                    double dist = MutantEndermanEntity.this.getDistance(entity);
                    double x = MutantEndermanEntity.this.posX - entity.posX;
                    double z = MutantEndermanEntity.this.posZ - entity.posZ;
                    if ((MutantEndermanEntity.this.getEntityBoundingBox()).minY <= (entity.getEntityBoundingBox()).maxY && dist <= 4.0D && EntityUtil.getHeadAngle(MutantEndermanEntity.this, x, z) < 3.0F + (1.0F - (float) dist / 4.0F) * 40.0F) {
                        if (entity.attackEntityFrom(damageSource, (attackDamage > 0.0F) ? (attackDamage + (lower ? 1.0F : 3.0F)) : 0.0F))
                            MutantEndermanEntity.this.applyEnchantments(MutantEndermanEntity.this, entity);
                        float power = 0.4F + MutantEndermanEntity.this.rand.nextFloat() * 0.2F;
                        if (!lower) power += 0.2F;
                        entity.motionX = -x / dist * power;
                        entity.motionY = (power * 0.6F);
                        entity.motionZ = -z / dist * power;
                    }
                }
            }
        }

        public void resetTask() {
            MutantEndermanEntity.this.setAttackID(0);
        }
    }

    class CloneGoal extends EntityAIBase {
        private final List<EndersoulCloneEntity> cloneList = new ArrayList<>();

        private EntityLivingBase attackTarget;

        public boolean shouldExecute() {
            if (MutantEndermanEntity.this.getAttackTarget() == null) return false;
            if (MutantEndermanEntity.this.heldBlock[1] == 0 && MutantEndermanEntity.this.heldBlock[2] == 0)
                return (MutantEndermanEntity.this.attackID == 6 || (MutantEndermanEntity.this.attackID == 0 && MutantEndermanEntity.this.rand.nextInt(300) == 0));
            return false;
        }

        public void startExecuting() {
            MutantEndermanEntity.this.setAttackID(6);
            MutantEndermanEntity.this.setClone(true);
            MutantEndermanEntity.this.extinguish();
            MutantEndermanEntity.this.clearActivePotions();
            MutantEndermanEntity.this.stepHeight = 1.0F;
            MutantEndermanEntity.this.hurtResistantTime = 15;
            this.attackTarget = MutantEndermanEntity.this.getAttackTarget();
            for (int i = 0; i < 7; i++) {
                double d1 = this.attackTarget.posX + (MutantEndermanEntity.this.rand.nextDouble() - 0.5D) * 24.0D;
                double d2 = this.attackTarget.posY + 8.0D;
                double d3 = this.attackTarget.posZ + (MutantEndermanEntity.this.rand.nextDouble() - 0.5D) * 24.0D;
                createClone(d1, d2, d3);
            }
            double x = this.attackTarget.posX + (MutantEndermanEntity.this.rand.nextDouble() - 0.5D) * 24.0D;
            double y = this.attackTarget.posY + 8.0D;
            double z = this.attackTarget.posZ + (MutantEndermanEntity.this.rand.nextDouble() - 0.5D) * 24.0D;
            EntityUtil.teleportTo(MutantEndermanEntity.this, x, y, z);
            createClone(MutantEndermanEntity.this.prevPosX, MutantEndermanEntity.this.prevPosY, MutantEndermanEntity.this.prevPosZ);
            EntityUtil.divertAttackers(MutantEndermanEntity.this, getRandomClone());
        }

        public boolean shouldContinueExecuting() {
            return (MutantEndermanEntity.this.attackID == 6 && MutantEndermanEntity.this.getAttackTarget() != null && MutantEndermanEntity.this.getAttackTarget().isEntityAlive() && !this.cloneList.isEmpty() && MutantEndermanEntity.this.isClone() && MutantEndermanEntity.this.attackTick < 600);
        }

        public void updateTask() {
            for (int i = this.cloneList.size() - 1; i >= 0; i--) {
                EndersoulCloneEntity clone = this.cloneList.get(i);
                if (!clone.isEntityAlive()) {
                    this.cloneList.remove(i);
                } else if (clone.getAttackTarget() != MutantEndermanEntity.this.getAttackTarget()) {
                    clone.setAttackTarget(MutantEndermanEntity.this.getAttackTarget());
                }
            }
        }

        public void resetTask() {
            MutantEndermanEntity.this.setAttackID(0);
            MutantEndermanEntity.this.setClone(false);
            for (EndersoulCloneEntity clone : this.cloneList) {
                if (clone.isEntityAlive()) clone.setDead();
            }
            this.cloneList.clear();
            MutantEndermanEntity.this.getNavigator().clearPath();
            MutantEndermanEntity.this.stepHeight = 1.4F;
            this.attackTarget.setRevengeTarget(MutantEndermanEntity.this);
            this.attackTarget = null;
        }

        private void createClone(double x, double y, double z) {
            EndersoulCloneEntity clone = new EndersoulCloneEntity(MutantEndermanEntity.this, x, y, z);
            clone.setAttackTarget(this.attackTarget);
            MutantEndermanEntity.this.world.spawnEntity(clone);
            this.cloneList.add(clone);
        }

        private EndersoulCloneEntity getRandomClone() {
            return this.cloneList.isEmpty() ? null : this.cloneList.get(MutantEndermanEntity.this.rand.nextInt(this.cloneList.size()));
        }
    }

    class ScreamGoal extends EntityAIBase {
        public ScreamGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            if (MutantEndermanEntity.this.getAttackTarget() != null && MutantEndermanEntity.this.attackID == 0)
                return MutantEndermanEntity.this.screamDelayTick <= 0 && MutantEndermanEntity.this.rand.nextInt(MutantEndermanEntity.this.isWet() ? 400 : 1200) == 0;
            return false;
        }

        public void startExecuting() {
            MutantEndermanEntity.this.setAttackID(5);
            MutantEndermanEntity.this.livingSoundTime = -MutantEndermanEntity.this.getTalkInterval();
        }

        public boolean shouldContinueExecuting() {
            return (MutantEndermanEntity.this.attackTick < 165);
        }

        public void updateTask() {
            MutantEndermanEntity.this.getNavigator().clearPath();
            if (MutantEndermanEntity.this.attackTick == 40) {
                if (MutantEndermanEntity.this.world.isRaining() && ForgeEventFactory.getMobGriefingEvent(MutantEndermanEntity.this.world, MutantEndermanEntity.this))
                    MutantEndermanEntity.this.world.getWorldInfo().setRaining(false);
                MutantEndermanEntity.this.playSound(MBSoundEvents.ENTITY_MUTANT_ENDERMAN_SCREAM, 6.0F, 0.7F + MutantEndermanEntity.this.rand.nextFloat() * 0.2F);
            }
        }

        public void resetTask() {
            MutantEndermanEntity.this.setAttackID(0);
            MutantEndermanEntity.this.screamDelayTick = 600;
        }
    }

    class TeleportGoal extends EntityAIBase {
        public TeleportGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            return (MutantEndermanEntity.this.attackID == 4);
        }

        public void startExecuting() {
            MutantEndermanEntity.this.attackTick = 0;
            MutantEndermanEntity.this.getNavigator().clearPath();
            if (MutantEndermanEntity.this.getAttackTarget() != null)
                MutantEndermanEntity.this.getLookHelper().setLookPositionWithEntity(MutantEndermanEntity.this.getAttackTarget(), 30.0F, 30.0F);
            MutantEndermanEntity.this.playSound(MBSoundEvents.ENTITY_MUTANT_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            MutantEndermanEntity.teleportAttack(MutantEndermanEntity.this);
            MutantEndermanEntity.this.setPosition(MutantEndermanEntity.this.getTeleportPosition().getX() + 0.5D, MutantEndermanEntity.this.getTeleportPosition().getY(), MutantEndermanEntity.this.getTeleportPosition().getZ() + 0.5D);
            MutantEndermanEntity.this.playSound(MBSoundEvents.ENTITY_MUTANT_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            MutantEndermanEntity.teleportAttack(MutantEndermanEntity.this);
            MutantEndermanEntity.this.setPosition(MutantEndermanEntity.this.prevPosX, MutantEndermanEntity.this.prevPosY, MutantEndermanEntity.this.prevPosZ);
        }

        public boolean shouldContinueExecuting() {
            return (shouldExecute() && MutantEndermanEntity.this.attackTick < 10);
        }

        public void resetTask() {
            MutantEndermanEntity.this.fallDistance = 0.0F;
            MutantEndermanEntity.this.setPosition(MutantEndermanEntity.this.getTeleportPosition().getX() + 0.5D, MutantEndermanEntity.this.getTeleportPosition().getY(), MutantEndermanEntity.this.getTeleportPosition().getZ() + 0.5D);
            MutantEndermanEntity.this.setAttackID(0);
            MutantEndermanEntity.this.dismountRidingEntity();
            MutantEndermanEntity.this.prevPosX = MutantEndermanEntity.this.posX;
            MutantEndermanEntity.this.prevPosY = MutantEndermanEntity.this.posY;
            MutantEndermanEntity.this.prevPosZ = MutantEndermanEntity.this.posZ;
        }
    }

    class TeleSmashGoal extends EntityAIBase {
        private EntityLivingBase attackTarget;

        public TeleSmashGoal() {
            setMutexBits(3);
        }

        public boolean shouldExecute() {
            this.attackTarget = MutantEndermanEntity.this.getAttackTarget();
            return (this.attackTarget != null && MutantEndermanEntity.this.attackID == 7);
        }

        public void startExecuting() {
            MutantEndermanEntity.this.setAttackID(7);
            this.attackTarget.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 5));
            this.attackTarget.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160 + this.attackTarget.getRNG().nextInt(160), 0));
        }

        public boolean shouldContinueExecuting() {
            return (MutantEndermanEntity.this.attackID == 7 && MutantEndermanEntity.this.attackTick < 30);
        }

        public void updateTask() {
            MutantEndermanEntity.this.getNavigator().clearPath();
            if (MutantEndermanEntity.this.attackTick < 20)
                MutantEndermanEntity.this.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
            if (MutantEndermanEntity.this.attackTick == 17) this.attackTarget.dismountRidingEntity();
            if (MutantEndermanEntity.this.attackTick == 18) {
                double x = this.attackTarget.posX + ((this.attackTarget.getRNG().nextFloat() - 0.5F) * 14.0F);
                double y = this.attackTarget.posY + this.attackTarget.getRNG().nextFloat() + ((this.attackTarget instanceof EntityPlayer) ? 13.0D : 7.0D);
                double z = this.attackTarget.posZ + ((this.attackTarget.getRNG().nextFloat() - 0.5F) * 14.0F);
                EntityUtil.sendParticlePacket(this.attackTarget, MBParticles.ENDERSOUL, 256);
                this.attackTarget.setPositionAndUpdate(x, y, z);
                this.attackTarget.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, this.attackTarget.getSoundCategory(), 1.2F, 0.9F + this.attackTarget.getRNG().nextFloat() * 0.2F);
                this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(MutantEndermanEntity.this).setDamageBypassesArmor().setMagicDamage(), 6.0F);
            }
        }

        public void resetTask() {
            MutantEndermanEntity.this.setAttackID(0);
            this.attackTarget = null;
        }
    }

    public class ThrowBlockGoal extends EntityAIBase {
        public boolean shouldExecute() {
            if (MutantEndermanEntity.this.attackID != 0) return false;
            if (!MutantEndermanEntity.this.triggerThrowBlock && MutantEndermanEntity.this.rand.nextInt(28 - MutantEndermanEntity.this.hurtTime) != 0)
                return false;
            if (MutantEndermanEntity.this.getAttackTarget() != null && !MutantEndermanEntity.this.getEntitySenses().canSee(MutantEndermanEntity.this.getAttackTarget()))
                return false;
            int id = MutantEndermanEntity.this.getThrowingHand();
            if (id == -1) return false;
            MutantEndermanEntity.this.setActiveArm(id);
            return true;
        }

        public void startExecuting() {
            MutantEndermanEntity.this.attackTick = 0;
            MutantEndermanEntity.this.setAttackID(2);
            int id = MutantEndermanEntity.this.getActiveArm();
            MutantEndermanEntity.this.world.spawnEntity(new ThrowableBlockEntity(MutantEndermanEntity.this.world, MutantEndermanEntity.this, id));
            MutantEndermanEntity.this.sendHoldBlock(id, 0);
        }

        public boolean shouldContinueExecuting() {
            return (MutantEndermanEntity.this.attackID == 2 && MutantEndermanEntity.this.attackTick < 14);
        }

        public void resetTask() {
            MutantEndermanEntity.this.setAttackID(0);
            MutantEndermanEntity.this.setActiveArm(0);
            MutantEndermanEntity.this.triggerThrowBlock = false;
        }
    }
}

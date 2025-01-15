package chumbanotz.mutantbeasts.entity.mutant;

import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import chumbanotz.mutantbeasts.entity.ai.EntityAIAvoidDamage;
import chumbanotz.mutantbeasts.entity.ai.EntityAIHurtByNearestTarget;
import chumbanotz.mutantbeasts.entity.ai.MBEntityAIAttackMelee;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBSoundEvents;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SpiderPigEntity extends EntityTameable implements IJumpingMount {
    private static final DataParameter<Boolean> CLIMBING = EntityDataManager.createKey(SpiderPigEntity.class, DataSerializers.BOOLEAN);

    private static final Set<Item> TEMPTATION_ITEMS = ImmutableSet.of(Items.CARROT, Items.POTATO, Items.BEETROOT, Items.PORKCHOP, Items.SPIDER_EYE);
    private final List<WebPos> webList = new ArrayList<>(12);
    private int leapCooldown;
    private int leapTick;
    private boolean isLeaping;
    private float chargePower;
    private int chargingTick;
    private int chargeExhaustion;
    private boolean chargeExhausted;

    public SpiderPigEntity(World worldIn) {
        super(worldIn);
        setSize(1.4F, 0.9F);
    }

    public static boolean isPigOrSpider(EntityLivingBase livingEntity) {
        return (livingEntity.getClass() == EntityPig.class || livingEntity.getClass() == EntitySpider.class);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, (new MBEntityAIAttackMelee(this, 1.1D)).setMaxAttackTick(15));
        this.tasks.addTask(2, new LeapAttackGoal());
        this.tasks.addTask(3, new EntityAIAvoidDamage(this, 1.1D));
        this.tasks.addTask(4, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        this.tasks.addTask(5, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(6, new EntityAITempt(this, 1.1D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(7, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(2, new EntityAIHurtByNearestTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, (Predicate<Entity>) entity -> entity instanceof EntityLivingBase && SpiderPigEntity.isPigOrSpider((EntityLivingBase) entity)));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, false);
    }

    public boolean isBesideClimbableBlock() {
        return this.dataManager.get(CLIMBING);
    }

    private void setBesideClimbableBlock(boolean climbing) {
        this.dataManager.set(CLIMBING, climbing);
    }

    public boolean isSaddled() {
        return ((this.dataManager.get(TAMED) & 0x2) != 0);
    }

    private void setSaddled(boolean saddled) {
        byte b0 = this.dataManager.get(TAMED);
        this.dataManager.set(TAMED, saddled ? (byte) (b0 | 0x2) : (byte) (b0 & 0xFFFFFFFD));
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public float getEyeHeight() {
        return this.height * 0.75F;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return (potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn));
    }

    public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
        if (!isChild()) super.setAttackTarget(entitylivingbaseIn);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return TEMPTATION_ITEMS.contains(stack.getItem());
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void onUpdate() {
        super.onUpdate();
        setBesideClimbableBlock(this.collidedHorizontally);
        if (this.chargeExhaustion >= 120) this.chargeExhausted = true;
        if (this.chargeExhaustion <= 0) this.chargeExhausted = false;
        this.chargeExhaustion = Math.max(0, this.chargeExhaustion - 1);
        if (!this.world.isRemote) {
            this.leapCooldown = Math.max(0, this.leapCooldown - 1);
            if (this.leapTick > 10 && this.onGround) this.isLeaping = false;
            updateWebList(false);
            updateChargeState();
            if (isTamed() && this.ticksExisted % 600 == 0) heal(1.0F);
        }
    }

    private void updateWebList(boolean onlyCheckSize) {
        if (!onlyCheckSize) {
            for (int i = 0; i < this.webList.size(); i++) {
                WebPos coord = this.webList.get(i);
                if (this.world.getBlockState(coord).getBlock() != Blocks.WEB) {
                    this.webList.remove(i);
                    i--;
                } else {
                    --coord.timeLeft;
                }
            }
            if (!this.webList.isEmpty()) {
                WebPos first = this.webList.get(0);
                if (first.timeLeft < 0) {
                    this.webList.remove(0);
                    if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) removeWeb(first);
                }
            }
        }
        while (this.webList.size() > 12) {
            WebPos first = this.webList.remove(0);
            removeWeb(first);
        }
    }

    private void removeWeb(BlockPos pos) {
        if (this.world.getBlockState(pos).getBlock() == Blocks.WEB) this.world.destroyBlock(pos, false);
    }

    private void updateChargeState() {
        if (this.chargingTick > 0)
            for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity((Entity) this, getEntityBoundingBox())) {
                if (entity != this && entity.canBeCollidedWith() && !isOnSameTeam(entity)) attackEntityAsMob(entity);
            }
        this.chargingTick = Math.max(0, this.chargingTick - 1);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (isTamed() && isOwner(player)) {
            if (itemstack.getItem() instanceof ItemFood && isBreedingItem(itemstack) && getHealth() < getMaxHealth()) {
                heal(((ItemFood) itemstack.getItem()).getHealAmount(itemstack));
                consumeItemFromStack(player, itemstack);
                return true;
            }
            if (itemstack.getItem() == Items.SADDLE) {
                if (!player.isSneaking() && !isSaddled() && !isChild()) {
                    setSaddled(true);
                    this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                    consumeItemFromStack(player, itemstack);
                    return true;
                }
            } else if (isSaddled() && !isBeingRidden()) {
                if (!player.isSneaking()) {
                    if (!this.world.isRemote) {
                        player.startRiding(this);
                        this.navigator.clearPath();
                    }
                    return true;
                }
                setSaddled(false);
                this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                if (!this.world.isRemote) dropItem(Items.SADDLE, 1);
                return true;
            }
        }
        return super.processInteract(player, hand);
    }

    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        return EntityUtil.shouldAttackEntity(target, owner, false);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.isLeaping = false;
        if (this.rand.nextInt(2) == 0 && ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
            double dx = entityIn.posX - entityIn.prevPosX;
            double dz = entityIn.posZ - entityIn.prevPosZ;
            BlockPos pos = new BlockPos((int) (entityIn.posX + dx * 0.5D), MathHelper.floor((getEntityBoundingBox()).minY), (int) (entityIn.posZ + dz * 0.5D));
            Material material = this.world.getBlockState(pos).getMaterial();
            if (!material.isSolid() && !material.isLiquid() && material != Material.WEB) {
                this.world.setBlockState(pos, Blocks.WEB.getDefaultState());
                this.webList.add(new WebPos(pos, (this.chargingTick > 0) ? 600 : 1200));
                updateWebList(true);
                this.motionY = Math.max(0.25D, this.motionY);
                this.fallDistance = 0.0F;
            }
        }
        float damage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (!(entityIn instanceof EntitySpider) && !(entityIn instanceof SpiderPigEntity) && this.world.isMaterialInBB(entityIn.getEntityBoundingBox(), Material.WEB))
            damage += 4.0F;
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (flag) applyEnchantments(this, entityIn);
        return flag;
    }

    public boolean canJump() {
        return (isSaddled() && !this.chargeExhausted && this.onGround && !this.collidedHorizontally);
    }

    public void setJumpPower(int jumpPowerIn) {
        this.chargeExhaustion += 50 * jumpPowerIn / 100;
        this.chargePower = 1.0F * jumpPowerIn / 100.0F;
    }

    public void handleStartJump(int jumpPowerIn) {
        this.chargingTick = 8 * jumpPowerIn / 100;
    }

    public void handleStopJump() {
    }

    protected boolean isMovementBlocked() {
        return (super.isMovementBlocked() || (isBeingRidden() && isSaddled()));
    }

    @Nullable
    public Entity getControllingPassenger() {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    public boolean canBeSteered() {
        return getControllingPassenger() instanceof EntityLivingBase;
    }

    public void travel(float strafe, float vertical, float forward) {
        if (isBeingRidden() && canBeSteered()) {
            EntityLivingBase livingentity = (EntityLivingBase) getControllingPassenger();
            this.stepHeight = 1.0F;
            this.prevRotationYaw = this.rotationYaw = this.rotationYawHead = livingentity.rotationYaw;
            this.prevRotationPitch = this.rotationPitch = livingentity.rotationPitch * 0.4F;
            setRotation(this.rotationYaw, this.rotationPitch);
            while (this.renderYawOffset > this.rotationYawHead + 180.0F) this.renderYawOffset -= 360.0F;
            while (this.renderYawOffset < this.rotationYawHead - 180.0F) this.renderYawOffset += 360.0F;
            if (!this.chargeExhausted && this.chargePower > 0.0F && (this.onGround || this.collidedHorizontally)) {
                Vec3d lookVec = getLookVec();
                double power = 1.600000023841858D * this.chargePower;
                this.motionX = lookVec.x * power;
                this.motionY = 0.30000001192092896D;
                this.motionZ = lookVec.z * power;
                this.chargePower = 0.0F;
            } else {
                this.chargePower = 0.0F;
            }
            this.jumpMovementFactor = getAIMoveSpeed() * 0.1F;
            if (canPassengerSteer()) {
                strafe = livingentity.moveStrafing * 0.8F;
                forward = livingentity.moveForward * 0.6F;
                setAIMoveSpeed((float) getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafe, vertical, forward);
            } else if (livingentity instanceof EntityPlayer) {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            } else {
                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d1 = this.posX - this.prevPosX;
                double d0 = this.posZ - this.prevPosZ;
                float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
                if (f2 > 1.0F) f2 = 1.0F;
                this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
            }
        } else {
            this.stepHeight = 0.6F;
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        if (!this.world.isRemote) {
            if (entityLivingIn instanceof CreeperMinionEntity && !isTamed()) {
                CreeperMinionEntity minion = (CreeperMinionEntity) entityLivingIn;
                EntityLivingBase owner = minion.getOwner();
                if (owner instanceof EntityPlayer && !ForgeEventFactory.onAnimalTame(this, (EntityPlayer) owner)) {
                    playTameEffect(true);
                    this.world.setEntityState(this, (byte) 7);
                    setTamedBy((EntityPlayer) owner);
                    minion.setDead();
                } else {
                    playTameEffect(false);
                    this.world.setEntityState(this, (byte) 6);
                }
            }
            if (isPigOrSpider(entityLivingIn))
                EntityUtil.convertMobWithNBT(entityLivingIn, new SpiderPigEntity(this.world), false);
        }
    }

    protected boolean canDespawn() {
        return !isTamed();
    }

    public boolean isOnLadder() {
        return isBesideClimbableBlock();
    }

    public void setInWeb() {
    }

    public EntityAgeable createChild(EntityAgeable ageable) {
        if (this.rand.nextInt(20) == 0) return new EntityPig(this.world);
        SpiderPigEntity spiderPig = new SpiderPigEntity(this.world);
        UUID uuid = getOwnerId();
        if (uuid != null) {
            spiderPig.setOwnerId(uuid);
            spiderPig.setTamed(true);
        }
        return spiderPig;
    }

    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        super.dropEquipment(wasRecentlyHit, lootingModifier);
        if (isSaddled()) {
            dropItem(Items.SADDLE, 1);
            setSaddled(false);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.world.isRemote && !this.webList.isEmpty() && ForgeEventFactory.getMobGriefingEvent(this.world, this))
            for (WebPos webPos : this.webList)
                removeWeb(webPos);
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Saddled", isSaddled());
        if (!this.webList.isEmpty()) {
            NBTTagList nbtTagList = new NBTTagList();
            for (WebPos coord : this.webList) {
                NBTTagCompound compound1 = NBTUtil.createPosTag(coord);
                compound1.setInteger("TimeLeft", coord.timeLeft);
                nbtTagList.appendTag(compound1);
            }
            compound.setTag("Webs", nbtTagList);
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setSaddled(compound.getBoolean("Saddled"));
        NBTTagList listnbt = compound.getTagList("Webs", 10);
        for (int i = 0; i < listnbt.tagCount(); i++) {
            NBTTagCompound compound1 = listnbt.getCompoundTagAt(i);
            this.webList.add(i, new WebPos(NBTUtil.getPosFromTag(compound1), compound1.getInteger("TimeLeft")));
        }
    }

    protected SoundEvent getAmbientSound() {
        return MBSoundEvents.ENTITY_SPIDER_PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return MBSoundEvents.ENTITY_SPIDER_PIG_HURT;
    }

    protected SoundEvent getDeathSound() {
        return MBSoundEvents.ENTITY_SPIDER_PIG_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    protected ResourceLocation getLootTable() {
        return EntityUtil.getLootTable(this);
    }

    static class WebPos extends BlockPos {
        private int timeLeft;

        public WebPos(BlockPos pos, int timeLeft) {
            super(pos);
            this.timeLeft = timeLeft;
        }
    }

    class LeapAttackGoal extends EntityAIBase {
        public boolean shouldExecute() {
            EntityLivingBase target = SpiderPigEntity.this.getAttackTarget();
            return (target != null && SpiderPigEntity.this.leapCooldown <= 0 && (SpiderPigEntity.this.onGround || SpiderPigEntity.this.collidedHorizontally || SpiderPigEntity.this.isInWater() || SpiderPigEntity.this.isInLava()) && ((SpiderPigEntity.this.getDistanceSq(target) < 64.0D && SpiderPigEntity.this.rand.nextInt(8) == 0) || SpiderPigEntity.this.getDistanceSq(target) < 6.25D));
        }

        public void startExecuting() {
            SpiderPigEntity.this.isLeaping = true;
            SpiderPigEntity.this.leapCooldown = 15;
            EntityLivingBase target = SpiderPigEntity.this.getAttackTarget();
            double x = target.posX - SpiderPigEntity.this.posX;
            double y = target.posY - SpiderPigEntity.this.posY;
            double z = target.posZ - SpiderPigEntity.this.posZ;
            double d = MathHelper.sqrt(x * x + y * y + z * z);
            double scale = (2.0F + 0.2F * SpiderPigEntity.this.rand.nextFloat() * SpiderPigEntity.this.rand.nextFloat());
            SpiderPigEntity.this.motionX = x / d * scale;
            SpiderPigEntity.this.motionY = y / d * scale * 0.5D + 0.3D;
            SpiderPigEntity.this.motionZ = z / d * scale;
        }

        public boolean shouldContinueExecuting() {
            return (SpiderPigEntity.this.isLeaping && SpiderPigEntity.this.leapTick < 40);
        }

        public void updateTask() {
            ++SpiderPigEntity.this.leapTick;
        }

        public void resetTask() {
            SpiderPigEntity.this.leapTick = 0;
        }
    }
}

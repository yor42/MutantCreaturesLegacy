package chumbanotz.mutantbeasts.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MutantArrowEntity extends Entity {
    private static final DataParameter<Float> TARGET_X = EntityDataManager.createKey(MutantArrowEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> TARGET_Y = EntityDataManager.createKey(MutantArrowEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> TARGET_Z = EntityDataManager.createKey(MutantArrowEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> SPEED = EntityDataManager.createKey(MutantArrowEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Integer> CLONES = EntityDataManager.createKey(MutantArrowEntity.class, DataSerializers.VARINT);
    private final List<Entity> pointedEntities = new ArrayList<>();
    private int damage = 10 + this.rand.nextInt(3);
    private PotionEffect potionEffect;

    private EntityLivingBase shooter;

    public MutantArrowEntity(World world) {
        super(world);
        this.noClip = true;
    }

    public MutantArrowEntity(World world, EntityLivingBase shooter, EntityLivingBase target) {
        this(world);
        this.shooter = shooter;
        if (!world.isRemote) {
            setTargetX(target.posX);
            setTargetY(target.posY);
            setTargetZ(target.posZ);
        }
        double yPos = shooter.posY + shooter.getEyeHeight();
        if (shooter instanceof chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity)
            yPos = shooter.posY + 1.28D;
        setPosition(shooter.posX, yPos, shooter.posZ);
        double x = getTargetX() - this.posX;
        double y = getTargetY() - this.posY;
        double z = getTargetZ() - this.posZ;
        double d = Math.sqrt(x * x + z * z);
        this.rotationYaw = 180.0F + (float) Math.toDegrees(Math.atan2(x, z));
        this.rotationPitch = (float) Math.toDegrees(Math.atan2(y, d));
    }

    protected void entityInit() {
        this.dataManager.register(TARGET_X, 0.0F);
        this.dataManager.register(TARGET_Y, 0.0F);
        this.dataManager.register(TARGET_Z, 0.0F);
        this.dataManager.register(SPEED, 2.0F);
        this.dataManager.register(CLONES, 10);
    }

    public double getTargetX() {
        return this.dataManager.get(TARGET_X);
    }

    public void setTargetX(double x) {
        this.dataManager.set(TARGET_X, (float) x);
    }

    public double getTargetY() {
        return this.dataManager.get(TARGET_Y);
    }

    public void setTargetY(double y) {
        this.dataManager.set(TARGET_Y, (float) y);
    }

    public double getTargetZ() {
        return this.dataManager.get(TARGET_Z);
    }

    public void setTargetZ(double z) {
        this.dataManager.set(TARGET_Z, (float) z);
    }

    public float getSpeed() {
        return this.dataManager.get(SPEED);
    }

    public void setSpeed(float speed) {
        this.dataManager.set(SPEED, speed);
    }

    public int getClones() {
        return this.dataManager.get(CLONES);
    }

    public void setClones(int clones) {
        this.dataManager.set(CLONES, clones);
    }

    public void randomize(float scale) {
        setTargetX(getTargetX() + ((this.rand.nextFloat() - 0.5F) * scale * 2.0F));
        setTargetY(getTargetY() + ((this.rand.nextFloat() - 0.5F) * scale * 2.0F));
        setTargetZ(getTargetZ() + ((this.rand.nextFloat() - 0.5F) * scale * 2.0F));
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setPotionEffect(PotionEffect effect) {
        this.potionEffect = effect;
    }

    public void onUpdate() {
        super.onUpdate();
        double x = getTargetX() - this.posX;
        double y = getTargetY() - this.posY;
        double z = getTargetZ() - this.posZ;
        double d = Math.sqrt(x * x + z * z);
        this.rotationYaw = 180.0F + (float) Math.toDegrees(Math.atan2(x, z));
        if (this.rotationYaw > 360.0F)
            this.rotationYaw -= 360.0F;
        this.rotationPitch = (float) Math.toDegrees(Math.atan2(y, d));
        if (!this.world.isRemote) {
            if (this.ticksExisted == 2)
                hitEntities(0);
            if (this.ticksExisted == 3)
                hitEntities(32);
            if (this.ticksExisted == 4)
                handleEntities();
        }
        if (this.ticksExisted > 10)
            setDead();
    }

    private void hitEntities(int offset) {
        double targetX = getTargetX();
        double targetY = getTargetY();
        double targetZ = getTargetZ();
        double d3 = this.posX - targetX;
        double d4 = this.posY - targetY;
        double d5 = this.posZ - targetZ;
        double dist = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
        double dx = (targetX - this.posX) / dist;
        double dy = (targetY - this.posY) / dist;
        double dz = (targetZ - this.posZ) / dist;
        for (int i = offset; i < offset + 64; i++) {
            double x = this.posX + dx * i * 0.5D;
            double y = this.posY + dy * i * 0.5D;
            double z = this.posZ + dz * i * 0.5D;
            AxisAlignedBB box = (new AxisAlignedBB(x, y, z, x, y, z)).grow(0.3D);
            this.pointedEntities.addAll(this.world.getEntitiesWithinAABBExcludingEntity(this.shooter, box));
        }
    }

    private void handleEntities() {
        this.pointedEntities.remove(this);
        DamageSource damageSource = (new EntityDamageSourceIndirect("arrow", this, this.shooter) {
            public Vec3d getDamageLocation() {
                return null;
            }
        }).setProjectile();
        for (Entity entity : this.pointedEntities) {
            if ((entity instanceof net.minecraft.entity.MultiPartEntityPart && entity.attackEntityFrom(DamageSource.GENERIC.setExplosion(), this.damage)) || (entity.canBeCollidedWith() && entity.attackEntityFrom(damageSource, this.damage))) {
                applyEnchantments(this.shooter, entity);
                this.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ARROW_HIT, getSoundCategory(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                if (this.potionEffect != null && entity instanceof EntityLivingBase)
                    ((EntityLivingBase) entity).addPotionEffect(this.potionEffect);
            }
        }
        this.pointedEntities.clear();
    }

    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false;
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
    }
}

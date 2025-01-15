package chumbanotz.mutantbeasts.entity.projectile;

import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import chumbanotz.mutantbeasts.entity.EndersoulCloneEntity;
import chumbanotz.mutantbeasts.entity.SkullSpiritEntity;
import chumbanotz.mutantbeasts.entity.mutant.*;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.util.MBParticles;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ChemicalXEntity extends EntityThrowable {
    public static final Predicate<Entity> IS_APPLICABLE;
    private static final Map<Class<? extends EntityLiving>, Class<? extends EntityLiving>> MUTATIONS = (Map<Class<? extends EntityLiving>, Class<? extends EntityLiving>>) (new ImmutableMap.Builder())
            .put(EntityCreeper.class, MutantCreeperEntity.class)
            .put(EntityEnderman.class, MutantEndermanEntity.class)
            .put(EntityPig.class, SpiderPigEntity.class)
            .put(EntitySkeleton.class, MutantSkeletonEntity.class)
            .put(EntitySnowman.class, MutantSnowGolemEntity.class)
            .put(EntityZombie.class, MutantZombieEntity.class)
            .build();

    static {
        IS_APPLICABLE = (target -> {
            Class<?> entityClass = target.getClass();
            return (target.isNonBoss() && !MUTATIONS.containsValue(entityClass) && entityClass != CreeperMinionEntity.class && entityClass != EndersoulCloneEntity.class);
        });
    }

    public ChemicalXEntity(World worldIn) {
        super(worldIn);
    }

    public ChemicalXEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public ChemicalXEntity(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Nullable
    public static Class<? extends EntityLiving> getMutantOf(EntityLiving target) {
        Class<?> entityClass = target.getClass();
        if (!MUTATIONS.containsKey(entityClass))
            return null;
        if (entityClass == EntityPig.class && (!target.isPotionActive(MobEffects.UNLUCK) || target.getActivePotionEffect(MobEffects.UNLUCK).getAmplifier() != 13))
            return null;
        if (target instanceof EntityZombie && target.isChild())
            return null;
        return MUTATIONS.get(entityClass);
    }

    protected float getGravityVelocity() {
        return 0.05F;
    }

    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            int i;
            for (i = this.rand.nextInt(5); i < 50; i++) {
                float x = (this.rand.nextFloat() - 0.5F) * 1.2F;
                float y = this.rand.nextFloat() * 0.2F;
                float z = (this.rand.nextFloat() - 0.5F) * 1.2F;
                this.world.spawnParticle(MBParticles.SKULL_SPIRIT, this.posX, this.posY, this.posZ, x, y, z);
            }
            for (i = 5 + this.rand.nextInt(3); i >= 0; i--) {
                float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
                float y = 0.1F + this.rand.nextFloat() * 0.1F;
                float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, x, y, z, Item.getIdFromItem(MBItems.CHEMICAL_X));
            }
        }
    }

    protected void onImpact(RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(result.getBlockPos()).getCollisionBoundingBox(this.world, result.getBlockPos()) == Block.NULL_AABB)
            return;
        if (!this.world.isRemote) {
            EntityLiving target = null;
            if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityLiving && IS_APPLICABLE.test(result.entityHit)) {
                target = (EntityLiving) result.entityHit;
            } else {
                List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, getEntityBoundingBox().grow(12.0D, 8.0D, 12.0D), IS_APPLICABLE);
                if (!list.isEmpty()) {
                    list.sort(new EntityAINearestAttackableTarget.Sorter(this));
                    EntityLiving nearestTarget = list.get(0);
                    if (getDistanceSq(nearestTarget) < 144.0D)
                        target = nearestTarget;
                }
            }
            if (target != null) {
                SkullSpiritEntity spirit = new SkullSpiritEntity(this.world, target);
                spirit.setPosition(this.posX, this.posY, this.posZ);
                this.world.spawnEntity(spirit);
            }
            playSound(SoundEvents.ENTITY_SPLASH_POTION_BREAK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.world.setEntityState(this, (byte) 3);
            setDead();
        }
    }
}

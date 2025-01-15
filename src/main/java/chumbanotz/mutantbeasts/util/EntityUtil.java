package chumbanotz.mutantbeasts.util;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.packet.MBPacketHandler;
import chumbanotz.mutantbeasts.packet.SpawnParticlePacket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;

public class EntityUtil {
    private static final Field FIRE = ObfuscationReflectionHelper.findField(Entity.class, "field_190534_ay");

    public static int getFire(Entity entity) {
        try {
            return FIRE.getInt(entity);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Failed to access fire timer for " + entity, e);
        }
    }

    public static float getHeadAngle(EntityLivingBase livingEntity, double x, double z) {
        return Math.abs(MathHelper.wrapDegrees(livingEntity.rotationYawHead - (float) (Math.atan2(z, x) * 180.0D / Math.PI) + 90.0F));
    }

    public static void spawnLingeringCloud(EntityLivingBase entityLivingBase) {
        Collection<PotionEffect> collection = entityLivingBase.getActivePotionEffects();
        if (!collection.isEmpty()) {
            EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(entityLivingBase.world, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ);
            entityareaeffectcloud.setRadius(2.5F);
            entityareaeffectcloud.setRadiusOnUse(-0.5F);
            entityareaeffectcloud.setWaitTime(10);
            entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
            entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
            for (PotionEffect potioneffect : collection)
                entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
            entityLivingBase.world.spawnEntity(entityareaeffectcloud);
        }
    }

    public static void disableShield(EntityLivingBase entityLivingBase, int ticks) {
        if (entityLivingBase instanceof EntityPlayer && entityLivingBase.isActiveItemStackBlocking()) {
            ((EntityPlayer) entityLivingBase).getCooldownTracker().setCooldown(entityLivingBase.getActiveItemStack().getItem(), ticks);
            entityLivingBase.resetActiveHand();
            entityLivingBase.world.setEntityState(entityLivingBase, (byte) 30);
        }
    }

    public static void knockBackBlockingPlayer(Entity target) {
        if (target instanceof EntityPlayer && ((EntityPlayer) target).isActiveItemStackBlocking())
            target.velocityChanged = true;
    }

    public static void sendPlayerVelocityPacket(Entity entity) {
        if (entity instanceof EntityPlayerMP)
            ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
    }

    public static boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner, boolean canTargetCreepers) {
        if (owner instanceof EntityPlayer) {
            if (target instanceof net.minecraft.entity.monster.EntityCreeper) return canTargetCreepers;
            if (target instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target))
                return false;
            if (target instanceof EntityGolem && !(target instanceof IMob)) return false;
            return (!(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame());
        }
        return true;
    }

    public static void dropExperience(EntityLiving mob, int recentlyHit, Function<EntityPlayer, Integer> experiencePoints, EntityPlayer attackingPlayer) {
        if (!mob.world.isRemote && recentlyHit > 0 && mob.world.getGameRules().getBoolean("doMobLoot")) {
            int i = experiencePoints.apply(attackingPlayer);
            i = ForgeEventFactory.getExperienceDrop(mob, attackingPlayer, i);
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                mob.world.spawnEntity(new EntityXPOrb(mob.world, mob.posX, mob.posY, mob.posZ, j));
            }
        }
    }

    public static EntityLiving convertMobWithNBT(EntityLivingBase oldEntity, EntityLiving newEntity, boolean dropInventory) {
        if (oldEntity.isDead) return null;
        NBTTagCompound copiedNBT = oldEntity.writeToNBT(new NBTTagCompound());
        copiedNBT.setUniqueId("UUID", newEntity.getUniqueID());
        copiedNBT.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(newEntity.getAttributeMap()));
        copiedNBT.setFloat("Health", newEntity.getHealth());
        if (oldEntity.getTeam() != null) copiedNBT.setString("Team", oldEntity.getTeam().getName());
        if (copiedNBT.hasKey("ActiveEffects", 9)) {
            NBTTagList activeEffects = copiedNBT.getTagList("ActiveEffects", 10);
            for (int i = 0; i < activeEffects.tagCount(); i++) {
                NBTTagCompound compound = activeEffects.getCompoundTagAt(i);
                PotionEffect potionEffect = PotionEffect.readCustomPotionEffectFromNBT(compound);
                if (potionEffect != null && !newEntity.isPotionApplicable(potionEffect)) {
                    activeEffects.removeTag(i);
                    i--;
                }
            }
        }
        if (dropInventory && oldEntity.world.getGameRules().getBoolean("doMobLoot")) {
            copiedNBT.setBoolean("CanPickUpLoot", false);
            if (copiedNBT.hasKey("ArmorItems", 9)) {
                NBTTagList armorItems = copiedNBT.getTagList("ArmorItems", 10);
                NBTTagList armorDropChances = copiedNBT.getTagList("ArmorDropChances", 5);
                for (int i = 0; i < armorItems.tagCount(); i++) {
                    ItemStack itemStack = new ItemStack(armorItems.getCompoundTagAt(i));
                    if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack) && armorDropChances.getFloatAt(i) > 1.0F)
                        oldEntity.entityDropItem(itemStack, 0.0F);
                }
                copiedNBT.setTag("ArmorItems", new NBTTagList());
                copiedNBT.setTag("ArmorDropChances", new NBTTagList());
            }
            if (copiedNBT.hasKey("HandItems", 9)) {
                NBTTagList handItems = copiedNBT.getTagList("HandItems", 10);
                NBTTagList handDropChances = copiedNBT.getTagList("HandDropChances", 5);
                for (int i = 0; i < handItems.tagCount(); i++) {
                    ItemStack itemStack = new ItemStack(handItems.getCompoundTagAt(i));
                    if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack) && handDropChances.getFloatAt(i) > 1.0F)
                        oldEntity.entityDropItem(itemStack, 0.0F);
                    copiedNBT.setTag("HandItems", new NBTTagList());
                    copiedNBT.setTag("HandDropChances", new NBTTagList());
                }
            }
            if (oldEntity instanceof net.minecraft.entity.monster.EntityEnderman) {
                IBlockState iblockstate = Block.getBlockById(copiedNBT.getShort("carried")).getStateFromMeta(copiedNBT.getShort("carriedData") & 0xFFFF);
                if (iblockstate != null && iblockstate.getBlock() != null && iblockstate.getMaterial() != Material.AIR) {
                    Item item = Item.getItemFromBlock(iblockstate.getBlock());
                    int i = item.getHasSubtypes() ? iblockstate.getBlock().getMetaFromState(iblockstate) : 0;
                    oldEntity.entityDropItem(new ItemStack(item, 1, i), 0.0F);
                }
            }
        }
        newEntity.readFromNBT(copiedNBT);
        oldEntity.world.spawnEntity(newEntity);
        oldEntity.setDead();
        return newEntity;
    }

    public static ResourceLocation getLootTable(Entity entity) {
        return MutantBeasts.prefix("entities/" + EntityList.getKey(entity).getPath());
    }

    public static void spawnParticleAtEntity(EntityLivingBase entity, EnumParticleTypes particleType, int amount, int... parameters) {
        if (entity.world.isRemote) for (int i = 0; i < amount; i++) {
            double posX = entity.posX + (entity.getRNG().nextFloat() * entity.width * 2.0F) - entity.width;
            double posY = entity.posY + 0.5D + (entity.getRNG().nextFloat() * entity.height);
            double posZ = entity.posZ + (entity.getRNG().nextFloat() * entity.width * 2.0F) - entity.width;
            double x = entity.getRNG().nextGaussian() * 0.02D;
            double y = entity.getRNG().nextGaussian() * 0.02D;
            double z = entity.getRNG().nextGaussian() * 0.02D;
            entity.world.spawnParticle(particleType, posX, posY, posZ, x, y, z, parameters);
        }
    }

    public static void spawnEndersoulParticles(Entity entity, int amount, float speed) {
        for (int i = 0; i < amount; i++) {
            float f = (entity.world.rand.nextFloat() - 0.5F) * speed;
            float f1 = (entity.world.rand.nextFloat() - 0.5F) * speed;
            float f2 = (entity.world.rand.nextFloat() - 0.5F) * speed;
            double tempX = entity.posX + ((entity.world.rand.nextFloat() - 0.5F) * entity.width);
            double tempY = entity.posY + ((entity.world.rand.nextFloat() - 0.5F) * entity.height) + 0.5D;
            double tempZ = entity.posZ + ((entity.world.rand.nextFloat() - 0.5F) * entity.width);
            entity.world.spawnParticle(MBParticles.ENDERSOUL, tempX, tempY, tempZ, f, f1, f2);
        }
    }

    public static void sendParticlePacket(Entity entity, EnumParticleTypes particleType, int amount) {
        MBPacketHandler.INSTANCE.sendToAllAround(new SpawnParticlePacket(particleType, entity.posX, entity.posY, entity.posZ, entity.width, entity.height, entity.width, amount), new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 1024.0D));
    }

    public static Vec3d getDirVector(float rotation, float scale) {
        float rad = rotation * 0.017453292F;
        return new Vec3d((-MathHelper.sin(rad) * scale), 0.0D, (MathHelper.cos(rad) * scale));
    }

    public static boolean teleportTo(EntityLiving mob, double x, double y, double z) {
        BlockPos.MutableBlockPos pos = (new BlockPos.MutableBlockPos()).setPos(x, y, z);
        if (mob.world.isBlockLoaded(pos)) {
            do {
                pos.move(EnumFacing.DOWN);
            } while (pos.getY() > 0 && !mob.world.getBlockState(pos).getMaterial().blocksMovement());
            pos.move(EnumFacing.UP);
        }
        if (!mob.isOffsetPositionInLiquid((pos.getX() - MathHelper.floor(mob.posX)), (pos.getY() - MathHelper.floor(mob.posY)), (pos.getZ() - MathHelper.floor(mob.posZ))))
            return false;
        mob.moveToBlockPosAndAngles(pos, mob.rotationYaw, mob.rotationPitch);
        mob.getNavigator().clearPath();
        return true;
    }

    public static void divertAttackers(EntityLiving targetedMob, EntityLivingBase newTarget) {
        for (EntityLiving mob : targetedMob.world.getEntitiesWithinAABB(EntityLiving.class, targetedMob.getEntityBoundingBox().grow(16.0D, 10.0D, 16.0D))) {
            if (mob == targetedMob || mob == newTarget) continue;
            if (mob.getAttackTarget() == targetedMob) mob.setAttackTarget(newTarget);
        }
    }
}

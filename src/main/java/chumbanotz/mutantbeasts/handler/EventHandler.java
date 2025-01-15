package chumbanotz.mutantbeasts.handler;

import chumbanotz.mutantbeasts.entity.EndersoulFragmentEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import chumbanotz.mutantbeasts.entity.mutant.SpiderPigEntity;
import chumbanotz.mutantbeasts.item.HulkHammerItem;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import chumbanotz.mutantbeasts.util.EntityUtil;
import chumbanotz.mutantbeasts.util.MBParticles;
import chumbanotz.mutantbeasts.util.SeismicWave;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

@EventBusSubscriber(modid = Tags.MOD_ID)
public class EventHandler {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getWorld()).isRemote && event.getEntity() instanceof EntityCreature) {
            EntityCreature creature = (EntityCreature) event.getEntity();
            if (creature instanceof net.minecraft.entity.passive.EntityOcelot)
                creature.tasks.addTask(2, new EntityAIAvoidEntity(creature, MutantCreeperEntity.class, 16.0F, 0.8D, 1.33D));
            if (creature instanceof net.minecraft.entity.passive.EntityVillager)
                creature.tasks.addTask(1, new EntityAIAvoidEntity(creature, MutantZombieEntity.class, 12.0F, 0.8D, 0.8D));
            if (creature.getClass() == EntityPig.class)
                creature.tasks.addTask(2, new EntityAITempt(creature, 1.0D, Items.FERMENTED_SPIDER_EYE, false));
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
        if (event.getTarget().getClass() == EntityPig.class && !((EntityPig) event.getTarget()).isPotionActive(MobEffects.UNLUCK) && stack.getItem() == Items.FERMENTED_SPIDER_EYE) {
            if (!event.getEntityPlayer().isCreative()) stack.shrink(1);
            ((EntityPig) event.getTarget()).addPotionEffect(new PotionEffect(MobEffects.UNLUCK, 600, 13));
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (SpiderPigEntity.isPigOrSpider(event.getEntityLiving()) && event.getSource().getTrueSource() instanceof SpiderPigEntity)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingUseItem(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == MBItems.MUTANT_SKELETON_CHESTPLATE && event.getItem().getItemUseAction() == EnumAction.BOW && event.getDuration() > 4)
            event.setDuration(event.getDuration() - 3);
    }

    @SubscribeEvent
    public static void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote && !HulkHammerItem.WAVES.isEmpty() && HulkHammerItem.WAVES.containsKey(event.player.getUniqueID())) {
            EntityPlayer player = event.player;
            List<SeismicWave> waveList = HulkHammerItem.WAVES.get(player.getUniqueID());
            while (waveList.size() > 16) waveList.remove(0);
            SeismicWave wave = waveList.remove(0);
            wave.affectBlocks(player.world, player);
            AxisAlignedBB box = new AxisAlignedBB(wave.getX(), (wave.getY() + 1), wave.getZ(), (wave.getX() + 1), (wave.getY() + 2), (wave.getZ() + 1));
            for (Entity entity : player.world.getEntitiesWithinAABBExcludingEntity(player, box)) {
                if (entity.canBeCollidedWith() && player.getRidingEntity() != entity && entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (6 + player.getRNG().nextInt(3)))) {
                    if (entity instanceof EntityLivingBase)
                        EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, player);
                    EnchantmentHelper.applyArthropodEnchantments(player, entity);
                }
            }
            if (waveList.isEmpty()) HulkHammerItem.WAVES.remove(player.getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerShootArrow(ArrowLooseEvent event) {
        if (!(event.getWorld()).isRemote && event.getEntityPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == MBItems.MUTANT_SKELETON_SKULL && event.hasAmmo()) {
            event.setCanceled(true);
            EntityPlayer player = event.getEntityPlayer();
            World world = event.getWorld();
            ItemStack bow = event.getBow();
            ItemStack arrowStack = findAmmo(player);
            boolean inAir = (!player.onGround && !player.isInWater() && !player.isInLava());
            if (arrowStack.isEmpty()) arrowStack = new ItemStack(Items.ARROW);
            float velocity = ItemBow.getArrowVelocity(event.getCharge());
            boolean infiniteArrow = (player.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow && ((ItemArrow) arrowStack.getItem()).isInfinite(arrowStack, bow, player)));
            ItemArrow itemarrow = (arrowStack.getItem() instanceof ItemArrow) ? (ItemArrow) arrowStack.getItem() : (ItemArrow) Items.ARROW;
            EntityArrow entityarrow = itemarrow.createArrow(world, arrowStack, player);
            entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 2.0F, 1.0F);
            if (velocity == 1.0F && inAir) entityarrow.setIsCritical(true);
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);
            if (j > 0) entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);
            if (k > 0) entityarrow.setKnockbackStrength(k);
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) entityarrow.setFire(100);
            entityarrow.setDamage(entityarrow.getDamage() * (inAir ? 2.0D : 0.5D));
            bow.damageItem(1, player);
            if (infiniteArrow || (player.capabilities.isCreativeMode && (arrowStack.getItem() == Items.SPECTRAL_ARROW || arrowStack.getItem() == Items.TIPPED_ARROW)))
                entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
            world.spawnEntity(entityarrow);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
            if (!infiniteArrow && !player.capabilities.isCreativeMode) {
                arrowStack.shrink(1);
                if (arrowStack.isEmpty()) player.inventory.deleteStack(arrowStack);
            }
            player.addStat(StatList.getObjectUseStats(bow.getItem()));
        }
    }

    private static ItemStack findAmmo(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemArrow)
            return player.getHeldItem(EnumHand.OFF_HAND);
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemArrow)
            return player.getHeldItem(EnumHand.MAIN_HAND);
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack itemstack = player.inventory.getStackInSlot(i);
            if (itemstack.getItem() instanceof ItemArrow) return itemstack;
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        World world = (event.getPlayer()).world;
        EntityPlayer player = event.getPlayer();
        if (!world.isRemote) {
            ItemStack stack = event.getEntityItem().getItem();
            boolean isHand = (stack.getItem() == MBItems.ENDERSOUL_HAND && stack.isItemDamaged());
            if (stack.getItem() == Items.ENDER_EYE || isHand) {
                int count = 0;
                for (EndersoulFragmentEntity orb : world.getEntitiesWithinAABB(EndersoulFragmentEntity.class, player.getEntityBoundingBox().grow(8.0D))) {
                    if (orb.isEntityAlive() && orb.getOwner() == player) {
                        count++;
                        orb.setDead();
                    }
                }
                if (count > 0) {
                    EntityUtil.sendParticlePacket(player, MBParticles.ENDERSOUL, 256);
                    int addDmg = count * 60;
                    if (isHand) {
                        int dmg = stack.getItemDamage() - addDmg;
                        stack.setItemDamage(Math.max(dmg, 0));
                    } else {
                        ItemStack newStack = new ItemStack(MBItems.ENDERSOUL_HAND);
                        newStack.setItemDamage(MBItems.ENDERSOUL_HAND.getMaxDamage(stack) - addDmg);
                        event.getEntityItem().setItem(newStack);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> (entity instanceof EntityItem && ((EntityItem) entity).getItem().getItem() == MBItems.CREEPER_SHARD));
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MOD_ID)) ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
    }
}

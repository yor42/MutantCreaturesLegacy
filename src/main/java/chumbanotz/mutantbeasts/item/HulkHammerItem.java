package chumbanotz.mutantbeasts.item;

import chumbanotz.mutantbeasts.util.SeismicWave;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HulkHammerItem
extends Item {
    public static final Map<UUID, List<SeismicWave>> WAVES = new HashMap<UUID, List<SeismicWave>>();

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.MAINHAND;
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return true;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (state.getBlockHardness(worldIn, pos) != 0.0f) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    public int getItemEnchantability() {
        return 10;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.type == EnumEnchantmentType.WEAPON && enchantment != Enchantments.SWEEPING;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack heldItemStack = playerIn.getHeldItem(handIn);
        RayTraceResult result = this.rayTrace(worldIn, playerIn, true);
        if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK || result.sideHit.getOpposite() != EnumFacing.DOWN) {
            return new ActionResult(EnumActionResult.PASS, heldItemStack);
        }
        if (!worldIn.isRemote) {
            ArrayList<SeismicWave> list = new ArrayList<SeismicWave>();
            Vec3d vec = Vec3d.fromPitchYaw((float)0.0f, (float)playerIn.rotationYaw);
            int x = MathHelper.floor((double)(playerIn.posX + vec.x * 1.5));
            int y = MathHelper.floor((double)playerIn.getEntityBoundingBox().minY);
            int z = MathHelper.floor((double)(playerIn.posZ + vec.z * 1.5));
            int x1 = MathHelper.floor((double)(playerIn.posX + vec.x * 8.0));
            int z1 = MathHelper.floor((double)(playerIn.posZ + vec.z * 8.0));
            SeismicWave.createWaves(worldIn, list, x, z, x1, z1, y);
            HulkHammerItem.addWave(playerIn.getUniqueID(), list);
        }
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8f, 0.8f + playerIn.getRNG().nextFloat() * 0.4f);
        playerIn.getCooldownTracker().setCooldown(this, 25);
        playerIn.swingArm(handIn);
        playerIn.addStat(StatList.getObjectUseStats((Item)this));
        heldItemStack.damageItem(1, playerIn);
        return new ActionResult(EnumActionResult.SUCCESS, heldItemStack);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 8.0, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.0, 0));
        }
        return multimap;
    }

    public static void addWave(UUID name, List<SeismicWave> list) {
        List<SeismicWave> waves = null;
        Iterator<List<SeismicWave>> iterator = WAVES.values().iterator();
        while (iterator.hasNext()) {
            List<SeismicWave> waves1;
            waves = waves1 = iterator.next();
        }
        if (waves == null) {
            WAVES.put(name, list);
        } else {
            waves.addAll(list);
            WAVES.put(name, waves);
        }
    }
}

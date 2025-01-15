package chumbanotz.mutantbeasts.item;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class MutantSkeletonArmorItem extends ItemArmor {
    private static final ItemArmor.ArmorMaterial MUTANT_SKELETON = EnumHelper.addArmorMaterial("mutant_skeleton", Tags.MOD_ID + ":mutant_skeleton", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);

    public MutantSkeletonArmorItem(EntityEquipmentSlot equipmentSlotIn) {
        super(MUTANT_SKELETON, 0, equipmentSlotIn);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return (stack.getItem() == MBItems.MUTANT_SKELETON_SKULL) ? EnumRarity.UNCOMMON : super.getRarity(stack);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (this.armorType == EntityEquipmentSlot.LEGS && !player.isPotionActive(MobEffects.SPEED))
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1, 1, false, false));
        if (this.armorType == EntityEquipmentSlot.FEET && !player.isPotionActive(MobEffects.JUMP_BOOST))
            player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 1, player.isSprinting() ? 1 : 0, false, false));
    }

    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return (armorSlot == EntityEquipmentSlot.HEAD) ? (ModelBiped) MutantBeasts.PROXY.getMutantSkeletonArmorModel() : _default;
    }
}

package chumbanotz.mutantbeasts;

import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import chumbanotz.mutantbeasts.packet.MBPacketHandler;
import chumbanotz.mutantbeasts.util.IProxy;
import chumbanotz.mutantbeasts.util.MBParticles;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class MutantBeasts {
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_ID);
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID) {
        public ItemStack createIcon() {
            return new ItemStack(MBItems.CHEMICAL_X);
        }

        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values()) {
                if (entitylist$entityegginfo.spawnedID.getNamespace().equals(Tags.MOD_ID)) {
                    ItemStack itemstack = new ItemStack(Items.SPAWN_EGG, 1);
                    ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                    items.add(itemstack);
                }
            }
        }
    };
    @Instance(Tags.MOD_ID)
    public static MutantBeasts INSTANCE;
    @SidedProxy(clientSide = "chumbanotz.mutantbeasts.proxy.ClientProxy", serverSide = "chumbanotz.mutantbeasts.proxy.ServerProxy")
    public static IProxy PROXY;

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(Tags.MOD_ID, name);
    }

    public static ResourceLocation getEntityTexture(String name) {
        return prefix("textures/entity/" + name + ".png");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit();
        MBParticles.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, PROXY);
        ItemStack input = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD);
        ItemStack chemicalX = new ItemStack(MBItems.CHEMICAL_X);
        for (Item item : new Item[]{MBItems.CREEPER_SHARD, MBItems.ENDERSOUL_HAND, MBItems.HULK_HAMMER, MBItems.MUTANT_SKELETON_SKULL})
            BrewingRecipeRegistry.addRecipe(input, new ItemStack(item), chemicalX);
        MBPacketHandler.register();
    }
}

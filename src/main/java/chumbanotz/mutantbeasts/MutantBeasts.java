package chumbanotz.mutantbeasts;

import chumbanotz.mutantbeasts.compat.MBCompatHandler;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.packet.MBPacketHandler;
import chumbanotz.mutantbeasts.util.IProxy;
import chumbanotz.mutantbeasts.util.MBParticles;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "mutantbeasts", name = "Mutant Creatures Legacy", version = "1.12.2-1.0.3", acceptedMinecraftVersions = "[1.12.2]", dependencies = "required-after:minecraft;required-after:forge@[14.23.5.2779,);")
public class MutantBeasts {
    public static final String MOD_ID = "mutantbeasts";
    public static final String MOD_PREFIX = MOD_ID + ":";
    @Mod.Instance(value = "mutantbeasts")
    public static MutantBeasts INSTANCE;
    @SidedProxy(clientSide = "chumbanotz.mutantbeasts.client.ClientProxy", serverSide = "chumbanotz.mutantbeasts.ServerProxy")
    public static IProxy PROXY;
    public static final Logger LOGGER;
    public static final CreativeTabs CREATIVE_TAB;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit();
        MBParticles.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, PROXY);
        MBCompatHandler.init();
        MBPacketHandler.register();
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static ResourceLocation getEntityTexture(String name) {
        return MutantBeasts.prefix("textures/entity/" + name + ".png");
    }

    static {
        LOGGER = LogManager.getLogger(MOD_ID);
        CREATIVE_TAB = new CreativeTabs(MOD_ID) {

            public ItemStack createIcon() {
                return new ItemStack(MBItems.CHEMICAL_X);
            }

            public void displayAllRelevantItems(NonNullList<ItemStack> items) {
                super.displayAllRelevantItems(items);
                for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values()) {
                    if (!entitylist$entityegginfo.spawnedID.getNamespace().equals(MutantBeasts.MOD_ID)) continue;
                    ItemStack itemstack = new ItemStack(Items.SPAWN_EGG, 1);
                    ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                    items.add(itemstack);
                }
            }
        };
    }
}

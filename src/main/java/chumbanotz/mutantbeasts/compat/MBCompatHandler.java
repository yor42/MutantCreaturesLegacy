package chumbanotz.mutantbeasts.compat;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.compat.thaumcraft.MBThaumcraftPlugin;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MutantBeasts.MOD_ID)
public class MBCompatHandler {
    public static void preInit() {
    }

    public static void init() {
        if (Loader.isModLoaded("thaumcraft")) {
            MinecraftForge.EVENT_BUS.register(MBThaumcraftPlugin.class);
        }
    }

    public static void postInit() {
    }
}

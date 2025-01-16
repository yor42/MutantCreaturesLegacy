package chumbanotz.mutantbeasts.client;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.EndersoulHandModel;
import chumbanotz.mutantbeasts.item.MBItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid="mutantbeasts", value={Side.CLIENT})
public class ClientEventHandler {
    public static TextureAtlasSprite SKULL_SPIRIT_PARTICLE_SPIRTE;

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        SKULL_SPIRIT_PARTICLE_SPIRTE = event.getMap().registerSprite(MutantBeasts.prefix("particle/skull_spirit"));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ClientEventHandler.registerItemModel(MBItems.CHEMICAL_X);
        ClientEventHandler.registerItemModel(MBItems.CREEPER_MINION_TRACKER);
        ClientEventHandler.registerItemModel(MBItems.CREEPER_SHARD);
        ClientEventHandler.registerItemModel(MBItems.ENDERSOUL_HAND);
        ClientEventHandler.registerItemModel(MBItems.HULK_HAMMER);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_ARMS);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_BOOTS);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_CHESTPLATE);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_LEGGINGS);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_LIMB);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_PELVIS);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_RIB);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_RIB_CAGE);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_SHOULDER_PAD);
        ClientEventHandler.registerItemModel(MBItems.MUTANT_SKELETON_SKULL);
        ModelBakery.registerItemVariants(MBItems.ENDERSOUL_HAND, EndersoulHandModel.GUI_LOCATION, EndersoulHandModel.MODEL_LOCATION);
        ModelLoaderRegistry.registerLoader(EndersoulHandModel.Loader.INSTANCE);
    }

    private static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}

package chumbanotz.mutantbeasts.client;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.EndersoulHandModel;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Tags.MOD_ID, value = {Side.CLIENT})
public class ClientEventHandler {
    public static TextureAtlasSprite SKULL_SPIRIT_PARTICLE_SPIRTE;

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        SKULL_SPIRIT_PARTICLE_SPIRTE = event.getMap().registerSprite(MutantBeasts.prefix("particle/skull_spirit"));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        registerItemModel(MBItems.CHEMICAL_X);
        registerItemModel(MBItems.CREEPER_MINION_TRACKER);
        registerItemModel(MBItems.CREEPER_SHARD);
        registerItemModel(MBItems.ENDERSOUL_HAND);
        registerItemModel(MBItems.HULK_HAMMER);
        registerItemModel(MBItems.MUTANT_SKELETON_ARMS);
        registerItemModel(MBItems.MUTANT_SKELETON_BOOTS);
        registerItemModel(MBItems.MUTANT_SKELETON_CHESTPLATE);
        registerItemModel(MBItems.MUTANT_SKELETON_LEGGINGS);
        registerItemModel(MBItems.MUTANT_SKELETON_LIMB);
        registerItemModel(MBItems.MUTANT_SKELETON_PELVIS);
        registerItemModel(MBItems.MUTANT_SKELETON_RIB);
        registerItemModel(MBItems.MUTANT_SKELETON_RIB_CAGE);
        registerItemModel(MBItems.MUTANT_SKELETON_SHOULDER_PAD);
        registerItemModel(MBItems.MUTANT_SKELETON_SKULL);
        ModelBakery.registerItemVariants(MBItems.ENDERSOUL_HAND, EndersoulHandModel.GUI_LOCATION, EndersoulHandModel.MODEL_LOCATION);
        ModelLoaderRegistry.registerLoader(EndersoulHandModel.Loader.INSTANCE);
    }

    private static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}

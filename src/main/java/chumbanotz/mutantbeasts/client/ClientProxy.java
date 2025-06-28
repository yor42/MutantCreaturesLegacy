package chumbanotz.mutantbeasts.client;

import chumbanotz.mutantbeasts.MBConfig;
import chumbanotz.mutantbeasts.client.gui.CreeperMinionTrackerScreen;
import chumbanotz.mutantbeasts.client.model.MutantSkeletonArmorModel;
import chumbanotz.mutantbeasts.client.particle.EndersoulParticle;
import chumbanotz.mutantbeasts.client.particle.SkullSpiritParticle;
import chumbanotz.mutantbeasts.client.renderer.entity.*;
import chumbanotz.mutantbeasts.client.renderer.entity.layers.MBEntityLayerOnShoulder;
import chumbanotz.mutantbeasts.entity.*;
import chumbanotz.mutantbeasts.entity.mutant.*;
import chumbanotz.mutantbeasts.entity.projectile.ChemicalXEntity;
import chumbanotz.mutantbeasts.entity.projectile.MutantArrowEntity;
import chumbanotz.mutantbeasts.entity.projectile.ThrowableBlockEntity;
import chumbanotz.mutantbeasts.item.MBItems;
import chumbanotz.mutantbeasts.util.IProxy;
import chumbanotz.mutantbeasts.util.MBParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

public class ClientProxy
implements IProxy {
    private static final MutantSkeletonArmorModel MUTANT_SKELETON_ARMOR_MODEL = new MutantSkeletonArmorModel();

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(BodyPartEntity.class, BodyPartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ChemicalXEntity.class, manager -> new RenderSnowball<>(manager, MBItems.CHEMICAL_X, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(CreeperMinionEntity.class, CreeperMinionRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CreeperMinionEggEntity.class, CreeperMinionEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EndersoulCloneEntity.class, EndersoulCloneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EndersoulFragmentEntity.class, EndersoulFragmentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MutantArrowEntity.class, MutantArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MutantCreeperEntity.class, MutantCreeperRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MutantEndermanEntity.class, MutantEndermanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MutantSkeletonEntity.class, MutantSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MutantSnowGolemEntity.class, MutantSnowGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MutantZombieEntity.class, MutantZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SkullSpiritEntity.class, manager -> new Render<SkullSpiritEntity>(manager){

            protected ResourceLocation getEntityTexture(SkullSpiritEntity entity) {
                return null;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(SpiderPigEntity.class, SpiderPigRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ThrowableBlockEntity.class, ThrowableBlockRenderer::new);
        ClientRegistry.registerEntityShader(CreeperMinionEntity.class, new ResourceLocation("shaders/post/creeper.json"));
        ClientRegistry.registerEntityShader(EndersoulCloneEntity.class, new ResourceLocation("shaders/post/invert.json"));
        ClientRegistry.registerEntityShader(MutantEndermanEntity.class, new ResourceLocation("shaders/post/invert.json"));
    }

    @Override
    public void init() {
        MBItems.ENDERSOUL_HAND.setTileEntityItemStackRenderer(new MBTileEntityItemStackRenderer());
        Minecraft.getMinecraft().effectRenderer.registerParticle(MBParticles.ENDERSOUL.getParticleID(), new EndersoulParticle.Factory());
        Minecraft.getMinecraft().effectRenderer.registerParticle(MBParticles.SKULL_SPIRIT.getParticleID(), new SkullSpiritParticle.Factory());
        if (MBConfig.ENTITIES.creeperMinionOnShoulder) {
            for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
                List playerLayerRenderers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, "field_177097_h");
                playerLayerRenderers.removeIf(LayerEntityOnShoulder.class::isInstance);
                renderPlayer.addLayer(new MBEntityLayerOnShoulder());
            }
        }
    }

    @Override
    public World getWorldClient() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public Object getMutantSkeletonArmorModel() {
        return MUTANT_SKELETON_ARMOR_MODEL;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return ID == 0 ? new CreeperMinionTrackerScreen((CreeperMinionEntity)world.getEntityByID(x)) : null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}

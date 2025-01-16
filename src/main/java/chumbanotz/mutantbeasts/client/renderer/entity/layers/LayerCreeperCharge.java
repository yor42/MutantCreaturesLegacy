package chumbanotz.mutantbeasts.client.renderer.entity.layers;

import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import chumbanotz.mutantbeasts.entity.mutant.MutantCreeperEntity;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LayerCreeperCharge<T extends EntityLiving>
implements LayerRenderer<T> {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderLiving<T> renderer;
    private final ModelBase model;

    public LayerCreeperCharge(RenderLiving<T> creeperRenderer, ModelBase model) {
        this.renderer = creeperRenderer;
        this.model = model;
    }

    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof MutantCreeperEntity && ((MutantCreeperEntity)entitylivingbaseIn).getPowered() || entitylivingbaseIn instanceof CreeperMinionEntity && ((CreeperMinionEntity)entitylivingbaseIn).getPowered()) {
            LayerCreeperCharge.render(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, this.renderer.getMainModel(), this.model);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public static void render(@Nullable Entity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, ModelBase mainModel, ModelBase chargedModel) {
        GlStateManager.depthMask((entityIn == null || !entityIn.isInvisible() ? 1 : 0) != 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(LIGHTNING_TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.translate(ageInTicks * 0.01f, ageInTicks * 0.01f, 0.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        chargedModel.setModelAttributes(mainModel);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        chargedModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
}

package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantSnowGolemModel;
import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class MutantSnowGolemRenderer
extends RenderLiving<MutantSnowGolemEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_snow_golem/mutant_snow_golem");
    private static final ResourceLocation PUMPKIN_TEXTURE = MutantBeasts.getEntityTexture("mutant_snow_golem/pumpkin");
    private static final ResourceLocation GLOW_TEXTURE = MutantBeasts.getEntityTexture("mutant_snow_golem/glow");

    public MutantSnowGolemRenderer(RenderManager manager) {
        super(manager, new MutantSnowGolemModel(), 0.7f);
        this.addLayer(new PumpkinLayer());
        this.addLayer(new GlowLayer());
        this.addLayer(new HeldBlockLayer());
    }

    public void renderName(MutantSnowGolemEntity entity, double x, double y, double z) {
        super.renderName(entity, x, y, z);
        if (entity.getOwner() != null) {
            ITextComponent textComponent = entity.getOwner().getDisplayName();
            textComponent.getStyle().setItalic(true);
            if (this.canRenderName(entity)) {
                y += (float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15f * 0.025f;
            }
            super.renderEntityName(entity, x, y, z, textComponent.getFormattedText(), NAME_TAG_RANGE);
        }
    }

    public MutantSnowGolemModel getMainModel() {
        return (MutantSnowGolemModel)super.getMainModel();
    }

    protected ResourceLocation getEntityTexture(MutantSnowGolemEntity entity) {
        return TEXTURE;
    }

    class HeldBlockLayer
    implements LayerRenderer<MutantSnowGolemEntity> {
        HeldBlockLayer() {
        }

        public void doRenderLayer(MutantSnowGolemEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isEntityAlive() && entityIn.isThrowing() && entityIn.getThrowingTick() < 7) {
                GlStateManager.enableRescaleNormal();
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.4f, 0.0f, 0.0f);
                MutantSnowGolemRenderer.this.getMainModel().postRenderArm(0.0625f);
                GlStateManager.translate(0.0f, 0.9f, 0.0f);
                GlStateManager.scale(-0.8f, -0.8f, 0.8f);
                int i = entityIn.getBrightnessForRender();
                int j = i % 65536;
                int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                MutantSnowGolemRenderer.this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.translate(-0.5f, -0.5f, 0.5f);
                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.ICE.getDefaultState(), 1.0f);
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }

    class GlowLayer
    implements LayerRenderer<MutantSnowGolemEntity> {
        GlowLayer() {
        }

        public void doRenderLayer(MutantSnowGolemEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isPumpkinEquipped()) {
                MutantSnowGolemRenderer.this.bindTexture(GLOW_TEXTURE);
                GlStateManager.disableLighting();
                GlStateManager.depthMask((!entityIn.isInvisible() ? 1 : 0) != 0);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0f, 0.0f);
                float f1 = MathHelper.cos(ageInTicks * 0.1f);
                float f2 = MathHelper.cos(ageInTicks * 0.15f);
                GlStateManager.color(1.0f, 0.8f + 0.05f * f2, 0.15f + 0.2f * f1, 1.0f);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                MutantSnowGolemRenderer.this.mainModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                MutantSnowGolemRenderer.this.setLightmap(entityIn);
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }

    class PumpkinLayer
    implements LayerRenderer<MutantSnowGolemEntity> {
        PumpkinLayer() {
        }

        public void doRenderLayer(MutantSnowGolemEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isPumpkinEquipped() && !entityIn.isInvisible()) {
                MutantSnowGolemRenderer.this.bindTexture(PUMPKIN_TEXTURE);
                MutantSnowGolemRenderer.this.getMainModel().render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }

        public boolean shouldCombineTextures() {
            return true;
        }
    }
}

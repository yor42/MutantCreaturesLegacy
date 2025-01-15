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

public class MutantSnowGolemRenderer extends RenderLiving<MutantSnowGolemEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_snow_golem/mutant_snow_golem");

    private static final ResourceLocation PUMPKIN_TEXTURE = MutantBeasts.getEntityTexture("mutant_snow_golem/pumpkin");

    private static final ResourceLocation GLOW_TEXTURE = MutantBeasts.getEntityTexture("mutant_snow_golem/glow");

    public MutantSnowGolemRenderer(RenderManager manager) {
        super(manager, new MutantSnowGolemModel(), 0.7F);
        addLayer(new PumpkinLayer());
        addLayer(new GlowLayer());
        addLayer(new HeldBlockLayer());
    }

    public void renderName(MutantSnowGolemEntity entity, double x, double y, double z) {
        super.renderName(entity, x, y, z);
        if (entity.getOwner() != null) {
            ITextComponent textComponent = entity.getOwner().getDisplayName();
            textComponent.getStyle().setItalic(true);
            if (canRenderName(entity)) y += ((getFontRendererFromRenderManager()).FONT_HEIGHT * 1.15F * 0.025F);
            renderEntityName(entity, x, y, z, textComponent.getFormattedText(), NAME_TAG_RANGE);
        }
    }

    public MutantSnowGolemModel getMainModel() {
        return (MutantSnowGolemModel) super.getMainModel();
    }

    protected ResourceLocation getEntityTexture(MutantSnowGolemEntity entity) {
        return TEXTURE;
    }

    class PumpkinLayer implements LayerRenderer<MutantSnowGolemEntity> {
        public void doRenderLayer(MutantSnowGolemEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isPumpkinEquipped() && !entityIn.isInvisible()) {
                MutantSnowGolemRenderer.this.bindTexture(MutantSnowGolemRenderer.PUMPKIN_TEXTURE);
                MutantSnowGolemRenderer.this.getMainModel().render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }

        public boolean shouldCombineTextures() {
            return true;
        }
    }

    class GlowLayer implements LayerRenderer<MutantSnowGolemEntity> {
        public void doRenderLayer(MutantSnowGolemEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isPumpkinEquipped()) {
                MutantSnowGolemRenderer.this.bindTexture(MutantSnowGolemRenderer.GLOW_TEXTURE);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(!entityIn.isInvisible());
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
                float f1 = MathHelper.cos(ageInTicks * 0.1F);
                float f2 = MathHelper.cos(ageInTicks * 0.15F);
                GlStateManager.color(1.0F, 0.8F + 0.05F * f2, 0.15F + 0.2F * f1, 1.0F);
                (Minecraft.getMinecraft()).entityRenderer.setupFogColor(true);
                MutantSnowGolemRenderer.this.mainModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                (Minecraft.getMinecraft()).entityRenderer.setupFogColor(false);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                MutantSnowGolemRenderer.this.setLightmap(entityIn);
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }

    class HeldBlockLayer implements LayerRenderer<MutantSnowGolemEntity> {
        public void doRenderLayer(MutantSnowGolemEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isEntityAlive() && entityIn.isThrowing() && entityIn.getThrowingTick() < 7) {
                GlStateManager.enableRescaleNormal();
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.4F, 0.0F, 0.0F);
                MutantSnowGolemRenderer.this.getMainModel().postRenderArm(0.0625F);
                GlStateManager.translate(0.0F, 0.9F, 0.0F);
                GlStateManager.scale(-0.8F, -0.8F, 0.8F);
                int i = entityIn.getBrightnessForRender();
                int j = i % 65536;
                int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                MutantSnowGolemRenderer.this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.translate(-0.5F, -0.5F, 0.5F);
                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.ICE.getDefaultState(), 1.0F);
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }
}

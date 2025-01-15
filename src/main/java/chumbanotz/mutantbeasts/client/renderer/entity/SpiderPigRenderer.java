package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.SpiderPigModel;
import chumbanotz.mutantbeasts.entity.mutant.SpiderPigEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class SpiderPigRenderer extends RenderLiving<SpiderPigEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("spider_pig/spider_pig");

    private static final ResourceLocation SADDLE_TEXTURE = MutantBeasts.getEntityTexture("spider_pig/saddle");

    public SpiderPigRenderer(RenderManager manager) {
        super(manager, new SpiderPigModel(), 0.8F);
        addLayer(new SaddleLayer());
    }

    protected float getDeathMaxRotation(SpiderPigEntity entityLivingBaseIn) {
        return 180.0F;
    }

    protected void preRenderCallback(SpiderPigEntity entitylivingbaseIn, float partialTickTime) {
        float scale = 1.2F;
        if (entitylivingbaseIn.isChild()) {
            scale *= 0.5F;
            this.shadowSize *= 0.5F;
        } else {
            this.shadowSize = 0.8F;
        }
        GlStateManager.scale(scale, scale, scale);
    }

    protected ResourceLocation getEntityTexture(SpiderPigEntity entity) {
        return TEXTURE;
    }

    class SaddleLayer implements LayerRenderer<SpiderPigEntity> {
        public void doRenderLayer(SpiderPigEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entityIn.isSaddled()) {
                SpiderPigRenderer.this.bindTexture(SpiderPigRenderer.SADDLE_TEXTURE);
                SpiderPigRenderer.this.mainModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }
}

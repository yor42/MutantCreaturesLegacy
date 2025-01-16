package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantArrowModel;
import chumbanotz.mutantbeasts.entity.projectile.MutantArrowEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class MutantArrowRenderer
extends Render<MutantArrowEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_arrow");
    private final MutantArrowModel arrowModel = new MutantArrowModel();

    public MutantArrowRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    public boolean shouldRender(MutantArrowEntity entity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }

    public void doRender(MutantArrowEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate((float)x, (float)y, (float)z);
        this.bindEntityTexture(entity);
        float ageInTicks = (float)entity.ticksExisted + partialTicks;
        for (int i = 0; i < entity.getClones(); ++i) {
            GlStateManager.pushMatrix();
            float scale = entity.getSpeed() - (float)i * 0.08f;
            double x1 = (entity.getTargetX() - entity.posX) * (double)ageInTicks * (double)scale;
            double y1 = (entity.getTargetY() - entity.posY) * (double)ageInTicks * (double)scale;
            double z1 = (entity.getTargetZ() - entity.posZ) * (double)ageInTicks * (double)scale;
            GlStateManager.translate((float)x1, (float)y1, (float)z1);
            GlStateManager.rotate(entity.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(entity.rotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(1.2f, 1.2f, 1.2f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f - (float)i * 0.08f);
            this.arrowModel.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
            GlStateManager.popMatrix();
        }
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getEntityTexture(MutantArrowEntity entity) {
        return TEXTURE;
    }
}

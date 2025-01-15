package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantZombieModel;
import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MutantZombieRenderer extends RenderLiving<MutantZombieEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_zombie");

    public MutantZombieRenderer(RenderManager manager) {
        super(manager, new MutantZombieModel(), 1.0F);
    }

    protected void renderModel(MutantZombieEntity living, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (living.vanishTime > 0) {
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F - (living.vanishTime + ((MutantZombieModel) this.mainModel).getPartialTick()) / 100.0F * 0.6F);
        }
        super.renderModel(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (living.vanishTime > 0) {
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    protected void preRenderCallback(MutantZombieEntity entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(1.3F, 1.3F, 1.3F);
    }

    protected void applyRotations(MutantZombieEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.deathTime > 0) {
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
            int pitch = Math.min(20, entityLiving.deathTime);
            boolean reviving = false;
            if (entityLiving.deathTime > 100) {
                pitch = 140 - entityLiving.deathTime;
                reviving = true;
            }
            if (pitch > 0) {
                float f = (pitch + partialTicks - 1.0F) / 20.0F * 1.6F;
                if (reviving) f = (pitch - partialTicks) / 40.0F * 1.6F;
                f = MathHelper.sqrt(f);
                if (f > 1.0F) f = 1.0F;
                GlStateManager.rotate(f * getDeathMaxRotation(entityLiving), -1.0F, 0.0F, 0.0F);
            }
        } else {
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        }
    }

    protected float getDeathMaxRotation(MutantZombieEntity living) {
        return 80.0F;
    }

    protected ResourceLocation getEntityTexture(MutantZombieEntity entity) {
        return TEXTURE;
    }
}

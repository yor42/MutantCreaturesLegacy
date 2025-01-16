package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantZombieModel;
import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MutantZombieRenderer
extends RenderLiving<MutantZombieEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_zombie");

    public MutantZombieRenderer(RenderManager manager) {
        super(manager, new MutantZombieModel(), 1.0f);
    }

    protected void renderModel(MutantZombieEntity living, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (living.vanishTime > 0) {
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f - ((float)living.vanishTime + ((MutantZombieModel)this.mainModel).getPartialTick()) / 100.0f * 0.6f);
        }
        super.renderModel(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (living.vanishTime > 0) {
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    protected void preRenderCallback(MutantZombieEntity entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(1.3f, 1.3f, 1.3f);
    }

    protected void applyRotations(MutantZombieEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.deathTime > 0) {
            GlStateManager.rotate(180.0f - rotationYaw, 0.0f, 1.0f, 0.0f);
            int pitch = Math.min(20, entityLiving.deathTime);
            boolean reviving = false;
            if (entityLiving.deathTime > 100) {
                pitch = 140 - entityLiving.deathTime;
                reviving = true;
            }
            if (pitch > 0) {
                float f = ((float)pitch + partialTicks - 1.0f) / 20.0f * 1.6f;
                if (reviving) {
                    f = ((float)pitch - partialTicks) / 40.0f * 1.6f;
                }
                if ((f = MathHelper.sqrt(f)) > 1.0f) {
                    f = 1.0f;
                }
                GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), -1.0f, 0.0f, 0.0f);
            }
        } else {
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        }
    }

    protected float getDeathMaxRotation(MutantZombieEntity living) {
        return 80.0f;
    }

    protected ResourceLocation getEntityTexture(MutantZombieEntity entity) {
        return TEXTURE;
    }
}

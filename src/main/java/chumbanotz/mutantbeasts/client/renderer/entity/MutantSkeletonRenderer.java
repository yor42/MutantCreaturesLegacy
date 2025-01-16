package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantSkeletonModel;
import chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class MutantSkeletonRenderer
extends RenderLiving<MutantSkeletonEntity> {
    static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_skeleton");

    public MutantSkeletonRenderer(RenderManager manager) {
        super(manager, new MutantSkeletonModel(), 0.7f);
    }

    protected float getDeathMaxRotation(MutantSkeletonEntity entityLivingBaseIn) {
        return 0.0f;
    }

    protected ResourceLocation getEntityTexture(MutantSkeletonEntity entity) {
        return TEXTURE;
    }
}

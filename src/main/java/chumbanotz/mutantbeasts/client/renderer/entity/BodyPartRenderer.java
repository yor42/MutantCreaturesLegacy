package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.client.model.MutantSkeletonPartModel;
import chumbanotz.mutantbeasts.entity.BodyPartEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class BodyPartRenderer extends Render<BodyPartEntity> {
    private final MutantSkeletonPartModel model = new MutantSkeletonPartModel();

    public BodyPartRenderer(RenderManager renderManager) {
        super(renderManager);
        for (int i = this.model.boxList.size() - 1; i >= 0; i--) {
            ModelRenderer renderer = this.model.boxList.get(i);
            if (renderer.cubeList.isEmpty()) this.model.boxList.remove(i);
        }
    }

    public void doRender(BodyPartEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.rotate(yaw, 0.2F, 0.9F, -0.1F);
        GlStateManager.rotate(pitch, 0.9F, 0.1F, 0.2F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(1.2F, -1.2F, -1.2F);
        bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }
        this.model.setAngles();
        this.model.getSkeletonPart(entity.getPart()).render(0.0625F);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getEntityTexture(BodyPartEntity entity) {
        return MutantSkeletonRenderer.TEXTURE;
    }
}

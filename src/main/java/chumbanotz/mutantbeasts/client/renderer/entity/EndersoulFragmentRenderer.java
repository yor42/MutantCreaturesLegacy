package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.EndersoulFragmentModel;
import chumbanotz.mutantbeasts.entity.EndersoulFragmentEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class EndersoulFragmentRenderer extends Render<EndersoulFragmentEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("endersoul_fragment");

    private final EndersoulFragmentModel model = new EndersoulFragmentModel();

    public EndersoulFragmentRenderer(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.3F;
        this.shadowOpaque = 0.5F;
    }

    public void doRender(EndersoulFragmentEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y - 1.9F, (float) z);
        GlStateManager.scale(1.6F, 1.6F, 1.6F);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }
        EndersoulCloneRenderer.render(entity, TEXTURE, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, 0.0625F, this.model, 1.0F);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getEntityTexture(EndersoulFragmentEntity entity) {
        return TEXTURE;
    }
}

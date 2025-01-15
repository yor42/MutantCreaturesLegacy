package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.CreeperMinionEggModel;
import chumbanotz.mutantbeasts.client.renderer.entity.layers.LayerCreeperCharge;
import chumbanotz.mutantbeasts.entity.CreeperMinionEggEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class CreeperMinionEggRenderer extends Render<CreeperMinionEggEntity> {
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("creeper_minion_egg");

    private final CreeperMinionEggModel eggModel = new CreeperMinionEggModel();

    private final CreeperMinionEggModel chargedModel = new CreeperMinionEggModel(1.0F);

    public CreeperMinionEggRenderer(RenderManager manager) {
        super(manager);
        this.shadowSize = 0.4F;
    }

    public void doRender(CreeperMinionEggEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.scale(1.5F, 1.5F, 1.5F);
        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }
        this.eggModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        if (entity.isCharged())
            LayerCreeperCharge.render(entity, 0.0F, 0.0F, partialTicks, entity.ticksExisted + partialTicks, 0.0F, 0.0F, 0.0625F, this.eggModel, this.chargedModel);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getEntityTexture(CreeperMinionEggEntity entity) {
        return TEXTURE;
    }
}

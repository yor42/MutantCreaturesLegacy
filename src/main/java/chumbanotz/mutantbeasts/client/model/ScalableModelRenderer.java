package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ScalableModelRenderer extends ModelRenderer {
    private float scale = 1.0F;

    public ScalableModelRenderer(ModelBase model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void render(float scale) {
        if (!this.isHidden && this.showModel) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(this.scale, this.scale, this.scale);
            super.render(scale);
            GlStateManager.popMatrix();
        }
    }
}

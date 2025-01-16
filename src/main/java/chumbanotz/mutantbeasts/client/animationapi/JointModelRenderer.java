package chumbanotz.mutantbeasts.client.animationapi;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class JointModelRenderer
extends ModelRenderer {
    private final ModelRenderer model;

    public JointModelRenderer(ModelBase model, int x, int y) {
        super(model);
        this.model = new ModelRenderer(model, x, y);
        super.addChild(this.model);
    }

    public ModelRenderer setTextureOffset(int x, int y) {
        if (this.model != null) {
            this.model.setTextureOffset(x, y);
        }
        return this;
    }

    public ModelRenderer setTextureSize(int w, int h) {
        if (this.model != null) {
            this.model.setTextureSize(w, h);
        }
        return this;
    }

    public void addChild(ModelRenderer renderer) {
        this.model.addChild(renderer);
    }

    public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
        return this.model.addBox(partName, offX, offY, offZ, width, height, depth);
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
        return this.model.addBox(offX, offY, offZ, width, height, depth);
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored) {
        return this.model.addBox(offX, offY, offZ, width, height, depth, mirrored);
    }

    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor) {
        this.model.addBox(offX, offY, offZ, width, height, depth, scaleFactor);
    }

    public ModelRenderer getModel() {
        return this.model;
    }
}

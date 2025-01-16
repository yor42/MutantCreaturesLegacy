package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class CrossbowModel {
    public final ModelRenderer armwear;
    public final ModelRenderer middle;
    public final ModelRenderer middle1;
    public final ModelRenderer middle2;
    public final ModelRenderer side1;
    public final ModelRenderer side2;
    public final ModelRenderer side3;
    public final ModelRenderer side4;
    public final ModelRenderer rope1;
    public final ModelRenderer rope2;

    public CrossbowModel(ModelBase model) {
        this.armwear = new ModelRenderer(model, 0, 64);
        this.armwear.addBox(-2.0f, -3.0f, -2.0f, 4, 6, 4, 0.3f);
        this.middle = new ModelRenderer(model, 16, 64);
        this.middle.addBox(-2.0f, -2.0f, -3.0f, 4, 4, 6);
        this.middle.setRotationPoint(-3.5f, 0.0f, 0.0f);
        this.armwear.addChild(this.middle);
        this.middle1 = new ModelRenderer(model, 36, 64);
        this.middle1.addBox(-1.5f, -1.5f, -3.0f, 3, 3, 6);
        this.middle1.setRotationPoint(0.0f, 0.6f, -4.0f);
        this.middle.addChild(this.middle1);
        this.middle2 = new ModelRenderer(model, 36, 64);
        this.middle2.addBox(-1.5f, -1.5f, -3.0f, 3, 3, 6);
        this.middle2.setRotationPoint(0.0f, 0.6f, 4.0f);
        this.middle.addChild(this.middle2);
        this.side1 = new ModelRenderer(model, 0, 74);
        this.side1.addBox(-1.0f, -1.0f, -8.0f, 2, 2, 8);
        this.side1.setRotationPoint(0.0f, 0.0f, -2.0f);
        this.middle1.addChild(this.side1);
        this.side2 = new ModelRenderer(model, 0, 74);
        this.side2.addBox(-1.0f, -1.0f, 0.0f, 2, 2, 8);
        this.side2.setRotationPoint(0.0f, 0.0f, 2.0f);
        this.middle2.addChild(this.side2);
        this.side3 = new ModelRenderer(model, 20, 74);
        this.side3.addBox(-0.5f, -0.5f, -8.0f, 1, 1, 8);
        this.side3.setRotationPoint(0.0f, 0.0f, -5.0f);
        this.side1.addChild(this.side3);
        this.side4 = new ModelRenderer(model, 20, 74);
        this.side4.addBox(-0.5f, -0.5f, 0.0f, 1, 1, 8);
        this.side4.setRotationPoint(0.0f, 0.0f, 5.0f);
        this.side2.addChild(this.side4);
        this.rope1 = new ModelRenderer(model, 0, 84);
        this.rope1.addBox(-0.5f, -0.5f, 0.0f, 1, 1, 15, -0.4f);
        this.rope1.setRotationPoint(0.0f, 0.0f, -6.0f);
        this.side3.addChild(this.rope1);
        this.rope2 = new ModelRenderer(model, 0, 84);
        this.rope2.addBox(-0.5f, -0.5f, -15.0f, 1, 1, 15, -0.4f);
        this.rope2.setRotationPoint(0.0f, 0.0f, 6.0f);
        this.side4.addChild(this.rope2);
    }

    public void setAngles(float PI) {
        this.middle1.rotateAngleX = PI / 8.0f;
        this.middle2.rotateAngleX = -PI / 8.0f;
        this.side1.rotateAngleX = -PI / 5.0f;
        this.side2.rotateAngleX = PI / 5.0f;
        this.side3.rotateAngleX = -PI / 4.0f;
        this.side4.rotateAngleX = PI / 4.0f;
    }

    public void rotateRope() {
        this.rope1.rotateAngleX = -(this.middle1.rotateAngleX + this.side1.rotateAngleX + this.side3.rotateAngleX);
        this.rope2.rotateAngleX = -(this.middle2.rotateAngleX + this.side2.rotateAngleX + this.side4.rotateAngleX);
    }
}

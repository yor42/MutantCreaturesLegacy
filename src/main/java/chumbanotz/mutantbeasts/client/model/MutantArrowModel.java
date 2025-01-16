package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MutantArrowModel
extends ModelBase {
    private final ModelRenderer stick = new ModelRenderer(this, 0, 0);
    private final ModelRenderer point1;
    private final ModelRenderer point2;
    private final ModelRenderer point3;
    private final ModelRenderer point4;

    public MutantArrowModel() {
        this.stick.addBox(-0.5f, -0.5f, -13.0f, 1, 1, 26);
        this.stick.setRotationPoint(0.0f, 24.0f, 0.0f);
        this.point1 = new ModelRenderer(this, 0, 0);
        this.point1.addBox(-3.0f, -0.5f, 0.0f, 3, 1, 1, 0.25f);
        this.point1.setRotationPoint(0.0f, 0.0f, -12.0f);
        this.stick.addChild(this.point1);
        this.point2 = new ModelRenderer(this, 0, 0);
        this.point2.addBox(0.0f, -0.5f, 0.0f, 3, 1, 1, 0.251f);
        this.point2.setRotationPoint(0.0f, 0.0f, -12.0f);
        this.stick.addChild(this.point2);
        this.point3 = new ModelRenderer(this, 0, 2);
        this.point3.addBox(-0.5f, -3.0f, 0.0f, 1, 3, 1, 0.25f);
        this.point3.setRotationPoint(0.0f, 0.0f, -13.0f);
        this.stick.addChild(this.point3);
        this.point4 = new ModelRenderer(this, 0, 2);
        this.point4.addBox(-0.5f, 0.0f, 0.0f, 1, 3, 1, 0.251f);
        this.point4.setRotationPoint(0.0f, 0.0f, -13.0f);
        this.stick.addChild(this.point4);
        this.point1.rotateAngleY = 0.7853982f;
        this.point2.rotateAngleY = -0.7853982f;
        this.point3.rotateAngleX = -0.7853982f;
        this.point4.rotateAngleX = 0.7853982f;
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.stick.render(scale);
    }
}

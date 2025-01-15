package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.entity.EndersoulFragmentEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class EndersoulFragmentModel extends ModelBase {
    private final ModelRenderer base = new ModelRenderer(this);

    private final ModelRenderer[] sticks = new ModelRenderer[8];

    public EndersoulFragmentModel() {
        this.base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);
        this.base.setRotationPoint(0.0F, 22.0F, 0.0F);
        for (int i = 0; i < this.sticks.length; i++) {
            this.sticks[i] = new ModelRenderer(this);
            if (i < this.sticks.length / 2) {
                this.sticks[i].addBox(-0.5F, -4.0F, -0.5F, 1, 8, 1);
            } else {
                this.sticks[i].addBox(-0.5F, -6.0F, -0.5F, 1, 10, 1, 0.15F);
            }
            this.base.addChild(this.sticks[i]);
        }
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entityIn instanceof EndersoulFragmentEntity) {
            EndersoulFragmentEntity entity = (EndersoulFragmentEntity) entityIn;
            for (int i = 0; i < this.sticks.length; i++) {
                (this.sticks[i]).rotateAngleX = entity.stickRotations[i][0];
                (this.sticks[i]).rotateAngleY = entity.stickRotations[i][1];
                (this.sticks[i]).rotateAngleZ = entity.stickRotations[i][2];
            }
        }
        this.base.render(0.0625F);
    }
}

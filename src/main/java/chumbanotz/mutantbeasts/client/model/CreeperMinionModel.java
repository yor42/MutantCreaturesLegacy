package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.entity.Entity;

public class CreeperMinionModel
extends ModelCreeper {
    public CreeperMinionModel() {
        this(0.0f);
    }

    public CreeperMinionModel(float scale) {
        super(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing *= 3.0f, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.head.rotationPointY = 6.0f;
        this.body.rotationPointY = 6.0f;
        this.leg1.setRotationPoint(-2.0f, 18.0f, 4.0f);
        this.leg2.setRotationPoint(2.0f, 18.0f, 4.0f);
        this.leg3.setRotationPoint(-2.0f, 18.0f, -4.0f);
        this.leg4.setRotationPoint(2.0f, 18.0f, -4.0f);
        if (entityIn == null || entityIn instanceof CreeperMinionEntity && ((CreeperMinionEntity)entityIn).isSitting()) {
            this.head.rotationPointY += 6.0f;
            this.body.rotationPointY += 6.0f;
            this.leg1.rotationPointY += 4.0f;
            this.leg1.rotationPointZ -= 2.0f;
            this.leg2.rotationPointY += 4.0f;
            this.leg2.rotationPointZ -= 2.0f;
            this.leg3.rotationPointY += 4.0f;
            this.leg3.rotationPointZ += 2.0f;
            this.leg4.rotationPointY += 4.0f;
            this.leg4.rotationPointZ += 2.0f;
            this.leg1.rotateAngleX = 1.5707964f;
            this.leg2.rotateAngleX = 1.5707964f;
            this.leg3.rotateAngleX = -1.5707964f;
            this.leg4.rotateAngleX = -1.5707964f;
        }
    }
}

package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class MutantSkeletonArmorModel
extends ModelBiped {
    public MutantSkeletonArmorModel() {
        super(0.5f);
        this.bipedHead.cubeList.clear();
        this.bipedHeadwear.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();
        ModelRenderer head = new ModelRenderer(this, 0, 0);
        head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.4f);
        ModelRenderer jaw = new ModelRenderer(this, 32, 0);
        jaw.addBox(-4.0f, -3.0f, -8.0f, 8, 3, 8, 0.7f);
        jaw.setRotationPoint(0.0f, -0.2f, 3.5f);
        jaw.rotateAngleX = 0.09817477f;
        head.addChild(jaw);
        this.bipedHeadwear.addChild(head);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityArmorStand) {
            EntityArmorStand entityarmorstand = (EntityArmorStand)((Object)entityIn);
            this.bipedHead.rotateAngleX = (float)Math.PI / 180 * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = (float)Math.PI / 180 * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = (float)Math.PI / 180 * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0f, 1.0f, 0.0f);
            this.bipedBody.rotateAngleX = (float)Math.PI / 180 * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = (float)Math.PI / 180 * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = (float)Math.PI / 180 * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = (float)Math.PI / 180 * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = (float)Math.PI / 180 * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = (float)Math.PI / 180 * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = (float)Math.PI / 180 * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = (float)Math.PI / 180 * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = (float)Math.PI / 180 * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = (float)Math.PI / 180 * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = (float)Math.PI / 180 * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = (float)Math.PI / 180 * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9f, 11.0f, 0.0f);
            this.bipedRightLeg.rotateAngleX = (float)Math.PI / 180 * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = (float)Math.PI / 180 * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = (float)Math.PI / 180 * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9f, 11.0f, 0.0f);
            MutantSkeletonArmorModel.copyModelAngles((ModelRenderer)this.bipedHead, (ModelRenderer)this.bipedHeadwear);
        } else {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        }
    }
}

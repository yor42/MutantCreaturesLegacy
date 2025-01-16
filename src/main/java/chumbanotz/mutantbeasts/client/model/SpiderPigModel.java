package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.client.animationapi.JointModelRenderer;
import chumbanotz.mutantbeasts.entity.mutant.SpiderPigEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SpiderPigModel
extends ModelBase {
    private final ModelRenderer snout;
    private final JointModelRenderer head;
    private final ModelRenderer base;
    private final ModelRenderer body1;
    private final ModelRenderer body2;
    private final ModelRenderer butt;
    private final JointModelRenderer frontLeg1;
    private final JointModelRenderer frontLegF1;
    private final JointModelRenderer frontLeg2;
    private final JointModelRenderer frontLegF2;
    private final JointModelRenderer middleLeg1;
    private final JointModelRenderer middleLegF1;
    private final JointModelRenderer middleLeg2;
    private final JointModelRenderer middleLegF2;
    private final JointModelRenderer backLeg1;
    private final JointModelRenderer backLegF1;
    private final JointModelRenderer backLeg2;
    private final JointModelRenderer backLegF2;

    public SpiderPigModel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.base = new ModelRenderer(this);
        this.base.setRotationPoint(0.0f, 14.5f, -2.0f);
        this.body2 = new ModelRenderer(this, 32, 0);
        this.body2.addBox(-3.0f, -3.0f, 0.0f, 6, 6, 10);
        this.body2.setTextureOffset(44, 16).addBox(-5.0f, -5.0f, -4.0f, 10, 8, 12, -0.6f);
        this.base.addChild(this.body2);
        this.body1 = new JointModelRenderer(this, 64, 0);
        this.body1.addBox(-3.5f, -3.5f, -9.0f, 7, 7, 9);
        this.body1.setRotationPoint(0.0f, -1.0f, 1.5f);
        this.body2.addChild(this.body1);
        this.butt = new ModelRenderer(this, 0, 16);
        this.butt.addBox(-5.0f, -4.5f, 0.0f, 10, 9, 12);
        this.butt.setRotationPoint(0.0f, 0.0f, 7.0f);
        this.body2.addChild(this.butt);
        this.head = new JointModelRenderer(this, 0, 0);
        this.head.addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8);
        this.head.setRotationPoint(0.0f, 0.0f, -8.0f);
        this.body1.addChild(this.head);
        this.snout = new ModelRenderer(this, 24, 0);
        this.snout.addBox(-2.0f, 0.0f, -9.0f, 4, 3, 1);
        this.head.addChild(this.snout);
        this.frontLeg1 = new JointModelRenderer(this, 0, 37);
        this.frontLeg1.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2);
        this.frontLeg1.setRotationPoint(-3.5f, 0.0f, -5.0f);
        this.body1.addChild(this.frontLeg1);
        this.frontLegF1 = new JointModelRenderer(this, 8, 37);
        this.frontLegF1.addBox(-1.0f, 0.0f, -1.0f, 2, 16, 2);
        this.frontLegF1.setRotationPoint(-0.0f, 12.0f, -0.1f);
        this.frontLeg1.addChild(this.frontLegF1);
        this.frontLeg2 = new JointModelRenderer(this, 0, 37);
        this.frontLeg2.mirror = true;
        this.frontLeg2.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2);
        this.frontLeg2.setRotationPoint(3.5f, 0.0f, -5.0f);
        this.body1.addChild(this.frontLeg2);
        this.frontLegF2 = new JointModelRenderer(this, 8, 37);
        this.frontLegF2.mirror = true;
        this.frontLegF2.addBox(-1.0f, 0.0f, -1.0f, 2, 16, 2);
        this.frontLegF2.setRotationPoint(0.0f, 12.0f, 0.1f);
        this.frontLeg2.addChild(this.frontLegF2);
        this.middleLeg1 = new JointModelRenderer(this, 0, 37);
        this.middleLeg1.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2);
        this.middleLeg1.setRotationPoint(-3.5f, 0.0f, -3.0f);
        this.body1.addChild(this.middleLeg1);
        this.middleLegF1 = new JointModelRenderer(this, 8, 37);
        this.middleLegF1.addBox(-1.0f, 0.0f, -1.0f, 2, 16, 2);
        this.middleLegF1.setRotationPoint(0.0f, 12.0f, -0.1f);
        this.middleLeg1.addChild(this.middleLegF1);
        this.middleLeg2 = new JointModelRenderer(this, 0, 37);
        this.middleLeg2.mirror = true;
        this.middleLeg2.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2);
        this.middleLeg2.setRotationPoint(3.5f, 0.0f, -3.0f);
        this.body1.addChild(this.middleLeg2);
        this.middleLegF2 = new JointModelRenderer(this, 8, 37);
        this.middleLegF2.mirror = true;
        this.middleLegF2.addBox(-1.0f, 0.0f, -1.0f, 2, 16, 2);
        this.middleLegF2.setRotationPoint(0.0f, 12.0f, 0.1f);
        this.middleLeg2.addChild(this.middleLegF2);
        this.backLeg1 = new JointModelRenderer(this, 16, 37);
        this.backLeg1.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4);
        this.backLeg1.setRotationPoint(-2.5f, 2.0f, 7.0f);
        this.body2.addChild(this.backLeg1);
        this.backLegF1 = new JointModelRenderer(this, 16, 45);
        this.backLegF1.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4, 0.2f);
        this.backLegF1.setRotationPoint(0.0f, 3.0f, 0.0f);
        this.backLeg1.addChild(this.backLegF1);
        this.backLeg2 = new JointModelRenderer(this, 32, 37);
        this.backLeg2.mirror = true;
        this.backLeg2.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4);
        this.backLeg2.setRotationPoint(2.5f, 2.0f, 7.0f);
        this.body2.addChild(this.backLeg2);
        this.backLegF2 = new JointModelRenderer(this, 16, 45);
        this.backLegF2.mirror = true;
        this.backLegF2.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4, 0.2f);
        this.backLegF2.setRotationPoint(0.0f, 3.0f, 0.0f);
        this.backLeg2.addChild(this.backLegF2);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setAngles();
        this.animate((SpiderPigEntity)entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.base.render(scale);
    }

    public void setAngles() {
        SpiderPigModel.resetAngles(this.head, this.head.getModel(), this.body1, this.body2, this.butt);
        SpiderPigModel.resetAngles(this.frontLeg1, this.frontLeg1.getModel(), this.frontLegF1, this.frontLegF1.getModel(), this.frontLeg2, this.frontLeg2.getModel(), this.frontLegF2, this.frontLegF2.getModel());
        SpiderPigModel.resetAngles(this.middleLeg1, this.middleLeg1.getModel(), this.middleLegF1, this.middleLegF1.getModel(), this.middleLeg2, this.middleLeg2.getModel(), this.middleLegF2, this.middleLegF2.getModel());
        SpiderPigModel.resetAngles(this.backLeg1, this.backLeg1.getModel(), this.backLegF1, this.backLegF1.getModel(), this.backLeg2, this.backLeg2.getModel(), this.backLegF2, this.backLegF2.getModel());
        this.body1.rotateAngleX += 0.3926991f;
        this.body2.rotateAngleX += -0.05235988f;
        this.butt.rotateAngleX += 0.5711987f;
        this.head.rotateAngleX += -0.3926991f;
        this.frontLeg1.rotateAngleX += -(this.body1.rotateAngleX + this.body2.rotateAngleX);
        this.frontLeg1.rotateAngleY += -1.0471976f;
        this.frontLeg1.getModel().rotateAngleZ += 2.0943952f;
        this.frontLegF1.rotateAngleZ += -1.6534699f;
        this.frontLeg2.rotateAngleX += -(this.body1.rotateAngleX + this.body2.rotateAngleX);
        this.frontLeg2.rotateAngleY += 1.0f;
        this.frontLeg2.getModel().rotateAngleZ += -2.0943952f;
        this.frontLegF2.rotateAngleZ += 1.6534699f;
        this.middleLeg1.rotateAngleX += -(this.body1.rotateAngleX + this.body2.rotateAngleX);
        this.middleLeg1.rotateAngleY += -0.31415927f;
        this.middleLeg1.getModel().rotateAngleZ += 2.0399954f;
        this.middleLegF1.rotateAngleZ += -1.6534699f;
        this.middleLeg2.rotateAngleX += -(this.body1.rotateAngleX + this.body2.rotateAngleX);
        this.middleLeg2.rotateAngleY += 0.31415927f;
        this.middleLeg2.getModel().rotateAngleZ += -2.0399954f;
        this.middleLegF2.rotateAngleZ += 1.6534699f;
        this.backLeg1.rotateAngleX += -0.3926991f;
        this.backLeg1.getModel().rotateAngleZ += 0.3926991f;
        this.backLegF1.rotateAngleZ += -0.3926991f;
        this.backLegF1.getModel().rotateAngleX += 0.5711987f;
        this.backLeg2.rotateAngleX += -0.3926991f;
        this.backLeg2.getModel().rotateAngleZ += -0.3926991f;
        this.backLegF2.rotateAngleZ += 0.3926991f;
        this.backLegF2.getModel().rotateAngleX += 0.5711987f;
    }

    public void animate(SpiderPigEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        float moveAnim = MathHelper.sin((float)(f * 0.9f)) * f1;
        float moveAnim1 = MathHelper.sin((float)(f * 0.9f + 0.3f)) * f1;
        float moveAnim1d = MathHelper.sin((float)(f * 0.9f + 0.3f + 0.5f)) * f1;
        float moveAnim2 = MathHelper.sin((float)(f * 0.9f + 0.9f)) * f1;
        float moveAnim2d = MathHelper.sin((float)(f * 0.9f + 0.9f + 0.5f)) * f1;
        float moveAnim3 = MathHelper.sin((float)(f * 0.9f - 0.3f)) * f1;
        float moveAnim3d = MathHelper.sin((float)(f * 0.9f - 0.3f + 0.5f)) * f1;
        float moveAnim4 = MathHelper.sin((float)(f * 0.9f - 0.9f)) * f1;
        float moveAnim4d = MathHelper.sin((float)(f * 0.9f - 0.9f + 0.5f)) * f1;
        float breatheAnim = MathHelper.sin((float)(f2 * 0.2f));
        float faceYaw = f3 * (float)Math.PI / 180.0f;
        float facePitch = f4 * (float)Math.PI / 180.0f;
        this.head.rotateAngleX += breatheAnim * 0.02f;
        this.body1.rotateAngleX += breatheAnim * 0.005f;
        this.butt.rotateAngleX += -breatheAnim * 0.015f;
        this.head.getModel().rotateAngleX += facePitch;
        this.head.getModel().rotateAngleY += faceYaw;
        this.frontLeg1.getModel().rotateAngleZ += -moveAnim1 * (float)Math.PI / 6.0f;
        this.frontLeg1.getModel().rotateAngleX += -0.3926991f * f1;
        this.frontLegF1.rotateAngleZ += moveAnim1d * (float)Math.PI / 6.0f + 0.2617994f * f1;
        this.frontLeg2.getModel().rotateAngleZ += moveAnim2 * (float)Math.PI / 6.0f;
        this.frontLeg2.getModel().rotateAngleX += -0.3926991f * f1;
        this.frontLegF2.rotateAngleZ += -(moveAnim2d * (float)Math.PI / 6.0f + 0.2617994f * f1);
        this.middleLeg1.getModel().rotateAngleZ += -moveAnim3 * (float)Math.PI / 6.0f;
        this.middleLeg1.getModel().rotateAngleX += -0.8975979f * f1;
        this.middleLegF1.rotateAngleZ += moveAnim3d * (float)Math.PI / 6.0f + 0.3926991f * f1;
        this.middleLeg2.getModel().rotateAngleZ += moveAnim4 * (float)Math.PI / 6.0f;
        this.middleLeg2.getModel().rotateAngleX += -0.8975979f * f1;
        this.middleLegF2.rotateAngleZ += -(moveAnim4d * (float)Math.PI / 6.0f + 0.3926991f * f1);
        this.backLeg1.rotateAngleX += -moveAnim4 * (float)Math.PI / 5.0f + 0.2617994f * f1;
        this.backLeg2.rotateAngleX += -moveAnim1 * (float)Math.PI / 5.0f + 0.2617994f * f1;
        this.body2.rotateAngleX += -moveAnim * (float)Math.PI / 20.0f;
        this.head.rotateAngleX += moveAnim * (float)Math.PI / 20.0f;
    }

    public static void resetAngles(ModelRenderer ... boxes) {
        for (ModelRenderer box : boxes) {
            box.rotateAngleX = 0.0f;
            box.rotateAngleY = 0.0f;
            box.rotateAngleZ = 0.0f;
        }
    }
}

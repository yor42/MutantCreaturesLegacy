package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.client.animationapi.JointModelRenderer;
import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class MutantSnowGolemModel
extends ModelBase {
    private final ModelRenderer pelvis;
    private final ModelRenderer abdomen;
    private final ModelRenderer chest;
    private final JointModelRenderer head;
    private final ModelRenderer headCore;
    private final JointModelRenderer arm1;
    private final JointModelRenderer arm2;
    private final JointModelRenderer forearm1;
    private final JointModelRenderer forearm2;
    private final JointModelRenderer leg1;
    private final JointModelRenderer leg2;
    private final JointModelRenderer foreleg1;
    private final JointModelRenderer foreleg2;
    private float partialTick;

    public MutantSnowGolemModel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.pelvis = new ModelRenderer(this);
        this.pelvis.setRotationPoint(0.0f, 13.5f, 5.0f);
        this.abdomen = new ModelRenderer(this, 0, 32);
        this.abdomen.addBox(-5.0f, -8.0f, -4.0f, 10, 8, 8);
        this.pelvis.addChild(this.abdomen);
        this.chest = new ModelRenderer(this, 24, 36);
        this.chest.addBox(-8.0f, -12.0f, -6.0f, 16, 12, 12);
        this.chest.setRotationPoint(0.0f, -6.0f, 0.0f);
        this.head = new JointModelRenderer(this, 0, 0);
        this.head.setTextureSize(64, 32);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.5f);
        this.head.setRotationPoint(0.0f, -12.0f, -2.0f);
        this.chest.addChild(this.head);
        this.headCore = new ModelRenderer(this, 64, 0);
        this.headCore.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8);
        this.headCore.setTextureOffset(80, 46).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, -0.5f);
        this.headCore.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.head.addChild(this.headCore);
        this.abdomen.addChild(this.chest);
        this.arm1 = new JointModelRenderer(this, 68, 16);
        this.arm1.addBox(-2.5f, 0.0f, -2.5f, 5, 10, 5);
        this.arm1.setRotationPoint(-9.0f, -11.0f, 0.0f);
        this.chest.addChild(this.arm1);
        this.forearm1 = new JointModelRenderer(this, 96, 0);
        this.forearm1.addBox(-3.0f, 0.0f, -3.0f, 6, 12, 6);
        this.forearm1.setRotationPoint(0.0f, 10.0f, 0.0f);
        this.arm1.addChild(this.forearm1);
        this.arm2 = new JointModelRenderer(this, 68, 16);
        this.arm2.mirror = true;
        this.arm2.addBox(-2.5f, 0.0f, -2.5f, 5, 10, 5);
        this.arm2.setRotationPoint(9.0f, -11.0f, 0.0f);
        this.chest.addChild(this.arm2);
        this.forearm2 = new JointModelRenderer(this, 96, 0);
        this.forearm2.mirror = true;
        this.forearm2.addBox(-3.0f, 0.0f, -3.0f, 6, 12, 6);
        this.forearm2.setRotationPoint(0.0f, 10.0f, 0.0f);
        this.arm2.addChild(this.forearm2);
        this.leg1 = new JointModelRenderer(this, 88, 18);
        this.leg1.addBox(-3.0f, 0.0f, -3.0f, 6, 8, 6);
        this.leg1.setRotationPoint(-4.0f, -1.0f, -3.0f);
        this.pelvis.addChild(this.leg1);
        this.foreleg1 = new JointModelRenderer(this, 88, 32);
        this.foreleg1.addBox(-3.0f, 0.0f, -3.0f, 6, 8, 6);
        this.foreleg1.setRotationPoint(-1.0f, 6.0f, -0.0f);
        this.leg1.addChild(this.foreleg1);
        this.leg2 = new JointModelRenderer(this, 88, 18);
        this.leg2.mirror = true;
        this.leg2.addBox(-3.0f, 0.0f, -3.0f, 6, 8, 6);
        this.leg2.setRotationPoint(4.0f, -1.0f, -3.0f);
        this.pelvis.addChild(this.leg2);
        this.foreleg2 = new JointModelRenderer(this, 88, 32);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-3.0f, 0.0f, -3.0f, 6, 8, 6);
        this.foreleg2.setRotationPoint(1.0f, 6.0f, -0.0f);
        this.leg2.addChild(this.foreleg2);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setAngles();
        this.animate((MutantSnowGolemEntity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.pelvis.render(scale);
    }

    private void setAngles() {
        this.pelvis.rotationPointY = 13.5f;
        this.abdomen.rotateAngleX = 0.1308997f;
        this.chest.rotateAngleX = 0.1308997f;
        this.chest.rotateAngleY = 0.0f;
        this.head.rotateAngleX = -0.2617994f;
        this.head.getModel().rotateAngleX = 0.0f;
        this.head.getModel().rotateAngleY = 0.0f;
        this.arm1.rotateAngleX = -0.31415927f;
        this.arm1.rotateAngleZ = 0.0f;
        this.arm1.getModel().rotateAngleX = 0.0f;
        this.arm1.getModel().rotateAngleY = 0.5235988f;
        this.arm1.getModel().rotateAngleZ = 0.5235988f;
        this.forearm1.rotateAngleY = -0.5235988f;
        this.forearm1.rotateAngleZ = -0.2617994f;
        this.forearm1.getModel().rotateAngleX = -0.5235988f;
        this.arm2.rotateAngleX = -0.31415927f;
        this.arm2.rotateAngleZ = 0.0f;
        this.arm2.getModel().rotateAngleX = 0.0f;
        this.arm2.getModel().rotateAngleY = -0.5235988f;
        this.arm2.getModel().rotateAngleZ = -0.5235988f;
        this.forearm2.rotateAngleY = 0.5235988f;
        this.forearm2.rotateAngleZ = 0.2617994f;
        this.forearm2.getModel().rotateAngleX = -0.5235988f;
        this.leg1.rotateAngleX = -0.62831855f;
        this.leg1.getModel().rotateAngleZ = 0.5235988f;
        this.foreleg1.rotateAngleZ = -0.5235988f;
        this.foreleg1.getModel().rotateAngleX = 0.69813174f;
        this.leg2.rotateAngleX = -0.62831855f;
        this.leg2.getModel().rotateAngleZ = -0.5235988f;
        this.foreleg2.rotateAngleZ = 0.5235988f;
        this.foreleg2.getModel().rotateAngleX = 0.69813174f;
    }

    private void animate(MutantSnowGolemEntity golem, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float temp = 0.5f;
        float walkAnim = MathHelper.sin((float)(limbSwing * 0.45f)) * limbSwingAmount;
        float walkAnim1 = (MathHelper.cos((float)((limbSwing - temp) * 0.45f)) + temp) * limbSwingAmount;
        float walkAnim2 = (MathHelper.cos((float)((limbSwing - temp + (float)Math.PI * 2) * 0.45f)) + temp) * limbSwingAmount;
        float breatheAnim = MathHelper.sin((float)(ageInTicks * 0.11f));
        float faceYaw = netHeadYaw * (float)Math.PI / 180.0f;
        float facePitch = headPitch * (float)Math.PI / 180.0f;
        if (golem.isThrowing()) {
            this.animateThrow(golem.getThrowingTick());
            float scale1 = 1.0f - MathHelper.clamp((float)((float)golem.getThrowingTick() / 4.0f), (float)0.0f, (float)1.0f);
            walkAnim *= scale1;
        }
        this.head.getModel().rotateAngleX -= breatheAnim * 0.01f;
        this.chest.rotateAngleX -= breatheAnim * 0.01f;
        this.arm1.rotateAngleZ += breatheAnim * 0.03f;
        this.arm2.rotateAngleZ -= breatheAnim * 0.03f;
        this.head.getModel().rotateAngleX += facePitch;
        this.head.getModel().rotateAngleY += faceYaw;
        this.pelvis.rotationPointY += Math.abs(walkAnim) * 1.5f;
        this.abdomen.rotateAngleX += limbSwingAmount * 0.2f;
        this.chest.rotateAngleY -= walkAnim * 0.1f;
        this.head.rotateAngleX -= limbSwingAmount * 0.2f;
        this.arm1.rotateAngleX -= walkAnim * 0.6f;
        this.arm2.rotateAngleX += walkAnim * 0.6f;
        this.forearm1.getModel().rotateAngleX -= walkAnim * 0.2f;
        this.forearm2.getModel().rotateAngleX += walkAnim * 0.2f;
        this.leg1.rotateAngleX += walkAnim1 * 1.1f;
        this.leg2.rotateAngleX += walkAnim2 * 1.1f;
        this.foreleg1.getModel().rotateAngleX += walkAnim * 0.2f;
        this.foreleg2.getModel().rotateAngleX -= walkAnim * 0.2f;
    }

    private void animateThrow(int fullTick) {
        if (fullTick < 7) {
            float tick = ((float)fullTick + this.partialTick) / 7.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += -f * 0.2f;
            this.chest.rotateAngleX += -f * 0.4f;
            this.arm1.rotateAngleX += -f * 1.6f;
            this.arm1.rotateAngleZ += f * 0.8f;
            this.arm2.rotateAngleX += -f * 1.6f;
            this.arm2.rotateAngleZ += -f * 0.8f;
        } else if (fullTick < 10) {
            float tick = ((float)(fullTick - 7) + this.partialTick) / 3.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += -f * 0.4f + 0.2f;
            this.chest.rotateAngleX += -f * 0.6f + 0.2f;
            this.arm1.rotateAngleX += -f * 0.8f - 0.8f;
            this.arm1.rotateAngleZ += 0.8f;
            this.arm2.rotateAngleX += -f * 0.8f - 0.8f;
            this.arm2.rotateAngleZ += -0.8f;
        } else if (fullTick < 14) {
            this.abdomen.rotateAngleX += 0.2f;
            this.chest.rotateAngleX += 0.2f;
            this.arm1.rotateAngleX += -0.8f;
            this.arm1.rotateAngleZ += 0.8f;
            this.arm2.rotateAngleX += -0.8f;
            this.arm2.rotateAngleZ += -0.8f;
        } else if (fullTick < 20) {
            float tick = ((float)(fullTick - 14) + this.partialTick) / 6.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += f * 0.2f;
            this.chest.rotateAngleX += f * 0.2f;
            this.arm1.rotateAngleX += -f * 0.8f;
            this.arm1.rotateAngleZ += f * 0.8f;
            this.arm2.rotateAngleX += -f * 0.8f;
            this.arm2.rotateAngleZ += -f * 0.8f;
        }
    }

    public void postRenderArm(float scale) {
        this.pelvis.postRender(scale);
        this.abdomen.postRender(scale);
        this.chest.postRender(scale);
        this.arm1.postRender(scale);
        this.arm1.getModel().postRender(scale);
        this.forearm1.postRender(scale);
        this.forearm1.getModel().postRender(scale);
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.partialTick = partialTickTime;
    }
}

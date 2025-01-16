package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.client.animationapi.Animator;
import chumbanotz.mutantbeasts.client.animationapi.JointModelRenderer;
import chumbanotz.mutantbeasts.client.model.CrossbowModel;
import chumbanotz.mutantbeasts.client.model.ScalableModelRenderer;
import chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class MutantSkeletonModel
extends ModelBase {
    private final ModelRenderer skeleBase;
    private final ModelRenderer pelvis;
    private final ModelRenderer waist;
    private final Spine[] spine;
    private final ModelRenderer neck;
    private final JointModelRenderer head;
    private final ModelRenderer jaw;
    private final ModelRenderer shoulder1;
    private final ModelRenderer shoulder2;
    private final JointModelRenderer arm1;
    private final JointModelRenderer arm2;
    private final JointModelRenderer forearm1;
    private final JointModelRenderer forearm2;
    private final JointModelRenderer leg1;
    private final JointModelRenderer leg2;
    private final JointModelRenderer foreleg1;
    private final JointModelRenderer foreleg2;
    private final CrossbowModel bow;
    private final Animator animator;
    private float partialTick;

    public MutantSkeletonModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.skeleBase = new ModelRenderer(this);
        this.skeleBase.setRotationPoint(0.0f, 3.0f, 0.0f);
        this.pelvis = new ModelRenderer(this, 0, 16);
        this.pelvis.addBox(-4.0f, -6.0f, -3.0f, 8, 6, 6);
        this.skeleBase.addChild(this.pelvis);
        this.waist = new ModelRenderer(this, 32, 0);
        this.waist.addBox(-2.5f, -8.0f, -2.0f, 5, 8, 4);
        this.waist.setRotationPoint(0.0f, -5.0f, 0.0f);
        this.pelvis.addChild(this.waist);
        this.spine = new Spine[3];
        this.spine[0] = new Spine(this);
        this.spine[0].middle.setRotationPoint(0.0f, -7.0f, 0.0f);
        this.waist.addChild(this.spine[0].middle);
        for (int i = 1; i < this.spine.length; ++i) {
            this.spine[i] = new Spine(this);
            this.spine[i].middle.setRotationPoint(0.0f, -5.0f, 0.0f);
            this.spine[i - 1].middle.addChild(this.spine[i].middle);
        }
        this.neck = new ModelRenderer(this, 64, 0);
        this.neck.addBox(-1.5f, -4.0f, -1.5f, 3, 4, 3);
        this.neck.setRotationPoint(0.0f, -4.0f, 0.0f);
        this.spine[2].middle.addChild(this.neck);
        this.head = new JointModelRenderer(this, 0, 0);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.4f);
        this.head.setRotationPoint(0.0f, -4.0f, -1.0f);
        this.neck.addChild(this.head);
        this.jaw = new ModelRenderer(this, 72, 0);
        this.jaw.addBox(-4.0f, -3.0f, -8.0f, 8, 3, 8, 0.7f);
        this.jaw.setRotationPoint(0.0f, -0.2f, 3.5f);
        this.head.addChild(this.jaw);
        this.shoulder1 = new ModelRenderer(this, 28, 16);
        this.shoulder1.addBox(-4.0f, -3.0f, -3.0f, 8, 3, 6);
        this.shoulder1.setRotationPoint(-7.0f, -3.0f, -1.0f);
        this.spine[2].middle.addChild(this.shoulder1);
        this.shoulder2 = new ModelRenderer(this, 28, 16);
        this.shoulder2.mirror = true;
        this.shoulder2.addBox(-4.0f, -3.0f, -3.0f, 8, 3, 6);
        this.shoulder2.setRotationPoint(7.0f, -3.0f, -1.0f);
        this.spine[2].middle.addChild(this.shoulder2);
        this.arm1 = new JointModelRenderer(this, 0, 28);
        this.arm1.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.arm1.setRotationPoint(-1.0f, -1.0f, 0.0f);
        this.shoulder1.addChild(this.arm1);
        this.arm2 = new JointModelRenderer(this, 0, 28);
        this.arm2.mirror = true;
        this.arm2.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.arm2.setRotationPoint(1.0f, -1.0f, 0.0f);
        this.shoulder2.addChild(this.arm2);
        this.forearm1 = new JointModelRenderer(this, 16, 28);
        this.forearm1.addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, -0.01f);
        this.forearm1.setRotationPoint(0.0f, 11.0f, 0.0f);
        this.arm1.addChild(this.forearm1);
        this.forearm2 = new JointModelRenderer(this, 16, 28);
        this.forearm2.mirror = true;
        this.forearm2.addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, -0.01f);
        this.forearm2.setRotationPoint(0.0f, 11.0f, 0.0f);
        this.arm2.addChild(this.forearm2);
        this.leg1 = new JointModelRenderer(this, 0, 28);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.leg1.setRotationPoint(-2.5f, -2.5f, 0.0f);
        this.pelvis.addChild(this.leg1);
        this.leg2 = new JointModelRenderer(this, 0, 28);
        this.leg2.mirror = true;
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.leg2.setRotationPoint(2.5f, -2.5f, 0.0f);
        this.pelvis.addChild(this.leg2);
        this.foreleg1 = new JointModelRenderer(this, 32, 28);
        this.foreleg1.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.foreleg1.setRotationPoint(0.0f, 12.0f, 0.0f);
        this.leg1.addChild(this.foreleg1);
        this.foreleg2 = new JointModelRenderer(this, 32, 28);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.foreleg2.setRotationPoint(0.0f, 12.0f, 0.0f);
        this.leg2.addChild(this.foreleg2);
        this.bow = new CrossbowModel(this);
        this.bow.armwear.setRotationPoint(0.0f, 8.0f, 0.0f);
        this.forearm1.addChild(this.bow.armwear);
        this.animator = new Animator(this);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.animator.update((MutantSkeletonEntity)entityIn, this.partialTick);
        this.setAngles();
        this.animate((MutantSkeletonEntity)entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.skeleBase.render(scale);
    }

    private void setAngles() {
        this.skeleBase.rotationPointY = 3.0f;
        this.pelvis.rotateAngleX = -0.31415927f;
        this.waist.rotateAngleX = 0.22439948f;
        for (int i = 0; i < this.spine.length; ++i) {
            this.spine[i].setAngles((float)Math.PI, i == 1);
        }
        this.neck.rotateAngleX = -0.1308997f;
        this.head.rotateAngleX = -0.1308997f;
        this.jaw.rotateAngleX = 0.09817477f;
        this.shoulder1.rotateAngleX = -0.7853982f;
        this.shoulder2.rotateAngleX = -0.7853982f;
        this.arm1.getModel().rotateAngleX = 0.5235988f;
        this.arm1.getModel().rotateAngleZ = 0.31415927f;
        this.arm2.getModel().rotateAngleX = 0.5235988f;
        this.arm2.getModel().rotateAngleZ = -0.31415927f;
        this.forearm1.getModel().rotateAngleX = -0.5235988f;
        this.forearm2.getModel().rotateAngleX = -0.5235988f;
        this.leg1.rotateAngleX = -0.2617994f - this.pelvis.rotateAngleX;
        this.leg1.rotateAngleZ = 0.19634955f;
        this.leg2.rotateAngleX = -0.2617994f - this.pelvis.rotateAngleX;
        this.leg2.rotateAngleZ = -0.19634955f;
        this.foreleg1.rotateAngleZ = -0.1308997f;
        this.foreleg1.getModel().rotateAngleX = 0.31415927f;
        this.foreleg2.rotateAngleZ = 0.1308997f;
        this.foreleg2.getModel().rotateAngleX = 0.31415927f;
        this.bow.setAngles((float)Math.PI);
        this.bow.rotateRope();
    }

    private void animate(MutantSkeletonEntity skele, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float f5) {
        float scale;
        float walkAnim1 = MathHelper.sin((float)(limbSwing * 0.5f));
        float walkAnim2 = MathHelper.sin((float)(limbSwing * 0.5f - 1.1f));
        float breatheAnim = MathHelper.sin((float)(ageInTicks * 0.1f));
        float faceYaw = netHeadYaw * (float)Math.PI / 180.0f;
        float facePitch = headPitch * (float)Math.PI / 180.0f;
        if (skele.getAnimationID() == 1) {
            this.animateMelee(skele.getAnimationTick());
            this.bow.rotateRope();
            scale = 1.0f - MathHelper.clamp((float)((float)skele.getAnimationTick() / 4.0f), (float)0.0f, (float)1.0f);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
        } else if (skele.getAnimationID() == 2) {
            this.animateShoot(skele.getAnimationTick(), facePitch, faceYaw);
            scale = 1.0f - MathHelper.clamp((float)((float)skele.getAnimationTick() / 4.0f), (float)0.0f, (float)1.0f);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            facePitch *= scale;
            faceYaw *= scale;
        } else if (skele.getAnimationID() == 3) {
            this.animateMultiShoot(skele.getAnimationTick(), facePitch, faceYaw);
            scale = 1.0f - MathHelper.clamp((float)((float)skele.getAnimationTick() / 4.0f), (float)0.0f, (float)1.0f);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            facePitch *= scale;
            faceYaw *= scale;
        } else if (this.animator.setAnimation(4)) {
            this.animateConstrict();
            this.bow.rotateRope();
            scale = 1.0f - MathHelper.clamp((float)((float)skele.getAnimationTick() / 6.0f), (float)0.0f, (float)1.0f);
            facePitch *= scale;
            faceYaw *= scale;
        } else {
            this.bow.rotateRope();
        }
        this.skeleBase.rotationPointY -= (-0.5f + Math.abs(walkAnim1)) * limbSwingAmount;
        this.spine[0].middle.rotateAngleY -= walkAnim1 * 0.06f * limbSwingAmount;
        this.arm1.rotateAngleX -= walkAnim1 * 0.9f * limbSwingAmount;
        this.arm2.rotateAngleX += walkAnim1 * 0.9f * limbSwingAmount;
        this.leg1.rotateAngleX += (0.2f + walkAnim1) * 1.0f * limbSwingAmount;
        this.leg2.rotateAngleX -= (-0.2f + walkAnim1) * 1.0f * limbSwingAmount;
        this.foreleg1.getModel().rotateAngleX += (0.6f + walkAnim2) * 0.6f * limbSwingAmount;
        this.foreleg2.getModel().rotateAngleX -= (-0.6f + walkAnim2) * 0.6f * limbSwingAmount;
        for (int i = 0; i < this.spine.length; ++i) {
            this.spine[i].animate(breatheAnim);
        }
        this.head.rotateAngleX -= breatheAnim * 0.02f;
        this.jaw.rotateAngleX += breatheAnim * 0.04f + 0.04f;
        this.arm1.rotateAngleZ += breatheAnim * 0.025f;
        this.arm2.rotateAngleZ -= breatheAnim * 0.025f;
        this.head.getModel().rotateAngleX += facePitch;
        this.head.getModel().rotateAngleY += faceYaw;
    }

    private void animateMelee(int fullTick) {
        if (fullTick < 3) {
            float tick = ((float)fullTick + this.partialTick) / 3.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += f * (float)Math.PI / 16.0f;
            }
            this.arm1.rotateAngleY += f * (float)Math.PI / 10.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 4.0f;
            this.arm2.rotateAngleZ += f * (float)(-Math.PI) / 16.0f;
        } else if (fullTick < 5) {
            float tick = ((float)(fullTick - 3) + this.partialTick) / 2.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += f * 0.5890486f - 0.3926991f;
            }
            this.arm1.rotateAngleY += f * 2.7307692f - 2.41661f;
            this.arm1.rotateAngleZ += f * 1.1780972f - 0.3926991f;
            this.arm2.rotateAngleZ += -0.19634955f;
        } else if (fullTick < 8) {
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -0.3926991f;
            }
            this.arm1.rotateAngleY += -2.41661f;
            this.arm1.rotateAngleZ += -0.3926991f;
            this.arm2.rotateAngleZ += -0.19634955f;
        } else if (fullTick < 14) {
            float tick = ((float)(fullTick - 8) + this.partialTick) / 6.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += f * (float)(-Math.PI) / 8.0f;
            }
            this.arm1.rotateAngleY += f * (float)(-Math.PI) / 1.3f;
            this.arm1.rotateAngleZ += f * (float)(-Math.PI) / 8.0f;
            this.arm2.rotateAngleZ += f * (float)(-Math.PI) / 16.0f;
        }
    }

    private void animateShoot(int fullTick, float facePitch, float faceYaw) {
        if (fullTick < 5) {
            float tick = ((float)fullTick + this.partialTick) / 5.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.arm1.getModel().rotateAngleX += -f * (float)Math.PI / 4.0f;
            this.arm1.rotateAngleY += -f * (float)Math.PI / 2.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 16.0f;
            this.forearm1.rotateAngleX += f * (float)Math.PI / 7.0f;
            this.arm2.getModel().rotateAngleX += -f * (float)Math.PI / 4.0f;
            this.arm2.rotateAngleY += f * (float)Math.PI / 2.0f;
            this.arm2.rotateAngleZ += -f * (float)Math.PI / 16.0f;
            this.arm2.getModel().rotateAngleZ += -f * (float)Math.PI / 8.0f;
            this.forearm2.rotateAngleX += -f * (float)Math.PI / 6.0f;
            this.bow.rotateRope();
        } else if (fullTick < 12) {
            float tick = ((float)(fullTick - 5) + this.partialTick) / 7.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            float f1s = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f * 0.4f));
            this.head.getModel().rotateAngleY += f1 * (float)Math.PI / 4.0f;
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -f1 * (float)Math.PI / 12.0f;
                this.spine[i].middle.rotateAngleX += f1 * facePitch / 3.0f;
                this.spine[i].middle.rotateAngleY += f1 * faceYaw / 3.0f;
            }
            this.arm1.getModel().rotateAngleX += f * 0.2617994f - 1.0471976f;
            this.arm1.rotateAngleY += f * -0.9424778f - 0.62831855f;
            this.arm1.rotateAngleZ += f * -0.850848f + 1.0471976f;
            this.forearm1.rotateAngleX += 0.44879895f;
            this.arm2.getModel().rotateAngleX += f * 1.8325956f - 2.6179938f;
            this.arm2.rotateAngleY += f * 0.9424778f + 0.62831855f;
            this.arm2.rotateAngleZ += f * 0.850848f - 1.0471976f;
            this.arm2.getModel().rotateAngleZ += -f * (float)Math.PI / 8.0f;
            this.forearm2.rotateAngleX += f * 0.10471976f - 0.62831855f;
            this.bow.middle1.rotateAngleX += -f1s * (float)Math.PI / 16.0f;
            this.bow.side1.rotateAngleX += -f1s * (float)Math.PI / 24.0f;
            this.bow.middle2.rotateAngleX += f1s * (float)Math.PI / 16.0f;
            this.bow.side2.rotateAngleX += f1s * (float)Math.PI / 24.0f;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f1s * (float)Math.PI / 6.0f;
            this.bow.rope2.rotateAngleX += -f1s * (float)Math.PI / 6.0f;
        } else if (fullTick < 26) {
            this.head.getModel().rotateAngleY += 0.7853982f;
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -0.2617994f;
                this.spine[i].middle.rotateAngleX += facePitch / 3.0f;
                this.spine[i].middle.rotateAngleY += faceYaw / 3.0f;
            }
            this.arm1.getModel().rotateAngleX += -1.0471976f;
            this.arm1.rotateAngleY += -0.62831855f;
            this.arm1.rotateAngleZ += 1.0f;
            this.forearm1.rotateAngleX += 0.44879895f;
            this.arm2.getModel().rotateAngleX += -2.6179938f;
            this.arm2.rotateAngleY += 0.62831855f;
            this.arm2.rotateAngleZ += -1.0471976f;
            this.forearm2.rotateAngleX += -0.62831855f;
            float tick = MathHelper.clamp((float)((float)(fullTick - 25) + this.partialTick), (float)0.0f, (float)1.0f);
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.bow.middle1.rotateAngleX += -f * (float)Math.PI / 16.0f;
            this.bow.side1.rotateAngleX += -f * (float)Math.PI / 24.0f;
            this.bow.middle2.rotateAngleX += f * (float)Math.PI / 16.0f;
            this.bow.side2.rotateAngleX += f * (float)Math.PI / 24.0f;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.bow.rope2.rotateAngleX += -f * (float)Math.PI / 6.0f;
        } else if (fullTick < 30) {
            float tick = ((float)(fullTick - 26) + this.partialTick) / 4.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.head.getModel().rotateAngleY += f * (float)Math.PI / 4.0f;
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -f * (float)Math.PI / 12.0f;
                this.spine[i].middle.rotateAngleX += f * facePitch / 3.0f;
                this.spine[i].middle.rotateAngleY += f * faceYaw / 3.0f;
            }
            this.arm1.getModel().rotateAngleX += -f * (float)Math.PI / 3.0f;
            this.arm1.rotateAngleY += -f * (float)Math.PI / 5.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 3.0f;
            this.forearm1.rotateAngleX += f * (float)Math.PI / 7.0f;
            this.arm2.getModel().rotateAngleX += -f * (float)Math.PI / 1.2f;
            this.arm2.rotateAngleY += f * (float)Math.PI / 5.0f;
            this.arm2.rotateAngleZ += -f * (float)Math.PI / 3.0f;
            this.forearm2.rotateAngleX += -f * (float)Math.PI / 5.0f;
            this.bow.rotateRope();
        }
    }

    protected void animateMultiShoot(int fullTick, float facePitch, float faceYaw) {
        if (fullTick < 10) {
            float tick = ((float)fullTick + this.partialTick) / 10.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.skeleBase.rotationPointY += f * 3.5f;
            this.spine[0].middle.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.head.rotateAngleX += -f * (float)Math.PI / 4.0f;
            this.arm1.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 16.0f;
            this.arm2.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.arm2.rotateAngleZ += -f * (float)Math.PI / 16.0f;
            this.leg1.rotateAngleX += -f * (float)Math.PI / 8.0f;
            this.leg2.rotateAngleX += -f * (float)Math.PI / 8.0f;
            this.foreleg1.getModel().rotateAngleX += f * (float)Math.PI / 4.0f;
            this.foreleg2.getModel().rotateAngleX += f * (float)Math.PI / 4.0f;
            this.bow.rotateRope();
        } else if (fullTick < 12) {
            float tick = ((float)(fullTick - 10) + this.partialTick) / 2.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.skeleBase.rotationPointY += f * 3.5f;
            this.spine[0].middle.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.head.rotateAngleX += -f * (float)Math.PI / 4.0f;
            this.arm1.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 16.0f;
            this.arm2.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.arm2.rotateAngleZ += -f * (float)Math.PI / 16.0f;
            this.leg1.rotateAngleX += -f * (float)Math.PI / 8.0f;
            this.leg2.rotateAngleX += -f * (float)Math.PI / 8.0f;
            this.foreleg1.getModel().rotateAngleX += f * (float)Math.PI / 4.0f;
            this.foreleg2.getModel().rotateAngleX += f * (float)Math.PI / 4.0f;
            this.arm1.rotateAngleZ += -f1 * (float)Math.PI / 14.0f;
            this.arm2.rotateAngleZ += f1 * (float)Math.PI / 14.0f;
            this.leg1.rotateAngleZ += -f1 * (float)Math.PI / 24.0f;
            this.leg2.rotateAngleZ += f1 * (float)Math.PI / 24.0f;
            this.foreleg1.rotateAngleZ += f1 * (float)Math.PI / 64.0f;
            this.foreleg2.rotateAngleZ += -f1 * (float)Math.PI / 64.0f;
            this.bow.rotateRope();
        } else if (fullTick < 14) {
            this.arm1.rotateAngleZ += -0.22439948f;
            this.arm2.rotateAngleZ += 0.22439948f;
            this.leg1.rotateAngleZ += -0.1308997f;
            this.leg2.rotateAngleZ += 0.1308997f;
            this.foreleg1.rotateAngleZ += 0.049087387f;
            this.foreleg2.rotateAngleZ += -0.049087387f;
            this.bow.rotateRope();
        } else if (fullTick < 17) {
            float tick = ((float)(fullTick - 14) + this.partialTick) / 3.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.arm1.rotateAngleZ += -f1 * (float)Math.PI / 14.0f;
            this.arm2.rotateAngleZ += f1 * (float)Math.PI / 14.0f;
            this.leg1.rotateAngleZ += -f1 * (float)Math.PI / 24.0f;
            this.leg2.rotateAngleZ += f1 * (float)Math.PI / 24.0f;
            this.foreleg1.rotateAngleZ += f1 * (float)Math.PI / 64.0f;
            this.foreleg2.rotateAngleZ += -f1 * (float)Math.PI / 64.0f;
            this.arm1.getModel().rotateAngleX += -f * (float)Math.PI / 4.0f;
            this.arm1.rotateAngleY += -f * (float)Math.PI / 2.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 16.0f;
            this.forearm1.rotateAngleX += f * (float)Math.PI / 7.0f;
            this.arm2.getModel().rotateAngleX += -f * (float)Math.PI / 4.0f;
            this.arm2.rotateAngleY += f * (float)Math.PI / 2.0f;
            this.arm2.rotateAngleZ += -f * (float)Math.PI / 16.0f;
            this.arm2.getModel().rotateAngleZ += -f * (float)Math.PI / 8.0f;
            this.forearm2.rotateAngleX += -f * (float)Math.PI / 6.0f;
            this.bow.rotateRope();
        } else if (fullTick < 20) {
            float tick = ((float)(fullTick - 17) + this.partialTick) / 3.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            float f1s = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f * 0.4f));
            this.head.getModel().rotateAngleY += f1 * (float)Math.PI / 4.0f;
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -f1 * (float)Math.PI / 12.0f;
                this.spine[i].middle.rotateAngleX += f1 * facePitch / 3.0f;
                this.spine[i].middle.rotateAngleY += f1 * faceYaw / 3.0f;
            }
            this.arm1.getModel().rotateAngleX += f * 0.2617994f - 1.0471976f;
            this.arm1.rotateAngleY += f * -0.9424778f - 0.62831855f;
            this.arm1.rotateAngleZ += f * -0.850848f + 1.0471976f;
            this.forearm1.rotateAngleX += 0.44879895f;
            this.arm2.getModel().rotateAngleX += f * 1.8325956f - 2.6179938f;
            this.arm2.rotateAngleY += f * 0.9424778f + 0.62831855f;
            this.arm2.rotateAngleZ += f * 0.850848f - 1.0471976f;
            this.arm2.getModel().rotateAngleZ += -f * (float)Math.PI / 8.0f;
            this.forearm2.rotateAngleX += f * 0.10471976f - 0.62831855f;
            this.bow.middle1.rotateAngleX += -f1s * (float)Math.PI / 16.0f;
            this.bow.side1.rotateAngleX += -f1s * (float)Math.PI / 24.0f;
            this.bow.middle2.rotateAngleX += f1s * (float)Math.PI / 16.0f;
            this.bow.side2.rotateAngleX += f1s * (float)Math.PI / 24.0f;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f1s * (float)Math.PI / 6.0f;
            this.bow.rope2.rotateAngleX += -f1s * (float)Math.PI / 6.0f;
        } else if (fullTick < 24) {
            this.head.getModel().rotateAngleY += 0.7853982f;
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -0.2617994f;
                this.spine[i].middle.rotateAngleX += facePitch / 3.0f;
                this.spine[i].middle.rotateAngleY += faceYaw / 3.0f;
            }
            this.arm1.getModel().rotateAngleX += -1.0471976f;
            this.arm1.rotateAngleY += -0.62831855f;
            this.arm1.rotateAngleZ += 1.0f;
            this.forearm1.rotateAngleX += 0.44879895f;
            this.arm2.getModel().rotateAngleX += -2.6179938f;
            this.arm2.rotateAngleY += 0.62831855f;
            this.arm2.rotateAngleZ += -1.0471976f;
            this.forearm2.rotateAngleX += -0.62831855f;
            float tick = MathHelper.clamp((float)((float)(fullTick - 25) + this.partialTick), (float)0.0f, (float)1.0f);
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.bow.middle1.rotateAngleX += -f * (float)Math.PI / 16.0f;
            this.bow.side1.rotateAngleX += -f * (float)Math.PI / 24.0f;
            this.bow.middle2.rotateAngleX += f * (float)Math.PI / 16.0f;
            this.bow.side2.rotateAngleX += f * (float)Math.PI / 24.0f;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.bow.rope2.rotateAngleX += -f * (float)Math.PI / 6.0f;
        } else if (fullTick < 28) {
            float tick = ((float)(fullTick - 24) + this.partialTick) / 4.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.head.getModel().rotateAngleY += f * (float)Math.PI / 4.0f;
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].middle.rotateAngleY += -f * (float)Math.PI / 12.0f;
                this.spine[i].middle.rotateAngleX += f * facePitch / 3.0f;
                this.spine[i].middle.rotateAngleY += f * faceYaw / 3.0f;
            }
            this.arm1.getModel().rotateAngleX += -f * (float)Math.PI / 3.0f;
            this.arm1.rotateAngleY += -f * (float)Math.PI / 5.0f;
            this.arm1.rotateAngleZ += f * (float)Math.PI / 3.0f;
            this.forearm1.rotateAngleX += f * (float)Math.PI / 7.0f;
            this.arm2.getModel().rotateAngleX += -f * (float)Math.PI / 1.2f;
            this.arm2.rotateAngleY += f * (float)Math.PI / 5.0f;
            this.arm2.rotateAngleZ += -f * (float)Math.PI / 3.0f;
            this.forearm2.rotateAngleX += -f * (float)Math.PI / 5.0f;
            this.bow.rotateRope();
        }
    }

    private void animateConstrict() {
        block6: {
            float f;
            float tick;
            int animTick;
            block7: {
                block5: {
                    this.animator.startPhase(5);
                    this.animator.rotate(this.waist, 0.1308997f, 0.0f, 0.0f);
                    for (animTick = 0; animTick < this.spine.length; ++animTick) {
                        tick = animTick == 0 ? 0.3926991f : (animTick == 2 ? -0.3926991f : 0.0f);
                        f = animTick == 1 ? 0.3926991f : 0.31415927f;
                        this.animator.rotate(this.spine[animTick].side1[0], tick, f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side1[1], 0.0f, 0.15707964f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side1[2], 0.0f, 0.2617994f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side2[0], tick, -f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side2[1], 0.0f, -0.15707964f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side2[2], 0.0f, -0.2617994f, 0.0f);
                    }
                    this.animator.rotate(this.arm1, 0.0f, 0.0f, 0.8975979f);
                    this.animator.rotate(this.arm2, 0.0f, 0.0f, -0.8975979f);
                    this.animator.move(this.skeleBase, 0.0f, 1.0f, 0.0f);
                    this.animator.rotate(this.leg1, -0.44879895f, 0.0f, 0.0f);
                    this.animator.rotate(this.leg2, -0.44879895f, 0.0f, 0.0f);
                    this.animator.rotate(this.foreleg1.getModel(), 0.5235988f, 0.0f, 0.0f);
                    this.animator.rotate(this.foreleg2.getModel(), 0.5235988f, 0.0f, 0.0f);
                    this.animator.endPhase();
                    this.animator.setStationaryPhase(2);
                    this.animator.startPhase(1);
                    this.animator.rotate(this.neck, 0.19634955f, 0.0f, 0.0f);
                    this.animator.rotate(this.head, 0.15707964f, 0.0f, 0.0f);
                    this.animator.rotate(this.waist, 0.31415927f, 0.0f, 0.0f);
                    this.animator.rotate(this.spine[0].middle, 0.2617994f, 0.0f, 0.0f);
                    for (animTick = 0; animTick < this.spine.length; ++animTick) {
                        tick = animTick == 0 ? 0.1308997f : (animTick == 2 ? -0.1308997f : 0.0f);
                        f = animTick == 1 ? -0.17453294f : -0.22439948f;
                        this.animator.rotate(this.spine[animTick].side1[0], tick - 0.08f, f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side1[1], 0.0f, 0.15707964f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side1[2], 0.0f, 0.2617994f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side2[0], tick + 0.08f, -f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side2[1], 0.0f, -0.15707964f, 0.0f);
                        this.animator.rotate(this.spine[animTick].side2[2], 0.0f, -0.2617994f, 0.0f);
                    }
                    this.animator.move(this.skeleBase, 0.0f, 1.0f, 0.0f);
                    this.animator.rotate(this.leg1, -0.44879895f, 0.0f, 0.0f);
                    this.animator.rotate(this.leg2, -0.44879895f, 0.0f, 0.0f);
                    this.animator.rotate(this.foreleg1.getModel(), 0.5235988f, 0.0f, 0.0f);
                    this.animator.rotate(this.foreleg2.getModel(), 0.5235988f, 0.0f, 0.0f);
                    this.animator.endPhase();
                    this.animator.setStationaryPhase(4);
                    this.animator.resetPhase(8);
                    animTick = this.animator.getEntity().getAnimationTick();
                    if (animTick >= 5) break block5;
                    tick = ((float)animTick + this.partialTick) / 5.0f;
                    f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
                    for (int i = 0; i < this.spine.length; ++i) {
                        this.spine[i].side1[0].setScale(1.0f + f * 0.6f);
                        this.spine[i].side2[0].setScale(1.0f + f * 0.6f);
                    }
                    break block6;
                }
                if (animTick >= 12) break block7;
                for (int i = 0; i < this.spine.length; ++i) {
                    this.spine[i].side1[0].setScale(1.6f);
                    this.spine[i].side2[0].setScale(1.6f);
                }
                break block6;
            }
            if (animTick >= 20) break block6;
            tick = ((float)(animTick - 12) + this.partialTick) / 8.0f;
            f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            for (int i = 0; i < this.spine.length; ++i) {
                this.spine[i].side1[0].setScale(1.0f + f * 0.6f);
                this.spine[i].side2[0].setScale(1.0f + f * 0.6f);
            }
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.partialTick = partialTickTime;
    }

    static class Spine {
        public final ModelRenderer middle;
        public final ScalableModelRenderer[] side1;
        public final ScalableModelRenderer[] side2;

        public Spine(ModelBase model) {
            this(model, false);
        }

        public Spine(ModelBase model, boolean skeletonPart) {
            this.middle = new ModelRenderer(model, 50, 0);
            this.middle.addBox(-2.5f, -4.0f, -2.0f, 5, 4, 4, 0.5f);
            this.side1 = new ScalableModelRenderer[3];
            this.side2 = new ScalableModelRenderer[3];
            this.side1[0] = new ScalableModelRenderer(model, 32, 12);
            this.side1[0].addBox(skeletonPart ? 0.0f : -6.0f, -2.0f, -2.0f, 6, 2, 2, 0.25f);
            if (!skeletonPart) {
                this.side1[0].setRotationPoint(-3.0f, -1.0f, 1.75f);
            }
            this.middle.addChild(this.side1[0]);
            this.side2[0] = new ScalableModelRenderer(model, 32, 12);
            this.side2[0].mirror = true;
            this.side2[0].addBox(skeletonPart ? -6.0f : 0.0f, -2.0f, -2.0f, 6, 2, 2, 0.25f);
            if (!skeletonPart) {
                this.side2[0].setRotationPoint(3.0f, -1.0f, 1.75f);
            }
            this.middle.addChild(this.side2[0]);
            this.side1[1] = new ScalableModelRenderer(model, 32, 12);
            this.side1[1].mirror = true;
            this.side1[1].addBox(-6.0f, -2.0f, -2.0f, 6, 2, 2, 0.2f);
            this.side1[1].setRotationPoint(skeletonPart ? -0.5f : -6.5f, 0.0f, 0.0f);
            this.side1[0].addChild(this.side1[1]);
            this.side2[1] = new ScalableModelRenderer(model, 32, 12);
            this.side2[1].addBox(0.0f, -2.0f, -2.0f, 6, 2, 2, 0.2f);
            this.side2[1].setRotationPoint(skeletonPart ? 0.5f : 6.5f, 0.0f, 0.0f);
            this.side2[0].addChild(this.side2[1]);
            this.side1[2] = new ScalableModelRenderer(model, 32, 12);
            this.side1[2].addBox(-6.0f, -2.0f, -2.0f, 6, 2, 2, 0.15f);
            this.side1[2].setRotationPoint(-6.4f, 0.0f, 0.0f);
            this.side1[1].addChild(this.side1[2]);
            this.side2[2] = new ScalableModelRenderer(model, 32, 12);
            this.side2[2].mirror = true;
            this.side2[2].addBox(0.0f, -2.0f, -2.0f, 6, 2, 2, 0.15f);
            this.side2[2].setRotationPoint(6.4f, 0.0f, 0.0f);
            this.side2[1].addChild(this.side2[2]);
        }

        private void resetAngles(ModelRenderer ... boxes) {
            for (ModelRenderer box : boxes) {
                box.rotateAngleX = 0.0f;
                box.rotateAngleY = 0.0f;
                box.rotateAngleZ = 0.0f;
            }
        }

        public void setAngles(float PI, boolean middleSpine) {
            this.resetAngles(this.middle);
            this.resetAngles(this.side1);
            this.resetAngles(this.side2);
            this.middle.rotateAngleX = PI / 18.0f;
            this.side1[0].rotateAngleY = -PI / 4.5f;
            this.side2[0].rotateAngleY = PI / 4.5f;
            this.side1[1].rotateAngleY = -PI / 3.0f;
            this.side2[1].rotateAngleY = PI / 3.0f;
            this.side1[2].rotateAngleY = -PI / 3.5f;
            this.side2[2].rotateAngleY = PI / 3.5f;
            if (middleSpine) {
                for (int i = 0; i < this.side1.length; ++i) {
                    this.side1[i].rotateAngleY *= 0.98f;
                    this.side2[i].rotateAngleY *= 0.98f;
                }
            }
            this.side1[0].setScale(1.0f);
            this.side2[0].setScale(1.0f);
        }

        public void animate(float breatheAnim) {
            this.side1[1].rotateAngleY += breatheAnim * 0.02f;
            this.side2[1].rotateAngleY -= breatheAnim * 0.02f;
        }
    }
}

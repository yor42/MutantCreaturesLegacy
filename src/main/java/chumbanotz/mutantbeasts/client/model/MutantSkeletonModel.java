package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.client.animationapi.Animator;
import chumbanotz.mutantbeasts.client.animationapi.IAnimatedEntity;
import chumbanotz.mutantbeasts.client.animationapi.JointModelRenderer;
import chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class MutantSkeletonModel extends ModelBase {
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
        this.skeleBase.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.pelvis = new ModelRenderer(this, 0, 16);
        this.pelvis.addBox(-4.0F, -6.0F, -3.0F, 8, 6, 6);
        this.skeleBase.addChild(this.pelvis);
        this.waist = new ModelRenderer(this, 32, 0);
        this.waist.addBox(-2.5F, -8.0F, -2.0F, 5, 8, 4);
        this.waist.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.pelvis.addChild(this.waist);
        this.spine = new Spine[3];
        this.spine[0] = new Spine(this);
        (this.spine[0]).middle.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.waist.addChild((this.spine[0]).middle);
        for (int i = 1; i < this.spine.length; i++) {
            this.spine[i] = new Spine(this);
            (this.spine[i]).middle.setRotationPoint(0.0F, -5.0F, 0.0F);
            (this.spine[i - 1]).middle.addChild((this.spine[i]).middle);
        }
        this.neck = new ModelRenderer(this, 64, 0);
        this.neck.addBox(-1.5F, -4.0F, -1.5F, 3, 4, 3);
        this.neck.setRotationPoint(0.0F, -4.0F, 0.0F);
        (this.spine[2]).middle.addChild(this.neck);
        this.head = new JointModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.4F);
        this.head.setRotationPoint(0.0F, -4.0F, -1.0F);
        this.neck.addChild(this.head);
        this.jaw = new ModelRenderer(this, 72, 0);
        this.jaw.addBox(-4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F);
        this.jaw.setRotationPoint(0.0F, -0.2F, 3.5F);
        this.head.addChild(this.jaw);
        this.shoulder1 = new ModelRenderer(this, 28, 16);
        this.shoulder1.addBox(-4.0F, -3.0F, -3.0F, 8, 3, 6);
        this.shoulder1.setRotationPoint(-7.0F, -3.0F, -1.0F);
        (this.spine[2]).middle.addChild(this.shoulder1);
        this.shoulder2 = new ModelRenderer(this, 28, 16);
        this.shoulder2.mirror = true;
        this.shoulder2.addBox(-4.0F, -3.0F, -3.0F, 8, 3, 6);
        this.shoulder2.setRotationPoint(7.0F, -3.0F, -1.0F);
        (this.spine[2]).middle.addChild(this.shoulder2);
        this.arm1 = new JointModelRenderer(this, 0, 28);
        this.arm1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.arm1.setRotationPoint(-1.0F, -1.0F, 0.0F);
        this.shoulder1.addChild(this.arm1);
        this.arm2 = new JointModelRenderer(this, 0, 28);
        this.arm2.mirror = true;
        this.arm2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.arm2.setRotationPoint(1.0F, -1.0F, 0.0F);
        this.shoulder2.addChild(this.arm2);
        this.forearm1 = new JointModelRenderer(this, 16, 28);
        this.forearm1.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, -0.01F);
        this.forearm1.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.arm1.addChild(this.forearm1);
        this.forearm2 = new JointModelRenderer(this, 16, 28);
        this.forearm2.mirror = true;
        this.forearm2.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, -0.01F);
        this.forearm2.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.arm2.addChild(this.forearm2);
        this.leg1 = new JointModelRenderer(this, 0, 28);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.leg1.setRotationPoint(-2.5F, -2.5F, 0.0F);
        this.pelvis.addChild(this.leg1);
        this.leg2 = new JointModelRenderer(this, 0, 28);
        this.leg2.mirror = true;
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.leg2.setRotationPoint(2.5F, -2.5F, 0.0F);
        this.pelvis.addChild(this.leg2);
        this.foreleg1 = new JointModelRenderer(this, 32, 28);
        this.foreleg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.foreleg1.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.leg1.addChild(this.foreleg1);
        this.foreleg2 = new JointModelRenderer(this, 32, 28);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.foreleg2.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.leg2.addChild(this.foreleg2);
        this.bow = new CrossbowModel(this);
        this.bow.armwear.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.forearm1.addChild(this.bow.armwear);
        this.animator = new Animator(this);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.animator.update((IAnimatedEntity) entityIn, this.partialTick);
        setAngles();
        animate((MutantSkeletonEntity) entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.skeleBase.render(scale);
    }

    private void setAngles() {
        this.skeleBase.rotationPointY = 3.0F;
        this.pelvis.rotateAngleX = -0.31415927F;
        this.waist.rotateAngleX = 0.22439948F;
        for (int i = 0; i < this.spine.length; i++)
            this.spine[i].setAngles(3.1415927F, (i == 1));
        this.neck.rotateAngleX = -0.1308997F;
        this.head.rotateAngleX = -0.1308997F;
        this.jaw.rotateAngleX = 0.09817477F;
        this.shoulder1.rotateAngleX = -0.7853982F;
        this.shoulder2.rotateAngleX = -0.7853982F;
        (this.arm1.getModel()).rotateAngleX = 0.5235988F;
        (this.arm1.getModel()).rotateAngleZ = 0.31415927F;
        (this.arm2.getModel()).rotateAngleX = 0.5235988F;
        (this.arm2.getModel()).rotateAngleZ = -0.31415927F;
        (this.forearm1.getModel()).rotateAngleX = -0.5235988F;
        (this.forearm2.getModel()).rotateAngleX = -0.5235988F;
        this.leg1.rotateAngleX = -0.2617994F - this.pelvis.rotateAngleX;
        this.leg1.rotateAngleZ = 0.19634955F;
        this.leg2.rotateAngleX = -0.2617994F - this.pelvis.rotateAngleX;
        this.leg2.rotateAngleZ = -0.19634955F;
        this.foreleg1.rotateAngleZ = -0.1308997F;
        (this.foreleg1.getModel()).rotateAngleX = 0.31415927F;
        this.foreleg2.rotateAngleZ = 0.1308997F;
        (this.foreleg2.getModel()).rotateAngleX = 0.31415927F;
        this.bow.setAngles(3.1415927F);
        this.bow.rotateRope();
    }

    private void animate(MutantSkeletonEntity skele, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float f5) {
        float walkAnim1 = MathHelper.sin(limbSwing * 0.5F);
        float walkAnim2 = MathHelper.sin(limbSwing * 0.5F - 1.1F);
        float breatheAnim = MathHelper.sin(ageInTicks * 0.1F);
        float faceYaw = netHeadYaw * 3.1415927F / 180.0F;
        float facePitch = headPitch * 3.1415927F / 180.0F;
        if (skele.getAnimationID() == 1) {
            animateMelee(skele.getAnimationTick());
            this.bow.rotateRope();
            float scale = 1.0F - MathHelper.clamp(skele.getAnimationTick() / 4.0F, 0.0F, 1.0F);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
        } else if (skele.getAnimationID() == 2) {
            animateShoot(skele.getAnimationTick(), facePitch, faceYaw);
            float scale = 1.0F - MathHelper.clamp(skele.getAnimationTick() / 4.0F, 0.0F, 1.0F);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            facePitch *= scale;
            faceYaw *= scale;
        } else if (skele.getAnimationID() == 3) {
            animateMultiShoot(skele.getAnimationTick(), facePitch, faceYaw);
            float scale = 1.0F - MathHelper.clamp(skele.getAnimationTick() / 4.0F, 0.0F, 1.0F);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            facePitch *= scale;
            faceYaw *= scale;
        } else if (this.animator.setAnimation(4)) {
            animateConstrict();
            this.bow.rotateRope();
            float scale = 1.0F - MathHelper.clamp(skele.getAnimationTick() / 6.0F, 0.0F, 1.0F);
            facePitch *= scale;
            faceYaw *= scale;
        } else {
            this.bow.rotateRope();
        }
        this.skeleBase.rotationPointY -= (-0.5F + Math.abs(walkAnim1)) * limbSwingAmount;
        (this.spine[0]).middle.rotateAngleY -= walkAnim1 * 0.06F * limbSwingAmount;
        this.arm1.rotateAngleX -= walkAnim1 * 0.9F * limbSwingAmount;
        this.arm2.rotateAngleX += walkAnim1 * 0.9F * limbSwingAmount;
        this.leg1.rotateAngleX += (0.2F + walkAnim1) * 1.0F * limbSwingAmount;
        this.leg2.rotateAngleX -= (-0.2F + walkAnim1) * 1.0F * limbSwingAmount;
        (this.foreleg1.getModel()).rotateAngleX += (0.6F + walkAnim2) * 0.6F * limbSwingAmount;
        (this.foreleg2.getModel()).rotateAngleX -= (-0.6F + walkAnim2) * 0.6F * limbSwingAmount;
        for (Spine value : this.spine) value.animate(breatheAnim);
        this.head.rotateAngleX -= breatheAnim * 0.02F;
        this.jaw.rotateAngleX += breatheAnim * 0.04F + 0.04F;
        this.arm1.rotateAngleZ += breatheAnim * 0.025F;
        this.arm2.rotateAngleZ -= breatheAnim * 0.025F;
        (this.head.getModel()).rotateAngleX += facePitch;
        (this.head.getModel()).rotateAngleY += faceYaw;
    }

    private void animateMelee(int fullTick) {
        if (fullTick < 3) {
            float tick = (fullTick + this.partialTick) / 3.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            for (Spine value : this.spine) value.middle.rotateAngleY += f * 3.1415927F / 16.0F;
            this.arm1.rotateAngleY += f * 3.1415927F / 10.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 4.0F;
            this.arm2.rotateAngleZ += f * -3.1415927F / 16.0F;
        } else if (fullTick < 5) {
            float tick = ((fullTick - 3) + this.partialTick) / 2.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            for (Spine value : this.spine) value.middle.rotateAngleY += f * 0.5890486F - 0.3926991F;
            this.arm1.rotateAngleY += f * 2.7307692F - 2.41661F;
            this.arm1.rotateAngleZ += f * 1.1780972F - 0.3926991F;
            this.arm2.rotateAngleZ -= 0.19634955F;
        } else if (fullTick < 8) {
            for (Spine value : this.spine) value.middle.rotateAngleY -= 0.3926991F;
            this.arm1.rotateAngleY -= 2.41661F;
            this.arm1.rotateAngleZ -= 0.3926991F;
            this.arm2.rotateAngleZ -= 0.19634955F;
        } else if (fullTick < 14) {
            float tick = ((fullTick - 8) + this.partialTick) / 6.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            for (Spine value : this.spine) value.middle.rotateAngleY += f * -3.1415927F / 8.0F;
            this.arm1.rotateAngleY += f * -3.1415927F / 1.3F;
            this.arm1.rotateAngleZ += f * -3.1415927F / 8.0F;
            this.arm2.rotateAngleZ += f * -3.1415927F / 16.0F;
        }
    }

    private void animateShoot(int fullTick, float facePitch, float faceYaw) {
        if (fullTick < 5) {
            float tick = (fullTick + this.partialTick) / 5.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            (this.arm1.getModel()).rotateAngleX += -f * 3.1415927F / 4.0F;
            this.arm1.rotateAngleY += -f * 3.1415927F / 2.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 16.0F;
            this.forearm1.rotateAngleX += f * 3.1415927F / 7.0F;
            (this.arm2.getModel()).rotateAngleX += -f * 3.1415927F / 4.0F;
            this.arm2.rotateAngleY += f * 3.1415927F / 2.0F;
            this.arm2.rotateAngleZ += -f * 3.1415927F / 16.0F;
            (this.arm2.getModel()).rotateAngleZ += -f * 3.1415927F / 8.0F;
            this.forearm2.rotateAngleX += -f * 3.1415927F / 6.0F;
            this.bow.rotateRope();
        } else if (fullTick < 12) {
            float tick = ((fullTick - 5) + this.partialTick) / 7.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            float f1s = MathHelper.sin(tick * 3.1415927F / 2.0F * 0.4F);
            (this.head.getModel()).rotateAngleY += f1 * 3.1415927F / 4.0F;
            for (Spine value : this.spine) {
                value.middle.rotateAngleY += -f1 * 3.1415927F / 12.0F;
                value.middle.rotateAngleX += f1 * facePitch / 3.0F;
                value.middle.rotateAngleY += f1 * faceYaw / 3.0F;
            }
            (this.arm1.getModel()).rotateAngleX += f * 0.2617994F - 1.0471976F;
            this.arm1.rotateAngleY += f * -0.9424778F - 0.62831855F;
            this.arm1.rotateAngleZ += f * -0.850848F + 1.0471976F;
            this.forearm1.rotateAngleX += 0.44879895F;
            (this.arm2.getModel()).rotateAngleX += f * 1.8325956F - 2.6179938F;
            this.arm2.rotateAngleY += f * 0.9424778F + 0.62831855F;
            this.arm2.rotateAngleZ += f * 0.850848F - 1.0471976F;
            (this.arm2.getModel()).rotateAngleZ += -f * 3.1415927F / 8.0F;
            this.forearm2.rotateAngleX += f * 0.10471976F - 0.62831855F;
            this.bow.middle1.rotateAngleX += -f1s * 3.1415927F / 16.0F;
            this.bow.side1.rotateAngleX += -f1s * 3.1415927F / 24.0F;
            this.bow.middle2.rotateAngleX += f1s * 3.1415927F / 16.0F;
            this.bow.side2.rotateAngleX += f1s * 3.1415927F / 24.0F;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f1s * 3.1415927F / 6.0F;
            this.bow.rope2.rotateAngleX += -f1s * 3.1415927F / 6.0F;
        } else if (fullTick < 26) {
            (this.head.getModel()).rotateAngleY += 0.7853982F;
            for (Spine value : this.spine) {
                value.middle.rotateAngleY -= 0.2617994F;
                value.middle.rotateAngleX += facePitch / 3.0F;
                value.middle.rotateAngleY += faceYaw / 3.0F;
            }
            (this.arm1.getModel()).rotateAngleX -= 1.0471976F;
            this.arm1.rotateAngleY -= 0.62831855F;
            this.arm1.rotateAngleZ++;
            this.forearm1.rotateAngleX += 0.44879895F;
            (this.arm2.getModel()).rotateAngleX -= 2.6179938F;
            this.arm2.rotateAngleY += 0.62831855F;
            this.arm2.rotateAngleZ -= 1.0471976F;
            this.forearm2.rotateAngleX -= 0.62831855F;
            float tick = MathHelper.clamp((fullTick - 25) + this.partialTick, 0.0F, 1.0F);
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.bow.middle1.rotateAngleX += -f * 3.1415927F / 16.0F;
            this.bow.side1.rotateAngleX += -f * 3.1415927F / 24.0F;
            this.bow.middle2.rotateAngleX += f * 3.1415927F / 16.0F;
            this.bow.side2.rotateAngleX += f * 3.1415927F / 24.0F;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f * 3.1415927F / 6.0F;
            this.bow.rope2.rotateAngleX += -f * 3.1415927F / 6.0F;
        } else if (fullTick < 30) {
            float tick = ((fullTick - 26) + this.partialTick) / 4.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            (this.head.getModel()).rotateAngleY += f * 3.1415927F / 4.0F;
            for (Spine value : this.spine) {
                value.middle.rotateAngleY += -f * 3.1415927F / 12.0F;
                value.middle.rotateAngleX += f * facePitch / 3.0F;
                value.middle.rotateAngleY += f * faceYaw / 3.0F;
            }
            (this.arm1.getModel()).rotateAngleX += -f * 3.1415927F / 3.0F;
            this.arm1.rotateAngleY += -f * 3.1415927F / 5.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 3.0F;
            this.forearm1.rotateAngleX += f * 3.1415927F / 7.0F;
            (this.arm2.getModel()).rotateAngleX += -f * 3.1415927F / 1.2F;
            this.arm2.rotateAngleY += f * 3.1415927F / 5.0F;
            this.arm2.rotateAngleZ += -f * 3.1415927F / 3.0F;
            this.forearm2.rotateAngleX += -f * 3.1415927F / 5.0F;
            this.bow.rotateRope();
        }
    }

    protected void animateMultiShoot(int fullTick, float facePitch, float faceYaw) {
        if (fullTick < 10) {
            float tick = (fullTick + this.partialTick) / 10.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.skeleBase.rotationPointY += f * 3.5F;
            (this.spine[0]).middle.rotateAngleX += f * 3.1415927F / 6.0F;
            this.head.rotateAngleX += -f * 3.1415927F / 4.0F;
            this.arm1.rotateAngleX += f * 3.1415927F / 6.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 16.0F;
            this.arm2.rotateAngleX += f * 3.1415927F / 6.0F;
            this.arm2.rotateAngleZ += -f * 3.1415927F / 16.0F;
            this.leg1.rotateAngleX += -f * 3.1415927F / 8.0F;
            this.leg2.rotateAngleX += -f * 3.1415927F / 8.0F;
            (this.foreleg1.getModel()).rotateAngleX += f * 3.1415927F / 4.0F;
            (this.foreleg2.getModel()).rotateAngleX += f * 3.1415927F / 4.0F;
            this.bow.rotateRope();
        } else if (fullTick < 12) {
            float tick = ((fullTick - 10) + this.partialTick) / 2.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.skeleBase.rotationPointY += f * 3.5F;
            (this.spine[0]).middle.rotateAngleX += f * 3.1415927F / 6.0F;
            this.head.rotateAngleX += -f * 3.1415927F / 4.0F;
            this.arm1.rotateAngleX += f * 3.1415927F / 6.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 16.0F;
            this.arm2.rotateAngleX += f * 3.1415927F / 6.0F;
            this.arm2.rotateAngleZ += -f * 3.1415927F / 16.0F;
            this.leg1.rotateAngleX += -f * 3.1415927F / 8.0F;
            this.leg2.rotateAngleX += -f * 3.1415927F / 8.0F;
            (this.foreleg1.getModel()).rotateAngleX += f * 3.1415927F / 4.0F;
            (this.foreleg2.getModel()).rotateAngleX += f * 3.1415927F / 4.0F;
            this.arm1.rotateAngleZ += -f1 * 3.1415927F / 14.0F;
            this.arm2.rotateAngleZ += f1 * 3.1415927F / 14.0F;
            this.leg1.rotateAngleZ += -f1 * 3.1415927F / 24.0F;
            this.leg2.rotateAngleZ += f1 * 3.1415927F / 24.0F;
            this.foreleg1.rotateAngleZ += f1 * 3.1415927F / 64.0F;
            this.foreleg2.rotateAngleZ += -f1 * 3.1415927F / 64.0F;
            this.bow.rotateRope();
        } else if (fullTick < 14) {
            this.arm1.rotateAngleZ -= 0.22439948F;
            this.arm2.rotateAngleZ += 0.22439948F;
            this.leg1.rotateAngleZ -= 0.1308997F;
            this.leg2.rotateAngleZ += 0.1308997F;
            this.foreleg1.rotateAngleZ += 0.049087387F;
            this.foreleg2.rotateAngleZ -= 0.049087387F;
            this.bow.rotateRope();
        } else if (fullTick < 17) {
            float tick = ((fullTick - 14) + this.partialTick) / 3.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.arm1.rotateAngleZ += -f1 * 3.1415927F / 14.0F;
            this.arm2.rotateAngleZ += f1 * 3.1415927F / 14.0F;
            this.leg1.rotateAngleZ += -f1 * 3.1415927F / 24.0F;
            this.leg2.rotateAngleZ += f1 * 3.1415927F / 24.0F;
            this.foreleg1.rotateAngleZ += f1 * 3.1415927F / 64.0F;
            this.foreleg2.rotateAngleZ += -f1 * 3.1415927F / 64.0F;
            (this.arm1.getModel()).rotateAngleX += -f * 3.1415927F / 4.0F;
            this.arm1.rotateAngleY += -f * 3.1415927F / 2.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 16.0F;
            this.forearm1.rotateAngleX += f * 3.1415927F / 7.0F;
            (this.arm2.getModel()).rotateAngleX += -f * 3.1415927F / 4.0F;
            this.arm2.rotateAngleY += f * 3.1415927F / 2.0F;
            this.arm2.rotateAngleZ += -f * 3.1415927F / 16.0F;
            (this.arm2.getModel()).rotateAngleZ += -f * 3.1415927F / 8.0F;
            this.forearm2.rotateAngleX += -f * 3.1415927F / 6.0F;
            this.bow.rotateRope();
        } else if (fullTick < 20) {
            float tick = ((fullTick - 17) + this.partialTick) / 3.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            float f1s = MathHelper.sin(tick * 3.1415927F / 2.0F * 0.4F);
            (this.head.getModel()).rotateAngleY += f1 * 3.1415927F / 4.0F;
            for (Spine value : this.spine) {
                value.middle.rotateAngleY += -f1 * 3.1415927F / 12.0F;
                value.middle.rotateAngleX += f1 * facePitch / 3.0F;
                value.middle.rotateAngleY += f1 * faceYaw / 3.0F;
            }
            (this.arm1.getModel()).rotateAngleX += f * 0.2617994F - 1.0471976F;
            this.arm1.rotateAngleY += f * -0.9424778F - 0.62831855F;
            this.arm1.rotateAngleZ += f * -0.850848F + 1.0471976F;
            this.forearm1.rotateAngleX += 0.44879895F;
            (this.arm2.getModel()).rotateAngleX += f * 1.8325956F - 2.6179938F;
            this.arm2.rotateAngleY += f * 0.9424778F + 0.62831855F;
            this.arm2.rotateAngleZ += f * 0.850848F - 1.0471976F;
            (this.arm2.getModel()).rotateAngleZ += -f * 3.1415927F / 8.0F;
            this.forearm2.rotateAngleX += f * 0.10471976F - 0.62831855F;
            this.bow.middle1.rotateAngleX += -f1s * 3.1415927F / 16.0F;
            this.bow.side1.rotateAngleX += -f1s * 3.1415927F / 24.0F;
            this.bow.middle2.rotateAngleX += f1s * 3.1415927F / 16.0F;
            this.bow.side2.rotateAngleX += f1s * 3.1415927F / 24.0F;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f1s * 3.1415927F / 6.0F;
            this.bow.rope2.rotateAngleX += -f1s * 3.1415927F / 6.0F;
        } else if (fullTick < 24) {
            (this.head.getModel()).rotateAngleY += 0.7853982F;
            for (Spine value : this.spine) {
                value.middle.rotateAngleY -= 0.2617994F;
                value.middle.rotateAngleX += facePitch / 3.0F;
                value.middle.rotateAngleY += faceYaw / 3.0F;
            }
            (this.arm1.getModel()).rotateAngleX -= 1.0471976F;
            this.arm1.rotateAngleY -= 0.62831855F;
            this.arm1.rotateAngleZ++;
            this.forearm1.rotateAngleX += 0.44879895F;
            (this.arm2.getModel()).rotateAngleX -= 2.6179938F;
            this.arm2.rotateAngleY += 0.62831855F;
            this.arm2.rotateAngleZ -= 1.0471976F;
            this.forearm2.rotateAngleX -= 0.62831855F;
            float tick = MathHelper.clamp((fullTick - 25) + this.partialTick, 0.0F, 1.0F);
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.bow.middle1.rotateAngleX += -f * 3.1415927F / 16.0F;
            this.bow.side1.rotateAngleX += -f * 3.1415927F / 24.0F;
            this.bow.middle2.rotateAngleX += f * 3.1415927F / 16.0F;
            this.bow.side2.rotateAngleX += f * 3.1415927F / 24.0F;
            this.bow.rotateRope();
            this.bow.rope1.rotateAngleX += f * 3.1415927F / 6.0F;
            this.bow.rope2.rotateAngleX += -f * 3.1415927F / 6.0F;
        } else if (fullTick < 28) {
            float tick = ((fullTick - 24) + this.partialTick) / 4.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            (this.head.getModel()).rotateAngleY += f * 3.1415927F / 4.0F;
            for (Spine value : this.spine) {
                value.middle.rotateAngleY += -f * 3.1415927F / 12.0F;
                value.middle.rotateAngleX += f * facePitch / 3.0F;
                value.middle.rotateAngleY += f * faceYaw / 3.0F;
            }
            (this.arm1.getModel()).rotateAngleX += -f * 3.1415927F / 3.0F;
            this.arm1.rotateAngleY += -f * 3.1415927F / 5.0F;
            this.arm1.rotateAngleZ += f * 3.1415927F / 3.0F;
            this.forearm1.rotateAngleX += f * 3.1415927F / 7.0F;
            (this.arm2.getModel()).rotateAngleX += -f * 3.1415927F / 1.2F;
            this.arm2.rotateAngleY += f * 3.1415927F / 5.0F;
            this.arm2.rotateAngleZ += -f * 3.1415927F / 3.0F;
            this.forearm2.rotateAngleX += -f * 3.1415927F / 5.0F;
            this.bow.rotateRope();
        }
    }

    private void animateConstrict() {
        this.animator.startPhase(5);
        this.animator.rotate(this.waist, 0.1308997F, 0.0F, 0.0F);
        int animTick;
        for (animTick = 0; animTick < this.spine.length; animTick++) {
            float tick = (animTick == 0) ? 0.3926991F : ((animTick == 2) ? -0.3926991F : 0.0F);
            float f = (animTick == 1) ? 0.3926991F : 0.31415927F;
            this.animator.rotate((this.spine[animTick]).side1[0], tick, f, 0.0F);
            this.animator.rotate((this.spine[animTick]).side1[1], 0.0F, 0.15707964F, 0.0F);
            this.animator.rotate((this.spine[animTick]).side1[2], 0.0F, 0.2617994F, 0.0F);
            this.animator.rotate((this.spine[animTick]).side2[0], tick, -f, 0.0F);
            this.animator.rotate((this.spine[animTick]).side2[1], 0.0F, -0.15707964F, 0.0F);
            this.animator.rotate((this.spine[animTick]).side2[2], 0.0F, -0.2617994F, 0.0F);
        }
        this.animator.rotate(this.arm1, 0.0F, 0.0F, 0.8975979F);
        this.animator.rotate(this.arm2, 0.0F, 0.0F, -0.8975979F);
        this.animator.move(this.skeleBase, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.leg1, -0.44879895F, 0.0F, 0.0F);
        this.animator.rotate(this.leg2, -0.44879895F, 0.0F, 0.0F);
        this.animator.rotate(this.foreleg1.getModel(), 0.5235988F, 0.0F, 0.0F);
        this.animator.rotate(this.foreleg2.getModel(), 0.5235988F, 0.0F, 0.0F);
        this.animator.endPhase();
        this.animator.setStationaryPhase(2);
        this.animator.startPhase(1);
        this.animator.rotate(this.neck, 0.19634955F, 0.0F, 0.0F);
        this.animator.rotate(this.head, 0.15707964F, 0.0F, 0.0F);
        this.animator.rotate(this.waist, 0.31415927F, 0.0F, 0.0F);
        this.animator.rotate((this.spine[0]).middle, 0.2617994F, 0.0F, 0.0F);
        for (animTick = 0; animTick < this.spine.length; animTick++) {
            float tick = (animTick == 0) ? 0.1308997F : ((animTick == 2) ? -0.1308997F : 0.0F);
            float f = (animTick == 1) ? -0.17453294F : -0.22439948F;
            this.animator.rotate((this.spine[animTick]).side1[0], tick - 0.08F, f, 0.0F);
            this.animator.rotate((this.spine[animTick]).side1[1], 0.0F, 0.15707964F, 0.0F);
            this.animator.rotate((this.spine[animTick]).side1[2], 0.0F, 0.2617994F, 0.0F);
            this.animator.rotate((this.spine[animTick]).side2[0], tick + 0.08F, -f, 0.0F);
            this.animator.rotate((this.spine[animTick]).side2[1], 0.0F, -0.15707964F, 0.0F);
            this.animator.rotate((this.spine[animTick]).side2[2], 0.0F, -0.2617994F, 0.0F);
        }
        this.animator.move(this.skeleBase, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.leg1, -0.44879895F, 0.0F, 0.0F);
        this.animator.rotate(this.leg2, -0.44879895F, 0.0F, 0.0F);
        this.animator.rotate(this.foreleg1.getModel(), 0.5235988F, 0.0F, 0.0F);
        this.animator.rotate(this.foreleg2.getModel(), 0.5235988F, 0.0F, 0.0F);
        this.animator.endPhase();
        this.animator.setStationaryPhase(4);
        this.animator.resetPhase(8);
        animTick = this.animator.getEntity().getAnimationTick();
        if (animTick < 5) {
            float tick = (animTick + this.partialTick) / 5.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            for (Spine value : this.spine) {
                value.side1[0].setScale(1.0F + f * 0.6F);
                value.side2[0].setScale(1.0F + f * 0.6F);
            }
        } else if (animTick < 12) {
            for (Spine value : this.spine) {
                value.side1[0].setScale(1.6F);
                value.side2[0].setScale(1.6F);
            }
        } else if (animTick < 20) {
            float tick = ((animTick - 12) + this.partialTick) / 8.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            for (Spine value : this.spine) {
                value.side1[0].setScale(1.0F + f * 0.6F);
                value.side2[0].setScale(1.0F + f * 0.6F);
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
            this.middle.addBox(-2.5F, -4.0F, -2.0F, 5, 4, 4, 0.5F);
            this.side1 = new ScalableModelRenderer[3];
            this.side2 = new ScalableModelRenderer[3];
            this.side1[0] = new ScalableModelRenderer(model, 32, 12);
            this.side1[0].addBox(skeletonPart ? 0.0F : -6.0F, -2.0F, -2.0F, 6, 2, 2, 0.25F);
            if (!skeletonPart) this.side1[0].setRotationPoint(-3.0F, -1.0F, 1.75F);
            this.middle.addChild(this.side1[0]);
            this.side2[0] = new ScalableModelRenderer(model, 32, 12);
            (this.side2[0]).mirror = true;
            this.side2[0].addBox(skeletonPart ? -6.0F : 0.0F, -2.0F, -2.0F, 6, 2, 2, 0.25F);
            if (!skeletonPart) this.side2[0].setRotationPoint(3.0F, -1.0F, 1.75F);
            this.middle.addChild(this.side2[0]);
            this.side1[1] = new ScalableModelRenderer(model, 32, 12);
            (this.side1[1]).mirror = true;
            this.side1[1].addBox(-6.0F, -2.0F, -2.0F, 6, 2, 2, 0.2F);
            this.side1[1].setRotationPoint(skeletonPart ? -0.5F : -6.5F, 0.0F, 0.0F);
            this.side1[0].addChild(this.side1[1]);
            this.side2[1] = new ScalableModelRenderer(model, 32, 12);
            this.side2[1].addBox(0.0F, -2.0F, -2.0F, 6, 2, 2, 0.2F);
            this.side2[1].setRotationPoint(skeletonPart ? 0.5F : 6.5F, 0.0F, 0.0F);
            this.side2[0].addChild(this.side2[1]);
            this.side1[2] = new ScalableModelRenderer(model, 32, 12);
            this.side1[2].addBox(-6.0F, -2.0F, -2.0F, 6, 2, 2, 0.15F);
            this.side1[2].setRotationPoint(-6.4F, 0.0F, 0.0F);
            this.side1[1].addChild(this.side1[2]);
            this.side2[2] = new ScalableModelRenderer(model, 32, 12);
            (this.side2[2]).mirror = true;
            this.side2[2].addBox(0.0F, -2.0F, -2.0F, 6, 2, 2, 0.15F);
            this.side2[2].setRotationPoint(6.4F, 0.0F, 0.0F);
            this.side2[1].addChild(this.side2[2]);
        }

        private void resetAngles(ModelRenderer... boxes) {
            for (ModelRenderer box : boxes) {
                box.rotateAngleX = 0.0F;
                box.rotateAngleY = 0.0F;
                box.rotateAngleZ = 0.0F;
            }
        }

        public void setAngles(float PI, boolean middleSpine) {
            resetAngles(this.middle);
            resetAngles(this.side1);
            resetAngles(this.side2);
            this.middle.rotateAngleX = PI / 18.0F;
            (this.side1[0]).rotateAngleY = -PI / 4.5F;
            (this.side2[0]).rotateAngleY = PI / 4.5F;
            (this.side1[1]).rotateAngleY = -PI / 3.0F;
            (this.side2[1]).rotateAngleY = PI / 3.0F;
            (this.side1[2]).rotateAngleY = -PI / 3.5F;
            (this.side2[2]).rotateAngleY = PI / 3.5F;
            if (middleSpine) for (int i = 0; i < this.side1.length; i++) {
                (this.side1[i]).rotateAngleY *= 0.98F;
                (this.side2[i]).rotateAngleY *= 0.98F;
            }
            this.side1[0].setScale(1.0F);
            this.side2[0].setScale(1.0F);
        }

        public void animate(float breatheAnim) {
            (this.side1[1]).rotateAngleY += breatheAnim * 0.02F;
            (this.side2[1]).rotateAngleY -= breatheAnim * 0.02F;
        }
    }
}

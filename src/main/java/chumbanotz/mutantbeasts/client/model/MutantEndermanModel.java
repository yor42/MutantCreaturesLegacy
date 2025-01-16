package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.client.model.ScalableModelRenderer;
import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import java.util.Arrays;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class MutantEndermanModel
extends ModelBase {
    private final ModelRenderer pelvis;
    private final ModelRenderer abdomen;
    private final ModelRenderer chest;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer mouth;
    private final Arm rightArm;
    private final Arm leftArm;
    private final Arm lowerRightArm;
    private final Arm lowerLeftArm;
    private final ModelRenderer legjoint1;
    private final ModelRenderer legjoint2;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer foreleg1;
    private final ModelRenderer foreleg2;
    private float partialTick;

    public MutantEndermanModel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.pelvis = new ModelRenderer(this);
        this.pelvis.setRotationPoint(0.0f, -15.5f, 8.0f);
        this.abdomen = new ModelRenderer(this, 32, 0);
        this.abdomen.addBox(-4.0f, -10.0f, -2.0f, 8, 10, 4);
        this.pelvis.addChild(this.abdomen);
        this.chest = new ModelRenderer(this, 50, 8);
        this.chest.addBox(-5.0f, -16.0f, -3.0f, 10, 16, 6);
        this.chest.setRotationPoint(0.0f, -8.0f, 0.0f);
        this.abdomen.addChild(this.chest);
        this.neck = new ModelRenderer(this, 32, 14);
        this.neck.addBox(-1.5f, -4.0f, -1.5f, 3, 4, 3);
        this.neck.setRotationPoint(0.0f, -15.0f, 0.0f);
        this.chest.addChild(this.neck);
        this.head = new ModelRenderer(this);
        this.head.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -8.0f, 8, 6, 8, 0.5f);
        this.head.setTextureOffset(0, 14).addBox(-4.0f, 3.0f, -8.0f, 8, 2, 8, 0.5f);
        this.head.setRotationPoint(0.0f, -5.0f, 3.0f);
        this.neck.addChild(this.head);
        this.mouth = new ModelRenderer(this, 0, 24);
        this.mouth.addBox(-4.0f, 3.0f, -8.0f, 8, 2, 8);
        this.head.addChild(this.mouth);
        this.rightArm = new Arm(this, this.chest, true);
        this.leftArm = new Arm(this, this.chest, false);
        this.lowerRightArm = new Arm(this, this.chest, true);
        ((Arm)this.lowerRightArm).arm.rotationPointY += 6.0f;
        this.lowerLeftArm = new Arm(this, this.chest, false);
        ((Arm)this.lowerLeftArm).arm.rotationPointY += 6.0f;
        this.legjoint1 = new ModelRenderer(this);
        this.legjoint1.setRotationPoint(-1.5f, 0.0f, 0.75f);
        this.abdomen.addChild(this.legjoint1);
        this.legjoint2 = new ModelRenderer(this);
        this.legjoint2.setRotationPoint(1.5f, 0.0f, 0.75f);
        this.abdomen.addChild(this.legjoint2);
        this.leg1 = new ModelRenderer(this, 0, 34);
        this.leg1.addBox(-1.5f, 0.0f, -1.5f, 3, 24, 3, 0.5f);
        this.leg1.setRotationPoint(0.0f, -2.0f, 0.0f);
        this.legjoint1.addChild(this.leg1);
        this.leg2 = new ModelRenderer(this, 0, 34);
        this.leg2.mirror = true;
        this.leg2.addBox(-1.5f, 0.0f, -1.5f, 3, 24, 3, 0.5f);
        this.leg2.setRotationPoint(0.0f, -2.0f, 0.0f);
        this.legjoint2.addChild(this.leg2);
        this.foreleg1 = new ModelRenderer(this, 12, 34);
        this.foreleg1.addBox(-1.5f, 0.0f, -1.5f, 3, 24, 3, 0.5f);
        this.foreleg1.setRotationPoint(0.0f, 23.0f, 0.0f);
        this.leg1.addChild(this.foreleg1);
        this.foreleg2 = new ModelRenderer(this, 12, 34);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-1.5f, 0.0f, -1.5f, 3, 24, 3, 0.5f);
        this.foreleg2.setRotationPoint(0.0f, 23.0f, 0.0f);
        this.leg2.addChild(this.foreleg2);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setAngles();
        MutantEndermanEntity mutantEnderman = (MutantEndermanEntity)entity;
        this.animate(mutantEnderman, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.lowerRightArm.arm.setScale(mutantEnderman.getArmScale(this.partialTick));
        this.lowerLeftArm.arm.setScale(mutantEnderman.getArmScale(this.partialTick));
        this.pelvis.render(scale);
    }

    private void setAngles() {
        this.pelvis.rotationPointY = -15.5f;
        this.abdomen.rotateAngleX = 0.31415927f;
        this.chest.rotateAngleX = 0.3926991f;
        this.chest.rotateAngleY = 0.0f;
        this.chest.rotateAngleZ = 0.0f;
        this.neck.rotateAngleX = 0.19634955f;
        this.neck.rotateAngleZ = 0.0f;
        this.head.rotateAngleX = -0.7853982f;
        this.head.rotateAngleY = 0.0f;
        this.head.rotateAngleZ = 0.0f;
        this.mouth.rotateAngleX = 0.0f;
        this.rightArm.setAngles();
        this.leftArm.setAngles();
        this.lowerRightArm.setAngles();
        ((Arm)this.lowerRightArm).arm.rotateAngleX += 0.1f;
        ((Arm)this.lowerRightArm).arm.rotateAngleZ -= 0.2f;
        this.lowerLeftArm.setAngles();
        ((Arm)this.lowerLeftArm).arm.rotateAngleX += 0.1f;
        ((Arm)this.lowerLeftArm).arm.rotateAngleZ += 0.2f;
        this.legjoint1.rotateAngleX = 0.0f;
        this.legjoint2.rotateAngleX = 0.0f;
        this.leg1.rotateAngleX = -0.8975979f;
        this.leg1.rotateAngleY = 0.0f;
        this.leg1.rotateAngleZ = 0.2617994f;
        this.leg2.rotateAngleX = -0.8975979f;
        this.leg2.rotateAngleY = 0.0f;
        this.leg2.rotateAngleZ = -0.2617994f;
        this.foreleg1.rotateAngleX = 0.7853982f;
        this.foreleg1.rotateAngleZ = -0.1308997f;
        this.foreleg2.rotateAngleX = 0.7853982f;
        this.foreleg2.rotateAngleZ = 0.1308997f;
    }

    private void animate(MutantEndermanEntity enderman, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float f5) {
        float scale;
        int arm;
        float walkSpeed = 0.3f;
        float walkAnim1 = (MathHelper.sin((float)((limbSwing - 0.8f) * walkSpeed)) + 0.8f) * limbSwingAmount;
        float walkAnim2 = -(MathHelper.sin((float)((limbSwing + 0.8f) * walkSpeed)) - 0.8f) * limbSwingAmount;
        float walkAnim3 = (MathHelper.sin((float)((limbSwing + 0.8f) * walkSpeed)) - 0.8f) * limbSwingAmount;
        float walkAnim4 = -(MathHelper.sin((float)((limbSwing - 0.8f) * walkSpeed)) + 0.8f) * limbSwingAmount;
        float[] walkAnim = new float[5];
        Arrays.fill(walkAnim, MathHelper.sin((float)(limbSwing * walkSpeed)) * limbSwingAmount);
        float breatheAnim = MathHelper.sin((float)(ageInTicks * 0.15f));
        float faceYaw = netHeadYaw * (float)Math.PI / 180.0f;
        float facePitch = headPitch * (float)Math.PI / 180.0f;
        for (arm = 1; arm < enderman.heldBlock.length; ++arm) {
            if (enderman.heldBlock[arm] == 0) continue;
            this.animateHoldBlock(enderman.heldBlockTick[arm], arm, enderman.hasTarget > 0);
            int n = arm;
            walkAnim[n] = walkAnim[n] * 0.4f;
        }
        if (enderman.getAttackID() == 1) {
            arm = enderman.getActiveArm();
            this.animateMelee(enderman.getAttackTick(), arm);
            walkAnim[arm] = 0.0f;
        }
        if (enderman.getAttackID() == 2) {
            arm = enderman.getActiveArm();
            this.animateThrowBlock(enderman.getAttackTick(), arm);
        }
        if (enderman.getAttackID() == 5) {
            this.animateScream(enderman.getAttackTick());
            scale = 1.0f - MathHelper.clamp((float)((float)enderman.getAttackTick() / 6.0f), (float)0.0f, (float)1.0f);
            faceYaw *= scale;
            facePitch *= scale;
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            walkAnim3 *= scale;
            walkAnim4 *= scale;
            Arrays.fill(walkAnim, 0.0f);
        }
        if (enderman.getAttackID() == 7) {
            this.animateTeleSmash(enderman.getAttackTick());
        }
        if (enderman.getAttackID() == 8) {
            this.animateDeath(enderman.deathTime);
            scale = 1.0f - MathHelper.clamp((float)((float)enderman.deathTime / 6.0f), (float)0.0f, (float)1.0f);
            faceYaw *= scale;
            facePitch *= scale;
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            walkAnim3 *= scale;
            walkAnim4 *= scale;
            Arrays.fill(walkAnim, 0.0f);
        }
        this.head.rotateAngleX += facePitch * 0.5f;
        this.head.rotateAngleY += faceYaw * 0.7f;
        this.head.rotateAngleZ -= faceYaw * 0.7f;
        this.neck.rotateAngleX += facePitch * 0.3f;
        this.chest.rotateAngleX += facePitch * 0.2f;
        this.mouth.rotateAngleX += breatheAnim * 0.02f + 0.02f;
        this.neck.rotateAngleX -= breatheAnim * 0.02f;
        ((Arm)this.rightArm).arm.rotateAngleZ += breatheAnim * 0.004f;
        ((Arm)this.leftArm).arm.rotateAngleZ -= breatheAnim * 0.004f;
        for (ModelRenderer finger : this.rightArm.finger) {
            finger.rotateAngleZ += breatheAnim * 0.05f;
        }
        ((Arm)this.rightArm).thumb.rotateAngleZ -= breatheAnim * 0.05f;
        for (ModelRenderer finger : this.leftArm.finger) {
            finger.rotateAngleZ -= breatheAnim * 0.05f;
        }
        ((Arm)this.leftArm).thumb.rotateAngleZ += breatheAnim * 0.05f;
        ((Arm)this.lowerRightArm).arm.rotateAngleZ += breatheAnim * 0.002f;
        ((Arm)this.lowerLeftArm).arm.rotateAngleZ -= breatheAnim * 0.002f;
        for (ModelRenderer finger : this.lowerRightArm.finger) {
            finger.rotateAngleZ += breatheAnim * 0.02f;
        }
        ((Arm)this.lowerRightArm).thumb.rotateAngleZ -= breatheAnim * 0.02f;
        for (ModelRenderer finger : this.lowerLeftArm.finger) {
            finger.rotateAngleZ -= breatheAnim * 0.02f;
        }
        ((Arm)this.lowerLeftArm).thumb.rotateAngleZ += breatheAnim * 0.02f;
        this.pelvis.rotationPointY -= Math.abs(walkAnim[0]);
        this.chest.rotateAngleY -= walkAnim[0] * 0.06f;
        ((Arm)this.rightArm).arm.rotateAngleX -= walkAnim[1] * 0.6f;
        ((Arm)this.leftArm).arm.rotateAngleX += walkAnim[2] * 0.6f;
        ((Arm)this.rightArm).forearm.rotateAngleX -= walkAnim[1] * 0.2f;
        ((Arm)this.leftArm).forearm.rotateAngleX += walkAnim[2] * 0.2f;
        ((Arm)this.lowerRightArm).arm.rotateAngleX -= walkAnim[3] * 0.3f;
        ((Arm)this.lowerLeftArm).arm.rotateAngleX += walkAnim[4] * 0.3f;
        ((Arm)this.lowerRightArm).forearm.rotateAngleX -= walkAnim[3] * 0.1f;
        ((Arm)this.lowerLeftArm).forearm.rotateAngleX += walkAnim[4] * 0.1f;
        this.legjoint1.rotateAngleX += walkAnim1 * 0.6f;
        this.legjoint2.rotateAngleX += walkAnim2 * 0.6f;
        this.foreleg1.rotateAngleX += walkAnim3 * 0.3f;
        this.foreleg2.rotateAngleX += walkAnim4 * 0.3f;
    }

    private void animateHoldBlock(int fullTick, int armID, boolean hasTarget) {
        float tick = ((float)fullTick + this.partialTick) / 10.0f;
        if (!hasTarget) {
            tick = fullTick == 0 ? 0.0f : ((float)fullTick - this.partialTick) / 10.0f;
        }
        float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
        if (armID == 1) {
            ((Arm)this.rightArm).arm.rotateAngleZ += f * 0.8f;
            ((Arm)this.rightArm).forearm.rotateAngleZ += f * 0.6f;
            ((Arm)this.rightArm).hand.rotateAngleY += f * 0.8f;
            ((Arm)this.rightArm).finger[0].rotateAngleX += -f * 0.2f;
            ((Arm)this.rightArm).finger[2].rotateAngleX += f * 0.2f;
            for (int i = 0; i < this.rightArm.finger.length; ++i) {
                ((Arm)this.rightArm).finger[i].rotateAngleZ += f * 0.6f;
            }
            ((Arm)this.rightArm).thumb.rotateAngleZ += -f * 0.4f;
        } else if (armID == 2) {
            ((Arm)this.leftArm).arm.rotateAngleZ += -f * 0.8f;
            ((Arm)this.leftArm).forearm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.leftArm).hand.rotateAngleY += -f * 0.8f;
            ((Arm)this.leftArm).finger[0].rotateAngleX += -f * 0.2f;
            ((Arm)this.leftArm).finger[2].rotateAngleX += f * 0.2f;
            for (int i = 0; i < this.leftArm.finger.length; ++i) {
                ((Arm)this.leftArm).finger[i].rotateAngleZ += -f * 0.6f;
            }
            ((Arm)this.leftArm).thumb.rotateAngleZ += f * 0.4f;
        } else if (armID == 3) {
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += f * 0.5f;
            ((Arm)this.lowerRightArm).forearm.rotateAngleZ += f * 0.4f;
            ((Arm)this.lowerRightArm).hand.rotateAngleY += f * 0.4f;
            ((Arm)this.lowerRightArm).finger[0].rotateAngleX += -f * 0.2f;
            ((Arm)this.lowerRightArm).finger[2].rotateAngleX += f * 0.2f;
            for (int i = 0; i < this.lowerRightArm.finger.length; ++i) {
                ((Arm)this.lowerRightArm).finger[i].rotateAngleZ += f * 0.6f;
            }
            ((Arm)this.lowerRightArm).thumb.rotateAngleZ += -f * 0.4f;
        } else if (armID == 4) {
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -f * 0.5f;
            ((Arm)this.lowerLeftArm).forearm.rotateAngleZ += -f * 0.4f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleY += -f * 0.4f;
            ((Arm)this.lowerLeftArm).finger[0].rotateAngleX += -f * 0.2f;
            ((Arm)this.lowerLeftArm).finger[2].rotateAngleX += f * 0.2f;
            for (int i = 0; i < this.lowerLeftArm.finger.length; ++i) {
                ((Arm)this.lowerLeftArm).finger[i].rotateAngleZ += -f * 0.6f;
            }
            ((Arm)this.lowerLeftArm).thumb.rotateAngleZ += f * 0.4f;
        }
    }

    private void animateMelee(int fullTick, int armID) {
        int right = (armID & 1) == 1 ? 1 : -1;
        Arm arm = this.getArmFromID(armID);
        if (fullTick < 2) {
            float tick = ((float)fullTick + this.partialTick) / 2.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            ((Arm)arm).arm.rotateAngleX += f * 0.2f;
            ((Arm)arm).finger[0].rotateAngleZ += f * 0.3f * (float)right;
            ((Arm)arm).finger[1].rotateAngleZ += f * 0.3f * (float)right;
            ((Arm)arm).finger[2].rotateAngleZ += f * 0.3f * (float)right;
            ((Arm)arm).foreFinger[0].rotateAngleZ += -f * 0.5f * (float)right;
            ((Arm)arm).foreFinger[1].rotateAngleZ += -f * 0.5f * (float)right;
            ((Arm)arm).foreFinger[2].rotateAngleZ += -f * 0.5f * (float)right;
        } else if (fullTick < 5) {
            float tick = ((float)(fullTick - 2) + this.partialTick) / 3.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.chest.rotateAngleY += -f1 * 0.1f * (float)right;
            ((Arm)arm).arm.rotateAngleX += f * 1.1f - 1.1f;
            ((Arm)arm).forearm.rotateAngleX += -f * 0.4f;
            ((Arm)arm).finger[0].rotateAngleZ += 0.3f * (float)right;
            ((Arm)arm).finger[1].rotateAngleZ += 0.3f * (float)right;
            ((Arm)arm).finger[2].rotateAngleZ += 0.3f * (float)right;
            ((Arm)arm).foreFinger[0].rotateAngleZ += -0.5f * (float)right;
            ((Arm)arm).foreFinger[1].rotateAngleZ += -0.5f * (float)right;
            ((Arm)arm).foreFinger[2].rotateAngleZ += -0.5f * (float)right;
        } else if (fullTick < 6) {
            this.chest.rotateAngleY += -0.1f * (float)right;
            ((Arm)arm).arm.rotateAngleX += -1.1f;
            ((Arm)arm).forearm.rotateAngleX += -0.4f;
            ((Arm)arm).finger[0].rotateAngleZ += 0.3f * (float)right;
            ((Arm)arm).finger[1].rotateAngleZ += 0.3f * (float)right;
            ((Arm)arm).finger[2].rotateAngleZ += 0.3f * (float)right;
            ((Arm)arm).foreFinger[0].rotateAngleZ += -0.5f * (float)right;
            ((Arm)arm).foreFinger[1].rotateAngleZ += -0.5f * (float)right;
            ((Arm)arm).foreFinger[2].rotateAngleZ += -0.5f * (float)right;
        } else if (fullTick < 10) {
            float tick = ((float)(fullTick - 6) + this.partialTick) / 4.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.chest.rotateAngleY += -f * 0.1f * (float)right;
            ((Arm)arm).arm.rotateAngleX += -f * 1.1f;
            ((Arm)arm).forearm.rotateAngleX += -f * 0.4f;
            ((Arm)arm).finger[0].rotateAngleZ += f * 0.3f * (float)right;
            ((Arm)arm).finger[1].rotateAngleZ += f * 0.3f * (float)right;
            ((Arm)arm).finger[2].rotateAngleZ += f * 0.3f * (float)right;
            ((Arm)arm).foreFinger[0].rotateAngleZ += -f * 0.5f * (float)right;
            ((Arm)arm).foreFinger[1].rotateAngleZ += -f * 0.5f * (float)right;
            ((Arm)arm).foreFinger[2].rotateAngleZ += -f * 0.5f * (float)right;
        }
    }

    private void animateThrowBlock(int fullTick, int armID) {
        if (armID == 1) {
            if (fullTick < 4) {
                float tick = ((float)fullTick + this.partialTick) / 4.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.rightArm).arm.rotateAngleX += -f1 * 1.5f;
                ((Arm)this.rightArm).arm.rotateAngleZ += f * 0.8f;
                ((Arm)this.rightArm).forearm.rotateAngleZ += f * 0.6f;
                ((Arm)this.rightArm).hand.rotateAngleY += f * 0.8f;
                ((Arm)this.rightArm).finger[0].rotateAngleX += -f * 0.2f;
                ((Arm)this.rightArm).finger[2].rotateAngleX += f * 0.2f;
                for (int i = 0; i < this.rightArm.finger.length; ++i) {
                    ((Arm)this.rightArm).finger[i].rotateAngleZ += f * 0.6f;
                }
                ((Arm)this.rightArm).thumb.rotateAngleZ += -f * 0.4f;
            } else if (fullTick < 7) {
                ((Arm)this.rightArm).arm.rotateAngleX += -1.5f;
            } else if (fullTick < 14) {
                float tick = ((float)(fullTick - 7) + this.partialTick) / 7.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.rightArm).arm.rotateAngleX += -f * 1.5f;
            }
        } else if (armID == 2) {
            if (fullTick < 4) {
                float tick = ((float)fullTick + this.partialTick) / 4.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.leftArm).arm.rotateAngleX += -f1 * 1.5f;
                ((Arm)this.leftArm).arm.rotateAngleZ += -f * 0.8f;
                ((Arm)this.leftArm).forearm.rotateAngleZ += -f * 0.6f;
                ((Arm)this.leftArm).hand.rotateAngleY += -f * 0.8f;
                ((Arm)this.leftArm).finger[0].rotateAngleX += -f * 0.2f;
                ((Arm)this.leftArm).finger[2].rotateAngleX += f * 0.2f;
                for (int i = 0; i < this.leftArm.finger.length; ++i) {
                    ((Arm)this.leftArm).finger[i].rotateAngleZ += -f * 0.6f;
                }
                ((Arm)this.leftArm).thumb.rotateAngleZ += f * 0.4f;
            } else if (fullTick < 7) {
                ((Arm)this.leftArm).arm.rotateAngleX += -1.5f;
            } else if (fullTick < 14) {
                float tick = ((float)(fullTick - 7) + this.partialTick) / 7.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.leftArm).arm.rotateAngleX += -f * 1.5f;
            }
        } else if (armID == 3) {
            if (fullTick < 4) {
                float tick = ((float)fullTick + this.partialTick) / 4.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.lowerRightArm).arm.rotateAngleX += -f1 * 1.5f;
                ((Arm)this.lowerRightArm).arm.rotateAngleZ += f * 0.5f;
                ((Arm)this.lowerRightArm).forearm.rotateAngleZ += f * 0.4f;
                ((Arm)this.lowerRightArm).hand.rotateAngleY += f * 0.4f;
                ((Arm)this.lowerRightArm).finger[0].rotateAngleX += -f * 0.2f;
                ((Arm)this.lowerRightArm).finger[2].rotateAngleX += f * 0.2f;
                for (int i = 0; i < this.lowerRightArm.finger.length; ++i) {
                    ((Arm)this.lowerRightArm).finger[i].rotateAngleZ += f * 0.6f;
                }
                ((Arm)this.lowerRightArm).thumb.rotateAngleZ += -f * 0.4f;
            } else if (fullTick < 7) {
                ((Arm)this.lowerRightArm).arm.rotateAngleX += -1.5f;
            } else if (fullTick < 14) {
                float tick = ((float)(fullTick - 7) + this.partialTick) / 7.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.lowerRightArm).arm.rotateAngleX += -f * 1.5f;
            }
        } else if (armID == 4) {
            if (fullTick < 4) {
                float tick = ((float)fullTick + this.partialTick) / 4.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f1 * 1.5f;
                ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -f * 0.5f;
                ((Arm)this.lowerLeftArm).forearm.rotateAngleZ += -f * 0.4f;
                ((Arm)this.lowerLeftArm).hand.rotateAngleY += -f * 0.4f;
                ((Arm)this.lowerLeftArm).finger[0].rotateAngleX += -f * 0.2f;
                ((Arm)this.lowerLeftArm).finger[2].rotateAngleX += f * 0.2f;
                for (int i = 0; i < this.lowerLeftArm.finger.length; ++i) {
                    ((Arm)this.lowerLeftArm).finger[i].rotateAngleZ += -f * 0.6f;
                }
                ((Arm)this.lowerLeftArm).thumb.rotateAngleZ += f * 0.4f;
            } else if (fullTick < 7) {
                ((Arm)this.lowerLeftArm).arm.rotateAngleX += -1.5f;
            } else if (fullTick < 14) {
                float tick = ((float)(fullTick - 7) + this.partialTick) / 7.0f;
                float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
                ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f * 1.5f;
            }
        }
    }

    private void animateScream(int fullTick) {
        if (fullTick < 35) {
            int i;
            float tick = ((float)fullTick + this.partialTick) / 35.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += f * 0.3f;
            this.chest.rotateAngleX += f * 0.4f;
            this.neck.rotateAngleX += f * 0.2f;
            this.head.rotateAngleX += f * 0.3f;
            ((Arm)this.rightArm).arm.rotateAngleX += -f * 0.6f;
            ((Arm)this.rightArm).arm.rotateAngleY += f * 0.4f;
            ((Arm)this.rightArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.rightArm).hand.rotateAngleZ += -f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.rightArm).finger[i].rotateAngleZ += f * 0.3f;
                ((Arm)this.rightArm).foreFinger[i].rotateAngleZ += -f * 0.5f;
            }
            ((Arm)this.leftArm).arm.rotateAngleX += -f * 0.6f;
            ((Arm)this.leftArm).arm.rotateAngleY += -f * 0.4f;
            ((Arm)this.leftArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.leftArm).hand.rotateAngleZ += f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.leftArm).finger[i].rotateAngleZ += -f * 0.3f;
                ((Arm)this.leftArm).foreFinger[i].rotateAngleZ += f * 0.5f;
            }
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += f * 0.2f;
            ((Arm)this.lowerRightArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.lowerRightArm).hand.rotateAngleZ += -f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.lowerRightArm).finger[i].rotateAngleZ += f * 0.3f;
                ((Arm)this.lowerRightArm).foreFinger[i].rotateAngleZ += -f * 0.5f;
            }
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.lowerLeftArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleZ += f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.lowerLeftArm).finger[i].rotateAngleZ += -f * 0.3f;
                ((Arm)this.lowerLeftArm).foreFinger[i].rotateAngleZ += f * 0.5f;
            }
        } else if (fullTick < 40) {
            int i;
            this.abdomen.rotateAngleX += 0.3f;
            this.chest.rotateAngleX += 0.4f;
            this.neck.rotateAngleX += 0.2f;
            this.head.rotateAngleX += 0.3f;
            ((Arm)this.rightArm).arm.rotateAngleX += -0.6f;
            ((Arm)this.rightArm).arm.rotateAngleY += 0.4f;
            ((Arm)this.rightArm).forearm.rotateAngleX += -0.8f;
            ((Arm)this.rightArm).hand.rotateAngleZ += -0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.rightArm).finger[i].rotateAngleZ += 0.3f;
                ((Arm)this.rightArm).foreFinger[i].rotateAngleZ += -0.5f;
            }
            ((Arm)this.leftArm).arm.rotateAngleX += -0.6f;
            ((Arm)this.leftArm).arm.rotateAngleY += -0.4f;
            ((Arm)this.leftArm).forearm.rotateAngleX += -0.8f;
            ((Arm)this.leftArm).hand.rotateAngleZ += 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.leftArm).finger[i].rotateAngleZ += -0.3f;
                ((Arm)this.leftArm).foreFinger[i].rotateAngleZ += 0.5f;
            }
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += 0.2f;
            ((Arm)this.lowerRightArm).forearm.rotateAngleX += -0.8f;
            ((Arm)this.lowerRightArm).hand.rotateAngleZ += -0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.lowerRightArm).finger[i].rotateAngleZ += 0.3f;
                ((Arm)this.lowerRightArm).foreFinger[i].rotateAngleZ += -0.5f;
            }
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -0.4f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -0.2f;
            ((Arm)this.lowerLeftArm).forearm.rotateAngleX += -0.8f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleZ += 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.lowerLeftArm).finger[i].rotateAngleZ += -0.3f;
                ((Arm)this.lowerLeftArm).foreFinger[i].rotateAngleZ += 0.5f;
            }
        } else if (fullTick < 44) {
            int i;
            float tick = ((float)(fullTick - 40) + this.partialTick) / 4.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += -f * 0.1f + 0.4f;
            this.chest.rotateAngleX += f * 0.1f + 0.3f;
            this.chest.rotateAngleZ += f1 * 0.5f;
            this.neck.rotateAngleX += f * 0.2f;
            this.neck.rotateAngleZ += f1 * 0.2f;
            this.head.rotateAngleX += f * 1.2f - 0.8f;
            this.head.rotateAngleZ += f1 * 0.4f;
            this.mouth.rotateAngleX += f1 * 0.6f;
            ((Arm)this.rightArm).arm.rotateAngleX += -f * 0.6f;
            ((Arm)this.rightArm).arm.rotateAngleY += 0.4f;
            ((Arm)this.rightArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.rightArm).hand.rotateAngleZ += -f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.rightArm).finger[i].rotateAngleZ += f * 0.3f;
                ((Arm)this.rightArm).foreFinger[i].rotateAngleZ += -f * 0.5f;
            }
            ((Arm)this.leftArm).arm.rotateAngleX += -f * 0.6f;
            ((Arm)this.leftArm).arm.rotateAngleY += -0.4f;
            ((Arm)this.leftArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.leftArm).hand.rotateAngleZ += f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.leftArm).finger[i].rotateAngleZ += -f * 0.3f;
                ((Arm)this.leftArm).foreFinger[i].rotateAngleZ += f * 0.5f;
            }
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += -f * 0.1f + 0.3f;
            ((Arm)this.lowerRightArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.lowerRightArm).hand.rotateAngleZ += -f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.lowerRightArm).finger[i].rotateAngleZ += f * 0.3f;
                ((Arm)this.lowerRightArm).foreFinger[i].rotateAngleZ += -f * 0.5f;
            }
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += f * 0.1f - 0.3f;
            ((Arm)this.lowerLeftArm).forearm.rotateAngleX += -f * 0.8f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleZ += f * 0.4f;
            for (i = 0; i < 3; ++i) {
                ((Arm)this.lowerLeftArm).finger[i].rotateAngleZ += -f * 0.3f;
                ((Arm)this.lowerLeftArm).foreFinger[i].rotateAngleZ += f * 0.5f;
            }
            this.leg1.rotateAngleZ += f1 * 0.1f;
            this.leg2.rotateAngleZ += -f1 * 0.1f;
        } else if (fullTick < 155) {
            float tick = ((float)(fullTick - 44) + this.partialTick) / 111.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += 0.4f;
            this.chest.rotateAngleX += 0.3f;
            this.chest.rotateAngleZ += f * 1.0f - 0.5f;
            this.neck.rotateAngleZ += f * 0.4f - 0.2f;
            this.head.rotateAngleX += -0.8f;
            this.head.rotateAngleZ += f * 0.8f - 0.4f;
            this.mouth.rotateAngleX += 0.6f;
            ((Arm)this.rightArm).arm.rotateAngleY += 0.4f;
            ((Arm)this.leftArm).arm.rotateAngleY += -0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += 0.3f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -0.3f;
            this.leg1.rotateAngleZ += 0.1f;
            this.leg2.rotateAngleZ += -0.1f;
        } else if (fullTick < 160) {
            float tick = ((float)(fullTick - 155) + this.partialTick) / 5.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.abdomen.rotateAngleX += f * 0.4f;
            this.chest.rotateAngleX += f * 0.3f;
            this.chest.rotateAngleZ += -f * 0.5f;
            this.neck.rotateAngleZ += -f * 0.2f;
            this.head.rotateAngleX += -f * 0.8f;
            this.head.rotateAngleZ += -f * 0.4f;
            this.mouth.rotateAngleX += f * 0.6f;
            ((Arm)this.rightArm).arm.rotateAngleY += f * 0.4f;
            ((Arm)this.leftArm).arm.rotateAngleY += -f * 0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += f * 0.3f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -f * 0.3f;
            this.leg1.rotateAngleZ += f * 0.1f;
            this.leg2.rotateAngleZ += -f * 0.1f;
        }
    }

    private void animateTeleSmash(int fullTick) {
        if (fullTick < 18) {
            float tick = ((float)fullTick + this.partialTick) / 18.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.chest.rotateAngleX += -f * 0.3f;
            ((Arm)this.rightArm).arm.rotateAngleY += f * 0.2f;
            ((Arm)this.rightArm).arm.rotateAngleZ += f * 0.8f;
            ((Arm)this.rightArm).hand.rotateAngleY += f * 1.7f;
            ((Arm)this.leftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.leftArm).arm.rotateAngleZ += -f * 0.8f;
            ((Arm)this.leftArm).hand.rotateAngleY += -f * 1.7f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += f * 0.2f;
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += f * 0.6f;
            ((Arm)this.lowerRightArm).hand.rotateAngleY += f * 1.7f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleY += -f * 1.7f;
        } else if (fullTick < 20) {
            float tick = ((float)(fullTick - 18) + this.partialTick) / 2.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.chest.rotateAngleX += -f * 0.3f;
            ((Arm)this.rightArm).arm.rotateAngleX += -f1 * 0.8f;
            ((Arm)this.rightArm).arm.rotateAngleY += 0.2f;
            ((Arm)this.rightArm).arm.rotateAngleZ += 0.8f;
            ((Arm)this.rightArm).hand.rotateAngleY += 1.7f;
            ((Arm)this.leftArm).arm.rotateAngleX += -f1 * 0.8f;
            ((Arm)this.leftArm).arm.rotateAngleY += -0.2f;
            ((Arm)this.leftArm).arm.rotateAngleZ += -0.8f;
            ((Arm)this.leftArm).hand.rotateAngleY += -1.7f;
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -f1 * 0.9f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += 0.2f;
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += 0.6f;
            ((Arm)this.lowerRightArm).hand.rotateAngleY += 1.7f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f1 * 0.9f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -0.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -0.6f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleY += -1.7f;
        } else if (fullTick < 24) {
            ((Arm)this.rightArm).arm.rotateAngleX += -0.8f;
            ((Arm)this.rightArm).arm.rotateAngleY += 0.2f;
            ((Arm)this.rightArm).arm.rotateAngleZ += 0.8f;
            ((Arm)this.rightArm).hand.rotateAngleY += 1.7f;
            ((Arm)this.leftArm).arm.rotateAngleX += -0.8f;
            ((Arm)this.leftArm).arm.rotateAngleY += -0.2f;
            ((Arm)this.leftArm).arm.rotateAngleZ += -0.8f;
            ((Arm)this.leftArm).hand.rotateAngleY += -1.7f;
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -0.9f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += 0.2f;
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += 0.6f;
            ((Arm)this.lowerRightArm).hand.rotateAngleY += 1.7f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -0.9f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -0.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -0.6f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleY += -1.7f;
        } else if (fullTick < 30) {
            float tick = ((float)(fullTick - 24) + this.partialTick) / 6.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            ((Arm)this.rightArm).arm.rotateAngleX += -f * 0.8f;
            ((Arm)this.rightArm).arm.rotateAngleY += f * 0.2f;
            ((Arm)this.rightArm).arm.rotateAngleZ += f * 0.8f;
            ((Arm)this.rightArm).hand.rotateAngleY += f * 1.7f;
            ((Arm)this.leftArm).arm.rotateAngleX += -f * 0.8f;
            ((Arm)this.leftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.leftArm).arm.rotateAngleZ += -f * 0.8f;
            ((Arm)this.leftArm).hand.rotateAngleY += -f * 1.7f;
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -f * 0.9f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += f * 0.2f;
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += f * 0.6f;
            ((Arm)this.lowerRightArm).hand.rotateAngleY += f * 1.7f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f * 0.9f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.lowerLeftArm).hand.rotateAngleY += -f * 1.7f;
        }
    }

    private void animateDeath(int deathTick) {
        if (deathTick < 80) {
            float tick = ((float)deathTick + this.partialTick) / 80.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.head.rotateAngleX += f * 0.4f;
            this.neck.rotateAngleX += f * 0.3f;
            this.pelvis.rotationPointY += -f * 12.0f;
            ((Arm)this.rightArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.rightArm).arm.rotateAngleY += f * 0.4f;
            ((Arm)this.rightArm).arm.rotateAngleZ += f * 0.6f;
            ((Arm)this.rightArm).forearm.rotateAngleX += -f * 1.2f;
            ((Arm)this.leftArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.leftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.leftArm).arm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.leftArm).forearm.rotateAngleX += -f * 1.2f;
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += f * 0.4f;
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += f * 0.6f;
            ((Arm)this.lowerRightArm).forearm.rotateAngleX += -f * 1.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -f * 0.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.lowerLeftArm).forearm.rotateAngleX += -f * 1.2f;
            this.leg1.rotateAngleX += -f * 0.9f;
            this.leg1.rotateAngleY += f * 0.3f;
            this.leg2.rotateAngleX += -f * 0.9f;
            this.leg2.rotateAngleY += -f * 0.3f;
            this.foreleg1.rotateAngleX += f * 1.6f;
            this.foreleg2.rotateAngleX += f * 1.6f;
        } else if (deathTick < 84) {
            float tick = ((float)(deathTick - 80) + this.partialTick) / 4.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.head.rotateAngleX += f * 0.4f;
            this.mouth.rotateAngleX += f1 * 0.6f;
            this.neck.rotateAngleX += f * 0.4f - 0.1f;
            this.chest.rotateAngleX += -f1 * 0.8f;
            this.abdomen.rotateAngleX += -f1 * 0.2f;
            this.pelvis.rotationPointY += -12.0f;
            ((Arm)this.rightArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.rightArm).arm.rotateAngleY += -f * 1.4f + 1.8f;
            ((Arm)this.rightArm).arm.rotateAngleZ += f * 0.6f;
            ((Arm)this.rightArm).forearm.rotateAngleX += -f * 1.2f;
            ((Arm)this.leftArm).arm.rotateAngleX += -f * 0.4f;
            ((Arm)this.leftArm).arm.rotateAngleY += f * 1.6f - 1.8f;
            ((Arm)this.leftArm).arm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.leftArm).forearm.rotateAngleX += -f * 1.2f;
            ((Arm)this.lowerRightArm).arm.rotateAngleX += -f * 0.5f + 0.1f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += -f * 1.1f + 1.5f;
            ((Arm)this.lowerRightArm).arm.rotateAngleZ += f * 0.6f;
            ((Arm)this.lowerRightArm).forearm.rotateAngleX += -f * 1.2f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += -f * 0.5f + 0.1f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += f * 1.1f - 1.5f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleZ += -f * 0.6f;
            ((Arm)this.lowerLeftArm).forearm.rotateAngleX += -f * 1.2f;
            this.leg1.rotateAngleX += -f * 1.7f + 0.8f;
            this.leg1.rotateAngleY += f * 0.3f;
            this.leg1.rotateAngleZ += f1 * 0.2f;
            this.leg2.rotateAngleX += -f * 1.7f + 0.8f;
            this.leg2.rotateAngleY += -f * 0.3f;
            this.leg2.rotateAngleZ += -f1 * 0.2f;
            this.foreleg1.rotateAngleX += f * 1.6f;
            this.foreleg2.rotateAngleX += f * 1.6f;
        } else {
            this.mouth.rotateAngleX += 0.6f;
            this.neck.rotateAngleX += -0.1f;
            this.chest.rotateAngleX += -0.8f;
            this.abdomen.rotateAngleX += -0.2f;
            this.pelvis.rotationPointY += -12.0f;
            ((Arm)this.rightArm).arm.rotateAngleY += 1.8f;
            ((Arm)this.leftArm).arm.rotateAngleY += -1.8f;
            ((Arm)this.lowerRightArm).arm.rotateAngleX += 0.1f;
            ((Arm)this.lowerRightArm).arm.rotateAngleY += 1.5f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleX += 0.1f;
            ((Arm)this.lowerLeftArm).arm.rotateAngleY += -1.5f;
            this.leg1.rotateAngleX += 0.8f;
            this.leg1.rotateAngleZ += 0.2f;
            this.leg2.rotateAngleX += 0.8f;
            this.leg2.rotateAngleZ += -0.2f;
        }
    }

    public Arm getArmFromID(int armID) {
        return armID == 1 ? this.rightArm : (armID == 2 ? this.leftArm : (armID == 3 ? this.lowerRightArm : this.lowerLeftArm));
    }

    public void postRenderArm(float scale, int armID) {
        this.pelvis.postRender(scale);
        this.abdomen.postRender(scale);
        this.chest.postRender(scale);
        this.getArmFromID(armID).postRender(scale);
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.partialTick = partialTickTime;
    }

    public static void resetAngles(ModelRenderer model) {
        model.rotateAngleX = 0.0f;
        model.rotateAngleY = 0.0f;
        model.rotateAngleZ = 0.0f;
    }

    static class Arm {
        private final ScalableModelRenderer arm;
        private final ModelRenderer forearm;
        private final ModelRenderer hand;
        private final ModelRenderer[] finger;
        private final ModelRenderer[] foreFinger;
        private final ModelRenderer thumb;
        private final boolean right;

        public Arm(ModelBase model, ModelRenderer connect, boolean right) {
            int i;
            this.right = right;
            this.finger = new ModelRenderer[3];
            this.foreFinger = new ModelRenderer[3];
            this.arm = new ScalableModelRenderer(model, 92, 0);
            this.arm.mirror = !this.right;
            this.arm.addBox(-1.5f, 0.0f, -1.5f, 3, 22, 3, 0.1f);
            this.arm.setRotationPoint(this.right ? -4.0f : 4.0f, -14.0f, 0.0f);
            connect.addChild(this.arm);
            this.forearm = new ModelRenderer(model, 104, 0);
            this.forearm.mirror = !this.right;
            this.forearm.addBox(-1.5f, 0.0f, -1.5f, 3, 18, 3);
            this.forearm.setRotationPoint(0.0f, 21.0f, 1.0f);
            this.arm.addChild(this.forearm);
            this.hand = new ModelRenderer(model);
            this.hand.setRotationPoint(0.0f, 17.5f, 0.0f);
            this.forearm.addChild(this.hand);
            float fingerScale = 0.6f;
            for (i = 0; i < this.finger.length; ++i) {
                this.finger[i] = new ModelRenderer(model, 76, 0);
                this.finger[i].mirror = !this.right;
                this.finger[i].addBox(-0.5f, 0.0f, -0.5f, 1, i == 1 ? 6 : 5, 1, fingerScale);
            }
            this.finger[0].setRotationPoint(this.right ? -0.5f : 0.5f, 0.0f, -1.0f);
            this.finger[1].setRotationPoint(this.right ? -0.5f : 0.5f, 0.0f, 0.0f);
            this.finger[2].setRotationPoint(this.right ? -0.5f : 0.5f, 0.0f, 1.0f);
            for (i = 0; i < this.foreFinger.length; ++i) {
                this.foreFinger[i] = new ModelRenderer(model, 76, 0);
                this.foreFinger[i].mirror = !this.right;
                this.foreFinger[i].addBox(-0.5f, 0.0f, -0.5f, 1, i == 1 ? 6 : 5, 1, fingerScale - 0.01f);
                this.foreFinger[i].setRotationPoint(0.0f, 0.5f + (float)(i == 1 ? 6 : 5), 0.0f);
            }
            for (i = 0; i < this.finger.length; ++i) {
                this.hand.addChild(this.finger[i]);
                this.finger[i].addChild(this.foreFinger[i]);
            }
            this.thumb = new ModelRenderer(model, 76, 0);
            this.thumb.mirror = this.right;
            this.thumb.addBox(-0.5f, 0.0f, -0.5f, 1, 5, 1, fingerScale);
            this.thumb.setRotationPoint(this.right ? 0.5f : -0.5f, 0.0f, -0.5f);
            this.hand.addChild(this.thumb);
        }

        private void setAngles() {
            MutantEndermanModel.resetAngles(this.arm);
            MutantEndermanModel.resetAngles(this.forearm);
            MutantEndermanModel.resetAngles(this.hand);
            for (int i = 0; i < this.finger.length; ++i) {
                MutantEndermanModel.resetAngles(this.finger[i]);
                MutantEndermanModel.resetAngles(this.foreFinger[i]);
            }
            MutantEndermanModel.resetAngles(this.thumb);
            if (this.right) {
                this.arm.rotateAngleX = -0.5235988f;
                this.arm.rotateAngleZ = 0.5235988f;
                this.forearm.rotateAngleX = -0.62831855f;
                this.hand.rotateAngleY = -0.3926991f;
                this.finger[0].rotateAngleX = -0.2617994f;
                this.finger[1].rotateAngleZ = 0.17453294f;
                this.finger[2].rotateAngleX = 0.2617994f;
                this.foreFinger[0].rotateAngleZ = -0.2617994f;
                this.foreFinger[1].rotateAngleZ = -0.3926991f;
                this.foreFinger[2].rotateAngleZ = -0.2617994f;
                this.thumb.rotateAngleX = -0.62831855f;
                this.thumb.rotateAngleZ = -0.3926991f;
            } else {
                this.arm.rotateAngleX = -0.5235988f;
                this.arm.rotateAngleZ = -0.5235988f;
                this.forearm.rotateAngleX = -0.62831855f;
                this.hand.rotateAngleY = 0.3926991f;
                this.finger[0].rotateAngleX = -0.2617994f;
                this.finger[1].rotateAngleZ = -0.17453294f;
                this.finger[2].rotateAngleX = 0.2617994f;
                this.foreFinger[0].rotateAngleZ = 0.2617994f;
                this.foreFinger[1].rotateAngleZ = 0.3926991f;
                this.foreFinger[2].rotateAngleZ = 0.2617994f;
                this.thumb.rotateAngleX = -0.62831855f;
                this.thumb.rotateAngleZ = 0.3926991f;
            }
        }

        private void postRender(float scale) {
            this.arm.postRender(scale);
            this.forearm.postRender(scale);
            this.hand.postRender(scale);
        }
    }
}

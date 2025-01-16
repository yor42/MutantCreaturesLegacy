package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class MutantZombieModel
extends ModelBase {
    private final ModelRenderer pelvis;
    private final ModelRenderer waist;
    private final ModelRenderer chest;
    private final ModelRenderer head;
    private final ModelRenderer arm1;
    private final ModelRenderer arm2;
    private final ModelRenderer forearm1;
    private final ModelRenderer forearm2;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer foreleg1;
    private final ModelRenderer foreleg2;
    private float partialTick;

    public MutantZombieModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.pelvis = new ModelRenderer(this);
        this.pelvis.setRotationPoint(0.0f, 10.0f, 6.0f);
        this.waist = new ModelRenderer(this, 0, 44);
        this.waist.addBox(-7.0f, -16.0f, -6.0f, 14, 16, 12);
        this.pelvis.addChild(this.waist);
        this.chest = new ModelRenderer(this, 0, 16);
        this.chest.addBox(-12.0f, -12.0f, -8.0f, 24, 12, 16);
        this.chest.setRotationPoint(0.0f, -12.0f, 0.0f);
        this.waist.addChild(this.chest);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8);
        this.head.setRotationPoint(0.0f, -11.0f, -4.0f);
        this.chest.addChild(this.head);
        this.arm1 = new ModelRenderer(this, 104, 0);
        this.arm1.addBox(-3.0f, 0.0f, -3.0f, 6, 16, 6);
        this.arm1.setRotationPoint(-11.0f, -8.0f, 2.0f);
        this.chest.addChild(this.arm1);
        this.arm2 = new ModelRenderer(this, 104, 0);
        this.arm2.mirror = true;
        this.arm2.addBox(-3.0f, 0.0f, -3.0f, 6, 16, 6);
        this.arm2.setRotationPoint(11.0f, -8.0f, 2.0f);
        this.chest.addChild(this.arm2);
        this.forearm1 = new ModelRenderer(this, 104, 22);
        this.forearm1.addBox(-3.0f, 0.0f, -3.0f, 6, 16, 6, 0.1f);
        this.forearm1.setRotationPoint(0.0f, 14.0f, 0.0f);
        this.arm1.addChild(this.forearm1);
        this.forearm2 = new ModelRenderer(this, 104, 22);
        this.forearm2.mirror = true;
        this.forearm2.addBox(-3.0f, 0.0f, -3.0f, 6, 16, 6, 0.1f);
        this.forearm2.setRotationPoint(0.0f, 14.0f, 0.0f);
        this.arm2.addChild(this.forearm2);
        this.leg1 = new ModelRenderer(this, 80, 0);
        this.leg1.addBox(-3.0f, 0.0f, -3.0f, 6, 11, 6);
        this.leg1.setRotationPoint(-5.0f, -2.0f, 0.0f);
        this.pelvis.addChild(this.leg1);
        this.leg2 = new ModelRenderer(this, 80, 0);
        this.leg2.mirror = true;
        this.leg2.addBox(-3.0f, 0.0f, -3.0f, 6, 11, 6);
        this.leg2.setRotationPoint(5.0f, -2.0f, 0.0f);
        this.pelvis.addChild(this.leg2);
        this.foreleg1 = new ModelRenderer(this, 80, 17);
        this.foreleg1.addBox(-3.0f, 0.0f, -3.0f, 6, 8, 6, 0.1f);
        this.foreleg1.setRotationPoint(0.0f, 9.5f, 0.0f);
        this.leg1.addChild(this.foreleg1);
        this.foreleg2 = new ModelRenderer(this, 80, 17);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-3.0f, 0.0f, -3.0f, 6, 8, 6, 0.1f);
        this.foreleg2.setRotationPoint(0.0f, 9.5f, 0.0f);
        this.leg2.addChild(this.foreleg2);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setAngles();
        this.animate((MutantZombieEntity)entity, f, f1, f2, f3, f4, f5);
        this.pelvis.render(f5);
    }

    public void setAngles() {
        this.pelvis.rotationPointY = 10.0f;
        this.waist.rotateAngleX = 0.19634955f;
        this.chest.rotateAngleX = 0.5235988f;
        this.chest.rotateAngleY = 0.0f;
        this.head.rotateAngleX = -0.71994835f;
        this.head.rotateAngleY = 0.0f;
        this.head.rotateAngleZ = 0.0f;
        this.arm1.rotateAngleX = -0.32724923f;
        this.arm1.rotateAngleY = 0.0f;
        this.arm1.rotateAngleZ = 0.3926991f;
        this.arm2.rotateAngleX = -0.32724923f;
        this.arm2.rotateAngleY = 0.0f;
        this.arm2.rotateAngleZ = -0.3926991f;
        this.forearm1.rotateAngleX = -1.0471976f;
        this.forearm2.rotateAngleX = -1.0471976f;
        this.leg1.rotateAngleX = -0.7853982f;
        this.leg1.rotateAngleY = 0.0f;
        this.leg1.rotateAngleZ = 0.0f;
        this.leg2.rotateAngleX = -0.7853982f;
        this.leg2.rotateAngleY = 0.0f;
        this.leg2.rotateAngleZ = 0.0f;
        this.foreleg1.rotateAngleX = 0.7853982f;
        this.foreleg2.rotateAngleX = 0.7853982f;
    }

    public void animate(MutantZombieEntity zombie, float f, float f1, float f2, float f3, float f4, float f5) {
        float walkAnim1 = (MathHelper.sin((float)((f - 0.7f) * 0.4f)) + 0.7f) * f1;
        float walkAnim2 = -(MathHelper.sin((float)((f + 0.7f) * 0.4f)) - 0.7f) * f1;
        float walkAnim = MathHelper.sin((float)(f * 0.4f)) * f1;
        float breatheAnim = MathHelper.sin((float)(f2 * 0.1f));
        float faceYaw = f3 * (float)Math.PI / 180.0f;
        float facePitch = f4 * (float)Math.PI / 180.0f;
        if (zombie.deathTime <= 0) {
            float scale;
            if (zombie.getAttackID() == 1) {
                this.animateMelee(zombie.getAttackTick());
            }
            if (zombie.getAttackID() == 3) {
                this.animateRoar(zombie.getAttackTick());
                scale = 1.0f - MathHelper.clamp((float)((float)zombie.getAttackTick() / 6.0f), (float)0.0f, (float)1.0f);
                walkAnim1 *= scale;
                walkAnim2 *= scale;
                walkAnim *= scale;
                facePitch *= scale;
            }
            if (zombie.getAttackID() == 2) {
                this.animateThrow(zombie);
                scale = 1.0f - MathHelper.clamp((float)((float)zombie.getAttackTick() / 3.0f), (float)0.0f, (float)1.0f);
                walkAnim1 *= scale;
                walkAnim2 *= scale;
                walkAnim *= scale;
                facePitch *= scale;
            }
        } else {
            this.animateDeath(zombie);
            float scale = 1.0f - MathHelper.clamp((float)((float)zombie.deathTime / 6.0f), (float)0.0f, (float)1.0f);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            walkAnim *= scale;
            breatheAnim *= scale;
            faceYaw *= scale;
            facePitch *= scale;
        }
        this.chest.rotateAngleX += breatheAnim * 0.02f;
        this.arm1.rotateAngleZ -= breatheAnim * 0.05f;
        this.arm2.rotateAngleZ += breatheAnim * 0.05f;
        this.head.rotateAngleX += facePitch * 0.6f;
        this.head.rotateAngleY += faceYaw * 0.8f;
        this.head.rotateAngleZ -= faceYaw * 0.2f;
        this.chest.rotateAngleX += facePitch * 0.4f;
        this.chest.rotateAngleY += faceYaw * 0.2f;
        this.pelvis.rotationPointY += MathHelper.sin((float)(f * 0.8f)) * f1 * 0.5f;
        this.chest.rotateAngleY -= walkAnim * 0.1f;
        this.arm1.rotateAngleX -= walkAnim * 0.6f;
        this.arm2.rotateAngleX += walkAnim * 0.6f;
        this.leg1.rotateAngleX += walkAnim1 * 0.9f;
        this.leg2.rotateAngleX += walkAnim2 * 0.9f;
    }

    protected void animateMelee(int fullTick) {
        this.arm1.rotateAngleZ = 0.0f;
        this.arm2.rotateAngleZ = 0.0f;
        if (fullTick < 8) {
            float tick = ((float)fullTick + this.partialTick) / 8.0f;
            float f = -MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.2f;
            this.chest.rotateAngleX += f * 0.2f;
            this.arm1.rotateAngleX += f * 2.3f;
            this.arm1.rotateAngleZ += f1 * (float)Math.PI / 8.0f;
            this.arm2.rotateAngleX += f * 2.3f;
            this.arm2.rotateAngleZ -= f1 * (float)Math.PI / 8.0f;
            this.forearm1.rotateAngleX += f * 0.8f;
            this.forearm2.rotateAngleX += f * 0.8f;
        } else if (fullTick < 12) {
            float tick = ((float)(fullTick - 8) + this.partialTick) / 4.0f;
            float f = -MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.9f + 0.7f;
            this.chest.rotateAngleX += f * 0.9f + 0.7f;
            this.arm1.rotateAngleX += f * 0.2f - 2.1f;
            this.arm1.rotateAngleZ += f1 * 0.3f;
            this.arm2.rotateAngleX += f * 0.2f - 2.1f;
            this.arm2.rotateAngleZ -= f1 * 0.3f;
            this.forearm1.rotateAngleX += f * 1.0f + 0.2f;
            this.forearm2.rotateAngleX += f * 1.0f + 0.2f;
        } else if (fullTick < 16) {
            this.waist.rotateAngleX += 0.7f;
            this.chest.rotateAngleX += 0.7f;
            this.arm1.rotateAngleX -= 2.1f;
            this.arm1.rotateAngleZ += 0.3f;
            this.arm2.rotateAngleX -= 2.1f;
            this.arm2.rotateAngleZ -= 0.3f;
            this.forearm1.rotateAngleX += 0.2f;
            this.forearm2.rotateAngleX += 0.2f;
        } else if (fullTick < 24) {
            float tick = ((float)(fullTick - 16) + this.partialTick) / 8.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.7f;
            this.chest.rotateAngleX += f * 0.7f;
            this.arm1.rotateAngleX -= f * 2.1f;
            this.arm1.rotateAngleZ += f * -0.09269908f + 0.3926991f;
            this.arm2.rotateAngleX -= f * 2.1f;
            this.arm2.rotateAngleZ -= f * -0.09269908f + 0.3926991f;
            this.forearm1.rotateAngleX += f * 0.2f;
            this.forearm2.rotateAngleX += f * 0.2f;
        } else {
            this.arm1.rotateAngleZ += 0.3926991f;
            this.arm2.rotateAngleZ += -0.3926991f;
        }
    }

    protected void animateRoar(int fullTick) {
        float f1;
        float f;
        float tick;
        if (fullTick < 10) {
            tick = ((float)fullTick + this.partialTick) / 10.0f;
            f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            f1 = MathHelper.sin((float)(tick * (float)Math.PI * (float)Math.PI / 8.0f));
            this.waist.rotateAngleX += f * 0.2f;
            this.chest.rotateAngleX += f * 0.4f;
            this.chest.rotateAngleY += f1 * 0.06f;
            this.head.rotateAngleX += f * 0.8f;
            this.arm1.rotateAngleX -= f * 1.2f;
            this.arm1.rotateAngleZ += f * 0.6f;
            this.arm2.rotateAngleX -= f * 1.2f;
            this.arm2.rotateAngleZ -= f * 0.6f;
            this.forearm1.rotateAngleX -= f * 0.8f;
            this.forearm2.rotateAngleX -= f * 0.8f;
        } else if (fullTick < 15) {
            tick = ((float)(fullTick - 10) + this.partialTick) / 5.0f;
            f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.39634955f - 0.19634955f;
            this.chest.rotateAngleX += f * 0.6f - 0.2f;
            this.head.rotateAngleX += f * 1.0f - 0.2f;
            this.arm1.rotateAngleX -= f * 2.2f - 1.0f;
            this.arm1.rotateAngleY += f1 * 0.4f;
            this.arm1.rotateAngleZ += 0.6f;
            this.arm2.rotateAngleX -= f * 2.2f - 1.0f;
            this.arm2.rotateAngleY -= f1 * 0.4f;
            this.arm2.rotateAngleZ -= 0.6f;
            this.forearm1.rotateAngleX -= f * 1.0f - 0.2f;
            this.forearm2.rotateAngleX -= f * 1.0f - 0.2f;
            this.leg1.rotateAngleY += f1 * 0.3f;
            this.leg2.rotateAngleY -= f1 * 0.3f;
        } else if (fullTick < 75) {
            this.waist.rotateAngleX -= 0.19634955f;
            this.chest.rotateAngleX -= 0.2f;
            this.head.rotateAngleX -= 0.2f;
            this.addRotation(this.arm1, 1.0f, 0.4f, 0.6f);
            this.addRotation(this.arm2, 1.0f, -0.4f, -0.6f);
            this.forearm1.rotateAngleX += 0.2f;
            this.forearm2.rotateAngleX += 0.2f;
            this.leg1.rotateAngleY += 0.3f;
            this.leg2.rotateAngleY -= 0.3f;
        } else if (fullTick < 90) {
            tick = ((float)(fullTick - 75) + this.partialTick) / 15.0f;
            f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX -= f * 0.69634956f - 0.5f;
            this.chest.rotateAngleX -= f * 0.7f - 0.5f;
            this.head.rotateAngleX -= f * 0.6f - 0.4f;
            this.addRotation(this.arm1, f * 2.6f - 1.6f, f * 0.4f, f * 0.99269915f - 0.3926991f);
            this.addRotation(this.arm2, f * 2.6f - 1.6f, -f * 0.4f, -f * 0.99269915f + 0.3926991f);
            this.forearm1.rotateAngleX += f * -0.6f + 0.8f;
            this.forearm2.rotateAngleX += f * -0.6f + 0.8f;
            this.leg1.rotateAngleY += f * 0.3f;
            this.leg2.rotateAngleY -= f * 0.3f;
        } else if (fullTick < 110) {
            this.waist.rotateAngleX += 0.5f;
            this.chest.rotateAngleX += 0.5f;
            this.head.rotateAngleX += 0.4f;
            this.addRotation(this.arm1, -1.6f, 0.0f, -0.3926991f);
            this.addRotation(this.arm2, -1.6f, 0.0f, 0.3926991f);
            this.forearm1.rotateAngleX += 0.8f;
            this.forearm2.rotateAngleX += 0.8f;
        } else {
            tick = ((float)(fullTick - 110) + this.partialTick) / 10.0f;
            f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.5f;
            this.chest.rotateAngleX += f * 0.5f;
            this.head.rotateAngleX += f * 0.4f;
            this.addRotation(this.arm1, f * -1.6f, 0.0f, f * (float)(-Math.PI) / 8.0f);
            this.addRotation(this.arm2, f * -1.6f, 0.0f, f * (float)Math.PI / 8.0f);
            this.forearm1.rotateAngleX += f * 0.8f;
            this.forearm2.rotateAngleX += f * 0.8f;
        }
        if (fullTick >= 10 && fullTick < 75) {
            tick = ((float)(fullTick - 10) + this.partialTick) / 65.0f;
            f = MathHelper.sin((float)(tick * (float)Math.PI * 8.0f));
            f1 = MathHelper.sin((float)(tick * (float)Math.PI * 8.0f + 0.7853982f));
            this.head.rotateAngleY += f * 0.5f - f1 * 0.2f;
            this.head.rotateAngleZ -= f * 0.5f;
            this.chest.rotateAngleY += f1 * 0.06f;
        }
    }

    protected void animateThrow(MutantZombieEntity zombie) {
        if (zombie.getAttackTick() < 3) {
            float tick = ((float)zombie.getAttackTick() + this.partialTick) / 3.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.chest.rotateAngleX -= f * 0.4f;
            this.arm1.rotateAngleX -= f * 1.8f;
            this.arm1.rotateAngleZ -= f * (float)Math.PI / 8.0f;
            this.arm2.rotateAngleX -= f * 1.8f;
            this.arm2.rotateAngleZ += f * (float)Math.PI / 8.0f;
        } else if (zombie.getAttackTick() < 5) {
            this.chest.rotateAngleX -= 0.4f;
            this.arm1.rotateAngleX -= 1.0f;
            this.arm1.rotateAngleZ = 0.0f;
            this.arm2.rotateAngleX -= 1.0f;
            this.arm2.rotateAngleZ = 0.0f;
        } else if (zombie.getAttackTick() < 8) {
            float tick = ((float)(zombie.getAttackTick() - 5) + this.partialTick) / 3.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f1 * 0.2f;
            this.chest.rotateAngleX -= f * 0.6f - 0.2f;
            this.arm1.rotateAngleX -= f * 2.2f - 0.4f;
            this.arm1.rotateAngleZ -= f * (float)Math.PI / 8.0f;
            this.arm2.rotateAngleX -= f * 2.2f - 0.4f;
            this.arm2.rotateAngleZ += f * (float)Math.PI / 8.0f;
            this.forearm1.rotateAngleX -= f1 * 0.4f;
            this.forearm2.rotateAngleX -= f1 * 0.4f;
        } else if (zombie.getAttackTick() < 10) {
            this.waist.rotateAngleX += 0.2f;
            this.chest.rotateAngleX += 0.2f;
            this.arm1.rotateAngleX += 0.4f;
            this.arm2.rotateAngleX += 0.4f;
            this.forearm1.rotateAngleX -= 0.4f;
            this.forearm2.rotateAngleX -= 0.4f;
        } else if (zombie.getAttackTick() < 15) {
            float tick = ((float)(zombie.getAttackTick() - 10) + this.partialTick) / 5.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.39634955f - 0.19634955f;
            this.chest.rotateAngleX += f * 0.8f - 0.6f;
            this.arm1.rotateAngleX += f * 3.0f - 2.6f;
            this.arm2.rotateAngleX += f * 3.0f - 2.6f;
            this.forearm1.rotateAngleX -= f * 0.4f;
            this.forearm2.rotateAngleX -= f * 0.4f;
            this.leg1.rotateAngleX += f1 * 0.6f;
            this.leg2.rotateAngleX += f1 * 0.6f;
        } else if (zombie.throwHitTick == -1) {
            this.waist.rotateAngleX -= 0.19634955f;
            this.chest.rotateAngleX -= 0.6f;
            this.arm1.rotateAngleX -= 2.6f;
            this.arm2.rotateAngleX -= 2.6f;
            this.leg1.rotateAngleX += 0.6f;
            this.leg2.rotateAngleX += 0.6f;
        } else if (zombie.throwHitTick < 5) {
            float tick = ((float)zombie.throwHitTick + this.partialTick) / 3.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            float f1 = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX -= f * 0.39634955f - 0.2f;
            this.chest.rotateAngleX -= f * 0.8f - 0.2f;
            this.addRotation(this.arm1, -(f * 2.2f + 0.4f), -f1 * (float)Math.PI / 8.0f, f1 * 0.4f);
            this.addRotation(this.arm2, -(f * 2.2f + 0.4f), f1 * (float)Math.PI / 8.0f, -f1 * 0.4f);
            this.forearm1.rotateAngleX += f1 * 0.2f;
            this.forearm2.rotateAngleX += f1 * 0.2f;
            this.leg1.rotateAngleX += f * 0.8f - 0.2f;
            this.leg2.rotateAngleX += f * 0.8f - 0.2f;
        } else if (zombie.throwFinishTick == -1) {
            this.waist.rotateAngleX += 0.2f;
            this.chest.rotateAngleX += 0.2f;
            this.addRotation(this.arm1, -0.4f, -0.3926991f, 0.4f);
            this.addRotation(this.arm2, -0.4f, 0.3926991f, -0.4f);
            this.forearm1.rotateAngleX += 0.2f;
            this.forearm2.rotateAngleX += 0.2f;
            this.leg1.rotateAngleX -= 0.2f;
            this.leg2.rotateAngleX -= 0.2f;
        } else if (zombie.throwFinishTick < 10) {
            float tick = ((float)zombie.throwFinishTick + this.partialTick) / 10.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.waist.rotateAngleX += f * 0.2f;
            this.chest.rotateAngleX += f * 0.2f;
            this.addRotation(this.arm1, -f * 0.4f, -f * (float)Math.PI / 8.0f, f * 0.4f);
            this.addRotation(this.arm1, -f * 0.4f, f * (float)Math.PI / 8.0f, -f * 0.4f);
            this.forearm1.rotateAngleX += f * 0.2f;
            this.forearm2.rotateAngleX += f * 0.2f;
            this.leg1.rotateAngleX -= f * 0.2f;
            this.leg2.rotateAngleX -= f * 0.2f;
        }
    }

    protected void animateDeath(MutantZombieEntity zombie) {
        if (zombie.deathTime <= 20) {
            float tick = ((float)zombie.deathTime + this.partialTick - 1.0f) / 20.0f;
            float f = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
            this.pelvis.rotationPointY += f * 28.0f;
            this.head.rotateAngleX -= f * (float)Math.PI / 10.0f;
            this.head.rotateAngleY += f * (float)Math.PI / 5.0f;
            this.chest.rotateAngleX -= f * (float)Math.PI / 12.0f;
            this.waist.rotateAngleX -= f * (float)Math.PI / 10.0f;
            this.arm1.rotateAngleX -= f * (float)Math.PI / 2.0f;
            this.arm1.rotateAngleY += f * (float)Math.PI / 2.8f;
            this.arm2.rotateAngleX -= f * (float)Math.PI / 2.0f;
            this.arm2.rotateAngleY -= f * (float)Math.PI / 2.8f;
            this.leg1.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.leg1.rotateAngleZ += f * (float)Math.PI / 12.0f;
            this.leg2.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.leg2.rotateAngleZ -= f * (float)Math.PI / 12.0f;
        } else if (zombie.deathTime <= 100) {
            this.pelvis.rotationPointY += 28.0f;
            this.head.rotateAngleX -= 0.31415927f;
            this.head.rotateAngleY += 0.62831855f;
            this.chest.rotateAngleX -= 0.2617994f;
            this.waist.rotateAngleX -= 0.31415927f;
            this.arm1.rotateAngleX = (float)((double)this.arm1.rotateAngleX - 1.57079635);
            this.arm1.rotateAngleY = (float)((double)this.arm1.rotateAngleY + 1.12199739);
            this.arm2.rotateAngleX = (float)((double)this.arm2.rotateAngleX - 1.57079635);
            this.arm2.rotateAngleY = (float)((double)this.arm2.rotateAngleY - 1.12199739);
            this.leg1.rotateAngleX += 0.5235988f;
            this.leg1.rotateAngleZ += 0.2617994f;
            this.leg2.rotateAngleX += 0.5235988f;
            this.leg2.rotateAngleZ -= 0.2617994f;
        } else {
            float tick = ((float)(40 - (140 - zombie.deathTime)) + this.partialTick) / 40.0f;
            float f = MathHelper.cos((float)(tick * (float)Math.PI / 2.0f));
            this.pelvis.rotationPointY += f * 28.0f;
            this.head.rotateAngleX -= f * (float)Math.PI / 10.0f;
            this.head.rotateAngleY += f * (float)Math.PI / 5.0f;
            this.chest.rotateAngleX -= f * (float)Math.PI / 12.0f;
            this.waist.rotateAngleX -= f * (float)Math.PI / 10.0f;
            this.arm1.rotateAngleX -= f * (float)Math.PI / 2.0f;
            this.arm1.rotateAngleY += f * (float)Math.PI / 2.8f;
            this.arm2.rotateAngleX -= f * (float)Math.PI / 2.0f;
            this.arm2.rotateAngleY -= f * (float)Math.PI / 2.8f;
            this.leg1.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.leg1.rotateAngleZ += f * (float)Math.PI / 12.0f;
            this.leg2.rotateAngleX += f * (float)Math.PI / 6.0f;
            this.leg2.rotateAngleZ -= f * (float)Math.PI / 12.0f;
        }
    }

    public void addRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX += x;
        model.rotateAngleY += y;
        model.rotateAngleZ += z;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public void setLivingAnimations(EntityLivingBase entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.partialTick = partialTick;
    }
}

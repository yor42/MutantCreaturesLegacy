package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class MutantZombieModel extends ModelBase {
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
        this.pelvis.setRotationPoint(0.0F, 10.0F, 6.0F);
        this.waist = new ModelRenderer(this, 0, 44);
        this.waist.addBox(-7.0F, -16.0F, -6.0F, 14, 16, 12);
        this.pelvis.addChild(this.waist);
        this.chest = new ModelRenderer(this, 0, 16);
        this.chest.addBox(-12.0F, -12.0F, -8.0F, 24, 12, 16);
        this.chest.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.waist.addChild(this.chest);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
        this.head.setRotationPoint(0.0F, -11.0F, -4.0F);
        this.chest.addChild(this.head);
        this.arm1 = new ModelRenderer(this, 104, 0);
        this.arm1.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6);
        this.arm1.setRotationPoint(-11.0F, -8.0F, 2.0F);
        this.chest.addChild(this.arm1);
        this.arm2 = new ModelRenderer(this, 104, 0);
        this.arm2.mirror = true;
        this.arm2.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6);
        this.arm2.setRotationPoint(11.0F, -8.0F, 2.0F);
        this.chest.addChild(this.arm2);
        this.forearm1 = new ModelRenderer(this, 104, 22);
        this.forearm1.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6, 0.1F);
        this.forearm1.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.arm1.addChild(this.forearm1);
        this.forearm2 = new ModelRenderer(this, 104, 22);
        this.forearm2.mirror = true;
        this.forearm2.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6, 0.1F);
        this.forearm2.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.arm2.addChild(this.forearm2);
        this.leg1 = new ModelRenderer(this, 80, 0);
        this.leg1.addBox(-3.0F, 0.0F, -3.0F, 6, 11, 6);
        this.leg1.setRotationPoint(-5.0F, -2.0F, 0.0F);
        this.pelvis.addChild(this.leg1);
        this.leg2 = new ModelRenderer(this, 80, 0);
        this.leg2.mirror = true;
        this.leg2.addBox(-3.0F, 0.0F, -3.0F, 6, 11, 6);
        this.leg2.setRotationPoint(5.0F, -2.0F, 0.0F);
        this.pelvis.addChild(this.leg2);
        this.foreleg1 = new ModelRenderer(this, 80, 17);
        this.foreleg1.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6, 0.1F);
        this.foreleg1.setRotationPoint(0.0F, 9.5F, 0.0F);
        this.leg1.addChild(this.foreleg1);
        this.foreleg2 = new ModelRenderer(this, 80, 17);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6, 0.1F);
        this.foreleg2.setRotationPoint(0.0F, 9.5F, 0.0F);
        this.leg2.addChild(this.foreleg2);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        setAngles();
        animate((MutantZombieEntity) entity, f, f1, f2, f3, f4, f5);
        this.pelvis.render(f5);
    }

    public void setAngles() {
        this.pelvis.rotationPointY = 10.0F;
        this.waist.rotateAngleX = 0.19634955F;
        this.chest.rotateAngleX = 0.5235988F;
        this.chest.rotateAngleY = 0.0F;
        this.head.rotateAngleX = -0.71994835F;
        this.head.rotateAngleY = 0.0F;
        this.head.rotateAngleZ = 0.0F;
        this.arm1.rotateAngleX = -0.32724923F;
        this.arm1.rotateAngleY = 0.0F;
        this.arm1.rotateAngleZ = 0.3926991F;
        this.arm2.rotateAngleX = -0.32724923F;
        this.arm2.rotateAngleY = 0.0F;
        this.arm2.rotateAngleZ = -0.3926991F;
        this.forearm1.rotateAngleX = -1.0471976F;
        this.forearm2.rotateAngleX = -1.0471976F;
        this.leg1.rotateAngleX = -0.7853982F;
        this.leg1.rotateAngleY = 0.0F;
        this.leg1.rotateAngleZ = 0.0F;
        this.leg2.rotateAngleX = -0.7853982F;
        this.leg2.rotateAngleY = 0.0F;
        this.leg2.rotateAngleZ = 0.0F;
        this.foreleg1.rotateAngleX = 0.7853982F;
        this.foreleg2.rotateAngleX = 0.7853982F;
    }

    public void animate(MutantZombieEntity zombie, float f, float f1, float f2, float f3, float f4, float f5) {
        float walkAnim1 = (MathHelper.sin((f - 0.7F) * 0.4F) + 0.7F) * f1;
        float walkAnim2 = -(MathHelper.sin((f + 0.7F) * 0.4F) - 0.7F) * f1;
        float walkAnim = MathHelper.sin(f * 0.4F) * f1;
        float breatheAnim = MathHelper.sin(f2 * 0.1F);
        float faceYaw = f3 * 3.1415927F / 180.0F;
        float facePitch = f4 * 3.1415927F / 180.0F;
        if (zombie.deathTime <= 0) {
            if (zombie.getAttackID() == 1)
                animateMelee(zombie.getAttackTick());
            if (zombie.getAttackID() == 3) {
                animateRoar(zombie.getAttackTick());
                float scale = 1.0F - MathHelper.clamp(zombie.getAttackTick() / 6.0F, 0.0F, 1.0F);
                walkAnim1 *= scale;
                walkAnim2 *= scale;
                walkAnim *= scale;
                facePitch *= scale;
            }
            if (zombie.getAttackID() == 2) {
                animateThrow(zombie);
                float scale = 1.0F - MathHelper.clamp(zombie.getAttackTick() / 3.0F, 0.0F, 1.0F);
                walkAnim1 *= scale;
                walkAnim2 *= scale;
                walkAnim *= scale;
                facePitch *= scale;
            }
        } else {
            animateDeath(zombie);
            float scale = 1.0F - MathHelper.clamp(zombie.deathTime / 6.0F, 0.0F, 1.0F);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            walkAnim *= scale;
            breatheAnim *= scale;
            faceYaw *= scale;
            facePitch *= scale;
        }
        this.chest.rotateAngleX += breatheAnim * 0.02F;
        this.arm1.rotateAngleZ -= breatheAnim * 0.05F;
        this.arm2.rotateAngleZ += breatheAnim * 0.05F;
        this.head.rotateAngleX += facePitch * 0.6F;
        this.head.rotateAngleY += faceYaw * 0.8F;
        this.head.rotateAngleZ -= faceYaw * 0.2F;
        this.chest.rotateAngleX += facePitch * 0.4F;
        this.chest.rotateAngleY += faceYaw * 0.2F;
        this.pelvis.rotationPointY += MathHelper.sin(f * 0.8F) * f1 * 0.5F;
        this.chest.rotateAngleY -= walkAnim * 0.1F;
        this.arm1.rotateAngleX -= walkAnim * 0.6F;
        this.arm2.rotateAngleX += walkAnim * 0.6F;
        this.leg1.rotateAngleX += walkAnim1 * 0.9F;
        this.leg2.rotateAngleX += walkAnim2 * 0.9F;
    }

    protected void animateMelee(int fullTick) {
        this.arm1.rotateAngleZ = 0.0F;
        this.arm2.rotateAngleZ = 0.0F;
        if (fullTick < 8) {
            float tick = (fullTick + this.partialTick) / 8.0F;
            float f = -MathHelper.sin(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.2F;
            this.chest.rotateAngleX += f * 0.2F;
            this.arm1.rotateAngleX += f * 2.3F;
            this.arm1.rotateAngleZ += f1 * 3.1415927F / 8.0F;
            this.arm2.rotateAngleX += f * 2.3F;
            this.arm2.rotateAngleZ -= f1 * 3.1415927F / 8.0F;
            this.forearm1.rotateAngleX += f * 0.8F;
            this.forearm2.rotateAngleX += f * 0.8F;
        } else if (fullTick < 12) {
            float tick = ((fullTick - 8) + this.partialTick) / 4.0F;
            float f = -MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.9F + 0.7F;
            this.chest.rotateAngleX += f * 0.9F + 0.7F;
            this.arm1.rotateAngleX += f * 0.2F - 2.1F;
            this.arm1.rotateAngleZ += f1 * 0.3F;
            this.arm2.rotateAngleX += f * 0.2F - 2.1F;
            this.arm2.rotateAngleZ -= f1 * 0.3F;
            this.forearm1.rotateAngleX += f + 0.2F;
            this.forearm2.rotateAngleX += f + 0.2F;
        } else if (fullTick < 16) {
            this.waist.rotateAngleX += 0.7F;
            this.chest.rotateAngleX += 0.7F;
            this.arm1.rotateAngleX -= 2.1F;
            this.arm1.rotateAngleZ += 0.3F;
            this.arm2.rotateAngleX -= 2.1F;
            this.arm2.rotateAngleZ -= 0.3F;
            this.forearm1.rotateAngleX += 0.2F;
            this.forearm2.rotateAngleX += 0.2F;
        } else if (fullTick < 24) {
            float tick = ((fullTick - 16) + this.partialTick) / 8.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.7F;
            this.chest.rotateAngleX += f * 0.7F;
            this.arm1.rotateAngleX -= f * 2.1F;
            this.arm1.rotateAngleZ += f * -0.09269908F + 0.3926991F;
            this.arm2.rotateAngleX -= f * 2.1F;
            this.arm2.rotateAngleZ -= f * -0.09269908F + 0.3926991F;
            this.forearm1.rotateAngleX += f * 0.2F;
            this.forearm2.rotateAngleX += f * 0.2F;
        } else {
            this.arm1.rotateAngleZ += 0.3926991F;
            this.arm2.rotateAngleZ -= 0.3926991F;
        }
    }

    protected void animateRoar(int fullTick) {
        if (fullTick < 10) {
            float tick = (fullTick + this.partialTick) / 10.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F * 3.1415927F / 8.0F);
            this.waist.rotateAngleX += f * 0.2F;
            this.chest.rotateAngleX += f * 0.4F;
            this.chest.rotateAngleY += f1 * 0.06F;
            this.head.rotateAngleX += f * 0.8F;
            this.arm1.rotateAngleX -= f * 1.2F;
            this.arm1.rotateAngleZ += f * 0.6F;
            this.arm2.rotateAngleX -= f * 1.2F;
            this.arm2.rotateAngleZ -= f * 0.6F;
            this.forearm1.rotateAngleX -= f * 0.8F;
            this.forearm2.rotateAngleX -= f * 0.8F;
        } else if (fullTick < 15) {
            float tick = ((fullTick - 10) + this.partialTick) / 5.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.39634955F - 0.19634955F;
            this.chest.rotateAngleX += f * 0.6F - 0.2F;
            this.head.rotateAngleX += f - 0.2F;
            this.arm1.rotateAngleX -= f * 2.2F - 1.0F;
            this.arm1.rotateAngleY += f1 * 0.4F;
            this.arm1.rotateAngleZ += 0.6F;
            this.arm2.rotateAngleX -= f * 2.2F - 1.0F;
            this.arm2.rotateAngleY -= f1 * 0.4F;
            this.arm2.rotateAngleZ -= 0.6F;
            this.forearm1.rotateAngleX -= f - 0.2F;
            this.forearm2.rotateAngleX -= f - 0.2F;
            this.leg1.rotateAngleY += f1 * 0.3F;
            this.leg2.rotateAngleY -= f1 * 0.3F;
        } else if (fullTick < 75) {
            this.waist.rotateAngleX -= 0.19634955F;
            this.chest.rotateAngleX -= 0.2F;
            this.head.rotateAngleX -= 0.2F;
            addRotation(this.arm1, 1.0F, 0.4F, 0.6F);
            addRotation(this.arm2, 1.0F, -0.4F, -0.6F);
            this.forearm1.rotateAngleX += 0.2F;
            this.forearm2.rotateAngleX += 0.2F;
            this.leg1.rotateAngleY += 0.3F;
            this.leg2.rotateAngleY -= 0.3F;
        } else if (fullTick < 90) {
            float tick = ((fullTick - 75) + this.partialTick) / 15.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX -= f * 0.69634956F - 0.5F;
            this.chest.rotateAngleX -= f * 0.7F - 0.5F;
            this.head.rotateAngleX -= f * 0.6F - 0.4F;
            addRotation(this.arm1, f * 2.6F - 1.6F, f * 0.4F, f * 0.99269915F - 0.3926991F);
            addRotation(this.arm2, f * 2.6F - 1.6F, -f * 0.4F, -f * 0.99269915F + 0.3926991F);
            this.forearm1.rotateAngleX += f * -0.6F + 0.8F;
            this.forearm2.rotateAngleX += f * -0.6F + 0.8F;
            this.leg1.rotateAngleY += f * 0.3F;
            this.leg2.rotateAngleY -= f * 0.3F;
        } else if (fullTick < 110) {
            this.waist.rotateAngleX += 0.5F;
            this.chest.rotateAngleX += 0.5F;
            this.head.rotateAngleX += 0.4F;
            addRotation(this.arm1, -1.6F, 0.0F, -0.3926991F);
            addRotation(this.arm2, -1.6F, 0.0F, 0.3926991F);
            this.forearm1.rotateAngleX += 0.8F;
            this.forearm2.rotateAngleX += 0.8F;
        } else {
            float tick = ((fullTick - 110) + this.partialTick) / 10.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.5F;
            this.chest.rotateAngleX += f * 0.5F;
            this.head.rotateAngleX += f * 0.4F;
            addRotation(this.arm1, f * -1.6F, 0.0F, f * -3.1415927F / 8.0F);
            addRotation(this.arm2, f * -1.6F, 0.0F, f * 3.1415927F / 8.0F);
            this.forearm1.rotateAngleX += f * 0.8F;
            this.forearm2.rotateAngleX += f * 0.8F;
        }
        if (fullTick >= 10 && fullTick < 75) {
            float tick = ((fullTick - 10) + this.partialTick) / 65.0F;
            float f = MathHelper.sin(tick * 3.1415927F * 8.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F * 8.0F + 0.7853982F);
            this.head.rotateAngleY += f * 0.5F - f1 * 0.2F;
            this.head.rotateAngleZ -= f * 0.5F;
            this.chest.rotateAngleY += f1 * 0.06F;
        }
    }

    protected void animateThrow(MutantZombieEntity zombie) {
        if (zombie.getAttackTick() < 3) {
            float tick = (zombie.getAttackTick() + this.partialTick) / 3.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.chest.rotateAngleX -= f * 0.4F;
            this.arm1.rotateAngleX -= f * 1.8F;
            this.arm1.rotateAngleZ -= f * 3.1415927F / 8.0F;
            this.arm2.rotateAngleX -= f * 1.8F;
            this.arm2.rotateAngleZ += f * 3.1415927F / 8.0F;
        } else if (zombie.getAttackTick() < 5) {
            this.chest.rotateAngleX -= 0.4F;
            this.arm1.rotateAngleX--;
            this.arm1.rotateAngleZ = 0.0F;
            this.arm2.rotateAngleX--;
            this.arm2.rotateAngleZ = 0.0F;
        } else if (zombie.getAttackTick() < 8) {
            float tick = ((zombie.getAttackTick() - 5) + this.partialTick) / 3.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f1 * 0.2F;
            this.chest.rotateAngleX -= f * 0.6F - 0.2F;
            this.arm1.rotateAngleX -= f * 2.2F - 0.4F;
            this.arm1.rotateAngleZ -= f * 3.1415927F / 8.0F;
            this.arm2.rotateAngleX -= f * 2.2F - 0.4F;
            this.arm2.rotateAngleZ += f * 3.1415927F / 8.0F;
            this.forearm1.rotateAngleX -= f1 * 0.4F;
            this.forearm2.rotateAngleX -= f1 * 0.4F;
        } else if (zombie.getAttackTick() < 10) {
            this.waist.rotateAngleX += 0.2F;
            this.chest.rotateAngleX += 0.2F;
            this.arm1.rotateAngleX += 0.4F;
            this.arm2.rotateAngleX += 0.4F;
            this.forearm1.rotateAngleX -= 0.4F;
            this.forearm2.rotateAngleX -= 0.4F;
        } else if (zombie.getAttackTick() < 15) {
            float tick = ((zombie.getAttackTick() - 10) + this.partialTick) / 5.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.39634955F - 0.19634955F;
            this.chest.rotateAngleX += f * 0.8F - 0.6F;
            this.arm1.rotateAngleX += f * 3.0F - 2.6F;
            this.arm2.rotateAngleX += f * 3.0F - 2.6F;
            this.forearm1.rotateAngleX -= f * 0.4F;
            this.forearm2.rotateAngleX -= f * 0.4F;
            this.leg1.rotateAngleX += f1 * 0.6F;
            this.leg2.rotateAngleX += f1 * 0.6F;
        } else if (zombie.throwHitTick == -1) {
            this.waist.rotateAngleX -= 0.19634955F;
            this.chest.rotateAngleX -= 0.6F;
            this.arm1.rotateAngleX -= 2.6F;
            this.arm2.rotateAngleX -= 2.6F;
            this.leg1.rotateAngleX += 0.6F;
            this.leg2.rotateAngleX += 0.6F;
        } else if (zombie.throwHitTick < 5) {
            float tick = (zombie.throwHitTick + this.partialTick) / 3.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            float f1 = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX -= f * 0.39634955F - 0.2F;
            this.chest.rotateAngleX -= f * 0.8F - 0.2F;
            addRotation(this.arm1, -(f * 2.2F + 0.4F), -f1 * 3.1415927F / 8.0F, f1 * 0.4F);
            addRotation(this.arm2, -(f * 2.2F + 0.4F), f1 * 3.1415927F / 8.0F, -f1 * 0.4F);
            this.forearm1.rotateAngleX += f1 * 0.2F;
            this.forearm2.rotateAngleX += f1 * 0.2F;
            this.leg1.rotateAngleX += f * 0.8F - 0.2F;
            this.leg2.rotateAngleX += f * 0.8F - 0.2F;
        } else if (zombie.throwFinishTick == -1) {
            this.waist.rotateAngleX += 0.2F;
            this.chest.rotateAngleX += 0.2F;
            addRotation(this.arm1, -0.4F, -0.3926991F, 0.4F);
            addRotation(this.arm2, -0.4F, 0.3926991F, -0.4F);
            this.forearm1.rotateAngleX += 0.2F;
            this.forearm2.rotateAngleX += 0.2F;
            this.leg1.rotateAngleX -= 0.2F;
            this.leg2.rotateAngleX -= 0.2F;
        } else if (zombie.throwFinishTick < 10) {
            float tick = (zombie.throwFinishTick + this.partialTick) / 10.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.waist.rotateAngleX += f * 0.2F;
            this.chest.rotateAngleX += f * 0.2F;
            addRotation(this.arm1, -f * 0.4F, -f * 3.1415927F / 8.0F, f * 0.4F);
            addRotation(this.arm1, -f * 0.4F, f * 3.1415927F / 8.0F, -f * 0.4F);
            this.forearm1.rotateAngleX += f * 0.2F;
            this.forearm2.rotateAngleX += f * 0.2F;
            this.leg1.rotateAngleX -= f * 0.2F;
            this.leg2.rotateAngleX -= f * 0.2F;
        }
    }

    protected void animateDeath(MutantZombieEntity zombie) {
        if (zombie.deathTime <= 20) {
            float tick = (zombie.deathTime + this.partialTick - 1.0F) / 20.0F;
            float f = MathHelper.sin(tick * 3.1415927F / 2.0F);
            this.pelvis.rotationPointY += f * 28.0F;
            this.head.rotateAngleX -= f * 3.1415927F / 10.0F;
            this.head.rotateAngleY += f * 3.1415927F / 5.0F;
            this.chest.rotateAngleX -= f * 3.1415927F / 12.0F;
            this.waist.rotateAngleX -= f * 3.1415927F / 10.0F;
            this.arm1.rotateAngleX -= f * 3.1415927F / 2.0F;
            this.arm1.rotateAngleY += f * 3.1415927F / 2.8F;
            this.arm2.rotateAngleX -= f * 3.1415927F / 2.0F;
            this.arm2.rotateAngleY -= f * 3.1415927F / 2.8F;
            this.leg1.rotateAngleX += f * 3.1415927F / 6.0F;
            this.leg1.rotateAngleZ += f * 3.1415927F / 12.0F;
            this.leg2.rotateAngleX += f * 3.1415927F / 6.0F;
            this.leg2.rotateAngleZ -= f * 3.1415927F / 12.0F;
        } else if (zombie.deathTime <= 100) {
            this.pelvis.rotationPointY += 28.0F;
            this.head.rotateAngleX -= 0.31415927F;
            this.head.rotateAngleY += 0.62831855F;
            this.chest.rotateAngleX -= 0.2617994F;
            this.waist.rotateAngleX -= 0.31415927F;
            this.arm1.rotateAngleX = (float) (this.arm1.rotateAngleX - 1.57079635D);
            this.arm1.rotateAngleY = (float) (this.arm1.rotateAngleY + 1.12199739D);
            this.arm2.rotateAngleX = (float) (this.arm2.rotateAngleX - 1.57079635D);
            this.arm2.rotateAngleY = (float) (this.arm2.rotateAngleY - 1.12199739D);
            this.leg1.rotateAngleX += 0.5235988F;
            this.leg1.rotateAngleZ += 0.2617994F;
            this.leg2.rotateAngleX += 0.5235988F;
            this.leg2.rotateAngleZ -= 0.2617994F;
        } else {
            float tick = ((40 - 140 - zombie.deathTime) + this.partialTick) / 40.0F;
            float f = MathHelper.cos(tick * 3.1415927F / 2.0F);
            this.pelvis.rotationPointY += f * 28.0F;
            this.head.rotateAngleX -= f * 3.1415927F / 10.0F;
            this.head.rotateAngleY += f * 3.1415927F / 5.0F;
            this.chest.rotateAngleX -= f * 3.1415927F / 12.0F;
            this.waist.rotateAngleX -= f * 3.1415927F / 10.0F;
            this.arm1.rotateAngleX -= f * 3.1415927F / 2.0F;
            this.arm1.rotateAngleY += f * 3.1415927F / 2.8F;
            this.arm2.rotateAngleX -= f * 3.1415927F / 2.0F;
            this.arm2.rotateAngleY -= f * 3.1415927F / 2.8F;
            this.leg1.rotateAngleX += f * 3.1415927F / 6.0F;
            this.leg1.rotateAngleZ += f * 3.1415927F / 12.0F;
            this.leg2.rotateAngleX += f * 3.1415927F / 6.0F;
            this.leg2.rotateAngleZ -= f * 3.1415927F / 12.0F;
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

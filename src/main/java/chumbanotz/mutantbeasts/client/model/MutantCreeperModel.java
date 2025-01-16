package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MutantCreeperModel
extends ModelBase {
    private final ModelRenderer pelvis;
    private final ModelRenderer body;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer frleg;
    private final ModelRenderer flleg;
    private final ModelRenderer frforeleg;
    private final ModelRenderer flforeleg;
    private final ModelRenderer brleg;
    private final ModelRenderer blleg;
    private final ModelRenderer brforeleg;
    private final ModelRenderer blforeleg;

    public MutantCreeperModel() {
        this(0.0f);
    }

    public MutantCreeperModel(float scale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.pelvis = new ModelRenderer(this, 0, 0);
        this.pelvis.addBox(-5.0f, -14.0f, -4.0f, 10, 14, 8, scale);
        this.pelvis.setRotationPoint(0.0f, 14.0f, -3.0f);
        this.body = new ModelRenderer(this, 36, 0);
        this.body.addBox(-4.5f, -14.0f, -3.5f, 9, 16, 7, scale);
        this.body.setRotationPoint(0.0f, -12.0f, 0.0f);
        this.pelvis.addChild(this.body);
        this.neck = new ModelRenderer(this, 68, 0);
        this.neck.addBox(-4.0f, -14.0f, -3.0f, 8, 14, 6, scale);
        this.neck.setRotationPoint(0.0f, -11.0f, 1.0f);
        this.body.addChild(this.neck);
        this.head = new ModelRenderer(this, 0, 22);
        this.head.addBox(-5.0f, -12.0f, -5.0f, 10, 12, 10, scale);
        this.head.setRotationPoint(0.0f, -12.0f, 1.0f);
        this.neck.addChild(this.head);
        this.frleg = new ModelRenderer(this, 40, 24);
        this.frleg.addBox(-3.0f, -4.0f, -14.0f, 6, 4, 14, scale);
        this.frleg.setRotationPoint(3.0f, 0.0f, 0.0f);
        this.pelvis.addChild(this.frleg);
        this.flleg = new ModelRenderer(this, 40, 24);
        this.flleg.mirror = true;
        this.flleg.addBox(-3.0f, -4.0f, -14.0f, 6, 4, 14, scale);
        this.flleg.setRotationPoint(-3.0f, 0.0f, 0.0f);
        this.pelvis.addChild(this.flleg);
        this.frforeleg = new ModelRenderer(this, 96, 0);
        this.frforeleg.addBox(-3.5f, 0.0f, -4.0f, 7, 20, 8, scale);
        this.frforeleg.setRotationPoint(0.0f, -4.0f, -14.0f);
        this.frleg.addChild(this.frforeleg);
        this.flforeleg = new ModelRenderer(this, 96, 0);
        this.flforeleg.mirror = true;
        this.flforeleg.addBox(-3.5f, 0.0f, -4.0f, 7, 20, 8, scale);
        this.flforeleg.setRotationPoint(0.0f, -4.0f, -14.0f);
        this.flleg.addChild(this.flforeleg);
        this.brleg = new ModelRenderer(this, 0, 44);
        this.brleg.addBox(-2.0f, -4.0f, 0.0f, 4, 4, 14, scale);
        this.brleg.setRotationPoint(2.0f, -2.0f, 4.0f);
        this.pelvis.addChild(this.brleg);
        this.blleg = new ModelRenderer(this, 0, 44);
        this.blleg.mirror = true;
        this.blleg.addBox(-2.0f, -4.0f, 0.0f, 4, 4, 14, scale);
        this.blleg.setRotationPoint(-2.0f, -2.0f, 4.0f);
        this.pelvis.addChild(this.blleg);
        this.brforeleg = new ModelRenderer(this, 80, 28);
        this.brforeleg.addBox(-3.0f, 0.0f, -3.0f, 6, 18, 6, scale);
        this.brforeleg.setRotationPoint(0.0f, -4.0f, 14.0f);
        this.brleg.addChild(this.brforeleg);
        this.blforeleg = new ModelRenderer(this, 80, 28);
        this.blforeleg.mirror = true;
        this.blforeleg.addBox(-3.0f, 0.0f, -3.0f, 6, 18, 6, scale);
        this.blforeleg.setRotationPoint(0.0f, -4.0f, 14.0f);
        this.blleg.addChild(this.blforeleg);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setAngles();
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.pelvis.render(scale);
    }

    private void setAngles() {
        this.pelvis.rotationPointY = 14.0f;
        this.pelvis.rotateAngleX = -0.7853982f;
        this.body.rotateAngleX = 0.9424778f;
        this.body.rotateAngleY = 0.0f;
        this.neck.rotateAngleX = 1.0471976f;
        this.head.rotateAngleX = 0.5235988f;
        this.frleg.rotateAngleX = 0.31415927f;
        this.frleg.rotateAngleY = -0.7853982f;
        this.frleg.rotateAngleZ = 0.0f;
        this.flleg.rotateAngleX = 0.31415927f;
        this.flleg.rotateAngleY = 0.7853982f;
        this.flleg.rotateAngleZ = 0.0f;
        this.frforeleg.rotateAngleX = -0.20943952f;
        this.frforeleg.rotateAngleY = 0.3926991f;
        this.flforeleg.rotateAngleX = -0.20943952f;
        this.flforeleg.rotateAngleY = -0.3926991f;
        this.brleg.rotateAngleX = 0.9f;
        this.brleg.rotateAngleY = 0.62831855f;
        this.brleg.rotateAngleZ = 0.0f;
        this.blleg.rotateAngleX = 0.9f;
        this.blleg.rotateAngleY = -0.62831855f;
        this.blleg.rotateAngleZ = 0.0f;
        this.brforeleg.rotateAngleX = 0.48332196f;
        this.blforeleg.rotateAngleX = 0.48332196f;
    }

    private void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        float breatheAnim = MathHelper.sin((float)(f2 * 0.1f));
        float walkAnim1 = (MathHelper.sin((float)(f * (float)Math.PI / 4.0f)) + 0.4f) * f1;
        float walkAnim2 = (MathHelper.sin((float)(f * (float)Math.PI / 4.0f + (float)Math.PI)) + 0.4f) * f1;
        if (walkAnim1 < 0.0f) {
            walkAnim1 = 0.0f;
        }
        if (walkAnim2 < 0.0f) {
            walkAnim2 = 0.0f;
        }
        float walkAnim3 = MathHelper.sin((float)(f * (float)Math.PI / 8.0f)) * f1;
        float walkAnim4 = (MathHelper.sin((float)(f * (float)Math.PI / 4.0f + 1.5707964f)) + 0.4f) * f1;
        float walkAnim5 = (MathHelper.sin((float)(f * (float)Math.PI / 4.0f + 4.712389f)) + 0.4f) * f1;
        if (walkAnim4 < 0.0f) {
            walkAnim4 = 0.0f;
        }
        if (walkAnim5 < 0.0f) {
            walkAnim5 = 0.0f;
        }
        float walkAnim6 = MathHelper.sin((float)(f * (float)Math.PI / 8.0f + 1.5707964f)) * f1;
        float faceYaw = f3 / 57.295776f;
        float facePitch = f4 / 57.295776f;
        float f6 = faceYaw / 3.0f;
        float f7 = facePitch / 3.0f;
        this.pelvis.rotationPointY += MathHelper.sin((float)(f * (float)Math.PI / 4.0f)) * f1 * 0.5f;
        this.body.rotateAngleX += breatheAnim * 0.02f;
        this.body.rotateAngleX += f7;
        this.body.rotateAngleY += f6;
        this.neck.rotateAngleX += breatheAnim * 0.02f;
        this.neck.rotateAngleX += f7;
        this.neck.rotateAngleY = f6;
        this.head.rotateAngleX += breatheAnim * 0.02f;
        this.head.rotateAngleX += f7;
        this.head.rotateAngleY = f6;
        this.frleg.rotateAngleX -= walkAnim1 * 0.3f;
        this.frleg.rotateAngleY += walkAnim3 * 0.2f;
        this.frleg.rotateAngleZ += walkAnim3 * 0.2f;
        this.flleg.rotateAngleX -= walkAnim2 * 0.3f;
        this.flleg.rotateAngleY -= walkAnim3 * 0.2f;
        this.flleg.rotateAngleZ -= walkAnim3 * 0.2f;
        this.brleg.rotateAngleX += walkAnim5 * 0.3f;
        this.brleg.rotateAngleY -= walkAnim6 * 0.2f;
        this.brleg.rotateAngleZ -= walkAnim6 * 0.2f;
        this.blleg.rotateAngleX += walkAnim4 * 0.3f;
        this.blleg.rotateAngleY += walkAnim6 * 0.2f;
        this.blleg.rotateAngleZ += walkAnim6 * 0.2f;
        if (this.swingProgress > -9990.0f) {
            float swingAnim = MathHelper.sin((float)(this.swingProgress * (float)Math.PI));
            this.body.rotateAngleX += swingAnim * (float)Math.PI / 3.0f;
            this.neck.rotateAngleX -= swingAnim * (float)Math.PI / 4.0f;
        }
    }
}

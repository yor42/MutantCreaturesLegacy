package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MutantCreeperModel extends ModelBase {
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
        this(0.0F);
    }

    public MutantCreeperModel(float scale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.pelvis = new ModelRenderer(this, 0, 0);
        this.pelvis.addBox(-5.0F, -14.0F, -4.0F, 10, 14, 8, scale);
        this.pelvis.setRotationPoint(0.0F, 14.0F, -3.0F);
        this.body = new ModelRenderer(this, 36, 0);
        this.body.addBox(-4.5F, -14.0F, -3.5F, 9, 16, 7, scale);
        this.body.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.pelvis.addChild(this.body);
        this.neck = new ModelRenderer(this, 68, 0);
        this.neck.addBox(-4.0F, -14.0F, -3.0F, 8, 14, 6, scale);
        this.neck.setRotationPoint(0.0F, -11.0F, 1.0F);
        this.body.addChild(this.neck);
        this.head = new ModelRenderer(this, 0, 22);
        this.head.addBox(-5.0F, -12.0F, -5.0F, 10, 12, 10, scale);
        this.head.setRotationPoint(0.0F, -12.0F, 1.0F);
        this.neck.addChild(this.head);
        this.frleg = new ModelRenderer(this, 40, 24);
        this.frleg.addBox(-3.0F, -4.0F, -14.0F, 6, 4, 14, scale);
        this.frleg.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.pelvis.addChild(this.frleg);
        this.flleg = new ModelRenderer(this, 40, 24);
        this.flleg.mirror = true;
        this.flleg.addBox(-3.0F, -4.0F, -14.0F, 6, 4, 14, scale);
        this.flleg.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.pelvis.addChild(this.flleg);
        this.frforeleg = new ModelRenderer(this, 96, 0);
        this.frforeleg.addBox(-3.5F, 0.0F, -4.0F, 7, 20, 8, scale);
        this.frforeleg.setRotationPoint(0.0F, -4.0F, -14.0F);
        this.frleg.addChild(this.frforeleg);
        this.flforeleg = new ModelRenderer(this, 96, 0);
        this.flforeleg.mirror = true;
        this.flforeleg.addBox(-3.5F, 0.0F, -4.0F, 7, 20, 8, scale);
        this.flforeleg.setRotationPoint(0.0F, -4.0F, -14.0F);
        this.flleg.addChild(this.flforeleg);
        this.brleg = new ModelRenderer(this, 0, 44);
        this.brleg.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 14, scale);
        this.brleg.setRotationPoint(2.0F, -2.0F, 4.0F);
        this.pelvis.addChild(this.brleg);
        this.blleg = new ModelRenderer(this, 0, 44);
        this.blleg.mirror = true;
        this.blleg.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 14, scale);
        this.blleg.setRotationPoint(-2.0F, -2.0F, 4.0F);
        this.pelvis.addChild(this.blleg);
        this.brforeleg = new ModelRenderer(this, 80, 28);
        this.brforeleg.addBox(-3.0F, 0.0F, -3.0F, 6, 18, 6, scale);
        this.brforeleg.setRotationPoint(0.0F, -4.0F, 14.0F);
        this.brleg.addChild(this.brforeleg);
        this.blforeleg = new ModelRenderer(this, 80, 28);
        this.blforeleg.mirror = true;
        this.blforeleg.addBox(-3.0F, 0.0F, -3.0F, 6, 18, 6, scale);
        this.blforeleg.setRotationPoint(0.0F, -4.0F, 14.0F);
        this.blleg.addChild(this.blforeleg);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setAngles();
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.pelvis.render(scale);
    }

    private void setAngles() {
        this.pelvis.rotationPointY = 14.0F;
        this.pelvis.rotateAngleX = -0.7853982F;
        this.body.rotateAngleX = 0.9424778F;
        this.body.rotateAngleY = 0.0F;
        this.neck.rotateAngleX = 1.0471976F;
        this.head.rotateAngleX = 0.5235988F;
        this.frleg.rotateAngleX = 0.31415927F;
        this.frleg.rotateAngleY = -0.7853982F;
        this.frleg.rotateAngleZ = 0.0F;
        this.flleg.rotateAngleX = 0.31415927F;
        this.flleg.rotateAngleY = 0.7853982F;
        this.flleg.rotateAngleZ = 0.0F;
        this.frforeleg.rotateAngleX = -0.20943952F;
        this.frforeleg.rotateAngleY = 0.3926991F;
        this.flforeleg.rotateAngleX = -0.20943952F;
        this.flforeleg.rotateAngleY = -0.3926991F;
        this.brleg.rotateAngleX = 0.9F;
        this.brleg.rotateAngleY = 0.62831855F;
        this.brleg.rotateAngleZ = 0.0F;
        this.blleg.rotateAngleX = 0.9F;
        this.blleg.rotateAngleY = -0.62831855F;
        this.blleg.rotateAngleZ = 0.0F;
        this.brforeleg.rotateAngleX = 0.48332196F;
        this.blforeleg.rotateAngleX = 0.48332196F;
    }

    private void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        float breatheAnim = MathHelper.sin(f2 * 0.1F);
        float walkAnim1 = (MathHelper.sin(f * 3.1415927F / 4.0F) + 0.4F) * f1;
        float walkAnim2 = (MathHelper.sin(f * 3.1415927F / 4.0F + 3.1415927F) + 0.4F) * f1;
        if (walkAnim1 < 0.0F)
            walkAnim1 = 0.0F;
        if (walkAnim2 < 0.0F)
            walkAnim2 = 0.0F;
        float walkAnim3 = MathHelper.sin(f * 3.1415927F / 8.0F) * f1;
        float walkAnim4 = (MathHelper.sin(f * 3.1415927F / 4.0F + 1.5707964F) + 0.4F) * f1;
        float walkAnim5 = (MathHelper.sin(f * 3.1415927F / 4.0F + 4.712389F) + 0.4F) * f1;
        if (walkAnim4 < 0.0F)
            walkAnim4 = 0.0F;
        if (walkAnim5 < 0.0F)
            walkAnim5 = 0.0F;
        float walkAnim6 = MathHelper.sin(f * 3.1415927F / 8.0F + 1.5707964F) * f1;
        float faceYaw = f3 / 57.295776F;
        float facePitch = f4 / 57.295776F;
        float f6 = faceYaw / 3.0F;
        float f7 = facePitch / 3.0F;
        this.pelvis.rotationPointY += MathHelper.sin(f * 3.1415927F / 4.0F) * f1 * 0.5F;
        this.body.rotateAngleX += breatheAnim * 0.02F;
        this.body.rotateAngleX += f7;
        this.body.rotateAngleY += f6;
        this.neck.rotateAngleX += breatheAnim * 0.02F;
        this.neck.rotateAngleX += f7;
        this.neck.rotateAngleY = f6;
        this.head.rotateAngleX += breatheAnim * 0.02F;
        this.head.rotateAngleX += f7;
        this.head.rotateAngleY = f6;
        this.frleg.rotateAngleX -= walkAnim1 * 0.3F;
        this.frleg.rotateAngleY += walkAnim3 * 0.2F;
        this.frleg.rotateAngleZ += walkAnim3 * 0.2F;
        this.flleg.rotateAngleX -= walkAnim2 * 0.3F;
        this.flleg.rotateAngleY -= walkAnim3 * 0.2F;
        this.flleg.rotateAngleZ -= walkAnim3 * 0.2F;
        this.brleg.rotateAngleX += walkAnim5 * 0.3F;
        this.brleg.rotateAngleY -= walkAnim6 * 0.2F;
        this.brleg.rotateAngleZ -= walkAnim6 * 0.2F;
        this.blleg.rotateAngleX += walkAnim4 * 0.3F;
        this.blleg.rotateAngleY += walkAnim6 * 0.2F;
        this.blleg.rotateAngleZ += walkAnim6 * 0.2F;
        if (this.swingProgress > -9990.0F) {
            float swingAnim = MathHelper.sin(this.swingProgress * 3.1415927F);
            this.body.rotateAngleX += swingAnim * 3.1415927F / 3.0F;
            this.neck.rotateAngleX -= swingAnim * 3.1415927F / 4.0F;
        }
    }
}

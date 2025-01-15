package chumbanotz.mutantbeasts.client.model;

import chumbanotz.mutantbeasts.client.animationapi.JointModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class MutantSkeletonPartModel extends ModelBase {
    private final ModelRenderer pelvis;

    private final MutantSkeletonModel.Spine[] spine;

    private final JointModelRenderer head;

    private final ModelRenderer jaw;

    private final JointModelRenderer arm1;

    private final JointModelRenderer arm2;

    private final JointModelRenderer forearm1;

    private final JointModelRenderer forearm2;

    private final JointModelRenderer leg1;

    private final JointModelRenderer leg2;

    private final JointModelRenderer foreleg1;

    private final JointModelRenderer foreleg2;

    private final ModelRenderer shoulder1;

    private final ModelRenderer shoulder2;

    public MutantSkeletonPartModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.pelvis = new ModelRenderer(this, 0, 16);
        this.pelvis.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 6);
        this.spine = new MutantSkeletonModel.Spine[3];
        for (int i = 0; i < this.spine.length; i++) {
            this.spine[i] = new MutantSkeletonModel.Spine(this, true);
            this.boxList.remove((this.spine[i]).middle);
        }
        this.head = new JointModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.4F);
        this.jaw = new ModelRenderer(this, 72, 0);
        this.jaw.addBox(-4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F);
        this.jaw.setRotationPoint(0.0F, 3.8F, 3.7F);
        this.head.addChild(this.jaw);
        this.arm1 = new JointModelRenderer(this, 0, 28);
        this.arm1.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
        this.arm2 = new JointModelRenderer(this, 0, 28);
        this.arm2.mirror = true;
        this.arm2.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
        this.forearm1 = new JointModelRenderer(this, 16, 28);
        this.forearm1.addBox(-2.0F, -7.0F, -2.0F, 4, 14, 4, -0.01F);
        this.forearm2 = new JointModelRenderer(this, 16, 28);
        this.forearm2.mirror = true;
        this.forearm2.addBox(-2.0F, -7.0F, -2.0F, 4, 14, 4, -0.01F);
        this.leg1 = new JointModelRenderer(this, 0, 28);
        this.leg1.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
        this.leg2 = new JointModelRenderer(this, 0, 28);
        this.leg2.mirror = true;
        this.leg2.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
        this.foreleg1 = new JointModelRenderer(this, 32, 28);
        this.foreleg1.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
        this.foreleg2 = new JointModelRenderer(this, 32, 28);
        this.foreleg2.mirror = true;
        this.foreleg2.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
        this.shoulder1 = new ModelRenderer(this, 28, 16);
        this.shoulder1.addBox(-4.0F, -1.5F, -3.0F, 8, 3, 6);
        this.shoulder2 = new ModelRenderer(this, 28, 16);
        this.shoulder2.mirror = true;
        this.shoulder2.addBox(-4.0F, -1.5F, -3.0F, 8, 3, 6);
    }

    public void setAngles() {
        this.jaw.rotateAngleX = 0.09817477F;
        for (int i = 0; i < this.spine.length; i++)
            this.spine[i].setAngles(3.1415927F, (i == 1));
    }

    public ModelRenderer getSkeletonPart(int index) {
        return this.boxList.get(index);
    }
}

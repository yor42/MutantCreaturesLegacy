package chumbanotz.mutantbeasts.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CreeperMinionEggModel
extends ModelBase {
    private final ModelRenderer egg = new ModelRenderer(this, 0, 0);

    public CreeperMinionEggModel() {
        this(0.0f);
    }

    public CreeperMinionEggModel(float scale) {
        this.egg.setRotationPoint(0.0f, 22.0f, 0.0f);
        this.egg.addBox(-2.0f, 1.0f, -2.0f, 4, 1, 4, scale);
        this.egg.addBox(-3.0f, -3.0f, -3.0f, 6, 4, 6, scale);
        this.egg.addBox(-1.0f, -6.0f, -1.0f, 2, 1, 2, scale);
        this.egg.addBox(-2.0f, -5.0f, -2.0f, 4, 2, 4, scale);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.egg.render(scale);
    }
}

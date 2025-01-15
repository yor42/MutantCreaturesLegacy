package chumbanotz.mutantbeasts.client.animationapi;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class Animator {
    private final ModelBase mainModel;
    private final Map<ModelRenderer, Transform> transformMap = new HashMap<>();
    private final Map<ModelRenderer, Transform> prevTransformMap = new HashMap<>();
    private int tempTick;
    private int prevTempTick;
    private boolean correctAnim;
    private IAnimatedEntity animEntity;
    private float partialTick;

    public Animator(ModelBase model) {
        this.mainModel = model;
    }

    public IAnimatedEntity getEntity() {
        return this.animEntity;
    }

    public void update(IAnimatedEntity entity, float partialTick) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnim = false;
        this.animEntity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
        this.partialTick = partialTick;
        for (ModelRenderer box : this.mainModel.boxList) {
            box.rotateAngleX = 0.0F;
            box.rotateAngleY = 0.0F;
            box.rotateAngleZ = 0.0F;
        }
    }

    public boolean setAnimation(int animID) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnim = (this.animEntity.getAnimationID() == animID);
        return this.correctAnim;
    }

    public void startPhase(int duration) {
        if (this.correctAnim) {
            this.prevTempTick = this.tempTick;
            this.tempTick += duration;
        }
    }

    public void setStationaryPhase(int duration) {
        startPhase(duration);
        endPhase(true);
    }

    public void resetPhase(int duration) {
        startPhase(duration);
        endPhase();
    }

    public void rotate(ModelRenderer box, float x, float y, float z) {
        if (this.correctAnim) getTransform(box).addRotation(x, y, z);
    }

    public void move(ModelRenderer box, float x, float y, float z) {
        if (this.correctAnim) getTransform(box).addOffset(x, y, z);
    }

    private Transform getTransform(ModelRenderer box) {
        return this.transformMap.computeIfAbsent(box, b -> new Transform());
    }

    public void endPhase() {
        endPhase(false);
    }

    private void endPhase(boolean stationary) {
        if (this.correctAnim) {
            int animTick = this.animEntity.getAnimationTick();
            if (animTick >= this.prevTempTick && animTick < this.tempTick) if (stationary) {
                for (ModelRenderer model : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(model);
                    model.rotateAngleX += transform.getRotationX();
                    model.rotateAngleY += transform.getRotationY();
                    model.rotateAngleZ += transform.getRotationZ();
                    model.rotationPointX += transform.getOffsetX();
                    model.rotationPointY += transform.getOffsetY();
                    model.rotationPointZ += transform.getOffsetZ();
                }
            } else {
                float tick = ((animTick - this.prevTempTick) + this.partialTick) / (this.tempTick - this.prevTempTick);
                float inc = MathHelper.sin(tick * 3.1415927F / 2.0F);
                float dec = 1.0F - inc;
                for (ModelRenderer model : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(model);
                    model.rotateAngleX += dec * transform.getRotationX();
                    model.rotateAngleY += dec * transform.getRotationY();
                    model.rotateAngleZ += dec * transform.getRotationZ();
                    model.rotationPointX += dec * transform.getOffsetX();
                    model.rotationPointY += dec * transform.getOffsetY();
                    model.rotationPointZ += dec * transform.getOffsetZ();
                }
                for (ModelRenderer model : this.transformMap.keySet()) {
                    Transform transform = this.transformMap.get(model);
                    model.rotateAngleX += inc * transform.getRotationX();
                    model.rotateAngleY += inc * transform.getRotationY();
                    model.rotateAngleZ += inc * transform.getRotationZ();
                    model.rotationPointX += inc * transform.getOffsetX();
                    model.rotationPointY += inc * transform.getOffsetY();
                    model.rotationPointZ += inc * transform.getOffsetZ();
                }
            }
            if (!stationary) {
                this.prevTransformMap.clear();
                this.prevTransformMap.putAll(this.transformMap);
                this.transformMap.clear();
            }
        }
    }
}

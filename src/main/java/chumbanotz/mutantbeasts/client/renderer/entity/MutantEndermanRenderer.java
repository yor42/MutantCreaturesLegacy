package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantEndermanModel;
import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import java.lang.reflect.Field;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class MutantEndermanRenderer
extends RenderLiving<MutantEndermanEntity> {
    private static final Field RENDER_POS_X = ObfuscationReflectionHelper.findField(RenderManager.class, "field_78725_b");
    private static final Field RENDER_POS_Y = ObfuscationReflectionHelper.findField(RenderManager.class, "field_78726_c");
    private static final Field RENDER_POS_Z = ObfuscationReflectionHelper.findField(RenderManager.class, "field_78723_d");
    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_enderman/mutant_enderman");
    private static final ResourceLocation EYES_TEXTURE = MutantBeasts.getEntityTexture("mutant_enderman/eyes");
    private static final ResourceLocation DEATH_TEXTURE = MutantBeasts.getEntityTexture("mutant_enderman/death");
    private final MutantEndermanModel endermanModel;
    private final ModelEnderman cloneModel;
    private boolean teleportAttack;

    public MutantEndermanRenderer(RenderManager manager) {
        super(manager, new MutantEndermanModel(), 0.8f);
        this.endermanModel = (MutantEndermanModel)this.mainModel;
        this.cloneModel = new ModelEnderman(0.0f);
        this.addLayer(new EyesLayer());
        this.addLayer(new EndersoulLayer());
        this.addLayer(new HeldBlocksLayer());
    }

    public boolean shouldRender(MutantEndermanEntity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.getAttackID() == 4) {
            BlockPos pos = livingEntity.getTeleportPosition();
            float width = livingEntity.width / 2.0f;
            return camera.isBoundingBoxInFrustum(new AxisAlignedBB((double)pos.getX() + 0.5 - (double)width, pos.getY(), (double)pos.getZ() + 0.5 - (double)width, (double)pos.getX() + 0.5 + (double)width, (double)pos.getY() + (double)livingEntity.height, (double)pos.getZ() + 0.5 + (double)width));
        }
        return false;
    }

    protected void renderModel(MutantEndermanEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn.deathTime > 80) {
            float blendFactor = (float)(entitylivingbaseIn.deathTime - 80) / 200.0f;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, blendFactor);
            this.bindTexture(DEATH_TEXTURE);
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.depthFunc(514);
        }
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GlStateManager.depthFunc(515);
    }

    public void doRender(MutantEndermanEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        boolean death;
        if (entity.isClone()) {
            this.shadowSize = 0.5f;
            this.shadowOpaque = 0.5f;
            this.mainModel = this.cloneModel;
        } else {
            this.shadowSize = 0.8f;
            this.shadowOpaque = 1.0f;
            this.mainModel = this.endermanModel;
        }
        this.teleportAttack = false;
        this.cloneModel.isAttacking = entity.isAggressive();
        boolean forcedLook = entity.getAttackID() == 3;
        boolean scream = entity.getAttackID() == 5;
        boolean clone = entity.isClone() && entity.isAggressive();
        boolean telesmash = entity.getAttackID() == 7 && entity.getAttackTick() < 18;
        boolean bl = death = entity.getAttackID() == 8;
        if (forcedLook || scream || clone || telesmash || death) {
            double shake = 0.03;
            if (entity.getAttackTick() >= 40 && !clone && !death) {
                shake *= 0.5;
            }
            if (clone) {
                shake = 0.02;
            }
            if (death) {
                shake = entity.getAttackTick() < 80 ? (double)0.02f : (double)0.05f;
            }
            x += entity.getRNG().nextGaussian() * shake;
            z += entity.getRNG().nextGaussian() * shake;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.getAttackID() == 4) {
            this.teleportAttack = true;
            try {
                double renderPosX = (double)entity.getTeleportPosition().getX() + 0.5 - RENDER_POS_X.getDouble(this.renderManager);
                double renderPosY = (double)entity.getTeleportPosition().getY() - RENDER_POS_Y.getDouble(this.renderManager);
                double renderPosZ = (double)entity.getTeleportPosition().getZ() + 0.5 - RENDER_POS_Z.getDouble(this.renderManager);
                super.doRender(entity, renderPosX, renderPosY, renderPosZ, entityYaw, partialTicks);
                super.doRenderShadowAndFire(entity, renderPosX, renderPosY, renderPosZ, entityYaw, partialTicks);
            }
            catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Something went wrong trying to access private fields in RenderManager", e);
            }
        }
    }

    protected float getDeathMaxRotation(MutantEndermanEntity entityLivingBaseIn) {
        return 0.0f;
    }

    protected ResourceLocation getEntityTexture(MutantEndermanEntity entity) {
        return entity.isClone() ? null : TEXTURE;
    }

    class HeldBlocksLayer
    implements LayerRenderer<MutantEndermanEntity> {
        HeldBlocksLayer() {
        }

        public void doRenderLayer(MutantEndermanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!(MutantEndermanRenderer.this.mainModel instanceof MutantEndermanModel)) {
                return;
            }
            GlStateManager.enableRescaleNormal();
            for (int i = 1; i < entityIn.heldBlock.length; ++i) {
                if (entityIn.heldBlock[i] == 0) continue;
                GlStateManager.pushMatrix();
                ((MutantEndermanModel)MutantEndermanRenderer.this.mainModel).postRenderArm(0.0625f, i);
                GlStateManager.translate(0.0f, 1.2f, 0.0f);
                float tick = (float)entityIn.ticksExisted + (float)i * 2.0f * (float)Math.PI + partialTicks;
                GlStateManager.rotate(tick * 10.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(tick * 8.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(tick * 6.0f, 0.0f, 0.0f, 1.0f);
                float f = 0.75f;
                GlStateManager.scale(-f, -f, f);
                int var4 = entityIn.getBrightnessForRender();
                int var5 = var4 % 65536;
                int var6 = var4 / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5, (float)var6);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                MutantEndermanRenderer.this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.translate(-0.5f, -0.5f, 0.5f);
                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Block.getStateById(entityIn.heldBlock[i]), 1.0f);
                GlStateManager.popMatrix();
            }
            GlStateManager.disableRescaleNormal();
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }

    class EndersoulLayer
    implements LayerRenderer<MutantEndermanEntity> {
        EndersoulLayer() {
        }

        public void doRenderLayer(MutantEndermanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            boolean teleport = entityIn.getAttackID() == 4 && entityIn.getAttackTick() < 10;
            boolean scream = entityIn.getAttackID() == 5;
            boolean clone = entityIn.isClone();
            if (teleport || scream || clone) {
                float glowScale = 2.0f;
                float alpha = 1.0f;
                if (teleport) {
                    glowScale = 1.2f + ((float)entityIn.getAttackTick() + partialTicks) / 10.0f;
                    if (MutantEndermanRenderer.this.teleportAttack) {
                        glowScale = 2.2f - ((float)entityIn.getAttackTick() + partialTicks) / 10.0f;
                        if (entityIn.getAttackTick() < 2) {
                            alpha = ((float)entityIn.getAttackTick() + partialTicks) / 2.0f;
                        }
                    } else if (entityIn.getAttackTick() >= 8) {
                        alpha -= ((float)(entityIn.getAttackTick() - 8) + partialTicks) / 2.0f;
                    }
                }
                if (scream) {
                    if (entityIn.getAttackTick() < 40) {
                        glowScale = 1.2f + ((float)entityIn.getAttackTick() + partialTicks) / 40.0f;
                        alpha = ((float)entityIn.getAttackTick() + partialTicks) / 40.0f;
                    } else if (entityIn.getAttackTick() < 160) {
                        glowScale = 2.2f;
                    } else {
                        glowScale = 2.2f - ((float)entityIn.getAttackTick() + partialTicks) / 10.0f;
                        alpha = 1.0f - ((float)entityIn.getAttackTick() + partialTicks) / 40.0f;
                    }
                }
                if (!clone) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(glowScale, glowScale * 0.8f, glowScale);
                }
                EndersoulCloneRenderer.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, MutantEndermanRenderer.this.mainModel, alpha);
                if (!clone) {
                    GlStateManager.popMatrix();
                }
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }

    class EyesLayer
    implements LayerRenderer<MutantEndermanEntity> {
        EyesLayer() {
        }

        public void doRenderLayer(MutantEndermanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!entityIn.isClone()) {
                GlStateManager.disableLighting();
                GlStateManager.depthMask((!entityIn.isInvisible() ? 1 : 0) != 0);
                MutantEndermanRenderer.this.bindTexture(EYES_TEXTURE);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0f, 0.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                MutantEndermanRenderer.this.mainModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                MutantEndermanRenderer.this.setLightmap(entityIn);
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }
}

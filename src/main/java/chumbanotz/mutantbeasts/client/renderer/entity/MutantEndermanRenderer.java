package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.MutantEndermanModel;
import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
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

import java.lang.reflect.Field;

public class MutantEndermanRenderer extends RenderLiving<MutantEndermanEntity> {
    private static final Field RENDER_POS_X = ObfuscationReflectionHelper.findField(RenderManager.class, "field_78725_b");

    private static final Field RENDER_POS_Y = ObfuscationReflectionHelper.findField(RenderManager.class, "field_78726_c");

    private static final Field RENDER_POS_Z = ObfuscationReflectionHelper.findField(RenderManager.class, "field_78723_d");

    private static final ResourceLocation TEXTURE = MutantBeasts.getEntityTexture("mutant_enderman/mutant_enderman");

    private static final ResourceLocation EYES_TEXTURE = MutantBeasts.getEntityTexture("mutant_enderman/eyes");

    private static final ResourceLocation DEATH_TEXTURE = MutantBeasts.getEntityTexture("mutant_enderman/death");

    private final MutantEndermanModel endermanModel = (MutantEndermanModel) this.mainModel;

    private final ModelEnderman cloneModel = new ModelEnderman(0.0F);

    private boolean teleportAttack;

    public MutantEndermanRenderer(RenderManager manager) {
        super(manager, new MutantEndermanModel(), 0.8F);
        addLayer(new EyesLayer());
        addLayer(new EndersoulLayer());
        addLayer(new HeldBlocksLayer());
    }

    public boolean shouldRender(MutantEndermanEntity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) return true;
        if (livingEntity.getAttackID() == 4) {
            BlockPos pos = livingEntity.getTeleportPosition();
            float width = livingEntity.width / 2.0F;
            return camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos.getX() + 0.5D - width, pos.getY(), pos.getZ() + 0.5D - width, pos.getX() + 0.5D + width, pos.getY() + livingEntity.height, pos.getZ() + 0.5D + width));
        }
        return false;
    }

    protected void renderModel(MutantEndermanEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entitylivingbaseIn.deathTime > 80) {
            float blendFactor = (entitylivingbaseIn.deathTime - 80) / 200.0F;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, blendFactor);
            bindTexture(DEATH_TEXTURE);
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.depthFunc(514);
        }
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GlStateManager.depthFunc(515);
    }

    public void doRender(MutantEndermanEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.isClone()) {
            this.shadowSize = 0.5F;
            this.shadowOpaque = 0.5F;
            this.mainModel = this.cloneModel;
        } else {
            this.shadowSize = 0.8F;
            this.shadowOpaque = 1.0F;
            this.mainModel = this.endermanModel;
        }
        this.teleportAttack = false;
        this.cloneModel.isAttacking = entity.isAggressive();
        boolean forcedLook = (entity.getAttackID() == 3);
        boolean scream = (entity.getAttackID() == 5);
        boolean clone = (entity.isClone() && entity.isAggressive());
        boolean telesmash = (entity.getAttackID() == 7 && entity.getAttackTick() < 18);
        boolean death = (entity.getAttackID() == 8);
        if (forcedLook || scream || clone || telesmash || death) {
            double shake = 0.03D;
            if (entity.getAttackTick() >= 40 && !clone && !death) shake *= 0.5D;
            if (clone) shake = 0.02D;
            if (death) shake = (entity.getAttackTick() < 80) ? 0.019999999552965164D : 0.05000000074505806D;
            x += entity.getRNG().nextGaussian() * shake;
            z += entity.getRNG().nextGaussian() * shake;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.getAttackID() == 4) {
            this.teleportAttack = true;
            try {
                double renderPosX = entity.getTeleportPosition().getX() + 0.5D - RENDER_POS_X.getDouble(this.renderManager);
                double renderPosY = entity.getTeleportPosition().getY() - RENDER_POS_Y.getDouble(this.renderManager);
                double renderPosZ = entity.getTeleportPosition().getZ() + 0.5D - RENDER_POS_Z.getDouble(this.renderManager);
                super.doRender(entity, renderPosX, renderPosY, renderPosZ, entityYaw, partialTicks);
                doRenderShadowAndFire(entity, renderPosX, renderPosY, renderPosZ, entityYaw, partialTicks);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException("Something went wrong trying to access private fields in RenderManager", e);
            }
        }
    }

    protected float getDeathMaxRotation(MutantEndermanEntity entityLivingBaseIn) {
        return 0.0F;
    }

    protected ResourceLocation getEntityTexture(MutantEndermanEntity entity) {
        return entity.isClone() ? null : TEXTURE;
    }

    class EyesLayer implements LayerRenderer<MutantEndermanEntity> {
        public void doRenderLayer(MutantEndermanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!entityIn.isClone()) {
                GlStateManager.disableLighting();
                GlStateManager.depthMask(!entityIn.isInvisible());
                MutantEndermanRenderer.this.bindTexture(MutantEndermanRenderer.EYES_TEXTURE);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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

    class EndersoulLayer implements LayerRenderer<MutantEndermanEntity> {
        public void doRenderLayer(MutantEndermanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            boolean teleport = (entityIn.getAttackID() == 4 && entityIn.getAttackTick() < 10);
            boolean scream = (entityIn.getAttackID() == 5);
            boolean clone = entityIn.isClone();
            if (teleport || scream || clone) {
                float glowScale = 2.0F;
                float alpha = 1.0F;
                if (teleport) {
                    glowScale = 1.2F + (entityIn.getAttackTick() + partialTicks) / 10.0F;
                    if (MutantEndermanRenderer.this.teleportAttack) {
                        glowScale = 2.2F - (entityIn.getAttackTick() + partialTicks) / 10.0F;
                        if (entityIn.getAttackTick() < 2) alpha = (entityIn.getAttackTick() + partialTicks) / 2.0F;
                    } else if (entityIn.getAttackTick() >= 8) {
                        alpha -= ((entityIn.getAttackTick() - 8) + partialTicks) / 2.0F;
                    }
                }
                if (scream) if (entityIn.getAttackTick() < 40) {
                    glowScale = 1.2F + (entityIn.getAttackTick() + partialTicks) / 40.0F;
                    alpha = (entityIn.getAttackTick() + partialTicks) / 40.0F;
                } else if (entityIn.getAttackTick() < 160) {
                    glowScale = 2.2F;
                } else {
                    glowScale = 2.2F - (entityIn.getAttackTick() + partialTicks) / 10.0F;
                    alpha = 1.0F - (entityIn.getAttackTick() + partialTicks) / 40.0F;
                }
                if (!clone) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(glowScale, glowScale * 0.8F, glowScale);
                }
                EndersoulCloneRenderer.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, MutantEndermanRenderer.this.mainModel, alpha);
                if (!clone) GlStateManager.popMatrix();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }

    class HeldBlocksLayer implements LayerRenderer<MutantEndermanEntity> {
        public void doRenderLayer(MutantEndermanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!(MutantEndermanRenderer.this.mainModel instanceof MutantEndermanModel)) return;
            GlStateManager.enableRescaleNormal();
            for (int i = 1; i < entityIn.heldBlock.length; i++) {
                if (entityIn.heldBlock[i] != 0) {
                    GlStateManager.pushMatrix();
                    ((MutantEndermanModel) MutantEndermanRenderer.this.mainModel).postRenderArm(0.0625F, i);
                    GlStateManager.translate(0.0F, 1.2F, 0.0F);
                    float tick = entityIn.ticksExisted + i * 2.0F * 3.1415927F + partialTicks;
                    GlStateManager.rotate(tick * 10.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(tick * 8.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(tick * 6.0F, 0.0F, 0.0F, 1.0F);
                    float f = 0.75F;
                    GlStateManager.scale(-f, -f, f);
                    int var4 = entityIn.getBrightnessForRender();
                    int var5 = var4 % 65536;
                    int var6 = var4 / 65536;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var5, var6);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    MutantEndermanRenderer.this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    GlStateManager.translate(-0.5F, -0.5F, 0.5F);
                    Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Block.getStateById(entityIn.heldBlock[i]), 1.0F);
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.disableRescaleNormal();
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }
}

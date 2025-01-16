package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import chumbanotz.mutantbeasts.entity.projectile.ThrowableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ThrowableBlockRenderer
extends Render<ThrowableBlockEntity> {
    public ThrowableBlockRenderer(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.6f;
    }

    public void doRender(ThrowableBlockEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getThrower() instanceof MutantSnowGolemEntity) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + 0.5f, (float)z);
            GlStateManager.rotate(entity.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(((float)entity.ticksExisted + partialTicks) * 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(((float)entity.ticksExisted + partialTicks) * 12.0f, 0.0f, 0.0f, -1.0f);
            this.bindEntityTexture(entity);
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(entity.getBlockState(), 1.0f);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + 0.5f, (float)z);
            GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(((float)entity.ticksExisted + partialTicks) * 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(((float)entity.ticksExisted + partialTicks) * 12.0f, 0.0f, 0.0f, -1.0f);
            float scale = 0.75f;
            GlStateManager.scale(-scale, -scale, scale);
            this.bindEntityTexture(entity);
            int var4 = entity.getBrightnessForRender();
            int var5 = var4 % 65536;
            int var6 = var4 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5, (float)var6);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(entity.getBlockState(), 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(ThrowableBlockEntity entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

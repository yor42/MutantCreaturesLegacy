package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.entity.projectile.ThrowableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class ThrowableBlockRenderer extends Render<ThrowableBlockEntity> {
    public ThrowableBlockRenderer(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.6F;
    }

    public void doRender(ThrowableBlockEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getThrower() instanceof chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
            GlStateManager.rotate(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((entity.ticksExisted + partialTicks) * 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate((entity.ticksExisted + partialTicks) * 12.0F, 0.0F, 0.0F, -1.0F);
            bindEntityTexture(entity);
            GlStateManager.translate(-0.5F, -0.5F, 0.5F);
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(entity.getBlockState(), 1.0F);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((entity.ticksExisted + partialTicks) * 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate((entity.ticksExisted + partialTicks) * 12.0F, 0.0F, 0.0F, -1.0F);
            float scale = 0.75F;
            GlStateManager.scale(-scale, -scale, scale);
            bindEntityTexture(entity);
            int var4 = entity.getBrightnessForRender();
            int var5 = var4 % 65536;
            int var6 = var4 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var5, var6);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.translate(-0.5F, -0.5F, 0.5F);
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(entity.getBlockState(), 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(ThrowableBlockEntity entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

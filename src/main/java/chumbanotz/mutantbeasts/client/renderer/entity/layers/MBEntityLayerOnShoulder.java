package chumbanotz.mutantbeasts.client.renderer.entity.layers;

import chumbanotz.mutantbeasts.client.model.CreeperMinionModel;
import chumbanotz.mutantbeasts.client.renderer.entity.CreeperMinionRenderer;
import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MBEntityLayerOnShoulder implements LayerRenderer<EntityPlayer> {
    private final ModelParrot parrotModel = new ModelParrot();

    private final CreeperMinionModel creeperMinionModel = new CreeperMinionModel();

    private final CreeperMinionModel chargedModel = new CreeperMinionModel(2.0F);

    private static boolean isEntityOnShoulder(NBTTagCompound compound, Class<? extends Entity> entityClass) {
        return (EntityList.getClassFromName(compound.getString("id")) == entityClass);
    }

    public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        renderOnShoulder(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, true);
        renderOnShoulder(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, false);
        GlStateManager.disableRescaleNormal();
    }

    private void renderOnShoulder(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean leftShoulder) {
        NBTTagCompound compoundnbt = leftShoulder ? player.getLeftShoulderEntity() : player.getRightShoulderEntity();
        if (compoundnbt.isEmpty()) return;
        if (isEntityOnShoulder(compoundnbt, EntityParrot.class)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(leftShoulder ? 0.4F : -0.4F, player.isSneaking() ? -1.3F : -1.5F, 0.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(RenderParrot.PARROT_TEXTURES[compoundnbt.getInteger("Variant")]);
            this.parrotModel.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);
            this.parrotModel.setRotationAngles(limbSwing, limbSwingAmount, 0.0F, netHeadYaw, headPitch, scale, player);
            this.parrotModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        } else if (isEntityOnShoulder(compoundnbt, CreeperMinionEntity.class)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(leftShoulder ? 0.42F : -0.42F, player.isSneaking() ? -0.55F : -0.75F, 0.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(CreeperMinionRenderer.TEXTURE);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            this.creeperMinionModel.render(null, 0.0F, 0.0F, ageInTicks, netHeadYaw, headPitch, scale);
            if (compoundnbt.getBoolean("Powered"))
                LayerCreeperCharge.render(null, 0.0F, 0.0F, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, this.creeperMinionModel, this.chargedModel);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}

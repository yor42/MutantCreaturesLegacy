package chumbanotz.mutantbeasts.client.renderer.entity.layers;

import chumbanotz.mutantbeasts.client.model.CreeperMinionModel;
import chumbanotz.mutantbeasts.client.renderer.entity.CreeperMinionRenderer;
import chumbanotz.mutantbeasts.client.renderer.entity.layers.LayerCreeperCharge;
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

public class MBEntityLayerOnShoulder
implements LayerRenderer<EntityPlayer> {
    private final ModelParrot parrotModel = new ModelParrot();
    private final CreeperMinionModel creeperMinionModel = new CreeperMinionModel();
    private final CreeperMinionModel chargedModel = new CreeperMinionModel(2.0f);

    public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.renderOnShoulder(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, true);
        this.renderOnShoulder(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, false);
        GlStateManager.disableRescaleNormal();
    }

    private void renderOnShoulder(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean leftShoulder) {
        NBTTagCompound compoundnbt;
        NBTTagCompound nBTTagCompound = compoundnbt = leftShoulder ? player.getLeftShoulderEntity() : player.getRightShoulderEntity();
        if (compoundnbt.isEmpty()) {
            return;
        }
        if (MBEntityLayerOnShoulder.isEntityOnShoulder(compoundnbt, EntityParrot.class)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(leftShoulder ? 0.4f : -0.4f), (float)(player.isSneaking() ? -1.3f : -1.5f), (float)0.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(RenderParrot.PARROT_TEXTURES[compoundnbt.getInteger("Variant")]);
            this.parrotModel.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);
            this.parrotModel.setRotationAngles(limbSwing, limbSwingAmount, 0.0f, netHeadYaw, headPitch, scale, player);
            this.parrotModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.popMatrix();
        } else if (MBEntityLayerOnShoulder.isEntityOnShoulder(compoundnbt, CreeperMinionEntity.class)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(leftShoulder ? 0.42f : -0.42f), (float)(player.isSneaking() ? -0.55f : -0.75f), (float)0.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(CreeperMinionRenderer.TEXTURE);
            GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
            this.creeperMinionModel.render(null, 0.0f, 0.0f, ageInTicks, netHeadYaw, headPitch, scale);
            if (compoundnbt.getBoolean("Powered")) {
                LayerCreeperCharge.render(null, 0.0f, 0.0f, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, this.creeperMinionModel, this.chargedModel);
            }
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    private static boolean isEntityOnShoulder(NBTTagCompound compound, Class<? extends Entity> entityClass) {
        return EntityList.getClassFromName((String)compound.getString("id")) == entityClass;
    }
}

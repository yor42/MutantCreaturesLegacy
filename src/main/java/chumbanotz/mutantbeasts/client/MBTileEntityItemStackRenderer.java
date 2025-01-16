package chumbanotz.mutantbeasts.client;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.client.model.EndersoulHandModel;
import chumbanotz.mutantbeasts.client.renderer.entity.EndersoulCloneRenderer;
import chumbanotz.mutantbeasts.item.MBItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MBTileEntityItemStackRenderer
extends TileEntityItemStackRenderer {
    private static final ResourceLocation ENDER_SOUL_HAND_TEXTURE = MutantBeasts.getEntityTexture("endersoul_hand");
    private final EndersoulHandModel enderSoulHandModel = new EndersoulHandModel();

    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() == MBItems.ENDERSOUL_HAND) {
            GlStateManager.pushMatrix();
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            EndersoulCloneRenderer.render(player, ENDER_SOUL_HAND_TEXTURE, 0.0f, 0.0f, (float)player.ticksExisted + Minecraft.getMinecraft().getRenderPartialTicks(), 0.0f, 0.0f, 0.0f, this.enderSoulHandModel, 1.0f);
            GlStateManager.popMatrix();
        }
    }
}

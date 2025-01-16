package chumbanotz.mutantbeasts.client.gui;

import chumbanotz.mutantbeasts.MBConfig;
import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import chumbanotz.mutantbeasts.packet.CreeperMinionTrackerPacket;
import chumbanotz.mutantbeasts.packet.MBPacketHandler;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class CreeperMinionTrackerScreen
extends GuiScreen {
    private static final ResourceLocation TEXTURE = MutantBeasts.prefix("textures/gui/creeper_minion_tracker.png");
    private final int xSize = 176;
    private final int ySize = 166;
    private int guiX;
    private int guiY;
    private final CreeperMinionEntity creeperMinion;
    private boolean canRideOnShoulder;
    private boolean canDestroyBlocks;
    private boolean alwaysShowName;

    public CreeperMinionTrackerScreen(CreeperMinionEntity creeperMinion) {
        this.creeperMinion = creeperMinion;
    }

    public void initGui() {
        this.canDestroyBlocks = this.creeperMinion.canDestroyBlocks();
        this.alwaysShowName = this.creeperMinion.getAlwaysRenderNameTag();
        this.canRideOnShoulder = this.creeperMinion.canRideOnShoulder();
        this.guiX = (this.width - this.xSize) / 2;
        this.guiY = (this.height - this.ySize) / 2;
        int buttonWidth = this.xSize / 2 - 10;
        this.addButton(new GuiButton(0, this.guiX + 8, this.guiY + this.ySize - 78, buttonWidth * 2 + 4, 20, this.canDestroyBlocks()));
        this.addButton(new GuiButton(1, this.guiX + 8, this.guiY + this.ySize - 54, buttonWidth * 2 + 4, 20, this.getShowName()));
        this.addButton(new GuiButton(2, this.guiX + 8, this.guiY + this.ySize - 30, buttonWidth * 2 + 4, 20, this.canRideOnShoulder()));
        if (!this.creeperMinion.isOwner(this.mc.player)) {
            for (GuiButton button : this.buttonList) {
                button.enabled = false;
            }
        }
        if (!MBConfig.creeperMinionOnShoulder) {
            this.buttonList.get(2).enabled = false;
        }
        Keyboard.enableRepeatEvents(true);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private String getShowName() {
        return CreeperMinionTrackerScreen.format("always_show_name") + I18n.format(this.alwaysShowName ? "options.on" : "options.off", new Object[0]);
    }

    private String canDestroyBlocks() {
        return CreeperMinionTrackerScreen.format("destroys_blocks") + I18n.format(this.canDestroyBlocks ? "options.on" : "options.off", new Object[0]);
    }

    private String canRideOnShoulder() {
        if (MBConfig.creeperMinionOnShoulder) {
            return CreeperMinionTrackerScreen.format("can_ride_on_shoulder") + I18n.format(this.canRideOnShoulder ? "options.on" : "options.off", new Object[0]);
        }
        return CreeperMinionTrackerScreen.format("disabled");
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.canDestroyBlocks = !this.canDestroyBlocks;
                MBPacketHandler.INSTANCE.sendToServer(new CreeperMinionTrackerPacket(this.creeperMinion, 0, this.canDestroyBlocks));
                button.displayString = this.canDestroyBlocks();
                break;
            }
            case 1: {
                this.alwaysShowName = !this.alwaysShowName;
                MBPacketHandler.INSTANCE.sendToServer(new CreeperMinionTrackerPacket(this.creeperMinion, 1, this.alwaysShowName));
                button.displayString = this.getShowName();
                break;
            }
            case 2: {
                this.canRideOnShoulder = !this.canRideOnShoulder;
                MBPacketHandler.INSTANCE.sendToServer(new CreeperMinionTrackerPacket(this.creeperMinion, 2, this.canRideOnShoulder));
                button.displayString = this.canRideOnShoulder();
                break;
            }
        }
    }

    public void updateScreen() {
        if (!this.creeperMinion.isEntityAlive()) {
            this.mc.player.closeScreen();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, this.xSize, this.ySize);
        int health = (int)(this.creeperMinion.getHealth() * 150.0f / this.creeperMinion.getMaxHealth());
        this.drawTexturedModalRect(this.guiX + 13, this.guiY + 16, 0, 166, health, 6);
        this.fontRenderer.drawString(this.creeperMinion.getDisplayName().getUnformattedText(), this.guiX + 13, this.guiY + 5, 0x404040);
        this.fontRenderer.drawString(CreeperMinionTrackerScreen.format("health"), this.guiX + 13, this.guiY + 28, 0x404040);
        this.fontRenderer.drawString(CreeperMinionTrackerScreen.format("explosion"), this.guiX + 13, this.guiY + 48, 0x404040);
        this.fontRenderer.drawString(CreeperMinionTrackerScreen.format("blast_radius"), this.guiX + 13, this.guiY + 68, 0x404040);
        StringBuilder sb = new StringBuilder();
        sb.append(this.creeperMinion.getHealth() / 2.0f).append(" / ").append(this.creeperMinion.getMaxHealth() / 2.0f);
        this.drawCenteredString(this.fontRenderer, sb.toString(), this.guiX + this.xSize / 2 + 38, this.guiY + 30, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, this.creeperMinion.canExplodeContinuously() ? CreeperMinionTrackerScreen.format("continuous") : CreeperMinionTrackerScreen.format("one_time"), this.guiX + this.xSize / 2 + 38, this.guiY + 50, 0xFFFFFF);
        int temp = (int)(this.creeperMinion.getExplosionRadius() * 10.0f);
        sb = new StringBuilder().append((float)temp / 10.0f);
        this.drawCenteredString(this.fontRenderer, sb.toString(), this.guiX + this.xSize / 2 + 38, this.guiY + 70, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private static String format(String key, Object ... parameters) {
        return I18n.format("gui.mutantbeasts.creeper_minion_tracker." + key, parameters);
    }
}

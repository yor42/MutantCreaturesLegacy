package chumbanotz.mutantbeasts.client.gui;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.config.MBConfig;
import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import chumbanotz.mutantbeasts.packet.CreeperMinionTrackerPacket;
import chumbanotz.mutantbeasts.packet.MBPacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class CreeperMinionTrackerScreen extends GuiScreen {
    private static final ResourceLocation TEXTURE = MutantBeasts.prefix("textures/gui/creeper_minion_tracker.png");

    private final CreeperMinionEntity creeperMinion;
    private int guiX;
    private int guiY;
    private boolean canRideOnShoulder;

    private boolean canDestroyBlocks;

    private boolean alwaysShowName;

    public CreeperMinionTrackerScreen(CreeperMinionEntity creeperMinion) {
        this.creeperMinion = creeperMinion;
    }

    private static String format(String key, Object... parameters) {
        return I18n.format("gui.mutantbeasts.creeper_minion_tracker." + key, parameters);
    }

    public void initGui() {
        this.canDestroyBlocks = this.creeperMinion.canDestroyBlocks();
        this.alwaysShowName = this.creeperMinion.getAlwaysRenderNameTag();
        this.canRideOnShoulder = this.creeperMinion.canRideOnShoulder();
        this.guiX = (this.width - 176) / 2;
        this.guiY = (this.height - 166) / 2;
        int buttonWidth = 176 / 2 - 10;
        addButton(new GuiButton(0, this.guiX + 8, this.guiY + 166 - 78, buttonWidth * 2 + 4, 20, canDestroyBlocks()));
        addButton(new GuiButton(1, this.guiX + 8, this.guiY + 166 - 54, buttonWidth * 2 + 4, 20, getShowName()));
        addButton(new GuiButton(2, this.guiX + 8, this.guiY + 166 - 30, buttonWidth * 2 + 4, 20, canRideOnShoulder()));
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
        return format("always_show_name") + I18n.format(this.alwaysShowName ? "options.on" : "options.off");
    }

    private String canDestroyBlocks() {
        return format("destroys_blocks") + I18n.format(this.canDestroyBlocks ? "options.on" : "options.off");
    }

    private String canRideOnShoulder() {
        if (MBConfig.creeperMinionOnShoulder)
            return format("can_ride_on_shoulder") + I18n.format(this.canRideOnShoulder ? "options.on" : "options.off");
        return format("disabled");
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.canDestroyBlocks = !this.canDestroyBlocks;
                MBPacketHandler.INSTANCE.sendToServer(new CreeperMinionTrackerPacket(this.creeperMinion, 0, this.canDestroyBlocks));
                button.displayString = canDestroyBlocks();
                break;
            case 1:
                this.alwaysShowName = !this.alwaysShowName;
                MBPacketHandler.INSTANCE.sendToServer(new CreeperMinionTrackerPacket(this.creeperMinion, 1, this.alwaysShowName));
                button.displayString = getShowName();
                break;
            case 2:
                this.canRideOnShoulder = !this.canRideOnShoulder;
                MBPacketHandler.INSTANCE.sendToServer(new CreeperMinionTrackerPacket(this.creeperMinion, 2, this.canRideOnShoulder));
                button.displayString = canRideOnShoulder();
                break;
        }
    }

    public void updateScreen() {
        if (!this.creeperMinion.isEntityAlive()) this.mc.player.closeScreen();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(this.guiX, this.guiY, 0, 0, 176, 166);
        int health = (int) (this.creeperMinion.getHealth() * 150.0F / this.creeperMinion.getMaxHealth());
        drawTexturedModalRect(this.guiX + 13, this.guiY + 16, 0, 166, health, 6);
        this.fontRenderer.drawString(this.creeperMinion.getDisplayName().getUnformattedText(), this.guiX + 13, this.guiY + 5, 4210752);
        this.fontRenderer.drawString(format("health"), this.guiX + 13, this.guiY + 28, 4210752);
        this.fontRenderer.drawString(format("explosion"), this.guiX + 13, this.guiY + 48, 4210752);
        this.fontRenderer.drawString(format("blast_radius"), this.guiX + 13, this.guiY + 68, 4210752);
        StringBuilder sb = new StringBuilder();
        sb.append(this.creeperMinion.getHealth() / 2.0F).append(" / ").append(this.creeperMinion.getMaxHealth() / 2.0F);
        drawCenteredString(this.fontRenderer, sb.toString(), this.guiX + 176 / 2 + 38, this.guiY + 30, 16777215);
        drawCenteredString(this.fontRenderer, this.creeperMinion.canExplodeContinuously() ? format("continuous") : format("one_time"), this.guiX + 176 / 2 + 38, this.guiY + 50, 16777215);
        int temp = (int) (this.creeperMinion.getExplosionRadius() * 10.0F);
        sb = (new StringBuilder()).append(temp / 10.0F);
        drawCenteredString(this.fontRenderer, sb.toString(), this.guiX + 176 / 2 + 38, this.guiY + 70, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}

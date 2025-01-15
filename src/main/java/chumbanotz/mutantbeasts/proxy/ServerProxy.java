package chumbanotz.mutantbeasts.proxy;

import chumbanotz.mutantbeasts.util.IProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class ServerProxy implements IProxy {
    public void preInit() {
    }

    public void init() {
    }

    public World getWorldClient() {
        return null;
    }

    public Object getMutantSkeletonArmorModel() {
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        throw new IllegalArgumentException("Can't call client gui element on the server!");
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}

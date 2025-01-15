package chumbanotz.mutantbeasts.util;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public interface IProxy extends IGuiHandler {
    void preInit();

    void init();

    World getWorldClient();

    Object getMutantSkeletonArmorModel();
}

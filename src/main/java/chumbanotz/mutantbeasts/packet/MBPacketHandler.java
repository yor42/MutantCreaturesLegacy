package chumbanotz.mutantbeasts.packet;

import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MBPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);

    public static void register() {
        INSTANCE.registerMessage(CreeperMinionTrackerPacket.Handler.class, CreeperMinionTrackerPacket.class, 0, Side.SERVER);
        INSTANCE.registerMessage(HeldBlockPacket.Handler.class, HeldBlockPacket.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(SpawnParticlePacket.Handler.class, SpawnParticlePacket.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(TeleportPacket.Handler.class, TeleportPacket.class, 3, Side.CLIENT);
    }
}

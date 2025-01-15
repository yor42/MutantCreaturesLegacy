package chumbanotz.mutantbeasts.client.animationapi;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public interface IAnimatedEntity extends IEntityAdditionalSpawnData {
    int getAnimationID();

    void setAnimationID(int paramInt);

    int getAnimationTick();

    void setAnimationTick(int paramInt);

    default void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(getAnimationID());
        buffer.writeInt(getAnimationTick());
    }

    default void readSpawnData(ByteBuf additionalData) {
        setAnimationID(additionalData.readInt());
        setAnimationTick(additionalData.readInt());
    }
}

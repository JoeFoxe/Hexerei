package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.BirdData;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class BirdPacket<T extends BirdData> implements IHexPacket<T> {

    @Override
    public void encode(T data, PacketBuffer buf) {
        buf.writeInt(data.entityId);
    }

    @Override
    public abstract T decode(PacketBuffer buf);

    @Override
    public final void handle(T data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity target = ctx.get().getSender().world.getEntityByID(data.entityId);

            if (!(target instanceof PigeonEntity)) {
                return;
            }
            this.handleBird((PigeonEntity) target, data, ctx);
        });
        ctx.get().setPacketHandled(true);
    }
    public abstract void handleBird(PigeonEntity birdIn, T data, Supplier<NetworkEvent.Context> ctx);
}
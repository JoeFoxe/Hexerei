package net.joefoxe.hexerei.util;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IHexPacket<D> {

    public void encode(D data, PacketBuffer buf);

    public D decode(PacketBuffer buf);

    public void handle(D data, Supplier<NetworkEvent.Context> ctx);
}
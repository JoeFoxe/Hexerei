package net.joefoxe.hexerei.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PigeonInventoryPagePacket implements IHexPacket<PigeonInventoryPageData> {

    @Override
    public PigeonInventoryPageData decode(PacketBuffer buf) {
        return new PigeonInventoryPageData(buf.readInt());
    }

    @Override
    public void encode(PigeonInventoryPageData data, PacketBuffer buf) {
        buf.writeInt(data.page);
    }

    @Override
    public void handle(PigeonInventoryPageData data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                ServerPlayerEntity player = ctx.get().getSender();
//                Container container = player.containerMenu;
//                if (container instanceof PigeonInventoriesContainer) {
//                    PigeonInventoriesContainer inventories = (PigeonInventoriesContainer) container;
//                    int page = MathHelper.clamp(data.page, 0, Math.max(0, inventories.getTotalNumColumns() - 9));
//
//                    inventories.setPage(page);
//                    inventories.setData(0, page);
//                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
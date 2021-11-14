package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class OpenPigeonScreenPacket implements IHexPacket<OpenPigeonScreenData> {

    @Override
    public OpenPigeonScreenData decode(PacketBuffer buf) {
        return new OpenPigeonScreenData();
    }

    @Override
    public void encode(OpenPigeonScreenData data, PacketBuffer buf) {
    }

    @Override
    public void handle(OpenPigeonScreenData data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                ServerPlayerEntity player = ctx.get().getSender();
                List<PigeonEntity> canis = player.world.getEntitiesWithinAABB(PigeonEntity.class, player.getBoundingBox().grow(12D, 12D, 12D));
//                PigeonScreens.openPigeonInventoriesScreen(player, canis);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
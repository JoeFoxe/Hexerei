package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FriendlyFirePacket extends BirdPacket<FriendlyFireData> {

    @Override
    public void encode(FriendlyFireData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeBoolean(data.friendlyFire);
    }

    @Override
    public FriendlyFireData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        boolean obeyOthers = buf.readBoolean();
        return new FriendlyFireData(entityId, obeyOthers);
    }

    @Override
    public void handleBird(PigeonEntity canisIn, FriendlyFireData data, Supplier<NetworkEvent.Context> ctx) {
        if (!canisIn.canInteract(ctx.get().getSender())) {
            return;
        }
        canisIn.setCanPlayersAttack(data.friendlyFire);
    }
}
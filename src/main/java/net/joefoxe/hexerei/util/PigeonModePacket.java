package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PigeonModePacket extends BirdPacket<PigeonModeData> {

    @Override
    public void encode(PigeonModeData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeInt(data.mode.getIndex());
    }

    @Override
    public PigeonModeData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        int modeIndex = buf.readInt();
        return new PigeonModeData(entityId, EnumMode.byIndex(modeIndex));
    }

    @Override
    public void handleBird(PigeonEntity canisIn, PigeonModeData data, Supplier<NetworkEvent.Context> ctx) {
        if (!canisIn.canInteract(ctx.get().getSender())) {
            return;
        }
        canisIn.setMode(data.mode);
    }
}
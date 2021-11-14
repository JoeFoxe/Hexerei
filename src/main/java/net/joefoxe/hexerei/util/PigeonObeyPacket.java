package net.joefoxe.hexerei.util;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PigeonObeyPacket extends BirdPacket<PigeonObeyData> {

    @Override
    public void encode(PigeonObeyData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeBoolean(data.obeyOthers);
    }

    @Override
    public PigeonObeyData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        boolean obeyOthers = buf.readBoolean();
        return new PigeonObeyData(entityId, obeyOthers);
    }


    @Override
    public void handleBird(PigeonEntity pigeonEntity, PigeonObeyData data, Supplier<NetworkEvent.Context> ctx) {
        if (!pigeonEntity.canInteract(ctx.get().getSender())) {
            return;
        }
        pigeonEntity.setWillObeyOthers(data.obeyOthers);
    }
}
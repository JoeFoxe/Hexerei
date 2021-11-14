package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PigeonNamePacket extends BirdPacket<PigeonNameData> {

    @Override
    public void encode(PigeonNameData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeString(data.name, 64);
    }

    @Override
    public PigeonNameData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        String name = buf.readString(64);
        return new PigeonNameData(entityId, name);
    }

    @Override
    public void handleBird(PigeonEntity pigeonIn, PigeonNameData data, Supplier<NetworkEvent.Context> ctx) {
        if (!pigeonIn.canInteract(ctx.get().getSender())) {
            return;
        }
        if (data.name.isEmpty()) {
            pigeonIn.setCustomName(null);
        }
        else {
            pigeonIn.setCustomName(new StringTextComponent(data.name));
        }
    }
}
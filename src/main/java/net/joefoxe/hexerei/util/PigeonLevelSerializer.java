package net.joefoxe.hexerei.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

public class PigeonLevelSerializer implements IDataSerializer<PigeonLevel> {

    @Override
    public void write(PacketBuffer buf, PigeonLevel value) {
        buf.writeInt(value.getLevel(PigeonLevel.Type.NORMAL));
    }

    @Override
    public PigeonLevel read(PacketBuffer buf) {
        return new PigeonLevel(buf.readInt());
    }

    @Override
    public PigeonLevel copyValue(PigeonLevel value) {
        return value.copy();
    }
}
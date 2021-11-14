package net.joefoxe.hexerei.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CofferLocationsSerializer<D, T extends IDataSerializer<D>> implements IDataSerializer<DimensionDependantArg<D>> {

    @Override
    public void write(PacketBuffer buf, DimensionDependantArg<D> value) {
        IDataSerializer<D> ser = value.getSerializer();
        buf.writeInt(DataSerializers.getSerializerId(ser));
        buf.writeInt(value.size());
        value.entrySet().forEach((entry) -> {
            buf.writeResourceLocation(entry.getKey().getLocation());
            ser.write(buf, entry.getValue());
        });
    }

    @Override
    public DimensionDependantArg<D> read(PacketBuffer buf) {
        IDataSerializer<D> ser = (IDataSerializer<D>) DataSerializers.getSerializer(buf.readInt());
        DimensionDependantArg<D> value = new DimensionDependantArg<>(() -> ser);
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation loc = buf.readResourceLocation();
            RegistryKey<World> type = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, loc);
            D subV = ser.read(buf);
            value.map.put(type, subV);
        }
        return value;
    }
    @Override public DimensionDependantArg<D> copyValue(DimensionDependantArg<D> value) {
        return value.copy();
    }
}
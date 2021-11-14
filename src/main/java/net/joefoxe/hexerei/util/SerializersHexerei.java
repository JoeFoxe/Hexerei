package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.Hexerei;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.Supplier;

public class SerializersHexerei {

    public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, Hexerei.MOD_ID);

    public static final RegistryObject<DataSerializerEntry> COFFER_LOC_SERIALIZER = register2("bird_coffer_location", CofferLocationsSerializer::new);

    public static final RegistryObject<DataSerializerEntry> SKILL_SERIALIZER = register2("skills", SkillListSerializer::new);

    public static final RegistryObject<DataSerializerEntry> PIGEON_LEVEL_SERIALIZER = register2("pigeon_level", PigeonLevelSerializer::new);

    public static final RegistryObject<DataSerializerEntry> MODE_SERIALIZER = register2("mode", ModeSerializer::new);

    private static <X extends IDataSerializer<?>> RegistryObject<DataSerializerEntry> register2(final String name, final Supplier<X> factory) {
        return register(name, () -> new DataSerializerEntry(factory.get()));
    }

    private static RegistryObject<DataSerializerEntry> register(final String name, final Supplier<DataSerializerEntry> sup) {
        return SERIALIZERS.register(name, sup);
    }
}
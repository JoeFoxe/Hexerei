package net.joefoxe.hexerei.world.gen;

import net.joefoxe.hexerei.world.structure.ModStructures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ModStructureGeneration {
    public static void generateStructures(final BiomeLoadingEvent event) {
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        if(types.contains(BiomeDictionary.Type.SWAMP)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();

            structures.add(() -> ModStructures.WITCH_HUT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            structures.add(() -> ModStructures.MANGROVE_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }
    }
}
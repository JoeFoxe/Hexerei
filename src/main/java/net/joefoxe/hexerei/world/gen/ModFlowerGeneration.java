package net.joefoxe.hexerei.world.gen;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ModFlowerGeneration {

    public static void generateFlowers(final BiomeLoadingEvent event) {
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        if(types.contains(BiomeDictionary.Type.SWAMP)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

//            base.add(() -> ModConfiguredFeatures.MANDRAKE_FLOWER_CONFIG);
            base.add(() -> ModConfiguredFeatures.MUGWORT_BUSH_CONFIG);
            base.add(() -> ModConfiguredFeatures.BELLADONNA_FLOWER_CONFIG);
            base.add(() -> ModConfiguredFeatures.YELLOW_DOCK_BUSH_CONFIG);

            if(event.getName().toString().matches("hexerei:willow_swamp") ) {

                base.add(() -> ModConfiguredFeatures.MANDRAKE_FLOWER_CONFIG);
                base.add(() -> ModConfiguredFeatures.MUGWORT_BUSH_CONFIG);
                base.add(() -> ModConfiguredFeatures.BELLADONNA_FLOWER_CONFIG);
                base.add(() -> ModConfiguredFeatures.YELLOW_DOCK_BUSH_CONFIG);
            }
        }
    }
}

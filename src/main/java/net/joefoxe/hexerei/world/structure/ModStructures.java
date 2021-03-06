package net.joefoxe.hexerei.world.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.world.structure.structures.DarkCovenStructure;
import net.joefoxe.hexerei.world.structure.structures.GenericJigsawStructure;
import net.joefoxe.hexerei.world.structure.structures.MangroveTreeStructure;
import net.joefoxe.hexerei.world.structure.structures.WitchHutStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURES =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Hexerei.MOD_ID);
    public static final Set<ResourceLocation> HEXEREI_STRUCTURE_START_PIECES = new HashSet<>();

    public static final RegistryObject<Structure<NoFeatureConfig>> WITCH_HUT =
            STRUCTURES.register("witch_hut", WitchHutStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> MANGROVE_TREE =
            STRUCTURES.register("mangrove_tree", MangroveTreeStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> DARK_COVEN =
            STRUCTURES.register("dark_coven", DarkCovenStructure::new);

//    public static final RegistryObject<Structure<NoFeatureConfig>> DARK_COVEN = addToStructureMaps("dark_coven", () -> (new GenericJigsawStructure.Builder<>(new ResourceLocation(Hexerei.MOD_ID, "coven/dark_coven/town_centers")).setStructureSize(6).setBiomeRange(1).setStructureBlacklistRange(6).build()));

    private static <T extends Structure<?>> RegistryObject<T> addToStructureMaps(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }

    /* average distance apart in chunks between spawn attempts */
    /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/
    /* this modifies the seed of the structure so no two structures always spawn over each-other.
    Make this large and unique. */
    public static void setupStructures() {
        setupMapSpacingAndLand(WITCH_HUT.get(),
                new StructureSeparationSettings(20,8, 1234567890),
                false);
        setupMapSpacingAndLand(MANGROVE_TREE.get(),
                new StructureSeparationSettings(3,1, 1234567890),
                false);
        setupMapSpacingAndLand(DARK_COVEN.get(),
                new StructureSeparationSettings(29,11, 1418987890),
                false);
    }

    /**
     * Adds the provided structure to the registry, and adds the separation settings.
     * The rarity of the structure is determined based on the values passed into
     * this method in the structureSeparationSettings argument.
     * This method is called by setupStructures above.
     **/
    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings,
                                                                       boolean transformSurroundingLand) {
        //add our structures into the map in Structure class
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        /*
         * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
         * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structure that have pieces stacked vertically or change in heights.
         *
         */
        if (transformSurroundingLand) {
            Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.field_236384_t_)
                    .add(structure)
                    .build();
        }

        /*
         * This is the map that holds the default spacing of all structures.
         * Always add your structure to here so that other mods can utilize it if needed.
         *
         * However, while it does propagate the spacing to some correct dimensions from this map,
         * it seems it doesn't always work for code made dimensions as they read from this list beforehand.
         *
         * Instead, we will use the WorldEvent.Load event in ModWorldEvents to add the structure
         * spacing from this list into that dimension or to do dimension blacklisting properly.
         * We also use our entry in DimensionStructuresSettings.DEFAULTS in WorldEvent.Load as well.
         *
         * DEFAULTS requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
         */
        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();

        /*
         * There are very few mods that relies on seeing your structure in the
         * noise settings registry before the world is made.
         *
         * You may see some mods add their spacings to DimensionSettings.BUILTIN_OVERWORLD instead of the
         * NOISE_GENERATOR_SETTINGS loop below but that field only applies for the default overworld and
         * won't add to other worldtypes or dimensions (like amplified or Nether).
         * So yeah, don't do DimensionSettings.BUILTIN_OVERWORLD. Use the NOISE_GENERATOR_SETTINGS loop
         * below instead if you must.
         */
        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap =
                    settings.getValue().getStructures().func_236195_a_();
            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             * I take no chances myself. You never know what another mods does...
             *
             * structureConfig requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
             */
            if (structureMap instanceof ImmutableMap) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().getStructures().func_236195_a_();

            } else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        STRUCTURES.register(eventBus);
    }
}
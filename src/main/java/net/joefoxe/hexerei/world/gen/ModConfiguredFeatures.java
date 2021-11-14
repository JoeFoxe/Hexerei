package net.joefoxe.hexerei.world.gen;

import net.joefoxe.hexerei.block.ModBlocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.OptionalInt;

public class ModConfiguredFeatures {

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> MAHOGANY =
            register("mahogany", Feature.TREE.withConfiguration((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(ModBlocks.MAHOGANY_LOG.get().getDefaultState()),
                            new SimpleBlockStateProvider(ModBlocks.MAHOGANY_LEAVES.get().getDefaultState()),
                            new AcaciaFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(1)),
                            new FancyTrunkPlacer(6, 4, 3),
                            new ThreeLayerFeature(2,2, 1, 0,1, OptionalInt.of(2)))).setIgnoreVines().build()));

    public static final ConfiguredFeature<?, ?> MANDRAKE_FLOWER_CONFIG = Feature.FLOWER.withConfiguration((
                    new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.MANDRAKE_FLOWER.get().getDefaultState()),
                            SimpleBlockPlacer.PLACER)).tries(12).build())
            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(3);


//TwoLayerFeature(int limit, int lowerSize, int upperSize)
//ThreeLayerFeature(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, OptionalInt size)

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key,
                                                                                 ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }
}
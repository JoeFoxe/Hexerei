package net.joefoxe.hexerei.world.gen;

import com.google.common.collect.ImmutableList;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.util.HexereiAbstractTreeFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
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
                            SimpleBlockPlacer.PLACER)).tries(1).build())
            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(1);

    public static final ConfiguredFeature<?, ?> YELLOW_DOCK_BUSH_CONFIG = Feature.RANDOM_PATCH.withConfiguration((
                    new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.YELLOW_DOCK_BUSH.get().getDefaultState()), new DoublePlantBlockPlacer())).tries(1).preventProjection().build())
            .withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7)));

    public static final ConfiguredFeature<?, ?> MUGWORT_BUSH_CONFIG = Feature.RANDOM_PATCH.withConfiguration((
                    new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.MUGWORT_BUSH.get().getDefaultState()), new DoublePlantBlockPlacer())).tries(1).preventProjection().build())
            .withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.FLOWER_TALL_GRASS_PLACEMENT).square().withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 0, 7)));

    public static final ConfiguredFeature<?, ?> BELLADONNA_FLOWER_CONFIG = Feature.FLOWER.withConfiguration((
                    new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.BELLADONNA_FLOWER.get().getDefaultState()),
                            SimpleBlockPlacer.PLACER)).tries(1).build())
            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(3);

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> WILLOW = register("willow_tree", ModFeatures.WILLOW_TREE.get().withConfiguration((
            new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(ModBlocks.MAHOGANY_LOG.get().getDefaultState()),
                    new SimpleBlockStateProvider(ModBlocks.MAHOGANY_LEAVES.get().getDefaultState()),
                    new BlobFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 3),
                    new StraightTrunkPlacer(4, 8, 0),
                    new TwoLayerFeature(1, 0, 1)
            )).setDecorators(ImmutableList.of(new CocoaTreeDecorator(0.2F), TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.field_236871_b_)).setIgnoreVines().build()));


//TwoLayerFeature(int limit, int lowerSize, int upperSize)
//ThreeLayerFeature(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, OptionalInt size)

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key,
                                                                                 ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }


}
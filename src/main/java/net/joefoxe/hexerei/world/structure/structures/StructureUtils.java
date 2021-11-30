package net.joefoxe.hexerei.world.structure.structures;


import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.joefoxe.hexerei.Hexerei;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class StructureUtils {

    // Weighted Random from: https://stackoverflow.com/a/6737362
    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, Random random) {
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += pair.getSecond();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < rlList.size() - 1; ++index) {
            randomWeightPicked -= rlList.get(index).getSecond();
            if (randomWeightPicked <= 0.0) break;
        }

        return rlList.get(index).getFirst();
    }

    //////////////////////////////
    private static final Map<BlockState, Boolean> IS_FULLCUBE_MAP = new HashMap<>();

    public static boolean isFullCube(IWorldReader world, BlockPos pos, BlockState state){
        if(!IS_FULLCUBE_MAP.containsKey(state)){
            boolean isFullCube = Block.isOpaque(state.getCollisionShape(world, pos)) || state.getBlock() instanceof LeavesBlock;
            IS_FULLCUBE_MAP.put(state, isFullCube);
        }
        return IS_FULLCUBE_MAP.get(state);
    }

    //////////////////////////////

    // Helper method to make chests always face away from walls
    public static BlockState orientateChest(IServerWorld blockView, BlockPos blockPos, BlockState blockState) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Direction bestDirection = blockState.get(HorizontalBlock.HORIZONTAL_FACING);

        for(Direction facing : Direction.Plane.HORIZONTAL) {
            mutable.setPos(blockPos).move(facing);

            // Checks if wall is in this side
            if (isFullCube(blockView, mutable, blockView.getBlockState(mutable))) {
                bestDirection = facing;

                // Exit early if facing open space opposite of wall
                mutable.move(facing.getOpposite(), 2);
                if(!blockView.getBlockState(mutable).getMaterial().isSolid()){
                    break;
                }
            }
        }

        // Make chest face away from wall
        return blockState.with(HorizontalBlock.HORIZONTAL_FACING, bestDirection.getOpposite());
    }

    ///////////////////////////////////////////

    /**
     * Will serialize (if possible) both features and check if they are the same feature.
     * If cannot serialize, compare the feature itself to see if it is the same.
     */
    public static boolean serializeAndCompareFeature(ConfiguredFeature<?, ?> configuredFeature1, ConfiguredFeature<?, ?> configuredFeature2) {

        Optional<JsonElement> configuredFeatureJSON1 = ConfiguredFeature.CODEC.encode(configuredFeature1, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();
        Optional<JsonElement> configuredFeatureJSON2 = ConfiguredFeature.CODEC.encode(configuredFeature2, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

        // One of the configuredfeatures cannot be serialized
        if(!configuredFeatureJSON1.isPresent() || !configuredFeatureJSON2.isPresent()) {
            return false;
        }

        // Compare the JSON to see if it's the same ConfiguredFeature in the end.
        return configuredFeatureJSON1.equals(configuredFeatureJSON2);
    }

    //////////////////////////////////////////////

//    public static boolean isBlacklistedForWorld(ISeedReader currentWorld, ResourceLocation worldgenObjectID){
//        ResourceLocation worldID = currentWorld.getWorld().getDimensionKey().getLocation();
//
//        // Apply disallow first. (Default behavior is it adds to all dimensions)
//        boolean allowInDim = BiomeDimensionAllowDisallow.DIMENSION_DISALLOW.getOrDefault(worldgenObjectID, new ArrayList<>())
//                .stream().noneMatch(pattern -> pattern.matcher(worldID.toString()).find());
//
//        // Apply allow to override disallow if dimension is targeted in both.
//        // Lets disallow to turn off spawn for a group of dimensions while allow can turn it back one for one of them.
//        if(!allowInDim && BiomeDimensionAllowDisallow.DIMENSION_ALLOW.getOrDefault(worldgenObjectID, new ArrayList<>())
//                .stream().anyMatch(pattern -> pattern.matcher(worldID.toString()).find())){
//            allowInDim = true;
//        }
//
//        return !allowInDim;
//    }

    //////////////////////////////

//    public static BlockPos getHighestLand(ChunkGenerator chunkGenerator, MutableBoundingBox boundingBox, boolean canBeOnLiquid) {
//        BlockPos.Mutable mutable = new BlockPos.Mutable().setPos(
//                boundingBox.func_215126_f().getX(),
//                chunkGenerator.getMaxBuildHeight() - 20,
//                boundingBox.func_215126_f().getZ());
//
//        IBlockReader blockView = chunkGenerator.func_230348_a_(mutable.getX(), mutable.getZ());
//        BlockState currentBlockstate;
//        while (mutable.getY() > chunkGenerator.getSeaLevel()) {
//            currentBlockstate = blockView.getBlockState(mutable);
//            if (!currentBlockstate.canOcclude()) {
//                mutable.move(Direction.DOWN);
//                continue;
//            }
//            else if (blockView.getBlockState(mutable.offset(0, 3, 0)).getMaterial() == Material.AIR && (canBeOnLiquid ? !currentBlockstate.isAir() : currentBlockstate.canOcclude())) {
//                return mutable;
//            }
//            mutable.move(Direction.DOWN);
//        }
//
//        return mutable;
//    }
//
//
//    public static BlockPos getLowestLand(ChunkGenerator chunkGenerator, MutableBoundingBox boundingBox, boolean canBeOnLiquid){
//        BlockPos.Mutable mutable = new BlockPos.Mutable().set(
//                boundingBox.func_215126_f().getX(),
//                chunkGenerator.getSeaLevel() + 1,
//                boundingBox.func_215126_f().getZ());
//
//        IBlockReader blockView = chunkGenerator.func_230348_a_(mutable.getX(), mutable.getZ());
//        BlockState currentBlockstate = blockView.getBlockState(mutable);
//        while (mutable.getY() <= chunkGenerator.getGroundHeight() - 20) {
//
//            if((canBeOnLiquid ? !currentBlockstate.isAir() : currentBlockstate.canOcclude()) &&
//                    blockView.getBlockState(mutable.above()).getMaterial() == Material.AIR &&
//                    blockView.getBlockState(mutable.above(5)).getMaterial() == Material.AIR)
//            {
//                mutable.move(Direction.UP);
//                return mutable;
//            }
//
//            mutable.move(Direction.UP);
//            currentBlockstate = blockView.getBlockState(mutable);
//        }
//
//        return mutable.set(mutable.getX(), chunkGenerator.getSeaLevel(), mutable.getZ());
//    }

    //////////////////////////////////////////////

    public static int getFirstLandYFromPos(IWorldReader worldView, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.setPos(pos);
        IChunk currentChunk = worldView.getChunk(mutable);
        BlockState currentState = currentChunk.getBlockState(mutable);

        while(mutable.getY() >= 0 && isReplaceableByStructures(currentState)) {
            mutable.move(Direction.DOWN);
            currentState = currentChunk.getBlockState(mutable);
        }

        return mutable.getY();
    }

    private static boolean isReplaceableByStructures(BlockState blockState) {
        return blockState.isAir() || blockState.getMaterial().isLiquid() || blockState.getMaterial().isReplaceable();
    }

    //////////////////////////////////////////////

    public static void centerAllPieces(BlockPos targetPos, List<StructurePiece> pieces){
        if(pieces.isEmpty()) return;

        Vector3i structureCenter = pieces.get(0).getBoundingBox().func_215126_f();
        int xOffset = targetPos.getX() - structureCenter.getX();
        int zOffset = targetPos.getZ() - structureCenter.getZ();

        for(StructurePiece structurePiece : pieces){
            structurePiece.offset(xOffset, 0, zOffset);
        }
    }
}
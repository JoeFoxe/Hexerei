package net.joefoxe.hexerei.world.structure.structures;

import net.joefoxe.hexerei.Hexerei;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import static net.minecraft.world.gen.GenerationStage.*;

public class WitchHutStructure extends Structure<NoFeatureConfig> {
    public WitchHutStructure() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public Decoration getDecorationStage() {
        return Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeSource,
                                     long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ,
                                     Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos centerOfChunk = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);
        int landHeight = chunkGenerator.getHeight(centerOfChunk.getX(), centerOfChunk.getZ(),
                Heightmap.Type.WORLD_SURFACE_WG);

        IBlockReader columnOfBlocks = chunkGenerator.func_230348_a_(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.up(landHeight));


//        for(int i = 2; i <= 7; i += 5) {
//            for(int j = 1; j <= 5; j += 4) {
//                this.replaceAirAndLiquidDownwards(p_230383_1_, Blocks.OAK_LOG.getDefaultState(), j, -1, i, p_230383_5_);
//            }
//        }


        return topBlock.getFluidState().isEmpty();
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return WitchHutStructure.Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ,
                     MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override // generatePieces
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator,
                                   TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn,
                                   NoFeatureConfig config) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos blockpos = new BlockPos(x, 0, z);

            //addpieces()
            JigsawManager.func_242837_a(dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                            .getOrDefault(new ResourceLocation(Hexerei.MOD_ID, "witch_hut/start_pool")),
                            10), AbstractVillagePiece::new, chunkGenerator, templateManagerIn,
                    blockpos, this.components, this.rand,false,true);

            this.components.forEach(piece -> piece.offset(0, 2, 0));
            this.components.forEach(piece -> piece.getBoundingBox().minY -= 1);

            WitchHutPiece witchhutpiece = new WitchHutPiece(this.rand, chunkX * 16, chunkZ * 16);
            this.components.add(witchhutpiece);


            this.recalculateStructureSize();

            LogManager.getLogger().log(Level.DEBUG, "WitchHut at " +
                    this.components.get(0).getBoundingBox().minX + " " +
                    this.components.get(0).getBoundingBox().minY + " " +
                    this.components.get(0).getBoundingBox().minZ);
        }
    }

//    protected void replaceAirAndLiquidDownwards(ISeedReader worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingboxIn) {
//        int i = this.getXWithOffset(x, z);
//        int j = this.getYWithOffset(y);
//        int k = this.getZWithOffset(x, z);
//        if (boundingboxIn.isVecInside(new BlockPos(i, j, k))) {
//            while((worldIn.isAirBlock(new BlockPos(i, j, k)) || worldIn.getBlockState(new BlockPos(i, j, k)).getMaterial().isLiquid()) && j > 1) {
//                worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
//                --j;
//            }
//
//        }
//    }

}
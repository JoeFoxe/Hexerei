package net.joefoxe.hexerei.world.structure.structures;

import com.mojang.serialization.Codec;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.*;
import org.lwjgl.system.CallbackI;

import java.util.*;

public class HexereiAbstractTreeFeature extends Feature<BaseTreeFeatureConfig>{

    private static final ResourceLocation WILLOW_TREE1 = new ResourceLocation("hexerei:willow_tree1");
    private static final ResourceLocation WILLOW_TREE2 = new ResourceLocation("hexerei:willow_tree2");
    private static final ResourceLocation WILLOW_TREE3 = new ResourceLocation("hexerei:willow_tree3");
    private static final ResourceLocation[] WILLOW_TREE = new ResourceLocation[]{WILLOW_TREE1, WILLOW_TREE2, WILLOW_TREE3};

    public HexereiAbstractTreeFeature(Codec codec) {
        super(codec);
    }

    public static boolean isAirOrLeavesAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES);
        });
    }

    public static boolean isAirOrLeavesOrLogsAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.LOGS);
        });
    }

    private static boolean isDirtOrFarmlandAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) || block == Blocks.FARMLAND;
        });
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BaseTreeFeatureConfig config) {

        int i = rand.nextInt(WILLOW_TREE.length);

        if (!isDirtOrFarmlandAt(reader, pos.down()))
            return false;

        for(int j = 0; j < 8; j++) {

            BlockPos upPos = new BlockPos(pos).up();
            for(int k = 0; k < j; k++)
            {
                upPos = upPos.up();
            }

            if (!isAirOrLeavesOrLogsAt(reader, upPos)) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.north())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.south())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.east())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.east().north())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.east().south())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.west())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.west().north())) {
                return false;
            }
            if (!isAirOrLeavesOrLogsAt(reader, upPos.west().south())) {
                return false;
            }
        }


        if (isAirOrLeavesOrLogsAt(reader, pos.down().north()))
            return false;
        if (isAirOrLeavesOrLogsAt(reader, pos.down().south()))
            return false;
        if (isAirOrLeavesOrLogsAt(reader, pos.down().east()))
            return false;
        if (isAirOrLeavesOrLogsAt(reader, pos.down().west()))
            return false;

        IntegrityProcessor integrityprocessor = new IntegrityProcessor(0.9F);

        TemplateManager templatemanager = reader.getWorld().getServer().getTemplateManager();
        Template template = templatemanager.getTemplateDefaulted(WILLOW_TREE[i]);

        if (template == null) {
            Hexerei.LOGGER.error("Identifier to the specified nbt file was not found! : {}", WILLOW_TREE[i]);
            return false;
        }
        Rotation rotation = Rotation.randomRotation(rand);

        // For proper offsetting the feature so it rotate properly around position parameter.
        BlockPos halfLengths = new BlockPos(
                template.getSize().getX() / 2,
                template.getSize().getY() / 2,
                template.getSize().getZ() / 2);

        BlockPos.Mutable mutable = new BlockPos.Mutable().setPos(pos);

        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setCenterOffset(halfLengths).setIgnoreEntities(false);
        Optional<StructureProcessorList> processor = reader.getWorld().getServer().getDynamicRegistries().getRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOptional(
                new ResourceLocation(Hexerei.MOD_ID, "mangrove_tree/mangrove_tree_legs"));
        processor.orElse(ProcessorLists.EMPTY).func_242919_a().forEach(placementsettings::addProcessor); // add all processors

        template.func_237152_b_(reader, mutable.setPos(pos).move(-halfLengths.getX(), 0, -halfLengths.getZ()), placementsettings, rand);

        return true;
    }

}
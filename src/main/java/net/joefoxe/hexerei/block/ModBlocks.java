package net.joefoxe.hexerei.block;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.custom.*;
import net.joefoxe.hexerei.block.custom.trees.MahoganyTree;
import net.joefoxe.hexerei.item.ModItemGroup;
import net.joefoxe.hexerei.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, Hexerei.MOD_ID);

    public static final RegistryObject<Block> SCRAP_BLOCK = registerBlock("scrap_block",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5f)));

    public static final RegistryObject<Block> MIXING_CAULDRON = registerBlock("mixing_cauldron",
            () -> new MixingCauldron(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f).setLightLevel(state -> 6 + 3 * state.get(MixingCauldron.LEVEL))));

    public static final RegistryObject<Block> PESTLE_AND_MORTAR = registerBlock("pestle_and_mortar",
            () -> new PestleAndMortar(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> CRYSTAL_BALL = registerBlock("crystal_ball",
            () -> new CrystalBall(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f).setLightLevel(state -> 9)));

    public static final RegistryObject<Block> HERB_JAR = registerBlock("herb_jar",
            () -> new HerbJar(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> HERB_DRYING_RACK_FULL = registerBlock("herb_drying_rack_full",
            () -> new HerbDryingRack(AbstractBlock.Properties.create(Material.WOOD).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> CANDELABRA = registerBlock("candelabra",
            () -> new Candelabra(AbstractBlock.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2f).setLightLevel(state -> state.get(Candelabra.LIT) ? 15 : 0)));

    public static final RegistryObject<Block> CANDLE = registerBlock("candle",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f).setLightLevel(state -> Math.min(state.get(Candle.CANDLES_LIT) * 12, 15))));

    public static final RegistryObject<Block> CANDLE_BLUE = registerBlock("candle_blue",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_BLACK = registerBlock("candle_black",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_LIME = registerBlock("candle_lime",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_ORANGE = registerBlock("candle_orange",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_PINK = registerBlock("candle_pink",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_PURPLE = registerBlock("candle_purple",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_RED = registerBlock("candle_red",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_TEAL = registerBlock("candle_teal",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_YELLOW = registerBlock("candle_yellow",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().notSolid().harvestLevel(0).hardnessAndResistance(1f)));



    public static final RegistryObject<Block> MAHOGANY_LOG = registerBlock("mahogany_log",
            () -> new MahoganyLog(AbstractBlock.Properties.from(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> MAHOGANY_WOOD = registerBlock("mahogany_wood",
            () -> new MahoganyWood(AbstractBlock.Properties.from(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_MAHOGANY_LOG = registerBlock("stripped_mahogany_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_LOG)));

    public static final RegistryObject<Block> STRIPPED_MAHOGANY_WOOD = registerBlock("stripped_mahogany_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> MAHOGANY_PLANKS = registerBlock("mahogany_planks",
            () -> new Block(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MAHOGANY_STAIRS = registerBlock("mahogany_stairs",
            () -> new StairsBlock(() -> MAHOGANY_PLANKS.get().getDefaultState(),
                    AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MAHOGANY_FENCE = registerBlock("mahogany_fence",
            () -> new FenceBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MAHOGANY_FENCE_GATE = registerBlock("mahogany_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MAHOGANY_SLAB = registerBlock("mahogany_slab",
            () -> new SlabBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MAHOGANY_BUTTON = registerBlock("mahogany_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS).doesNotBlockMovement()));

    public static final RegistryObject<Block> MAHOGANY_PRESSURE_PLATE = registerBlock("mahogany_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING ,AbstractBlock.Properties.from(Blocks.OAK_PLANKS).doesNotBlockMovement()));

    public static final RegistryObject<Block> MAHOGANY_DOOR = registerBlock("mahogany_door",
            () -> new DoorBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS).notSolid()));

    public static final RegistryObject<Block> MAHOGANY_TRAPDOOR = registerBlock("mahogany_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS).notSolid()));

    public static final RegistryObject<Block> MAHOGANY_LEAVES = registerBlock("mahogany_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.from(Blocks.OAK_LEAVES).tickRandomly().sound(SoundType.PLANT).notSolid().setSuffocates(Properties::never).setBlocksVision(Properties::never)));

    public static final RegistryObject<Block> MAHOGANY_SAPLING = registerBlock("mahogany_sapling",
            () -> new SaplingBlock(new MahoganyTree(), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));




    public static final RegistryObject<Block> WILLOW_LOG = registerBlock("willow_log",
            () -> new WillowLog(AbstractBlock.Properties.from(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> WILLOW_WOOD = registerBlock("willow_wood",
            () -> new WillowWood(AbstractBlock.Properties.from(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_WILLOW_LOG = registerBlock("stripped_willow_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_LOG)));

    public static final RegistryObject<Block> STRIPPED_WILLOW_WOOD = registerBlock("stripped_willow_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> WILLOW_PLANKS = registerBlock("willow_planks",
            () -> new Block(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> WILLOW_STAIRS = registerBlock("willow_stairs",
            () -> new StairsBlock(() -> WILLOW_PLANKS.get().getDefaultState(),
                    AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> WILLOW_FENCE = registerBlock("willow_fence",
            () -> new FenceBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> WILLOW_FENCE_GATE = registerBlock("willow_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> WILLOW_SLAB = registerBlock("willow_slab",
            () -> new SlabBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> WILLOW_BUTTON = registerBlock("willow_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS).doesNotBlockMovement()));

    public static final RegistryObject<Block> WILLOW_PRESSURE_PLATE = registerBlock("willow_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING ,AbstractBlock.Properties.from(Blocks.OAK_PLANKS).doesNotBlockMovement()));

    public static final RegistryObject<Block> WILLOW_DOOR = registerBlock("willow_door",
            () -> new DoorBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS).notSolid()));

    public static final RegistryObject<Block> WILLOW_TRAPDOOR = registerBlock("willow_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS).notSolid()));

    public static final RegistryObject<Block> WILLOW_LEAVES = registerBlock("willow_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.from(Blocks.OAK_LEAVES).tickRandomly().sound(SoundType.PLANT).notSolid().setSuffocates(Properties::never).setBlocksVision(Properties::never)));

//    public static final RegistryObject<Block> WILLOW_SAPLING = registerBlock("willow_sapling",
//            () -> new SaplingBlock(new WillowTree(), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));




    public static final RegistryObject<Block> MANDRAKE_FLOWER = registerBlock("mandrake_flower",
            () -> new FlowerBlock(Effects.REGENERATION, 3, AbstractBlock.Properties.from(Blocks.DANDELION)));

    public static final RegistryObject<Block> BELLADONNA_FLOWER = registerBlock("belladonna_flower",
            () -> new FlowerBlock(Effects.POISON, 3, AbstractBlock.Properties.from(Blocks.DANDELION)));

    public static final RegistryObject<Block> MUGWORT_BUSH = registerBlock("mugwort_bush",
            () -> new TallFlowerBlock(AbstractBlock.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().zeroHardnessAndResistance().sound(SoundType.PLANT)));

    public static final RegistryObject<Block> YELLOW_DOCK_BUSH = registerBlock("yellow_dock_bush",
            () -> new TallFlowerBlock(AbstractBlock.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().zeroHardnessAndResistance().sound(SoundType.PLANT)));


    public static final RegistryObject<Block> COFFER = registerBlock("coffer",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> BOOK_OF_SHADOWS_ALTAR = registerBlock("book_of_shadows_altar",
            () -> new Altar(AbstractBlock.Properties.create(Material.WOOD).harvestTool(ToolType.AXE).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> SAGE = BLOCKS.register("sage_crop",
            () -> new SageBlock(AbstractBlock.Properties.from(Blocks.WHEAT)));

    // SIGILS
    public static final RegistryObject<Block> BLOOD_SIGIL = registerBlockNoItem("blood_sigil",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5f)));

    // BLOCK PARTIALS

    public static final RegistryObject<Block> CANDLE_0_OF_7 = registerBlockNoItem("candle_0_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_1_OF_7 = registerBlockNoItem("candle_1_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_2_OF_7 = registerBlockNoItem("candle_2_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_3_OF_7 = registerBlockNoItem("candle_3_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_4_OF_7 = registerBlockNoItem("candle_4_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_5_OF_7 = registerBlockNoItem("candle_5_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_6_OF_7 = registerBlockNoItem("candle_6_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CANDLE_7_OF_7 = registerBlockNoItem("candle_7_of_7",
            () -> new Candle(AbstractBlock.Properties.create(Material.PLANTS).harvestLevel(0).doesNotBlockMovement().hardnessAndResistance(1f)));

    public static final RegistryObject<Block> CRYSTAL_BALL_ORB = registerBlockNoItem("crystal_ball_orb",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> CRYSTAL_BALL_LARGE_RING = registerBlockNoItem("crystal_ball_large_ring",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> CRYSTAL_BALL_SMALL_RING = registerBlockNoItem("crystal_ball_small_ring",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> CRYSTAL_BALL_STAND = registerBlockNoItem("crystal_ball_stand",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> BOOK_OF_SHADOWS_COVER = registerBlockNoItem("book_of_shadows_cover",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> BOOK_OF_SHADOWS_BACK = registerBlockNoItem("book_of_shadows_back",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> BOOK_OF_SHADOWS_BINDING = registerBlockNoItem("book_of_shadows_binding",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> BOOK_OF_SHADOWS_PAGE = registerBlockNoItem("book_of_shadows_page_blank",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> COFFER_LID = registerBlockNoItem("coffer_lid",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> COFFER_CONTAINER = registerBlockNoItem("coffer_container",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> COFFER_HINGE = registerBlockNoItem("coffer_hinge",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> HERB_JAR_GENERIC = registerBlockNoItem("herb_jar_generic",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> HERB_JAR_BELLADONNA = registerBlockNoItem("herb_jar_belladonna",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> HERB_JAR_MANDRAKE_FLOWER = registerBlockNoItem("herb_jar_mandrake_flower",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> HERB_JAR_MANDRAKE_ROOT = registerBlockNoItem("herb_jar_mandrake_root",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> HERB_JAR_MUGWORT = registerBlockNoItem("herb_jar_mugwort",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> HERB_JAR_YELLOW_DOCK = registerBlockNoItem("herb_jar_yellow_dock",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.HEXEREI_GROUP)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static class Properties {

        public static boolean always(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
            return true;
        }

        public static boolean hasPostProcess(BlockState state, IBlockReader reader, BlockPos pos) {
            return true;
        }

        public static boolean never(BlockState state, IBlockReader reader, BlockPos pos) {
            return false;
        }
    }

}

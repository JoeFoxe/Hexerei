package net.joefoxe.hexerei.block;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.custom.*;
import net.joefoxe.hexerei.item.ModItemGroup;
import net.joefoxe.hexerei.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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

    public static final RegistryObject<Block> HERB_DRYING_RACK_FULL = registerBlock("herb_drying_rack_full",
            () -> new HerbDryingRack(AbstractBlock.Properties.create(Material.WOOD).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> CANDELABRA = registerBlock("candelabra",
            () -> new Candelabra(AbstractBlock.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> BOOK_OF_SHADOWS_ALTAR = registerBlock("book_of_shadows_altar",
            () -> new Altar(AbstractBlock.Properties.create(Material.WOOD).harvestTool(ToolType.AXE).harvestLevel(0).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> SAGE = BLOCKS.register("sage_crop",
            () -> new SageBlock(AbstractBlock.Properties.from(Blocks.WHEAT)));

    // SIGILS
    public static final RegistryObject<Block> BLOOD_SIGIL = registerBlockNoItem("blood_sigil",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5f)));


    // COFFER
    public static final RegistryObject<Block> COFFER = registerBlock("coffer",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> COFFER_LID = registerBlockNoItem("coffer_lid",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> COFFER_CONTAINER = registerBlockNoItem("coffer_container",
            () -> new Coffer(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(8f)));

    public static final RegistryObject<Block> COFFER_HINGE = registerBlockNoItem("coffer_hinge",
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


}

package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Hexerei.MOD_ID);

    public static RegistryObject<TileEntityType<MixingCauldronTile>> MIXING_CAULDRON_TILE = TILE_ENTITIES.register("mixing_cauldron_entity", () -> TileEntityType.Builder.create(MixingCauldronTile::new, ModBlocks.MIXING_CAULDRON.get()).build(null));

    public static RegistryObject<TileEntityType<CofferTile>> COFFER_TILE = TILE_ENTITIES.register("coffer_entity", () -> TileEntityType.Builder.create(CofferTile::new, ModBlocks.COFFER.get()).build(null));

    public static RegistryObject<TileEntityType<HerbJarTile>> HERB_JAR_TILE = TILE_ENTITIES.register("herb_jar_entity", () -> TileEntityType.Builder.create(HerbJarTile::new, ModBlocks.HERB_JAR.get()).build(null));

    public static RegistryObject<TileEntityType<CrystalBallTile>> CRYSTAL_BALL_TILE = TILE_ENTITIES.register("crystal_ball_entity", () -> TileEntityType.Builder.create(CrystalBallTile::new, ModBlocks.CRYSTAL_BALL.get()).build(null));

    public static RegistryObject<TileEntityType<BookOfShadowsAltarTile>> BOOK_OF_SHADOWS_ALTAR_TILE = TILE_ENTITIES.register("book_of_shadows_altar_entity", () -> TileEntityType.Builder.create(BookOfShadowsAltarTile::new, ModBlocks.BOOK_OF_SHADOWS_ALTAR.get()).build(null));

    public static RegistryObject<TileEntityType<CandleTile>> CANDLE_TILE = TILE_ENTITIES.register("candle_entity", () -> TileEntityType.Builder.create(CandleTile::new, ModBlocks.CANDLE.get(), ModBlocks.CANDLE_BLUE.get(), ModBlocks.CANDLE_BLACK.get(), ModBlocks.CANDLE_LIME.get(), ModBlocks.CANDLE_ORANGE.get(), ModBlocks.CANDLE_PINK.get(), ModBlocks.CANDLE_PURPLE.get(), ModBlocks.CANDLE_RED.get(), ModBlocks.CANDLE_TEAL.get(), ModBlocks.CANDLE_YELLOW.get()).build(null));


    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}

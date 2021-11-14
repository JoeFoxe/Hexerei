package net.joefoxe.hexerei;

import com.google.common.collect.ImmutableMap;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.client.renderer.color.ModBlockColors;
import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.FoodHandler;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.PigeonReviveCommand;
import net.joefoxe.hexerei.client.renderer.entity.render.BroomRenderer;
import net.joefoxe.hexerei.client.renderer.entity.render.BuffZombieRenderer;
import net.joefoxe.hexerei.client.renderer.entity.render.PigeonRenderer;
import net.joefoxe.hexerei.container.CofferContainer;
import net.joefoxe.hexerei.container.ModContainers;
import net.joefoxe.hexerei.data.recipes.ModRecipeTypes;
import net.joefoxe.hexerei.fluid.ModFluids;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.particle.ModParticleTypes;
import net.joefoxe.hexerei.screen.CofferScreen;
import net.joefoxe.hexerei.screen.HerbJarScreen;
import net.joefoxe.hexerei.screen.MixingCauldronScreen;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.joefoxe.hexerei.tileentity.renderer.*;
import net.joefoxe.hexerei.util.*;
import net.joefoxe.hexerei.world.processor.MangroveTreeLegProcessor;
import net.joefoxe.hexerei.world.processor.WitchHutLegProcessor;
import net.joefoxe.hexerei.world.structure.ModStructures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.AxeItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Hexerei.MOD_ID)
public class Hexerei
{

    public static final String MOD_ID = "hexerei";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(HexereiConstants.CHANNEL_NAME)
            .clientAcceptedVersions(HexereiConstants.PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(HexereiConstants.PROTOCOL_VERSION::equals)
            .networkProtocolVersion(HexereiConstants.PROTOCOL_VERSION::toString)
            .simpleChannel();

    public static IStructureProcessorType<WitchHutLegProcessor> WITCH_HUT_LEG_PROCESSOR = () -> WitchHutLegProcessor.CODEC;
    public static IStructureProcessorType<MangroveTreeLegProcessor> MANGROVE_TREE_LEG_PROCESSOR = () -> MangroveTreeLegProcessor.CODEC;

    public Hexerei() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::gatherData);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModFluids.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModRecipeTypes.register(eventBus);
        ModParticleTypes.PARTICLES.register(eventBus);
        ModStructures.register(eventBus);

        SerializersHexerei.SERIALIZERS.register(eventBus);
        PigeonSkills.SKILLS.register(eventBus);
        PigeonAttributes.ATTRIBUTES.register(eventBus);
        eventBus.addListener(PigeonRegistries::newRegistry);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.addListener(this::onServerStarting);
        forgeEventBus.addListener(this::registerCommands);

        ModEntityTypes.register(eventBus);

        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
//        LOGGER.info("HELLO FROM PREINIT");
//        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        PigeonPacketHandler.init();
        FoodHandler.registerHandler(new SeedFoodHandler());
        PigeonReviveCommand.registerSerializers();
        PigeonEntity.initDataParameters();
        ModEntityTypes.addEntityAttributes();

        event.enqueueWork(() -> {
            AxeItem.BLOCK_STRIPPING_MAP = new ImmutableMap.Builder<Block, Block>().putAll(AxeItem.BLOCK_STRIPPING_MAP)
                    .put(ModBlocks.MAHOGANY_LOG.get(), ModBlocks.STRIPPED_MAHOGANY_LOG.get())
                    .put(ModBlocks.MAHOGANY_WOOD.get(), ModBlocks.STRIPPED_MAHOGANY_WOOD.get())
                    .put(ModBlocks.WILLOW_LOG.get(), ModBlocks.STRIPPED_WILLOW_LOG.get())
                    .put(ModBlocks.WILLOW_WOOD.get(), ModBlocks.STRIPPED_WILLOW_WOOD.get()).build();
            ModStructures.setupStructures();

            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, "witch_hut_leg_processor"), WITCH_HUT_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, "mangrove_tree_leg_processor"), MANGROVE_TREE_LEG_PROCESSOR);


        });
    }

    public void registerCommands(final RegisterCommandsEvent event) {
        PigeonReviveCommand.register(event.getDispatcher());
    }

    private void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModFluids.QUICKSILVER_FLUID.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModFluids.QUICKSILVER_FLOWING.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModFluids.QUICKSILVER_BLOCK.get(), RenderType.getTranslucent());

            RenderTypeLookup.setRenderLayer(ModFluids.BLOOD_FLUID.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModFluids.BLOOD_FLOWING.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModFluids.BLOOD_BLOCK.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.MAHOGANY_DOOR.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.MAHOGANY_TRAPDOOR.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.MAHOGANY_TRAPDOOR.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.WILLOW_DOOR.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.WILLOW_TRAPDOOR.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.WILLOW_TRAPDOOR.get(), RenderType.getTranslucent());

            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BALL.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BALL_ORB.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BALL_LARGE_RING.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BALL_SMALL_RING.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.HERB_JAR.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.HERB_DRYING_RACK_FULL.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MAHOGANY_SAPLING.get(), RenderType.getCutout());
//            RenderTypeLookup.setRenderLayer(ModBlocks.WILLOW_SAPLING.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANDRAKE_FLOWER.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.BELLADONNA_FLOWER.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MUGWORT_BUSH.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.YELLOW_DOCK_BUSH.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.CANDELABRA.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.SAGE.get(), RenderType.getCutout());

            ScreenManager.registerFactory(ModContainers.MIXING_CAULDRON_CONTAINER.get(), MixingCauldronScreen::new);
            ScreenManager.registerFactory(ModContainers.COFFER_CONTAINER.get(), CofferScreen::new);
            ScreenManager.registerFactory(ModContainers.HERB_JAR_CONTAINER.get(), HerbJarScreen::new);

            ClientRegistry.bindTileEntityRenderer(ModTileEntities.MIXING_CAULDRON_TILE.get(), MixingCauldronRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.COFFER_TILE.get(), CofferRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.HERB_JAR_TILE.get(), HerbJarRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.CRYSTAL_BALL_TILE.get(), CrystalBallRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.BOOK_OF_SHADOWS_ALTAR_TILE.get(), BookOfShadowsAltarRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.CANDLE_TILE.get(), CandleRenderer::new);



            HexereiPacketHandler.register();
        });

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BROOM.get(), BroomRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BUFF_ZOMBIE.get(), BuffZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGEON.get(), PigeonRenderer::new);

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("hexerei", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public static String sId(String name) {
        return MOD_ID + ":" + name;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation("hexerei", path);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}

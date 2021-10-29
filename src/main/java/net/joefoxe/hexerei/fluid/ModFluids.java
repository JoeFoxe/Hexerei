package net.joefoxe.hexerei.fluid;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {

    public static final ResourceLocation QUICKSILVER_STILL_RL = new ResourceLocation(Hexerei.MOD_ID + ":block/quicksilver_still");
    public static final ResourceLocation QUICKSILVER_FLOWING_RL = new ResourceLocation(Hexerei.MOD_ID + ":block/quicksilver_flow");
    public static final ResourceLocation QUICKSILVER_OVERLAY_RL = new ResourceLocation(Hexerei.MOD_ID + ":block/quicksilver_overlay");
    public static final ResourceLocation BLOOD_STILL_RL = new ResourceLocation(Hexerei.MOD_ID + ":block/blood_still");
    public static final ResourceLocation BLOOD_FLOWING_RL = new ResourceLocation(Hexerei.MOD_ID + ":block/blood_flow");
    public static final ResourceLocation BLOOD_OVERLAY_RL = new ResourceLocation(Hexerei.MOD_ID + ":block/blood_overlay");
    public static final Material BLOOD = (new Material.Builder(MaterialColor.WATER)).doesNotBlockMovement().notSolid().replaceable().liquid().build();


    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Hexerei.MOD_ID);

    public static final RegistryObject<FlowingFluid> QUICKSILVER_FLUID = FLUIDS.register("quicksilver_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.QUICKSILVER_PROPERTIES));

    public static final RegistryObject<FlowingFluid> QUICKSILVER_FLOWING = FLUIDS.register("quicksilver_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluids.QUICKSILVER_PROPERTIES));

    public static final ForgeFlowingFluid.Properties QUICKSILVER_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> QUICKSILVER_FLUID.get(), () -> QUICKSILVER_FLOWING.get(), FluidAttributes.builder(QUICKSILVER_STILL_RL, QUICKSILVER_FLOWING_RL).density(15).luminosity(15).viscosity(15).sound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA).overlay(QUICKSILVER_OVERLAY_RL).color(0xF9FFFFFF).gaseous()).slopeFindDistance(2).levelDecreasePerBlock(2).block(() -> ModFluids.QUICKSILVER_BLOCK.get()).bucket(() -> ModItems.QUICKSILVER_BUCKET.get());

    public static final RegistryObject<FlowingFluidBlock> QUICKSILVER_BLOCK = ModBlocks.BLOCKS.register("quicksilver", () -> new FlowingFluidBlock(() -> ModFluids.QUICKSILVER_FLUID.get(),
            AbstractBlock.Properties.create(Material.LAVA).doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));


    public static final RegistryObject<BloodFluid.Source> BLOOD_FLUID = FLUIDS.register("blood_fluid", () -> new BloodFluid.Source(ModFluids.BLOOD_PROPERTIES));

    public static final RegistryObject<BloodFluid.Flowing> BLOOD_FLOWING = FLUIDS.register("blood_flowing", () -> new BloodFluid.Flowing(ModFluids.BLOOD_PROPERTIES));

    public static final BloodFluid.Properties BLOOD_PROPERTIES = new BloodFluid.Properties(
            () -> BLOOD_FLUID.get(), () -> BLOOD_FLOWING.get(), FluidAttributes.builder(BLOOD_STILL_RL, BLOOD_FLOWING_RL)
            .density(1500)
            .luminosity(15)
            .viscosity(2000)
            .sound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK)
            .overlay(BLOOD_OVERLAY_RL)
            .color(0xF9FFFFFF)
            .gaseous())
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2)
            .block(() -> ModFluids.BLOOD_BLOCK.get())
            .bucket(() -> ModItems.BLOOD_BUCKET.get());



    public static final RegistryObject<FlowingFluidBlock> BLOOD_BLOCK = ModBlocks.BLOCKS.register("blood", () -> new FlowingFluidBlock(() -> ModFluids.BLOOD_FLUID.get(),
            AbstractBlock.Properties.create(BLOOD).doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }




}



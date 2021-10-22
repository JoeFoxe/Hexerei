package net.joefoxe.armormod.fluid;

import net.joefoxe.armormod.ArmorMod;
import net.joefoxe.armormod.block.ModBlocks;
import net.joefoxe.armormod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
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

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation(ArmorMod.MOD_ID + ":block/quicksilver_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation(ArmorMod.MOD_ID + ":block/quicksilver_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation(ArmorMod.MOD_ID + ":block/quicksilver_overlay");

    
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ArmorMod.MOD_ID);

    public static final RegistryObject<FlowingFluid> QUICKSILVER_FLUID = FLUIDS.register("quicksilver_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.QUICKSILVER_PROPERTIES));

    public static final RegistryObject<FlowingFluid> QUICKSILVER_FLOWING = FLUIDS.register("quicksilver_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluids.QUICKSILVER_PROPERTIES));

    public static final ForgeFlowingFluid.Properties QUICKSILVER_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> QUICKSILVER_FLUID.get(), () -> QUICKSILVER_FLOWING.get(), FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL).density(15).luminosity(15).viscosity(15).sound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA).overlay(WATER_OVERLAY_RL).color(0xF9FFFFFF).gaseous()).slopeFindDistance(2).levelDecreasePerBlock(2).block(() -> ModFluids.QUICKSILVER_BLOCK.get()).bucket(() -> ModItems.QUICKSILVER_BUCKET.get());

    public static final RegistryObject<FlowingFluidBlock> QUICKSILVER_BLOCK = ModBlocks.BLOCKS.register("quicksilver", () -> new FlowingFluidBlock(() -> ModFluids.QUICKSILVER_FLUID.get(), AbstractBlock.Properties.create(Material.LAVA).doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }



}

package net.joefoxe.hexerei.event;

import net.joefoxe.hexerei.fluid.ModFluids;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void getFogDensity(EntityViewRenderEvent.FogDensity event) {
        ActiveRenderInfo info = event.getInfo();
        FluidState fluidState = info.getFluidState();
        if (fluidState.isEmpty())
            return;
        Fluid fluid = fluidState.getFluid();

        if (fluid.isEquivalentTo(ModFluids.BLOOD_FLUID.get())) {
            event.setDensity(0.75f);
            event.setCanceled(true);
            return;
        }

    }

    @SubscribeEvent
    public static void getFogColor(EntityViewRenderEvent.FogColors event) {
        ActiveRenderInfo info = event.getInfo();
        FluidState fluidState = info.getFluidState();
        if (fluidState.isEmpty())
            return;
        Fluid fluid = fluidState.getFluid();

        if (fluid.isEquivalentTo(ModFluids.BLOOD_FLUID.get())) {
            event.setRed(48 / 256f);
            event.setGreen(4 / 256f);
            event.setBlue(4 / 256f);
        }

    }
}

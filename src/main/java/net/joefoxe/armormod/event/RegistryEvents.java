package net.joefoxe.armormod.event;

import net.joefoxe.armormod.model.ModModels;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        ModModels.setupRenderLayers();
    }



}

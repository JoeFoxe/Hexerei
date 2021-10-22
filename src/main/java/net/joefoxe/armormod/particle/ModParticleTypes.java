package net.joefoxe.armormod.particle;

import net.joefoxe.armormod.ArmorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;
import java.util.function.Supplier;

public class ModParticleTypes {
    public static ParticleType<CauldronParticleData> cauldronParticleType;

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ArmorMod.MOD_ID);

    public static final RegistryObject<BasicParticleType> CAULDRON = PARTICLES.register("cauldron_particle", () -> new BasicParticleType(true));

//    @SubscribeEvent
//    public static void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> iParticleTypeRegisterEvent) {
//        cauldronParticleType = new CauldronParticleType();
//        cauldronParticleType.setRegistryName("armormod:cauldron_particle");
//        iParticleTypeRegisterEvent.getRegistry().register(cauldronParticleType);
//    }

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        // not actually required for this example....
    }

}
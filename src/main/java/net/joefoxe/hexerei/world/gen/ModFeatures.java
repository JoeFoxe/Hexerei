package net.joefoxe.hexerei.world.gen;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.world.structure.structures.HexereiAbstractTreeFeature;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES
            = DeferredRegister.create(ForgeRegistries.FEATURES, Hexerei.MOD_ID);


    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> WILLOW_TREE = FEATURES.register("willow_tree",
            () -> new HexereiAbstractTreeFeature(NoFeatureConfig.CODEC));


    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }


}

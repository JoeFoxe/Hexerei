package net.joefoxe.armormod.data.recipes;

import net.joefoxe.armormod.ArmorMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {
    public static DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArmorMod.MOD_ID);

    public static final RegistryObject<MixingCauldronRecipe.Serializer> MIXING_SERIALIZER = RECIPE_SERIALIZER.register("mixingcauldron", MixingCauldronRecipe.Serializer::new);

    public static IRecipeType<MixingCauldronRecipe> MIXING_CAULDRON_RECIPE = new MixingCauldronRecipe.MixingCauldronRecipeType();

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, MixingCauldronRecipe.TYPE_ID, MIXING_CAULDRON_RECIPE);
    }
}

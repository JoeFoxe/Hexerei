package net.joefoxe.hexerei.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.state.properties.LiquidType;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;

public class MixingCauldronRecipe implements IMixingCauldronRecipe{

    //I didnt full implement weather yet, I had it in then took it out in replace for the liquid type. still debaiting on if I should add it back. maybe add another enum to the weather for ANY?
    public enum Weather {
        CLEAR,
        RAIN,
        THUNDERING;

        public static Weather getWeatherByString(String s) {
            return Objects.equals(s, "thundering") ? THUNDERING : Objects.equals(s, "rain") ? RAIN : CLEAR;
        }
    }
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final Weather weather;
    private final LiquidType liquid;
    private final LiquidType liquidOutput;
    private final int fluidLevelsConsumed;


    public MixingCauldronRecipe(ResourceLocation id, ItemStack output,
                                    NonNullList<Ingredient> recipeItems, Weather weather, LiquidType liquid, LiquidType liquidOutput, int fluidLevelsConsumed) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.weather = weather;
        this.liquid = liquid;
        this.liquidOutput = liquidOutput;
        this.fluidLevelsConsumed = fluidLevelsConsumed;
    }


    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if(recipeItems.get(0).test(inv.getStackInSlot(0)) &&
            recipeItems.get(1).test(inv.getStackInSlot(1)) &&
            recipeItems.get(2).test(inv.getStackInSlot(2)) &&
            recipeItems.get(3).test(inv.getStackInSlot(3)) &&
            recipeItems.get(4).test(inv.getStackInSlot(4)) &&
            recipeItems.get(5).test(inv.getStackInSlot(5)) &&
            recipeItems.get(6).test(inv.getStackInSlot(6)) &&
            recipeItems.get(7).test(inv.getStackInSlot(7)))
        {
            return true;
        }
        return false;

    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    public Weather getWeather() {
        return this.weather;
    }

    public LiquidType getLiquid() { return this.liquid; }

    public LiquidType getLiquidOutput() { return this.liquidOutput; }

    public int getFluidLevelsConsumed() { return this.fluidLevelsConsumed; }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.MIXING_CAULDRON.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.MIXING_SERIALIZER.get();
    }

    public static class MixingCauldronRecipeType implements IRecipeType<MixingCauldronRecipe> {
        @Override
        public String toString() {
            return MixingCauldronRecipe.TYPE_ID.toString();
        }
    }


    // for Serializing the recipe into/from a json
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<MixingCauldronRecipe> {

        @Override
        public MixingCauldronRecipe read(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
            String weather = JSONUtils.getString(json, "weather");
            String liquid = JSONUtils.getString(json, "liquid");
            String liquidOutput = JSONUtils.getString(json, "liquidOutput");
            int fluidLevelsConsumed = JSONUtils.getInt(json, "fluidLevelsConsumed");

            JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(8, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.deserialize(ingredients.get(i)));
            }

            return new MixingCauldronRecipe(recipeId, output,
                    inputs, Weather.getWeatherByString(weather), LiquidType.valueOf(liquid.toUpperCase(Locale.ROOT)), LiquidType.valueOf(liquidOutput.toUpperCase(Locale.ROOT)), fluidLevelsConsumed);
        }

        @Nullable
        @Override
        public MixingCauldronRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(8, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.read(buffer));
            }

            ItemStack output = buffer.readItemStack();
            return new MixingCauldronRecipe(recipeId, output,
                    inputs, null, LiquidType.EMPTY, LiquidType.EMPTY, 1);
        }

        @Override
        public void write(PacketBuffer buffer, MixingCauldronRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput(), false);
        }
    }
}

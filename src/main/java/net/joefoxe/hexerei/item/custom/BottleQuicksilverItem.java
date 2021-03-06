package net.joefoxe.hexerei.item.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Properties;

public class BottleQuicksilverItem extends Item {

    public static Food FOOD = new Food.Builder().saturation(1).hunger(1).setAlwaysEdible().effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.8F).effect(new EffectInstance(Effects.POISON, 300, 0), 0.8F).build();

    public BottleQuicksilverItem(Properties properties) {
        super(properties.food(FOOD));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (!world.isRemote && entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
            ItemStack itemstack = ((ServerPlayerEntity) entityLiving).getHeldItem(Hand.MAIN_HAND);
            if (itemstack.isEmpty()) {
                player.setHeldItem(Hand.MAIN_HAND, itemstack3);
            } else if (!player.inventory.addItemStackToInventory(itemstack3)) {
                player.dropItem(itemstack3, false);
            } else if (player instanceof ServerPlayerEntity) {
                player.sendContainerToPlayer(player.container);
            }
        }

        return super.onItemUseFinish(stack, world, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.bottle_quicksilver_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.bottle_quicksilver"));
        }


        super.addInformation(stack, world, tooltip, flagIn);
    }

}

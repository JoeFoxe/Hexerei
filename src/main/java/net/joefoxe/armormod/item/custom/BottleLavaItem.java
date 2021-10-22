package net.joefoxe.armormod.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BottleLavaItem extends Item {

    public static Food FOOD = new Food.Builder().saturation(0).hunger(0).setAlwaysEdible().build();

    public BottleLavaItem(Properties properties) {
        super(properties.food(FOOD));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (!world.isRemote && entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
            ItemStack itemstack = ((ServerPlayerEntity) entityLiving).getHeldItem(Hand.MAIN_HAND);
            entityLiving.setFire(10);
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
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);

        Random rand = new Random();

        if(rand.nextDouble() > 0.5d) {
            stack.damageItem(1, (PlayerEntity) entity, player -> player.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.armormod.bottle_lava_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.armormod.bottle_lava"));
        }


        super.addInformation(stack, world, tooltip, flagIn);
    }
}


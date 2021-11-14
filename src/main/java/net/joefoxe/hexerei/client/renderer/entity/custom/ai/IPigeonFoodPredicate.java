package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.minecraft.block.Block;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IPigeonFoodPredicate {
    public boolean isFood(ItemStack stackIn);
}

package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

public interface ITargetsDroppedItems {

    boolean canTargetItem(ItemStack stack);

    void onGetItem(ItemEntity e);

    default void onFindTarget(ItemEntity e){}

    default double getMaxDistToItem(){return 2.0D; }
}
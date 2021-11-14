package net.joefoxe.hexerei.items;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class JarSlot extends SlotItemHandler {
    private final int index;

    public JarSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return ((JarHandler) this.getItemHandler()).getStackLimit(this.index, stack);
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return other instanceof JarSlot && ((JarSlot) other).getItemHandler() == this.getItemHandler();
    }
}
package net.joefoxe.hexerei.items;

import net.joefoxe.hexerei.config.HexConfig;
import net.joefoxe.hexerei.util.HexereiTags;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class JarHandler extends ItemStackHandler {

    public final int stacklimit;

    public JarHandler(int size, int stacklimit) {
        super(size);
        this.stacklimit = stacklimit;
    }

    public boolean isEmpty(){
        return IntStream.range(0, this.getSlots()).allMatch(i -> this.getStackInSlot(i).isEmpty());
    }

    public boolean noValidSlots(){
        return IntStream.range(0,getSlots())
                .mapToObj(this::getStackInSlot)
                .allMatch(stack -> stack.isEmpty());
    }

//    @Override
//    public void onContentsChanged(int slot) {
//        markDirty();
//    }



    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        this.stacks.set(slot, stack);
//        onContentsChanged(slot);
    }


    @Override
    public int getSlotLimit(int slot) {
        return stacklimit;
    }

    @Override
    public int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return stacklimit;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
//        if(HexConfig.JARS_ONLY_HOLD_HERBS.get())
//            return stack.getItem().isIn(HexereiTags.Items.HERB_ITEM);
        return true;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, stacklimit);
        //attempting to extract equal to or greater than what is present
        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                    this.stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
            }
                return existing;
            //there is more than requested
        } else {
            if (!simulate) {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }
            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    public NonNullList<ItemStack> getContents(){
        return stacks;
    }

    @Override
    public CompoundNBT serializeNBT() {
        ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < getContents().size(); i++) {
            if (!getContents().get(i).isEmpty()) {
                int realCount = Math.min(stacklimit, getContents().get(i).getCount());
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("Slot", i);
                getContents().get(i).write(itemTag);
                itemTag.putInt("ExtendedCount", realCount);
                nbtTagList.add(itemTag);
            }
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", getContents().size());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : getContents().size());
        ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                if (itemTags.contains("StackList", Constants.NBT.TAG_LIST)) {
                    ItemStack stack = ItemStack.EMPTY;
                    ListNBT stackTagList = itemTags.getList("StackList", Constants.NBT.TAG_COMPOUND);
                    for (int j = 0; j < stackTagList.size(); j++) {
                        CompoundNBT itemTag = stackTagList.getCompound(j);
                        ItemStack temp = ItemStack.read(itemTag);
                        if (!temp.isEmpty()) {
                            if (stack.isEmpty()) stack = temp;
                            else stack.grow(temp.getCount());
                        }
                    }
                    if (!stack.isEmpty()) {
                        int count = stack.getCount();
                        count = Math.min(count, getStackLimit(slot, stack));
                        stack.setCount(count);

                        stacks.set(slot, stack);
                    }
                } else {
                    ItemStack stack = ItemStack.read(itemTags);
                    if (itemTags.contains("ExtendedCount", Constants.NBT.TAG_INT)) {
                        stack.setCount(itemTags.getInt("ExtendedCount"));
                    }
                    stacks.set(slot, stack);
                }
            }
        }
        onLoad();
    }

}

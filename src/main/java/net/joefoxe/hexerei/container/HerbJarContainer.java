package net.joefoxe.hexerei.container;

import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.items.JarSlot;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class HerbJarContainer extends Container {
    private final TileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;


    public HerbJarContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.HERB_JAR_CONTAINER.get(), windowId);
        this.tileEntity = world.getTileEntity(pos);
        playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);


        layoutPlayerInventorySlots(11, 147);

        //add slots for mixing cauldron
        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new JarSlot(h, 0, 83, 74));
            });
        }
//
//        trackInt(new IntReferenceHolder() {
//            @Override
//            public void set(int value) {
//                ((CofferTile)tileEntity).setButtonToggled(value);
//            }
//            @Override
//            public int get() {
//                return ((CofferTile)tileEntity).getButtonToggled();
//            }
//    });



    }


//    public void playSound() {
//        this.tileEntity.getWorld().playSound((PlayerEntity)null, this.tileEntity.getPos(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);;
//    }

    public static boolean canAddItemToSlot(@Nullable Slot slot, @Nonnull ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slot == null || !slot.getHasStack();
        if (slot != null) {
            ItemStack slotStack = slot.getStack();

            if (!flag && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(slotStack, stack)) {
                return slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slot.getItemStackLimit(slotStack);
            }
        }
        return flag;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        ItemStack itemstack = ItemStack.EMPTY;
        PlayerInventory PlayerInventory = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int j1 = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);

            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                this.resetDrag();
            } else if (PlayerInventory.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(dragType);

                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot7 = this.inventorySlots.get(slotId);
                ItemStack mouseStack = PlayerInventory.getItemStack();

                if (slot7 != null && HerbJarContainer.canAddItemToSlot(slot7, mouseStack, true) && slot7.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    this.dragSlots.add(slot7);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack mouseStackCopy = PlayerInventory.getItemStack().copy();
                    int k1 = PlayerInventory.getItemStack().getCount();

                    for (Slot dragSlot : this.dragSlots) {
                        ItemStack mouseStack = PlayerInventory.getItemStack();

                        if (dragSlot != null && HerbJarContainer.canAddItemToSlot(dragSlot, mouseStack, true) && dragSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(dragSlot)) {
                            ItemStack itemstack14 = mouseStackCopy.copy();
                            int j3 = dragSlot.getHasStack() ? dragSlot.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
                            int k3 = dragSlot.getItemStackLimit(itemstack14);

                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            dragSlot.putStack(itemstack14);
                        }
                    }

                    mouseStackCopy.setCount(k1);
                    PlayerInventory.setItemStack(mouseStackCopy);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!PlayerInventory.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(PlayerInventory.getItemStack(), true);
                        PlayerInventory.setItemStack(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.dropItem(PlayerInventory.getItemStack().split(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot5 = this.inventorySlots.get(slotId);

                if (slot5 == null || !slot5.canTakeStack(player)) {
                    return ItemStack.EMPTY;
                }

                for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId)) {
                    itemstack = itemstack7.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot6 = this.inventorySlots.get(slotId);

                if (slot6 != null) {
                    ItemStack slotStack = slot6.getStack();
                    ItemStack mouseStack = PlayerInventory.getItemStack();

                    if (!slotStack.isEmpty()) {
                        itemstack = slotStack.copy();
                    }

                    if (slotStack.isEmpty()) {
                        if (!mouseStack.isEmpty() && slot6.isItemValid(mouseStack)) {
                            int i3 = dragType == 0 ? mouseStack.getCount() : 1;

                            if (i3 > slot6.getItemStackLimit(mouseStack)) {
                                i3 = slot6.getItemStackLimit(mouseStack);
                            }

                            slot6.putStack(mouseStack.split(i3));
                        }
                    } else if (slot6.canTakeStack(player)) {
                        if (mouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                slot6.putStack(ItemStack.EMPTY);
                                PlayerInventory.setItemStack(ItemStack.EMPTY);
                            } else {
                                int toMove;
                                if (slot6 instanceof JarSlot) {
                                    if (slotStack.getMaxStackSize() < slotStack.getCount())
                                        toMove = dragType == 0 ? slotStack.getMaxStackSize() : (slotStack.getMaxStackSize() + 1) / 2;
                                    else toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                                } else {
                                    toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                                }
                                //int toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                                PlayerInventory.setItemStack(slot6.decrStackSize(toMove));

                                if (slotStack.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, PlayerInventory.getItemStack());
                            }
                        } else if (slot6.isItemValid(mouseStack)) {
                            if (slotStack.getItem() == mouseStack.getItem() && ItemStack.areItemStackTagsEqual(slotStack, mouseStack)) {
                                int k2 = dragType == 0 ? mouseStack.getCount() : 1;

                                if (k2 > slot6.getItemStackLimit(mouseStack) - slotStack.getCount()) {
                                    k2 = slot6.getItemStackLimit(mouseStack) - slotStack.getCount();
                                }

                                mouseStack.shrink(k2);
                                slotStack.grow(k2);
                            } else if (mouseStack.getCount() <= slot6.getItemStackLimit(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                                slot6.putStack(mouseStack);
                                PlayerInventory.setItemStack(slotStack);
                            }
                        } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.areItemStackTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty()) {
                            int j2 = slotStack.getCount();

                            if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                                mouseStack.grow(j2);
                                slotStack = slot6.decrStackSize(j2);

                                if (slotStack.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, PlayerInventory.getItemStack());
                            }
                        }
                    }

                    slot6.onSlotChanged();
                }
            }
        } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            //just don't do anything as swapping slots is unsupported
      /*
      Slot slot4 = this.inventorySlots.get(slotId);
      ItemStack itemstack6 = PlayerInventory.getStackInSlot(dragType);
      ItemStack slotStack = slot4.getStack();
      if (!itemstack6.isEmpty() || !slotStack.isEmpty()) {
        if (itemstack6.isEmpty()) {
          if (slot4.canTakeStack(player)) {
            int maxAmount = slotStack.getMaxStackSize();
            if (slotStack.getCount() > maxAmount) {
              ItemStack newSlotStack = slotStack.copy();
              ItemStack takenStack = newSlotStack.split(maxAmount);
              PlayerInventory.setInventorySlotContents(dragType, takenStack);
              //slot4.onSwapCraft(takenStack.getCount());
              slot4.putStack(newSlotStack);
              slot4.onTake(player, takenStack);
            } else {
              PlayerInventory.setInventorySlotContents(dragType, slotStack);
              //slot4.onSwapCraft(slotStack.getCount());
              slot4.putStack(ItemStack.EMPTY);
              slot4.onTake(player, slotStack);
            }
          }
        } else if (slotStack.isEmpty()) {
          if (slot4.isItemValid(itemstack6)) {
            int l1 = slot4.getItemStackLimit(itemstack6);
            if (itemstack6.getCount() > l1) {
              slot4.putStack(itemstack6.split(l1));
            } else {
              slot4.putStack(itemstack6);
              PlayerInventory.setInventorySlotContents(dragType, ItemStack.EMPTY);
            }
          }
        } else if (slot4.canTakeStack(player) && slot4.isItemValid(itemstack6)) {
          int i2 = slot4.getItemStackLimit(itemstack6);
          if (itemstack6.getCount() > i2) {
            slot4.putStack(itemstack6.split(i2));
            slot4.onTake(player, slotStack);
            if (!PlayerInventory.addItemStackToInventory(slotStack)) {
              player.dropItem(slotStack, true);
            }
          } else {
            slot4.putStack(itemstack6);
            if (slotStack.getCount() > slotStack.getMaxStackSize()) {
              ItemStack remainder = slotStack.copy();
              PlayerInventory.setInventorySlotContents(dragType, remainder.split(slotStack.getMaxStackSize()));
              if (!PlayerInventory.addItemStackToInventory(remainder)) {
                player.dropItem(remainder, true);
              }
            } else {
              PlayerInventory.setInventorySlotContents(dragType, slotStack);
            }
            slot4.onTake(player, slotStack);
          }
        }
      }*/
        } else if (clickTypeIn == ClickType.CLONE && player.abilities.isCreativeMode && PlayerInventory.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = this.inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack()) {
                ItemStack itemstack5 = slot3.getStack().copy();
                itemstack5.setCount(itemstack5.getMaxStackSize());
                PlayerInventory.setItemStack(itemstack5);
            }
        } else if (clickTypeIn == ClickType.THROW && PlayerInventory.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot2 = this.inventorySlots.get(slotId);

            if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
                ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
                slot2.onTake(player, itemstack4);
                player.dropItem(itemstack4, true);
            }
        } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack mouseStack = PlayerInventory.getItemStack();

            if (!mouseStack.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
                int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int j = dragType == 0 ? 1 : -1;

                for (int k = 0; k < 2; ++k) {
                    for (int l = i; l >= 0 && l < this.inventorySlots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
                        Slot slot1 = this.inventorySlots.get(l);

                        if (slot1.getHasStack() && HerbJarContainer.canAddItemToSlot(slot1, mouseStack, true) && slot1.canTakeStack(player) && this.canMergeSlot(mouseStack, slot1)) {
                            ItemStack itemstack2 = slot1.getStack();

                            if (k != 0 || itemstack2.getCount() < slot1.getItemStackLimit(itemstack2)) {
                                int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
                                ItemStack itemstack3 = slot1.decrStackSize(i1);
                                mouseStack.grow(i1);

                                if (itemstack3.isEmpty()) {
                                    slot1.putStack(ItemStack.EMPTY);
                                }

                                slot1.onTake(player, itemstack3);
                            }
                        }
                    }
                }
            }

            this.detectAndSendChanges();
        }

        if (itemstack.getCount() > 64) {
            itemstack = itemstack.copy();
            itemstack.setCount(64);
        }

        return itemstack;
    }


    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        while (!stack.isEmpty()) {
            if (reverseDirection) {
                if (i < startIndex) break;
            } else {
                if (i >= endIndex) break;
            }

            Slot slot = this.inventorySlots.get(i);
            ItemStack itemstack = slot.getStack();

            if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                int j = itemstack.getCount() + stack.getCount();
                int maxSize = slot.getItemStackLimit(itemstack);

                if (j <= maxSize) {
                    stack.setCount(0);
                    itemstack.setCount(j);
                    slot.onSlotChanged();
                    flag = true;
                } else if (itemstack.getCount() < maxSize) {
                    stack.shrink(maxSize - itemstack.getCount());
                    itemstack.setCount(maxSize);
                    slot.onSlotChanged();
                    flag = true;
                }
            }

            i += (reverseDirection) ? -1 : 1;
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) i = endIndex - 1;
            else i = startIndex;

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) break;
                } else {
                    if (i >= endIndex) break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getItemStackLimit(stack)) {
                        slot1.putStack(stack.split(slot1.getItemStackLimit(stack)));
                    } else {
                        slot1.putStack(stack.split(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                i += (reverseDirection) ? -1 : 1;
            }
        }

        return flag;
    }

//
//    @Override
//    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
//        boolean flag = false;
//        int i = startIndex;
//        if (reverseDirection) {
//            i = endIndex - 1;
//        }
//
//        if (stack.isStackable()) {
//            while(!stack.isEmpty()) {
//                if (reverseDirection) {
//                    if (i < startIndex) {
//                        break;
//                    }
//                } else if (i >= endIndex) {
//                    break;
//                }
//
//                Slot slot = this.inventorySlots.get(i);
//                ItemStack itemstack = slot.getStack();
//                if (!itemstack.isEmpty() && areItemsAndTagsEqual(stack, itemstack)) {
//                    int j = itemstack.getCount() + stack.getCount();
//                    int maxSize = 1024;
//                    if (j <= maxSize) {
//                        stack.setCount(0);
//                        itemstack.setCount(j);
//                        slot.onSlotChanged();
//                        flag = true;
//                    } else if (itemstack.getCount() < maxSize) {
//                        stack.shrink(maxSize - itemstack.getCount());
//                        itemstack.setCount(maxSize);
//                        slot.onSlotChanged();
//                        flag = true;
//                    }
//                }
//
//                if (reverseDirection) {
//                    --i;
//                } else {
//                    ++i;
//                }
//            }
//        }
//
//        if (!stack.isEmpty()) {
//            if (reverseDirection) {
//                i = endIndex - 1;
//            } else {
//                i = startIndex;
//            }
//
//            while(true) {
//                if (reverseDirection) {
//                    if (i < startIndex) {
//                        break;
//                    }
//                } else if (i >= endIndex) {
//                    break;
//                }
//
//                Slot slot1 = this.inventorySlots.get(i);
//                ItemStack itemstack1 = slot1.getStack();
//                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
//                    if (stack.getCount() > 1024) {
//                        slot1.putStack(stack.split(1024));
//                    } else {
//                        slot1.putStack(stack.split(stack.getCount()));
//                    }
//
//                    slot1.onSlotChanged();
//                    flag = true;
//                    break;
//                }
//
//                if (reverseDirection) {
//                    --i;
//                } else {
//                    ++i;
//                }
//            }
//        }
//
//        return flag;
//    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()),
                playerIn, ModBlocks.HERB_JAR.get());
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();


        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }
        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }
}

package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.custom.Candle;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.container.MixingCauldronContainer;
import net.joefoxe.hexerei.data.recipes.MixingCauldronRecipe;
import net.joefoxe.hexerei.data.recipes.ModRecipeTypes;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.state.properties.LiquidType;
import net.joefoxe.hexerei.util.HexereiTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static net.joefoxe.hexerei.block.custom.MixingCauldron.LEVEL;

public class MixingCauldronTile extends LockableLootTileEntity implements ISidedInventory ,ITickableTileEntity, IClearable, INamedContainerProvider {

//    private final ItemStackHandler itemHandler = createHandler();
//    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public boolean crafting;
    public int craftDelay;
    public float degrees;
    private boolean crafted;
    private boolean extracted = false;
    private int isColliding = 0;  // 15 is colliding, 0 is no longer colliding
    public static final int craftDelayMax = 100;
    private long tickedGameTime;
    private NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);

    private static final int[] SLOTS_INPUT = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    private static final int[] SLOTS_OUTPUT = new int[]{8};

    VoxelShape BLOOD_SIGIL_SHAPE = Block.makeCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    VoxelShape HOPPER_SHAPE = Block.makeCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 4.0D, 14.0D);



    public MixingCauldronTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if(this.world != null)
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.getTileEntity().getPos()), this.world.getBlockState(this.getTileEntity().getPos()),
                    Constants.BlockFlags.BLOCK_UPDATE);

    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + Hexerei.MOD_ID + ".mixing_cauldron");
    }

    public MixingCauldronTile() {
        this(ModTileEntities.MIXING_CAULDRON_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;

    }


    /**
     * Returns the stack in the given slot.
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.items.size() ? this.items.get(index) : ItemStack.EMPTY;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
//        markDirty();
        ItemStack itemStack = ItemStackHelper.getAndSplit(this.items, index, count);
        if(itemStack.getCount() < 1)
            itemStack.setCount(1);
        return itemStack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {

        return ItemStackHelper.getAndRemove(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= 0 && index < this.items.size()) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            this.items.set(index, itemStack);
        }

        markDirty();
    }



    public boolean isItemValidForSlot(int index, ItemStack stack) {

        if (index == 8)
            return false;
        if (index == 9 && !stack.getItem().isIn(HexereiTags.Items.SIGILS))
            return false;
        return this.items.get(index).isEmpty();
    }

    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_OUTPUT;
        } else {
            return SLOTS_INPUT;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
//        if (index == 3) {
//            return stack.getItem() == Items.GLASS_BOTTLE;
//        } else {
//            return true;
//        }
        return true;
    }

    public int getCraftDelay() {
        return this.craftDelay;
    }
    public void setCraftDelay(int delay) {
        this.craftDelay =  delay;
    }



    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new MixingCauldronContainer(id, this.world, this.pos, player, player.player);
    }

    @Override
    public void clear() {
        super.clear();
        this.items.clear();
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 4096D;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB aabb = super.getRenderBoundingBox().grow(5, 5, 5);
        return aabb;
    }

    /*
     * When we call this.world.notifyBlockUpdate, this method is called on the
     * server to generate a packet to send to the client. If you have lots of data,
     * it's a good idea to keep track of which data has changed since the last time
     * this TE was synced, and then only send the changed data; this reduces the
     * amount of packets sent, which is good we only have one value to sync so we'll
     * just write everything into the NBT again.
     */
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);

        return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
    }

    /*
     * This method gets called on the client when it receives the packet that was
     * sent in getUpdatePacket(). And here we just read the data from the packet
     * that was recieved.
     */
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.world.getBlockState(this.pos) ,pkt.getNbtCompound());
    }

    /*
     * This method is called to generate NBT for syncing a packet when a client
     * loads a chunk that this tile entity is in. We want to tell the client as much
     * data as it needs to know since it doesn't know any data at this current
     * stage. We usually just need to put write() in here. If you ever have data
     * that would be written to the disk but the client doesn't ever need to know,
     * you can just sync the need-to-know data instead of calling write() there's an
     * equivalent method for reading the update tag but it just defaults to read()
     * anyway.
     */
    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(10) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 8)
                    return false;
                if (slot == 9 && !stack.getItem().isIn(HexereiTags.Items.SIGILS))
                    return false;
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

//            @Nonnull
//            @Override
//            public ItemStack extractItem(int slot, int amount, boolean simulate) {
//                if(slot != 8)
//                    return ItemStack.EMPTY;
//                return super.extractItem(slot, amount, simulate);
//            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    public Item getItemInSlot(int slot) {
        return this.items.get(slot).getItem();
    }


    public int getCraftMaxDelay() {
        return this.craftDelayMax;
    }


    public boolean getCrafted() {
        return this.crafted;
    }

    public int getNumberOfItems() {

        int num = 0;
        for(int i = 0; i < 8; i++)
        {
            if(this.items.get(i) != ItemStack.EMPTY)
                num++;
        }
        return num;

    }

    private void strikeLightning() {
        if(!this.world.isRemote()) {
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld)world, null, null,
                    pos, SpawnReason.TRIGGERED, true, true);
        }
    }

    public void onEntityCollision(Entity entity) {
        BlockPos blockpos = this.getPos();
        if (entity instanceof ItemEntity) {
            if (VoxelShapes.compare(VoxelShapes.create(entity.getBoundingBox().offset((double)(-blockpos.getX()), (double)(-blockpos.getY()), (double)(-blockpos.getZ()))), HOPPER_SHAPE, IBooleanFunction.AND)) {
                if(captureItem((ItemEntity)entity))
                    ((MixingCauldron)this.getBlockState().getBlock()).setEmitParticles(2);
            }
        }else
        {
            if (VoxelShapes.compare(VoxelShapes.create(entity.getBoundingBox().offset((double)(-blockpos.getX()), (double)(-blockpos.getY()), (double)(-blockpos.getZ()))), BLOOD_SIGIL_SHAPE, IBooleanFunction.AND)) {
                if(this.isColliding <= 1 && this.getItemInSlot(9).getItem() == ModItems.BLOOD_SIGIL.get()) {
                    Random random = new Random();
                    entity.attackEntityFrom(DamageSource.MAGIC, 3.0f);
                    if(random.nextDouble() > 0.5f)
                    {
                        if(this.getBlockState().get(MixingCauldron.FLUID) == LiquidType.EMPTY || (this.getBlockState().get(MixingCauldron.FLUID) == LiquidType.BLOOD && this.getBlockState().get(LEVEL) < 3)) {
                            ((MixingCauldron) this.getBlockState().getBlock()).setFillLevel(world, pos, this.getBlockState(), this.getBlockState().get(LEVEL) + 1, LiquidType.BLOOD);
                            entity.playSound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK,1.5f, 1.5f);
                            ((MixingCauldron)this.getBlockState().getBlock()).setEmitParticles(2);

                        }
                    }
                }

                this.isColliding = 6; // little cooldown so you dont constantly take damage, you must jump on the nails to take damage

            }

        }

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {

        return super.getCapability(cap);
    }

    public boolean captureItem(ItemEntity itemEntity) {
        boolean flag = false;
        ItemStack itemstack = itemEntity.getItem().copy();

        //check if there is a slot open  getFirstOpenSlot
        if (getFirstOpenSlot() >= 0)
        {
            this.setInventorySlotContents(getFirstOpenSlot(), itemstack);
//            this.itemHandler.insertItem(getFirstOpenSlot(), itemstack, false);
            itemEntity.getItem().shrink(1);
            //((MixingCauldron)this.getBlockState().getBlock()).emitCraftCompletedParticles();
            return true;
        }
        return false;
    }



    public int getFirstOpenSlot(){
        for(int i = 0; i < 8; i++) {
            if(this.items.get(i).isEmpty())
                return i;
        }
        return -1;
    }


    public void craft(){
        Inventory inv = new Inventory(10);
        for (int i = 0; i < 9; i++) {
            inv.setInventorySlotContents(i, this.items.get(i));
        }


        Optional<MixingCauldronRecipe> recipe = world.getRecipeManager()
                .getRecipe(ModRecipeTypes.MIXING_CAULDRON_RECIPE, inv, world);

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getRecipeOutput();
            //ask for delay
            if(iRecipe.getLiquid() == (this.world.getBlockState(this.pos).get(MixingCauldron.FLUID)) && (inv.getStackInSlot(8) == ItemStack.EMPTY || inv.getStackInSlot(8).getCount() == 0) && this.crafted == false && iRecipe.getFluidLevelsConsumed() <= (this.world.getBlockState(this.pos).get(MixingCauldron.LEVEL))) {
                this.crafting = true;

                if(this.craftDelay >= this.craftDelayMax) {
                    Random rand = new Random();
                    craftTheItem(output);
                    markDirty();
                    world.setBlockState(pos, world.getBlockState(pos).with(MixingCauldron.FLUID, iRecipe.getLiquidOutput()), 3);

                    //for setting a cooldown on crafting so the animations can take place
                    this.crafted = true;

                    for(int i = 0; i < iRecipe.getFluidLevelsConsumed(); i++)
                        ((MixingCauldron)this.world.getBlockState(this.pos).getBlock()).subtractLevel(this.world, pos);

               }
            }


        });
        if(this.crafting)
            this.craftDelay += 2;
        if(this.craftDelay >= 1)
            this.craftDelay--;
        this.crafting = false;
        if(this.craftDelay > 0)
            this.world.setBlockState(pos, this.world.getBlockState(this.pos).with(MixingCauldron.CRAFT_DELAY, Integer.valueOf(MathHelper.clamp(this.craftDelay - 1, 0, MixingCauldronTile.craftDelayMax))), 2);
        if(this.craftDelay < 10)
            this.crafted = false;

    }

    private void craftTheItem(ItemStack output) {
//        itemHandler.extractItem(0, 1, false);
//        itemHandler.extractItem(1, 1, false);
//        itemHandler.extractItem(2, 1, false);
//        itemHandler.extractItem(3, 1, false);
//        itemHandler.extractItem(4, 1, false);
//        itemHandler.extractItem(5, 1, false);
//        itemHandler.extractItem(6, 1, false);
//        itemHandler.extractItem(7, 1, false);

        if(output.getItem() == ModItems.TALLOW_IMPURITY.get())
        {
            Random random = new Random();
            if(random.nextInt(5) != 0)
                output = ItemStack.EMPTY;
        }
        this.setInventorySlotContents(0, ItemStack.EMPTY);
        this.setInventorySlotContents(1, ItemStack.EMPTY);
        this.setInventorySlotContents(2, ItemStack.EMPTY);
        this.setInventorySlotContents(3, ItemStack.EMPTY);
        this.setInventorySlotContents(4, ItemStack.EMPTY);
        this.setInventorySlotContents(5, ItemStack.EMPTY);
        this.setInventorySlotContents(6, ItemStack.EMPTY);
        this.setInventorySlotContents(7, ItemStack.EMPTY);
        this.setInventorySlotContents(8, output);
//        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(1, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(2, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(3, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(4, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(5, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(6, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(7, ItemStack.EMPTY);
//        itemHandler.setStackInSlot(8, output);


    }

    @Override
    public void tick() {
        if(world.isRemote)
            return;
        this.tickedGameTime = this.world.getGameTime();
        craft();
        if(extracted)
        {
            extracted = false;
            markDirty();
        }
        this.isColliding--;

    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

}

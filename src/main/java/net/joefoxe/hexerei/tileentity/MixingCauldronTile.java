package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.container.MixingCauldronContainer;
import net.joefoxe.hexerei.data.recipes.MixingCauldronRecipe;
import net.joefoxe.hexerei.data.recipes.ModRecipeTypes;
import net.joefoxe.hexerei.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class MixingCauldronTile extends LockableLootTileEntity implements ITickableTileEntity, IClearable, INamedContainerProvider {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public boolean crafting;
    public int craftDelay;
    public static final int craftDelayMax = 100;

    protected NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);


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
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(),
                Constants.BlockFlags.BLOCK_UPDATE);
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
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        ItemStackHelper.saveAllItems(compound, this.items);
        return super.write(compound);
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

    /*
     * This method is called after it has recieved an update tag to tell it what to
     * do with that tag. All we do in here is read as we don't need to do anything
     * else with it.
     */
//    @Override
//    public void handleUpdateTag(CompoundNBT tag) {
//        this.read(this.world.getBlockState(this.pos) ,tag);
//    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 8)
                    return false;
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    public Item getItemInSlot(int slot) {

        return this.itemHandler.getStackInSlot(slot).getItem();

    }


    public int getCraftMaxDelay() {

        return this.craftDelayMax;

    }

    public int getCraftDelay() {

//        System.out.println(this.craftDelay);
//        System.out.println(this.getNumberOfItems());
        return this.craftDelay;

    }

    public int getNumberOfItems() {

        int num = 0;
        for(int i = 0; i < this.itemHandler.getSlots() - 1; i++)
        {
            if(this.itemHandler.getStackInSlot(i) != ItemStack.EMPTY)
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

    public void craft(){
        Inventory inv = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setInventorySlotContents(i, itemHandler.getStackInSlot(i));
        }

        Optional<MixingCauldronRecipe> recipe = world.getRecipeManager()
                .getRecipe(ModRecipeTypes.MIXING_CAULDRON_RECIPE, inv, world);


//        System.out.println("--1--");
        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getRecipeOutput();
            //ask for delay
            if(iRecipe.getLiquid() == (this.world.getBlockState(this.pos).get(MixingCauldron.FLUID))) {
                this.crafting = true;

                if(this.craftDelay >= this.craftDelayMax) {
                    craftTheItem(output);
                    markDirty();
                }
            }
//            System.out.println(craftDelay);
//            System.out.println(getCraftDelay());
            //this.world.setBlockState(pos, this.world.getBlockState(this.pos).with(MixingCauldron.CRAFT_DELAY, this.craftDelay), 2);
//            System.out.println(this.craftDelay);
//            System.out.println("----");


        });
        if(this.crafting)
            this.craftDelay += 2;
        if(this.craftDelay >= 1)
            this.craftDelay--;
        this.crafting = false;
        if(this.craftDelay > 0) {
            this.world.setBlockState(pos, this.world.getBlockState(this.pos).with(MixingCauldron.CRAFT_DELAY, Integer.valueOf(MathHelper.clamp(this.craftDelay - 1, 0, MixingCauldronTile.craftDelayMax))), 2);
            System.out.println(this.world.getBlockState(this.pos).get(MixingCauldron.CRAFT_DELAY));
        }

//        System.out.println(this.craftDelay);
//        System.out.println("--2--");




        //MixingCauldron.setCraftDelay(this.world, this.pos, world.getBlockState(this.pos), this.craftDelay);

    }

    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);
        itemHandler.extractItem(3, 1, false);
        itemHandler.extractItem(4, 1, false);
        itemHandler.extractItem(5, 1, false);
        itemHandler.extractItem(6, 1, false);
        itemHandler.extractItem(7, 1, false);
        itemHandler.setStackInSlot(8, output);

    }

    @Override
    public void tick() {
        if(world.isRemote)
            return;

        craft();

    }

    @Override
    public int getSizeInventory() {
        return 0;
    }
}

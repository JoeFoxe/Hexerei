package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.container.HerbJarContainer;
import net.joefoxe.hexerei.items.JarHandler;
import net.joefoxe.hexerei.util.HexereiPacketHandler;
import net.joefoxe.hexerei.util.message.MessageCountUpdate;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Function;

public class HerbJarTile extends LockableLootTileEntity implements ITickableTileEntity, IClearable, INamedContainerProvider {

    public JarHandler itemHandler;
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler).cast();

    protected NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    private final IReorderingProcessor[] renderText = new IReorderingProcessor[1];

    private final ITextComponent[] signText = new ITextComponent[]{new StringTextComponent("Text")};

    public int degreesOpened;

    public ITextComponent customName;

    private long lastClickTime;
    private UUID lastClickUUID;

    public HerbJarTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.itemHandler = new JarHandler(1, 1024);
    }

    public void readInventory(CompoundNBT compound) {
        itemHandler.deserializeNBT(compound);
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
//        assert this.world != null;
//        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(),
//                Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + Hexerei.MOD_ID + ".herb_jar");
    }

    public HerbJarTile() {
        this(ModTileEntities.HERB_JAR_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        super.read(state, nbt);
        if (nbt.contains("CustomName", 8))
            this.customName = ITextComponent.Serializer.getComponentFromJson(nbt.getString("CustomName"));
    }


    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        if (this.customName != null)
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        ItemStackHelper.saveAllItems(compound, this.items);
        return super.write(compound);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new HerbJarContainer(id, this.world, this.pos, player, player.player);
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

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);

        return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.world.getBlockState(this.pos) ,pkt.getNbtCompound());
    }


    @Nullable
    @OnlyIn(Dist.CLIENT)
    public IReorderingProcessor reorderText(int row, Function<ITextComponent, IReorderingProcessor> textProcessorFunction) {
        if (this.renderText[row] == null && this.customName != null) {
            this.renderText[row] = textProcessorFunction.apply(this.customName);
        }

        return this.renderText[row];
    }


    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }


    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1024;
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

    public int getNumberOfItems() {

        int num = 0;
        for(int i = 0; i < this.itemHandler.getSlots(); i++)
        {
            if(this.itemHandler.getStackInSlot(i) != ItemStack.EMPTY)
                num++;
        }
        return num;

    }

    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.getPosX() - pos.getX();
        double deltaY = entity.getPosY() - pos.getY();
        double deltaZ = entity.getPosZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    @Override
    public ITextComponent getDisplayName() {
        return customName != null ? customName
                : new TranslationTextComponent("");
    }

    @Nonnull
    public ItemStack takeItemsFromSlot (int slot, int count) {

        ItemStack stack = this.itemHandler.getStackInSlot(slot).copy();
        stack.setCount(Math.min(count, this.itemHandler.getStackInSlot(slot).getMaxStackSize()));
        this.itemHandler.getStackInSlot(slot).setCount(this.itemHandler.getStackInSlot(slot).getCount() - stack.getCount());

        return stack;
    }

    public int putItemsIntoSlot (int slot, @Nonnull ItemStack stack, int count) {
        if (this.itemHandler.getContents().get(0).isEmpty()) {
            this.itemHandler.insertItem(0, stack.copy(), false);
//            this.itemHandler.getStackInSlot(0).setCount(count);
            markDirty();
            stack.shrink(count);
            return count;
        }

        if (this.itemHandler.getContents().get(0).getItem() != stack.getItem())
            return 0;

        int countAdded = Math.min(count, stack.getCount());
        countAdded = Math.min(countAdded, 1024 - this.itemHandler.getContents().get(0).getCount());

        this.itemHandler.getContents().get(0).setCount(this.itemHandler.getContents().get(0).getCount() + countAdded);
        stack.shrink(countAdded);

        return countAdded;
    }


    @OnlyIn(Dist.CLIENT)
    public void clientUpdateCount (final int slot, final int count) {
        if (!getWorld().isRemote)
            return;
        Minecraft.getInstance().enqueue(() -> HerbJarTile.this.clientUpdateCountAsync(slot, count));
    }

    @OnlyIn(Dist.CLIENT)
    private void clientUpdateCountAsync (int slot, int count) {
//        System.out.println("Message received. " + this.itemHandler.getStackInSlot(0).getCount() + " : " + count);
        if (this.itemHandler.getStackInSlot(0).getCount() != count){
            ItemStack newStack = this.itemHandler.getStackInSlot(0).copy();
//            newStack.setCount(count);
//            System.out.println(newStack);
            this.itemHandler.setStackInSlot(0, newStack);

        }

//        System.out.println("Message received. " + this.itemHandler.getStackInSlot(0).getCount() + " : " + count);

    }

    protected void syncClientCount (int slot, int count) {
        if (getWorld() != null && getWorld().isRemote)
            return;

        PacketDistributor.TargetPoint point = new PacketDistributor.TargetPoint(
                getPos().getX(), getPos().getY(), getPos().getZ(), 500, getWorld().getDimensionKey());
        HexereiPacketHandler.instance.send(PacketDistributor.NEAR.with(() -> point), new MessageCountUpdate(getPos(), slot, count));
    }



    public int interactPutItemsIntoSlot (PlayerEntity player) {
        int count;
        if (getWorld().getGameTime() - lastClickTime < 10 && player.getUniqueID().equals(lastClickUUID))
            count = interactPutCurrentInventoryIntoSlot(0, player);
        else
            count = interactPutCurrentItemIntoSlot(0, player);

        lastClickTime = getWorld().getGameTime();
        lastClickUUID = player.getUniqueID();

        return count;
    }

    public int interactPutCurrentItemIntoSlot (int slot, PlayerEntity player) {

        int count = 0;
        ItemStack playerStack = player.inventory.getCurrentItem();
        if (!playerStack.isEmpty())
            count = putItemsIntoSlot(slot, playerStack, playerStack.getCount());

        return count;
    }


    public int interactPutCurrentInventoryIntoSlot (int slot, PlayerEntity player) {
        int count = 0;
        if (!this.itemHandler.getContents().get(0).isEmpty()) {
            for (int i = 0, n = player.inventory.getSizeInventory(); i < n; i++) {
                ItemStack subStack = player.inventory.getStackInSlot(i);
                if (!subStack.isEmpty()) {
                    int subCount = putItemsIntoSlot(slot, subStack, subStack.getCount());
                    if (subCount > 0 && subStack.getCount() == 0)
                        player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);

                    count += subCount;
                }
            }
        }

        if (count > 0)
            if (player instanceof ServerPlayerEntity)
                ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);

        return count;
    }

    @Override
    public ITextComponent getCustomName() {
        return this.customName;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public ITextComponent getName() {
        return customName;
    }

    public int getDegreesOpened() {
        return this.degreesOpened;
    }
    public void setDegreesOpened(int degrees) {
        this.degreesOpened =  degrees;
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.items, false);
        }

        return compound;
    }


    @Override
    public void tick() {
        if(world.isRemote)
            return;

        boolean flag = false;
        PlayerList list = ServerLifecycleHooks.getCurrentServer().getPlayerList();

//        for(int i = 0; i < list.getPlayers().size(); i++)
//        {
//            ServerPlayerEntity player = list.getPlayers().get(i);
//
//            if(Math.floor(getDistanceToEntity(player, this.pos)) < 4D)
//            {
//                int distanceFromSide = (lidOpenAmount/2)-Math.abs((lidOpenAmount/2)-this.degreesOpened);
//                flag = true;
//
//                if(this.degreesOpened + Math.floor(((double)distanceFromSide/(double)(lidOpenAmount/2)) * 6) + 2 < 112)
//                    degreesOpened += Math.floor(((double)distanceFromSide/(double)(lidOpenAmount/2)) * 6) + 2;
//                else
//                    degreesOpened = 112;
//                ((Coffer)this.getBlockState().getBlock()).setAngle(world, pos, this.getBlockState(), this.degreesOpened);
//                break;
//            }
//
//        }
//        if(!flag)
//        {
//
//            int distanceFromSide = (lidOpenAmount/2)-Math.abs((lidOpenAmount/2)-this.degreesOpened);
//
//            if(this.degreesOpened + Math.floor(((double)distanceFromSide/(double)(lidOpenAmount/2)) * 6) + 2 > 0)
//                degreesOpened -= Math.floor(((double)distanceFromSide/(double)(lidOpenAmount/2)) * 6) + 2;
//            else
//                degreesOpened = 0;
//            ((Coffer)this.getBlockState().getBlock()).setAngle(world, pos, this.getBlockState(), this.degreesOpened);
//
//        }
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

}

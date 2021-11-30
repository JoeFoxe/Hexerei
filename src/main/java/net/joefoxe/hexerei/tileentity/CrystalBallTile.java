package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.custom.Coffer;
import net.joefoxe.hexerei.block.custom.CrystalBall;
import net.joefoxe.hexerei.container.CofferContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrystalBallTile extends TileEntity implements ITickableTileEntity{

//    public final ItemStackHandler itemHandler = createHandler();
//    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
//    protected NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    public int degreesSpun;
    public int bobAmount;
    public float orbOffset;
    public float smallRingOffset;
    public float largeRingOffset;

    public CrystalBallTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);

        orbOffset = 0;
        smallRingOffset = 0;
        largeRingOffset = 0;
    }

//    public void readInventory(CompoundNBT compound) {
//        itemHandler.deserializeNBT(compound);
//    }

//    @Override
//    protected NonNullList<ItemStack> getItems() {
//        return this.items;
//    }

//    @Override
//    protected void setItems(NonNullList<ItemStack> itemsIn) {
//        this.items = itemsIn;
//    }

//    @Override
//    public void markDirty() {
//        super.markDirty();
//        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(),
//                Constants.BlockFlags.BLOCK_UPDATE);
//    }

//    @Override
//    protected ITextComponent getDefaultName() {
//        return new TranslationTextComponent("container." + Hexerei.MOD_ID + ".coffer");
//    }

    public CrystalBallTile() {
        this(ModTileEntities.CRYSTAL_BALL_TILE.get());
    }

//    @Override
//    public void read(BlockState state, CompoundNBT nbt) {
//        itemHandler.deserializeNBT(nbt.getCompound("inv"));
//        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
//        super.read(state, nbt);
//        if (nbt.contains("CustomName", 8))
//            this.customName = ITextComponent.Serializer.getComponentFromJson(nbt.getString("CustomName"));
//    }


//    @Override
//    public CompoundNBT write(CompoundNBT compound) {
//        compound.put("inv", itemHandler.serializeNBT());
//        if (this.customName != null)
//            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
//        ItemStackHelper.saveAllItems(compound, this.items);
//        return super.write(compound);
//    }
//
//    @Override
//    protected Container createMenu(int id, PlayerInventory player) {
//        return new CofferContainer(id, this.world, this.pos, player, player.player);
//    }
//
//
//    @Override
//    public void clear() {
//        super.clear();
//        this.items.clear();
//    }
//
//    @Override
//    public SUpdateTileEntityPacket getUpdatePacket() {
//        CompoundNBT nbt = new CompoundNBT();
//        this.write(nbt);
//
//        return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
//        this.read(this.world.getBlockState(this.pos) ,pkt.getNbtCompound());
//    }
//
//    @Override
//    public CompoundNBT getUpdateTag() {
//        return this.write(new CompoundNBT());
//    }
//
//
//    private ItemStackHandler createHandler() {
//        return new ItemStackHandler(36) {
//            @Override
//            protected void onContentsChanged(int slot) {
//                markDirty();
//            }
//
//            @Override
//            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
//                return true;
//            }
//
//            @Override
//            public int getSlotLimit(int slot) {
//                return 64;
//            }
//
//            @Nonnull
//            @Override
//            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
//                if(!isItemValid(slot, stack)) {
//                    return stack;
//                }
//
//                return super.insertItem(slot, stack, simulate);
//            }
//        };
//    }
//
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return handler.cast();
//        }
//        return super.getCapability(cap, side);
//    }
//
//    public Item getItemInSlot(int slot) {
//        return this.itemHandler.getStackInSlot(slot).getItem();
//    }
//
//    public int getNumberOfItems() {
//
//        int num = 0;
//        for(int i = 0; i < this.itemHandler.getSlots(); i++)
//        {
//            if(this.itemHandler.getStackInSlot(i) != ItemStack.EMPTY)
//                num++;
//        }
//        return num;
//
//    }

    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.getPosX() - pos.getX();
        double deltaY = entity.getPosY() - pos.getY();
        double deltaZ = entity.getPosZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

//    @Override
//    public ITextComponent getDisplayName() {
//        return customName != null ? customName
//                : new TranslationTextComponent("");
//    }
//
//    @Override
//    public ITextComponent getCustomName() {
//        return this.customName;
//    }
//
//    @Override
//    public boolean hasCustomName() {
//        return customName != null;
//    }
//
//    @Override
//    public ITextComponent getName() {
//        return customName;
//    }
//
//    public int getdegreesSpun() {
//        return this.degreesSpun;
//    }
//    public void setdegreesSpun(int degrees) {
//        this.degreesSpun =  degrees;
//    }
//
//    public int getButtonToggled() {
//        return this.buttonToggled;
//    }
//    public void setButtonToggled(int degrees) {
//        this.buttonToggled =  degrees;
//    }
//
//    public CompoundNBT saveToNbt(CompoundNBT compound) {
//        if (!this.checkLootAndWrite(compound)) {
//            ItemStackHelper.saveAllItems(compound, this.items, false);
//        }
//
//        return compound;
//    }
//

    private float moveTo(float input, float movedTo, float speed)
    {
        float distance = movedTo - input;

        if(Math.abs(distance) <= speed)
        {
            return movedTo;
        }

        if(distance > 0)
        {
            input += speed;
        } else {
            input -= speed;
        }

        return input;
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
    public void tick() {
        if(world.isRemote) {
            float currentTime = this.getWorld().getGameTime();

            if(largeRingOffset > -7f) {
            if(degreesSpun + 1 < 112)
                degreesSpun += 1;
            else
                degreesSpun = 0;
            }

            if(((CrystalBall)this.getBlockState().getBlock()).getPlayerNear(this.getWorld(), this.getPos())) {
                orbOffset = moveTo(orbOffset, (float)Math.sin(Math.PI * (currentTime) / 10) / 4f, 0.25f) ;
                smallRingOffset = moveTo(smallRingOffset, (float)Math.sin(Math.PI * (currentTime + 20) / 15) / 4f, 0.25f);
                largeRingOffset = moveTo(largeRingOffset, (float)Math.sin(Math.PI * (currentTime + 40) / 20) / 4f, 0.35f);

            } else {

                orbOffset = moveTo(orbOffset, -0.5f, 0.1f);
                smallRingOffset = moveTo(smallRingOffset, -4.5f, 0.25f);
                largeRingOffset = moveTo(largeRingOffset, -7f, 0.25f);
            }

            return;
        }
        boolean flag = false;
        PlayerList list = ServerLifecycleHooks.getCurrentServer().getPlayerList();

        for(int i = 0; i < list.getPlayers().size(); i++)
        {
            ServerPlayerEntity player = list.getPlayers().get(i);

            if(Math.floor(getDistanceToEntity(player, this.pos)) < 4D)
            {
                flag = true;

                ((CrystalBall)this.getBlockState().getBlock()).setPlayerNear(world, pos, this.getBlockState(), true);
                break;
            }

        }
        if(!flag)
        {

            ((CrystalBall)this.getBlockState().getBlock()).setPlayerNear(world, pos, this.getBlockState(), false);

        }
    }

//    @Override
//    public int getSizeInventory() {
//        return 0;
//    }

}

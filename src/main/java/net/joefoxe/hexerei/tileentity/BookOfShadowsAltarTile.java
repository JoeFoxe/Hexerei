package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.block.custom.CrystalBall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.lwjgl.system.CallbackI;

public class BookOfShadowsAltarTile extends TileEntity implements ITickableTileEntity{

//    public final ItemStackHandler itemHandler = createHandler();
//    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
//    protected NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    public int degreesSpun;
    public float degreesOpened;
    public float degreesFlopped;
    public float pageOneRotation;
    public float pageTwoRotation;
    public float numberOfCandles;
    public float maxCandles = 3;
    public BlockPos candlePos1;
    public BlockPos candlePos2;
    public BlockPos candlePos3;
    public int candlePos1Slot;
    public int candlePos2Slot;
    public int candlePos3Slot;
    public float degreesSpunCandles;

    public Vector3d closestPlayerPos;
    public double closestDist;

    public final double maxDist = 8;

    public BookOfShadowsAltarTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);

        pageOneRotation = 0;
        pageTwoRotation = 0;
        degreesFlopped = 90;
        degreesOpened = 90; // reversed because the model is made so the book is opened from the start so adding 90 degrees from the start will close the book
        degreesSpun = 0;
        candlePos1Slot = 0;
        candlePos2Slot = 0;
        candlePos3Slot = 0;

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

    public BookOfShadowsAltarTile() {
        this(ModTileEntities.BOOK_OF_SHADOWS_ALTAR_TILE.get());
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
        double deltaX = entity.getPositionVec().getX() - pos.getX() - 0.5f;
        double deltaY = entity.getPositionVec().getY() - pos.getY() - 0.5f;
        double deltaZ = entity.getPositionVec().getZ() - pos.getZ() - 0.5f;

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    public static double getDistance(float x1, float y1, float x2, float y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
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

    private float moveTo(float input, float moveTo, float speed)
    {
        float distance = moveTo - input;

        if(Math.abs(distance) <= speed)
        {
            return moveTo;
        }

        if(distance > 0)
        {
            input += speed;
        } else {
            input -= speed;
        }

        return input;
    }

    private float moveToAngle(float input, float movedTo, float speed)
    {
        float distance = movedTo - input;

        if(Math.abs(distance) <= speed)
        {
            return movedTo;
        }

        if(distance > 0)
        {
            if(Math.abs(distance) < 180)
                input += speed;
            else
                input -= speed;
        } else {
            if(Math.abs(distance) < 180)
                input -= speed;
            else
                input += speed;
        }

        if(input < -90){
            input += 360;
        }
        if(input > 270)
            input -= 360;

        return input;
    }

    public float getAngle(Vector3d pos) {
        float angle = (float) Math.toDegrees(Math.atan2(pos.getZ() - this.getPos().getZ() - 0.5f, pos.getX() - this.getPos().getX() - 0.5f));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    private boolean getCandle(World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof CandleTile;
    }

    @Override
    public void tick() {
        if(world.isRemote) {

            PlayerList list = ServerLifecycleHooks.getCurrentServer().getPlayerList();

            closestPlayerPos = null;
            closestDist = maxDist;
            numberOfCandles = 0;

            candlePos1 = new BlockPos(0, 0, 0);
            candlePos2 = new BlockPos(0, 0, 0);
            candlePos3 = new BlockPos(0, 0, 0);

            for(int k = -1; k <= 1; ++k) {
                for(int l = -1; l <= 1; ++l) {
                    if ((k != 0 || l != 0) && world.isAirBlock(pos.add(l, 0, k)) && world.isAirBlock(pos.add(l, 1, k))) {
                        if(getCandle(world, pos.add(l * 2, 0, k * 2)) && numberOfCandles < maxCandles)
                        {
                            for(int i = 0; i < ((CandleTile)world.getTileEntity(pos.add(l * 2, 0, k * 2))).numberOfCandles; i++) {

                                if ((i == 0 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k * 2))).candleLit1 == 1)
                                        || (i == 1 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k * 2))).candleLit2 == 1)
                                        || (i == 2 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k * 2))).candleLit3 == 1)
                                        || (i == 3 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k * 2))).candleLit4 == 1)) {
                                    if (numberOfCandles == 0) {
                                        candlePos1 = pos.add(l * 2, 0, k * 2);
                                        candlePos1Slot = i;
                                    }
                                    if (numberOfCandles == 1) {
                                        candlePos2 = pos.add(l * 2, 0, k * 2);
                                        candlePos2Slot = i;
                                    }
                                    if (numberOfCandles == 2) {
                                        candlePos3 = pos.add(l * 2, 0, k * 2);
                                        candlePos3Slot = i;
                                    }
                                    numberOfCandles++;
                                }
                            }

                        }
                        if(getCandle(world, pos.add(l * 2, 1, k * 2)) && numberOfCandles < maxCandles)
                        {

                            for(int i = 0; i < ((CandleTile)world.getTileEntity(pos.add(l * 2, 1, k * 2))).numberOfCandles; i++) {

                                if ((i == 0 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k * 2))).candleLit1 == 1)
                                        || (i == 1 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k * 2))).candleLit2 == 1)
                                        || (i == 2 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k * 2))).candleLit3 == 1)
                                        || (i == 3 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k * 2))).candleLit4 == 1)) {
                                    if (numberOfCandles == 0) {
                                        candlePos1 = pos.add(l * 2, 1, k * 2);
                                        candlePos1Slot = i;
                                    }
                                    if (numberOfCandles == 1) {
                                        candlePos2 = pos.add(l * 2, 1, k * 2);
                                        candlePos2Slot = i;
                                    }
                                    if (numberOfCandles == 2) {
                                        candlePos3 = pos.add(l * 2, 1, k * 2);
                                        candlePos3Slot = i;
                                    }
                                    numberOfCandles++;
                                }
                            }
                        }

                        if (l != 0 && k != 0) {

                            if(getCandle(world, pos.add(l * 2, 0, k)) && numberOfCandles < maxCandles)
                            {

                                for(int i = 0; i < ((CandleTile)world.getTileEntity(pos.add(l * 2, 0, k))).numberOfCandles; i++) {

                                    if ((i == 0 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k))).candleLit1 == 1)
                                            || (i == 1 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k))).candleLit2 == 1)
                                            || (i == 2 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k))).candleLit3 == 1)
                                            || (i == 3 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 0, k))).candleLit4 == 1)) {
                                        if (numberOfCandles == 0) {
                                            candlePos1 = pos.add(l * 2, 0, k);
                                            candlePos1Slot = i;
                                        }
                                        if (numberOfCandles == 1) {
                                            candlePos2 = pos.add(l * 2, 0, k);
                                            candlePos2Slot = i;
                                        }
                                        if (numberOfCandles == 2) {
                                            candlePos3 = pos.add(l * 2, 0, k);
                                            candlePos3Slot = i;
                                        }
                                        numberOfCandles++;
                                    }
                                }
                            }
                            if(getCandle(world, pos.add(l * 2, 1, k)) && numberOfCandles < maxCandles)
                            {

                                for(int i = 0; i < ((CandleTile)world.getTileEntity(pos.add(l * 2, 1, k))).numberOfCandles; i++) {

                                    if ((i == 0 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k))).candleLit1 == 1)
                                            || (i == 1 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k))).candleLit2 == 1)
                                            || (i == 2 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k))).candleLit3 == 1)
                                            || (i == 3 && ((CandleTile) world.getTileEntity(pos.add(l * 2, 1, k))).candleLit4 == 1)) {
                                        if (numberOfCandles == 0) {
                                            candlePos1 = pos.add(l * 2, 1, k);
                                            candlePos1Slot = i;
                                        }
                                        if (numberOfCandles == 1) {
                                            candlePos2 = pos.add(l * 2, 1, k);
                                            candlePos2Slot = i;
                                        }
                                        if (numberOfCandles == 2) {
                                            candlePos3 = pos.add(l * 2, 1, k);
                                            candlePos3Slot = i;
                                        }
                                        numberOfCandles++;
                                    }
                                }

                            }
                            if(getCandle(world, pos.add(l, 0, k * 2)) && numberOfCandles < maxCandles)
                            {

                                for(int i = 0; i < ((CandleTile)world.getTileEntity(pos.add(l, 0, k * 2))).numberOfCandles; i++) {

                                    if ((i == 0 && ((CandleTile) world.getTileEntity(pos.add(l, 0, k * 2))).candleLit1 == 1)
                                            || (i == 1 && ((CandleTile) world.getTileEntity(pos.add(l, 0, k * 2))).candleLit2 == 1)
                                            || (i == 2 && ((CandleTile) world.getTileEntity(pos.add(l, 0, k * 2))).candleLit3 == 1)
                                            || (i == 3 && ((CandleTile) world.getTileEntity(pos.add(l, 0, k * 2))).candleLit4 == 1)) {
                                        if (numberOfCandles == 0) {
                                            candlePos1 = pos.add(l, 0, k * 2);
                                            candlePos1Slot = i;
                                        }
                                        if (numberOfCandles == 1) {
                                            candlePos2 = pos.add(l, 0, k * 2);
                                            candlePos2Slot = i;
                                        }
                                        if (numberOfCandles == 2) {
                                            candlePos3 = pos.add(l, 0, k * 2);
                                            candlePos3Slot = i;
                                        }
                                        numberOfCandles++;
                                    }
                                }
                            }
                            if(getCandle(world, pos.add(l, 1, k * 2)) && numberOfCandles < maxCandles)
                            {

                                for(int i = 0; i < ((CandleTile)world.getTileEntity(pos.add(l, 1, k * 2))).numberOfCandles; i++) {

                                    if ((i == 0 && ((CandleTile) world.getTileEntity(pos.add(l, 1, k * 2))).candleLit1 == 1)
                                            || (i == 1 && ((CandleTile) world.getTileEntity(pos.add(l, 1, k * 2))).candleLit2 == 1)
                                            || (i == 2 && ((CandleTile) world.getTileEntity(pos.add(l, 1, k * 2))).candleLit3 == 1)
                                            || (i == 3 && ((CandleTile) world.getTileEntity(pos.add(l, 1, k * 2))).candleLit4 == 1)) {
                                        if (numberOfCandles == 0) {
                                            candlePos1 = pos.add(l, 1, k * 2);
                                            candlePos1Slot = i;
                                        }
                                        if (numberOfCandles == 1) {
                                            candlePos2 = pos.add(l, 1, k * 2);
                                            candlePos2Slot = i;
                                        }
                                        if (numberOfCandles == 2) {
                                            candlePos3 = pos.add(l, 1, k * 2);
                                            candlePos3Slot = i;
                                        }
                                        numberOfCandles++;
                                    }
                                }
                            }

                        }
                    }
                }
            }

//            System.out.println(candlePos1);
//            System.out.println(candlePos2);
//            System.out.println(candlePos3);
//            System.out.println(numberOfCandles);
//            System.out.println("-------");

            if(numberOfCandles>=0)

            if(numberOfCandles >= 1)
            {
                degreesSpunCandles = moveToAngle(degreesSpunCandles, degreesSpunCandles + 1, 0.025f);

                if(candlePos1Slot == 0) {

                    ((CandleTile) world.getTileEntity(candlePos1)).candleReturn1 = false;
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosX1 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosX1, (pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosX1) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosY1 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosY1, (pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosY1) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ1 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosZ1, (pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ1) / 3f));
                }
                if(candlePos1Slot == 1) {
                    ((CandleTile) world.getTileEntity(candlePos1)).candleReturn2 = false;
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosX2 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosX2, (pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosX2) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosY2 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosY2, (pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosY2) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ2 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosZ2, (pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ2) / 3f));
                }
                if(candlePos1Slot == 2) {
                    ((CandleTile) world.getTileEntity(candlePos1)).candleReturn3 = false;
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosX3 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosX3, (pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosX3) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosY3 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosY3, (pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosY3) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ3 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosZ3, (pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ3) / 3f));
                }
                if(candlePos1Slot == 3) {
                    ((CandleTile) world.getTileEntity(candlePos1)).candleReturn4 = false;
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosX4 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosX4, (pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos1.getX() + (float)Math.sin(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosX4) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosY4 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosY4, (pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos1.getY() + 1f + (float)Math.sin(world.getGameTime()/10f)/10) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosY4) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ4 = moveTo(((CandleTile) world.getTileEntity(candlePos1)).candlePosZ4, (pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos1.getZ() + (float)Math.cos(degreesSpunCandles) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos1)).candlePosZ4) / 3f));
                }
            }
            if(numberOfCandles >= 2)
            {
                if(candlePos2Slot == 0) {
                    ((CandleTile) world.getTileEntity(candlePos2)).candleReturn1 = false;
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosX1 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosX1, (pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosX1) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosY1 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosY1, (pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosY1) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ1 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosZ1, (pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ1) / 3f));
                }
                if(candlePos2Slot == 1) {
                    ((CandleTile) world.getTileEntity(candlePos2)).candleReturn2 = false;
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosX2 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosX2, (pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosX2) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosY2 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosY2, (pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosY2) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ2 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosZ2, (pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ2) / 3f));
                }
                if(candlePos2Slot == 2) {
                    ((CandleTile) world.getTileEntity(candlePos2)).candleReturn3 = false;
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosX3 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosX3, (pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosX3) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosY3 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosY3, (pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosY3) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ3 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosZ3, (pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ3) / 3f));
                }
                if(candlePos2Slot == 3) {
                    ((CandleTile) world.getTileEntity(candlePos2)).candleReturn4 = false;
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosX4 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosX4, (pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos2.getX() + (float)Math.sin(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosX4) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosY4 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosY4, (pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos2.getY() + 1f + (float)Math.sin((world.getGameTime() + 10)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosY4) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ4 = moveTo(((CandleTile) world.getTileEntity(candlePos2)).candlePosZ4, (pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos2.getZ() + (float)Math.cos(degreesSpunCandles + (numberOfCandles == 2 ? Math.PI : Math.PI*2f/3f)) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos2)).candlePosZ4) / 3f));
                }
            }
            if(numberOfCandles >= 3)
            {
                if(candlePos3Slot == 0) {
                    ((CandleTile) world.getTileEntity(candlePos3)).candleReturn1 = false;
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosX1 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosX1, (pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosX1) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosY1 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosY1, (pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosY1) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ1 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosZ1, (pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ1) / 3f));
                }
                if(candlePos3Slot == 1) {
                    ((CandleTile) world.getTileEntity(candlePos3)).candleReturn2 = false;
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosX2 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosX2, (pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosX2) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosY2 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosY2, (pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosY2) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ2 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosZ2, (pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ2) / 3f));
                }
                if(candlePos3Slot == 2) {
                    ((CandleTile) world.getTileEntity(candlePos3)).candleReturn3 = false;
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosX3 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosX3, (pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosX3) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosY3 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosY3, (pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosY3) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ3 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosZ3, (pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ3) / 3f));
                }
                if(candlePos3Slot == 3) {
                    ((CandleTile) world.getTileEntity(candlePos3)).candleReturn4 = false;
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosX4 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosX4, (pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getX() - candlePos3.getX() + (float)Math.sin(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosX4) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosY4 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosY4, (pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10), 0.02f + 0.08f * (Math.abs((pos.getY() - candlePos3.getY() + 1f + (float)Math.sin((world.getGameTime() + 20)/10f)/10) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosY4) / 3f));
                    ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ4 = moveTo(((CandleTile) world.getTileEntity(candlePos3)).candlePosZ4, (pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + Math.PI*2f/3f*2f) * 1.25f), 0.02f + 0.20f * (Math.abs((pos.getZ() - candlePos3.getZ() + (float)Math.cos(degreesSpunCandles + (Math.PI*2f/3f*2f) * 1.25f) * 1.25f) - ((CandleTile) world.getTileEntity(candlePos3)).candlePosZ4) / 3f));
                }
            }





            for(int i = 0; i < list.getPlayers().size(); i++)
            {
                ServerPlayerEntity player = list.getPlayers().get(i);

                if(Math.floor(getDistanceToEntity(player, this.pos)) < maxDist)
                {

                    if(Math.floor(getDistanceToEntity(player, this.pos)) < closestDist) {
                        closestDist = (getDistanceToEntity(player, this.pos));
                        closestPlayerPos = player.getPositionVec();
                    }

                }

            }


            if(closestPlayerPos != null) {
                if(degreesFlopped == 0)
                    degreesSpun = (int)moveToAngle(degreesSpun, 270 - (int)getAngle(closestPlayerPos), 3);

                degreesFlopped = moveTo(degreesFlopped, 0, 5);
            }
            else
            {
                degreesFlopped = moveTo(degreesFlopped, 90, 3);
            }

            if(degreesFlopped == 0)
                degreesOpened = moveTo(degreesOpened, (float)(closestDist * (360 / maxDist))/4, 3);
            else
                degreesOpened = moveTo(degreesOpened, 90, 2);


        }

    }

//    @Override
//    public int getSizeInventory() {
//        return 0;
//    }

}

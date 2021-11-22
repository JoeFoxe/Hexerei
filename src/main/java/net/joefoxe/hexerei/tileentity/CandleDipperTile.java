package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.state.properties.LiquidType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.lwjgl.system.CallbackI;

import java.util.Objects;
import java.util.Random;

public class CandleDipperTile extends TileEntity implements ITickableTileEntity{

//    public final ItemStackHandler itemHandler = createHandler();
//    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
//    protected NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

//    public int degreesSpun;
//    public float degreesOpened;
//    public float degreesFlopped;
//    public float pageOneRotation;
//    public float pageTwoRotation;
    public float numberOfCandles;
    public Vector3d candlePos1;
    public Vector3d candlePos2;
    public Vector3d candlePos3;
    public int candlePos1Slot;
    public int candlePos2Slot;
    public int candlePos3Slot;
    public boolean candle1Dunking;
    public boolean candle2Dunking;
    public boolean candle3Dunking;
    public int candle1DippedTimes = 0;
    public int candle2DippedTimes = 0;
    public int candle3DippedTimes = 0;
    public int candle1DunkingCooldown = 60;
    public int candle2DunkingCooldown = 60;
    public int candle3DunkingCooldown = 60;
    public int candleDunkingCooldownMax = 200;
    public int candleDunkingCooldownStart = 60;
    public int candleDunkingTimeMax = 200;
    public int candle1DunkingTime = candleDunkingTimeMax;
    public int candle2DunkingTime = candleDunkingTimeMax;
    public int candle3DunkingTime = candleDunkingTimeMax;

    public Vector3d closestPlayerPos;
    public double closestDist;

    public final double maxDist = 8;

    public CandleDipperTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);

        candlePos1 = new Vector3d(0.5f,0.5f,0.5f);
        candlePos2 = new Vector3d(0.5f,0.5f,0.5f);
        candlePos3 = new Vector3d(0.5f,0.5f,0.5f);
        candle1Dunking = false;
        candle2Dunking = false;
        candle3Dunking = false;

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

    public CandleDipperTile() {
        this(ModTileEntities.CANDLE_DIPPER_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
//        itemHandler.deserializeNBT(nbt.getCompound("inv"));
//        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
//        super.read(state, nbt);
//        if (nbt.contains("CustomName", 8))
//            this.customName = ITextComponent.Serializer.getComponentFromJson(nbt.getString("CustomName"));
            if (nbt.contains("candlePos1Slot",  Constants.NBT.TAG_INT))
                candlePos1Slot = nbt.getInt("candlePos1Slot");
            if (nbt.contains("candlePos2Slot",  Constants.NBT.TAG_INT))
                candlePos2Slot = nbt.getInt("candlePos2Slot");
            if (nbt.contains("candlePos3Slot",  Constants.NBT.TAG_INT))
                candlePos3Slot = nbt.getInt("candlePos3Slot");
            if (nbt.contains("candle1DippedTimes",  Constants.NBT.TAG_INT))
                candle1DippedTimes = nbt.getInt("candle1DippedTimes");
            if (nbt.contains("candle2DippedTimes",  Constants.NBT.TAG_INT))
                candle2DippedTimes = nbt.getInt("candle2DippedTimes");
            if (nbt.contains("candle3DippedTimes",  Constants.NBT.TAG_INT))
                candle3DippedTimes = nbt.getInt("candle3DippedTimes");
            if (nbt.contains("candle1DunkingCooldown",  Constants.NBT.TAG_INT))
                candle1DunkingCooldown = nbt.getInt("candle1DunkingCooldown");
            if (nbt.contains("candle2DunkingCooldown",  Constants.NBT.TAG_INT))
                candle2DunkingCooldown = nbt.getInt("candle2DunkingCooldown");
            if (nbt.contains("candle3DunkingCooldown",  Constants.NBT.TAG_INT))
                candle3DunkingCooldown = nbt.getInt("candle3DunkingCooldown");
            if (nbt.contains("candle1DunkingTime",  Constants.NBT.TAG_INT))
                candle1DunkingTime = nbt.getInt("candle1DunkingTime");
            if (nbt.contains("candle2DunkingTime",  Constants.NBT.TAG_INT))
                candle2DunkingTime = nbt.getInt("candle2DunkingTime");
            if (nbt.contains("candle3DunkingTime",  Constants.NBT.TAG_INT))
                candle3DunkingTime = nbt.getInt("candle3DunkingTime");
            if (nbt.contains("candle1Dunking",  Constants.NBT.TAG_INT))
                candle1Dunking = nbt.getInt("candle1Dunking") == 1;
            if (nbt.contains("candle2Dunking",  Constants.NBT.TAG_INT))
                candle2Dunking = nbt.getInt("candle2Dunking") == 1;
            if (nbt.contains("candle3Dunking",  Constants.NBT.TAG_INT))
                candle3Dunking = nbt.getInt("candle3Dunking") == 1;
            super.read(state, nbt);

        }


    @Override
    public CompoundNBT write(CompoundNBT compound) {
//        compound.put("inv", itemHandler.serializeNBT());
//        if (this.customName != null)
//            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
//        ItemStackHelper.saveAllItems(compound, this.items);

        compound.putInt("candlePos1Slot", candlePos1Slot);
        compound.putInt("candlePos2Slot", candlePos2Slot);
        compound.putInt("candlePos3Slot", candlePos3Slot);

        compound.putInt("candle1DippedTimes", candle1DippedTimes);
        compound.putInt("candle2DippedTimes", candle2DippedTimes);
        compound.putInt("candle3DippedTimes", candle3DippedTimes);

        compound.putInt("candle1DunkingCooldown", candle1DunkingCooldown);
        compound.putInt("candle2DunkingCooldown", candle2DunkingCooldown);
        compound.putInt("candle3DunkingCooldown", candle3DunkingCooldown);

        compound.putInt("candle1DunkingTime", candle1DunkingTime);
        compound.putInt("candle2DunkingTime", candle2DunkingTime);
        compound.putInt("candle3DunkingTime", candle3DunkingTime);

        compound.putInt("candle1Dunking", candle1Dunking ? 1 : 0);
        compound.putInt("candle2Dunking", candle2Dunking ? 1 : 0);
        compound.putInt("candle3Dunking", candle3Dunking ? 1 : 0);

        return super.write(compound);
    }

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

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }



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

    public float getSpeed(double pos, double posTo) {
        return (float)(0.01f + 0.10f * (Math.abs(pos - posTo) / 3f));
    }

    public Vector3d rotateAroundVec(Vector3d vector3dCenter,float rotation,Vector3d vector3d)
    {
        Vector3d newVec = vector3d.subtract(vector3dCenter);
        newVec = newVec.rotateYaw(rotation/180f*(float)Math.PI);
        newVec = newVec.add(vector3dCenter);

        return newVec;
    }

    public int interactDipper (PlayerEntity player, BlockRayTraceResult hit) {
        if(player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.STRING)
        {

            Random rand = new Random();
            if(candlePos1Slot == 0) {
                candlePos1Slot = 1;
                candle1DunkingCooldown = candleDunkingCooldownStart;
                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 1.0F);
                if (!player.abilities.isCreativeMode)
                    player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                return 1;
            }
            else if(candlePos2Slot == 0) {
                candlePos2Slot = 1;
                candle2DunkingCooldown = candleDunkingCooldownStart;
                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 1.0F);
                if (!player.abilities.isCreativeMode)
                    player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                return 1;
            }
            else if(candlePos3Slot == 0) {
                candlePos3Slot = 1;
                candle3DunkingCooldown = candleDunkingCooldownStart;
                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 1.0F);
                if (!player.abilities.isCreativeMode)
                    player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                return 1;
            }
        } else if(player.getHeldItem(Hand.MAIN_HAND).isEmpty())
        {

            Random rand = new Random();
            if(candlePos1Slot != 0 && candle1DippedTimes >= 3) {
                candle1DippedTimes = 0;
                candlePos1Slot = 0;
                candle1DunkingTime = candleDunkingTimeMax;
                candle1Dunking = false;
                candle1DunkingCooldown = candleDunkingCooldownMax;
                player.inventory.placeItemBackInInventory(world,new ItemStack(ModBlocks.CANDLE.get()));

                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 1.0F);
                return 1;

            }
            else if(candlePos2Slot != 0 && candle2DippedTimes >= 3) {
                candle2DippedTimes = 0;
                candlePos2Slot = 0;
                candle2DunkingTime = candleDunkingTimeMax;
                candle2Dunking = false;
                candle2DunkingCooldown = candleDunkingCooldownMax;
                player.inventory.placeItemBackInInventory(world,new ItemStack(ModBlocks.CANDLE.get()));

                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 1.0F);
                return 1;

            }
            else if(candlePos3Slot != 0 && candle3DippedTimes >= 3) {
                candle3DippedTimes = 0;
                candlePos3Slot = 0;
                candle3DunkingTime = candleDunkingTimeMax;
                candle3Dunking = false;
                candle3DunkingCooldown = candleDunkingCooldownMax;
                player.inventory.placeItemBackInInventory(world,new ItemStack(ModBlocks.CANDLE.get()));

                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 1.0F);
                return 1;

            }


        }

        return 0;
    }

    @Override
    public void tick() {

            closestPlayerPos = null;
            closestDist = maxDist;
            numberOfCandles = 0;

            Vector3d targetPos1 = new Vector3d(4f / 16f, 0f / 16f, 1f / 16f);
            Vector3d targetPos2 = new Vector3d(8f / 16f, 0f / 16f, 1f / 16f);
            Vector3d targetPos3 = new Vector3d(12f / 16f, 0f / 16f, 1f / 16f);


            BlockState blockState = world.getBlockState(this.pos.down());
            if(blockState.getBlock() instanceof MixingCauldron) {
                if (candlePos1Slot > 0 && candle1DippedTimes < 3) {
                    if (blockState.get(MixingCauldron.LEVEL) > 0 && blockState.get(MixingCauldron.FLUID) == LiquidType.TALLOW)
                        candle1DunkingCooldown--;
                    if (candle1DunkingCooldown <= 0) {
                        candle1DunkingCooldown = candleDunkingCooldownMax;
                        candle1Dunking = true;
                    }
                    targetPos1 = new Vector3d(targetPos1.getX(), 5f / 16f + Math.sin((this.world.getGameTime()) / 16f) / 32f, 8f / 16f);
                }

                if (candlePos2Slot > 0 && candle2DippedTimes < 3) {
                    if (blockState.get(MixingCauldron.LEVEL) > 0 && blockState.get(MixingCauldron.FLUID) == LiquidType.TALLOW)
                        candle2DunkingCooldown--;
                    if (candle2DunkingCooldown <= 0) {
                        candle2DunkingCooldown = candleDunkingCooldownMax;
                        candle2Dunking = true;
                    }
                    targetPos2 = new Vector3d(targetPos2.getX(), 5f / 16f + Math.sin((this.world.getGameTime() + 20f) / 14f) / 32f, 8f / 16f);
                }
                if (candlePos3Slot > 0 && candle3DippedTimes < 3) {
                    if (blockState.get(MixingCauldron.LEVEL) > 0 && blockState.get(MixingCauldron.FLUID) == LiquidType.TALLOW)
                        candle3DunkingCooldown--;
                    if (candle3DunkingCooldown <= 0) {
                        candle3DunkingCooldown = candleDunkingCooldownMax;
                        candle3Dunking = true;
                    }
                    targetPos3 = new Vector3d(targetPos3.getX(), 5f / 16f + Math.sin((this.world.getGameTime() + 40f) / 15f) / 32f, 8f / 16f);
                }

                float dist;
                if (candle1Dunking) {
                    if (blockState.get(MixingCauldron.LEVEL) > 0 && blockState.get(MixingCauldron.FLUID) == LiquidType.TALLOW)
                        candle1DunkingTime--;
                    candle1DunkingCooldown = candleDunkingCooldownMax;
                    if (candle1DunkingTime <= 0) {
                        candle1DunkingTime = candleDunkingTimeMax;
                        candle1Dunking = false;
                        candle1DunkingCooldown = candleDunkingCooldownStart;
                        candle1DippedTimes++;
                        chanceDecreaseLevel(0.16f);
                    }
                    dist = (3 - blockState.get(MixingCauldron.LEVEL));
                    if (blockState.get(MixingCauldron.LEVEL) == 0)
                        dist = -2;
                    targetPos1 = new Vector3d(targetPos1.getX(), -(dist * 3f) / 16f + Math.sin((this.world.getGameTime()) / 16f) / 32f, 8f / 16f);
                }
                if (candle2Dunking) {
                    if (blockState.get(MixingCauldron.LEVEL) > 0 && blockState.get(MixingCauldron.FLUID) == LiquidType.TALLOW)
                        candle2DunkingTime--;
                    candle2DunkingCooldown = candleDunkingCooldownMax;
                    if (candle2DunkingTime <= 0) {
                        candle2DunkingTime = candleDunkingTimeMax;
                        candle2Dunking = false;
                        candle2DunkingCooldown = candleDunkingCooldownStart;
                        candle2DippedTimes++;
                        chanceDecreaseLevel(0.16f);
                    }
                    dist = (3 - blockState.get(MixingCauldron.LEVEL));
                    if (blockState.get(MixingCauldron.LEVEL) == 0)
                        dist = -2;
                    targetPos2 = new Vector3d(targetPos2.getX(), -(dist * 3f) / 16f + Math.sin((this.world.getGameTime() + 20f) / 14f) / 32f, 8f / 16f);
                }
                if (candle3Dunking) {
                    if (blockState.get(MixingCauldron.LEVEL) > 0 && blockState.get(MixingCauldron.FLUID) == LiquidType.TALLOW)
                        candle3DunkingTime--;
                    candle3DunkingCooldown = candleDunkingCooldownMax;
                    if (candle3DunkingTime <= 0) {
                        candle3DunkingTime = candleDunkingTimeMax;
                        candle3Dunking = false;
                        candle3DunkingCooldown = candleDunkingCooldownStart;
                        candle3DippedTimes++;
                        chanceDecreaseLevel(0.16f);
                    }
                    dist = (3 - blockState.get(MixingCauldron.LEVEL));
                    if (blockState.get(MixingCauldron.LEVEL) == 0)
                        dist = -2;
                    targetPos3 = new Vector3d(targetPos3.getX(), -(dist * 3f) / 16f + Math.sin((this.world.getGameTime() + 40f) / 15f) / 32f, 8f / 16f);
                }

                if (candle1DippedTimes >= 3)
                    targetPos1 = new Vector3d(targetPos1.getX(), 10f / 16f + Math.sin((this.world.getGameTime()) / 16f) / 32f, 8f / 16f);
                if (candle2DippedTimes >= 3)
                    targetPos2 = new Vector3d(targetPos2.getX(), 10f / 16f + Math.sin((this.world.getGameTime() + 20f) / 14f) / 32f, 8f / 16f);
                if (candle3DippedTimes >= 3)
                    targetPos3 = new Vector3d(targetPos3.getX(), 10f / 16f + Math.sin((this.world.getGameTime() + 40f) / 15f) / 32f, 8f / 16f);

                if (this.getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                    targetPos1 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 180, targetPos1);
                    targetPos2 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 180, targetPos2);
                    targetPos3 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 180, targetPos3);
                } else if (this.getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                    targetPos1 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 0, targetPos1);
                    targetPos2 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 0, targetPos2);
                    targetPos3 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 0, targetPos3);
                } else if (this.getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                    targetPos1 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 90, targetPos1);
                    targetPos2 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 90, targetPos2);
                    targetPos3 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 90, targetPos3);
                } else if (this.getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                    targetPos1 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 270, targetPos1);
                    targetPos2 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 270, targetPos2);
                    targetPos3 = rotateAroundVec(new Vector3d(0.5f, 0, 0.5f), 270, targetPos3);
                }
                candlePos1 = new Vector3d(
                        moveTo((float) candlePos1.x, (float) targetPos1.getX(), getSpeed((float) candlePos1.x, targetPos1.getX())),
                        moveTo((float) candlePos1.y, (float) targetPos1.getY(), 0.75f * getSpeed((float) candlePos1.y, targetPos1.getY())),
                        moveTo((float) candlePos1.z, (float) targetPos1.getZ(), getSpeed((float) candlePos1.z, targetPos1.getZ())));
                candlePos2 = new Vector3d(
                        moveTo((float) candlePos2.x, (float) targetPos2.getX(), getSpeed((float) candlePos2.x, targetPos2.getX())),
                        moveTo((float) candlePos2.y, (float) targetPos2.getY(), 0.75f * getSpeed((float) candlePos2.y, targetPos2.getY())),
                        moveTo((float) candlePos2.z, (float) targetPos2.getZ(), getSpeed((float) candlePos2.z, targetPos2.getZ())));
                candlePos3 = new Vector3d(
                        moveTo((float) candlePos3.x, (float) targetPos3.getX(), getSpeed((float) candlePos3.x, targetPos3.getX())),
                        moveTo((float) candlePos3.y, (float) targetPos3.getY(), 0.75f * getSpeed((float) candlePos3.y, targetPos3.getY())),
                        moveTo((float) candlePos3.z, (float) targetPos3.getZ(), getSpeed((float) candlePos3.z, targetPos3.getZ())));

            }


//            if(closestPlayerPos != null) {
//                if(degreesFlopped == 0)
//                    degreesSpun = (int)moveToAngle(degreesSpun, 270 - (int)getAngle(closestPlayerPos), 3);
//
//                degreesFlopped = moveTo(degreesFlopped, 0, 5);
//            }
//            else
//            {
//                degreesFlopped = moveTo(degreesFlopped, 90, 3);
//            }
//
//            if(degreesFlopped == 0)
//                degreesOpened = moveTo(degreesOpened, (float)(closestDist * (360 / maxDist))/4, 3);
//            else
//                degreesOpened = moveTo(degreesOpened, 90, 2);


    }

    private void chanceDecreaseLevel(float percent) {
        BlockState blockState = world.getBlockState(this.pos.down());
        Random random = new Random();

        if(blockState.getBlock() instanceof MixingCauldron)
        {
            if(random.nextFloat() <= percent)
                ((MixingCauldron)blockState.getBlock()).subtractLevel(world, this.pos.down());
        }


    }

//    @Override
//    public int getSizeInventory() {
//        return 0;
//    }

}

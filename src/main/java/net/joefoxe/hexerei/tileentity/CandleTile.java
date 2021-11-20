package net.joefoxe.hexerei.tileentity;

import net.joefoxe.hexerei.block.custom.Candle;
import net.joefoxe.hexerei.block.custom.CrystalBall;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Random;

public class CandleTile extends TileEntity implements ITickableTileEntity{

//    public final ItemStackHandler itemHandler = createHandler();
//    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
//    protected NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    public int candleType1;
    public int candleType2;
    public int candleType3;
    public int candleType4;
    public boolean candleReturn1;
    public boolean candleReturn2;
    public boolean candleReturn3;
    public boolean candleReturn4;
    public int numberOfCandles;
    public float candlePosX1;
    public float candlePosY1;
    public float candlePosZ1;
    public float candlePosX2;
    public float candlePosY2;
    public float candlePosZ2;
    public float candlePosX3;
    public float candlePosY3;
    public float candlePosZ3;
    public float candlePosX4;
    public float candlePosY4;
    public float candlePosZ4;
    public int candleHeight1;
    public int candleHeight2;
    public int candleHeight3;
    public int candleHeight4;
    public int candleLit1;
    public int candleLit2;
    public int candleLit3;
    public int candleLit4;
    public int candleMeltTimer1;
    public int candleMeltTimer2;
    public int candleMeltTimer3;
    public int candleMeltTimer4;
    public int candleMeltTimerMAX = 6000;
    private boolean startupFlag;

    public CandleTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);

        startupFlag = false;
        candleType1 = 0;
        candleType2 = 0;
        candleType3 = 0;
        candleType4 = 0;
        numberOfCandles = 0;
        candlePosX1 = 0;
        candlePosY1 = 0;
        candlePosZ1 = 0;
        candlePosX2 = 0;
        candlePosY2 = 0;
        candlePosZ2 = 0;
        candlePosX3 = 0;
        candlePosY3 = 0;
        candlePosZ3 = 0;
        candlePosX4 = 0;
        candlePosY4 = 0;
        candlePosZ4 = 0;
        candleLit1 = 0;
        candleLit2 = 0;
        candleLit3 = 0;
        candleLit4 = 0;
        candleMeltTimer1 = candleMeltTimerMAX;
        candleMeltTimer2 = candleMeltTimerMAX;
        candleMeltTimer3 = candleMeltTimerMAX;
        candleMeltTimer4 = candleMeltTimerMAX;
        candleReturn1 = false;
        candleReturn2 = false;
        candleReturn3 = false;
        candleReturn4 = false;
    }

    public CandleTile() {
        this(ModTileEntities.CANDLE_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {

        if (nbt.contains("candleType1",  Constants.NBT.TAG_INT))
            candleType1 = nbt.getInt("candleType1");
        if (nbt.contains("candleType2",  Constants.NBT.TAG_INT))
            candleType2 = nbt.getInt("candleType2");
        if (nbt.contains("candleType3",  Constants.NBT.TAG_INT))
            candleType3 = nbt.getInt("candleType3");
        if (nbt.contains("candleType4",  Constants.NBT.TAG_INT))
            candleType4 = nbt.getInt("candleType4");
        if (nbt.contains("candleHeight1",  Constants.NBT.TAG_INT))
            candleHeight1 = nbt.getInt("candleHeight1");
        if (nbt.contains("candleHeight2",  Constants.NBT.TAG_INT))
            candleHeight2 = nbt.getInt("candleHeight2");
        if (nbt.contains("candleHeight3",  Constants.NBT.TAG_INT))
            candleHeight3 = nbt.getInt("candleHeight3");
        if (nbt.contains("candleHeight4",  Constants.NBT.TAG_INT))
            candleHeight4 = nbt.getInt("candleHeight4");
        if (nbt.contains("candleLit1",  Constants.NBT.TAG_INT))
            candleLit1 = nbt.getInt("candleLit1");
        if (nbt.contains("candleLit2",  Constants.NBT.TAG_INT))
            candleLit2 = nbt.getInt("candleLit2");
        if (nbt.contains("candleLit3",  Constants.NBT.TAG_INT))
            candleLit3 = nbt.getInt("candleLit3");
        if (nbt.contains("candleLit4",  Constants.NBT.TAG_INT))
            candleLit4 = nbt.getInt("candleLit4");
        if (nbt.contains("candleMeltTimer1",  Constants.NBT.TAG_INT))
            candleMeltTimer1 = nbt.getInt("candleMeltTimer1");
        if (nbt.contains("candleMeltTimer2",  Constants.NBT.TAG_INT))
            candleMeltTimer2 = nbt.getInt("candleMeltTimer2");
        if (nbt.contains("candleMeltTimer3",  Constants.NBT.TAG_INT))
            candleMeltTimer3 = nbt.getInt("candleMeltTimer3");
        if (nbt.contains("candleMeltTimer4",  Constants.NBT.TAG_INT))
            candleMeltTimer4 = nbt.getInt("candleMeltTimer4");
        super.read(state, nbt);

    }


    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if(candleType1 != 0)
            compound.putInt("candleType1", candleType1);
        if(candleType2 != 0)
            compound.putInt("candleType2", candleType2);
        if(candleType3 != 0)
            compound.putInt("candleType3", candleType3);
        if(candleType4 != 0)
            compound.putInt("candleType4", candleType4);
        if(candleHeight1 != 0)
            compound.putInt("candleHeight1", candleHeight1);
        if(candleHeight2 != 0)
            compound.putInt("candleHeight2", candleHeight2);
        if(candleHeight3 != 0)
            compound.putInt("candleHeight3", candleHeight3);
        if(candleHeight4 != 0)
            compound.putInt("candleHeight4", candleHeight4);
        if(candleLit1 != 0)
            compound.putInt("candleLit1", candleLit1);
        if(candleLit2 != 0)
            compound.putInt("candleLit2", candleLit2);
        if(candleLit3 != 0)
            compound.putInt("candleLit3", candleLit3);
        if(candleLit4 != 0)
            compound.putInt("candleLit4", candleLit4);
        if(candleMeltTimer1 != 0)
            compound.putInt("candleMeltTimer1", candleMeltTimer1);
        if(candleMeltTimer2 != 0)
            compound.putInt("candleMeltTimer2", candleMeltTimer2);
        if(candleMeltTimer3 != 0)
            compound.putInt("candleMeltTimer3", candleMeltTimer3);
        if(candleMeltTimer4 != 0)
            compound.putInt("candleMeltTimer4", candleMeltTimer4);
        return super.write(compound);
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

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }



    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.getPosX() - pos.getX();
        double deltaY = entity.getPosY() - pos.getY();
        double deltaZ = entity.getPosZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }


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
        AxisAlignedBB aabb = super.getRenderBoundingBox().grow(25, 25, 25);
        return aabb;
    }

    @Override
    public void tick() {
        Random random = new Random();


            int candlesLit = 0;

            if(world.getTileEntity(pos) instanceof CandleTile)
            {
                if(candleLit1 == 1)
                    candlesLit++;
                if(candleLit2 == 1)
                    candlesLit++;
                if(candleLit3 == 1)
                    candlesLit++;
                if(candleLit4 == 1)
                    candlesLit++;
            }

            world.setBlockState(pos, world.getBlockState(pos).with(Candle.CANDLES_LIT, candlesLit), 3);


        if(world.isRemote) {
            if (candleReturn1) {
                candlePosX1 = moveTo(candlePosX1, 0, 0.125f);
                candlePosY1 = moveTo(candlePosY1, 0, 0.025f);
                candlePosZ1 = moveTo(candlePosZ1, 0, 0.125f);
            }
            if (candleReturn2) {
                candlePosX2 = moveTo(candlePosX2, 0, 0.125f);
                candlePosY2 = moveTo(candlePosY2, 0, 0.025f);
                candlePosZ2 = moveTo(candlePosZ2, 0, 0.125f);
            }
            if (candleReturn3) {
                candlePosX3 = moveTo(candlePosX3, 0, 0.125f);
                candlePosY3 = moveTo(candlePosY3, 0, 0.025f);
                candlePosZ3 = moveTo(candlePosZ3, 0, 0.125f);
            }
            if (candleReturn4) {
                candlePosX4 = moveTo(candlePosX4, 0, 0.125f);
                candlePosY4 = moveTo(candlePosY4, 0, 0.025f);
                candlePosZ4 = moveTo(candlePosZ4, 0, 0.125f);
            }
        }
        if(this.getBlockState().get(Candle.SLOT_ONE_TYPE) != null && !startupFlag) {
            candleType1 = this.getBlockState().get(Candle.SLOT_ONE_TYPE);
            candleHeight1 = 7;
            candleMeltTimer1 = candleMeltTimerMAX;
            startupFlag = true;
        }


        numberOfCandles = 0;

        if(candleType1 != 0)
            numberOfCandles++;
        if(candleType2 != 0)
            numberOfCandles++;
        if(candleType3 != 0)
            numberOfCandles++;
        if(candleType4 != 0)
            numberOfCandles++;


        if(candleType1 != 0)
        {

            if(candleLit1 != 0) {
                float xOffset = 0;
                float zOffset = 0;

                if (numberOfCandles == 4) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = 3f / 16f;
                        zOffset = 2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = -3f / 16f;
                        zOffset = -2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = -2f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = 2f / 16f;
                        zOffset = -3f / 16f;
                    }
                }
                else if (numberOfCandles == 3) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = -1f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = 1f / 16f;
                        zOffset = -3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = -3f / 16f;
                        zOffset = -1f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = 3f / 16f;
                        zOffset = 1f / 16f;
                    }
                }
                else if (numberOfCandles == 2) {

                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = 3f / 16f;
                        zOffset = -2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = -3f / 16f;
                        zOffset = 2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = 2f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = -2f / 16f;
                        zOffset = -3f / 16f;
                    }
                }
                else if (numberOfCandles == 1) {
                    xOffset = 0;
                    zOffset = 0;
                }

                if(!(candlePosX1 != 0 || candlePosY1 != 0 || candlePosZ1 != 0)) {


                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);

                } else
                {
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + candlePosX1, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f + candlePosY1, pos.getZ() + 0.5f + candlePosZ1, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + candlePosX1, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f + candlePosY1, pos.getZ() + 0.5f + candlePosZ1, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                }
                candleMeltTimer1--;
                if (candleMeltTimer1 <= 0) {
                    candleMeltTimer1 = candleMeltTimerMAX;
                    candleHeight1--;

                    if(!(candlePosX1 != 0 || candlePosY1 != 0 || candlePosZ1 != 0)) {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    } else
                    {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + candlePosX1, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f + candlePosY1, pos.getZ() + 0.5f + candlePosZ1, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    }


                    if (candleHeight1 <= 0) {
                        candleType1 = 0;
                        updateCandleSlots();
                        BlockState blockstate = this.getWorld().getBlockState(this.getPos());
                        if (!world.isRemote())
                            this.getWorld().setBlockState(this.getPos(), this.getBlockState().with(Candle.CANDLES, Integer.valueOf(Math.max(1, blockstate.get(Candle.CANDLES) - 1))));
                        world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);

                        if(!(candlePosX1 != 0 || candlePosY1 != 0 || candlePosZ1 != 0)) {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        } else
                        {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX1, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f + candlePosY1, pos.getZ() + 0.5f + candlePosZ1, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX1, pos.getY() + 3f / 16f + (float) candleHeight1 / 16f + candlePosY1, pos.getZ() + 0.5f + candlePosZ1, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        }

                        //candleHeight1 = 0;
                    }
                }
            }
        }
        if(candleType2 != 0)
        {

            if(candleLit2 != 0) {
                float xOffset = 0;
                float zOffset = 0;


//                if(tileEntityIn.numberOfCandles == 4)
//                    matrixStackIn.translate(-2f/16f , 0f/16f, -3f/16f);
//                else if(tileEntityIn.numberOfCandles == 3)
//                    matrixStackIn.translate(3f/16f , 0f/16f, 1f/16f);
//                else if(tileEntityIn.numberOfCandles == 2)
//                    matrixStackIn.translate(-3f/16f , 0f/16f, 3f/16f);
                if (numberOfCandles == 4) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = -2f / 16f;
                        zOffset = -3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = 2f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = 3f / 16f;
                        zOffset = -2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = -3f / 16f;
                        zOffset = 2f / 16f;
                    }
                }
                else if (numberOfCandles == 3) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = 3f / 16f;
                        zOffset = 1f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = -3f / 16f;
                        zOffset = -1f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = -1f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = 1f / 16f;
                        zOffset = -3f / 16f;
                    }
                }
                else if (numberOfCandles == 2) {

                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = -3f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = 3f / 16f;
                        zOffset = -3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = -3f / 16f;
                        zOffset = -3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = 3f / 16f;
                        zOffset = 3f / 16f;
                    }
                }
                else if (numberOfCandles == 1) {
                    xOffset = 0;
                    zOffset = 0;
                }

                if(!(candlePosX2 != 0 || candlePosY2 != 0 || candlePosZ2 != 0)) {


                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);

                } else
                {
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + candlePosX2, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f + candlePosY2, pos.getZ() + 0.5f + candlePosZ2, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + candlePosX2, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f + candlePosY2, pos.getZ() + 0.5f + candlePosZ2, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                }
                candleMeltTimer2--;
                if (candleMeltTimer2 <= 0) {
                    candleMeltTimer2 = candleMeltTimerMAX;
                    candleHeight2--;

                    if(!(candlePosX2 != 0 || candlePosY2 != 0 || candlePosZ2 != 0)) {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    } else
                    {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + candlePosX2, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f + candlePosY2, pos.getZ() + 0.5f + candlePosZ2, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    }


                    if (candleHeight2 <= 0) {
                        candleType2 = 0;
                        updateCandleSlots();
                        BlockState blockstate = this.getWorld().getBlockState(this.getPos());
                        if (!world.isRemote())
                            this.getWorld().setBlockState(this.getPos(), this.getBlockState().with(Candle.CANDLES, Integer.valueOf(Math.max(1, blockstate.get(Candle.CANDLES) - 1))));
                        world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);
                        if(!(candlePosX2 != 0 || candlePosY2 != 0 || candlePosZ2 != 0)) {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        } else
                        {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX2, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f + candlePosY2, pos.getZ() + 0.5f + candlePosZ2, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX2, pos.getY() + 3f / 16f + (float) candleHeight2 / 16f + candlePosY2, pos.getZ() + 0.5f + candlePosZ2, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        }
                        //candleHeight1 = 0;
                    }
                }
            }
        }
        if(candleType3 != 0)
        {

            if(candleLit3 != 0) {
                float xOffset = 0;
                float zOffset = 0;


                if (numberOfCandles == 4) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = -2f / 16f;
                        zOffset = 2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = 2f / 16f;
                        zOffset = -2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = 2f / 16f;
                        zOffset = 2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = -2f / 16f;
                        zOffset = -2f / 16f;
                    }
                }
                else if (numberOfCandles == 3) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = -2f / 16f;
                        zOffset = -3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = 2f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = 3f / 16f;
                        zOffset = -2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = -3f / 16f;
                        zOffset = 2f / 16f;
                    }
                }

                if(!(candlePosX3 != 0 || candlePosY3 != 0 || candlePosZ3 != 0)) {


                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);

                } else
                {
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + candlePosX3, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f + candlePosY3, pos.getZ() + 0.5f + candlePosZ3, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + candlePosX3, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f + candlePosY3, pos.getZ() + 0.5f + candlePosZ3, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                }
                candleMeltTimer3--;
                if (candleMeltTimer3 <= 0) {
                    candleMeltTimer3 = candleMeltTimerMAX;
                    candleHeight3--;

                    if(!(candlePosX3 != 0 || candlePosY3 != 0 || candlePosZ3 != 0)) {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    } else
                    {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + candlePosX3, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f + candlePosY3, pos.getZ() + 0.5f + candlePosZ3, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    }


                    if (candleHeight3 <= 0) {
                        candleType3 = 0;
                        updateCandleSlots();
                        BlockState blockstate = this.getWorld().getBlockState(this.getPos());
                        if (!world.isRemote())
                            this.getWorld().setBlockState(this.getPos(), this.getBlockState().with(Candle.CANDLES, Integer.valueOf(Math.max(1, blockstate.get(Candle.CANDLES) - 1))));
                        world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);
                        if(!(candlePosX3 != 0 || candlePosY3 != 0 || candlePosZ3 != 0)) {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        } else
                        {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX3, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f + candlePosY3, pos.getZ() + 0.5f + candlePosZ3, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX3, pos.getY() + 3f / 16f + (float) candleHeight3 / 16f + candlePosY3, pos.getZ() + 0.5f + candlePosZ3, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        }
                        //candleHeight1 = 0;
                    }
                }
            }
        }
        if(candleType4 != 0)
        {

            if(candleLit4 != 0) {
                float xOffset = 0;
                float zOffset = 0;


                if (numberOfCandles == 4) {
                    if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                        xOffset = 3f / 16f;
                        zOffset = -2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
                        xOffset = -3f / 16f;
                        zOffset = 2f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
                        xOffset = -2f / 16f;
                        zOffset = 3f / 16f;
                    }if (getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
                        xOffset = 2f / 16f;
                        zOffset = -3f / 16f;
                    }
                }

                if(!(candlePosX4 != 0 || candlePosY4 != 0 || candlePosZ4 != 0)) {


                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);

                } else
                {
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + candlePosX4, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f + candlePosY4, pos.getZ() + 0.5f + candlePosZ4, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    if (random.nextInt(10) == 0)
                        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + candlePosX4, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f + candlePosY4, pos.getZ() + 0.5f + candlePosZ4, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                }
                candleMeltTimer4--;
                if (candleMeltTimer4 <= 0) {
                    candleMeltTimer4 = candleMeltTimerMAX;
                    candleHeight4--;

                    if(!(candlePosX4 != 0 || candlePosY4 != 0 || candlePosZ4 != 0)) {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + xOffset, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    } else
                    {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f + candlePosX4, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f + candlePosY4, pos.getZ() + 0.5f + candlePosZ4, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.015d, (random.nextDouble() - 0.5d) / 50d);
                    }


                    if (candleHeight4 <= 0) {
                        candleType4 = 0;
                        updateCandleSlots();
                        BlockState blockstate = this.getWorld().getBlockState(this.getPos());
                        if (!world.isRemote())
                            this.getWorld().setBlockState(this.getPos(), this.getBlockState().with(Candle.CANDLES, Integer.valueOf(Math.max(1, blockstate.get(Candle.CANDLES) - 1))));
                        world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);
                        if(!(candlePosX4 != 0 || candlePosY4 != 0 || candlePosZ4 != 0)) {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + xOffset, pos.getY() + 0.2d, pos.getZ() + 0.5f + zOffset, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        } else
                        {
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX4, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f + candlePosY4, pos.getZ() + 0.5f + candlePosZ4, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos)), pos.getX() + 0.5f + candlePosX4, pos.getY() + 3f / 16f + (float) candleHeight4 / 16f + candlePosY4, pos.getZ() + 0.5f + candlePosZ4, (random.nextDouble() - 0.5d) / 50d, (random.nextDouble() + 0.5d) * 0.045d, (random.nextDouble() - 0.5d) / 50d);
                        }
                        //candleHeight1 = 0;
                    }
                }
            }
        }

        candleReturn1 = true;
        candleReturn2 = true;
        candleReturn3 = true;
        candleReturn4 = true;


        if(candleType1 == 0 && candleType2 == 0 && candleType3 == 0 && candleType4 == 0)
            this.getWorld().destroyBlock(this.getPos(), false);

        if(world.isRemote) {

            return;
        }

    }

    public void updateCandleSlots() {
        if(candleType1 == 0)
        {
            candleType1 = candleType2;
            candleHeight1 = candleHeight2;
            candleMeltTimer1 = candleMeltTimer2;
            candleLit1 = candleLit2;
            candleType2 = 0;
            candleLit2 = 0;
            candleHeight2 = 7;
            candleMeltTimer2 = candleMeltTimerMAX;
        }
        if(candleType2 == 0)
        {
            candleType2 = candleType3;
            candleHeight2 = candleHeight3;
            candleMeltTimer2 = candleMeltTimer3;
            candleLit2 = candleLit3;
            candleType3 = 0;
            candleLit3 = 0;
            candleHeight3 = 7;
            candleMeltTimer3 = candleMeltTimerMAX;
        }
        if(candleType3 == 0)
        {
            candleType3 = candleType4;
            candleHeight3 = candleHeight4;
            candleMeltTimer3 = candleMeltTimer4;
            candleLit3 = candleLit4;
            candleType4 = 0;
            candleLit4 = 0;
            candleHeight4 = 7;
            candleMeltTimer4 = candleMeltTimerMAX;
        }
    }


}

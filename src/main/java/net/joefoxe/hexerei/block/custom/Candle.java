package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.block.ITileEntity;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.tileentity.CandleTile;
import net.joefoxe.hexerei.tileentity.CofferTile;
import net.joefoxe.hexerei.tileentity.CrystalBallTile;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Candle extends Block implements ITileEntity<CandleTile>, IWaterLoggable {

    public static final IntegerProperty CANDLES = IntegerProperty.create("candles", 1, 4);
    public static final IntegerProperty CANDLES_LIT = IntegerProperty.create("candles_lit", 0, 4);
    public static final IntegerProperty SLOT_ONE_TYPE = IntegerProperty.create("slot_one_type", 0, 10);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty PLAYER_NEAR = BooleanProperty.create("player_near");

    protected static final VoxelShape FLAT_SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 0.0D, 14.0D);
    protected static final VoxelShape ONE_SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
    protected static final VoxelShape TWO_SHAPE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
    protected static final VoxelShape THREE_SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
    protected static final VoxelShape FOUR_SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);
    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getPos());
        int candleType = 1;
        if(context.getItem().getItem() == ModBlocks.CANDLE_BLUE.get().asItem())
            candleType = 2;
        if(context.getItem().getItem() == ModBlocks.CANDLE_BLACK.get().asItem())
            candleType = 3;
        if(context.getItem().getItem() == ModBlocks.CANDLE_LIME.get().asItem())
            candleType = 4;
        if(context.getItem().getItem() == ModBlocks.CANDLE_ORANGE.get().asItem())
            candleType = 5;
        if(context.getItem().getItem() == ModBlocks.CANDLE_PINK.get().asItem())
            candleType = 6;
        if(context.getItem().getItem() == ModBlocks.CANDLE_PURPLE.get().asItem())
            candleType = 7;
        if(context.getItem().getItem() == ModBlocks.CANDLE_RED.get().asItem())
            candleType = 8;
        if(context.getItem().getItem() == ModBlocks.CANDLE_CYAN.get().asItem())
            candleType = 9;
        if(context.getItem().getItem() == ModBlocks.CANDLE_YELLOW.get().asItem())
            candleType = 10;
        if (blockstate.matchesBlock(ModBlocks.CANDLE.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_BLUE.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_BLACK.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_LIME.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_ORANGE.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_PINK.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_PURPLE.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_RED.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_CYAN.get())
                || blockstate.matchesBlock(ModBlocks.CANDLE_YELLOW.get())) {


            if(context.getWorld().getTileEntity(context.getPos()) instanceof CandleTile)
            {
                CandleTile tile = (CandleTile) context.getWorld().getTileEntity(context.getPos());
                if(tile.candleType2 == 0) {
                    tile.candleType2 = candleType;
                    tile.candleHeight2 = 7;
                    tile.candleMeltTimer2 = tile.candleMeltTimerMAX;
                }
                else if(tile.candleType3 == 0) {
                    tile.candleType3 = candleType;
                    tile.candleHeight3 = 7;
                    tile.candleMeltTimer3 = tile.candleMeltTimerMAX;
                }
                else if(tile.candleType4 == 0) {
                    tile.candleType4 = candleType;
                    tile.candleHeight4 = 7;
                    tile.candleMeltTimer4 = tile.candleMeltTimerMAX;
                }
            }
            return blockstate.with(CANDLES, Integer.valueOf(Math.min(4, blockstate.get(CANDLES) + 1)));

        } else {
            FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
            boolean flag = fluidstate.getFluid() == Fluids.WATER;

            return super.getStateForPlacement(context).with(WATERLOGGED, Boolean.valueOf(flag)).with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing()).with(SLOT_ONE_TYPE, candleType).with(CANDLES_LIT, 0);
        }
    }


    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return (useContext.getItem().getItem() == ModBlocks.CANDLE.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_BLUE.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_BLACK.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_LIME.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_ORANGE.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_PINK.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_PURPLE.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_RED.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_CYAN.get().asItem()
                || useContext.getItem().getItem() == ModBlocks.CANDLE_YELLOW.get().asItem())
                && state.get(CANDLES) < 4 ? true : super.isReplaceable(state, useContext);
    }

    public void dropCandles(World world, BlockPos pos) {

        TileEntity entity = world.getTileEntity(pos);
        if(entity instanceof CandleTile && !world.isRemote()) {
            CandleTile candleTile = (CandleTile) entity;
            ItemStack itemStack = new ItemStack(ModBlocks.CANDLE.get());
            if(7 - candleTile.candleHeight1 < 1) {
                if (candleTile.candleType1 == 1) {
                    itemStack = new ItemStack(ModBlocks.CANDLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 2) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLUE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 3) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLACK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 4) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_LIME.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 5) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_ORANGE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 6) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PINK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 7) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PURPLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 8) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_RED.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 9) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_CYAN.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType1 == 10) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_YELLOW.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                }
            }


            if(7 - candleTile.candleHeight2 < 1) {
                if (candleTile.candleType2 == 1) {
                    itemStack = new ItemStack(ModBlocks.CANDLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 2) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLUE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 3) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLACK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 4) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_LIME.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 5) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_ORANGE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 6) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PINK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 7) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PURPLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 8) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_RED.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 9) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_CYAN.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType2 == 10) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_YELLOW.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                }
            }


            if(7 - candleTile.candleHeight3 < 1) {
                if (candleTile.candleType3 == 1) {
                    itemStack = new ItemStack(ModBlocks.CANDLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 2) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLUE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 3) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLACK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 4) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_LIME.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 5) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_ORANGE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 6) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PINK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 7) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PURPLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 8) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_RED.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 9) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_CYAN.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType3 == 10) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_YELLOW.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                }
            }


            if(7 - candleTile.candleHeight4 < 1) {
                if (candleTile.candleType4 == 1) {
                    itemStack = new ItemStack(ModBlocks.CANDLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 2) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLUE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 3) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_BLACK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 4) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_LIME.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 5) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_ORANGE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 6) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PINK.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 7) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_PURPLE.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 8) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_RED.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 9) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_CYAN.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                } else if (candleTile.candleType4 == 10) {
                    itemStack = new ItemStack(ModBlocks.CANDLE_YELLOW.get());
                    spawnAsEntity((ServerWorld) world, pos, itemStack);
                }
            }
        }
    }


    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        dropCandles(world, pos);

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(CANDLES)) {
            case 1:
            default:
                return ONE_SHAPE;
            case 2:
                return TWO_SHAPE;
            case 3:
                return THREE_SHAPE;
            case 4:
                return FOUR_SHAPE;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        Random random = new Random();
            if(itemstack.getItem() == Items.FLINT_AND_STEEL)
            {


                if (canBeLit(state, pos, worldIn)) {

                    if (((CandleTile) worldIn.getTileEntity(pos)).candleLit1 == 0)
                        ((CandleTile) worldIn.getTileEntity(pos)).candleLit1 = 1;
                    else if (((CandleTile) worldIn.getTileEntity(pos)).candleLit2 == 0)
                        ((CandleTile) worldIn.getTileEntity(pos)).candleLit2 = 1;
                    else if (((CandleTile) worldIn.getTileEntity(pos)).candleLit3 == 0)
                        ((CandleTile) worldIn.getTileEntity(pos)).candleLit3 = 1;
                    else if (((CandleTile) worldIn.getTileEntity(pos)).candleLit4 == 0)
                        ((CandleTile) worldIn.getTileEntity(pos)).candleLit4 = 1;
                    else
                        return ActionResultType.FAIL;

                    worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);
                    itemstack.damageItem(1, player, player1 -> player1.sendBreakAnimation(handIn));

                    return ActionResultType.func_233537_a_(worldIn.isRemote());
                }

            }
        return ActionResultType.PASS;
    }

    public Candle(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)).with(PLAYER_NEAR, Boolean.valueOf(false)).with(CANDLES_LIT, 0));
    }

    public static void spawnSmokeParticles(World worldIn, BlockPos pos, boolean spawnExtraSmoke) {
        Random random = worldIn.getRandom();
        BasicParticleType basicparticletype = ParticleTypes.SMOKE;
        worldIn.addOptionalParticle(basicparticletype, true, (double)pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + random.nextDouble() + random.nextDouble(), (double)pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (spawnExtraSmoke) {
            worldIn.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.25D + random.nextDouble() / 2.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + 0.4D, (double)pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }

    }


    public static void extinguish(IWorld world, BlockPos pos, BlockState state) {
        if (world.isRemote()) {
            for(int i = 0; i < 20; ++i) {
                spawnSmokeParticles((World)world, pos, true);
            }
        }

    }

    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
            CandleTile tile = ((CandleTile)worldIn.getTileEntity(pos));
            boolean flag = (tile.candleLit1 == 1 || tile.candleLit2 == 1 || tile.candleLit3 == 1 || tile.candleLit4 == 1);
            if (flag) {
                if (!worldIn.isRemote()) {
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                extinguish(worldIn, pos, state);
                tile.candleLit1 = 0;
                tile.candleLit2 = 0;
                tile.candleLit3 = 0;
                tile.candleLit4 = 0;

            }

            worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
            worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            return true;
        } else {
            return false;
        }
    }

    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
        if (projectile.isBurning()) {
            CandleTile tile = ((CandleTile)worldIn.getTileEntity(hit.getPos()));
            boolean flagLit = (tile.candleLit1 == 1 && tile.candleLit2 == 1 && tile.candleLit3 == 1 && tile.candleLit4 == 1);
            Entity entity = projectile.getShooter();
            boolean flag = entity == null || entity instanceof PlayerEntity || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entity);
            if (flag && !flagLit && !state.get(WATERLOGGED)) {
                if(tile.candleType1 != 0)
                    tile.candleLit1 = 1;
                if(tile.candleType2 != 0)
                    tile.candleLit2 = 1;
                if(tile.candleType3 != 0)
                    tile.candleLit3 = 1;
                if(tile.candleType4 != 0)
                    tile.candleLit4 = 1;
            }

        }

    }


    @SuppressWarnings("deprecation")
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, CANDLES, WATERLOGGED, PLAYER_NEAR, SLOT_ONE_TYPE, CANDLES_LIT);
    }

    public static boolean canBeLit(BlockState state, BlockPos pos, World world) {
        return !state.get(BlockStateProperties.WATERLOGGED) && (((CandleTile)world.getTileEntity(pos)).candleLit1 == 0 || (((CandleTile)world.getTileEntity(pos)).candleLit2 == 0 && ((CandleTile)world.getTileEntity(pos)).candleType2 != 0) || (((CandleTile)world.getTileEntity(pos)).candleLit3 == 0 && ((CandleTile)world.getTileEntity(pos)).candleType3 != 0) || (((CandleTile)world.getTileEntity(pos)).candleLit4 == 0 && ((CandleTile)world.getTileEntity(pos)).candleType4 != 0));
    }


    public void setPlayerNear(World worldIn, BlockPos pos, BlockState state, boolean near) {
        worldIn.setBlockState(pos, state.with(PLAYER_NEAR, near));
    }

    public boolean getPlayerNear(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).get(PLAYER_NEAR);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {

        if(!state.isValidPosition(world, pos))
        {
            if(!world.isRemote() && world instanceof ServerWorld) {
                dropCandles(((ServerWorld) world).getWorld(), pos);
            }
        }

        return !state.isValidPosition(world, pos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, pos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }


    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, world, pos, explosion);

        if (world instanceof ServerWorld) {
            if (world.getBlockState(pos) != state && !world.isRemote()) {
                dropCandles(world, pos);
            }

        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.candle_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.candle"));
        }
        super.addInformation(stack, world, tooltip, flagIn);
    }


//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
//
//        //world.addParticle(ParticleTypes.ENCHANT, pos.getX() + Math.round(rand.nextDouble()), pos.getY() + 1.2d, pos.getZ() + Math.round(rand.nextDouble()) , (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.035d ,(rand.nextDouble() - 0.5d) / 50d);
//
//        super.animateTick(state, world, pos, rand);
//    }
//
//    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
//        return new INamedContainerProvider() {
//            @Override
//            public ITextComponent getDisplayName() {
//                if(((CofferTile)worldIn.getTileEntity(pos)).customName != null)
//                    return new TranslationTextComponent(((CofferTile)worldIn.getTileEntity(pos)).customName.getString());
//                return new TranslationTextComponent("screen.hexerei.coffer");
//            }
//
//            @Nullable
//            @Override
//            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
//                return new CofferContainer(i, worldIn, pos, playerInventory, playerEntity);
//            }
//        };
//    }
//
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.CANDLE_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public Class<CandleTile> getTileEntityClass() {
        return CandleTile.class;
    }
}

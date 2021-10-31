package net.joefoxe.hexerei.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class Candelabra extends Block implements IWaterLoggable {

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

        for(Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockstate = this.getDefaultState().with(HANGING, Boolean.valueOf(direction == Direction.UP));
                if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
                    return blockstate.with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER)).with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
                }
            }
        }

        return null;
    }

    // hitbox REMEMBER TO DO THIS
    public static final VoxelShape GROUNDED_SHAPE = Stream.of(
            Block.makeCuboidShape(6.5, 11, 6.5, 9.5, 12, 9.5),
            Block.makeCuboidShape(1, 5.5, 7, 3, 7.5, 9),
            Block.makeCuboidShape(13, 8.5, 7, 15, 12.5, 9),
            Block.makeCuboidShape(6.5, 1, 6.5, 9.5, 2, 9.5),
            Block.makeCuboidShape(1, 8.5, 7, 3, 12.5, 9),
            Block.makeCuboidShape(3, 5.5, 7.01, 13, 6.5, 9.01),
            Block.makeCuboidShape(12.5, 7.5, 6.5, 15.5, 8.5, 9.5),
            Block.makeCuboidShape(7, 2, 7, 9, 11, 9),
            Block.makeCuboidShape(13, 5.5, 7, 15, 7.5, 9),
            Block.makeCuboidShape(6.5, 9.5, 12.5, 9.5, 10.5, 15.5),
            Block.makeCuboidShape(0.5, 7.5, 6.5, 3.5, 8.5, 9.5),
            Block.makeCuboidShape(7, 10.5, 13, 9, 14.5, 15),
            Block.makeCuboidShape(5.5, 0, 5.5, 10.5, 1, 10.5),
            Block.makeCuboidShape(7, 10.5, 1, 9, 14.5, 3),
            Block.makeCuboidShape(7, 12, 7, 9, 16, 9),
            Block.makeCuboidShape(7, 7.5, 13, 9, 9.5, 15),
            Block.makeCuboidShape(7.01, 7.5, 3, 9.01, 8.5, 13),
            Block.makeCuboidShape(7, 7.5, 1, 9, 9.5, 3),
            Block.makeCuboidShape(6.5, 9.5, 0.5, 9.5, 10.5, 3.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final VoxelShape HANGING_SHAPES = Stream.of(
            Block.makeCuboidShape(6.5, 11, 6.5, 9.5, 12, 9.5),
            Block.makeCuboidShape(1, 5.5, 7, 3, 7.5, 9),
            Block.makeCuboidShape(13, 8.5, 7, 15, 12.5, 9),
            Block.makeCuboidShape(1, 8.5, 7, 3, 12.5, 9),
            Block.makeCuboidShape(3, 5.5, 7.01, 13, 6.5, 9.01),
            Block.makeCuboidShape(12.5, 7.5, 6.5, 15.5, 8.5, 9.5),
            Block.makeCuboidShape(7, 2, 7, 9, 11, 9),
            Block.makeCuboidShape(7, 12, 7, 9, 16, 9),
            Block.makeCuboidShape(13, 5.5, 7, 15, 7.5, 9),
            Block.makeCuboidShape(6.5, 9.5, 12.5, 9.5, 10.5, 15.5),
            Block.makeCuboidShape(0.5, 7.5, 6.5, 3.5, 8.5, 9.5),
            Block.makeCuboidShape(7, 10.5, 13, 9, 14.5, 15),
            Block.makeCuboidShape(7, 10.5, 1, 9, 14.5, 3),
            Block.makeCuboidShape(7, 7.5, 13, 9, 9.5, 15),
            Block.makeCuboidShape(7.01, 7.5, 3, 9.01, 8.5, 13),
            Block.makeCuboidShape(7, 7.5, 1, 9, 9.5, 3),
            Block.makeCuboidShape(6.5, 9.5, 0.5, 9.5, 10.5, 3.5),
            Block.makeCuboidShape(7.5, 0, 7.5, 8.5, 2, 8.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final VoxelShape GROUNDED_SHAPE_TURNED =Stream.of(
            Block.makeCuboidShape(6.5, 11, 6.5, 9.5, 12, 9.5),
            Block.makeCuboidShape(7, 5.5, 13, 9, 7.5, 15),
            Block.makeCuboidShape(7, 8.5, 1, 9, 12.5, 3),
            Block.makeCuboidShape(6.5, 1, 6.5, 9.5, 2, 9.5),
            Block.makeCuboidShape(7, 8.5, 13, 9, 12.5, 15),
            Block.makeCuboidShape(7, 5.5, 3, 9.01, 6.5, 13),
            Block.makeCuboidShape(6.5, 7.5, 0.5, 9.5, 8.5, 3.5),
            Block.makeCuboidShape(7, 2, 7, 9, 11, 9),
            Block.makeCuboidShape(7, 5.5, 1, 9, 7.5, 3.),
            Block.makeCuboidShape(12.5, 9.5, 6.5, 15.5, 10.5, 9.5),
            Block.makeCuboidShape(6.5, 7.5, 12.5, 9.5, 8.5, 15.5),
            Block.makeCuboidShape(13, 10.5, 7, 15, 14.5, 9.),
            Block.makeCuboidShape(5.5, 0, 5.5, 10.5, 1, 10.5),
            Block.makeCuboidShape(1, 10.5, 7, 3, 14.5, 9),
            Block.makeCuboidShape(7, 12, 7, 9, 16, 9),
            Block.makeCuboidShape(13, 7.5, 7, 15, 9.5, 9),
            Block.makeCuboidShape(3, 7.5, 7, 13, 8.5, 9),
            Block.makeCuboidShape(1, 7.5, 7, 3, 9.5, 9),
            Block.makeCuboidShape(0.5, 9.5, 6.5, 3.5, 10.5, 9.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final VoxelShape HANGING_SHAPES_TURNED = Stream.of(
            Block.makeCuboidShape(6.5, 11, 6.5, 9.5, 12, 9.5),
            Block.makeCuboidShape(7, 5.5, 13.0, 9, 7.5, 15.0),
            Block.makeCuboidShape(7, 8.5, 1.03, 9, 12.5, 3.0),
            Block.makeCuboidShape(7, 8.5, 13.0, 9, 12.5, 15.0),
            Block.makeCuboidShape(7.01, 5.5, 3, 9, 6.5, 13.0),
            Block.makeCuboidShape(6.5, 7.5, 0.5, 9.5, 8.5, 3.5),
            Block.makeCuboidShape(7, 2, 7.0, 9, 11, 9.0),
            Block.makeCuboidShape(7, 12, 7.0, 9, 16, 9.0),
            Block.makeCuboidShape(7, 5.5, 1.03, 9, 7.5, 3),
            Block.makeCuboidShape(12.5, 9.5, 6.5, 15.5, 10.5, 9.5),
            Block.makeCuboidShape(6.5, 7.5, 12.5, 9.5, 8.5, 15.5),
            Block.makeCuboidShape(13, 10.5, 7.0, 15, 14.5, 9.0),
            Block.makeCuboidShape(1, 10.5, 7.0, 3, 14.5, 9.0),
            Block.makeCuboidShape(13, 7.5, 7.0, 15, 9.5, 9.0),
            Block.makeCuboidShape(3, 7.5, 7, 13, 8.5, 9),
            Block.makeCuboidShape(1, 7.5, 7.0, 3, 9.5, 9.0),
            Block.makeCuboidShape(0.5, 9.5, 6.5, 3.5, 10.5, 9.5),
            Block.makeCuboidShape(7.5, 0, 7.5, 8.5, 2, 8.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH || state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH ? (state.get(HANGING) ? HANGING_SHAPES : GROUNDED_SHAPE) : (state.get(HANGING) ? HANGING_SHAPES_TURNED : GROUNDED_SHAPE_TURNED);
    }


//    @SuppressWarnings("deprecation")
//    @Override
//    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        ItemStack itemstack = player.getHeldItem(handIn);
//        if(!worldIn.isRemote()) {
//
//            TileEntity tileEntity = worldIn.getTileEntity(pos);
//
//            if(tileEntity instanceof CofferTile) {
//                INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
//
//                NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getPos());
//
//            } else {
//                throw new IllegalStateException("Our Container provider is missing!");
//            }
//        }
//        return ActionResultType.SUCCESS;
//    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public Candelabra(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(HANGING, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, HANGING, WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = getBlockConnected(state).getOpposite();
        return Block.hasEnoughSolidSide(worldIn, pos.offset(direction), direction.getOpposite());
    }

    protected static Direction getBlockConnected(BlockState state) {
        return state.get(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

//    @Override
//    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
//        super.onBlockExploded(state, world, pos, explosion);
//
//        if (world instanceof ServerWorld) {
//            ItemStack cloneItemStack = getItem(world, pos, state);
//            if (world.getBlockState(pos) != state && !world.isRemote()) {
//                world.addEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, cloneItemStack));
//            }
//
//        }
//    }

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
//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//        TileEntity te = ModTileEntities.COFFER_TILE.get().create();
//        return te;
//    }
//
//    @Override
//    public boolean hasTileEntity(BlockState state) {
//        return true;
//    }
//
//    @Override
//    public Class<CofferTile> getTileEntityClass() {
//        return CofferTile.class;
//    }
}

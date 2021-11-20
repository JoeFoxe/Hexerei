package net.joefoxe.hexerei.block.custom;

import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class HerbDryingRack extends Block implements IWaterLoggable {


    public static final IntegerProperty ANGLE = IntegerProperty.create("angle", 0, 180);
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
        return this.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing()).with(ANGLE, 0).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
    }

    // hitbox REMEMBER TO DO THIS
    public static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(0.5, 5.5, 7.5, 15.5, 16, 8.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final VoxelShape SHAPE_TURNED = Stream.of(
            Block.makeCuboidShape(7.5, 5.5, 0.5, 8.5, 16, 15.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return p_220053_1_.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH || p_220053_1_.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH ? SHAPE : SHAPE_TURNED;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.herb_drying_rack_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.herb_drying_rack"));
        }
        super.addInformation(stack, world, tooltip, flagIn);
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

    public HerbDryingRack(Properties properties) {

        super(properties.notSolid());
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, ANGLE, WATERLOGGED);
    }

    public void setAngle(World worldIn, BlockPos pos, BlockState state, int angle) {
        worldIn.setBlockState(pos, state.with(ANGLE, Integer.valueOf(MathHelper.clamp(angle, 0, 180))), 2);
    }

    public int getAngle(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).get(ANGLE);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return !state.isValidPosition(world, pos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, pos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasEnoughSolidSide(worldIn, pos.up(), Direction.DOWN);
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

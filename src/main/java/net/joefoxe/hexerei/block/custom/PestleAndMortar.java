package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.block.ITileEntity;
import net.joefoxe.hexerei.container.CofferContainer;
import net.joefoxe.hexerei.tileentity.CofferTile;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class PestleAndMortar extends Block implements IWaterLoggable {


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
            Block.makeCuboidShape(3, 6, 3, 13, 7, 13),
            Block.makeCuboidShape(4, 2, 4, 12, 6, 12),
            Block.makeCuboidShape(5, 0, 5, 11, 2, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
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

    public PestleAndMortar(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)));
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

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
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
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.pestle_and_mortar_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.pestle_and_mortar"));
        }
        super.addInformation(stack, world, tooltip, flagIn);
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

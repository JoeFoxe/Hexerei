package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.block.ITileEntity;
import net.joefoxe.hexerei.tileentity.CandleDipperTile;
import net.joefoxe.hexerei.tileentity.HerbJarTile;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class CandleDipper extends Block implements ITileEntity<CandleDipperTile>, IWaterLoggable {

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

            if (this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) && context.getWorld().getBlockState(context.getPos().down()).getBlock() instanceof MixingCauldron) {
                return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER)).with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
        }
        return null;
    }



    // hitbox REMEMBER TO DO THIS
    public static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(14, 1, 4, 16, 6, 12),
            Block.makeCuboidShape(13, -1, 3.5, 17, 1, 6.5),
            Block.makeCuboidShape(13, -1, 9.5, 17, 1, 12.5),
            Block.makeCuboidShape(-1, -1, 9.5, 3, 1, 12.5),
            Block.makeCuboidShape(0, 1, 4, 2, 6, 12),
            Block.makeCuboidShape(-1, -1, 3.5, 3, 1, 6.5),
            Block.makeCuboidShape(2, -1, 2, 14, 0, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final VoxelShape SHAPE_TURNED = Stream.of(
            Block.makeCuboidShape(4, 1, 0, 12, 6, 2),
            Block.makeCuboidShape(3.5, -1, -1, 6.5, 1, 3),
            Block.makeCuboidShape(9.5, -1, -1, 12.5, 1, 3),
            Block.makeCuboidShape(9.5, -1, 13, 12.5, 1, 17),
            Block.makeCuboidShape(4, 1, 14, 12, 6, 16),
            Block.makeCuboidShape(3.5, -1, 13, 6.5, 1, 17),
            Block.makeCuboidShape(2, -1, 2, 14, 0, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH || state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH ? (SHAPE) : (SHAPE_TURNED);
    }




    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        Random random = new Random();
//        if(itemstack.getItem() == Items.STRING)
//        {
//
//            if(worldIn.getTileEntity(pos) instanceof CandleDipperTile) {
//                CandleDipperTile candleDipperTile = (CandleDipperTile) worldIn.getTileEntity(pos);
//                if(candleDipperTile.candlePos1Slot == 0)
//                    candleDipperTile.candlePos1Slot = 1;
//                else if(candleDipperTile.candlePos2Slot == 0)
//                    candleDipperTile.candlePos2Slot = 1;
//                else if(candleDipperTile.candlePos3Slot == 0)
//                    candleDipperTile.candlePos3Slot = 1;
//                else
//                    return ActionResultType.PASS;
//
////                worldIn.setBlockState(pos, state.with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
//                worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);
//
////                itemstack.damageItem(1, player, player1 -> player1.sendBreakAnimation(handIn));
//
//                return ActionResultType.func_233537_a_(worldIn.isRemote());
//            }
//
//
//        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof CandleDipperTile) {
            ((CandleDipperTile)tileEntity).interactDipper(player, hit);
            return ActionResultType.SUCCESS;
        }


        return ActionResultType.PASS;
    }


    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public CandleDipper(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, WATERLOGGED);
    }

//    @Override
//    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
//        if (!entityIn.isImmuneToFire() && state.get(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn)) {
//            entityIn.attackEntityFrom(DamageSource.IN_FIRE, 0.25f);
//        }
//
//        super.onEntityCollision(state, worldIn, pos, entityIn);
//    }

    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {

            worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
            worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            return true;
        } else {
            return false;
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return (worldIn.getBlockState(pos.down()).getBlock() instanceof MixingCauldron);
    }

    protected static Direction getBlockConnected(BlockState state) {
        return Direction.DOWN;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.candle_dipper_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.candle_dipper"));
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
//        if (state.get(LIT)) {
//            if (rand.nextInt(10) == 0) {
//                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat()/2, rand.nextFloat() * 0.7F + 0.6F, false);
//            }
//
//            if(!state.get(HANGING)) {
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 17f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 17f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//            }
//
//            if(state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST || state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//            }
//            if(state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH || state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
//                if (rand.nextInt(3) == 0)
//                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
//            }
//
//
//        }
    }
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
        TileEntity te = ModTileEntities.CANDLE_DIPPER_TILE.get().create();
        return te;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public Class<CandleDipperTile> getTileEntityClass() {
        return CandleDipperTile.class;
    }
}

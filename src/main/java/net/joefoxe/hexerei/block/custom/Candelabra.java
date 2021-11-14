package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.tileentity.CandleTile;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class Candelabra extends Block implements IWaterLoggable {

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

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
                    return blockstate.with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER)).with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing()).with(LIT, Boolean.valueOf(false));
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


    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        Random random = new Random();
        if(itemstack.getItem() == Items.FLINT_AND_STEEL)
        {
            if (canBeLit(state)) {

                worldIn.setBlockState(pos, state.with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
                worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 1.0F);
                itemstack.damageItem(1, player, player1 -> player1.sendBreakAnimation(handIn));

                return ActionResultType.func_233537_a_(worldIn.isRemote());
            }

        }
        return ActionResultType.PASS;
    }

    public static boolean canBeLit(BlockState state) {
        return !state.get(BlockStateProperties.WATERLOGGED) && !state.get(BlockStateProperties.LIT);
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public Candelabra(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(HANGING, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)).with(LIT, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, HANGING, WATERLOGGED, LIT);
    }

//    @Override
//    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
//        if (!entityIn.isImmuneToFire() && state.get(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn)) {
//            entityIn.attackEntityFrom(DamageSource.IN_FIRE, 0.25f);
//        }
//
//        super.onEntityCollision(state, worldIn, pos, entityIn);
//    }

    public static void extinguish(IWorld world, BlockPos pos, BlockState state) {
        if (world.isRemote()) {
            for(int i = 0; i < 20; ++i) {
                spawnSmokeParticles((World)world, pos, true);
            }
        }

        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof CampfireTileEntity) {
            ((CampfireTileEntity)tileentity).dropAllItems();
        }

    }

    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
            boolean flag = state.get(LIT);
            if (flag) {
                if (!worldIn.isRemote()) {
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                extinguish(worldIn, pos, state);
            }

            worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)).with(LIT, Boolean.valueOf(false)), 3);
            worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            return true;
        } else {
            return false;
        }
    }

    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
        if (!worldIn.isRemote && projectile.isBurning()) {
            Entity entity = projectile.getShooter();
            boolean flag = entity == null || entity instanceof PlayerEntity || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entity);
            if (flag && !state.get(LIT) && !state.get(WATERLOGGED)) {
                BlockPos blockpos = hit.getPos();
                worldIn.setBlockState(blockpos, state.with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            }
        }

    }

    public static void spawnSmokeParticles(World worldIn, BlockPos pos, boolean spawnExtraSmoke) {
        Random random = worldIn.getRandom();
        BasicParticleType basicparticletype = ParticleTypes.CAMPFIRE_COSY_SMOKE;
        worldIn.addOptionalParticle(basicparticletype, true, (double)pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + random.nextDouble() + random.nextDouble(), (double)pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (spawnExtraSmoke) {
            worldIn.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.25D + random.nextDouble() / 2.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + 0.4D, (double)pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }

    }

    public static boolean isLit(BlockState state) {
        return state.hasProperty(LIT) && state.get(LIT);
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.get(LIT)) {
            if (rand.nextInt(10) == 0) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat()/2, rand.nextFloat() * 0.7F + 0.6F, false);
            }

            if(!state.get(HANGING)) {
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 17f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 17f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
            }

            if(state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST || state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
            }
            if(state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH || state.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f + 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f, pos.getY() + 16f / 16f, pos.getZ() + 0.5f - 6f / 16f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f + 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);

                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.01d, (rand.nextDouble() - 0.5d) / 100d);
                if (rand.nextInt(3) == 0)
                    world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5f - 6f / 16f, pos.getY() + 14f / 16f, pos.getZ() + 0.5f, (rand.nextDouble() - 0.5d) / 100d, (rand.nextDouble() + 0.5d) * 0.035d, (rand.nextDouble() - 0.5d) / 100d);
            }


        }
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

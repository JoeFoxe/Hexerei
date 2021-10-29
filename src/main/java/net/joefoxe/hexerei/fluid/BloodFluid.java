package net.joefoxe.hexerei.fluid;

import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.particle.ModParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.*;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public abstract class BloodFluid extends ForgeFlowingFluid {

    @Override
    protected void randomTick(World world, BlockPos pos, FluidState state, Random random) {
        super.randomTick(world, pos, state, random);
    }

    @Override
    public void tick(World worldIn, BlockPos pos, FluidState state) {

        super.tick(worldIn, pos, state);
    }

    protected BloodFluid(Properties properties) {

        super(properties);
    }

    @Override
    public Fluid getFlowingFluid() {
        return ModFluids.BLOOD_FLOWING.get();
    }

    @Override
    public Fluid getStillFluid() {
        return ModFluids.BLOOD_FLUID.get();
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn, pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 2;
    }

    @Override
    public Item getFilledBucket() {
        return ModItems.BLOOD_BUCKET.get();
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100f;
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        return ModFluids.BLOOD_BLOCK.get().getDefaultState().with(FlowingFluidBlock.LEVEL, Integer.valueOf(getLevelFromState(state)));
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }


//    public boolean isEquivalentTo(Fluid fluidIn) {
//        return fluidIn == Fluids.WATER || fluidIn == Fluids.FLOWING_WATER;
//    }
//


    public static class Flowing extends ForgeFlowingFluid {
        protected Flowing(Properties properties) {
            super(properties);
        }

        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(FluidState state) {
            return false;
        }

        @OnlyIn(Dist.CLIENT)
        public void animateTick(World worldIn, BlockPos pos, FluidState state, Random random) {
            if (!state.isSource() && !state.get(FALLING)) {
                if (random.nextInt(64) == 0) {
                    worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
                }
            } else if (random.nextInt(20) == 0) {
                worldIn.addParticle(ModParticleTypes.BLOOD.get(), (double)pos.getX() + random.nextDouble(), (double)pos.getY() + (random.nextDouble() * (state.get(LEVEL_1_8) / 8)), (double)pos.getZ() + random.nextDouble(), -0.01D+(random.nextDouble()*0.02), -0.01D+(random.nextDouble()*0.02), -0.01D+(random.nextDouble()*0.02));
            }
            //worldIn.addParticle(ModParticleTypes.BLOOD.get(), (double)pos.getX() + random.nextDouble(), (double)pos.getY() + (random.nextDouble() * (state.get(LEVEL_1_8) / 8)), (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    public static class Source extends ForgeFlowingFluid {
        protected Source(Properties properties) {
            super(properties);
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }

        @OnlyIn(Dist.CLIENT)
        public void animateTick(World worldIn, BlockPos pos, FluidState state, Random random) {
            if (!state.isSource() && !state.get(FALLING)) {
                if (random.nextInt(64) == 0) {
                    worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
                }
            } else if (random.nextInt(10) == 0) {
                worldIn.addParticle(ModParticleTypes.BLOOD.get(), (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), -0.01D+(random.nextDouble()*0.02), -0.01D+(random.nextDouble()*0.02), -0.01D+(random.nextDouble()*0.02));
            }
            //worldIn.addParticle(ModParticleTypes.BLOOD.get(), (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

    }

}

package net.joefoxe.hexerei.block.custom;

import com.google.common.collect.Lists;
import net.joefoxe.hexerei.block.ITileEntity;
import net.joefoxe.hexerei.container.HerbJarContainer;
import net.joefoxe.hexerei.tileentity.HerbJarTile;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
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
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class HerbJar extends Block implements ITileEntity<HerbJarTile>, IWaterLoggable {

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
    public static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(5, -0.5, 5, 11, 0, 11),
            Block.makeCuboidShape(5.5, 13, 5.5, 10.5, 15, 10.5),
            Block.makeCuboidShape(4.5, 12, 10.5, 11.5, 14, 11.5),
            Block.makeCuboidShape(4.5, 12, 4.5, 11.5, 14, 5.5),
            Block.makeCuboidShape(4.5, 12, 5.5, 5.5, 14, 10.5),
            Block.makeCuboidShape(10.5, 12, 5.5, 11.5, 14, 10.5),
            Block.makeCuboidShape(4, 0, 4, 12, 11, 12),
            Block.makeCuboidShape(5, 11, 5, 11, 12, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);

        if ((itemstack.isEmpty() && player.isSneaking()) || state.get(HorizontalBlock.HORIZONTAL_FACING).getOpposite() != hit.getFace()) {

            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(!worldIn.isRemote()) {
                if (tileEntity instanceof HerbJarTile) {
                    INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
                    NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());
                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }

            return ActionResultType.SUCCESS;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof HerbJarTile) {
            ((HerbJarTile)tileEntity).interactPutItems(player);
        }

        return ActionResultType.SUCCESS;
    }


    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        ItemStack cloneItemStack = getItem(world, pos, state);
        if(!world.isRemote())
            spawnAsEntity((ServerWorld)world, pos, cloneItemStack);

        super.onBlockHarvested(world, pos, state, player);
    }

    protected BlockRayTraceResult rayTraceEyes(World world, PlayerEntity player, double length) {
        Vector3d eyePos = player.getEyePosition(1);
        Vector3d lookPos = player.getLook(1);
        Vector3d endPos = eyePos.add(lookPos.x * length, lookPos.y * length, lookPos.z * length);
        RayTraceContext context = new RayTraceContext(eyePos, endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player);
        return world.rayTraceBlocks(context);
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) {
        BlockRayTraceResult rayResult = rayTraceEyes(worldIn, playerIn, playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue() + 1);
        if (rayResult.getType() == RayTraceResult.Type.MISS)
            return;

        Direction side = rayResult.getFace();

        TileEntity tile = worldIn.getTileEntity(pos);
        HerbJarTile herbJarTile = null;
        //System.out.println(worldIn.isRemote());
        if(tile instanceof  HerbJarTile)
            herbJarTile = (HerbJarTile) tile;
        if (state.get(HorizontalBlock.HORIZONTAL_FACING).getOpposite() != rayResult.getFace())
            return;

        ItemStack item;
        if (playerIn.isSneaking()) {
            item = herbJarTile.takeItems(0, herbJarTile.itemHandler.getStackInSlot(0).getCount());
        }
        else {
            item = herbJarTile.takeItems(0, 1);
        }

        if (!item.isEmpty()) {
            if (!playerIn.inventory.addItemStackToInventory(item)) {
                dropItemStack(worldIn, pos.offset(side), playerIn, item);
                worldIn.notifyBlockUpdate(pos, state, state, 3);
            }
            else
                worldIn.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, .2f, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * .7f + 1) * 2);
        }

        super.onBlockClicked(state, worldIn, pos, playerIn);
    }

    private void dropItemStack (World world, BlockPos pos, PlayerEntity player, @Nonnull ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX() + .5f, pos.getY() + .3f, pos.getZ() + .5f, stack);
        Vector3d motion = entity.getMotion();
        entity.addVelocity(-motion.x, -motion.y, -motion.z);
        world.addEntity(entity);
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public HerbJar(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(HANGING, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, HANGING, WATERLOGGED);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, world, pos, explosion);

        if (world instanceof ServerWorld) {
            ItemStack cloneItemStack = getItem(world, pos, state);
            if (world.getBlockState(pos) != state && !world.isRemote()) {
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, cloneItemStack));
            }

        }
    }

//    @Override
//    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//
//        if(Screen.hasShiftDown()) {
//            tooltip.add(new TranslationTextComponent("tooltip.hexerei.herb_jar_shift"));
//        } else {
//            tooltip.add(new TranslationTextComponent("tooltip.hexerei.herb_jar"));
//        }
//        super.addInformation(stack, world, tooltip, flagIn);
//    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        CompoundNBT inv = stack.getOrCreateTag().getCompound("Inventory");
        ListNBT tagList = inv.getList("Items", Constants.NBT.TAG_COMPOUND);
        if(Screen.hasShiftDown()) {

            tooltip.add(new TranslationTextComponent("\u00A7e--------------\u00A7r"));
            for (int i = 0; i < tagList.size(); i++)
            {
                CompoundNBT itemTags = tagList.getCompound(i);
                itemTags.putInt("Count", 1);
                TranslationTextComponent itemText = new TranslationTextComponent(ItemStack.read(itemTags).getTranslationKey());
                int countText = Integer.parseInt(String.valueOf(itemTags.get("ExtendedCount")));
                itemText.appendString(" x" + countText);

                tooltip.add(itemText);
            }
            if(tagList.size() < 1)
            {
                tooltip.add(new TranslationTextComponent("Can be placed in the world."));
                tooltip.add(new TranslationTextComponent("Can store items and be moved like a shulker box."));
                tooltip.add(new TranslationTextComponent("Can be renamed."));
                tooltip.add(new TranslationTextComponent("Only holds one type of item, but can hold up to 1024."));
            }

        } else {
            tooltip.add(new TranslationTextComponent("\u00A7e--------------\u00A7r"));

            for (int i = 0; i < Math.min(tagList.size(), 1); i++)
            {
                CompoundNBT itemTags = tagList.getCompound(i);
                itemTags.putInt("Count", 1);
                TranslationTextComponent itemText = new TranslationTextComponent(ItemStack.read(itemTags).getTranslationKey());
                int countText = Integer.parseInt(String.valueOf(itemTags.get("ExtendedCount")));
                itemText.appendString(" x" + countText);

                tooltip.add(itemText);
            }
//            if(tagList.size() > 3) {
//                tooltip.add(new TranslationTextComponent(". . . "));
//                tooltip.add(new TranslationTextComponent(""));
//                tooltip.add(new TranslationTextComponent("Hold \u00A7eSHIFT\u00A7r to see more"));
//            }
//            else
            if(tagList.size() < 1)
            {

                tooltip.add(new TranslationTextComponent("tooltip.hexerei.herb_jar"));
            }
        }
        super.addInformation(stack, world, tooltip, flagIn);
    }


    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        ItemStack item = new ItemStack(this);
        Optional<HerbJarTile> tileEntityOptional = Optional.ofNullable(getTileEntity(worldIn, pos));
//        System.out.println(worldIn.getTileEntity(pos));
        CompoundNBT tag = item.getOrCreateTag();
        CompoundNBT inv = tileEntityOptional.map(herb_jar -> herb_jar.itemHandler.serializeNBT())
                .orElse(new CompoundNBT());
        tag.put("Inventory", inv);

        ITextComponent customName = tileEntityOptional.map(HerbJarTile::getCustomName)
                .orElse(null);
        if (customName != null)
            item.setDisplayName(customName);
        return item;
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            ((HerbJarTile)tileentity).customName = stack.getDisplayName();
        }

        if (worldIn.isRemote())
            return;
        if (stack == null)
            return;
        withTileEntityDo(worldIn, pos, te -> {
            te.readInventory(stack.getOrCreateTag()
                    .getCompound("Inventory"));
        });

    }

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
        if(!stateIn.isValidPosition(worldIn, currentPos))
        {
            if(!worldIn.isRemote()) {
                ItemStack cloneItemStack = getItem(worldIn, currentPos, stateIn);
                worldIn.addEntity(new ItemEntity(((ServerWorld) worldIn).getWorld(), currentPos.getX() + 0.5f, currentPos.getY() - 0.5f, currentPos.getZ() + 0.5f, cloneItemStack));
            }
        }

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
    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                if(((HerbJarTile)worldIn.getTileEntity(pos)).customName != null)
                    return new TranslationTextComponent(((HerbJarTile)worldIn.getTileEntity(pos)).customName.getString());
                return new TranslationTextComponent("screen.hexerei.herb_jar");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new HerbJarContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        TileEntity te = ModTileEntities.HERB_JAR_TILE.get().create();
        return te;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public Class<HerbJarTile> getTileEntityClass() {
        return HerbJarTile.class;
    }
}

package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.block.ITileEntity;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.container.CofferContainer;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.particle.ModParticleTypes;
import net.joefoxe.hexerei.state.properties.LiquidType;
import net.joefoxe.hexerei.tileentity.CofferTile;
import net.joefoxe.hexerei.tileentity.MixingCauldronTile;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
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
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class Coffer extends Block implements ITileEntity<CofferTile>, IWaterLoggable {


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
            Block.makeCuboidShape(2, 0, 4, 14, 4, 12)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final VoxelShape SHAPE_TURNED = Stream.of(
            Block.makeCuboidShape(4, 0, 2, 12, 4, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        if (p_220053_1_.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST || p_220053_1_.get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST)
            return SHAPE_TURNED;
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if(!worldIn.isRemote()) {

            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(tileEntity instanceof CofferTile) {
                INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

                NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getPos());

            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    public Coffer(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)));
    }

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
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (player instanceof FakePlayer)
            return;
        if (world instanceof ServerWorld) {
            ItemStack cloneItemStack = getItem(world, pos, state);
            world.destroyBlock(pos, false);
            if (world.getBlockState(pos) != state && !world.isRemote()) {
                if(player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.AIR)
                    player.setHeldItem(Hand.MAIN_HAND,cloneItemStack);
                else
                    player.inventory.placeItemBackInInventory(world, cloneItemStack);
            }

        }
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


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        ItemStack item = new ItemStack(this);
        Optional<CofferTile> tileEntityOptional = Optional.ofNullable(getTileEntity(worldIn, pos));

        CompoundNBT tag = item.getOrCreateTag();
        CompoundNBT inv = tileEntityOptional.map(coffer -> coffer.itemHandler.serializeNBT())
                .orElse(new CompoundNBT());
        tag.put("Inventory", inv);

        ITextComponent customName = tileEntityOptional.map(CofferTile::getCustomName)
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
            ((CofferTile)tileentity).customName = stack.getDisplayName();
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

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        CompoundNBT inv = stack.getOrCreateTag().getCompound("Inventory");
        ListNBT tagList = inv.getList("Items", Constants.NBT.TAG_COMPOUND);
        if(Screen.hasShiftDown()) {

            tooltip.add(new TranslationTextComponent("\u00A7e--------------\u00A7r"));
            for (int i = 0; i < tagList.size(); i++)
            {
                CompoundNBT itemTags = tagList.getCompound(i);

                TranslationTextComponent itemText = new TranslationTextComponent(ItemStack.read(itemTags).getTranslationKey());
                int countText = ItemStack.read(itemTags).getCount();
                itemText.appendString(" x" + countText);

                tooltip.add(itemText);
            }
            if(tagList.size() < 1)
            {
                tooltip.add(new TranslationTextComponent("Can be placed in the world."));
                tooltip.add(new TranslationTextComponent("Can store items and be moved like a shulker box."));
                tooltip.add(new TranslationTextComponent("Can be renamed."));
                tooltip.add(new TranslationTextComponent("Punch the Coffer to pick up directly to your inventory."));
            }

        } else {
//            tooltip.add(new TranslationTextComponent("tooltip.hexerei.coffer"));
            tooltip.add(new TranslationTextComponent("\u00A7e--------------\u00A7r"));

            for (int i = 0; i < Math.min(tagList.size(), 3); i++)
            {
                CompoundNBT itemTags = tagList.getCompound(i);

                TranslationTextComponent itemText = new TranslationTextComponent(ItemStack.read(itemTags).getTranslationKey());
                int countText = ItemStack.read(itemTags).getCount();
                itemText.appendString(" x" + countText);

                tooltip.add(itemText);
            }
            if(tagList.size() > 3) {
                tooltip.add(new TranslationTextComponent(". . . "));
                tooltip.add(new TranslationTextComponent(""));
                tooltip.add(new TranslationTextComponent("Hold \u00A7eSHIFT\u00A7r to see more"));
            }
            else if(tagList.size() < 1)
            {

                tooltip.add(new TranslationTextComponent("tooltip.hexerei.coffer"));
            }
        }
        super.addInformation(stack, world, tooltip, flagIn);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {

        //world.addParticle(ParticleTypes.ENCHANT, pos.getX() + Math.round(rand.nextDouble()), pos.getY() + 1.2d, pos.getZ() + Math.round(rand.nextDouble()) , (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.035d ,(rand.nextDouble() - 0.5d) / 50d);

        super.animateTick(state, world, pos, rand);
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                if(((CofferTile)worldIn.getTileEntity(pos)).customName != null)
                    return new TranslationTextComponent(((CofferTile)worldIn.getTileEntity(pos)).customName.getString());
                return new TranslationTextComponent("screen.hexerei.coffer");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new CofferContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        TileEntity te = ModTileEntities.COFFER_TILE.get().create();
        return te;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public Class<CofferTile> getTileEntityClass() {
        return CofferTile.class;
    }
}

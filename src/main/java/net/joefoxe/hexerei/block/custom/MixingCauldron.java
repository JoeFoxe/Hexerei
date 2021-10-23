package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.container.MixingCauldronContainer;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.particle.CauldronParticleData;
import net.joefoxe.hexerei.particle.ModParticleTypes;
import net.joefoxe.hexerei.state.properties.LiquidType;
import net.joefoxe.hexerei.tileentity.MixingCauldronTile;
import net.joefoxe.hexerei.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class MixingCauldron extends Block {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_3;
    public static final EnumProperty<LiquidType> FLUID = EnumProperty.create("fluid", LiquidType.class);
    public static final IntegerProperty CRAFT_DELAY = IntegerProperty.create("delay", 0, MixingCauldronTile.craftDelayMax);

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemstack) {
        super.onBlockPlacedBy(world, pos, state, entity, itemstack);

        //this.setFillLevel(world, pos, state, 0, LiquidType.EMPTY);

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(LEVEL, 0).with(FLUID, LiquidType.EMPTY).with(CRAFT_DELAY, 0);
    }


    public static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(0, 3, 0, 16, 16, 2),
            Block.makeCuboidShape(1, 13, -0.5, 15, 14, 0),
            Block.makeCuboidShape(1, 13, 16, 15, 14, 16.5),
            Block.makeCuboidShape(-0.5, 13, 1, 0, 14, 15),
            Block.makeCuboidShape(16, 13, 1, 16.5, 14, 15),
            Block.makeCuboidShape(-0.5, 11, 1, 0, 12, 15),
            Block.makeCuboidShape(1, 11, 16, 15, 12, 16.5),
            Block.makeCuboidShape(16, 11, 1, 16.5, 12, 15),
            Block.makeCuboidShape(1, 11, -0.5, 15, 12, 0),
            Block.makeCuboidShape(-1.25, 15.5, -1.25, 1.75, 16.5, 1.75),
            Block.makeCuboidShape(-0.75, 16.5, 1.25, 1.25, 17.5, 1.75),
            Block.makeCuboidShape(-1.25, 16.5, -1.25, -0.75, 17.5, 1.75),
            Block.makeCuboidShape(1.25, 16.5, -1.25, 1.75, 17.5, 1.75),
            Block.makeCuboidShape(-0.75, 16.5, -1.25, 1.25, 17.5, -0.75),
            Block.makeCuboidShape(0, 3, 14, 16, 16, 16),
            Block.makeCuboidShape(14, 3, 2, 16, 16, 14),
            Block.makeCuboidShape(1, 2, 1, 15, 2.5, 15),
            Block.makeCuboidShape(0.5, 1.5, 0.5, 15.5, 2, 15.5),
            Block.makeCuboidShape(0.5, 2.5, 0.5, 15.5, 3, 15.5),
            Block.makeCuboidShape(-0.25, 2.6, -0.25, 1.75, 3.5, 1.75),
            Block.makeCuboidShape(-0.75, 3.5, -0.75, 1.25, 15.5, 1.25),
            Block.makeCuboidShape(0.25, 0.5, 0.25, 3.25, 2.75, 3.25),
            Block.makeCuboidShape(-0.25, 0, -0.25, 3.75, 0.5, 3.75),
            Block.makeCuboidShape(12.75, 0.5, 0.25, 15.75, 2.75, 3.25),
            Block.makeCuboidShape(12.25, 0, -0.25, 16.25, 0.5, 3.75),
            Block.makeCuboidShape(12.75, 0.5, 12.75, 15.75, 2.75, 15.75),
            Block.makeCuboidShape(12.25, 0, 12.25, 16.25, 0.5, 16.25),
            Block.makeCuboidShape(0.25, 0.5, 12.75, 3.25, 2.75, 15.75),
            Block.makeCuboidShape(-0.25, 0, 12.25, 3.75, 0.5, 16.25),
            Block.makeCuboidShape(14.25, 2.6, -0.25, 16.25, 3.5, 1.75),
            Block.makeCuboidShape(14.75, 3.5, -0.75, 16.75, 15.5, 1.25),
            Block.makeCuboidShape(14.25, 15.5, -1.25, 17.25, 16.5, 1.75),
            Block.makeCuboidShape(14.25, 16.5, -1.25, 14.75, 17.5, 1.75),
            Block.makeCuboidShape(14.75, 16.5, -1.25, 16.75, 17.5, -0.75),
            Block.makeCuboidShape(16.75, 16.5, -1.25, 17.25, 17.5, 1.75),
            Block.makeCuboidShape(14.75, 16.5, 1.25, 16.75, 17.5, 1.75),
            Block.makeCuboidShape(14.25, 2.6, 14.25, 16.25, 3.5, 16.25),
            Block.makeCuboidShape(14.75, 3.5, 14.75, 16.75, 15.5, 16.75),
            Block.makeCuboidShape(14.25, 15.5, 14.25, 17.25, 16.5, 17.25),
            Block.makeCuboidShape(14.25, 16.5, 14.25, 14.75, 17.5, 17.25),
            Block.makeCuboidShape(14.75, 16.5, 16.75, 16.75, 17.5, 17.25),
            Block.makeCuboidShape(16.75, 16.5, 14.25, 17.25, 17.5, 17.25),
            Block.makeCuboidShape(14.75, 16.5, 14.25, 16.75, 17.5, 14.75),
            Block.makeCuboidShape(-0.25, 2.6, 14.25, 1.75, 3.5, 16.25),
            Block.makeCuboidShape(-0.75, 3.5, 14.75, 1.25, 15.5, 16.75),
            Block.makeCuboidShape(-1.25, 15.5, 14.25, 1.75, 16.5, 17.25),
            Block.makeCuboidShape(-0.75, 16.5, 16.75, 1.25, 17.5, 17.25),
            Block.makeCuboidShape(-1.25, 16.5, 14.25, -0.75, 17.5, 17.25),
            Block.makeCuboidShape(1.25, 16.5, 14.25, 1.75, 17.5, 17.25),
            Block.makeCuboidShape(-0.75, 16.5, 14.25, 1.25, 17.5, 14.75),
            Block.makeCuboidShape(0, 3, 2, 2, 16, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    public boolean useShapeForLightOcclusion(BlockState p_220074_1_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (itemstack.isEmpty()) {
            if(!worldIn.isRemote()) {
                TileEntity tileEntity = worldIn.getTileEntity(pos);

                if(tileEntity instanceof MixingCauldronTile) {
                    INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

                    NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getPos());

                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
        } else {
            int i = state.get(LEVEL);
            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET) {
                if ((state.get(FLUID) == LiquidType.WATER || state.get(FLUID) == LiquidType.EMPTY) && i < 3 && !worldIn.isRemote) {

                    player.addStat(Stats.FILL_CAULDRON);
                    this.setFillLevel(worldIn, pos, state, 3, LiquidType.WATER);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET))) {
                        player.dropItem(new ItemStack(Items.BUCKET), false);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.LAVA_BUCKET) {
                if ((state.get(FLUID) == LiquidType.LAVA || state.get(FLUID) == LiquidType.EMPTY) && i < 3 && !worldIn.isRemote) {

                    player.addStat(Stats.FILL_CAULDRON);
                    this.setFillLevel(worldIn, pos, state, 3, LiquidType.LAVA);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET))) {
                        player.dropItem(new ItemStack(Items.BUCKET), false);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.MILK_BUCKET) {
                if ((state.get(FLUID) == LiquidType.MILK || state.get(FLUID) == LiquidType.EMPTY) && i < 3 && !worldIn.isRemote) {

                    player.addStat(Stats.FILL_CAULDRON);
                    this.setFillLevel(worldIn, pos, state, 3, LiquidType.MILK);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET))) {
                        player.dropItem(new ItemStack(Items.BUCKET), false);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL_FISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == ModItems.QUICKSILVER_BUCKET.get()) {
                if ((state.get(FLUID) == LiquidType.QUICKSILVER || state.get(FLUID) == LiquidType.EMPTY) && i < 3 && !worldIn.isRemote) {

                    player.addStat(Stats.FILL_CAULDRON);
                    this.setFillLevel(worldIn, pos, state, 3, LiquidType.QUICKSILVER);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET))) {
                        player.dropItem(new ItemStack(Items.BUCKET), false);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.BUCKET) {
                if (i == 3 && !worldIn.isRemote) {
                    if(state.get(FLUID) == LiquidType.WATER) {
                        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        itemstack.shrink(1);
                        this.setFillLevel(worldIn, pos, state, 0, LiquidType.EMPTY);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                            player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    } else if(state.get(FLUID) == LiquidType.LAVA) {
                        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        itemstack.shrink(1);
                        this.setFillLevel(worldIn, pos, state, 0, LiquidType.EMPTY);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.LAVA_BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.LAVA_BUCKET))) {
                            player.dropItem(new ItemStack(Items.LAVA_BUCKET), false);
                        }
                    } else if(state.get(FLUID) == LiquidType.MILK) {
                        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL_FISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        itemstack.shrink(1);
                        this.setFillLevel(worldIn, pos, state, 0, LiquidType.EMPTY);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.MILK_BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                            player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
                        }
                    } else if(state.get(FLUID) == LiquidType.QUICKSILVER) {
                        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        itemstack.shrink(1);
                        this.setFillLevel(worldIn, pos, state, 0, LiquidType.EMPTY);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(ModItems.QUICKSILVER_BUCKET.get()));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(ModItems.QUICKSILVER_BUCKET.get()))) {
                            player.dropItem(new ItemStack(ModItems.QUICKSILVER_BUCKET.get()), false);
                        }
                    }


                    player.addStat(Stats.USE_CAULDRON);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.GLASS_BOTTLE) {
                if (i > 0 && !worldIn.isRemote) {
                    if(state.get(FLUID) == LiquidType.WATER) {
                        ItemStack itemstack4 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);
                        player.addStat(Stats.USE_CAULDRON);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, itemstack4);
                        } else if (!player.inventory.addItemStackToInventory(itemstack4)) {
                            player.dropItem(itemstack4, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                        }
                    } else if(state.get(FLUID) == LiquidType.LAVA) {
                        ItemStack itemstack5 = new ItemStack(ModItems.LAVA_BOTTLE.get());
                        player.addStat(Stats.USE_CAULDRON);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, itemstack5);
                        } else if (!player.inventory.addItemStackToInventory(itemstack5)) {
                            player.dropItem(itemstack5, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                        }
                    } else if(state.get(FLUID) == LiquidType.MILK) {
                        ItemStack itemstack6 = new ItemStack(ModItems.MILK_BOTTLE.get());
                        player.addStat(Stats.USE_CAULDRON);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, itemstack6);
                        } else if (!player.inventory.addItemStackToInventory(itemstack6)) {
                            player.dropItem(itemstack6, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                        }
                    } else if(state.get(FLUID) == LiquidType.QUICKSILVER) {
                        ItemStack itemstack7 = new ItemStack(ModItems.QUICKSILVER_BOTTLE.get());
                        player.addStat(Stats.USE_CAULDRON);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, itemstack7);
                        } else if (!player.inventory.addItemStackToInventory(itemstack7)) {
                            player.dropItem(itemstack7, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                        }
                    }

                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    this.setFillLevel(worldIn, pos, state, i - 1, i - 1 > 0 ? state.get(FLUID) : LiquidType.EMPTY);

                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.POTION && PotionUtils.getPotionFromItem(itemstack) == Potions.WATER && (state.get(FLUID) == LiquidType.WATER || state.get(FLUID) == LiquidType.EMPTY) && i < 3) {
                if (!worldIn.isRemote) {

                    ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                    player.addStat(Stats.USE_CAULDRON);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, itemstack3);
                    } else if (!player.inventory.addItemStackToInventory(itemstack3)) {
                        player.dropItem(itemstack3, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setFillLevel(worldIn, pos, state, i + 1, LiquidType.WATER);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == ModItems.LAVA_BOTTLE.get() && (state.get(FLUID) == LiquidType.LAVA || state.get(FLUID) == LiquidType.EMPTY) && i < 3) {
                if (!worldIn.isRemote) {

                    ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                    player.addStat(Stats.USE_CAULDRON);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, itemstack3);
                    } else if (!player.inventory.addItemStackToInventory(itemstack3)) {
                        player.dropItem(itemstack3, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setFillLevel(worldIn, pos, state, i + 1, LiquidType.LAVA);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == ModItems.MILK_BOTTLE.get() && (state.get(FLUID) == LiquidType.MILK || state.get(FLUID) == LiquidType.EMPTY) && i < 3) {
                if (!worldIn.isRemote) {

                    ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                    player.addStat(Stats.USE_CAULDRON);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, itemstack3);
                    } else if (!player.inventory.addItemStackToInventory(itemstack3)) {
                        player.dropItem(itemstack3, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setFillLevel(worldIn, pos, state, i + 1, LiquidType.MILK);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == ModItems.QUICKSILVER_BOTTLE.get() && (state.get(FLUID) == LiquidType.QUICKSILVER || state.get(FLUID) == LiquidType.EMPTY) && i < 3) {
                if (!worldIn.isRemote) {

                    ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                    player.addStat(Stats.USE_CAULDRON);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, itemstack3);
                    } else if (!player.inventory.addItemStackToInventory(itemstack3)) {
                        player.dropItem(itemstack3, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setFillLevel(worldIn, pos, state, i + 1, LiquidType.QUICKSILVER);
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            }
        }

        return ActionResultType.SUCCESS;
    }

    public void setFillLevel(World worldIn, BlockPos pos, BlockState state, int level, LiquidType type) {
        worldIn.setBlockState(pos, state.with(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))).with(FLUID, type), 2);
        //worldIn.setBlockState(pos, state.with(FLUID_TYPE, type), 2);
        //worldIn.updateComparatorOutputLevel(pos, this);

        //System.out.println("FILLED");
    }

    public static void setCraftDelay(World worldIn, BlockPos pos, BlockState state, int delay) {
        worldIn.setBlockState(pos, state.with(CRAFT_DELAY, delay), 2);

    }

    public MixingCauldron(Properties properties) {

        super(properties.notSolid());
    }
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {

        //builder.add(FLUID_TYPE);
        builder.add(LEVEL, FLUID, CRAFT_DELAY);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        MixingCauldronTile te = (MixingCauldronTile) worldIn.getTileEntity(pos);

        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(0)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(1)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(2)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(3)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(4)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(5)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(6)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(7)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(8)));
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, h.getStackInSlot(8)));
            if (!player.abilities.isCreativeMode)
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f, new ItemStack(ModBlocks.MIXING_CAULDRON.get().asItem())));
        });

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {

        // get slots and animate particles based off number of items in the cauldron
        int num = ((MixingCauldronTile)world.getTileEntity(pos)).getNumberOfItems();
        //int delay = ((MixingCauldronTile)world.getTileEntity(pos)).getCraftDelay();
        //System.out.println(state.get(CRAFT_DELAY));

        world.addParticle(ParticleTypes.FLAME, pos.getX() + Math.round(rand.nextDouble()), pos.getY() + 1.2d, pos.getZ() + Math.round(rand.nextDouble()) , (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.035d ,(rand.nextDouble() - 0.5d) / 50d);
        world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), pos.getX() + Math.round(rand.nextDouble()), pos.getY() + 1.2d, pos.getZ() + Math.round(rand.nextDouble()) , (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 2d ,(rand.nextDouble() - 0.5d) / 50d);
        world.addParticle(ParticleTypes.SMOKE, pos.getX() + Math.round(rand.nextDouble()), pos.getY() + 1.2d, pos.getZ() + Math.round(rand.nextDouble()) , (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.045d ,(rand.nextDouble() - 0.5d) / 50d);

        if(state.get(LEVEL) > 0) {
            for(int i = 0; i < state.get(LEVEL); i++) {
                if(rand.nextDouble() > 0.5f)
                    world.addParticle(ModParticleTypes.CAULDRON.get(), pos.getX() + 0.2d + (0.6d * rand.nextDouble()), pos.getY() + 0.95d + (0.05d * rand.nextDouble()) - ((3 - state.get(LEVEL)) * 0.15), pos.getZ() + 0.2d + (0.6d * rand.nextDouble()), (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.004d, (rand.nextDouble() - 0.5d) / 50d);
            }for(int i = 0; i < num; i++) {
                if(rand.nextDouble() > 0.5f)
                    world.addParticle(ModParticleTypes.CAULDRON.get(), pos.getX() + 0.2d + (0.6d * rand.nextDouble()), pos.getY() + 0.95d + (0.05d * rand.nextDouble()) - ((3 - state.get(LEVEL)) * 0.15), pos.getZ() + 0.2d + (0.6d * rand.nextDouble()), (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.004d, (rand.nextDouble() - 0.5d) / 50d);
            }
            if(state.get(FLUID) == LiquidType.WATER || state.get(FLUID) == LiquidType.MILK)
            {
                    world.addParticle(ParticleTypes.BUBBLE, pos.getX() + 0.2d + (0.6d * rand.nextDouble()), pos.getY() + 0.95d + (0.05d * rand.nextDouble()) - ((3 - state.get(LEVEL)) * 0.15), pos.getZ() + 0.2d + (0.6d * rand.nextDouble()), (rand.nextDouble() - 0.5d) / 50d, (rand.nextDouble() + 0.5d) * 0.005d, (rand.nextDouble() - 0.5d) / 50d);
            }
        }


        super.animateTick(state, world, pos, rand);
    }



    @SuppressWarnings("deprecation")
    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {

        if(!worldIn.isRemote())
        {
            System.out.println("Left clicked a block");
        }
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        //Firestone.lightEntityOnFire(entityIn, 5);
        super.onEntityWalk(worldIn, pos, entityIn);
    }


    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new MixingCauldronContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.MIXING_CAULDRON_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}

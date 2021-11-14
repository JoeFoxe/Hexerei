package net.joefoxe.hexerei.block.custom;

import net.joefoxe.hexerei.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class MahoganyWood extends RotatedPillarBlock {
    public MahoganyWood(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                           ItemStack stack, ToolType toolType) {
        boolean rightClickedWithAxe = toolType == ToolType.AXE;
        BlockState toReturn = ModBlocks.MAHOGANY_WOOD.get().getDefaultState();

        if(rightClickedWithAxe){
            toReturn = ModBlocks.STRIPPED_MAHOGANY_WOOD.get().getDefaultState().with(AXIS, state.get(AXIS));
        }

        return toReturn;
    }
}


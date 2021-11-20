package net.joefoxe.hexerei.item.custom;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;

public class FloweringLilyPadItem extends BlockItem {
    public FloweringLilyPadItem(Block blockIn, Item.Properties builder) {
        super(blockIn, builder);
    }


    public ActionResultType onItemUse(ItemUseContext context) {
        return ActionResultType.PASS;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        BlockRayTraceResult blockraytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        BlockRayTraceResult blockraytraceresult1 = blockraytraceresult.withPosition(blockraytraceresult.getPos().up());
//        if(worldIn.getFluidState(blockraytraceresult.getPos()).getFluid() != Fluids.WATER)
//            return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
        ActionResultType actionresulttype = super.onItemUse(new ItemUseContext(playerIn, handIn, blockraytraceresult1));
        return new ActionResult<>(actionresulttype, playerIn.getHeldItem(handIn));
    }
}

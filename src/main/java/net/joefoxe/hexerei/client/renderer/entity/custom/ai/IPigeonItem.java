package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface IPigeonItem {
    /**
     * Implement on item class called when player interacts with a pigeon
     * @param pigeonIn The pigeon the item is being used on
     * @param worldIn The world the pigeon is in
     * @param playerIn The player who clicked
     * @param handIn The hand used
     * @return The result of the interaction
     */

    public ActionResultType processInteract(AbstractPigeonEntity pigeonIn, World worldIn, PlayerEntity playerIn, Hand handIn);

    @Deprecated
    default ActionResultType onInteractWithPigeon(AbstractPigeonEntity pigeonIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        return processInteract(pigeonIn, worldIn, playerIn, handIn);
    }
}
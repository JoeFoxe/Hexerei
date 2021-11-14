package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InteractionHandler {

    private static final List<IPigeonItem> HANDLERS = Collections.synchronizedList(new ArrayList<>(4));

    public static void registerHandler(IPigeonItem handler) {HANDLERS.add(handler);}

    public static ActionResultType getMatch(@Nullable AbstractPigeonEntity pigeonIn, ItemStack stackIn, PlayerEntity playerIn, Hand handIn, World worldIn) {
        if (stackIn.getItem() instanceof IPigeonItem) {
            IPigeonItem item = (IPigeonItem) stackIn.getItem();
            ActionResultType result = item.processInteract(pigeonIn, worldIn, playerIn, handIn);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }

        for (IPigeonItem handler : HANDLERS) {
            ActionResultType result = handler.processInteract(pigeonIn, worldIn, playerIn, handIn);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }
        return ActionResultType.PASS;
    }
}
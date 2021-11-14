package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.IPigeonFoodHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class SeedFoodHandler implements IPigeonFoodHandler {

    @Override
    public boolean isFood(ItemStack stackIn) {
        return stackIn.getItem() != Items.PUMPKIN_SEEDS;
    }

    @Override
    public boolean canConsume(AbstractPigeonEntity pigeonIn, ItemStack stackIn, Entity entityIn) {
        return this.isFood(stackIn);
    }

    @Override
    public ActionResultType consume(AbstractPigeonEntity pigeonIn, ItemStack stackIn, Entity entityIn) {

        if (pigeonIn.getHealth() < pigeonIn.getMaxHealth()) {
            if (!pigeonIn.world.isRemote) {
                int heal = stackIn.getItem().getFood().getHealing() * 5;

                pigeonIn.setHealth(pigeonIn.getMaxHealth());
                pigeonIn.consumeItemFromStack(entityIn, stackIn);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
}
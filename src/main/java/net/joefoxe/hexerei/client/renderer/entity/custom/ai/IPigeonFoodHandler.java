package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import javax.annotation.Nullable;

import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;

public interface IPigeonFoodHandler extends IPigeonFoodPredicate {

    public boolean canConsume(AbstractPigeonEntity pigeonIn, ItemStack stackIn, @Nullable Entity entityIn);

    public ActionResultType consume(AbstractPigeonEntity pigeonIn, ItemStack stackIn, @Nullable Entity entityIn);
}
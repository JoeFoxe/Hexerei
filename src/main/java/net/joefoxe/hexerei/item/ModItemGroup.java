package net.joefoxe.hexerei.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup HEXEREI_GROUP = new ItemGroup("hexereiModTab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.ARMOR_SCRAP.get());
        }
    };
}

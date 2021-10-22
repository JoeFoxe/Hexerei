package net.joefoxe.armormod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup ARMOR_GROUP = new ItemGroup("armorModTab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.ARMOR_SCRAP.get());
        }
    };
}

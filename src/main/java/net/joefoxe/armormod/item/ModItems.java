package net.joefoxe.armormod.item;

import net.joefoxe.armormod.ArmorMod;
import net.joefoxe.armormod.fluid.ModFluids;
import net.joefoxe.armormod.item.custom.BottleLavaItem;
import net.joefoxe.armormod.item.custom.BottleMilkItem;
import net.joefoxe.armormod.item.custom.BottleQuicksilverItem;
import net.joefoxe.armormod.item.custom.Firestone;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ArmorMod.MOD_ID);

    public static final RegistryObject<Item> ARMOR_SCRAP = ITEMS.register("armor_scrap",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> WARHAMMER = ITEMS.register("warhammer",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> QUICKSILVER_BUCKET = ITEMS.register("quicksilver_bucket",
            () -> new BucketItem(() -> ModFluids.QUICKSILVER_FLUID.get(), new Item.Properties().maxStackSize(1).group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> QUICKSILVER_BOTTLE = ITEMS.register("quicksilver_bottle",
            () -> new BottleQuicksilverItem(new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> LAVA_BOTTLE = ITEMS.register("lava_bottle",
            () -> new BottleLavaItem(new Item.Properties().group(ModItemGroup.ARMOR_GROUP).maxDamage(100)));

    public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle",
            () -> new BottleMilkItem(new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> FIRESTONE = ITEMS.register("firestone",
            () -> new Firestone(new Item.Properties().group(ModItemGroup.ARMOR_GROUP).maxDamage(8)));

    public static final RegistryObject<Item> SCRAP_BOOTS = ITEMS.register("scrap_boots",
            () -> new ArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.FEET,
                    new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> SCRAP_LEGGINGS = ITEMS.register("scrap_leggings",
            () -> new ArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.LEGS,
                    new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> SCRAP_CHESTPLATE = ITEMS.register("scrap_chestplate",
            () -> new ArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.CHEST,
                    new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));

    public static final RegistryObject<Item> SCRAP_HELMET = ITEMS.register("scrap_helmet",
            () -> new ArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.HEAD,
                    new Item.Properties().group(ModItemGroup.ARMOR_GROUP)));



    public static final RegistryObject<Item> ORC_HELMET = ITEMS.register("orc_helmet",
            () -> new OrcArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.HEAD,
                    new Item.Properties().rarity(Rarity.RARE).group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> ORC_CHESTPLATE = ITEMS.register("orc_chestplate",
            () -> new OrcArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.CHEST,
                    new Item.Properties().rarity(Rarity.RARE).group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> ORC_LEGGINGS = ITEMS.register("orc_leggings",
            () -> new OrcArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.LEGS,
                    new Item.Properties().rarity(Rarity.RARE).group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> ORC_BOOTS = ITEMS.register("orc_boots",
            () -> new OrcArmorItem(ModArmorMaterial.ARMOR_SCRAP, EquipmentSlotType.FEET,
                    new Item.Properties().rarity(Rarity.RARE).group(ItemGroup.COMBAT)));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}

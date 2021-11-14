package net.joefoxe.hexerei.item;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.model.OrcArmorModel;
import net.joefoxe.hexerei.client.renderer.entity.model.WitchArmorModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WitchArmorItem extends ArmorItem {

    public WitchArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
        super(materialIn, slot, builder);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
//
//        ModModels.GearModel gearModel = ModModels.GearModel.REGISTRY.get(0);
//
//        if (gearModel == null) {
//            return null;
//        }
//
//        return (A) gearModel.forSlotType(armorSlot);
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return WitchArmorModel.getModel(armorSlot, entityLiving);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return Hexerei.MOD_ID + ":textures/models/armor/witch_armor_layer1.png";
    }
}
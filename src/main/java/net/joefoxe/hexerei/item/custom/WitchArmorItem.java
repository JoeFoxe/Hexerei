package net.joefoxe.hexerei.item.custom;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.model.OrcArmorModel;
import net.joefoxe.hexerei.client.renderer.entity.model.WitchArmorModel;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

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


    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.witch_armor_shift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.hexerei.witch_armor"));
        }
        super.addInformation(stack, world, tooltip, flagIn);
    }
}
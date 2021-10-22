package net.joefoxe.armormod.model;

import net.joefoxe.armormod.ArmorMod;
import net.joefoxe.armormod.block.ModBlocks;
import net.joefoxe.armormod.client.renderer.entity.model.ArmorModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ModModels {

    public static void setupRenderLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.MIXING_CAULDRON.get(), ModModels::isMixingCauldronValidLayer);
    }

    public static boolean isMixingCauldronValidLayer(RenderType layerToCheck) {
        return layerToCheck == RenderType.getCutout() || layerToCheck == RenderType.getTranslucent();
    }

}

package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public interface ISkillRenderer<T extends AbstractPigeonEntity> {
    default void render(LayerRenderer<T, EntityModel<T>> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T pigeon, SkillInstance inst, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
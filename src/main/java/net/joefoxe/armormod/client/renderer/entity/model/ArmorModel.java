package net.joefoxe.armormod.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ArmorModel <T extends LivingEntity> extends BipedModel<T> {
    protected final EquipmentSlotType slotType;
    protected ModelRenderer Head;

    protected ModelRenderer Body;
    protected ModelRenderer RightArm;
    protected ModelRenderer LeftArm;

    protected ModelRenderer RightLeg;
    protected ModelRenderer LeftLeg;

    protected ModelRenderer Belt;
    protected ModelRenderer RightBoot;
    protected ModelRenderer LeftBoot;

    public ArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, 0, 32, 32);
        this.slotType = slotType;
    }
    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.push();

        if (this.slotType == EquipmentSlotType.HEAD) {
            Head.copyModelAngles(this.bipedHead);
            Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        }
        else if (this.slotType == EquipmentSlotType.CHEST) {
            Body.copyModelAngles(this.bipedBody);
            Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            RightArm.copyModelAngles(this.bipedRightArm);
            RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            LeftArm.copyModelAngles(this.bipedLeftArm);
            LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        } else if (this.slotType == EquipmentSlotType.LEGS) {
            Belt.copyModelAngles(this.bipedBody);
            Belt.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            RightLeg.copyModelAngles(this.bipedRightLeg);
            RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            LeftLeg.copyModelAngles(this.bipedLeftLeg);
            LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        } else if (this.slotType == EquipmentSlotType.FEET) {
            RightBoot.copyModelAngles(this.bipedRightLeg);
            RightBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            LeftBoot.copyModelAngles(this.bipedLeftLeg);
            LeftBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        matrixStack.pop();

//        if (this.slotType == EquipmentSlotType.CHEST) {
//            Body.copyModelAngles(this.Body);
//            Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//            matrixStack.pop();
//
//        }
    }
}
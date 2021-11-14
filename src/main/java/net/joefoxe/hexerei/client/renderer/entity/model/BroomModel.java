package net.joefoxe.hexerei.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class BroomModel <T extends BroomEntity> extends EntityModel<T> {
        private final ModelRenderer bb_main;
        private final ModelRenderer cube_r1;
        private final ModelRenderer cube_r2;
        private final ModelRenderer cube_r3;
        private final ModelRenderer cube_r4;
        private final ModelRenderer cube_r5;
        private final ModelRenderer cube_r6;
        private final ModelRenderer cube_r7;
        private final ModelRenderer cube_r8;
        private final ModelRenderer cube_r9;
        private final ModelRenderer cube_r10;
        private final ModelRenderer cube_r11;
        private final ModelRenderer cube_r12;
        private final ModelRenderer cube_r13;
        private final ModelRenderer cube_r14;
        private final ModelRenderer cube_r15;
        private final ModelRenderer cube_r16;
        private final ModelRenderer cube_r17;
        private final ModelRenderer cube_r18;
        private final ModelRenderer cube_r19;
        private final ModelRenderer cube_r20;
        private final ModelRenderer cube_r21;
        private final ModelRenderer cube_r22;
        private final ModelRenderer cube_r23;
        private final ModelRenderer cube_r24;
        private final ModelRenderer cube_r25;
        private final ModelRenderer cube_r26;
        private final ModelRenderer cube_r27;
        private final ModelRenderer cube_r28;
        private final ModelRenderer cube_r29;
        private final ModelRenderer cube_r30;
        private final ModelRenderer cube_r31;
        private final ModelRenderer cube_r32;
        private final ModelRenderer cube_r33;
        private final ModelRenderer cube_r34;
        private final ModelRenderer cube_r35;
        private final ModelRenderer cube_r36;

        public BroomModel() {
            textureWidth = 64;
            textureHeight = 64;

            bb_main = new ModelRenderer(this);
            bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
            bb_main.setTextureOffset(28, 30).addBox(-21.45F, -3.01F, -1.4F, 4.0F, 2.0F, 2.0F, 0.0F, false);
            bb_main.setTextureOffset(0, 0).addBox(-11.0F, -3.0F, -1.0F, 25.0F, 2.0F, 2.0F, 0.0F, false);
            bb_main.setTextureOffset(0, 32).addBox(11.5F, -3.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
            bb_main.setTextureOffset(31, 18).addBox(10.0F, -3.5F, -1.5F, 1.0F, 3.0F, 3.0F, -0.2F, false);

            cube_r1 = new ModelRenderer(this);
            cube_r1.setRotationPoint(10.5764F, -1.9358F, -1.0442F);
            bb_main.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.5932F, -0.444F, 0.0043F);
            cube_r1.setTextureOffset(5, 32).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r2 = new ModelRenderer(this);
            cube_r2.setRotationPoint(10.5776F, -1.9892F, 1.0459F);
            bb_main.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.8986F, 0.444F, -0.0043F);
            cube_r2.setTextureOffset(32, 13).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r3 = new ModelRenderer(this);
            cube_r3.setRotationPoint(10.5814F, -0.9482F, -0.0107F);
            bb_main.addChild(cube_r3);
            setRotationAngle(cube_r3, -2.1539F, -0.0039F, -0.444F);
            cube_r3.setTextureOffset(32, 15).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r4 = new ModelRenderer(this);
            cube_r4.setRotationPoint(10.5814F, -3.0518F, 0.0107F);
            bb_main.addChild(cube_r4);
            setRotationAngle(cube_r4, 2.6458F, 0.0039F, 0.444F);
            cube_r4.setTextureOffset(5, 33).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r5 = new ModelRenderer(this);
            cube_r5.setRotationPoint(10.5814F, -1.3619F, -0.8758F);
            bb_main.addChild(cube_r5);
            setRotationAngle(cube_r5, 3.0439F, -0.2828F, -0.347F);
            cube_r5.setTextureOffset(13, 33).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r6 = new ModelRenderer(this);
            cube_r6.setRotationPoint(10.5814F, -2.6381F, 0.8758F);
            bb_main.addChild(cube_r6);
            setRotationAngle(cube_r6, -2.6721F, 0.2828F, 0.347F);
            cube_r6.setTextureOffset(33, 16).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r7 = new ModelRenderer(this);
            cube_r7.setRotationPoint(10.5814F, -2.7517F, -0.7805F);
            bb_main.addChild(cube_r7);
            setRotationAngle(cube_r7, 1.8575F, -0.3791F, 0.237F);
            cube_r7.setTextureOffset(33, 17).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r8 = new ModelRenderer(this);
            cube_r8.setRotationPoint(10.5814F, -1.2483F, 0.7805F);
            bb_main.addChild(cube_r8);
            setRotationAngle(cube_r8, -0.3242F, 0.3791F, -0.237F);
            cube_r8.setTextureOffset(5, 34).addBox(-1.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r9 = new ModelRenderer(this);
            cube_r9.setRotationPoint(-15.048F, -2.01F, 0.2544F);
            bb_main.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.4363F, 0.0F);
            cube_r9.setTextureOffset(30, 9).addBox(-2.35F, -0.99F, -2.725F, 4.0F, 2.0F, 2.0F, 0.0F, false);

            cube_r10 = new ModelRenderer(this);
            cube_r10.setRotationPoint(-12.198F, -2.01F, -0.3544F);
            bb_main.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.0F, -0.4363F, 0.0F);
            cube_r10.setTextureOffset(0, 22).addBox(-3.35F, -1.0F, -1.275F, 5.0F, 2.0F, 2.0F, 0.0F, false);

            cube_r11 = new ModelRenderer(this);
            cube_r11.setRotationPoint(19.2669F, -1.463F, 0.1896F);
            bb_main.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.752F, 0.081F, -0.152F);
            cube_r11.setTextureOffset(16, 4).addBox(-2.5F, -0.5F, 0.1F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r12 = new ModelRenderer(this);
            cube_r12.setRotationPoint(18.5814F, -2.0F, 0.0F);
            bb_main.addChild(cube_r12);
            setRotationAngle(cube_r12, 0.0278F, -0.0039F, -0.444F);
            cube_r12.setTextureOffset(16, 14).addBox(-1.4064F, -2.7995F, 0.031F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r13 = new ModelRenderer(this);
            cube_r13.setRotationPoint(18.5814F, -2.0F, 0.0F);
            bb_main.addChild(cube_r13);
            setRotationAngle(cube_r13, 0.0278F, 0.0039F, 0.444F);
            cube_r13.setTextureOffset(26, 4).addBox(-1.4064F, 1.7995F, -0.031F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r14 = new ModelRenderer(this);
            cube_r14.setRotationPoint(18.5853F, -3.029F, 2.3394F);
            bb_main.addChild(cube_r14);
            setRotationAngle(cube_r14, -1.0284F, -0.3791F, -0.237F);
            cube_r14.setTextureOffset(26, 14).addBox(-2.5F, -0.5F, -0.25F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r15 = new ModelRenderer(this);
            cube_r15.setRotationPoint(18.5853F, -0.971F, -2.3394F);
            bb_main.addChild(cube_r15);
            setRotationAngle(cube_r15, -1.0284F, 0.3791F, 0.237F);
            cube_r15.setTextureOffset(30, 8).addBox(-2.5F, -0.5F, 0.25F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r16 = new ModelRenderer(this);
            cube_r16.setRotationPoint(18.5853F, -0.971F, 2.3394F);
            bb_main.addChild(cube_r16);
            setRotationAngle(cube_r16, 1.0284F, -0.3791F, 0.237F);
            cube_r16.setTextureOffset(32, 5).addBox(-2.5F, -0.5F, -0.25F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r17 = new ModelRenderer(this);
            cube_r17.setRotationPoint(18.5853F, -3.029F, -2.3394F);
            bb_main.addChild(cube_r17);
            setRotationAngle(cube_r17, 1.0284F, 0.3791F, -0.237F);
            cube_r17.setTextureOffset(32, 6).addBox(-2.5F, -0.5F, 0.25F, 5.0F, 1.0F, 0.0F, 0.0F, false);

            cube_r18 = new ModelRenderer(this);
            cube_r18.setRotationPoint(14.1125F, -2.0F, 0.0F);
            bb_main.addChild(cube_r18);
            setRotationAngle(cube_r18, -1.5708F, 0.2182F, 0.0F);
            cube_r18.setTextureOffset(0, 4).addBox(-3.2256F, 0.7377F, -1.0F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r19 = new ModelRenderer(this);
            cube_r19.setRotationPoint(14.1125F, -2.0F, 0.0F);
            bb_main.addChild(cube_r19);
            setRotationAngle(cube_r19, -1.5708F, -0.2182F, 0.0F);
            cube_r19.setTextureOffset(0, 7).addBox(-3.2256F, -1.7377F, -1.0F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r20 = new ModelRenderer(this);
            cube_r20.setRotationPoint(14.1125F, -0.7322F, 0.0F);
            bb_main.addChild(cube_r20);
            setRotationAngle(cube_r20, 0.0F, 0.0F, 0.2182F);
            cube_r20.setTextureOffset(0, 10).addBox(-3.5F, -0.5F, -1.0F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r21 = new ModelRenderer(this);
            cube_r21.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r21);
            setRotationAngle(cube_r21, -0.5016F, -0.1555F, -0.2628F);
            cube_r21.setTextureOffset(0, 13).addBox(-3.6835F, -2.4408F, -1.0187F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r22 = new ModelRenderer(this);
            cube_r22.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r22);
            setRotationAngle(cube_r22, 0.0674F, -0.1556F, -0.132F);
            cube_r22.setTextureOffset(14, 24).addBox(-2.3184F, -0.3303F, -0.1273F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r23 = new ModelRenderer(this);
            cube_r23.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r23);
            setRotationAngle(cube_r23, -2.2449F, 0.0708F, 0.1557F);
            cube_r23.setTextureOffset(0, 26).addBox(-2.342F, -1.0624F, -0.1338F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r24 = new ModelRenderer(this);
            cube_r24.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r24);
            setRotationAngle(cube_r24, -1.2424F, -0.0249F, -0.1232F);
            cube_r24.setTextureOffset(14, 27).addBox(-2.3472F, -0.9002F, -1.0518F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r25 = new ModelRenderer(this);
            cube_r25.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r25);
            setRotationAngle(cube_r25, -0.6624F, -0.2947F, 0.1951F);
            cube_r25.setTextureOffset(28, 24).addBox(-2.2376F, -1.0799F, 0.2809F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r26 = new ModelRenderer(this);
            cube_r26.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r26);
            setRotationAngle(cube_r26, -1.543F, -0.0194F, -0.3413F);
            cube_r26.setTextureOffset(28, 27).addBox(-2.2195F, -1.0525F, -1.3331F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r27 = new ModelRenderer(this);
            cube_r27.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r27);
            setRotationAngle(cube_r27, -1.5463F, 0.0102F, 0.3493F);
            cube_r27.setTextureOffset(0, 29).addBox(-2.2422F, -0.9445F, 0.2697F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r28 = new ModelRenderer(this);
            cube_r28.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r28);
            setRotationAngle(cube_r28, -0.6605F, 0.2851F, -0.1865F);
            cube_r28.setTextureOffset(14, 30).addBox(-2.2241F, -0.9719F, -1.3219F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r29 = new ModelRenderer(this);
            cube_r29.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r29);
            setRotationAngle(cube_r29, -1.07F, -0.2748F, -0.1409F);
            cube_r29.setTextureOffset(16, 8).addBox(-2.2573F, -1.476F, -1.0308F, 6.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r30 = new ModelRenderer(this);
            cube_r30.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r30);
            setRotationAngle(cube_r30, -1.0714F, 0.2651F, 0.1485F);
            cube_r30.setTextureOffset(16, 11).addBox(-2.2654F, 0.45F, -1.0308F, 6.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r31 = new ModelRenderer(this);
            cube_r31.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r31);
            setRotationAngle(cube_r31, -0.5042F, 0.1461F, 0.2704F);
            cube_r31.setTextureOffset(0, 16).addBox(-3.7003F, 1.3873F, -1.0187F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r32 = new ModelRenderer(this);
            cube_r32.setRotationPoint(14.1125F, -3.2678F, 0.0F);
            bb_main.addChild(cube_r32);
            setRotationAngle(cube_r32, 0.0F, 0.0F, -0.2182F);
            cube_r32.setTextureOffset(16, 5).addBox(-3.5F, -0.5F, -1.0F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            cube_r33 = new ModelRenderer(this);
            cube_r33.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r33);
            setRotationAngle(cube_r33, -0.0662F, 0.3407F, 0.0044F);
            cube_r33.setTextureOffset(17, 15).addBox(-3.6415F, -0.9453F, -2.3127F, 7.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r34 = new ModelRenderer(this);
            cube_r34.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r34);
            setRotationAngle(cube_r34, -1.0074F, -0.1748F, 0.3065F);
            cube_r34.setTextureOffset(17, 18).addBox(-3.6524F, -0.9286F, 1.2811F, 7.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r35 = new ModelRenderer(this);
            cube_r35.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r35);
            setRotationAngle(cube_r35, -1.0045F, 0.1655F, -0.298F);
            cube_r35.setTextureOffset(0, 19).addBox(-3.632F, -1.1049F, -2.3358F, 7.0F, 2.0F, 1.0F, 0.0F, false);

            cube_r36 = new ModelRenderer(this);
            cube_r36.setRotationPoint(17.8711F, -1.972F, 0.0187F);
            bb_main.addChild(cube_r36);
            setRotationAngle(cube_r36, -0.0662F, -0.3505F, 0.0037F);
            cube_r36.setTextureOffset(15, 21).addBox(-3.6429F, -1.1216F, 1.3041F, 7.0F, 2.0F, 1.0F, 0.0F, false);
        }

    @Override
    public void setRotationAngles(BroomEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }


    /**
     * Sets this entity's model rotation angles
     */
//    public void setRotationAngles(BroomEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        this.func_228244_a_(entityIn, 0, limbSwing);
//        this.func_228244_a_(entityIn, 1, limbSwing);
//    }
//
//    public ImmutableList<ModelRenderer> getParts() {
//        return this.field_228243_f_;
//    }
//
//    public ModelRenderer func_228245_c_() {
//        return this.noWater;
//    }

//    protected ModelRenderer makePaddle(boolean p_187056_1_) {
//        ModelRenderer modelrenderer = (new ModelRenderer(this, 62, p_187056_1_ ? 0 : 20)).setTextureSize(128, 64);
//        int i = 20;
//        int j = 7;
//        int k = 6;
//        float f = -5.0F;
//        modelrenderer.addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F);
//        modelrenderer.addBox(p_187056_1_ ? -1.001F : 0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F);
//        return modelrenderer;
//    }

//    protected void func_228244_a_(BroomEntity p_228244_1_, int p_228244_2_, float p_228244_3_) {
//        float f = p_228244_1_.getRowingTime(p_228244_2_, p_228244_3_);
//        ModelRenderer modelrenderer = this.paddles[p_228244_2_];
//        modelrenderer.rotateAngleX = (float) MathHelper.clampedLerp((double)(-(float)Math.PI / 3F), (double)-0.2617994F, (double)((MathHelper.sin(-f) + 1.0F) / 2.0F));
//        modelrenderer.rotateAngleY = (float)MathHelper.clampedLerp((double)(-(float)Math.PI / 4F), (double)((float)Math.PI / 4F), (double)((MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F));
//        if (p_228244_2_ == 1) {
//            modelrenderer.rotateAngleY = (float)Math.PI - modelrenderer.rotateAngleY;
//        }
//
//    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
package net.joefoxe.hexerei.client.renderer.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.client.renderer.entity.model.BroomModel;
import net.joefoxe.hexerei.client.renderer.entity.model.PigeonModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)


public class BroomRenderer extends EntityRenderer<BroomEntity>
{
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(Hexerei.MOD_ID, "textures/entity/broom.png");
    protected final BroomModel modelBroom = new BroomModel();

    public BroomRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(BroomEntity entity) {
        return TEXTURE;
    }

    public void render(BroomEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, 0.375D + entityIn.floatingOffset, 0.0D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw - (entityIn.deltaRotation * 2)));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float)entityIn.getMotion().getY() * 20f));
        float f = (float)entityIn.getTimeSinceHit() - partialTicks;
        float f1 = entityIn.getDamageTaken() - partialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float)entityIn.getForwardDirection()));
        }

        float f2 = entityIn.getRockingAngle(partialTicks);
        if (!MathHelper.epsilonEquals(f2, 0.0F)) {
            matrixStackIn.rotate(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entityIn.getRockingAngle(partialTicks), true));
        }

//        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrixStackIn.translate(0, -1.6, 0);
        this.modelBroom.setRotationAngles(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.modelBroom.getRenderType(this.getEntityTexture(entityIn)));
        this.modelBroom.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!entityIn.canSwim()) {
            IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getWaterMask());
//            this.modelBroom.func_228245_c_().render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.NO_OVERLAY);
        }

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}

//
//public class BroomRenderer extends EntityRenderer<BroomEntity> {
//    private static final ResourceLocation[] BROOM_TEXTURES = new ResourceLocation[]{new ResourceLocation(Hexerei.MOD_ID,"textures/entity/broom.png")};
//    protected final BroomModel modelBroom = new BroomModel();
//
//    public BroomRenderer(EntityRendererManager renderManagerIn) {
//        super(renderManagerIn);
//        this.shadowSize = 0.8F;
//    }
//


//    /**
//     * Returns the location of an entity's texture.
//     */
//    public ResourceLocation getEntityTexture(BroomEntity entity) {
//        return BROOM_TEXTURES[0];
//    }
//}

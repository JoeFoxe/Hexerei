package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.CrystalBall;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.tileentity.CrystalBallTile;
import net.joefoxe.hexerei.tileentity.MixingCauldronTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class CrystalBallRenderer extends TileEntityRenderer<CrystalBallTile> {

    public CrystalBallRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(CrystalBallTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        matrixStackIn.push();
        matrixStackIn.translate(8f / 16f, 9f / 16f, 8f / 16f);
        matrixStackIn.translate(0f/16f , tileEntityIn.orbOffset/16f, 0f/16f);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesSpun * 4));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CRYSTAL_BALL_ORB.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(8f/16f , 7f/16f, 8f/16f);
        matrixStackIn.translate(0f/16f , tileEntityIn.largeRingOffset/16f, 0f/16f);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun * 6));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CRYSTAL_BALL_LARGE_RING.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(8f/16f , 4.5f/16f, 8f/16f);
        matrixStackIn.translate(0f/16f , tileEntityIn.smallRingOffset/16f, 0f/16f);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesSpun * 6));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CRYSTAL_BALL_SMALL_RING.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CRYSTAL_BALL_STAND.get().getDefaultState());
        matrixStackIn.pop();

    }

    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                            int combinedLightIn) {
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }


    private void renderBlock(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, BlockState state) {
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

    }


}

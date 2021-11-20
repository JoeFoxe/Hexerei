package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.tileentity.CandleDipperTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class CandleDipperRenderer extends TileEntityRenderer<CandleDipperTile> {

    public CandleDipperRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.getPosX() - pos.getX();
        double deltaY = entity.getPosY() - pos.getY();
        double deltaZ = entity.getPosZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    @Override
    public void render(CandleDipperTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        if(!tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).hasTileEntity())
            return;

        float rotation = 0;

        if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
            rotation = 180;
        } else if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
            rotation = 0;
        } else if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
            rotation = 90;
        } else if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
            rotation = 270;
        }

        matrixStackIn.push();
        matrixStackIn.translate(tileEntityIn.candlePos1.getX(), tileEntityIn.candlePos1.getY(), tileEntityIn.candlePos1.getZ());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_WICK_BASE.get().getDefaultState());
        matrixStackIn.pop();

        if(tileEntityIn.candlePos1Slot != 0) {
            matrixStackIn.push();
            matrixStackIn.translate(tileEntityIn.candlePos1.getX(), tileEntityIn.candlePos1.getY(), tileEntityIn.candlePos1.getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_WICK.get().getDefaultState());
            matrixStackIn.pop();
        }

        if(tileEntityIn.candle1DippedTimes > 0) {
            matrixStackIn.push();
            matrixStackIn.translate(tileEntityIn.candlePos1.getX(), tileEntityIn.candlePos1.getY(), tileEntityIn.candlePos1.getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            if(tileEntityIn.candle1DippedTimes == 1)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_1.get().getDefaultState());
            if(tileEntityIn.candle1DippedTimes == 2)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_2.get().getDefaultState());
            if(tileEntityIn.candle1DippedTimes == 3)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_3.get().getDefaultState());
            matrixStackIn.pop();
        }




        matrixStackIn.push();
        matrixStackIn.translate(tileEntityIn.candlePos2.getX(), tileEntityIn.candlePos2.getY(), tileEntityIn.candlePos2.getZ());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_WICK_BASE.get().getDefaultState());
        matrixStackIn.pop();

        if(tileEntityIn.candlePos2Slot != 0) {
            matrixStackIn.push();
            matrixStackIn.translate(tileEntityIn.candlePos2.getX(), tileEntityIn.candlePos2.getY(), tileEntityIn.candlePos2.getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_WICK.get().getDefaultState());
            matrixStackIn.pop();
        }

        if(tileEntityIn.candle2DippedTimes > 0) {
            matrixStackIn.push();
            matrixStackIn.translate(tileEntityIn.candlePos2.getX(), tileEntityIn.candlePos2.getY(), tileEntityIn.candlePos2.getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            if(tileEntityIn.candle2DippedTimes == 1)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_1.get().getDefaultState());
            if(tileEntityIn.candle2DippedTimes == 2)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_2.get().getDefaultState());
            if(tileEntityIn.candle2DippedTimes == 3)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_3.get().getDefaultState());
            matrixStackIn.pop();
        }



        matrixStackIn.push();
        matrixStackIn.translate(tileEntityIn.candlePos3.getX(), tileEntityIn.candlePos3.getY(), tileEntityIn.candlePos3.getZ());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_WICK_BASE.get().getDefaultState());
        matrixStackIn.pop();

        if(tileEntityIn.candlePos3Slot != 0) {
            matrixStackIn.push();
            matrixStackIn.translate(tileEntityIn.candlePos3.getX(), tileEntityIn.candlePos3.getY(), tileEntityIn.candlePos3.getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_WICK.get().getDefaultState());
            matrixStackIn.pop();
        }

        if(tileEntityIn.candle3DippedTimes > 0) {
            matrixStackIn.push();
            matrixStackIn.translate(tileEntityIn.candlePos3.getX(), tileEntityIn.candlePos3.getY(), tileEntityIn.candlePos3.getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            if(tileEntityIn.candle3DippedTimes == 1)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_1.get().getDefaultState());
            if(tileEntityIn.candle3DippedTimes == 2)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_2.get().getDefaultState());
            if(tileEntityIn.candle3DippedTimes == 3)
                renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.CANDLE_DIPPER_CANDLE_3.get().getDefaultState());
            matrixStackIn.pop();
        }

//
//        matrixStackIn.push();
//        matrixStackIn.translate(8f/16f , 18f/16f, 8f/16f);
//        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
//        matrixStackIn.translate(-(float)Math.sin((tileEntityIn.degreesSpun+90f)/57.1f)/32f , 0f/16f, -(float)Math.cos((tileEntityIn.degreesSpun+90f)/57.1f)/32f);
//        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
//        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
//        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-tileEntityIn.degreesOpened));
//        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(tileEntityIn.degreesFlopped));
//        matrixStackIn.translate(0,0, -(tileEntityIn.degreesFlopped/10f)/32);
//        matrixStackIn.translate(0 , (-0.5f * (tileEntityIn.degreesFlopped / 90))/16f, -(float)Math.sin((tileEntityIn.degreesFlopped)/57.1f)/32f);
//        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BACK.get().getDefaultState());
//        matrixStackIn.pop();
//
//        matrixStackIn.push();
//        matrixStackIn.translate(8f/16f , 18f/16f, 8f/16f);
//        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
//        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
//        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesFlopped));
//        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
//        //matrixStackIn.translate(-(float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesFlopped/5f) , 0f/16f, -(float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesFlopped/5f) );
//        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BINDING.get().getDefaultState());
//        matrixStackIn.pop();
//
//        matrixStackIn.push();
//        matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
//        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
//        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
//        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesFlopped));
//        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
//        matrixStackIn.translate(0,1f/32f,0);
//        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((70f-tileEntityIn.degreesOpened/1.29f)));
//        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().getDefaultState());
//        matrixStackIn.pop();
//
//        matrixStackIn.push();
//        matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
//        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
//        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
//        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesFlopped));
//        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
//        matrixStackIn.translate(0,1f/32f,0);
//        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-(70f-tileEntityIn.degreesOpened/1.29f)));
//        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().getDefaultState());
//        matrixStackIn.pop();




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

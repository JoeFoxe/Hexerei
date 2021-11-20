package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.tileentity.BookOfShadowsAltarTile;
import net.joefoxe.hexerei.tileentity.CrystalBallTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class BookOfShadowsAltarRenderer extends TileEntityRenderer<BookOfShadowsAltarTile> {

    public BookOfShadowsAltarRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    public static double getDistanceToEntity(Entity entity, BlockPos pos) {
        double deltaX = entity.getPosX() - pos.getX();
        double deltaY = entity.getPosY() - pos.getY();
        double deltaZ = entity.getPosZ() - pos.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    @Override
    public void render(BookOfShadowsAltarTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        if(!tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).hasTileEntity())
            return;

        matrixStackIn.push();
        matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun+90f)/57.1f)/32f , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun+90f)/57.1f)/32f);
        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesFlopped));
        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
        matrixStackIn.translate(0 , (-0.5f * (tileEntityIn.degreesFlopped / 90))/16f, (float)Math.sin((tileEntityIn.degreesFlopped)/57.1f)/32f);
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_COVER.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(8f/16f , 18f/16f, 8f/16f);
        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
        matrixStackIn.translate(-(float)Math.sin((tileEntityIn.degreesSpun+90f)/57.1f)/32f , 0f/16f, -(float)Math.cos((tileEntityIn.degreesSpun+90f)/57.1f)/32f);
        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(tileEntityIn.degreesFlopped));
        matrixStackIn.translate(0,0, -(tileEntityIn.degreesFlopped/10f)/32);
        matrixStackIn.translate(0 , (-0.5f * (tileEntityIn.degreesFlopped / 90))/16f, -(float)Math.sin((tileEntityIn.degreesFlopped)/57.1f)/32f);
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BACK.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(8f/16f , 18f/16f, 8f/16f);
        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesFlopped));
        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
        //matrixStackIn.translate(-(float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesFlopped/5f) , 0f/16f, -(float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesFlopped/5f) );
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_BINDING.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesFlopped));
        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
        matrixStackIn.translate(0,1f/32f,0);
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((70f-tileEntityIn.degreesOpened/1.29f)));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().getDefaultState());
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(8f / 16f, 18f / 16f, 8f / 16f);
        matrixStackIn.translate((float)Math.sin((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f) , 0f/16f, (float)Math.cos((tileEntityIn.degreesSpun)/57.1f)/32f * (tileEntityIn.degreesOpened/5f - 12f));
        matrixStackIn.translate(0 , -((tileEntityIn.degreesFlopped / 90))/16f, 0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.degreesSpun));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntityIn.degreesOpened));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-tileEntityIn.degreesFlopped));
        matrixStackIn.translate(0,0,-(tileEntityIn.degreesFlopped/10f)/32);
        matrixStackIn.translate(0,1f/32f,0);
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-(70f-tileEntityIn.degreesOpened/1.29f)));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BOOK_OF_SHADOWS_PAGE.get().getDefaultState());
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

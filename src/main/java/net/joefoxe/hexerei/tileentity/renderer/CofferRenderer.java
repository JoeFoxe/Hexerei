package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.Coffer;
import net.joefoxe.hexerei.tileentity.CofferTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class CofferRenderer extends TileEntityRenderer<CofferTile> {

    public CofferRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(CofferTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {






        matrixStackIn.push();
        if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH) {
            matrixStackIn.translate(8D / 16D, 4D / 16D, 4D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        } else if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {
            matrixStackIn.translate(8D / 16D, 4D / 16D, 12D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(0));
        } else if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST) {
            matrixStackIn.translate(12D / 16D, 4D / 16D, 8D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        } else if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {
            matrixStackIn.translate(4D / 16D, 4D / 16D, 8D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        }
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE)));
        renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_LID.get().getDefaultState());
        matrixStackIn.pop();

        float sideRotation = (((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135);

        if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH || tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH) {

            matrixStackIn.push();
            matrixStackIn.translate(11.7299D / 16D, 2.4772D / 16D, 5.475D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(11.7299D / 16D, 2.4772D / 16D, 10.525D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(4.2701/ 16D, 2.4772D / 16D, 5.475D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(4.2701 / 16D, 2.4772D / 16D, 10.525D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.translate(1D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 1.75D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 5D / 16D);
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_CONTAINER.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(11D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 1.75D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 5D / 16D);
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_CONTAINER.get().getDefaultState());
            matrixStackIn.pop();
        }

        if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST || tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST) {

            matrixStackIn.push();
            matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.translate(11.7299D / 16D, 2.4772D / 16D, 5.475D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.translate(11.7299D / 16D, 2.4772D / 16D, 10.525D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.translate(4.2701/ 16D, 2.4772D / 16D, 5.475D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.translate(4.2701 / 16D, 2.4772D / 16D, 10.525D / 16D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_HINGE.get().getDefaultState());
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.translate(1D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 1.75D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 5D / 16D);
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_CONTAINER.get().getDefaultState());
            matrixStackIn.pop();
            matrixStackIn.push();
            matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.translate(11D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 1.75D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 5D / 16D);
            renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.COFFER_CONTAINER.get().getDefaultState());
            matrixStackIn.pop();
        }

        //render items only if its at least slightly opened
        if((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) > 2)
        {
            if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.NORTH)
                renderItemsNorth(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
            if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.WEST)
                renderItemsWest(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
            if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.SOUTH)
                renderItemsSouth(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
            if (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(HorizontalBlock.HORIZONTAL_FACING) == Direction.EAST)
                renderItemsEast(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn);

        }


    }

    private void renderItemsNorth(CofferTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                                  IRenderTypeBuffer bufferIn, int combinedLightIn)
    {
        float sideRotation = (((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135);

        // item row 1
        matrixStackIn.push();
        matrixStackIn.translate(4.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(0)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(6D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(1)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(7.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(2)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(3)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(4)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(5)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(9.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(6)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(11D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(7)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(12.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(8)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 2

        matrixStackIn.push();
        matrixStackIn.translate(4.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(9)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(6D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(10)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(7.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(11)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(9.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(12)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(11D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(13)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(12.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(14)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 3

        matrixStackIn.push();
        matrixStackIn.translate(4.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(15)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(6D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(16)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(7.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(17)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(9.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(18)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(11D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(19)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(12.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(20)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 4

        matrixStackIn.push();
        matrixStackIn.translate(4.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(21)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(6D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(22)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(7.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(23)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(9.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(24)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(11D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(25)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(12.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(26)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 5
        matrixStackIn.push();
        matrixStackIn.translate(4.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(27)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(6D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(28)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(7.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(29)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(30)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(31)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(32)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(9.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(33)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(11D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(34)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(12.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(35)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

    }

    private void renderItemsSouth(CofferTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                                 IRenderTypeBuffer bufferIn, int combinedLightIn)
    {
        float sideRotation = (((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135);

        // item row 1
        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(4.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(0)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(6D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(1)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(7.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(2)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(3)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(4)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(5)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(9.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(6)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(11D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(7)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(12.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(8)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 2

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(4.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(9)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(6D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(10)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(7.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(11)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(9.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(12)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(11D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(13)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(12.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(14)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 3

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(4.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(15)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(6D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(16)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(7.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(17)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(9.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(18)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(11D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(19)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(12.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(20)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 4

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(4.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(21)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(6D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(22)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(7.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(23)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(9.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(24)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(11D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(25)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(12.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(26)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 5
        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(4.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(27)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(6D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(28)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(7.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(29)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(30)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(31)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(32)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();


        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(9.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(33)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(11D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(34)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStackIn.translate(12.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(35)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

    }

    private void renderItemsWest(CofferTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                                 IRenderTypeBuffer bufferIn, int combinedLightIn)
    {
        float sideRotation = (((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135);

        // item row 1
        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(4.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(0)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(6D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(1)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(7.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(2)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(3)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(4)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(5)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(9.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(6)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(11D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(7)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(12.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(8)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 2

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(4.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(9)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(6D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(10)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(7.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(11)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(9.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(12)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(11D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(13)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(12.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(14)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 3

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(4.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(15)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(6D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(16)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(7.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(17)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(9.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(18)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(11D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(19)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(12.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(20)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 4

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(4.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(21)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(6D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(22)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(7.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(23)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(9.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(24)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(11D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(25)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(12.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(26)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 5
        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(4.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(27)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(6D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(28)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(7.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(29)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(30)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(31)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(32)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();


        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(9.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(33)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(11D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(34)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0D / 16D, 0D / 16D, 16D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(12.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(35)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

    }

    private void renderItemsEast(CofferTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                                 IRenderTypeBuffer bufferIn, int combinedLightIn)
    {
        float sideRotation = (((float)tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(Coffer.ANGLE) / (float)tileEntityIn.lidOpenAmount) * 135);

        // item row 1
        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(4.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(0)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(6D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(1)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(7.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(2)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(3)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(4)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(3D / 16D - Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(5)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(9.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(6)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(11D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(7)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(12.5D/16D, 0.15D, 6.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(8)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 2

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(4.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(9)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(6D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(10)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(7.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(11)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(9.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(12)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(11D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(13)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(12.5D/16D, 0.15D, 7.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(14)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 3

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(4.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(15)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(6D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(16)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(7.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(17)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(9.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(18)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(11D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(19)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(12.5D/16D, 0.15D, 8.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(20)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 4

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(4.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(21)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(6D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(22)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(7.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(23)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(9.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(24)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(11D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(25)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(12.5D/16D, 0.15D, 9.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(26)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        // item row 5
        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(4.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(27)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(6D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(28)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(7.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(29)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 7D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(30)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 8D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(31)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(13D / 16D + Math.sin(((sideRotation - 90)/180f)*Math.PI) * 3D/16D, 4D / 16D-(Math.cos(((sideRotation + 90)/180f)*Math.PI) * 3D/16D), 9D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(32)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();


        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(9.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(33)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(11D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(34)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(16D / 16D, 0D / 16D, 0D / 16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
        matrixStackIn.translate(12.5D/16D, 0.15D, 10.5D/16D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(22.5f));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
        matrixStackIn.scale(0.20f, 0.20f, 0.20f);
        renderItem(new ItemStack(tileEntityIn.getItemInSlot(35)), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
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

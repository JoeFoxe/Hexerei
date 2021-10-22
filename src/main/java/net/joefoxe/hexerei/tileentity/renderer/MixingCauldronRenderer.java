package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.tileentity.MixingCauldronTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.system.CallbackI;

public class MixingCauldronRenderer extends TileEntityRenderer<MixingCauldronTile> {

    private float degrees;
    public MixingCauldronRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
        degrees = 0.0f;
    }

    @Override
    public void render(MixingCauldronTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {



            for(int i = 0; i < 8; i++)
            {
                ItemStack item = new ItemStack(tileEntityIn.getItemInSlot(i));
                if (!item.isEmpty()) {
                    matrixStackIn.push();
                    matrixStackIn.translate(0.5D, 1.5D, 0.5D);
                    float currentTime = tileEntityIn.getWorld().getGameTime() + partialTicks;

                    if(tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.LEVEL) > 0) {
                        matrixStackIn.translate(
                                0D + Math.sin(0.8 * i) / 3.5,
                                -1.15D + (0.20 * (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.LEVEL) + (Math.sin(Math.PI * (currentTime) / 30 + (i * 20)) / 10))),
                                0D + Math.cos(0.8 * i) / 3.5);
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees((float)((45 * i) -1f + (2 * Math.sin((degrees + i * 20) / 40)))));
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float)(82.5f + (5 * Math.cos((degrees + i * 22) / 40)))  + tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.CRAFT_DELAY)));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float)(-2.5f + (5 * Math.cos((degrees + i * 24) / 40))) ));
                    } else {
                        matrixStackIn.translate(0D + Math.sin(0.8 * i) / 3.5,-1.15D,0D + Math.cos(0.8 * i) / 3.5);
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45 * i));
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float)(85f) + tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.CRAFT_DELAY)));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f ));
                    }

                    matrixStackIn.scale(0.4f, 0.4f, 0.4f);
                    renderItem(item, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.pop();
                }


            }
            degrees++;


    }

    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                            int combinedLightIn) {
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }


}

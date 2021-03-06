package net.joefoxe.hexerei.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.tileentity.MixingCauldronTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.system.CallbackI;

import java.util.Objects;

public class MixingCauldronRenderer extends TileEntityRenderer<MixingCauldronTile> {

    public MixingCauldronRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(MixingCauldronTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {


            // Rendering for the items inside the cauldron
            float craftPercent = 0;
            //Mixing the items
            if(Objects.requireNonNull(tileEntityIn.getWorld()).getBlockState(tileEntityIn.getPos()).hasTileEntity()) {
                craftPercent = tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.CRAFT_DELAY) / (float) MixingCauldronTile.craftDelayMax;
            }
            else return;

            for(int i = 0; i < 8; i++)
            {
                ItemStack item = new ItemStack(tileEntityIn.getItemInSlot(i));
                if (!item.isEmpty()) {
                    matrixStackIn.push();
                    matrixStackIn.translate(0.5D, 1.5D, 0.5D);
                    float currentTime = tileEntityIn.getWorld().getGameTime() + partialTicks;

                    //rotation offset when crafting
                    double itemRotationOffset = 0.8 * i + (craftPercent * (20f * craftPercent));
                    if(tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.LEVEL) > 0) {
                        matrixStackIn.translate(
                                0D + Math.sin(itemRotationOffset) / (3.5f + ((craftPercent * craftPercent) * 10.0f)),
                                -1.15D + (0.20 * (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.LEVEL) + (Math.sin(Math.PI * (currentTime) / 30 + (i * 20)) / 10))),
                                0D + Math.cos(itemRotationOffset)  / (3.5f + ((craftPercent * craftPercent) * 10.0f)));
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees((float)((45 * i) -1f + (2 * Math.sin((tileEntityIn.degrees + i * 20) / 40)))));
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float)(82.5f + (5 * Math.cos((tileEntityIn.degrees + i * 22) / 40)))));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float)(-2.5f + (5 * Math.cos((tileEntityIn.degrees + i * 24) / 40))) ));
                        matrixStackIn.scale(1 - (craftPercent * 0.5f), 1 - (craftPercent * 0.5f), 1 - (craftPercent * 0.5f));
                    } else {
                        matrixStackIn.translate(0D + Math.sin(itemRotationOffset) / 3.5,-1.15D,0D + Math.cos(itemRotationOffset) / 3.5);
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45 * i));
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(85f ) );
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f ));
                    }

                    matrixStackIn.scale(0.4f, 0.4f, 0.4f);
                    renderItem(item, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.pop();
                }


            }
            // output item
            ItemStack item2 = new ItemStack(tileEntityIn.getItemInSlot(8));
            if (!item2.isEmpty()) {

                matrixStackIn.push();
                matrixStackIn.translate(0.5D, 1.5D, 0.5D);
                float currentTime = tileEntityIn.getWorld().getGameTime() + partialTicks;

                if(tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.LEVEL) > 0) {
                    matrixStackIn.translate(
                            0D ,
                            -1.15D + (0.20 * (tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).get(MixingCauldron.LEVEL) + (Math.sin(Math.PI * (currentTime) / 30 + 20) / 10))),
                            0D );
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees((float)((45) -1f + (2 * Math.sin((tileEntityIn.degrees + 20) / 40))) - ((craftPercent * craftPercent) * 720f)));
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float)(82.5f + (5 * Math.cos((tileEntityIn.degrees + 22) / 40)))));
                    matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float)(-2.5f + (5 * Math.cos((tileEntityIn.degrees + 24) / 40)))));
                } else {
                    matrixStackIn.translate(0D,-1.15D,0D);
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45 - ((craftPercent * craftPercent) * 720f)));
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(85f));
                    matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-2.5f));
                }

                matrixStackIn.scale(0.4f, 0.4f, 0.4f);
                renderItem(item2, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
                matrixStackIn.pop();


            }

            //gives the wobble
            ((MixingCauldronTile)tileEntityIn).degrees++;
            if (tileEntityIn.getItemInSlot(9) == ModItems.BLOOD_SIGIL.get()) {
            if(tileEntityIn.getItemInSlot(9).getItem() == ModItems.BLOOD_SIGIL.get())
                {
                    matrixStackIn.push();
                    renderBlock(matrixStackIn, bufferIn, combinedLightIn, ModBlocks.BLOOD_SIGIL.get().getDefaultState());
                    matrixStackIn.pop();
                }
            }


    }
    // THIS IS WHAT I WAS LOOKING FOR FOREVER AHHHHH
    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                            int combinedLightIn) {
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }


    private void renderBlock(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, BlockState state) {
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

    }


}

package net.joefoxe.armormod.screen;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.joefoxe.armormod.ArmorMod;
import net.joefoxe.armormod.block.ModBlocks;
import net.joefoxe.armormod.container.MixingCauldronContainer;
import net.joefoxe.armormod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MixingCauldronScreen extends ContainerScreen<MixingCauldronContainer> {
    private final ResourceLocation GUI = new ResourceLocation(ArmorMod.MOD_ID,
            "textures/gui/mixing_cauldron_gui.png");

    public MixingCauldronScreen(MixingCauldronContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getTextureManager().bindTexture(GUI);

        ItemRenderer itemRenderer = minecraft.getItemRenderer();

        RenderSystem.disableDepthTest();
        itemRenderer.renderItemIntoGUI(new ItemStack(ModBlocks.MIXING_CAULDRON.get().asItem()),
                this.guiLeft + 80,
                this.guiTop - 25);
        RenderSystem.enableDepthTest();

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        this.blit(matrixStack, i + 75, j - 30, 189, 0, 26, 26);
        this.blit(matrixStack, i + 150, j  + 22, 176, 26, 44, 44);
        this.blit(matrixStack, i - 18, j  + 22, 176, 26, 44, 44);


        if(container.isLightningStorm()) {
            this.blit(matrixStack, i + 83, j + 31, 215, 0, 8, 25);
        }
    }
}
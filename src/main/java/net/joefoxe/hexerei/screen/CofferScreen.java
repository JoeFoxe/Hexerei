package net.joefoxe.hexerei.screen;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.container.CofferContainer;
import net.joefoxe.hexerei.container.MixingCauldronContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.sound.SoundEvent;

public class CofferScreen extends ContainerScreen<CofferContainer> {
    private final ResourceLocation GUI = new ResourceLocation(Hexerei.MOD_ID,
            "textures/gui/coffer_gui.png");
    private final ResourceLocation INVENTORY = new ResourceLocation(Hexerei.MOD_ID,
            "textures/gui/inventory.png");

    public CofferScreen(CofferContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        playerInventoryTitleY = 135;
        titleY = 1;
        titleX = 4;
    }

    @Override
    protected void init() {
        super.init();

//        this.addButton(new ImageButton(this.guiLeft + 188, this.guiTop , 18, 18, 230, 26, 18, GUI, (button) -> {
////            this.recipeBookGui.initSearchBar(this.widthTooNarrow);
////            this.recipeBookGui.toggleVisibility();
////            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
//            ((ImageButton)button).setPosition(this.guiLeft + 188, this.guiTop );
//        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        //Rendering the cauldron item on the top of the screen

        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getTextureManager().bindTexture(GUI);

        ItemRenderer itemRenderer = minecraft.getItemRenderer();

        RenderSystem.disableDepthTest();
        itemRenderer.renderItemIntoGUI(new ItemStack(ModBlocks.COFFER.get().asItem()),
                this.guiLeft + 99,
                this.guiTop - 25);

        InventoryScreen.drawEntityOnScreen(this.guiLeft + 107, this.guiTop + 88, 20, (float)(this.guiLeft + 107 - mouseX) , (float)(this.guiTop + 88 - 30 - mouseY), this.minecraft.player);

        RenderSystem.enableDepthTest();


        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public ITextComponent getTitle() {
        return super.getTitle();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j - 3, 0, 0, 214, 157);
        this.blit(matrixStack, i + 94, j - 30, 230, 0, 26, 26);
        if(this.container.getToggled() == 1)
        {
            this.blit(matrixStack, i + 188, j + 130, 230, 44, 18, 18);
        }
        this.minecraft.getTextureManager().bindTexture(INVENTORY);
        this.blit(matrixStack, i + 3, j + 129, 0, 0, 176, 100);



    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        boolean mouseClicked = super.mouseClicked(x, y, button);


        if(x > this.guiLeft + 188 && x < this.guiLeft + 188 + 18 &&  y > this.guiTop + 129 && y < this.guiTop + 129 + 18){
            this.container.setToggled(1 - this.container.getToggled());
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
//        this.container.playSound();

        return mouseClicked;
    }
}
package net.joefoxe.hexerei.client.renderer.color;

import net.joefoxe.hexerei.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IRegistryDelegate;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItemColors {
    private ModItemColors() {}
    // FORGE: Use RegistryDelegates as non-Vanilla item ids are not constant

    @SubscribeEvent
    public static void initItemColors(ColorHandlerEvent.Item event) {

        event.getItemColors().register((stack, color) -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            if(block instanceof LilyPadBlock) {
                return GrassColors.get(0.0D, 0.5D);
            }
            return 0;
        }, ModBlocks.LILY_PAD_BLOCK.get());

    }


}
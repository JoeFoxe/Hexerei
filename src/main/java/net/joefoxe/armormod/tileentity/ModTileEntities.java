package net.joefoxe.armormod.tileentity;

import net.joefoxe.armormod.ArmorMod;
import net.joefoxe.armormod.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ArmorMod.MOD_ID);

    public static RegistryObject<TileEntityType<MixingCauldronTile>> MIXING_CAULDRON_TILE = TILE_ENTITIES.register("mixing_cauldron_entity", () -> TileEntityType.Builder.create(MixingCauldronTile::new, ModBlocks.MIXING_CAULDRON.get()).build(null));


    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}

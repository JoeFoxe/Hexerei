package net.joefoxe.hexerei.world.structure.structures;

import java.util.Locale;
        import net.minecraft.nbt.CompoundNBT;
        import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public interface IModStructurePieceType {
    net.minecraft.world.gen.feature.structure.IStructurePieceType WITCH_HUT_PIECE = register(WitchHutPiece::new, "whPiece");

    StructurePiece load(TemplateManager p_load_1_, CompoundNBT p_load_2_);

    static net.minecraft.world.gen.feature.structure.IStructurePieceType register(net.minecraft.world.gen.feature.structure.IStructurePieceType type, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), type);
    }
}
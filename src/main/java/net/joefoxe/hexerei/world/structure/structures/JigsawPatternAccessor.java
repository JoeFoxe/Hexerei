package net.joefoxe.hexerei.world.structure.structures;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nonnull;
import java.util.List;

@Mixin(JigsawPattern.class)
public interface JigsawPatternAccessor {
    @Accessor("rawTemplates")
    List<Pair<JigsawPiece, Integer>> hexerei_getRawTemplates();

    @Mutable
    @Accessor("rawTemplates")
    void hexerei_setRawTemplates(List<Pair<JigsawPiece, Integer>> elementCounts);

//    @Accessor("templates")
//    List<JigsawPiece> hexerei_getTemplates();
//
//    @Mutable
//    @Accessor("templates")
//    void hexerei_setTemplates(List<JigsawPiece> elements);
}
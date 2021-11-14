package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.Skill;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class PigeonRegistries {

    public static IForgeRegistry<Skill> SKILLS;

    public static void newRegistry(RegistryEvent.NewRegistry event) {
        IForgeRegistry<Skill> SKILLS = makeRegistry("skills", Skill.class).create();
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(final String name, Class<T> type) {
        return new RegistryBuilder<T>().setName(HexereiUtil.getResource(name)).setType(type);
    }
}

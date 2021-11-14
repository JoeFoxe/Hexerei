package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.Skill;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.SkillInstance;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class PigeonSkills {

    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(Skill.class, HexereiConstants.MOD_ID);
//    public static final RegistryObject<Skill> BRAWLER = registerInst("brawler", BrawlerSkill::new);

    private static <T extends Skill> RegistryObject<Skill> registerInst(final String name, final BiFunction<Skill, Integer, SkillInstance> sup) {
        return register(name, () -> new Skill(sup));
    }

    private static <T extends Skill> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return SKILLS.register(name, sup);
    }
}


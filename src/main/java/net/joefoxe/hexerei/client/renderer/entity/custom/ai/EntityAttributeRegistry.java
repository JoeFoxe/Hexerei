package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.registries.IForgeRegistry;

public class EntityAttributeRegistry {

    public static IForgeRegistry<Skill> SKILLS;

    public static final Logger LOGGER = LogManager.getLogger("hexerei");
}
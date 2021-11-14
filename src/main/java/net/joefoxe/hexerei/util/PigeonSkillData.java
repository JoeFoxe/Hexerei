package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.BirdData;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.Skill;

public class PigeonSkillData extends BirdData {
    public final Skill skill;

    public PigeonSkillData(int entityId, Skill skill) {
        super(entityId);
        this.skill = skill;
    }
}
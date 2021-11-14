package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.BirdData;

public class PigeonObeyData extends BirdData {
    public final boolean obeyOthers;

    public PigeonObeyData(int entityId, boolean obeyOthers) {
        super(entityId);
        this.obeyOthers = obeyOthers;
    }
}
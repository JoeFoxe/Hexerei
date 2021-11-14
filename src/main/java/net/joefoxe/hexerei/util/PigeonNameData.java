package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.BirdData;

public class PigeonNameData extends BirdData {
    public final String name;

    public PigeonNameData(int entityId, String name) {
        super(entityId);
        this.name = name;
    }
}
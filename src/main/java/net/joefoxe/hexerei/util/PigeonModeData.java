package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.BirdData;

public class PigeonModeData extends BirdData {
    public EnumMode mode;

    public PigeonModeData(int entityId, EnumMode modeIn) {
        super(entityId);
        this.mode = modeIn;
    }
}
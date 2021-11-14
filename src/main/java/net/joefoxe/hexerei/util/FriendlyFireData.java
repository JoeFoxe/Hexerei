package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.BirdData;

public class FriendlyFireData extends BirdData {

    public final boolean friendlyFire;

    public FriendlyFireData(int entityId, boolean friendlyFire) {
        super(entityId);
        this.friendlyFire = friendlyFire;
    }
}
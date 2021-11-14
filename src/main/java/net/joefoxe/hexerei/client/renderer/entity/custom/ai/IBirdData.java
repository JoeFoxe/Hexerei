package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import java.util.UUID;

public interface IBirdData {

    public UUID getBirdId();

    public UUID getOwnerId();

    public String getBirdName();

    public String getOwnerName();
}
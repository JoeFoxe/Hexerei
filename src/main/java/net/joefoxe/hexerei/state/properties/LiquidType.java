package net.joefoxe.hexerei.state.properties;

import net.minecraft.util.IStringSerializable;

public enum LiquidType implements IStringSerializable {
    WATER("water"),
    LAVA("lava"),
    EMPTY("empty"),
    QUICKSILVER("quicksilver"),
    MILK("milk");

    private final String name;

    private LiquidType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getString() {
        return this.name;
    }
}
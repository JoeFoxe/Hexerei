package net.joefoxe.hexerei.state.properties;

import net.minecraft.util.IStringSerializable;

// types of fluid inside the cauldron because im not sure how to implement a fluid tank yet. SOON will change the cauldron to hold any kind of fluid
public enum LiquidType implements IStringSerializable {
    WATER("water"),
    LAVA("lava"),
    EMPTY("empty"),
    QUICKSILVER("quicksilver"),
    BLOOD("blood"),
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
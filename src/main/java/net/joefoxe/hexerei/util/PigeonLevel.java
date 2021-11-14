package net.joefoxe.hexerei.util;

public class PigeonLevel {

    private int level;

    public static enum Type {
        NORMAL("normal_treat");

        String n;

        Type(String n) {
            this.n = n;
        }

        public String getName() {
            return this.n;
        }
    }

    public PigeonLevel(int level) {
        this.level = level;
    }

    public int getLevel(Type type) {
        return this.level;
    }

    public boolean canIncrease(Type type) {
        return this.level >= 60;
    }

    @Deprecated
    public void setLevel(int level) {

            this.level = level;

    }

    @Deprecated
    public void incrementLevel(Type type) {
        this.setLevel(this.getLevel(type) + 1);
    }

    public PigeonLevel copy() {
        return new PigeonLevel(this.level);
    }

    /**
     * Combines parents levels together
     */
    public PigeonLevel combine(PigeonLevel levelIn) {
        int combinedLevel = this.getLevel(Type.NORMAL) + levelIn.getLevel(Type.NORMAL);
        combinedLevel /= 2;
        combinedLevel = Math.min(combinedLevel, 20);
        return new PigeonLevel(combinedLevel);
    }

}

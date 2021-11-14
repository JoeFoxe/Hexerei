package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import java.util.Optional;
import java.util.function.Supplier;

import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IRegistryDelegate;

public class SkillInstance implements IPigeonAttributeModifier {
    protected final IRegistryDelegate<Skill> skillDelegate;
    protected int level;
    public SkillInstance(Skill SkillIn, int levelIn) {
        this(SkillIn.delegate, levelIn);
    }
    public SkillInstance(Skill SkillIn) {
        this(SkillIn.delegate, 1);
    }
    public SkillInstance(IRegistryDelegate<Skill> skillDelegateIn, int levelIn) {
        this.skillDelegate = skillDelegateIn;
        this.level = levelIn;
    }
    public Skill getSkill() {
        return this.skillDelegate.get();
    }
    public final int level() {
        return this.level;
    }
    public final void setLevel(int levelIn) {
        this.level = levelIn;
    }
    public boolean of(Supplier<Skill> SkillIn) {
        return this.of(SkillIn.get());
    }
    public boolean of(Skill SkillIn) {
        return this.of(SkillIn.delegate);
    }
    public boolean of(IRegistryDelegate<Skill> skillDelegateIn) {
        return skillDelegateIn.equals(this.skillDelegate);
    }
    public SkillInstance copy() {
        return this.skillDelegate.get().getDefault(this.level);
    }
    public void writeToNBT(AbstractPigeonEntity canisIn, CompoundNBT compound) {compound.putInt("level", this.level());}
    public void readFromNBT(AbstractPigeonEntity canisIn, CompoundNBT compound) {this.setLevel(compound.getInt("level"));}
    public void writeToBuf(PacketBuffer buf) {
        buf.writeInt(this.level());
    }
    public void readFromBuf(PacketBuffer buf) {
        this.setLevel(buf.readInt());
    }
    public final void writeInstance(AbstractPigeonEntity canisIn, CompoundNBT compound) {
        ResourceLocation rl = this.skillDelegate.name();
        if (rl != null) {
            compound.putString("type", rl.toString());
        }
        this.writeToNBT(canisIn, compound);
    }

    public static Optional<SkillInstance> readInstance(AbstractPigeonEntity canisIn, CompoundNBT compound) {
        ResourceLocation rl = ResourceLocation.tryCreate(compound.getString("type"));
        if (EntityAttributeRegistry.SKILLS.containsKey(rl)) {
            SkillInstance inst = EntityAttributeRegistry.SKILLS.getValue(rl).getDefault();
            inst.readFromNBT(canisIn, compound);
            return Optional.of(inst);
        } else {
            EntityAttributeRegistry.LOGGER.warn("Failed to load Skill {}", rl);
            return Optional.empty();
        }
    }
    @SuppressWarnings("unchecked")
    public <T extends SkillInstance> T cast(Class<T> type) {
        if (this.getClass().isAssignableFrom(type)) {
            return (T) this;
        } else {
            throw new RuntimeException("Could not cast " + this.getClass().getName() + " to " + type.getName());
        }
    }
    @Override public String toString() {return String.format("%s [skill: %s, level: %d]", this.getClass().getSimpleName(), skillDelegate.name()/*, this.level*/);}

    public void init(AbstractPigeonEntity pidgeonIn) {}
    public void set(AbstractPigeonEntity pidgeonIn, int levelBefore) {}

    public boolean hasRenderer() {
        return this.getSkill().hasRenderer();
    }
}
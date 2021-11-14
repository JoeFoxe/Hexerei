package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.SkillInstance;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.ArrayList;
import java.util.List;

public class SkillListSerializer implements IDataSerializer<List<SkillInstance>> {

    @Override
    public void write(PacketBuffer buf, List<SkillInstance> value) {
        buf.writeInt(value.size());
        for (SkillInstance inst : value) {
            buf.writeRegistryIdUnsafe(PigeonRegistries.SKILLS, inst.getSkill());
            inst.writeToBuf(buf);
        }
    }

    @Override
    public List<SkillInstance> read(PacketBuffer buf) {
        int size = buf.readInt();
        List<SkillInstance> newInst = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SkillInstance inst = buf.readRegistryIdUnsafe(PigeonRegistries.SKILLS).getDefault();
            inst.readFromBuf(buf);
            newInst.add(inst);
        }
        return newInst;
    }

    @Override
    public DataParameter<List<SkillInstance>> createKey(int id) {
        return new DataParameter<>(id, this);
    }

    @Override
    public List<SkillInstance> copyValue(List<SkillInstance> value) {
        List<SkillInstance> newInst = new ArrayList<>(value.size());

        for (SkillInstance inst : value) {
            newInst.add(inst.copy());
        }
        return newInst;
    }
}
package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.Skill;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PigeonSkillPacket extends BirdPacket<PigeonSkillData> {

    @Override
    public void encode(PigeonSkillData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeRegistryIdUnsafe(PigeonRegistries.SKILLS, data.skill);
    }

    @Override
    public PigeonSkillData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        Skill skill = buf.readRegistryIdUnsafe(PigeonRegistries.SKILLS);
        return new PigeonSkillData(entityId, skill);
    }

    @Override
    public void handleBird(PigeonEntity canisIn, PigeonSkillData data, Supplier<NetworkEvent.Context> ctx) {
        if (!canisIn.canInteract(ctx.get().getSender())) {
            return;
        }
//        if (!ConfigHandler.Skill.getFlag(data.skill)) {
//            RigoranthusEmortisReborn.LOGGER.info("{} tried to level a disabled skill ({})",
//                    ctx.get().getSender().getGameProfile().getName(),
//                    data.skill.getRegistryName());
//            return;
//        }
        int level = canisIn.getLevel(data.skill);

        if (level < data.skill.getMaxLevel() && canisIn.canSpendPoints(data.skill.getLevelCost(level + 1))) {
            canisIn.setSkillLevel(data.skill, level + 1);
        }
    }
}
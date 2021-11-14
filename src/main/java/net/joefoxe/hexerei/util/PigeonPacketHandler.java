package net.joefoxe.hexerei.util;

import net.joefoxe.hexerei.Hexerei;
import net.minecraftforge.fml.network.PacketDistributor;



public class PigeonPacketHandler {

    private static int disc = 0;

    public static void init() {
        registerPacket(new PigeonModePacket(), PigeonModeData.class);
        registerPacket(new PigeonNamePacket(), PigeonNameData.class);
        registerPacket(new PigeonObeyPacket(), PigeonObeyData.class);
        registerPacket(new PigeonSkillPacket(), PigeonSkillData.class);
        registerPacket(new FriendlyFirePacket(), FriendlyFireData.class);
        registerPacket(new OpenPigeonScreenPacket(), OpenPigeonScreenData.class);
        registerPacket(new PigeonInventoryPagePacket(), PigeonInventoryPageData.class);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        Hexerei.HANDLER.send(target, message);
    }

    public static <D> void registerPacket(IHexPacket<D> packet, Class<D> dataClass) {
        Hexerei.HANDLER.registerMessage(PigeonPacketHandler.disc++, dataClass, packet::encode, packet::decode, packet::handle);
    }
}

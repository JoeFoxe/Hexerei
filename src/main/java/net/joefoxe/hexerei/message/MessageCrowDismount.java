package net.joefoxe.hexerei.message;

import net.joefoxe.hexerei.client.renderer.entity.custom.CrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCrowDismount {

    public int rider;
    public int mount;

    public MessageCrowDismount(int rider, int mount) {
        this.rider = rider;
        this.mount = mount;
    }

    public MessageCrowDismount() {
    }

    public static MessageCrowDismount read(PacketBuffer buf) {
        return new MessageCrowDismount(buf.readInt(), buf.readInt());
    }

    public static void write(MessageCrowDismount message, PacketBuffer buf) {
        buf.writeInt(message.rider);
        buf.writeInt(message.mount);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageCrowDismount message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
//                player = AlexsMobs.PROXY.getClientSidePlayer();
            }

            if (player != null) {
                if (player.world != null) {
                    Entity entity = player.world.getEntityByID(message.rider);
                    Entity mountEntity = player.world.getEntityByID(message.mount);
                    if (entity instanceof CrowEntity && mountEntity != null) {
                        entity.stopRiding();
                    }
                }
            }
        }
    }
}
package net.joefoxe.hexerei.util.message;


import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAccelerating implements IMessage<MessageAccelerating>
{
    private BroomEntity.AccelerationDirection acceleration;

    public MessageAccelerating() {}

    public MessageAccelerating(BroomEntity.AccelerationDirection acceleration)
    {
        this.acceleration = acceleration;
    }

    @Override
    public void encode(MessageAccelerating message, PacketBuffer buffer)
    {
        buffer.writeEnumValue(message.acceleration);
    }

    @Override
    public MessageAccelerating decode(PacketBuffer buffer)
    {
        return new MessageAccelerating(buffer.readEnumValue(BroomEntity.AccelerationDirection.class));
    }

    @Override
    public void handle(MessageAccelerating message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                Entity riding = player.getRidingEntity();
                if(riding instanceof BroomEntity)
                {
//                    ((BroomEntity) riding).setAcceleration(message.acceleration);

//                    if(message.acceleration == BroomEntity.AccelerationDirection.FORWARD)
//                        riding.addVelocity(Math.sin((((BroomEntity) riding).rotationYaw+180f)/180f * Math.PI),0,-Math.cos((((BroomEntity) riding).rotationYaw+180f)/180f * Math.PI));
//                    if(message.acceleration == BroomEntity.AccelerationDirection.REVERSE)
//                        riding.addVelocity(-Math.sin((((BroomEntity) riding).rotationYaw+180f)/180f * Math.PI),0,Math.cos((((BroomEntity) riding).rotationYaw+180f)/180f * Math.PI));
////                    ((BroomEntity) riding).setMotion(-Math.sin((((BroomEntity) riding).rotationYaw+180f)/180f * Math.PI),0,Math.cos((((BroomEntity) riding).rotationYaw+180f)/180f * Math.PI));
//                    System.out.println(message.acceleration);
//                    riding.setNoGravity(true);
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
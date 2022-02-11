package com.theycallmeapp.reddragon.common.entity.network.message;

import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ControlMessageJumping {

    boolean isJumping;

    public ControlMessageJumping() {

    }

    public ControlMessageJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public static void encode(ControlMessageJumping message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.isJumping);
    }

    public static ControlMessageJumping decode(FriendlyByteBuf buffer) {
        return new ControlMessageJumping(buffer.readBoolean());
    }

    public static void handle(ControlMessageJumping message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player.getVehicle() instanceof RedDragonEntity) {
                RedDragonEntity dragon = (RedDragonEntity) player.getVehicle();
                dragon.setIsJumping(message.isJumping);
            }
        });
        context.setPacketHandled(true);

    }
}

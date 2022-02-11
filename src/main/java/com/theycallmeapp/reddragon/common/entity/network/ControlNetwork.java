package com.theycallmeapp.reddragon.common.entity.network;

import com.theycallmeapp.reddragon.RedDragonMod;
import com.theycallmeapp.reddragon.common.entity.network.message.ControlMessageGoingDown;
import com.theycallmeapp.reddragon.common.entity.network.message.ControlMessageJumping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ControlNetwork {

    public static final String NETWORK_VERSION = "0.1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(RedDragonMod.MOD_ID, "control"), () -> NETWORK_VERSION, NETWORK_VERSION::equals, NETWORK_VERSION::equals);

    public static void init() {
        INSTANCE.registerMessage(0, ControlMessageJumping.class, ControlMessageJumping::encode, ControlMessageJumping::decode, ControlMessageJumping::handle);
        INSTANCE.registerMessage(1, ControlMessageGoingDown.class, ControlMessageGoingDown::encode, ControlMessageGoingDown::decode, ControlMessageGoingDown::handle);
    }
}

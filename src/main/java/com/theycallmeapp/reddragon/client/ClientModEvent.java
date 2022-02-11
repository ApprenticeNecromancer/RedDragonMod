package com.theycallmeapp.reddragon.client;

import com.theycallmeapp.reddragon.RedDragonMod;
import com.theycallmeapp.reddragon.client.render.RedDragonRenderer;
import com.theycallmeapp.reddragon.init.EntityInit;
import com.theycallmeapp.reddragon.init.KeyBindsInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RedDragonMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvent {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.RED_DRAGON.get(), RedDragonRenderer::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        KeyBindsInit.register(event);
    }


}

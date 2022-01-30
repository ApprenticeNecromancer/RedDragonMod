package com.theycallmeapp.reddragon.init.event;

import com.theycallmeapp.reddragon.RedDragonMod;
import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import com.theycallmeapp.reddragon.init.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RedDragonMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityInit.RED_DRAGON.get(), RedDragonEntity.createAttributes().build());

    }
}

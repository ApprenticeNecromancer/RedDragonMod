package com.theycallmeapp.reddragon.init;

import com.theycallmeapp.reddragon.RedDragonMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class KeyBindsInit {

    public static KeyMapping keyDown;

    public static void register(FMLClientSetupEvent event) {
        keyDown = create("keyDown", GLFW.GLFW_KEY_LEFT_ALT);

        ClientRegistry.registerKeyBinding(keyDown);

    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + RedDragonMod.MOD_ID + "." + name, key, "key.category." + RedDragonMod.MOD_ID);
    }

}

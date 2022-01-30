package com.theycallmeapp.reddragon.init;

import com.theycallmeapp.reddragon.RedDragonMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemInit {

    private ItemInit() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RedDragonMod.MOD_ID);

    public static final RegistryObject<ForgeSpawnEggItem> RED_DRAGON_SPAWN_EGG = ITEMS.register("red_dragon_spawn_egg", () -> new ForgeSpawnEggItem(EntityInit.RED_DRAGON, 0xBF3102, 0x9DBF15, new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64)));

}

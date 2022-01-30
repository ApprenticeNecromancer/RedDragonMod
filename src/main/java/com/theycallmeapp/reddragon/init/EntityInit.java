package com.theycallmeapp.reddragon.init;

import com.theycallmeapp.reddragon.RedDragonMod;
import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {

    private EntityInit() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RedDragonMod.MOD_ID);

    public static final RegistryObject<EntityType<RedDragonEntity>> RED_DRAGON = ENTITIES.register("red_dragon",
            () -> EntityType.Builder.of(RedDragonEntity::new, MobCategory.CREATURE).sized(2.5f, 2.0f)
                    .build(new ResourceLocation(RedDragonMod.MOD_ID, "red_dragon").toString()));

}

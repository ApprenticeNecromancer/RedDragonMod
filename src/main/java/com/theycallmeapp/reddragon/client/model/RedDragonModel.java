package com.theycallmeapp.reddragon.client.model;

import com.theycallmeapp.reddragon.RedDragonMod;
import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RedDragonModel extends AnimatedGeoModel<RedDragonEntity> {


    @Override
    public ResourceLocation getModelLocation(RedDragonEntity entity) {

        return new ResourceLocation(RedDragonMod.MOD_ID, "geo/reddragon.geo.json");

    }

    @Override
    public ResourceLocation getTextureLocation(RedDragonEntity entity) {
        switch (entity.getDragonVariant()) {
            default:
            case 0:
                return new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragon.png");
            case 1:
                return new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragon1.png");
            case 2:
                return new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragon2.png");
            case 3:
                return new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragon3.png");

        }

    }

    @Override
    public ResourceLocation getAnimationFileLocation(RedDragonEntity entity) {
        if (entity.isBaby()) {
            return new ResourceLocation(RedDragonMod.MOD_ID, "animations/babydragon.animation.json");
        } else {
            return new ResourceLocation(RedDragonMod.MOD_ID, "animations/dragon.animation.json");
        }
    }

    @Override
    public void setLivingAnimations(RedDragonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if (entity.isBaby()) {

            IBone neck = this.getAnimationProcessor().getBone("neckBcontr");
            IBone head = this.getAnimationProcessor().getBone("headBcontr");
            IBone body = this.getAnimationProcessor().getBone("Body");
            IBone babybody = this.getAnimationProcessor().getBone("bodyB");

            LivingEntity entityIn = entity;

            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 2);
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 2);
            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 2);
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 2);

            body.setHidden(true);
            babybody.setHidden(false);

        } else {

            IBone head = this.getAnimationProcessor().getBone("HeadTrack");
            IBone neck1 = this.getAnimationProcessor().getBone("neckcontr1");
            IBone neck2 = this.getAnimationProcessor().getBone("neckcontr2");

            IBone bodyrotx = this.getAnimationProcessor().getBone("BodyRotx");

            IBone body = this.getAnimationProcessor().getBone("Body");
            IBone babybody = this.getAnimationProcessor().getBone("bodyB");

//            IBone tail = this.getAnimationProcessor().getBone("Tail1");




            LivingEntity entityIn = entity;
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 3);
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 3);
            neck1.setRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 3);
            neck1.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 3);
            neck2.setRotationX(extraData.headPitch * ((float) Math.PI / 180F) / 3);
            neck2.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F) / 3);

//            bodyrotx.setRotationX(extraData.headPitch * ((float) Math.PI / 110F));
//            bodyrotx.setRotationX(Mth.lerp(customPredicate.getPartialTick(), extraData.headPitch, extraData.headPitch) / 40);

            body.setHidden(false);
            babybody.setHidden(true);
        }

        // if (!!entity.isBaby() && entity.isSaddled())
        //    show saddle cubes
    }
}

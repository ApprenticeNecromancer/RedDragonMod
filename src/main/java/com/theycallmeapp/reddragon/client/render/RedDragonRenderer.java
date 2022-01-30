package com.theycallmeapp.reddragon.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.theycallmeapp.reddragon.client.model.RedDragonModel;
import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RedDragonRenderer extends GeoEntityRenderer<RedDragonEntity> {

    public RedDragonRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new RedDragonModel());
        this.addLayer(new DragonMarkingLayer(this));
    }

    @Override
    public RenderType getRenderType(RedDragonEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(getTextureLocation(animatable));
    }
}

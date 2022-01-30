package com.theycallmeapp.reddragon.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.theycallmeapp.reddragon.RedDragonMod;
import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;


@OnlyIn(Dist.CLIENT)
public class DragonMarkingLayer extends GeoLayerRenderer<RedDragonEntity> {

    private static final ResourceLocation DRAGON_MODEL = new ResourceLocation(RedDragonMod.MOD_ID, "geo/reddragon.geo.json");
    private static final ResourceLocation[] DRAGON_LAYER_TEXTURE = new ResourceLocation[]{new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragonlayer1.png"), new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragonlayer2.png"), new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragonlayer3.png"), new ResourceLocation(RedDragonMod.MOD_ID,"textures/model/entity/dragonlayer4.png")};

    public DragonMarkingLayer(IGeoRenderer<RedDragonEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, RedDragonEntity pLivingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        int patternint = pLivingEntity.getDragonOverlay();
        ResourceLocation DRAGON_LAYER = DRAGON_LAYER_TEXTURE[patternint];

        RenderType pattern = RenderType.entityTranslucent(DRAGON_LAYER);
        this.getRenderer().render(this.getEntityModel().getModel(DRAGON_MODEL), pLivingEntity, partialTicks, pattern, matrixStackIn, bufferIn, bufferIn.getBuffer(pattern), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
}

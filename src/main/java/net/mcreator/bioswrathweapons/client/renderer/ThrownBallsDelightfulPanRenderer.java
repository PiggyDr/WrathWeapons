package net.mcreator.bioswrathweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.model.BallsDelightfulPanModel;
import net.mcreator.bioswrathweapons.client.model.ballsdelightfulpanthrown;
import net.mcreator.bioswrathweapons.entity.ThrownBallsDelightfulPan;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class ThrownBallsDelightfulPanRenderer extends EntityRenderer<ThrownBallsDelightfulPan> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(BiosWrathWeaponsMod.MODID, "textures/block/ballsdelightfulpan.png");
    private final ballsdelightfulpanthrown<ThrownBallsDelightfulPan> model;

    public ThrownBallsDelightfulPanRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ballsdelightfulpanthrown<>(context.bakeLayer(ballsdelightfulpanthrown.LAYER_LOCATION));
    }

    public void render(ThrownBallsDelightfulPan entity, float idk1, float idk2, PoseStack poseStack, MultiBufferSource source, int idk3) {
        poseStack.pushPose();
//        p_116114_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.yRotO, p_116111_.getYRot()) - 90.0F));
//        p_116114_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.xRotO, p_116111_.getXRot()) + 90.0F));
//        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, p_116111_.isFoil());
//        this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //VertexConsumer vertexConsumer = source.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(source, this.model.renderType(TEXTURE_LOCATION), false, false);
        this.model.renderToBuffer(poseStack, vertexConsumer, idk3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        super.render(entity, idk1, idk2, poseStack, source, idk3);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownBallsDelightfulPan pan) {
        return TEXTURE_LOCATION;
    }
}

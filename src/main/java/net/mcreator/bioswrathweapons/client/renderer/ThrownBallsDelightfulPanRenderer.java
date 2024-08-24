package net.mcreator.bioswrathweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.model.BallsDelightfulPanModel;
import net.mcreator.bioswrathweapons.client.model.ballsdelightfulpanthrown;
import net.mcreator.bioswrathweapons.entity.ThrownBallsDelightfulPan;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
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

    public void render(ThrownBallsDelightfulPan entity, float yRotInterpolationThingy, float partialTick, PoseStack poseStack, MultiBufferSource source, int packedLight) {
        poseStack.pushPose();
//        //could probably do this directly in the model but im lazy
        poseStack.translate(0.0, 0.25, 0.0);
//
//        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
//        //rotate to motion
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
//        //correct way to throw a frisbee
//
//        poseStack.scale(2F, 2F, 2F);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(source, this.model.renderType(TEXTURE_LOCATION), false, entity.isFoil());
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        super.render(entity, yRotInterpolationThingy, partialTick, poseStack, source, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownBallsDelightfulPan pan) {
        return TEXTURE_LOCATION;
    }
}

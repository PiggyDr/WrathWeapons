package net.mcreator.bioswrathweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.model.ballsdelightfulpanthrown;
import net.mcreator.bioswrathweapons.entity.ThrownBallsDelightfulPan;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownBallsDelightfulPanRenderer extends EntityRenderer<ThrownBallsDelightfulPan> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(BiosWrathWeaponsMod.MODID, "textures/block/ballsdelightfulpan.png");
    private final ballsdelightfulpanthrown<ThrownBallsDelightfulPan> model;

    public ThrownBallsDelightfulPanRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ballsdelightfulpanthrown<>(context.bakeLayer(ballsdelightfulpanthrown.LAYER_LOCATION));
    }

    public void render(ThrownBallsDelightfulPan entity, float yRotInterpolationThingy, float partialTick, PoseStack poseStack, MultiBufferSource source, int packedLight) {
        poseStack.pushPose();
        //could probably do this directly in the model but im lazy
        poseStack.translate(0.0, 0.25, 0.0);

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        float rot = Mth.lerp(partialTick,
                ((entity.level().getGameTime() % 50) * (360F / 50)) + entity.getInitialYRot(),
                (((entity.level().getGameTime() + 1 ) % 50) * (360F / 50)) + entity.getInitialYRot());
        poseStack.mulPose(Axis.ZP.rotationDegrees(rot));
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

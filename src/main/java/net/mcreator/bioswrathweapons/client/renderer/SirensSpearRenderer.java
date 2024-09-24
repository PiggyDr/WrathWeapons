package net.mcreator.bioswrathweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.model.SirensSpearModel;
import net.mcreator.bioswrathweapons.entity.ThrownSirensSpear;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SirensSpearRenderer extends EntityRenderer<ThrownSirensSpear> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(BiosWrathWeaponsMod.MODID, "textures/item/sirens_spear.png");
    private final SirensSpearModel<ThrownSirensSpear> model;


    public SirensSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SirensSpearModel<>(context.bakeLayer(SirensSpearModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownSirensSpear trident) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(ThrownSirensSpear entity, float yRotInterpolationThingy, float partialTick, PoseStack poseStack, MultiBufferSource source, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(source, this.model.renderType(TEXTURE_LOCATION), false, entity.isFoil());
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, yRotInterpolationThingy, partialTick, poseStack, source, packedLight);
    }
}

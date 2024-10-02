package net.mcreator.bioswrathweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.model.SirensSpearModel;
import net.mcreator.bioswrathweapons.entity.ThrownSirensSpear;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SirensSpearRenderer extends EntityRenderer<ThrownSirensSpear> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(BiosWrathWeaponsMod.MODID, "textures/item/sirens_spear.png");
    private final SirensSpearModel<ThrownSirensSpear> model;
    private final ItemRenderer itemRenderer;


    public SirensSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SirensSpearModel<>(context.bakeLayer(SirensSpearModel.LAYER_LOCATION));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownSirensSpear trident) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(ThrownSirensSpear entity, float yRotInterpolationThingy, float partialTick, PoseStack poseStack, MultiBufferSource source, int packedLight) {

        //item model stuff
        ItemStack itemStack = entity.getItem();
        if (itemStack.isEmpty()) return;
        BakedModel itemModel = itemRenderer.getModel(itemStack, entity.level(), null, 0);

        //transformations
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) - 90.0F));

        //rendering
        this.itemRenderer.render(entity.getItem(), ItemDisplayContext.NONE, false, poseStack, source, packedLight, OverlayTexture.NO_OVERLAY, itemModel);

        poseStack.popPose();
        super.render(entity, yRotInterpolationThingy, partialTick, poseStack, source, packedLight);
    }
}

package net.mcreator.bioswrathweapons.client.model;
// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ballsdelightfulpanthrown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ballsdelightfulpanthrown"), "main");
	private final ModelPart Pan;

	public ballsdelightfulpanthrown(ModelPart root) {
		this.Pan = root.getChild("Pan");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Pan = partdefinition.addOrReplaceChild("Pan", CubeListBuilder.create().texOffs(0, 0).addBox(-5.2F, -7.0F, -0.3F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 15).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 15).addBox(-0.5F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 15).addBox(-5.2F, -7.0F, -1.3F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(3.8F, -7.0F, -1.3F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-4.2F, -7.0F, -1.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(-4.2F, 2.0F, -1.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Pan.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
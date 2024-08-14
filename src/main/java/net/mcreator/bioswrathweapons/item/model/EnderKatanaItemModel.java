package net.mcreator.bioswrathweapons.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.bioswrathweapons.item.EnderKatanaItem;

public class EnderKatanaItemModel extends GeoModel<EnderKatanaItem> {
	@Override
	public ResourceLocation getAnimationResource(EnderKatanaItem animatable) {
		return new ResourceLocation("bios_wrath_weapons", "animations/enderblade.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EnderKatanaItem animatable) {
		return new ResourceLocation("bios_wrath_weapons", "geo/enderblade.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EnderKatanaItem animatable) {
		return new ResourceLocation("bios_wrath_weapons", "textures/item/enderkatana.png");
	}
}

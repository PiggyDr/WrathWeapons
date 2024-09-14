package net.mcreator.bioswrathweapons.item.model;

import net.mcreator.bioswrathweapons.item.EnderKatanaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

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

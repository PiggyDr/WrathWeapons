package net.mcreator.bioswrathweapons.item.model;

import net.mcreator.bioswrathweapons.item.PuristSwordItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PuristSwordItemModel extends GeoModel<PuristSwordItem> {
	@Override
	public ResourceLocation getAnimationResource(PuristSwordItem animatable) {
		return new ResourceLocation("bios_wrath_weapons", "animations/purist_sword.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(PuristSwordItem animatable) {
		return new ResourceLocation("bios_wrath_weapons", "geo/purist_sword.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(PuristSwordItem animatable) {
		return new ResourceLocation("bios_wrath_weapons", "textures/item/purist_sword.png");
	}
}

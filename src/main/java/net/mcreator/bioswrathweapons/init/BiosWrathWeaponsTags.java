package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BiosWrathWeaponsTags {
    public static TagKey<DamageType> IS_WATER = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BiosWrathWeaponsMod.MODID, "is_water"));
}

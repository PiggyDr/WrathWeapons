package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class BiosWrathWeaponsTags {
    public static final TagKey<Item> SENDS_ATTACK_PACKET = ItemTags.create(new ResourceLocation(BiosWrathWeaponsMod.MODID, "sends_attack_packet"));
    public static final TagKey<DamageType> HAS_CUSTOM_DAMAGE_REDUCTION = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BiosWrathWeaponsMod.MODID, "has_custom_damage_reduction"));
    public static final TagKey<Item> SAVE_COOLDOWNS = ItemTags.create(new ResourceLocation(BiosWrathWeaponsMod.MODID, "save_cooldowns"));
    public static final TagKey<EntityType<?>> SIRENS_ESSENCE_PREVENTS_AGGRO_OF = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(BiosWrathWeaponsMod.MODID, "sirens_essence_prevents_aggro_of"));

    public static final TagKey<Item> ORIGINS_MEAT = ItemTags.create(new ResourceLocation("origins", "meat"));
}

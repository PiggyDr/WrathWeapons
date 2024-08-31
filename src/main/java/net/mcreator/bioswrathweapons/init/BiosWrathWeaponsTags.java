package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BiosWrathWeaponsTags {
    public static final TagKey<Item> SENDS_ATTACK_PACKET = ItemTags.create(new ResourceLocation(BiosWrathWeaponsMod.MODID, "sends_attack_packet"));
}


/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.bioswrathweapons.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import net.mcreator.bioswrathweapons.item.UnhoolyHammerTimeItem;
import net.mcreator.bioswrathweapons.item.SculkSaberItem;
import net.mcreator.bioswrathweapons.item.SculkCleaverItem;
import net.mcreator.bioswrathweapons.item.PuristSwordItem;
import net.mcreator.bioswrathweapons.item.HighElfBladeItem;
import net.mcreator.bioswrathweapons.item.GodsToolItem;
import net.mcreator.bioswrathweapons.item.EnderKatanaItem;
import net.mcreator.bioswrathweapons.item.DwarvenLegacyItem;
import net.mcreator.bioswrathweapons.item.BladeofthePixieItem;
import net.mcreator.bioswrathweapons.item.BallsDelightfulPanItem;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;

public class BiosWrathWeaponsModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BiosWrathWeaponsMod.MODID);
	public static final RegistryObject<Item> UNHOOLY_HAMMER_TIME = REGISTRY.register("unhooly_hammer_time", () -> new UnhoolyHammerTimeItem());
	public static final RegistryObject<Item> PURIST_SWORD = REGISTRY.register("purist_sword", () -> new PuristSwordItem());
	public static final RegistryObject<Item> SCULK_CLEAVER = REGISTRY.register("sculk_cleaver", () -> new SculkCleaverItem());
	public static final RegistryObject<Item> SCULK_SABER = REGISTRY.register("sculk_saber", () -> new SculkSaberItem());
	public static final RegistryObject<Item> DWARVEN_LEGACY = REGISTRY.register("dwarven_legacy", () -> new DwarvenLegacyItem());
	public static final RegistryObject<Item> BALLS_DELIGHTFUL_PAN = REGISTRY.register("balls_delightful_pan", () -> new BallsDelightfulPanItem());
	public static final RegistryObject<Item> ENDER_KATANA = REGISTRY.register("ender_katana", () -> new EnderKatanaItem());
	public static final RegistryObject<Item> HIGH_ELF_BLADE = REGISTRY.register("high_elf_blade", () -> new HighElfBladeItem());
	public static final RegistryObject<Item> BLADEOFTHE_PIXIE = REGISTRY.register("bladeofthe_pixie", () -> new BladeofthePixieItem());
	public static final RegistryObject<Item> GODS_TOOL = REGISTRY.register("gods_tool", () -> new GodsToolItem());
	// Start of user code block custom items
	// End of user code block custom items
}

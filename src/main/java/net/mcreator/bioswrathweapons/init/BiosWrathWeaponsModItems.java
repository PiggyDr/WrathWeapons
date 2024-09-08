
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import top.theillusivec4.curios.api.CuriosApi;

public class BiosWrathWeaponsModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BiosWrathWeaponsMod.MODID); //TODO make properties more consistent
	public static final RegistryObject<Item> UNHOOLY_HAMMER_TIME = REGISTRY.register("unhooly_hammer_time", () -> new UnhoolyHammerTimeItem());
	public static final RegistryObject<Item> PURIST_SWORD = REGISTRY.register("purist_sword", () -> new PuristSwordItem());
	public static final RegistryObject<Item> SCULK_CLEAVER = REGISTRY.register("sculk_cleaver", () -> new SculkCleaverItem());
	public static final RegistryObject<Item> SCULK_SABER = REGISTRY.register("sculk_saber", () -> new SculkSaberItem());
	public static final RegistryObject<Item> DWARVEN_LEGACY = REGISTRY.register("dwarven_legacy", () -> new DwarvenLegacyItem());
	public static final RegistryObject<Item> BALLS_DELIGHTFUL_PAN = REGISTRY.register("balls_delightful_pan", () -> new BallsDelightfulPanItem(BiosWrathWeaponsModBlocks.BALLS_DELIGHTFUL_PAN.get()));
	public static final RegistryObject<Item> ENDER_KATANA = REGISTRY.register("ender_katana", () -> new EnderKatanaItem());
	public static final RegistryObject<Item> HIGH_ELF_BLADE = REGISTRY.register("high_elf_blade", () -> new HighElfBladeItem());
	public static final RegistryObject<Item> BLADEOFTHE_PIXIE = REGISTRY.register("bladeofthe_pixie", () -> new BladeofthePixieItem());
	public static final RegistryObject<Item> GODS_TOOL = REGISTRY.register("gods_tool", () -> new GodsToolItem());
	public static final RegistryObject<Item> SIRENS_TRIDENT = REGISTRY.register("sirens_trident", SirensTridentItem::new);
	public static final RegistryObject<Item> REAPERS_STRIDE = REGISTRY.register("reapers_stride", ReapersStrideItem::new);
    // Start of user code block custom items
	public static final RegistryObject<Item> ENDER_ESSENCE = REGISTRY.register("ender_essence", () -> new EnderEssenceItem());
	public static final RegistryObject<Item> INDOMITABLE_ESSENCE = REGISTRY.register("indomitable_essence", () -> new IndomitableEssenceItem());
	public static final RegistryObject<Item> DWARVEN_ESSENCE = REGISTRY.register("dwarven_essence", () -> new DwarvenEssenceItem());
	public static final RegistryObject<Item> SIREN_ESSENCE = REGISTRY.register("siren_essence", SirenEssenceItem::new);
	public static final RegistryObject<Item> DARK_ESSENCE = REGISTRY.register("dark_essence", DarkEssence::new);

	public static boolean hasEssence(LivingEntity entity, Item essence) {
		return CuriosApi.getCuriosInventory(entity).lazyMap(inventory ->
						inventory.getStacksHandler("essence").map(slot ->
										slot.getStacks().getStackInSlot(0).getItem() == essence)
								.orElse(false))
				.orElse(false);
	}
	// End of user code block custom items
}

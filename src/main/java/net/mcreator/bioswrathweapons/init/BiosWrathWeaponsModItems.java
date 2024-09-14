
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.item.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class BiosWrathWeaponsModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BiosWrathWeaponsMod.MODID);
	public static final RegistryObject<Item> UNHOOLY_HAMMER_TIME = REGISTRY.register("unhooly_hammer_time", UnhoolyHammerTimeItem::new);
	public static final RegistryObject<Item> PURIST_SWORD = REGISTRY.register("purist_sword", PuristSwordItem::new);
	public static final RegistryObject<Item> SCULK_CLEAVER = REGISTRY.register("sculk_cleaver", SculkCleaverItem::new);
	public static final RegistryObject<Item> SCULK_SABER = REGISTRY.register("sculk_saber", SculkSaberItem::new);
	public static final RegistryObject<Item> DWARVEN_LEGACY = REGISTRY.register("dwarven_legacy", DwarvenLegacyItem::new);
	public static final RegistryObject<Item> BALLS_DELIGHTFUL_PAN = REGISTRY.register("balls_delightful_pan", () -> new BallsDelightfulPanItem(BiosWrathWeaponsModBlocks.BALLS_DELIGHTFUL_PAN.get()));
	public static final RegistryObject<Item> ENDER_KATANA = REGISTRY.register("ender_katana", EnderKatanaItem::new);
	public static final RegistryObject<Item> HIGH_ELF_BLADE = REGISTRY.register("high_elf_blade", HighElfBladeItem::new);
	public static final RegistryObject<Item> BLADE_OF_THE_PIXIE = REGISTRY.register("blade_of_the_pixie", BladeOfThePixieItem::new);
	public static final RegistryObject<Item> GODS_TOOL = REGISTRY.register("gods_tool", GodsToolItem::new);
	public static final RegistryObject<Item> SIRENS_TRIDENT = REGISTRY.register("sirens_trident", SirensTridentItem::new);
	public static final RegistryObject<Item> REAPERS_STRIDE = REGISTRY.register("reapers_stride", ReapersStrideItem::new);
    // Start of user code block custom items
	public static final RegistryObject<Item> ENDER_ESSENCE = REGISTRY.register("ender_essence", EnderEssenceItem::new);
	public static final RegistryObject<Item> INDOMITABLE_ESSENCE = REGISTRY.register("indomitable_essence", IndomitableEssenceItem::new);
	public static final RegistryObject<Item> DWARVEN_ESSENCE = REGISTRY.register("dwarven_essence", DwarvenEssenceItem::new);
	public static final RegistryObject<Item> SIREN_ESSENCE = REGISTRY.register("siren_essence", SirenEssenceItem::new);
	public static final RegistryObject<Item> DARK_ESSENCE = REGISTRY.register("dark_essence", DarkEssenceItem::new);
	public static final RegistryObject<Item> PHANTOM_ESSENCE = REGISTRY.register("phantom_essence", PhantomEssenceItem::new);
	public static final RegistryObject<Item> UNHOOLY_ESSENCE = REGISTRY.register("unhooly_essence", UnhoolyEssenceItem::new);
	public static final RegistryObject<Item> ROBOTIC_ESSENCE = REGISTRY.register("robotic_essence", RoboticEssenceItem::new);

	public static boolean hasEssence(LivingEntity entity, Item essence) {
		return getEssence(entity).map(itemStack -> itemStack.getItem() == essence).orElse(false);
	}
	public static Optional<ItemStack> getEssence(LivingEntity entity) {
		return CuriosApi.getCuriosInventory(entity).map(inventory ->
						inventory.getStacksHandler("essence").map(slot ->
										slot.getStacks().getStackInSlot(0)))
				.orElse(Optional.empty());
	}
	public static int getDoubleJumps(Item item) {
		if (item == PHANTOM_ESSENCE.get()) return 5;
		if (item == ROBOTIC_ESSENCE.get()) return 1;
		return 0;
	}
	// End of user code block custom items
}

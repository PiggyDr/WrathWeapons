
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BiosWrathWeaponsModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BiosWrathWeaponsMod.MODID);
	public static final RegistryObject<CreativeModeTab> WRATH_WEAPONS = REGISTRY.register("wrath_weapons",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.bios_wrath_weapons.wrath_weapons")).icon(() -> new ItemStack(BiosWrathWeaponsModItems.UNHOOLY_HAMMER_TIME.get())).displayItems((parameters, tabData) -> {
				tabData.accept(BiosWrathWeaponsModItems.BALLS_DELIGHTFUL_PAN.get());
				tabData.accept(BiosWrathWeaponsModItems.BLADE_OF_THE_PIXIE.get());
				tabData.accept(BiosWrathWeaponsModItems.DWARVEN_LEGACY.get());
				tabData.accept(BiosWrathWeaponsModItems.HIGH_ELF_BLADE.get());
				tabData.accept(BiosWrathWeaponsModItems.PURIST_SWORD.get());
				tabData.accept(BiosWrathWeaponsModItems.SCULK_CLEAVER.get());
				tabData.accept(BiosWrathWeaponsModItems.SCULK_SABER.get());
				tabData.accept(BiosWrathWeaponsModItems.UNHOOLY_HAMMER_TIME.get());
				tabData.accept(BiosWrathWeaponsModItems.ENDER_KATANA.get());
				tabData.accept(BiosWrathWeaponsModItems.SIRENS_TRIDENT.get());
				tabData.accept(BiosWrathWeaponsModItems.REAPERS_STRIDE.get());

				tabData.accept(BiosWrathWeaponsModItems.ENDER_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.DWARVEN_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.SIREN_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.DARK_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.PHANTOM_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.UNHOOLY_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.ROBOTIC_ESSENCE.get());
			})

					.build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(BiosWrathWeaponsModItems.GODS_TOOL.get());
		}
	}
}

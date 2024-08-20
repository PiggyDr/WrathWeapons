
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.bioswrathweapons.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BiosWrathWeaponsModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BiosWrathWeaponsMod.MODID);
	public static final RegistryObject<CreativeModeTab> WRATH_WEAPONS = REGISTRY.register("wrath_weapons",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.bios_wrath_weapons.wrath_weapons")).icon(() -> new ItemStack(BiosWrathWeaponsModItems.UNHOOLY_HAMMER_TIME.get())).displayItems((parameters, tabData) -> {
				tabData.accept(BiosWrathWeaponsModItems.BALLS_DELIGHTFUL_PAN.get());
				tabData.accept(BiosWrathWeaponsModItems.BLADEOFTHE_PIXIE.get());
				tabData.accept(BiosWrathWeaponsModItems.DWARVEN_LEGACY.get());
				tabData.accept(BiosWrathWeaponsModItems.HIGH_ELF_BLADE.get());
				tabData.accept(BiosWrathWeaponsModItems.PURIST_SWORD.get());
				tabData.accept(BiosWrathWeaponsModItems.SCULK_CLEAVER.get());
				tabData.accept(BiosWrathWeaponsModItems.SCULK_SABER.get());
				tabData.accept(BiosWrathWeaponsModItems.UNHOOLY_HAMMER_TIME.get());
				tabData.accept(BiosWrathWeaponsModItems.ENDER_KATANA.get());

				tabData.accept(BiosWrathWeaponsModItems.ENDER_ESSENCE.get());
				tabData.accept(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get());
			})

					.build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(BiosWrathWeaponsModItems.GODS_TOOL.get());
		}
	}
}

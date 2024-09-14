package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiosWrathWeaponsModSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BiosWrathWeaponsMod.MODID);
    public static final RegistryObject<SoundEvent> PLACEHOLDER = REGISTRY.register("generic.bww_placeholder", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BiosWrathWeaponsMod.MODID, "generic.bww_placeholder")));
    public static final RegistryObject<SoundEvent> KATANA_PROJECTILE_ACTIVATE = PLACEHOLDER;
    public static final RegistryObject<SoundEvent> INDOMITABLE_ESSENCE_ACTIVATE = REGISTRY.register("item.indomitable_essence.activate", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BiosWrathWeaponsMod.MODID, "item.indomitable_essence.activate")));
    public static final RegistryObject<SoundEvent> PHANTOM_ESSENCE_ACTIVATE = REGISTRY.register("item.phantom_essence.activate", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BiosWrathWeaponsMod.MODID, "item.phantom_essence.activate")));
    public static final RegistryObject<SoundEvent> ROBOTIC_ESSENCE_ACTIVATE = REGISTRY.register("item.robotic_essence.activate", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BiosWrathWeaponsMod.MODID, "item.robotic_essence.activate")));
}

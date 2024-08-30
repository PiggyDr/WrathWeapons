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
}

package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.effect.ButteredEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiosWrathWeaponsModMobEffects {
    public static DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BiosWrathWeaponsMod.MODID);
    public static RegistryObject<MobEffect> BUTTERED = REGISTRY.register("buttered", () -> new ButteredEffect(MobEffectCategory.HARMFUL, 0xEBDF78));
}

package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.effect.ButteredEffect;
import net.mcreator.bioswrathweapons.effect.WaterResistanceEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiosWrathWeaponsMobEffects {
    public static DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BiosWrathWeaponsMod.MODID);
    public static RegistryObject<MobEffect> WATER_RESISTANCE = REGISTRY.register("water_resistance", () -> new WaterResistanceEffect(MobEffectCategory.BENEFICIAL, 0)); //TODO add actual color
    public static RegistryObject<MobEffect> BUTTERED = REGISTRY.register("buttered", () -> new ButteredEffect(MobEffectCategory.HARMFUL, 0xEBDF78));
}

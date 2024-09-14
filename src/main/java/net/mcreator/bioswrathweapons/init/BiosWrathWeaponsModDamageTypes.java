package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BiosWrathWeaponsModDamageTypes {
    public static final ResourceKey<DamageType> REAPERS_STRIDE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BiosWrathWeaponsMod.MODID, "reapers_stride"));
    public static final ResourceKey<DamageType> THROWN_PAN = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BiosWrathWeaponsMod.MODID, "thrown_pan"));


    public static DamageSource reapersStride(LivingEntity attacker) {
        return new DamageSource((attacker.level().registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(REAPERS_STRIDE), attacker);
    }
    public static DamageSource thrownPan(Entity immediateSource, Entity attacker) {
        return new DamageSource((attacker.level().registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(THROWN_PAN), immediateSource, attacker);
    }
}

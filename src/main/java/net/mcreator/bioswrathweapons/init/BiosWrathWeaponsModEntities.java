package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.entity.EnderKatanaProjectile;
import net.mcreator.bioswrathweapons.entity.ThrownBallsDelightfulPan;
import net.mcreator.bioswrathweapons.entity.ThrownSirensTrident;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiosWrathWeaponsModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BiosWrathWeaponsMod.MODID);

    public static final RegistryObject<EntityType<ThrownBallsDelightfulPan>> THROWN_BDPAN = REGISTRY.register("balls_delightful_pan", () -> EntityType.Builder.<ThrownBallsDelightfulPan>of(ThrownBallsDelightfulPan::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("balls_delightful_pan"));
    public static final RegistryObject<EntityType<EnderKatanaProjectile>> ENDER_KATANA_PROJECTILE = REGISTRY.register("ender_katana_projectile", () -> EntityType.Builder.<EnderKatanaProjectile>of(EnderKatanaProjectile::new, MobCategory.MISC)
            .sized(1F, 1F).clientTrackingRange(4).updateInterval(20).build("ender_katana_projectile"));

    public static final RegistryObject<EntityType<ThrownSirensTrident>> SIRENS_TRIDENT = REGISTRY.register("sirens_trident", () -> EntityType.Builder.<ThrownSirensTrident>of(ThrownSirensTrident::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("sirens_trident"));

}

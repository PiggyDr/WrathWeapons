package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.entity.ThrownBallsDelightfulPan;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiosWrathWeaponsModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BiosWrathWeaponsMod.MODID);

    public static final RegistryObject<EntityType<ThrownBallsDelightfulPan>> THROWN_BDPAN = REGISTRY.register("balls_delightful_pan", () -> EntityType.Builder.<ThrownBallsDelightfulPan>of(ThrownBallsDelightfulPan::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("balls_delightful_pan"));
}

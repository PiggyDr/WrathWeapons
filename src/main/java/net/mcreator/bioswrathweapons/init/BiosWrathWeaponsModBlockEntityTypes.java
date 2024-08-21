package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.block.entity.BallsDelightfulPanBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiosWrathWeaponsModBlockEntityTypes {
    public static DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BiosWrathWeaponsMod.MODID);
    public static RegistryObject<BlockEntityType<BallsDelightfulPanBlockEntity>> BALLS_DELIGHTFUL_PAN = REGISTRY.register("balls_delightful_pan", () -> BlockEntityType.Builder.of(
            BallsDelightfulPanBlockEntity::new,
            BiosWrathWeaponsModBlocks.BALLS_DELIGHTFUL_PAN.get()
    ).build(null));
}

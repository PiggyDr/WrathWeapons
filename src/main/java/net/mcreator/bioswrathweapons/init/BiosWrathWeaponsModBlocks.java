package net.mcreator.bioswrathweapons.init;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.block.BallsDelightfulPanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BiosWrathWeaponsModBlocks {
    public static DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BiosWrathWeaponsMod.MODID);
    public static RegistryObject<Block> BALLS_DELIGHTFUL_PAN = REGISTRY.register("balls_delightful_pan", BallsDelightfulPanBlock::new);
}

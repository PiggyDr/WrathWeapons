package net.mcreator.bioswrathweapons.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import vectorwing.farmersdelight.common.block.SkilletBlock;

public class BallsDelightfulPanBlock extends SkilletBlock {

    public BallsDelightfulPanBlock() {
        super(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)); //i could probably retrieve the properties from the actual SKILLET RegistryObject but idk how lol
    }
}

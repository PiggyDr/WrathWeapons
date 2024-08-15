package net.mcreator.bioswrathweapons.block;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import javax.annotation.Nullable;

public class BallsDelightfulPanBlock extends SkilletBlock {

    public BallsDelightfulPanBlock() {
        super(Block.Properties.copy(ModBlocks.SKILLET.get()));
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ((BlockEntityType) BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get()).create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntity, BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get(), SkilletBlockEntity::animationTick);
        } else {
            return createTickerHelper(blockEntity, BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get(), SkilletBlockEntity::cookingTick);
        }
    }
}
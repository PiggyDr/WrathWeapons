package net.mcreator.bioswrathweapons.block;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.block.entity.BallsDelightfulPanBlockEntity;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/*
    this file or significant portions of it were taken from farmers delight; see [TODO: correct file] for details

    MIT License

    Copyright (c) 2020 vectorwing

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */

public class BallsDelightfulPanBlock extends SkilletBlock {

    private static final VoxelShape TRAY = Block.box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0);
    protected static final VoxelShape SHAPE_EAST = Block.box(1F, 0F, 3F, 11F, 2F, 13F);
    protected static final VoxelShape SHAPE_WEST = Block.box(5F, 0F, 3F, 15F, 2F, 13F);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(3F, 0F, 1F, 13F, 2F, 11F);
    protected static final VoxelShape SHAPE_NORTH = Block.box(3F, 0F, 5F, 13F, 2F, 15F);
    protected static final VoxelShape SHAPE_EAST_TRAY = Shapes.or(SHAPE_EAST, TRAY);
    protected static final VoxelShape SHAPE_WEST_TRAY = Shapes.or(SHAPE_WEST, TRAY);
    protected static final VoxelShape SHAPE_SOUTH_TRAY = Shapes.or(SHAPE_SOUTH, TRAY);
    protected static final VoxelShape SHAPE_NORTH_TRAY = Shapes.or(SHAPE_NORTH, TRAY);

    public BallsDelightfulPanBlock() {
        super(Block.Properties.copy(ModBlocks.SKILLET.get()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case EAST -> { return SHAPE_EAST; }
            case WEST -> { return SHAPE_WEST; }
            case SOUTH -> { return SHAPE_SOUTH; }
            default -> {}
        }
        return SHAPE_NORTH;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(SUPPORT))
            switch (state.getValue(FACING)) {
                case EAST -> { return SHAPE_EAST_TRAY; }
                case WEST -> { return SHAPE_WEST_TRAY; }
                case SOUTH -> { return SHAPE_SOUTH_TRAY; }
                default -> { return SHAPE_NORTH_TRAY; }
            }

        return this.getShape(state, level, pos, context);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        BiosWrathWeaponsMod.LOGGER.info("newBlockEntity");
        return BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get().create(pos, state);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        BiosWrathWeaponsMod.LOGGER.info("got ticker");
        if (level.isClientSide) {
            return createTickerHelper(blockEntity, BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get(), BallsDelightfulPanBlockEntity::animationTick);
        } else {
            return createTickerHelper(blockEntity, BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get(), BallsDelightfulPanBlockEntity::cookingTick);
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof BallsDelightfulPanBlockEntity skilletEntity) {
            if (!level.isClientSide) {
                ItemStack heldStack = player.getItemInHand(hand);
                EquipmentSlot heldSlot = hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                ItemStack remainderStack;
                if (heldStack.isEmpty()) {
                    remainderStack = skilletEntity.removeItem();
                    if (!player.isCreative()) {
                        player.setItemSlot(heldSlot, remainderStack);
                    }

                    return InteractionResult.SUCCESS;
                }

                remainderStack = skilletEntity.addItemToCook(heldStack, player);
                if (remainderStack.getCount() != heldStack.getCount()) {
                    if (!player.isCreative()) {
                        player.setItemSlot(heldSlot, remainderStack);
                    }

                    level.playSound((Player)null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof BallsDelightfulPanBlockEntity) {
                Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), ((BallsDelightfulPanBlockEntity)tileEntity).getInventory().getStackInSlot(0));
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }

    }

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        BallsDelightfulPanBlockEntity skilletEntity = (BallsDelightfulPanBlockEntity) level.getBlockEntity(pos);
        CompoundTag nbt = new CompoundTag();
        if (skilletEntity != null) {
            skilletEntity.writeSkilletItem(nbt);
        }

        if (!nbt.isEmpty()) {
            stack = ItemStack.of(nbt.getCompound("Skillet"));
        }

        return stack;
    }

    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof BallsDelightfulPanBlockEntity skilletEntity) {
            if (skilletEntity.isCooking()) {
                double x = (double)pos.getX() + 0.5;
                double y = (double)pos.getY();
                double z = (double)pos.getZ() + 0.5;
                if (rand.nextInt(10) == 0) {
                    level.playLocalSound(x, y, z, (SoundEvent) ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS, 0.4F, rand.nextFloat() * 0.2F + 0.9F, false);
                }
            }
        }

    }
}
package net.mcreator.bioswrathweapons.item;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.entity.ThrownSirensTrident;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SirensTridentItem extends Item implements Vanishable {

    public SirensTridentItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BiosWrathWeaponsMod.LOGGER.debug("use: " + player.getItemInHand(hand));
        ThrownSirensTrident trident = new ThrownSirensTrident(player, player.getItemInHand(hand).copy(), level);
        trident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5F, 1.0F);
        level.addFreshEntity(trident);

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos pos, Player player) {
        return !player.getAbilities().instabuild;
    }
}

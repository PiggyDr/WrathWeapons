package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.entity.ThrownSirensTrident;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SirensTridentItem extends Item implements Vanishable {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SirensTridentItem() {
        super(new Item.Properties().stacksTo(1));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 10D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.9D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int timeLeft) {
        if (this.getUseDuration(itemStack) - timeLeft >= 10 && entity instanceof Player player) {
            if (player.isInWaterOrRain()) {
                Vec3 motion = Vec3.directionFromRotation(player.getXRot(), player.getYRot()).normalize().scale(3.5);
                BiosWrathWeaponsMod.LOGGER.info(player.onGround());
                if (player.onGround())
                    player.move(MoverType.SELF, new Vec3(0.0D, 1.2D, 0.0D)); //no idea what this is for but it's in trident code so i should probably have it here
                player.setDeltaMovement(motion.x, motion.y, motion.z);
                BiosWrathWeaponsMod.LOGGER.info(motion);
                player.startAutoSpinAttack(20);
                level.playSound(null, player, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1F, 1F);
            } else if (!level.isClientSide()) {
                ThrownSirensTrident trident = new ThrownSirensTrident(player, itemStack.copy(), level);
                trident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5F, 1.0F);
                if (player.getAbilities().instabuild)
                    trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(trident);
                
                if (!player.getAbilities().instabuild)
                    player.getInventory().removeItem(itemStack);
                level.playSound(null, trident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos pos, Player player) {
        return !player.getAbilities().instabuild;
    }
}

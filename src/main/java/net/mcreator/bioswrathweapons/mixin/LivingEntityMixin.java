package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract void hurtArmor(DamageSource p_21122_, float p_21123_);

    @Shadow public abstract void remove(Entity.RemovalReason p_276115_);

    @Shadow protected abstract int increaseAirSupply(int p_21307_);

    @Shadow public abstract void indicateDamage(double p_270514_, double p_270826_);

    @Redirect(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtArmor(Lnet/minecraft/world/damagesource/DamageSource;F)V"
            )
    )
    private void bioswrathweapons$hurtArmorWithCustomDamageReduction(LivingEntity entity, DamageSource damageSource, float amount) {
        this.hurtArmor(damageSource, damageSource.is(BiosWrathWeaponsTags.HAS_CUSTOM_DAMAGE_REDUCTION) ? amount * 0.75F : amount);
    }

    @Redirect(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(FFF)F"
            )
    )
    private float bioswrathweapons$applyCustomDamageReduction(float amount, float armor, float armorToughness, DamageSource damageSource) {
        return damageSource.is(BiosWrathWeaponsTags.HAS_CUSTOM_DAMAGE_REDUCTION) ?
                CombatRules.getDamageAfterAbsorb(amount * 0.75F, armor, armorToughness) + 0.25F :
                CombatRules.getDamageAfterAbsorb(amount, armor, armorToughness);
    }

    @Redirect(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"
            )
    )
    private float bioswrathweapons$applyButteredFrictionModifier(BlockState instance, LevelReader levelReader, BlockPos pos, Entity entity) {
        return (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(BiosWrathWeaponsModMobEffects.BUTTERED.get())) ? instance.getFriction(levelReader, pos, entity) * 1.2F : instance.getFriction(levelReader, pos, entity);
    }

}

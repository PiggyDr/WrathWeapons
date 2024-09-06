package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract void hurtArmor(DamageSource p_21122_, float p_21123_);

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

}

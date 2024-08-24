package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
            method = "hurt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifyDamage(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> ci) {
        if (((LivingEntity)(Object)this).hasEffect(BiosWrathWeaponsMobEffects.WATER_RESISTANCE.get()) && p_21016_.is(BiosWrathWeaponsTags.IS_WATER)) {
            ci.setReturnValue(false);
        }
    }
}

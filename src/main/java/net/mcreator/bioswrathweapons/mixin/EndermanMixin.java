package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnderMan.class)
public abstract class EndermanMixin {

    @ModifyVariable(
            method = "setTarget",
            at = @At("HEAD"),
            argsOnly = true
    )
    private LivingEntity modifyTarget(LivingEntity value) {
        return BiosWrathWeaponsModItems.hasEssence(value, BiosWrathWeaponsModItems.ENDER_ESSENCE.get()) ? null : value;
    }
}

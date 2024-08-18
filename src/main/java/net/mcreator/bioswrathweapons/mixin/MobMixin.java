package net.mcreator.bioswrathweapons.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mob.class)
public class MobMixin {

    @ModifyVariable(
            method = "setTarget",
            at = @At("HEAD"),
            argsOnly = true
    )
    private LivingEntity changeTarget(LivingEntity value) {
        return value; //do smth later
    }
}

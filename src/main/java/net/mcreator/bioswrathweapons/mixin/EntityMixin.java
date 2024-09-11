package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.capability.EssenceDataCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> {

    @Shadow public abstract double getY();

    private EntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(
            method = "setOnGround", //more efficient than LivingTickEvent
            at = @At("HEAD")
    )
    private void resetJumps(boolean onGround, CallbackInfo ci) {
        if (onGround) this.getCapability(EssenceDataCapability.ESSENCE_DATA).ifPresent(EssenceDataCapability::resetDoubleJumps);
    }

    @Inject(
            method = "setOnGroundWithKnownMovement",
            at = @At("HEAD")
    )
    private void resetJumpsWithKnownMovement(boolean onGround, Vec3 idk, CallbackInfo ci) {
        if (onGround) this.getCapability(EssenceDataCapability.ESSENCE_DATA).ifPresent(EssenceDataCapability::resetDoubleJumps);
    }

    @ModifyArg(
            method = "checkFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V"
            ),
            index = 4 //fall distance
    )
    private float reduceFallDistanceForDoubleJumps(float fallDistance) {
        return this.getCapability(EssenceDataCapability.ESSENCE_DATA)
                .filter(EssenceDataCapability::hasJumpedFrom)
                .map(cap -> {
                    cap.setHasJumpedFrom(false);
                    return cap.getJumpedFrom() - (float) this.getY();
                })
                .orElse(fallDistance);
    }
}

package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.capability.EssenceDataCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> {

    private EntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(
            method = "setOnGround", //more efficient than LivingTickEvent
            at = @At("HEAD")
    )
    private void resetJumps(boolean onGround, CallbackInfo ci) {
        if(onGround) this.getCapability(EssenceDataCapability.ESSENCE_DATA).ifPresent(cap -> cap.setJumpsUsed(0));
    }

    @Inject(
            method = "setOnGroundWithKnownMovement",
            at = @At("HEAD")
    )
    private void resetJumpsWithKnownMovement(boolean onGround, Vec3 idk, CallbackInfo ci) {
        if(onGround) this.getCapability(EssenceDataCapability.ESSENCE_DATA).ifPresent(cap -> cap.setJumpsUsed(0));
    }
}

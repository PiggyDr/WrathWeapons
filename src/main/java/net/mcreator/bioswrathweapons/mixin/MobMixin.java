package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.item.SirenEssenceItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    private MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyVariable(
            method = "setTarget",
            at = @At("HEAD"),
            argsOnly = true
    )
    private LivingEntity modifyTarget(LivingEntity value) {
        return value == null
                || (SirenEssenceItem.TARGET_PREVENTION_MOBS.contains(this.getType())
                && BiosWrathWeaponsModItems.hasEssence(value, BiosWrathWeaponsModItems.SIREN_ESSENCE.get())) ? null : value;
    }
}

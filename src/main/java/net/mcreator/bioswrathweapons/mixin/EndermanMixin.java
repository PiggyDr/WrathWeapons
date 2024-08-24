package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(EnderMan.class)
public abstract class EndermanMixin {

    @ModifyVariable(
            method = "setTarget",
            at = @At("HEAD"),
            argsOnly = true
    )
    private LivingEntity modifyTarget(LivingEntity value) {
        return CuriosApi.getCuriosInventory(value).lazyMap(inventory ->
                inventory.getStacksHandler("essence").map(slot ->
                        slot.getStacks().getStackInSlot(0).getItem() == BiosWrathWeaponsModItems.ENDER_ESSENCE.get())
                        .orElse(false))
                .orElse(false)
                ? null : value;
    }
}

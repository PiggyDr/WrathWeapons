package net.mcreator.bioswrathweapons.mixin;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class ItemMixin {

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"
            )
    )
    private boolean denyNonMeatToSirenEssence(Player instance, boolean bypass, Level level, Player player, InteractionHand hand) {
        return (!BiosWrathWeaponsModItems.hasEssence(instance, BiosWrathWeaponsModItems.SIREN_ESSENCE.get())
                || instance.getItemInHand(hand).getFoodProperties(instance).isMeat()
                || instance.getItemInHand(hand).is(BiosWrathWeaponsTags.ORIGINS_MEAT))
                && instance.canEat(bypass);
    }
}

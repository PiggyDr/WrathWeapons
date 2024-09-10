package net.mcreator.bioswrathweapons.item;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.capability.EssenceDataCapability;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class PhantomEssenceItem extends AbstractAbilityEssenceItem {

    public PhantomEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static boolean doubleJumpAllowed(LivingEntity entity) {
        return entity.getCapability(EssenceDataCapability.ESSENCE_DATA)
                .map(cap -> cap.hasJumpsRemaining(entity)).orElse(false);
    }

    public static void doubleJump(Player player) {
        player.jumpFromGround();
        player.noJumpDelay = 10;
        player.causeFoodExhaustion(4F);
    }

    @Override
    public void useAbility(Player player) {

    }
}

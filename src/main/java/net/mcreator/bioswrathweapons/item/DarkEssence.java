package net.mcreator.bioswrathweapons.item;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class DarkEssence extends AbstractAbilityEssenceItem {


    public DarkEssence() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void useAbility(Player player) {
        BiosWrathWeaponsMod.LOGGER.info("used dark essence ability");
        player.level().explode(player, player.getX(), player.getY(), player.getZ(), (float) player.getDeltaMovement().length(), Level.ExplosionInteraction.MOB);
    }
}

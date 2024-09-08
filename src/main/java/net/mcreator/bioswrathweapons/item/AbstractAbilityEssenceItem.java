package net.mcreator.bioswrathweapons.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public abstract class AbstractAbilityEssenceItem extends Item {

    public AbstractAbilityEssenceItem(Properties properties) {
        super(properties);
    }

    public abstract void useAbility(Player player);
}

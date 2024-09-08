package net.mcreator.bioswrathweapons.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract class AbstractAbilityEssenceItem extends Item implements ICurioItem {

    public AbstractAbilityEssenceItem(Properties properties) {
        super(properties);
    }

    public abstract void useAbility(Player player);
}

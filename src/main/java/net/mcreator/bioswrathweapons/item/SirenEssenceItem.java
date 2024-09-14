package net.mcreator.bioswrathweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class SirenEssenceItem extends Item {

    public SirenEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }
}

package net.mcreator.bioswrathweapons.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class SirenEssenceItem extends Item {

    public static final List<EntityType<?>> TARGET_PREVENTION_MOBS = List.of(EntityType.DROWNED, EntityType.GUARDIAN);

    public SirenEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }
}

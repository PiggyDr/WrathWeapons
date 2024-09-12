package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class UnhoolyEssenceItem extends Item implements ICurioItem {

    public UnhoolyEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = LinkedHashMultimap.create();
        attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":speed_bonus", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        attributes.put(Attributes.JUMP_STRENGTH, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":jump_bonus", 1, AttributeModifier.Operation.ADDITION));
        return attributes;
    }
}

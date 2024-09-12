package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class IndomitableEssenceItem extends Item implements ICurioItem {

    public IndomitableEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = LinkedHashMultimap.create();
        attributes.put(Attributes.ARMOR, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":armor_bonus", 3, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);
        BiosWrathWeaponsMod.PROXY.addCooldownToTooltip(itemStack.getItem(), components);
    }
}

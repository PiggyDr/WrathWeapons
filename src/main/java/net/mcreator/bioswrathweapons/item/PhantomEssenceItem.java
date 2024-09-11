package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.capability.EssenceDataCapability;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.checkerframework.checker.units.qual.A;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

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
        player.getCapability(EssenceDataCapability.ESSENCE_DATA).ifPresent(cap -> {
            cap.incrementJumpsUsed();
            cap.updateJumpedFrom(player);
            cap.setHasJumpedFrom(true);
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = LinkedHashMultimap.create();
        attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":speed_bonus", 0.3, AttributeModifier.Operation.MULTIPLY_BASE));
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":health_bonus", 2, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    @Override
    public void useAbility(Player player) {
        //TODO
    }
}

package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class DwarvenEssenceItem extends AbstractAbilityEssenceItem {

    public DwarvenEssenceItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = LinkedHashMultimap.create();
        attributes.put(Attributes.ARMOR, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":armor_bonus", 5, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player && !inSunlight(player) && player.isCrouching()) {
            slotContext.entity().addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400));
        }
    }

    private boolean inSunlight(Player entity) {
        return (entity.level().canSeeSky(BlockPos.containing(entity.position()))
                || inExposedPowderSnow(entity.level(), BlockPos.containing(entity.position())))
                && entity.level().isDay()
                && !entity.isInWaterRainOrBubble();
    }

    private boolean inExposedPowderSnow(Level level, BlockPos pos) {
        while (level.getBlockState(pos).getBlock() == Blocks.POWDER_SNOW) {
            pos = pos.offset(0, 1, 0);
        }
        return level.canSeeSky(pos);
    }

    @Override
    public void useAbility(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 4));
        player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.DWARVEN_ESSENCE.get(), 600);
    }
}

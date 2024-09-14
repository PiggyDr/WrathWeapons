package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.capability.EssenceDataCapability;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class PhantomEssenceItem extends AbstractAbilityEssenceItem {

    public PhantomEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
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
        player.level().getEntitiesOfClass(Monster.class, new AABB(player.position(), player.position()).inflate(15D))
                .forEach(entity -> entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1), player));
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BiosWrathWeaponsModSounds.PHANTOM_ESSENCE_ACTIVATE.get(), SoundSource.PLAYERS, 10F, 1F);
        player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.PHANTOM_ESSENCE.get(), 2400);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);
        BiosWrathWeaponsMod.PROXY.addCooldownToTooltip(itemStack.getItem(), components);
    }
}

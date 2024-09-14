package net.mcreator.bioswrathweapons.item;

import com.github.sculkhorde.common.entity.SculkMiteAggressorEntity;
import com.github.sculkhorde.common.entity.SculkMiteEntity;
import com.github.sculkhorde.common.entity.infection.CursorSurfacePurifierEntity;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RoboticEssenceItem extends AbstractAbilityEssenceItem implements ICurioItem {

    public RoboticEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        LinkedHashMultimap<Attribute, AttributeModifier> attributes = LinkedHashMultimap.create();
        attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":speed_bonus", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        attributes.put(Attributes.ARMOR, new AttributeModifier(uuid, BiosWrathWeaponsMod.MODID + ":armor_bonus", 5, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    @Override
    public void useAbility(Player player) {
        if (!player.level().isClientSide()) {
            BiosWrathWeaponsMod.LOGGER.info("serverside cursor");
            int cursorCount = player.getRandom().nextInt(4);
            for (int i = 0; i < cursorCount; i++) {
                CursorSurfacePurifierEntity cursor = new CursorSurfacePurifierEntity(player.level());
                cursor.setPos(BlockPos.containing(player.position().add(0, -1, 0)).getCenter());
                cursor.setMaxTransformations(200); //same stats as purification flask
                cursor.setMaxRange(100);
                cursor.setSearchIterationsPerTick(5);
                cursor.setMaxLifeTimeMillis(TimeUnit.MINUTES.toMillis(1));
                cursor.setTickIntervalMilliseconds(150);
                player.level().addFreshEntity(cursor);
            }
        }

        player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.ROBOTIC_ESSENCE.get(), 1800);

        AABB aabb = new AABB(player.position(), player.position()).inflate(9);
        player.level().getEntitiesOfClass(SculkMiteEntity.class, aabb).forEach(mite -> mite.hurt(player.damageSources().sonicBoom(player), mite.getHealth()));
        player.level().getEntitiesOfClass(SculkMiteAggressorEntity.class, aabb).forEach(mite -> mite.hurt(player.damageSources().sonicBoom(player), mite.getHealth()));

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BiosWrathWeaponsModSounds.ROBOTIC_ESSENCE_ACTIVATE.get(), SoundSource.PLAYERS, 3F, 1F);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);
        BiosWrathWeaponsMod.PROXY.addCooldownToTooltip(itemStack.getItem(), components);
    }
}

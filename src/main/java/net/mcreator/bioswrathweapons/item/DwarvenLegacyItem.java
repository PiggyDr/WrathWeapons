
package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class DwarvenLegacyItem extends PickaxeItem {

	private static final UUID KNOCKBACK_UUID = UUID.fromString("9971fbfe-ba1b-40ba-a715-7d33dc398d33");

	public DwarvenLegacyItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 9f;
			}

			public float getAttackDamageBonus() {
				return 6f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 22;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 1, -3.1f, new Item.Properties().fireResistant());
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.literal("\u00A7dThe next light of the Dwarven Race."));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
			HashMultimap<Attribute, AttributeModifier> attributes = HashMultimap.create(super.getAttributeModifiers(slot, stack));
			attributes.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(KNOCKBACK_UUID, BiosWrathWeaponsMod.MODID + ":knockback_bonus", 2, AttributeModifier.Operation.ADDITION));
			return ImmutableMultimap.copyOf(attributes);
		} else {
			return super.getAttributeModifiers(slot, stack);
		}
	}

	@Override
	public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
		if (super.mineBlock(itemStack, level, state, pos, entity)) {
			if (entity instanceof ServerPlayer player) {

			} else {

			}
			return true;
		}
		return false;
	}
}

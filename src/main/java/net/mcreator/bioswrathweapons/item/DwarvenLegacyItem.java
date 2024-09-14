
package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
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
			if (!level.isClientSide()) {
				Vec3 offset = pos.getCenter().subtract(entity.getEyePosition());
				Vec3i[] axes = EnumSet.complementOf(
						EnumSet.of(Direction.getNearest(offset.x, offset.y, offset.z).getAxis()))
						.stream()
						.map(axis -> Direction.get(Direction.AxisDirection.POSITIVE, axis).getNormal())
						.toArray(Vec3i[]::new);
				for (int i=-1; i<2; i++) {
					for (int j =-1; j<2; j++) {
						if (i == 0 && j == 0)
							continue;
						BlockPos toDestroy = pos.offset(axes[0].multiply(i).offset(axes[1].multiply(j)));
						if (this.isCorrectToolForDrops(itemStack, level.getBlockState(toDestroy)))
							level.destroyBlock(toDestroy, true, entity);
					}
				}
			}
			return true;
		}
		return false;
	}
}

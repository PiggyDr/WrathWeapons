
package net.mcreator.bioswrathweapons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class HighElfBladeItem extends SwordItem {
	public HighElfBladeItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 9f;
			}

			public float getAttackDamageBonus() {
				return 1f;
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
		}, 3, -2.5f, new Item.Properties().fireResistant());
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.translatable("item.bios_wrath_weapons.high_elf_blade.tooltip").withStyle(ChatFormatting.AQUA));
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity attacker) {
		super.hurtEnemy(stack, entity, attacker);
		entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0), attacker);
		entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0), attacker);
		entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0), attacker);
		return true;
	}
}

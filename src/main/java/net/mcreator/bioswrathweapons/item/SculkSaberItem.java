
package net.mcreator.bioswrathweapons.item;

import com.github.sculkhorde.core.ModMobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class SculkSaberItem extends SwordItem {
	public SculkSaberItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 9f;
			}

			public float getAttackDamageBonus() {
				return 3f;
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
		list.add(Component.literal("\u00A76\u00A7l[Cleansing Protocol Confirmed]"));
	}

	@Override
	public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int noIdea, boolean holding) {
		super.inventoryTick(itemStack, level, entity, noIdea, holding);
		if (holding && entity instanceof LivingEntity lentity) {
			lentity.addEffect(new MobEffectInstance(ModMobEffects.PURITY.get(), 60));
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity attacker) {
		entity.addEffect(new MobEffectInstance(ModMobEffects.PURITY.get(), 600));
		return super.hurtEnemy(itemStack, entity, attacker);
	}

}


package net.mcreator.bioswrathweapons.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GodsToolItem extends SwordItem {
	public GodsToolItem() {
		super(new Tier() {
			public int getUses() {
				return 1;
			}

			public float getSpeed() {
				return 9f;
			}

			public float getAttackDamageBonus() {
				return 996f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 9999;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 3, 1f, new Item.Properties().fireResistant());
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.literal("\u00A7kPlay with their minds, friend."));
	}
}

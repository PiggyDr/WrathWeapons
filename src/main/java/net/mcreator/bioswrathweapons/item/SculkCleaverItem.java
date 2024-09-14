
package net.mcreator.bioswrathweapons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import java.util.List;
import java.util.Optional;

public class SculkCleaverItem extends SwordItem {
	public SculkCleaverItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 12f;
			}

			public float getAttackDamageBonus() {
				return 13f;
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
		}, 3, -3.35f, new Item.Properties().fireResistant());
	}

	public static void sweep(Player player) { //stolen from tetra (with the variables subbed out where necessary ofc) (also this is from the 1.18 branch before it went private so i had to port it)
		ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);

		Vec3 target = Vec3.directionFromRotation(player.getXRot(), player.getYRot())
				.normalize()
				.scale(2.0)
				.add(player.getEyePosition(0));
		AABB aoe = new AABB(target, target);

		// range values set up to mimic vanilla behaviour
		player.level().getEntitiesOfClass(LivingEntity.class, aoe.inflate(2.0, 1d, 2.0)).stream()
				.filter(entity -> entity != player)
				.filter(entity -> !player.isAlliedTo(entity))
				.forEach(entity -> {
					Vec3 kbDirection = Vec3.directionFromRotation(player.getXRot(), player.getYRot())
							.scale(player.getAttributeValue(Attributes.ATTACK_KNOCKBACK))
							.scale(Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.ATTACK_KNOCKBACK)));
					entity.knockback(kbDirection.x, kbDirection.y, kbDirection.z);

					DamageSource damageSource = player.damageSources().playerAttack(player);
					float targetModifier = EnchantmentHelper.getDamageBonus(itemStack, entity.getMobType());
					float critMultiplier = Optional.ofNullable(ForgeHooks.getCriticalHit(player, entity, false, 1.5F))
							.map(CriticalHitEvent::getDamageModifier)
							.orElse(1F);

					entity.hurt(damageSource, (float) ((player.getAttributeValue(Attributes.ATTACK_DAMAGE) + targetModifier) * critMultiplier * 0.75));

					EnchantmentHelper.doPostHurtEffects(entity, player);
					EnchantmentHelper.doPostDamageEffects(entity, player);

					entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 1));
				});
		player.sweepAttack();
	}

	@Override
	public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
		target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 1));
		return super.hurtEnemy(itemStack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.translatable("item.bios_wrath_weapons.sculk_cleaver.tooltip").withStyle(ChatFormatting.DARK_AQUA));
	}
}

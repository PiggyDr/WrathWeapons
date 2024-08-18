
package net.mcreator.bioswrathweapons.item;

//import com.github.sculkhorde.core.ModMobEffects;

import net.mcreator.bioswrathweapons.procedures.SculkSaberLivingEntityIsHitWithToolProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		SculkSaberLivingEntityIsHitWithToolProcedure.execute(entity);
		return retval;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.literal("\u00A76\u00A7l[Cleansing Protocol Confirmed]"));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		player.getCooldowns().addCooldown(itemStack.getItem(), 400);
		if (!level.isClientSide()) {
			AreaEffectCloud aec = new AreaEffectCloud(level, player.getX(), player.getY(), player.getZ());
			aec.setRadius(5);
			aec.setOwner(player);
			//aec.setPotion(new Potion(new MobEffectInstance(ModMobEffects.PURITY.get(), 1200))); //aec effect application is weird
			aec.setParticle(ParticleTypes.TOTEM_OF_UNDYING);
			((ServerLevel)level).addFreshEntity(aec);
		}
		return InteractionResultHolder.sidedSuccess(itemStack, !level.isClientSide());
	}
}

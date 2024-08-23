package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.List;

@ParametersAreNonnullByDefault
public class BallsDelightfulPanItem extends SkilletItem {
	private final Tier tier;
	private final float attackDamage;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public BallsDelightfulPanItem(Block block) {
		super(block, new Item.Properties().stacksTo(1).fireResistant().defaultDurability(0));
		this.attackDamage = 6.0F;
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.5F, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
		this.tier = new Tier() {
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

			public @NotNull Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		};
	}

	@Override
	public void appendHoverText(ItemStack itemstack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.literal("§6The Grand Chef has arrived."));
	}

	public float getDamage() {
		return this.attackDamage;
	}

	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {
		p_43278_.hurtAndBreak(1, p_43280_, (p_43296_) -> {
			p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
		if (p_43284_.getDestroySpeed(p_43283_, p_43285_) != 0.0F) {
			p_43282_.hurtAndBreak(2, p_43286_, (p_43276_) -> {
				p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
		return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
	}

	public Tier getTier() {
		return this.tier;
	}

	public int getEnchantmentValue() {
		return this.tier.getEnchantmentValue();
	}

	public boolean isValidRepairItem(ItemStack p_43311_, ItemStack p_43312_) {
		return this.tier.getRepairIngredient().test(p_43312_) || super.isValidRepairItem(p_43311_, p_43312_);
	}

	private static boolean isPlayerNearHeatSource(Player player, LevelReader level) { //TODO use reflection instead? idk
		if (player.isOnFire()) {
			return true;
		} else {
			BlockPos pos = player.blockPosition();
			Iterator var3 = BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1)).iterator();

			BlockPos nearbyPos;
			do {
				if (!var3.hasNext()) {
					return false;
				}

				nearbyPos = (BlockPos)var3.next();
			} while(!level.getBlockState(nearbyPos).is(ModTags.HEAT_SOURCES));

			return true;
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return stack.getOrCreateTag().contains("Cooking") ? super.getUseAnimation(stack) : UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return stack.getOrCreateTag().contains("Cooking") ? super.getUseDuration(stack) : 72000;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (stack.getOrCreateTag().contains("Cooking")) {
			super.releaseUsing(stack, level, entity, timeLeft);
		} else if (this.getUseDuration(stack) - timeLeft >= 10 && entity instanceof Player player) {
			ThrownTrident trident = new ThrownTrident(level, player, stack);
			trident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1, 0);
			level.addFreshEntity(trident);
			player.getInventory().removeItem(stack);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (isPlayerNearHeatSource(player, level)) {
			return super.use(level, player, hand);
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(player.getItemInHand(hand));
		}
	}
}
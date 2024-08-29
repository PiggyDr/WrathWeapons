package net.mcreator.bioswrathweapons.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.entity.ThrownBallsDelightfulPan;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class BallsDelightfulPanItem extends SkilletItem {
	private final Tier tier;
	private final float attackDamage;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;
	private static final IClientItemExtensions EXTENSION = new IClientItemExtensions() {
		@Override
		public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
			if (player.getUseItem() == itemInHand && player.isUsingItem() && !itemInHand.getOrCreateTag().contains("Cooking")) {
				int i = arm == HumanoidArm.RIGHT ? 1 : -1;
				float useProgress = Mth.lerp(
						partialTick,
						Math.max(0, player.getUseItemRemainingTicks() - itemInHand.getUseDuration() + 15),
						Math.max(0, player.getUseItemRemainingTicks() - itemInHand.getUseDuration() + 14)
				)/15;
				useProgress = 1 - (useProgress * useProgress);
				poseStack.translate(
						0.25*i,
						-1.18 + equipProcess * -0.3 + (useProgress > 0 ? Math.sin(player.level().getGameTime()) * 0.005 : 0),
						-1.2
				);
				if (useProgress > 0)
					poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(useProgress, -30F*i, 45F*i)));
				else
					poseStack.mulPose(Axis.YP.rotationDegrees(-30F*i));
				poseStack.mulPose(Axis.ZP.rotationDegrees(90F*i));
			}
			return false;
		}

		@Override
		public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
			if (
					!itemStack.isEmpty()
							&& entityLiving.getUsedItemHand() == hand
							&& entityLiving.getUseItemRemainingTicks() > 0
							&& !itemStack.getOrCreateTag().contains("Cooking")
			) {
				return HumanoidModel.ArmPose.BLOCK;
			}
			return HumanoidModel.ArmPose.EMPTY;
		}
	};

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

	public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
		itemStack.hurtAndBreak(1, target, (p_43296_) -> {
			p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		target.addEffect(new MobEffectInstance(BiosWrathWeaponsModMobEffects.BUTTERED.get(), 600));
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
	public int getUseDuration(ItemStack stack) {
		return stack.getOrCreateTag().contains("Cooking") ? super.getUseDuration(stack) : 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return stack.getOrCreateTag().contains("Cooking") ? super.getUseAnimation(stack) : UseAnim.CUSTOM;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(EXTENSION);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (stack.getOrCreateTag().contains("Cooking")) {
			super.releaseUsing(stack, level, entity, timeLeft);
		} else if (this.getUseDuration(stack) - timeLeft >= 5 && entity instanceof Player player && !level.isClientSide()) {
            ThrownBallsDelightfulPan pan = new ThrownBallsDelightfulPan(level, player, stack.copy());
			pan.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0.1F);
			level.addFreshEntity(pan);
			if (!player.getAbilities().instabuild)
				player.getInventory().removeItem(stack);
			if (!player.level().isClientSide())
				player.level().playSound(null, player.getX(), player.getEyeY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 1.0F, 0.4F);
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

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
		if (stack.getOrCreateTag().contains("Cooking"))
			super.onUseTick(level, entity, stack, count);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class BDPEvents {
		public BDPEvents () {
		}

		@SubscribeEvent
		public static void playSkilletAttackSound(LivingDamageEvent event) {
			DamageSource damageSource = event.getSource();
			Entity attacker = damageSource.getDirectEntity();
			if (attacker instanceof LivingEntity livingEntity) {
				if (livingEntity.getItemInHand(InteractionHand.MAIN_HAND).is((Item) BiosWrathWeaponsModItems.BALLS_DELIGHTFUL_PAN.get())) {
					float pitch = 0.9F + livingEntity.getRandom().nextFloat() * 0.2F;
					if (livingEntity instanceof Player player) {
						float attackPower = player.getAttackStrengthScale(0.0F);
						if (attackPower > 0.8F) {
							player.getCommandSenderWorld().playSound((Player)null, player.getX(), player.getY(), player.getZ(), (SoundEvent) ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 1.0F, pitch);
						} else {
							player.getCommandSenderWorld().playSound((Player)null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.ITEM_SKILLET_ATTACK_WEAK.get(), SoundSource.PLAYERS, 0.8F, 0.9F);
						}
					} else {
						livingEntity.getCommandSenderWorld().playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 1.0F, pitch);
					}
				}
			}
		}
	}
}
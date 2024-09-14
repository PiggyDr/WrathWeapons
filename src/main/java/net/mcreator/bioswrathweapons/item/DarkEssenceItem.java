package net.mcreator.bioswrathweapons.item;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class DarkEssenceItem extends AbstractAbilityEssenceItem {


    public DarkEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public void useAbility(Player player) {
        Vec3 origin = player.position().add(0F, 1F, 0F);
        shootSonicBoom(origin, Vec3.directionFromRotation(player.getXRot() + randomOffset(), player.getYRot() + randomOffset()), player);
        shootSonicBoom(origin, Vec3.directionFromRotation(player.getXRot() + randomOffset(), player.getYRot() + randomOffset() - 45), player);
        shootSonicBoom(origin, Vec3.directionFromRotation(player.getXRot() + randomOffset(), player.getYRot() + randomOffset() + 45), player);

        player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.DARK_ESSENCE.get(), 3600); //3 mins
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100));

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 16F, 1.3F);
    }

    private void shootSonicBoom(Vec3 origin, Vec3 direction, Player player) {
        for (int i = 1; i < 12; i++) {
            Vec3 segmentPos = origin.add(direction.scale(i));
            ((ServerLevel)player.level()).sendParticles(ParticleTypes.SONIC_BOOM, segmentPos.x, segmentPos.y, segmentPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            player.level().getEntitiesOfClass(LivingEntity.class, new AABB(segmentPos, segmentPos).inflate(0.5F), entity -> entity != player).forEach(entity -> {
                entity.hurt(player.damageSources().sonicBoom(player), 10);
            });
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        LivingEntity entity = slotContext.entity();
        Level level = entity.level();
        if (level.getGameTime() % Math.floor(Mth.lerp(entity.getHealth() / entity.getMaxHealth(), 15F, 40F)) == 0) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 8F, 1F);
        }
    }

    private static float randomOffset() {
        return (float) Mth.lerp(Math.random(), -7.5, 7.5);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);
        BiosWrathWeaponsMod.PROXY.addCooldownToTooltip(itemStack.getItem(), components);
    }
}

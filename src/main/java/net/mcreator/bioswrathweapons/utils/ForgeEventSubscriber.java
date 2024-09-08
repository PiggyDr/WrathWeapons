package net.mcreator.bioswrathweapons.utils;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModSounds;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.mcreator.bioswrathweapons.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = BiosWrathWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (CuriosApi.getCuriosInventory(event.getEntity()).lazyMap(inventory ->
                        inventory.getStacksHandler("essence").map(slot ->
                                        slot.getStacks().getStackInSlot(0).getItem() == BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get())
                                .orElse(false)).orElse(false) && event.getEntity() instanceof Player player
                && !player.getCooldowns().isOnCooldown(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get())
                && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {

            player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get(), 6000);
            player.setHealth(1);
            player.removeAllEffects();
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            event.setCanceled(true);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BiosWrathWeaponsModSounds.INDOMITABLE_ESSENCE_ACTIVATE.get(), player.getSoundSource(), 1.0F, 1.0F);
            BiosWrathWeaponsMod.sendToPlayer(new ClientboundIndomitableEssencePacket(), (ServerPlayer) player);
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (event.getEntity().hasEffect(BiosWrathWeaponsModMobEffects.BUTTERED.get())) {
            event.setAmount(event.getAmount()*1.25F);
        }
    }
}

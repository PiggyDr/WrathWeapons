package net.mcreator.bioswrathweapons.utils;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.mcreator.bioswrathweapons.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (CuriosApi.getCuriosInventory(event.getEntity()).lazyMap(inventory ->
                        inventory.getStacksHandler("essence").map(slot ->
                                        slot.getStacks().getStackInSlot(0).getItem() == BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get())
                                .orElse(false)).orElse(false) && event.getEntity() instanceof Player player
                && !player.getCooldowns().isOnCooldown(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get())) {

            player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get(), 6000);
            player.setHealth(1);
            player.removeAllEffects();
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            event.setCanceled(true);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, player.getSoundSource(), 1.0F, 1.0F);
            PacketHandler.sendToPlayer(new ClientboundIndomitableEssencePacket(), (ServerPlayer) player);
        }
    }

}

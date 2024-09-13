package net.mcreator.bioswrathweapons.utils;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.network.ClientboundCustomCooldownPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

public class CooldownUtils {

    public static void addCustomCooldown(Player player, Item item, int totalCooldown, int currentDuration) {
        ItemCooldowns itemCooldowns = player.getCooldowns();
        itemCooldowns.cooldowns.put(item, new ItemCooldowns.CooldownInstance(itemCooldowns.tickCount - totalCooldown + currentDuration, itemCooldowns.tickCount + currentDuration));
        if (player instanceof ServerPlayer) BiosWrathWeaponsMod.sendToPlayer(new ClientboundCustomCooldownPacket(item, totalCooldown, currentDuration), (ServerPlayer) player);
    }

    public static void addCustomCooldown(Player player, Item item, int[] array) {
        addCustomCooldown(player, item, array[1], array[0]);
    }
}

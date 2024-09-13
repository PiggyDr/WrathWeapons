package net.mcreator.bioswrathweapons.proxy;

import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

public class CommonProxy {

    public Map<Player, Map<Item, Integer>> cooldownsToApply = new HashMap<>();

    public void playTickableSound(LivingEntity source) {}

    public void stopPlayingTickableSound(LivingEntity source) {}

    public void displayIndomitableEssencePacket(ClientboundIndomitableEssencePacket msg) {}

    public void addCooldownToTooltip(Item item, List<Component> components) {}

    public void applyCooldowns(Player player) {
        for (Map.Entry<Item, Integer> cooldownEntry : this.cooldownsToApply.get(player).entrySet()) {
            player.getCooldowns().addCooldown(cooldownEntry.getKey(), cooldownEntry.getValue());
        }
    }

}

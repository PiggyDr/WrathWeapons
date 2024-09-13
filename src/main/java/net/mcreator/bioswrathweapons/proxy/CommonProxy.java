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

    public void applyCooldowns() {
        for (Map.Entry<Player, Map<Item, Integer>> playerEntry : this.cooldownsToApply.entrySet()) {
            for (Map.Entry<Item, Integer> cooldownEntry : playerEntry.getValue().entrySet()) {
                playerEntry.getKey().getCooldowns().addCooldown(cooldownEntry.getKey(), cooldownEntry.getValue());
            }
        }
    }

}

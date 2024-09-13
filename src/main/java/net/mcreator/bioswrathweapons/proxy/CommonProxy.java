package net.mcreator.bioswrathweapons.proxy;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.network.ClientboundCustomCooldownPacket;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.mcreator.bioswrathweapons.utils.CooldownUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonProxy {

    public Map<Player, Map<Item, int[]>> cooldownsToApply = new HashMap<>();

    public void playTickableSound(LivingEntity source) {}

    public void stopPlayingTickableSound(LivingEntity source) {}

    public void displayIndomitableEssencePacket(ClientboundIndomitableEssencePacket msg) {}

    public void addCooldownToTooltip(Item item, List<Component> components) {}

    public void applyCooldowns(Player player) {
        for (Map.Entry<Item, int[]> entry : this.cooldownsToApply.get(player).entrySet()) {

            ItemCooldowns itemCooldowns = player.getCooldowns(); //don't use addCooldown so percentage is preserved
            CooldownUtils.addCustomCooldown(player, entry.getKey(), entry.getValue());
            this.cooldownsToApply.remove(player);
        }
    }

    public void addCustomCooldown(Item item, int totalCooldown, int currentDuration) {}

}

package net.mcreator.bioswrathweapons.proxy;

import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.List;

public class CommonProxy {

    public void playTickableSound(LivingEntity source) {}

    public void stopPlayingTickableSound(LivingEntity source) {}

    public void displayIndomitableEssencePacket(ClientboundIndomitableEssencePacket msg) {}

    public void addCooldownToTooltip(Item item, List<Component> components) {}

}

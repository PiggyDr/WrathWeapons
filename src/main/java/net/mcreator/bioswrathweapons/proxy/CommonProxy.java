package net.mcreator.bioswrathweapons.proxy;

import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.minecraft.world.entity.LivingEntity;

public class CommonProxy {

    public void playTickableSound(LivingEntity source) {}

    public void stopPlayingTickableSound(LivingEntity source) {}

    public void displayIndomitableEssencePacket(ClientboundIndomitableEssencePacket msg) {}

}

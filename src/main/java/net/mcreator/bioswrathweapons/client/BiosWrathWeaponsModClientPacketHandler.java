package net.mcreator.bioswrathweapons.client;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
//import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class BiosWrathWeaponsModClientPacketHandler {

    @OnlyIn(Dist.CLIENT)
    public static void handle(Packet<?> packet, Supplier<NetworkEvent.Context> ctx) {
        if (packet instanceof ClientboundIndomitableEssencePacket) {
            //Minecraft.getInstance().gameRenderer.displayItemActivation(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get().getDefaultInstance());
        }
    }
}

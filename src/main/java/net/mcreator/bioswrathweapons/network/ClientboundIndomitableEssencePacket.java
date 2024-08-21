package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundIndomitableEssencePacket implements Packet<ClientPacketListener> {

    public ClientboundIndomitableEssencePacket() {
    }

    public ClientboundIndomitableEssencePacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public void write(FriendlyByteBuf p_131343_) {}

    @Override
    public void handle(ClientPacketListener p_131342_) {

    }

    public static void handle(ClientboundIndomitableEssencePacket packer, Supplier<NetworkEvent.Context> idk) {
        idk.get().enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get().getDefaultInstance());
        });
        idk.get().setPacketHandled(true);
    }
}

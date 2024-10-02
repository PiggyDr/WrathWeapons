package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ClientboundCustomCooldownPacket implements Packet<PacketListener> {
    Item item;
    int totalCooldown;
    int currentDuration;

    public ClientboundCustomCooldownPacket(Item item, int totalCooldown, int currentDuration) {
        this.item = item;
        this.totalCooldown = totalCooldown;
        this.currentDuration = currentDuration;
    }

    public ClientboundCustomCooldownPacket(FriendlyByteBuf buf) {
        this.item = buf.readRegistryIdSafe(Item.class);
        this.totalCooldown = buf.readInt();
        this.currentDuration = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeRegistryId(ForgeRegistries.ITEMS, this.item);
        buf.writeInt(this.totalCooldown);
        buf.writeInt(this.currentDuration);
    }
    @Override
    public void handle(PacketListener listener) {}

    public static void handle(ClientboundCustomCooldownPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BiosWrathWeaponsMod.PROXY.addCustomCooldown(packet.item, packet.totalCooldown, packet.currentDuration);
        });
        ctx.get().setPacketHandled(true);
    }
}

package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.item.PhantomEssenceItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundDoubleJumpPacket implements Packet<PacketListener> {
    public ServerboundDoubleJumpPacket() {}
    public ServerboundDoubleJumpPacket(FriendlyByteBuf buf) {}
    @Override
    public void write(FriendlyByteBuf buf) {}
    @Override
    public void handle(PacketListener listener) {}

    public static void handle(ServerboundDoubleJumpPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PhantomEssenceItem.doubleJump(ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}

package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.entity.EnderKatanaProjectile;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.item.EnderKatanaItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundKatanaAttackPacket implements Packet {

    public ServerboundKatanaAttackPacket() {}
    public ServerboundKatanaAttackPacket(FriendlyByteBuf buf) {}
    @Override public void write(FriendlyByteBuf buf) {}
    @Override public void handle(PacketListener listener) {}

    public static void handle(ServerboundKatanaAttackPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == BiosWrathWeaponsModItems.ENDER_KATANA.get() && !player.getCooldowns().isOnCooldown(BiosWrathWeaponsModItems.ENDER_KATANA.get()) && player.getAttackStrengthScale(0) == 1.0F) {
                EnderKatanaItem.shootProjectile(player);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2.0F, 1.0F);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

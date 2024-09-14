package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.item.AbstractAbilityEssenceItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

public class ServerboundEssenceAbilityPacket implements Packet<PacketListener> {

    public ServerboundEssenceAbilityPacket() {}
    public ServerboundEssenceAbilityPacket(FriendlyByteBuf buf) {}
    @Override
    public void write(FriendlyByteBuf buf) {}
    @Override
    public void handle(PacketListener buf) {}

    public static void handle(ServerboundEssenceAbilityPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BiosWrathWeaponsModItems.getEssence(player)
                                    .ifPresent(essence -> {
                                        Item essenceType = essence.getItem();
                                        if (!player.getCooldowns().isOnCooldown(essenceType) && essenceType instanceof AbstractAbilityEssenceItem abilityEssence)
                                            abilityEssence.useAbility(player);
                                    });
        });
        ctx.get().setPacketHandled(true);
    }
}

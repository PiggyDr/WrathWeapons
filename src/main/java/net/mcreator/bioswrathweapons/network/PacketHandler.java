package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BiosWrathWeaponsMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        byte packetId = 0;
        INSTANCE.registerMessage(packetId++,
                ClientboundIndomitableEssencePacket.class,
                ClientboundIndomitableEssencePacket::write,
                ClientboundIndomitableEssencePacket::new,
                ClientboundIndomitableEssencePacket::handle);
        INSTANCE.registerMessage(packetId++,
                ServerboundKatanaAttackPacket.class,
                ServerboundKatanaAttackPacket::write,
                ServerboundKatanaAttackPacket::new,
                ServerboundKatanaAttackPacket::handle);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }
}

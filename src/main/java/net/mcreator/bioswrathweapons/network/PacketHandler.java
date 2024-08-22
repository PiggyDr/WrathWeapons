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
//        INSTANCE.messageBuilder(ClientboundIndomitableEssencePacket.class, 58)
//                .encoder(ClientboundIndomitableEssencePacket::write)
//                .decoder(ClientboundIndomitableEssencePacket::new)
//                .noResponse()
//                .consumerMainThread((packet, ctx) -> {
//                    ClientUtils.displayActivation();
//                    BiosWrathWeaponsMod.LOGGER.info("client recieved ie packrt");
//                    Minecraft.getInstance().gameRenderer.displayItemActivation(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get().getDefaultInstance());
//                    ctx.get().setPacketHandled(true);
//                })
//                .add();
        INSTANCE.registerMessage(1,
                ClientboundIndomitableEssencePacket.class,
                ClientboundIndomitableEssencePacket::write,
                ClientboundIndomitableEssencePacket::new,
                ClientboundIndomitableEssencePacket::handle);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}

package net.mcreator.bioswrathweapons.network;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundIndomitableEssencePacket implements Packet<ClientPacketListener> {

    public ClientboundIndomitableEssencePacket() {
    }

    public ClientboundIndomitableEssencePacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public void write(FriendlyByteBuf p_131343_) {

    }

    @Override
    public void handle(ClientPacketListener p_131342_) {

    }

    public static void handle(ClientboundIndomitableEssencePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BiosWrathWeaponsMod.PROXY.displayIndomitableEssencePacket(msg));
        ctx.get().setPacketHandled(true);
    }

//    private static Runnable displayActivation() {
//        if (FMLEnvironment.dist == Dist.CLIENT)
//            return getClientSupplier();
//        else
//            return () -> {};
//    }

//    @OnlyIn(Dist.CLIENT)
//    private static Runnable getClientSupplier() {
//        return () -> ;
//    }


}

package net.mcreator.bioswrathweapons.utils;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.network.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BiosWrathWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BiosWrathWeaponsMod.addNetworkMessage(
                    ClientboundIndomitableEssencePacket.class,
                    ClientboundIndomitableEssencePacket::write,
                    ClientboundIndomitableEssencePacket::new,
                    ClientboundIndomitableEssencePacket::handle
            );
            BiosWrathWeaponsMod.addNetworkMessage(
                    ServerboundEmptyAttackPacket.class,
                    ServerboundEmptyAttackPacket::write,
                    ServerboundEmptyAttackPacket::new,
                    ServerboundEmptyAttackPacket::handle
            );
            BiosWrathWeaponsMod.addNetworkMessage(
                    ServerboundEssenceAbilityPacket.class,
                    ServerboundEssenceAbilityPacket::write,
                    ServerboundEssenceAbilityPacket::new,
                    ServerboundEssenceAbilityPacket::handle
            );
            BiosWrathWeaponsMod.addNetworkMessage(
                    ServerboundDoubleJumpPacket.class,
                    ServerboundDoubleJumpPacket::write,
                    ServerboundDoubleJumpPacket::new,
                    ServerboundDoubleJumpPacket::handle
            );
            BiosWrathWeaponsMod.addNetworkMessage(
                    ClientboundCustomCooldownPacket.class,
                    ClientboundCustomCooldownPacket::write,
                    ClientboundCustomCooldownPacket::new,
                    ClientboundCustomCooldownPacket::handle
            );
        });
    }
}

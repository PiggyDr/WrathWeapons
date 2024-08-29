package net.mcreator.bioswrathweapons.client.event;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.item.EnderKatanaItem;
import net.mcreator.bioswrathweapons.network.PacketHandler;
import net.mcreator.bioswrathweapons.network.ServerboundKatanaAttackPacket;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class ClientForgeEventSubscriber {

    @SubscribeEvent
    public static void onEmptyAttack(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).getItem() == BiosWrathWeaponsModItems.ENDER_KATANA.get() && !event.getEntity().getCooldowns().isOnCooldown(BiosWrathWeaponsModItems.ENDER_KATANA.get())) {
            PacketHandler.sendToServer(new ServerboundKatanaAttackPacket());
            EnderKatanaItem.shootProjectile(event.getEntity());
        }
    }
}

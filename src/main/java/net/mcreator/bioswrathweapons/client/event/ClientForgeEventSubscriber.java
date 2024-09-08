package net.mcreator.bioswrathweapons.client.event;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.Keybinds;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.mcreator.bioswrathweapons.item.EnderKatanaItem;
import net.mcreator.bioswrathweapons.item.SculkCleaverItem;
import net.mcreator.bioswrathweapons.network.PacketHandler;
import net.mcreator.bioswrathweapons.network.ServerboundEmptyAttackPacket;
import net.mcreator.bioswrathweapons.network.ServerboundEssenceAbilityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BiosWrathWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {

    @SubscribeEvent
    public static void onEmptyAttack(PlayerInteractEvent.LeftClickEmpty event) {
        BiosWrathWeaponsMod.LOGGER.debug("onEmptyAttack " + event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).is(BiosWrathWeaponsTags.SENDS_ATTACK_PACKET) + " " + event.getEntity().getAttackStrengthScale(0));
        if (event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).is(BiosWrathWeaponsTags.SENDS_ATTACK_PACKET) /*&& event.getEntity().getAttackStrengthScale(0) == 1.0F*/) {
            Item item = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).getItem();
            Player player = event.getEntity();
            BiosWrathWeaponsMod.LOGGER.debug("attack valid; " + item);
            if (item == BiosWrathWeaponsModItems.ENDER_KATANA.get()) {
                BiosWrathWeaponsMod.sendToServer(new ServerboundEmptyAttackPacket());
                EnderKatanaItem.shootProjectile(player);
            } else if (item == BiosWrathWeaponsModItems.SCULK_CLEAVER.get()) {
                SculkCleaverItem.sweep(player);
                BiosWrathWeaponsMod.sendToServer(new ServerboundEmptyAttackPacket());
            }
        }
    }

    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event) {
        if (event.getKey() == Keybinds.INSTANCE.essenceAbility.getKey().getValue() && Minecraft.getInstance().level != null) {
            BiosWrathWeaponsMod.PACKET_HANDLER.sendToServer(new ServerboundEssenceAbilityPacket());
        }
    }
}

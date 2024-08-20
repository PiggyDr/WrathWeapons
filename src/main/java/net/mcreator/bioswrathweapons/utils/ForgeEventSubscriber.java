package net.mcreator.bioswrathweapons.utils;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {

//    @SubscribeEvent
//    public static void handleDamage(LivingDamageEvent event) {
//        if (event.getEntity().hasEffect(BiosWrathWeaponsMobEffects.WATER_RESISTANCE.get()) && event.getSource().is(BiosWrathWeaponsTags.IS_WATER)) {
//            event.setCanceled(true);
//        }
//    }

}

package net.mcreator.bioswrathweapons.utils;

import com.github.L_Ender.cataclysm.client.sound.MeatShredderSound;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ReapersStrideSound extends MeatShredderSound {

    public ReapersStrideSound(LivingEntity user) {
        super(user);
    }

    @Override
    public boolean isValidItem(ItemStack itemStack) {
        return itemStack.is(BiosWrathWeaponsModItems.REAPERS_STRIDE.get());
    }
}

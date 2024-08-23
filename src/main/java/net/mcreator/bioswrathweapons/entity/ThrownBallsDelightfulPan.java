package net.mcreator.bioswrathweapons.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrownBallsDelightfulPan extends AbstractArrow {

    public ThrownBallsDelightfulPan(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    public boolean isFoil() {
        return false;
    }
}

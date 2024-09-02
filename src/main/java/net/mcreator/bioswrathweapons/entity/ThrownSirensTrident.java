package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ThrownSirensTrident extends ThrowableItemProjectile {

    public ThrownSirensTrident(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    @Override
    protected Item getDefaultItem() {
        return BiosWrathWeaponsModItems.BALLS_DELIGHTFUL_PAN.get();
    }

    public boolean isFoil() {
        return false; //TODO complete
    }
}

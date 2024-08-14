package net.mcreator.bioswrathweapons.procedures;

import net.minecraft.world.entity.Entity;

public class SculkSaberLivingEntityIsHitWithToolProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setSecondsOnFire(10);
	}
}

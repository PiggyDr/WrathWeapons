package net.mcreator.bioswrathweapons.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keybinds {
    public static final Keybinds INSTANCE = new Keybinds();

    private Keybinds() {}

    public final KeyMapping essenceAbility = new KeyMapping(
            "key." + BiosWrathWeaponsMod.MODID + ".essence_ability",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_Y, -1),
            KeyMapping.CATEGORY_MISC
    );
}

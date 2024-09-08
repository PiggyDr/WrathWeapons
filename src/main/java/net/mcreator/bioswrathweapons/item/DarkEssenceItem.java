package net.mcreator.bioswrathweapons.item;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class DarkEssenceItem extends AbstractAbilityEssenceItem {


    public DarkEssenceItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void useAbility(Player player) {
        BiosWrathWeaponsMod.LOGGER.info("used dark essence ability");
        player.level().explode(player, player.getX(), player.getY(), player.getZ(), (float) player.getDeltaMovement().length(), Level.ExplosionInteraction.MOB);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        LivingEntity entity = slotContext.entity();
        Level level = entity.level();
        if (level.getGameTime() % Math.floor(Mth.lerp(entity.getHealth() / entity.getMaxHealth(), 20F, 40F)) == 0) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 8F, 1F);
        }
    }
}

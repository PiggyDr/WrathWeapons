package net.mcreator.bioswrathweapons.proxy;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.mcreator.bioswrathweapons.client.sound.ReapersStrideSound;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.mcreator.bioswrathweapons.utils.CooldownUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final Int2ObjectMap<ReapersStrideSound> TODO_NAME_THIS_FIELD = new Int2ObjectOpenHashMap<>();

    @Override
    public void playTickableSound(LivingEntity source) {
        ReapersStrideSound sound = TODO_NAME_THIS_FIELD.computeIfAbsent(source.getId(), i -> new ReapersStrideSound(source));
        if (!sound.isSameEntity(source)) sound = new ReapersStrideSound(source);

        if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
        }
    }

    @Override
    public void stopPlayingTickableSound(LivingEntity source) {
        TODO_NAME_THIS_FIELD.remove(source.getId());
    }

    @Override
    public void displayIndomitableEssencePacket(ClientboundIndomitableEssencePacket msg) {
        Minecraft.getInstance().gameRenderer.displayItemActivation(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get().getDefaultInstance());
    }

    @Override
    public void addCooldownToTooltip(Item item, List<Component> components) {
        ItemCooldowns itemCooldowns = Minecraft.getInstance().player.getCooldowns();
        if (itemCooldowns.cooldowns.containsKey(item)) {
            int secondsToFinish = Math.floorDiv((itemCooldowns.cooldowns.get(item).endTime - itemCooldowns.tickCount), 20);
            components.add(Component.translatable("tooltip.bios_wrath_weapons.cooldown", (int) Math.floor((double) secondsToFinish / 60), secondsToFinish % 60).withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.translatable("tooltip.bios_wrath_weapons.cooldown_over").withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public void addCustomCooldown(Item item, int totalCooldown, int currentDuration) {
        CooldownUtils.addCustomCooldown(Minecraft.getInstance().player, item, totalCooldown, currentDuration);
    }
}

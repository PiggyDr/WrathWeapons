package net.mcreator.bioswrathweapons.utils;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.capability.EssenceDataCapability;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModSounds;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsTags;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BiosWrathWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (CuriosApi.getCuriosInventory(event.getEntity()).lazyMap(inventory ->
                        inventory.getStacksHandler("essence").map(slot ->
                                        slot.getStacks().getStackInSlot(0).getItem() == BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get())
                                .orElse(false)).orElse(false) && event.getEntity() instanceof Player player
                && !player.getCooldowns().isOnCooldown(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get())
                && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {

            player.getCooldowns().addCooldown(BiosWrathWeaponsModItems.INDOMITABLE_ESSENCE.get(), 6000);
            player.setHealth(1);
            player.removeAllEffects();
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            event.setCanceled(true);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BiosWrathWeaponsModSounds.INDOMITABLE_ESSENCE_ACTIVATE.get(), player.getSoundSource(), 1.0F, 1.0F);
            BiosWrathWeaponsMod.sendToPlayer(new ClientboundIndomitableEssencePacket(), (ServerPlayer) player);
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (event.getEntity().hasEffect(BiosWrathWeaponsModMobEffects.BUTTERED.get())) {
            event.setAmount(event.getAmount()*1.25F);
        }
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity && BiosWrathWeaponsModItems.hasEssence(livingEntity, BiosWrathWeaponsModItems.UNHOOLY_ESSENCE.get())) {
            livingEntity.heal(event.getAmount() / 10);
        }
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().getType() == EntityType.PLAYER && !event.getObject().getCapability(EssenceDataCapability.ESSENCE_DATA).isPresent()) {
            event.addCapability(EssenceDataCapability.RESOURCE_LOCATION, new EssenceDataCapability());
        }
    }

    @SubscribeEvent
    public static void saveCooldowns(PlayerEvent.SaveToFile event) { //i know nothing about java file manipulation so this is probably terrible
        ItemCooldowns itemCooldowns = event.getEntity().getCooldowns();
        CompoundTag cooldownNbt = new CompoundTag();
        for (Item item : itemCooldowns.cooldowns.keySet()) {
            BiosWrathWeaponsMod.LOGGER.info(ForgeRegistries.ITEMS.getKey(item));
            if (!item.getDefaultInstance().is(BiosWrathWeaponsTags.SAVE_COOLDOWNS)) continue;
            ItemCooldowns.CooldownInstance cooldown = itemCooldowns.cooldowns.get(item);
            BiosWrathWeaponsMod.LOGGER.info(cooldown);
            cooldownNbt.putIntArray(ForgeRegistries.ITEMS.getKey(item).toString(), new int[]{
                    cooldown.endTime - itemCooldowns.tickCount,
                    cooldown.endTime - cooldown.startTime
            });
        }

        File file = event.getPlayerFile("bww");
        try {
            file.createNewFile();
            NbtIo.write(cooldownNbt, file);
        } catch (IOException e) {
            BiosWrathWeaponsMod.LOGGER.error("Error while saving cooldowns");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void loadCooldowns(PlayerEvent.LoadFromFile event) {
        try {
            CompoundTag cooldownNbt = NbtIo.read(event.getPlayerFile("bww"));
            if (cooldownNbt == null) return;
            if (event.getEntity() instanceof ServerPlayer && ((ServerPlayer) event.getEntity()).connection == null) {
                Map<Item, int[]> cooldowns = new HashMap<>();
                for (String itemId : cooldownNbt.getAllKeys()) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                    if (item == null) continue;
                    cooldowns.put(item, cooldownNbt.getIntArray(itemId));
                }
                BiosWrathWeaponsMod.PROXY.cooldownsToApply.put(event.getEntity(), cooldowns);
            } else {
                for (String itemId : cooldownNbt.getAllKeys()) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                    if (item == null) continue;
                    CooldownUtils.addCustomCooldown(event.getEntity(), item, cooldownNbt.getIntArray(itemId));
                }
            }
        } catch (IOException e) {
            BiosWrathWeaponsMod.LOGGER.error("Error while loading cooldowns");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void applyCooldowns(PlayerEvent.PlayerLoggedInEvent event) {
        BiosWrathWeaponsMod.LOGGER.info("applyCooldowns");
        BiosWrathWeaponsMod.PROXY.applyCooldowns(event.getEntity());
    }
}

package net.mcreator.bioswrathweapons.proxy;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.mcreator.bioswrathweapons.client.sound.ReapersStrideSound;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.mcreator.bioswrathweapons.network.ClientboundIndomitableEssencePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
}

package net.mcreator.bioswrathweapons.capability;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class EssenceDataCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<EssenceDataCapability> ESSENCE_DATA = CapabilityManager.get(new CapabilityToken<>(){});
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(BiosWrathWeaponsMod.MODID, "essence_data");
    private final LazyOptional<EssenceDataCapability> optional = LazyOptional.of(() -> this);

    private int cooldown; //TODO implement
    private int jumpsUsed;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ESSENCE_DATA.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("jumps_used", jumpsUsed);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.jumpsUsed = nbt.getInt("jumps_used");
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int i) {
        this.cooldown = i;
    }

    public void decrementCooldown() {
        this.setCooldown(this.getCooldown() - 1);
    }

    public boolean onCooldown() {
        return this.cooldown > 0;
    }

    public int getJumpsUsed() {
        return jumpsUsed;
    }

    public void setJumpsUsed(int i) {
        this.jumpsUsed = i;
    }

    public void incrementJumpsUsed() {
        this.setJumpsUsed(this.getJumpsUsed() + 1);
    }

    public boolean hasJumpsRemaining(LivingEntity entity) {
        return BiosWrathWeaponsModItems.hasEssence(entity, BiosWrathWeaponsModItems.PHANTOM_ESSENCE.get()) && this.getJumpsUsed() < 5;
    }
}

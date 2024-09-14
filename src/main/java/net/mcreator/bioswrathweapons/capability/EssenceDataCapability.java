package net.mcreator.bioswrathweapons.capability;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class EssenceDataCapability implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<EssenceDataCapability> ESSENCE_DATA = CapabilityManager.get(new CapabilityToken<>(){});
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(BiosWrathWeaponsMod.MODID, "essence_data");
    private final LazyOptional<EssenceDataCapability> optional = LazyOptional.of(() -> this);

    private int jumpsUsed;
    private float jumpedFrom;
    private boolean hasJumpedFrom = false;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ESSENCE_DATA.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("jumps_used", jumpsUsed);
        nbt.putFloat("jumped_from", jumpedFrom);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.jumpsUsed = nbt.getInt("jumps_used");
        this.jumpedFrom = nbt.getFloat("jumped_from");
    }


    public void resetDoubleJumps() {
        this.setJumpsUsed(0);
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
        return this.getJumpsUsed() < BiosWrathWeaponsModItems.getEssence(entity).map(itemStack -> BiosWrathWeaponsModItems.getDoubleJumps(itemStack.getItem())).orElse(0);
    }

    public float getJumpedFrom() {
        return jumpedFrom;
    }

    public void setJumpedFrom(float f) {
        jumpedFrom = f;
    }

    public void updateJumpedFrom(LivingEntity entity) {
        this.setJumpedFrom(this.getJumpsUsed() == 1 ? (float) entity.getY() : Math.min(this.getJumpedFrom(), (float) entity.getY()));
    }

    public boolean hasJumpedFrom() {
        return hasJumpedFrom;
    }

    public void setHasJumpedFrom(boolean b) {
        this.hasJumpedFrom = b;
    }
}

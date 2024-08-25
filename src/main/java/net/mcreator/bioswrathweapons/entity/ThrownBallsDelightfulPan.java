package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.mcreator.bioswrathweapons.item.BallsDelightfulPanItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.Collection;

public class ThrownBallsDelightfulPan extends AbstractArrow {

    private ItemStack item;
    private boolean isReturning;
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownBallsDelightfulPan.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> ID_INITIAL_Y_ROT = SynchedEntityData.defineId(ThrownBallsDelightfulPan.class, EntityDataSerializers.FLOAT);

    public ThrownBallsDelightfulPan(EntityType<? extends ThrownBallsDelightfulPan> type, Level level) {
        super(type, level);
    }

    public ThrownBallsDelightfulPan(Level level, LivingEntity owner, ItemStack itemStack) {
        super(BiosWrathWeaponsModEntities.THROWN_BDPAN.get(), owner, level);
        this.item = itemStack;
        this.entityData.set(ID_FOIL, item.hasFoil());
        this.entityData.set(ID_INITIAL_Y_ROT, owner.getYRot());
//        BiosWrathWeaponsMod.LOGGER.info(item + " | " + level().isClientSide());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(ID_INITIAL_Y_ROT, 0F);
    }

    @Override
    protected ItemStack getPickupItem() {
        return item;
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    public float getInitialYRot() {
        return this.entityData.get(ID_INITIAL_Y_ROT);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        this.isReturning = true;
        this.setNoPhysics(true);

        float pitch = 0.9F + this.level().getRandom().nextFloat() * 0.2F;
        this.level().playSound(null, getX(), getY(), getZ(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 2.0F, pitch);

        if (
                this.level().isClientSide()
                && this.getOwner() != null
                && this.getOwner() instanceof Player owner
                && owner.distanceToSqr(this) > 4.0
        ) {
            BiosWrathWeaponsMod.LOGGER.info("playing client sound");
            owner.playSound(ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), 0.5F, pitch);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide()) {
            DamageSource damageSource = this.damageSources().arrow(this, result.getEntity());
            float damage = (float) this.calculateDamage();
            result.getEntity().hurt(damageSource, damage);
        }
        //TODO finish
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    private double calculateDamage() {
//        return 0;
//        BiosWrathWeaponsMod.LOGGER.info("calculateDamage: " + level().isClientSide());
        Collection<AttributeModifier> modifiers = this.item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE);
        AttributeInstance attributeInstance = new AttributeInstance(Attributes.ATTACK_DAMAGE, a -> {});
        for (AttributeModifier modifier : modifiers) {
            attributeInstance.addTransientModifier(modifier);
        }
//        BiosWrathWeaponsMod.LOGGER.info("calculateDmasaage: compelte");
        return attributeInstance.getValue();
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();
        if (this.isReturning && owner != null) {
            Vec3 vec3 = this.getOwner().getEyePosition().subtract(this.position());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(0.18)));
        }
//        BiosWrathWeaponsMod.LOGGER.info(item + " | " + level().isClientSide());
        super.tick();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.isReturning = nbt.getBoolean("is_returning");
        this.item = ItemStack.of(nbt.getCompound("item"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("is_returning", isReturning);
        nbt.put("item", item.save(new CompoundTag()));
    }
}

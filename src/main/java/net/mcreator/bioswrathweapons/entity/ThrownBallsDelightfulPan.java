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
import net.minecraft.world.entity.projectile.ThrownTrident;
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
    private static final EntityDataAccessor<Integer> ID_BOUNCES = SynchedEntityData.defineId(ThrownBallsDelightfulPan.class, EntityDataSerializers.INT);

    public ThrownBallsDelightfulPan(EntityType<? extends ThrownBallsDelightfulPan> type, Level level) {
        super(type, level);
    }

    public ThrownBallsDelightfulPan(Level level, LivingEntity owner, ItemStack itemStack) {
        super(BiosWrathWeaponsModEntities.THROWN_BDPAN.get(), owner, level);
        this.item = itemStack;
        this.entityData.set(ID_FOIL, item.hasFoil());
        this.entityData.set(ID_INITIAL_Y_ROT, owner.getYRot());
        this.entityData.set(ID_BOUNCES, 0);
        this.setPierceLevel((byte) 5);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(ID_INITIAL_Y_ROT, 0F);
        this.entityData.define(ID_BOUNCES, 0);
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

    public int getBounces() {
        return this.entityData.get(ID_BOUNCES);
    }

    public void incrementBounces() {
        this.entityData.set(ID_BOUNCES, this.entityData.get(ID_BOUNCES) + 1);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        float pitch = 0.9F + this.random.nextFloat() * 0.2F;
        this.level().playSound(null, getX(), getY(), getZ(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 2.0F, pitch);
        if (
                this.level().isClientSide()
                && this.getOwner() != null
                && this.getOwner() instanceof Player owner
                && owner.distanceToSqr(this) > 4.0
        ) {
            owner.playSound(ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), 0.5F, pitch);
        }

        if (true) {
            this.startReturn(result);
        } else {
            //TODO finish
        }
        this.incrementBounces();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        if (this.isReturning)
            return; //there was this weird issue where the game would freeze if a pan tried to hit an entity while returning. too lazy to figure out a proper solution but this one should be as good as any
        BiosWrathWeaponsMod.LOGGER.info("tried to hit entity " + p_36757_.getEntity() + " " + ((LivingEntity)p_36757_.getEntity()).getHealth());
        super.onHitEntity(p_36757_);
    }

    public void startReturn(HitResult result) {
        this.isReturning = true;
        this.setNoPhysics(true);

        if (result instanceof BlockHitResult blockHitResult) {
            switch (blockHitResult.getDirection()) {
                case UP, DOWN -> this.setDeltaMovement(getDeltaMovement().multiply(
                        random.triangle(1.5, 1),
                        Math.max(random.triangle(1.5, 1), 1) * -1,
                        random.triangle(1.5, 1)
                ));
                case NORTH, SOUTH -> this.setDeltaMovement(getDeltaMovement().multiply(1, 1, -1).add(
                        random.triangle(1.5, 1),
                        random.triangle(1.5, 1),
                        Math.max(random.triangle(1.5, 1), 1) * -1
                ));
                case EAST, WEST -> this.setDeltaMovement(getDeltaMovement().multiply(-1, 1, 1).add(
                        Math.max(random.triangle(1.5, 1), 1) * -1,
                        random.triangle(1.5, 1),
                        random.triangle(1.5, 1)
                ));
            }
        } else {
            this.setDeltaMovement(getDeltaMovement().multiply(
                    -random.triangle(1.5, 1),
                    -random.triangle(1.5, 1),
                    -random.triangle(1.5, 1)
            ));
        }
    }

    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    private double calculateDamage() {
        Collection<AttributeModifier> modifiers = this.item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE);
        AttributeInstance attributeInstance = new AttributeInstance(Attributes.ATTACK_DAMAGE, a -> {});
        for (AttributeModifier modifier : modifiers) {
            attributeInstance.addTransientModifier(modifier);
        }
        return attributeInstance.getValue();
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();
        if (this.isReturning && owner != null) {
            Vec3 vec3 = this.getOwner().getEyePosition().subtract(this.position());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(0.18)));
        }
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

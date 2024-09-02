package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collection;

public class ThrownSirensTrident extends AbstractArrow {

    private ItemStack item;
    private boolean isReturning;
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSirensTrident.class, EntityDataSerializers.BOOLEAN);

    public ThrownSirensTrident(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public ThrownSirensTrident(LivingEntity owner, ItemStack itemStack, Level level) {
        super(BiosWrathWeaponsModEntities.SIRENS_TRIDENT.get(), owner, level);
        this.item = itemStack;
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
        this.setPos(owner.getEyePosition());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Override
    protected ItemStack getPickupItem() {
        return item;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.startReturn();
        }

        if (this.isReturning && this.getOwner() != null) {
            Vec3 direction = this.getOwner().getEyePosition().subtract(this.position());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(direction.normalize().scale(0.15)));
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (this.level().isClientSide()) return;

        float damage = (float) getItemAttributeValue(Attributes.ATTACK_DAMAGE);
        double knockback = (getItemAttributeValue(Attributes.ATTACK_KNOCKBACK) * 0.75) + 0.3;
        if (entity instanceof LivingEntity lentity) {
            damage += EnchantmentHelper.getDamageBonus(this.item, lentity.getMobType());
            knockback += this.item.getEnchantmentLevel(Enchantments.KNOCKBACK);
        }
        damage *= 0.85F;

        DamageSource damageSource = this.damageSources().trident(this, getOwner() == null ? this : getOwner());
        if (entity.hurt(damageSource, damage) && entity.getType() != EntityType.ENDERMAN) {
            if (this.isOnFire())
                entity.setSecondsOnFire(5);

            if (entity instanceof LivingEntity lentity) {
                if (this.getOwner() instanceof LivingEntity livingOwner) {
                    EnchantmentHelper.doPostHurtEffects(lentity, livingOwner);
                    EnchantmentHelper.doPostDamageEffects(lentity, livingOwner);
                    lentity.setLastHurtByMob(livingOwner);
                }

                double kbResistance = Math.max(0.0D, 1.0D - lentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                Vec3 kbVector = this.getDeltaMovement().normalize().scale(knockback * kbResistance);
                lentity.knockback(kbVector.x, kbVector.y, kbVector.z);
            }

            if (this.getOwner() instanceof LivingEntity livingOwner) {
                livingOwner.setLastHurtMob(entity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));

        if (this.level() instanceof ServerLevel && this.level().isThundering() && EnchantmentHelper.hasChanneling(this.item) && this.level().canSeeSky(entity.blockPosition())) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
            if (bolt != null) {
                bolt.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
                bolt.setCause(this.getOwner() instanceof ServerPlayer serverOwner ? serverOwner : null);
                this.level().addFreshEntity(bolt);
                this.playSound(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0F);
                return;
            }
        }

        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 idk1, Vec3 idk2) {
        return this.isReturning ? null : super.findHitEntity(idk1, idk2);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    private void startReturn() {
        this.isReturning = true;
        this.setNoPhysics(true);
        this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
    }

    private double getItemAttributeValue(Attribute attribute) {
        Collection<AttributeModifier> modifiers = this.item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(attribute);
        AttributeInstance attributeInstance = new AttributeInstance(attribute, a -> {});
        for (AttributeModifier modifier : modifiers) {
            attributeInstance.addTransientModifier(modifier);
        }
        return attributeInstance.getValue();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("item", item.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        item = ItemStack.of(nbt.getCompound("item"));
    }
}

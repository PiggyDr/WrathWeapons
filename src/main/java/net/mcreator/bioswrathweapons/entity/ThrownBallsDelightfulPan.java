package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class ThrownBallsDelightfulPan extends AbstractArrow {

    private ItemStack item;
    private boolean isReturning;

    public ThrownBallsDelightfulPan(EntityType<? extends ThrownBallsDelightfulPan> type, Level level) {
        super(type, level);
    }

    public ThrownBallsDelightfulPan(Level level, LivingEntity owner, ItemStack itemStack) {
        super(BiosWrathWeaponsModEntities.THROWN_BDPAN.get(), owner, level);
        this.item = itemStack;
//        BiosWrathWeaponsMod.LOGGER.info(item + " | " + level().isClientSide());
    }

    @Override
    protected ItemStack getPickupItem() {
        return item;
    }

    public boolean isFoil() {
        return item.hasFoil();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.isReturning = true;
        this.setNoPhysics(true);
        //TODO add sfx
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

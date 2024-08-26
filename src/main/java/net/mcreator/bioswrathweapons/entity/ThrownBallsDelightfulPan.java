package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.mcreator.bioswrathweapons.item.BallsDelightfulPanItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ThrownBallsDelightfulPan extends AbstractArrow {

    private ItemStack item;
    private boolean isReturning;
    private List<Entity> hitEntities = new ArrayList<>();
    private long lastHitTime = -1;
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
        return item.copy();
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
        BiosWrathWeaponsMod.LOGGER.info("incrementBounces: " + getBounces());
        this.entityData.set(ID_BOUNCES, getBounces() + 1);
    }

    @Override
    protected void onHit(HitResult result) {
        BiosWrathWeaponsMod.LOGGER.info("onHit: " + result.getClass());
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

        if (this.getBounces() > 4) {
            this.startReturn(result);
        }

        AABB searchBox = new AABB(this.position().add(-5, -5, -5), this.position().add(5, 5, 5));
        List<Mob> potentialTargets = this.level().getEntitiesOfClass(Mob.class, searchBox);
        potentialTargets.removeAll(this.hitEntities.stream().filter(e -> e instanceof Mob).toList());
        if (potentialTargets.isEmpty()) {
            this.startReturn(result);
        } else {
            this.bounceToEntity(result, potentialTargets.stream().max((e1, e2) -> (int) (position().distanceTo(e1.position()) - position().distanceTo(e2.position()))).get());
        }

        this.incrementBounces();
        this.lastHitTime = this.level().getGameTime();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (entity == this.getOwner() || this.level().isClientSide()) return;

        float damage = (float) getItemAttributeValue(Attributes.ATTACK_DAMAGE);
        double knockback = (getItemAttributeValue(Attributes.ATTACK_KNOCKBACK) * 0.75) + 0.3;
        int fire = this.item.getEnchantmentLevel(Enchantments.FIRE_ASPECT) * 4;
        if (entity instanceof LivingEntity lentity) {
            damage += EnchantmentHelper.getDamageBonus(this.item, lentity.getMobType());
            knockback += this.item.getEnchantmentLevel(Enchantments.KNOCKBACK);
        }
        if (this.isOnFire())
            fire += 5;
        damage *= 0.85F;

        DamageSource damageSource = this.damageSources().arrow(this, getOwner() == null ? this : getOwner());
        if (entity.hurt(damageSource, damage) && entity.getType() != EntityType.ENDERMAN) {
            entity.setSecondsOnFire(Math.max(entity.getRemainingFireTicks(), fire));

            if (entity instanceof LivingEntity lentity) {
                if (this.getOwner() instanceof LivingEntity livingOwner) {
                    EnchantmentHelper.doPostHurtEffects(lentity, livingOwner);
                    EnchantmentHelper.doPostDamageEffects(lentity, livingOwner);
                }
                this.doPostHurtEffects(lentity);

                double kbResistance = Math.max(0.0D, 1.0D - lentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                Vec3 kbVector = this.getDeltaMovement().normalize().scale(knockback * kbResistance);
                lentity.knockback(kbVector.x, kbVector.y, kbVector.z);
            }

            if (this.getOwner() instanceof LivingEntity livingOwner) {
                livingOwner.setLastHurtMob(entity);
            }
        }

        this.hitEntities.add(entity);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
    }

    public void bounceToEntity(HitResult result, Entity target) {
        Vec3 push = target.position().subtract(this.position()).normalize();
        double scalar = 0.25;// / (Mth.sqrt((float) push.length()) * 4);
        push = push.multiply(0.25, 0.25, 0.25);
        if (result instanceof BlockHitResult blockHitResult) {
            Vec3i normal = blockHitResult.getDirection().getOpposite().getNormal();
            push.add(normal.getX() * 0.1, normal.getY() * 0.1, normal.getZ() * 0.1);
        }
        this.setDeltaMovement(push);
//        startReturn(result);
    }

    public void startReturn(@Nullable HitResult result) {
        this.isReturning = true;
        this.setNoPhysics(true);
        if (this.level().isClientSide())
            this.getOwner().playSound(SoundEvents.TRIDENT_RETURN);

        if (result instanceof BlockHitResult blockHitResult) {
            BiosWrathWeaponsMod.LOGGER.info("startReturn>block: " + blockHitResult.getBlockPos());
            switch (blockHitResult.getDirection()) {
                case UP, DOWN -> this.setDeltaMovement(getDeltaMovement().multiply(
                        random.triangle(1.5, 1),
                        Math.max(random.triangle(1.5, 1), 1) * -1,
                        random.triangle(1.5, 1)
                ));
                case NORTH, SOUTH -> this.setDeltaMovement(getDeltaMovement().multiply(
                        random.triangle(1.5, 1),
                        random.triangle(1.5, 1),
                        Math.max(random.triangle(1.5, 1), 1) * -1
                ));
                case EAST, WEST -> this.setDeltaMovement(getDeltaMovement().multiply(
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

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 idk1, Vec3 idk2) {
        if (this.isReturning) return null;
        EntityHitResult result = super.findHitEntity(idk1, idk2);
        if (result == null || this.hitEntities.contains(result.getEntity())) return null;
        return result;
    }

    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
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
    public void tick() {
        Entity owner = this.getOwner();
        if (this.isReturning && owner != null) {
            Vec3 vec3 = this.getOwner().getEyePosition().subtract(this.position());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(0.18)));
        }
        if (!this.isReturning && this.lastHitTime != -1 && this.level().getGameTime() - this.lastHitTime > 50)
            this.startReturn(null);
        super.tick();
        this.inGround = false; //stoppit abstractarrow >:( the pan is NOT stuck in the ground
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

package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModMobEffects;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;
import java.util.*;

public class ThrownBallsDelightfulPan extends AbstractHurtingProjectile { //probably shouldve used one of the thrown projectile classes

    private ItemStack item;
    private boolean isReturning;
    private final List<Entity> hitEntities = new ArrayList<>();
    private long lastHitTime = -1;
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownBallsDelightfulPan.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> ID_INITIAL_Y_ROT = SynchedEntityData.defineId(ThrownBallsDelightfulPan.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> ID_BOUNCES = SynchedEntityData.defineId(ThrownBallsDelightfulPan.class, EntityDataSerializers.INT);

    public ThrownBallsDelightfulPan(EntityType<? extends ThrownBallsDelightfulPan> type, Level level) {
        super(type, level);
    }

    public ThrownBallsDelightfulPan(Level level, LivingEntity owner, ItemStack itemStack) {
        super(BiosWrathWeaponsModEntities.THROWN_BDPAN.get(), owner, 0, 0, 0, level);
        this.moveTo(this.position().add(0, owner.getEyeHeight(), 0));
        this.reapplyPosition();
        this.item = itemStack;
        this.lastHitTime = level.getGameTime() + 200;
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
        this.entityData.set(ID_BOUNCES, getBounces() + 1);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.noPhysics || this.isRemoved()) return;
        this.noPhysics = true;
        this.playHitSound();

        if (this.getBounces() > 4) {
            this.startReturn(result);
        }

        AABB searchBox = new AABB(this.position().add(-8, -5, -8), this.position().add(8, 5, 8));
        List<Mob> potentialTargets = this.level().getEntitiesOfClass(Mob.class, searchBox);
        potentialTargets.removeAll(this.hitEntities.stream().filter(e -> e instanceof Mob).toList());
        if (potentialTargets.isEmpty()) {
            this.startReturn(result);
        } else {
            this.bounceToEntity(potentialTargets.stream().max((e1, e2) -> (int) (position().distanceTo(e1.position()) - position().distanceTo(e2.position()))).get());
        }

        this.incrementBounces();
        this.lastHitTime = this.level().getGameTime();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (this.level().isClientSide()) return;

        if (this.ownedBy(entity)) { //try to pick up pan
            if (this.isReturning) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F); //FIXME might not play?

                if (entity instanceof Player player && player.getInventory().canPlaceItem(1, this.item)) {
                    if (!(player.getAbilities().instabuild && player.getInventory().hasAnyOf(Set.of(BiosWrathWeaponsModItems.BALLS_DELIGHTFUL_PAN.get()))))
                        player.getInventory().add(this.item);
                } else {
                    ItemEntity itemEntity = new ItemEntity(this.level(), getX(), getY(), getZ(), this.item);
                    this.level().addFreshEntity(itemEntity);
                }

                this.discard();
            }
        } else { //try to hurt entity
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

            DamageSource damageSource = this.damageSources().trident(this, getOwner() == null ? this : getOwner());
            if (entity.hurt(damageSource, damage) && entity.getType() != EntityType.ENDERMAN) {
                entity.setSecondsOnFire(fire);

                if (entity instanceof LivingEntity lentity) {
                    if (this.getOwner() instanceof LivingEntity livingOwner) {
                        EnchantmentHelper.doPostHurtEffects(lentity, livingOwner);
                        EnchantmentHelper.doPostDamageEffects(lentity, livingOwner);
                        lentity.setLastHurtByMob(livingOwner);
                    }

                    double kbResistance = Math.max(0.0D, 1.0D - lentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 kbVector = this.getDeltaMovement().normalize().scale(knockback * kbResistance);
                    lentity.knockback(kbVector.x, kbVector.y, kbVector.z);
                    lentity.addEffect(new MobEffectInstance(BiosWrathWeaponsModMobEffects.BUTTERED.get(), 600));
                }

                if (this.getOwner() instanceof LivingEntity livingOwner) {
                    livingOwner.setLastHurtMob(entity);
                }
            }

            this.hitEntities.add(entity);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.noPhysics)
            super.onHitBlock(result);
    }

    private void playHitSound() {
        float pitch = 0.9F + this.random.nextFloat() * 0.2F;
        if (this.getOwner() instanceof Player player && this.level().isClientSide()) {
            player.playSound(ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), 0.5F, pitch);
        } else {
            this.level().playSound(null, getX(), getY(), getZ(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 2.0F, pitch);
        }
    }

    public void bounceToEntity(Entity target) {
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - this.getY();
        double z = target.getZ() - this.getZ();
        this.shoot(x, y + Math.sqrt(x * x + z * z) * 0.35, z, 0.8F, 0.1F);
    }

    public void startReturn(@Nullable HitResult result) {
        this.isReturning = true;
        if (this.getOwner() instanceof Player player && this.level().isClientSide()) {
            player.playSound(SoundEvents.TRIDENT_RETURN);
        } else {
            this.level().playSound(null, getX(), getY(), getZ(), SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        if (result instanceof BlockHitResult blockHitResult) {
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

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 0.99F;
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
            Vec3 direction = owner.getEyePosition().subtract(this.position());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(direction.normalize().scale(0.18)));
        }
        if (!this.isReturning) {
            if (!this.inBlock()) {
                this.noPhysics = false;
            }

            if (this.lastHitTime != -1 && this.level().getGameTime() - this.lastHitTime > 50) {
                this.startReturn(null);
            }
        }
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.05F, 0));
    }

    private boolean inBlock() {
        BlockPos pos = this.blockPosition();
        BlockState blockState = this.level().getBlockState(pos);
        if (!blockState.isAir()) {
            VoxelShape voxelshape = blockState.getCollisionShape(this.level(), pos);
            if (!voxelshape.isEmpty()) {
                return voxelshape.toAabbs().stream().anyMatch(aabb -> aabb.move(pos).contains(this.position()));
            }
        }
        return false;
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

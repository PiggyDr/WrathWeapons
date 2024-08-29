package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EnderKatanaProjectile extends AbstractHurtingProjectile {

    private long creationTime;

    public EnderKatanaProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    public EnderKatanaProjectile(LivingEntity owner, double powerX, double powerY, double powerZ, Level level) {
        super(BiosWrathWeaponsModEntities.ENDER_KATANA_PROJECTILE.get(), owner, powerX, powerY, powerZ, level);
        this.creationTime = this.level().getGameTime();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH.getType();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.isInvulnerableTo(source) && amount != 0) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    public void shoot(double x, double y, double z, float power, float inaccuracy) {
        super.shoot(x, y, z, power, inaccuracy);
        this.xPower = x * 0.2;
        this.yPower = y * 0.2;
        this.zPower = z * 0.2;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().getGameTime() - this.creationTime > 100) {
            this.discard();
        }
        if (!this.level().isClientSide()) {
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.DRAGON_BREATH.getType(), getX(), getY(), getZ(), 3, 0.2, 0.2, 0.2, 0.03);
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.REVERSE_PORTAL.getType(), getX(), getY(), getZ(), 6, 0.4, 0.4, 0.4, 0.03);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide()) {
            if (result.getEntity().hurt(this.damageSources().indirectMagic(this, this.getOwner()), 4) && this.getOwner() instanceof LivingEntity owner) {
                this.doEnchantDamageEffects(owner, result.getEntity());
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putLong("creation_time", creationTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        nbt.getLong("creation_time");
    }
}

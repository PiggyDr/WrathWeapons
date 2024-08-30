package net.mcreator.bioswrathweapons.entity;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

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
        if (!this.level().isClientSide()) {
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.DRAGON_BREATH.getType(), getX(), getY(), getZ(), 3, 0.2, 0.2, 0.2, 0.03);
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.REVERSE_PORTAL.getType(), getX(), getY(), getZ(), 6, 0.4, 0.4, 0.4, 0.03);
            if (this.level().getGameTime() - this.creationTime > 100) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.playSound();
        if (!this.level().isClientSide()) {
            Entity entity = result.getEntity();
            doFancyMagicAttack(entity);
            Vec3 pos = entity.position();
            AABB searchBox = new AABB(
                    new Vec3(pos.x()-12, pos.y()-12, pos.z()-12),
                    new Vec3(pos.x()+12, pos.y()+6, pos.z()+12)
            );
            chainAttack(searchBox, 2, entity instanceof Monster monster ? monster : null);
            this.discard();
        }
    }

    private void chainAttack(AABB searchBox, int targetCount, @Nullable Monster disallowed) {
        List<Monster> potentialTargets = level().getEntitiesOfClass(Monster.class, searchBox);
        if (disallowed != null) potentialTargets.removeAll(Set.of(disallowed));
        potentialTargets.sort((m1,m2) -> (int) (this.position().distanceToSqr(m1.position()) - this.position().distanceToSqr(m2.position())));
        for (Monster target : potentialTargets) {
            if (doFancyMagicAttack(target)) --targetCount;
            if (targetCount == 0) return;
        }
    }

    private boolean doFancyMagicAttack(Entity entity) {
        boolean hurt = entity.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 4);
        if (hurt) {
            if (this.getOwner() instanceof LivingEntity owner)
                this.doEnchantDamageEffects(owner, entity);
            if (!this.level().isClientSide()) {
                double deltaY = entity.getBbHeight() * 0.3;
                double deltaHorz = entity.getBbWidth() * 0.4;
                int count = (int) Math.floor(entity.getBbHeight()*entity.getBbWidth()*16);

                ((ServerLevel)this.level()).sendParticles(ParticleTypes.REVERSE_PORTAL.getType(), entity.getX(), entity.getY(1F), entity.getZ(), count*2, deltaHorz*1.2, deltaY*1.2, deltaHorz*1.2, 0.2);
                ((ServerLevel)this.level()).sendParticles(ParticleTypes.ENCHANTED_HIT.getType(), entity.getX(), entity.getY(1F), entity.getZ(), count, deltaHorz, deltaY, deltaHorz, 0);
            }
        }
        return hurt;
    }

    private void playSound() {
        BiosWrathWeaponsMod.LOGGER.info("playSound");
        if (this.getOwner() instanceof Player player) {
            BiosWrathWeaponsMod.LOGGER.info("playing to player");
            player.playSound(BiosWrathWeaponsModSounds.PLACEHOLDER.get(), 2F, 1F);
        } else {
            BiosWrathWeaponsMod.LOGGER.info("playing to world");
            this.level().playSound(null, getX(), getY(), getZ(), BiosWrathWeaponsModSounds.PLACEHOLDER.get(), SoundSource.PLAYERS, 2F, 1F);
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

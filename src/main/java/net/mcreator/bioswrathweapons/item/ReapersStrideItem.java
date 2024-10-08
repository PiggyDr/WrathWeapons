package net.mcreator.bioswrathweapons.item;

import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.items.Meat_Shredder;
import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ReapersStrideItem extends Meat_Shredder {

    public ReapersStrideItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    /*
        i cant be bothered to make anything resembling an actual formal licensing notice like i did for fd so uh
        the majority of this method was copied from l enders cataclysm; specifically the meat shredder
        i slightly increased the damage, changed the damage type, and swapped out the proxy tickable method so it accepts the item as valid
        lenders cataclysm is licenced under lgpl 3 which you can find at https://www.gnu.org/licenses/lgpl-3.0.en.html#license-text
     */
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        double range = 2.5;
        Vec3 srcVec = living.getEyePosition();
        Vec3 lookVec = living.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        float var9 = 1.0F;
        List<Entity> possibleList = level.getEntities(living, living.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate((double)var9, (double)var9, (double)var9));
        boolean flag = false;
        BiosWrathWeaponsMod.PROXY.playTickableSound(living);
        Iterator var13 = possibleList.iterator();

        while (var13.hasNext()) {
            Entity entity = (Entity)var13.next();
            if (entity instanceof LivingEntity) {
                float borderSize = 0.5F;
                AABB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
                if (collisionBB.contains(srcVec)) {
                    flag = true;
                } else if (interceptPos.isPresent()) {
                    flag = true;
                }

                if (flag) {
                    if (entity.hurt(BiosWrathWeaponsModDamageTypes.reapersStride(living), (float)living.getAttributeValue(Attributes.ATTACK_DAMAGE) / 8.5F + 0.5F)) {
                        int j = EnchantmentHelper.getFireAspect(living);
                        if (j > 0 && !entity.isOnFire()) {
                            entity.setSecondsOnFire(j * 4);
                        }
                    }

                    double d0 = (double)(level.getRandom().nextFloat() - 0.5F) + entity.getDeltaMovement().x;
                    double d1 = (double)(level.getRandom().nextFloat() - 0.5F) + entity.getDeltaMovement().y;
                    double d2 = (double)(level.getRandom().nextFloat() - 0.5F) + entity.getDeltaMovement().z;
                    double dist = (1.0F + level.getRandom().nextFloat() * 0.2F);
                    double d3 = d0 * dist;
                    double d4 = d1 * dist;
                    double d5 = d2 * dist;
                    entity.level().addParticle(ParticleTypes.LAVA, entity.getX(), living.getEyeY() - 0.1 + (entity.getEyePosition().y - living.getEyeY()), entity.getZ(), d3, d4, d5);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int remainingUseTicks) {
        world.playSound(null, living.getX(), living.getY(), living.getZ(), ModSounds.SHREDDER_END.get(), SoundSource.PLAYERS, 1.5F, 1.0F / (living.getRandom().nextFloat() * 0.4F + 0.8F));
        BiosWrathWeaponsMod.PROXY.stopPlayingTickableSound(living);
    }
}

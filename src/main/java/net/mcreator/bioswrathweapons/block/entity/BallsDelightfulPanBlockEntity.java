package net.mcreator.bioswrathweapons.block.entity;

import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.Optional;

/*
    this file or significant portions of it were taken from farmers delight; see the readme for details

    MIT License

    Copyright (c) 2020 vectorwing

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */

public class BallsDelightfulPanBlockEntity extends SyncedBlockEntity implements HeatableBlockEntity {
    private final ItemStackHandler inventory = this.createHandler();
    private int cookingTime;
    private int cookingTimeTotal;
    private ResourceLocation lastRecipeID;
    private ItemStack skilletStack;
    private int fireAspectLevel;

    public BallsDelightfulPanBlockEntity(BlockPos pos, BlockState state) {
        super(BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get(), pos, state);
        this.skilletStack = ItemStack.EMPTY;
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, BallsDelightfulPanBlockEntity skillet) {
        boolean isHeated = skillet.isHeated(level, pos);
        if (isHeated) {
            ItemStack cookingStack = skillet.getStoredStack();
            if (cookingStack.isEmpty()) {
                skillet.cookingTime = 0;
            } else {
                skillet.cookAndOutputItems(cookingStack, level);
            }
        } else if (skillet.cookingTime > 0) {
            skillet.cookingTime = Mth.clamp(skillet.cookingTime - 2, 0, skillet.cookingTimeTotal);
        }

    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, BallsDelightfulPanBlockEntity skillet) {
        if (skillet.isHeated(level, pos) && skillet.hasStoredStack()) {
            RandomSource random = level.random;
            double x;
            double y;
            double z;
            double motionX;
            if (random.nextFloat() < 0.2F) {
                x = (double)pos.getX() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                y = (double)pos.getY() + 0.1;
                z = (double)pos.getZ() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                motionX = random.nextBoolean() ? 0.015 : 0.005;
                level.addParticle((ParticleOptions)ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionX, 0.0);
            }

            if (skillet.fireAspectLevel > 0 && random.nextFloat() < (float)skillet.fireAspectLevel * 0.05F) {
                x = (double)pos.getX() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                y = (double)pos.getY() + 0.1;
                z = (double)pos.getZ() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                motionX = (double)(level.random.nextFloat() - 0.5F);
                double motionY = (double)(level.random.nextFloat() * 0.5F + 0.2F);
                double motionZ = (double)(level.random.nextFloat() - 0.5F);
                level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
            }
        }

    }

    private void cookAndOutputItems(ItemStack cookingStack, Level level) {
        if (level != null) {
            ++this.cookingTime;
            if (this.cookingTime >= this.cookingTimeTotal) {
                SimpleContainer wrapper = new SimpleContainer(new ItemStack[]{cookingStack});
                Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(wrapper);
                if (recipe.isPresent()) {
                    ItemStack resultStack = ((CampfireCookingRecipe)recipe.get()).assemble(wrapper, level.registryAccess());
                    Direction direction = ((Direction)this.getBlockState().getValue(SkilletBlock.FACING)).getClockWise();
                    ItemUtils.spawnItemEntity(level, resultStack.copy(), (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 0.3, (double)this.worldPosition.getZ() + 0.5, (double)((float)direction.getStepX() * 0.08F), 0.25, (double)((float)direction.getStepZ() * 0.08F));
                    this.cookingTime = 0;
                    this.inventory.extractItem(0, 1, false);
                }
            }

        }
    }

    public boolean isCooking() {
        return this.isHeated() && this.hasStoredStack();
    }

    public boolean isHeated() {
        return this.level != null ? this.isHeated(this.level, this.worldPosition) : false;
    }

    private Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper) {
        if (this.level == null) {
            return Optional.empty();
        } else {
            if (this.lastRecipeID != null) {
                Recipe<Container> recipe = (Recipe)((RecipeManagerAccessor)this.level.getRecipeManager()).getRecipeMap(RecipeType.CAMPFIRE_COOKING).get(this.lastRecipeID);
                if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, this.level)) {
                    return Optional.of((CampfireCookingRecipe)recipe);
                }
            }

            Optional<CampfireCookingRecipe> recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeWrapper, this.level);
            if (recipe.isPresent()) {
                this.lastRecipeID = ((CampfireCookingRecipe)recipe.get()).getId();
                return recipe;
            } else {
                return Optional.empty();
            }
        }
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.cookingTime = compound.getInt("CookTime");
        this.cookingTimeTotal = compound.getInt("CookTimeTotal");
        this.skilletStack = ItemStack.of(compound.getCompound("Skillet"));
        this.fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, this.skilletStack);
    }

    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putInt("CookTime", this.cookingTime);
        compound.putInt("CookTimeTotal", this.cookingTimeTotal);
        compound.put("Skillet", this.skilletStack.save(new CompoundTag()));
    }

    public CompoundTag writeSkilletItem(CompoundTag compound) {
        compound.put("Skillet", this.skilletStack.save(new CompoundTag()));
        return compound;
    }

    public void setSkilletItem(ItemStack stack) {
        this.skilletStack = stack.copy();
        this.fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        this.inventoryChanged();
    }

    public ItemStack addItemToCook(ItemStack addedStack, @Nullable Player player) {
        Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(new SimpleContainer(new ItemStack[]{addedStack}));
        if (recipe.isPresent()) {
            this.cookingTimeTotal = SkilletBlock.getSkilletCookingTime(((CampfireCookingRecipe)recipe.get()).getCookingTime(), this.fireAspectLevel);
            boolean wasEmpty = this.getStoredStack().isEmpty();
            ItemStack remainderStack = this.inventory.insertItem(0, addedStack.copy(), false);
            if (!ItemStack.matches(remainderStack, addedStack)) {
                this.lastRecipeID = ((CampfireCookingRecipe)recipe.get()).getId();
                this.cookingTime = 0;
                if (wasEmpty && this.level != null && this.isHeated(this.level, this.worldPosition)) {
                    this.level.playSound((Player)null, (double)((float)this.worldPosition.getX() + 0.5F), (double)((float)this.worldPosition.getY() + 0.5F), (double)((float)this.worldPosition.getZ() + 0.5F), (SoundEvent)ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
                }

                return remainderStack;
            }
        } else if (player != null) {
            player.displayClientMessage(TextUtils.getTranslation("block.skillet.invalid_item", new Object[0]), true);
        }

        return addedStack;
    }

    public ItemStack removeItem() {
        return this.inventory.extractItem(0, this.getStoredStack().getMaxStackSize(), false);
    }

    public IItemHandler getInventory() {
        return this.inventory;
    }

    public ItemStack getStoredStack() {
        return this.inventory.getStackInSlot(0);
    }

    public boolean hasStoredStack() {
        return !this.getStoredStack().isEmpty();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler() {
            protected void onContentsChanged(int slot) {
                BallsDelightfulPanBlockEntity.this.inventoryChanged();
            }
        };
    }

    public void setRemoved() {
        super.setRemoved();
    }
}
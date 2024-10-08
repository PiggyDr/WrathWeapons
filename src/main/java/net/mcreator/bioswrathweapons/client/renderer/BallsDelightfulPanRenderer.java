package net.mcreator.bioswrathweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mcreator.bioswrathweapons.block.entity.BallsDelightfulPanBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class BallsDelightfulPanRenderer implements BlockEntityRenderer<BallsDelightfulPanBlockEntity> {

    private final Random random = new Random();

    public BallsDelightfulPanRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BallsDelightfulPanBlockEntity skilletEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Direction direction = skilletEntity.getBlockState().getValue(StoveBlock.FACING);
        IItemHandler inventory = skilletEntity.getInventory();
        int posLong = (int) skilletEntity.getBlockPos().asLong();

        ItemStack stack = inventory.getStackInSlot(0);
        int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
        this.random.setSeed(seed);

        if (!stack.isEmpty()) {
            int itemRenderCount = this.getModelCount(stack);
            for (int i = 0; i < itemRenderCount; i++) {
                poseStack.pushPose();

                // Stack up items in the skillet, with a slight offset per item
                float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.06F;
                float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.06F;
                poseStack.translate(0.5D + xOffset, + 0.03 * (i + 1), 0.5D + zOffset);

                // Rotate item to face the skillet's front side
                float degrees = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(degrees));

                // move item to be in center of the pan
                poseStack.translate(0.0D, 0.0D, -0.1D);

                // Rotate item flat on the skillet. Use X and Y from now on
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                // Resize the items
                poseStack.scale(0.45F, 0.45F, 0.45F);

                if (skilletEntity.getLevel() != null)
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, skilletEntity.getLevel(), posLong);
                poseStack.popPose();
            }
        }
    }

    protected int getModelCount(ItemStack stack) {
        if (stack.getCount() > 48) {
            return 5;
        } else if (stack.getCount() > 32) {
            return 4;
        } else if (stack.getCount() > 16) {
            return 3;
        } else if (stack.getCount() > 1) {
            return 2;
        }
        return 1;
    }
}

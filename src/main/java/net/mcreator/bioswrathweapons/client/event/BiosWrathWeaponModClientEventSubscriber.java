package net.mcreator.bioswrathweapons.client.event;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.model.BallsDelightfulPanModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BiosWrathWeaponModClientEventSubscriber {

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) { //copied from the farmersdelight event subscriber that does the same thing
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();

        ModelResourceLocation ballsPanLocation = new ModelResourceLocation(new ResourceLocation(BiosWrathWeaponsMod.MODID, "balls_delightful_pan"), "inventory");
        BakedModel ballsPanModel = modelRegistry.get(ballsPanLocation);
        BakedModel ballsPanCookingModel = modelRegistry.get(new ModelResourceLocation(new ResourceLocation(BiosWrathWeaponsMod.MODID, "balls_delightful_pan_cooking"), "inventory"));
        modelRegistry.put(ballsPanLocation, new BallsDelightfulPanModel(event.getModelBakery(), ballsPanModel, ballsPanCookingModel));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(new ResourceLocation(BiosWrathWeaponsMod.MODID, "balls_delightful_pan_cooking"), "inventory"));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {

    }
}

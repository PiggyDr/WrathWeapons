package net.mcreator.bioswrathweapons.client.event;

import net.mcreator.bioswrathweapons.BiosWrathWeaponsMod;
import net.mcreator.bioswrathweapons.client.Keybinds;
import net.mcreator.bioswrathweapons.client.model.BallsDelightfulPanModel;
import net.mcreator.bioswrathweapons.client.model.SirensSpearModel;
import net.mcreator.bioswrathweapons.client.model.ballsdelightfulpanthrown;
import net.mcreator.bioswrathweapons.client.renderer.BallsDelightfulPanRenderer;
import net.mcreator.bioswrathweapons.client.renderer.SirensSpearRenderer;
import net.mcreator.bioswrathweapons.client.renderer.ThrownBallsDelightfulPanRenderer;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModBlockEntityTypes;
import net.mcreator.bioswrathweapons.init.BiosWrathWeaponsModEntities;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

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

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BiosWrathWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
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
        event.registerBlockEntityRenderer(BiosWrathWeaponsModBlockEntityTypes.BALLS_DELIGHTFUL_PAN.get(), BallsDelightfulPanRenderer::new);

        event.registerEntityRenderer(BiosWrathWeaponsModEntities.THROWN_BDPAN.get(), ThrownBallsDelightfulPanRenderer::new);
        event.registerEntityRenderer(BiosWrathWeaponsModEntities.ENDER_KATANA_PROJECTILE.get(), NoopRenderer::new);
        event.registerEntityRenderer(BiosWrathWeaponsModEntities.SIRENS_SPEAR.get(), SirensSpearRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ballsdelightfulpanthrown.LAYER_LOCATION, ballsdelightfulpanthrown::createBodyLayer);
        event.registerLayerDefinition(SirensSpearModel.LAYER_LOCATION, SirensSpearModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.INSTANCE.essenceAbility);
    }
}

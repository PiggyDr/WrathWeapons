tl;dr bio told me to make reapers stride just the meat shredder from cataclysm with some of the stats tweaked so i did that and had to copypaste one of the methods. i stole balls delightful pan mechanics independently since this is clearly an fd thing for lore reasons so it'd be weird not to imo. if any involved devs want me to  take this down or anything i will

this file documents all the stuff i stole and why

Ball's Delightful Pan is set up to mimic the Skillet from [Farmer's Delight](https://legacy.curseforge.com/minecraft/mc-mods/farmers-delight), which is a required dependency. Farmer's Delight is licensed under the MIT License:

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

if anyone particularly cares here's a list of all the skillet code i copied to make balls pan work

bioswrathweapons:textures/block/delightful_pan_tray_top.png is directly copied from farmersdelight:textures/block/cooking_pot_tray_top.png
net.mcreator.bioswrathweapons.client.event.ClientModEventSubscriber#onModelBake is copied from vectorwing.farmersdelight.client.event.ClientSetupEvents#onModelBake with superficial changes
net.mcreator.bioswrathweapons.client.event.ClientModEventSubscriber#onModelRegister is directly copied from vectorwing.farmersdelight.client.event.ClientSetupEvents#onModelRegister. idk if this should even really be put on this list since its a fairly standard ModelEvent.RegisterAdditional subscriber
net.mcreator.bioswrathweapons.client.model.BallsDelightfulPanModel is almost entirely copied from vectorwing.farmersdelight.client.model.SkilletModel with changes made to the item's transformation on the model and the model id
the properties of net.mcreator.bioswrathweapons.block.BallsDelightfulPanBlock are copied from the skillet's properties in wherever fd registers its blocks
obviously net.mcreator.bioswrathweapons.item.BallsDelightfulPanItem#isPlayerNearHeatSource is copied from vectorwing.farmersdelight.common.item.SkilletItem#isPlayerNearHeatSource
net.mcreator.bioswrathweapons.item.BallsDelightfulPanItem$BDPEvents and its method is copied from vectorwing.farmersdelight.common.item.SkilletItem#SkilletEvents
net.mcreator.bioswrathweapons.block.entity.BallsDelightfulPanBlockEntity is copied from whatever block entity class the skillet uses
either the entirety of net.mcreator.bioswrathweapons.block.BallsDelightfulPanBlock or a lot of its methods were copied from vectorwing.farmersdelight.common.block.SkilletBlock (probably the latter)

Reaper's Stride is set up to mimic the Meat Stealer from [L Ender's Cataclysm](https://legacy.curseforge.com/minecraft/mc-mods/lendercataclysm), which is a required dependency. L Ender's Cataclysm is licensed under the LGPL 3.0, which can be viewed [here](https://www.gnu.org/licenses/lgpl-3.0.en.html#license-text).

net.mcreator.bioswrathweapons.proxy.ClientProxy#playTickableSound, net.mcreator.bioswrathweapons.proxy.ClientProxy#stopPlayingTickableSound, and generally my whole tickable sound implementation is a stripped down version of Cataclysm's implementation (com.github.L_Ender.cataclysm.ClientProxy#playWorldSound, com.github.L_Ender.cataclysm.ClientProxy#clearSoundCacheFor) altho it should be noted that my playTickableSound is very stripped down compared to L_Ender's playWorldSound
net.mcreator.bioswrathweapons.item.ReapersStrideItem#onUseTick is almost entirely copied from com.github.L_Ender.cataclysm.items.Meat_Shredder#onUseTick

the Sculk Cleaver's sweep ability is heavily based on [tetra](https://legacy.curseforge.com/minecraft/mc-mods/tetra)'s implementation of the same concept

package net.mcreator.bioswrathweapons;

import net.mcreator.bioswrathweapons.init.*;
import net.mcreator.bioswrathweapons.item.EnderEssenceItem;
import net.mcreator.bioswrathweapons.proxy.ClientProxy;
import net.mcreator.bioswrathweapons.proxy.CommonProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(BiosWrathWeaponsMod.MODID)
public class BiosWrathWeaponsMod {
	public static final Logger LOGGER = LogManager.getLogger(BiosWrathWeaponsMod.class);
	public static final String MODID = "bios_wrath_weapons";
	public static CommonProxy PROXY = DistExecutor.unsafeRunForDist(
			() -> ClientProxy::new,
			() -> CommonProxy::new
	);

	public BiosWrathWeaponsMod() {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		BiosWrathWeaponsModItems.REGISTRY.register(bus);

		BiosWrathWeaponsModTabs.REGISTRY.register(bus);

		// Start of user code block mod init
		BiosWrathWeaponsModBlocks.REGISTRY.register(bus);
		BiosWrathWeaponsModBlockEntityTypes.REGISTRY.register(bus);
		BiosWrathWeaponsModMobEffects.REGISTRY.register(bus);
		BiosWrathWeaponsModEntities.REGISTRY.register(bus);
		BiosWrathWeaponsModSounds.REGISTRY.register(bus);
		// End of user code block mod init
	}

	// Start of user code block mod methods
	private void setup(FMLCommonSetupEvent event) {
		CuriosApi.registerCurio(BiosWrathWeaponsModItems.ENDER_ESSENCE.get(), new EnderEssenceItem());
	}
	// End of user code block mod methods
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		}
	}
}

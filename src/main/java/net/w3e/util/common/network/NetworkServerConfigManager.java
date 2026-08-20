package net.w3e.util.common.network;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.packs.resources.CloseableResourceManager;

import java.util.*;
import java.util.function.Consumer;

public class NetworkServerConfigManager {

	private static final List<Object> MANAGERS = new ArrayList<>();

	public static void register(Object object) {
		MANAGERS.add(Objects.requireNonNull(object));
	}

	public static void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(NetworkServerConfigManager::load);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(NetworkServerConfigManager::reload);
		ServerLifecycleEvents.AFTER_SAVE.register(NetworkServerConfigManager::save);

		ServerPlayConnectionEvents.JOIN.register(NetworkServerConfigManager::join);
		ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register(NetworkServerConfigManager::changeWorld);
	}

	@SuppressWarnings("unchecked")
	private static <T> void run(Class<T> type, Consumer<T> action) {
		for (Object object : MANAGERS) {
			if (type.isAssignableFrom(object.getClass())) {
				action.accept((T) object);
			}
		}
	}

	private static void load(MinecraftServer server) {
		run(LoadConfig.class, (e) -> e.onLoad(server));
	}

	private static void reload(MinecraftServer server, CloseableResourceManager resourceManager, boolean success) {
		save(server, false, false);
		load(server);

		run(CreateConfigPacket.class, (object) -> {
			var config = object.createConfigPacket();
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				ServerPlayNetworking.send(player, config);
			}
		});
		run(CreateWorldPacket.class, (object) -> {
			Map<ServerLevel, CustomPacketPayload> map = new IdentityHashMap<>();
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				CustomPacketPayload packet;
				try {
					packet = map.computeIfAbsent(player.level(), object::createWorldPacket);
				} catch (Exception e) {
					e.printStackTrace(System.err);
					continue;
				}
				ServerPlayNetworking.send(player, packet);
			}
		});
	}

	private static void save(MinecraftServer server, boolean flush, boolean force) {
		run(SaveConfig.class, (e) -> e.onSave(server));
	}

	private static void join(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
		var player = listener.getPlayer();
		run(CreateConfigPacket.class, (object) -> ServerPlayNetworking.send(player, object.createConfigPacket()));
		changeWorld(player, null, player.level());
	}

	private static void changeWorld(ServerPlayer player, @SuppressWarnings("unused") ServerLevel origin, ServerLevel destination) {
		run(CreateWorldPacket.class, (object) -> ServerPlayNetworking.send(player, object.createWorldPacket(destination)));
	}

	public interface LoadConfig {
		void onLoad(MinecraftServer server);
	}

	public interface SaveConfig {
		void onSave(MinecraftServer server);
	}

	public interface CreateConfigPacket {
		CustomPacketPayload createConfigPacket();

		default void sendToAllCreateConfig(MinecraftServer server) {
			run(CreateConfigPacket.class, (object) -> {
				var config = object.createConfigPacket();
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					ServerPlayNetworking.send(player, config);
				}
			});
		}
	}

	public interface CreateWorldPacket {
		CustomPacketPayload createWorldPacket(ServerLevel destination);
	}

	public interface LoadSaveConfig extends LoadConfig, SaveConfig {
	}

	public interface SyncPacket extends CreateConfigPacket, CreateWorldPacket {
	}

	public interface AllInOne extends LoadSaveConfig, SyncPacket {
	}

}

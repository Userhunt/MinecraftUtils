package net.w3e.util.client.texture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WDynamicTextureManager implements ResourceManagerReloadListener {

	public static final WDynamicTextureManager INSTANCE = new WDynamicTextureManager();

	static {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(
				Identifier.fromNamespaceAndPath("w3e", "dyanamic_texture_manager"), INSTANCE
		);
	}

	private final Map<Identifier, WDynamicTextureHolder> textureMap = new ConcurrentHashMap<>();

	public WDynamicTextureHolder register(WDynamicTexture dynamicTexture) {
		var holder = this.textureMap.computeIfAbsent(dynamicTexture.getTextureId(), WDynamicTextureHolder::new);
		holder.texture = dynamicTexture;
		Minecraft.getInstance().getTextureManager().register(dynamicTexture.getTextureId(), dynamicTexture);
		return holder;
	}

	public void release(WDynamicTexture texture) {
		var textureManager = Minecraft.getInstance().getTextureManager();
		textureManager.release(texture.getTextureId());
		var holder = this.textureMap.remove(texture.getTextureId());
		if (holder != null) {
			holder.texture = null;
		}
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		var iterator = this.textureMap.entrySet().iterator();
		//noinspection WhileLoopReplaceableByForEach
		while (iterator.hasNext()) {
			var next = iterator.next().getValue();
			/*if (next.isExpired()) {
				iterator.remove();
				continue;
			}*/
			next.texture = null;
		}
	}

	@RequiredArgsConstructor
	public static class WDynamicTextureHolder {

		@Getter
		private final Identifier textureId;
		@Getter
		private WDynamicTexture texture;

		private boolean isExpired() {
			if (this.texture == null) {
				return true;
			}
			return System.currentTimeMillis() > this.texture.getExpireTime();
		}

		public WDynamicTextureBuilder createBuilder() {
			return new WDynamicTextureBuilder(this.textureId);
		}

	}
}

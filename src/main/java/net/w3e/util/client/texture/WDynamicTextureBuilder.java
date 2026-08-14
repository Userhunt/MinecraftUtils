package net.w3e.util.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.w3e.util.client.minimap.MapTextureSegment;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class WDynamicTextureBuilder {

	private final Identifier textureId;

	private int maxIdleTime = 1000 * 60 * 15;

	private NativeImage nativeImage;

	public WDynamicTextureBuilder setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
		return this;
	}

	public WDynamicTextureBuilder createEmpty(int width, int height) {
		this.nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, true);
		return this;
	}

	public WDynamicTextureBuilder copyFromResourcesDirectly(Identifier vanillaId) {
		var client = Minecraft.getInstance();

		Identifier fullResourcePath = vanillaId.withPrefix("textures/").withSuffix(".png");

		Optional<Resource> resourceOptional = client.getResourceManager().getResource(fullResourcePath);

		if (resourceOptional.isPresent()) {
			try (InputStream stream = resourceOptional.get().open()) {
				NativeImage originalBytes = NativeImage.read(stream);

				NativeImage nativeImage = new NativeImage(
						originalBytes.format(),
						originalBytes.getWidth(),
						originalBytes.getHeight(),
						false
				);
				nativeImage.copyFrom(originalBytes);
				originalBytes.close();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		return this;
	}

	public WDynamicTextureBuilder modify(Consumer<NativeImage> modify) {
		modify.accept(this.nativeImage);
		return this;
	}

	public WDynamicTextureBuilder resizeDynamicTexture(int newWidth, int newHeight) {
		NativeImage oldPixels = this.nativeImage;

		this.nativeImage = new NativeImage(oldPixels.format(), newWidth, newHeight, false);

		oldPixels.copyRect(this.nativeImage, 0, 0, 0, 0, oldPixels.getWidth(), oldPixels.getHeight(), false, false);

		return this;
	}

	public WDynamicTextureBuilder fromMapSegment(MapTextureSegment segment) {
		var size = segment.getTextureSize();
		createEmpty(size, size);
		segment.applyToTexture(this.nativeImage);
		return this;
	}

	public WDynamicTexture build() {
		WDynamicTexture dynamicTexture = new WDynamicTexture(this.textureId, this.maxIdleTime, this.nativeImage);
		WDynamicTextureManager.INSTANCE.register(dynamicTexture);
		return dynamicTexture;
	}

}

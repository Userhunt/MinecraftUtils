package net.w3e.util.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import lombok.Getter;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

public class WDynamicTexture extends DynamicTexture {

	@Getter
	private final Identifier textureId;
	private final int maxIdleTime;
	private long lastUseTime;

	public WDynamicTexture(Identifier textureId, int maxIdleTime, NativeImage image) {
		super(() -> "WDynamicTexture@" + textureId, image);
		this.textureId = textureId;
		this.maxIdleTime = maxIdleTime;
		this.lastUseTime = System.currentTimeMillis();
	}

	@Override
	public GpuTexture getTexture() {
		this.lastUseTime = System.currentTimeMillis();
		return super.getTexture();
	}

	@Override
	public GpuTextureView getTextureView() {
		this.lastUseTime = System.currentTimeMillis();
		return super.getTextureView();
	}

	public long getExpireTime() {
		return this.lastUseTime + this.maxIdleTime;
	}

	public void editPixel(Consumer<NativeImage> edit) {
		NativeImage nativeImage = this.getPixels();
		edit.accept(nativeImage);
		this.upload();
	}

	public void release() {
		WDynamicTextureManager.INSTANCE.release(this);
	}

	public WDynamicTexture resizeDynamicTexture(int newWidth, int newHeight) {
		NativeImage oldPixels = this.getPixels();

		NativeImage newPixels = new NativeImage(oldPixels.format(), newWidth, newHeight, false);
		oldPixels.copyRect(newPixels, 0, 0, 0, 0, oldPixels.getWidth(), oldPixels.getHeight(), false, false);

		WDynamicTexture newTexture = new WDynamicTexture(this.textureId, this.maxIdleTime, newPixels);

		WDynamicTextureManager.INSTANCE.register(newTexture);

		return newTexture;
	}

	public static WDynamicTextureBuilder createBuilder(Identifier textureId) {
		return new WDynamicTextureBuilder(textureId);
	}

}

package net.w3e.util.common.io.codec;

import com.mojang.serialization.Codec;
import net.skds.lib2.io.codec.*;
import net.skds.lib2.io.json.elements.JsonElement;
import net.w3e.util.common.io.SosisonJsonOps;

import java.io.IOException;
import java.lang.reflect.Type;

public class MinecraftCodecWrapper<T> extends AbstractCodec<T> {

	private final UniversalCodec<JsonElement> jsonCodec = this.registry.getCodecIndirect(JsonElement.class);
	private final Codec<T> minecraftCodec;

	public MinecraftCodecWrapper(Type type, Codec<T> codec, CodecRegistry registry) {
		super(type, registry);
		this.minecraftCodec = codec;
	}

	@Override
	public final void write(T value, UniversalWriter writer) throws IOException {
		this.jsonCodec.write(this.minecraftCodec.encodeStart(SosisonJsonOps.INSTANCE, value).getOrThrow(), writer);
	}

	@Override
	public final T read(UniversalReader reader) throws IOException {
		return this.minecraftCodec.parse(SosisonJsonOps.INSTANCE, this.jsonCodec.read(reader)).getOrThrow();
	}

}

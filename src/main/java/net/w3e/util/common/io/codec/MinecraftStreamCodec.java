package net.w3e.util.common.io.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.skds.lib2.io.codec.SosisonUtils;
import net.skds.lib2.io.codec.UniversalCodec;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public record MinecraftStreamCodec<T>(UniversalCodec<T> codec) implements StreamCodec<ByteBuf, T> {

	public MinecraftStreamCodec(Type clazz) {
		this(SosisonUtils.getCompactRegistry().getCodecIndirect(clazz));
	}

	@Override
	public void encode(ByteBuf output, T value) {
		try {
			String jsonStr = this.codec.toJson(value);
			//System.out.println("write " + jsonStr);
			byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
			ByteBufCodecs.BYTE_ARRAY.encode(output, bytes);
		} catch (Exception e) {
			System.err.println(value);
			throw e;
		}
	}

	@Override
	public T decode(ByteBuf input) {
		byte[] bytes = ByteBufCodecs.BYTE_ARRAY.decode(input);
		String jsonStr = new String(bytes, StandardCharsets.UTF_8);
		try {
			//System.out.println("read " + jsonStr);
			return this.codec.parse(jsonStr);
		} catch (Exception e) {
			System.err.println(jsonStr);
			throw e;
		}
	}

}

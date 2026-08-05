package net.w3e.util.common.io.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.skds.lib2.io.codec.SosisonUtils;
import net.skds.lib2.io.codec.UniversalCodec;

import java.nio.charset.StandardCharsets;

public record MinecraftStreamCodec<T>(UniversalCodec<T> codec) implements StreamCodec<ByteBuf, T> {

	public MinecraftStreamCodec(Class<T> clazz) {
		this(SosisonUtils.getCompactRegistry().getCodecIndirect(clazz));
	}

	@Override
	public void encode(ByteBuf output, T value) {
		String jsonStr = this.codec.toJson(value);
		//System.out.println("write " + jsonStr);
		byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
		ByteBufCodecs.BYTE_ARRAY.encode(output, bytes);
	}

	@Override
	public T decode(ByteBuf input) {
		byte[] bytes = ByteBufCodecs.BYTE_ARRAY.decode(input);
		String jsonStr = new String(bytes, StandardCharsets.UTF_8);
		//System.out.println("read " + jsonStr);
		return this.codec.parse(jsonStr);
	}

}

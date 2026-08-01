package net.w3e.util.io;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.resources.Identifier;
import net.skds.lib2.io.codec.*;
import net.skds.lib2.io.json.elements.JsonArray;
import net.skds.lib2.io.json.elements.JsonElement;
import net.skds.lib2.io.json.elements.JsonObject;
import net.w3e.lib.io.CodecWrapper;

import java.io.IOException;
import java.lang.reflect.Type;

public class MinecraftCodecWrapper<T> extends AbstractCodec<T> {

	private static final CodecWrapper.Converter<?, ?> UNIMPLEMENTED_METHOD_WRITE = _ -> {
		throw new UnsupportedOperationException("Unimplemented method 'write'");
	};

	@SuppressWarnings("unchecked")
	public static <O, R> CodecWrapper.Converter<R, O> throwUnimplementedMethodWrite() {
		return (CodecWrapper.Converter<R, O>) UNIMPLEMENTED_METHOD_WRITE;
	}

	static {
		registerCodec(String.class, Identifier.class, Identifier::parse, Identifier::toString);
		registerCodec(String.class, EntitySelector.class, e -> EntityArgument.entities().parse(new StringReader(e)), throwUnimplementedMethodWrite());

		// item
		// block
		// command

		registerCodec(com.google.gson.JsonElement.class, JsonElement.class, MinecraftGsonUtil::toJson, MinecraftGsonUtil::fromJson);
		registerCodec(com.google.gson.JsonPrimitive.class, JsonElement.class, MinecraftGsonUtil::toJson, MinecraftGsonUtil::fromJson);
		registerCodec(com.google.gson.JsonNull.class, JsonElement.JsonNull.class, MinecraftGsonUtil::toJson, MinecraftGsonUtil::fromJson);
		registerCodec(com.google.gson.JsonObject.class, JsonObject.class, MinecraftGsonUtil::toJson, MinecraftGsonUtil::fromJson);
		registerCodec(com.google.gson.JsonArray.class, JsonArray.class, MinecraftGsonUtil::toJson, MinecraftGsonUtil::fromJson);
	}

	public static <O, R> void registerCodec(Class<O> originalClass, Class<R> replacedClass, CodecWrapper.Converter<O, R> originalConverter, CodecWrapper.Converter<R, O> replacedConverter) {
		CodecWrapper.registerToSosison(originalClass, replacedClass, originalConverter, replacedConverter);
	}

	public static void registerCodec(CodecWrapper<?, ?> converter) {
		CodecWrapper.registerToSosison(converter);
	}

	public static <T> void registerCodec(Class<T> cl, Codec<T> codec) {
		SosisonUtils.addFactory(cl, (type, registry) -> new MinecraftCodecWrapper<>(type, codec, registry));
	}

	public static void onInitialize() {

	}

	private final UniversalCodec<net.skds.lib2.io.json.elements.JsonElement> jsonCodec = this.registry.getCodecIndirect(net.skds.lib2.io.json.elements.JsonElement.class);
	private final Codec<T> minecraftCodec;

	private MinecraftCodecWrapper(Type type, Codec<T> codec, CodecRegistry registry) {
		super(type, registry);
		this.minecraftCodec = codec;
	}

	@Override
	public final void write(T value, UniversalWriter writer) throws IOException {
		this.jsonCodec.write(this.minecraftCodec.encodeStart(SKDSJsonOps.INSTANCE, value).getOrThrow(), writer);
	}

	@Override
	public final T read(UniversalReader reader) throws IOException {
		return this.minecraftCodec.parse(SKDSJsonOps.INSTANCE, this.jsonCodec.read(reader)).getOrThrow();
	}

}

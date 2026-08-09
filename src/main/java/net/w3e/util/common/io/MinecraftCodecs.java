package net.w3e.util.common.io;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.skds.lib2.io.codec.SosisonUtils;
import net.skds.lib2.io.json.elements.JsonArray;
import net.skds.lib2.io.json.elements.JsonElement;
import net.skds.lib2.io.json.elements.JsonObject;
import net.w3e.lib.ObjectWrapper;
import net.w3e.util.common.io.codec.MinecraftCodecWrapper;
import net.w3e.util.common.io.codec.MinecraftStreamCodec;

public class MinecraftCodecs {

	static {
		registerObjectConverterToSosison(String.class, Identifier.class, Identifier::parse, Identifier::toString);
		registerObjectConverterToSosison(String.class, EntitySelector.class, e -> EntityArgument.entities().parse(new StringReader(e)), ObjectWrapper.throwUnimplementedMethodWrite());

		// item
		// block
		// command

		registerObjectConverterToSosison(JsonElement.class, com.google.gson.JsonElement.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerObjectConverterToSosison(JsonElement.class, com.google.gson.JsonPrimitive.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerObjectConverterToSosison(JsonElement.JsonNull.class, com.google.gson.JsonNull.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerObjectConverterToSosison(JsonObject.class, com.google.gson.JsonObject.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerObjectConverterToSosison(JsonArray.class, com.google.gson.JsonArray.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
	}

	public static <T> Codec<T> createMinecraftCodec(Class<T> clazz) {
		return Codec.PASSTHROUGH.flatXmap(
				dynamic -> {
					JsonElement jsonElement = dynamic.convert(SosisonJsonOps.INSTANCE).getValue();
					//System.out.println("read " + jsonElement);
					return DataResult.success(SosisonUtils.parseJson(jsonElement, clazz));
				},
				value -> {
					String v = SosisonUtils.getCompactRegistry().getSerializer(clazz).toJson(value);
					//System.out.println("write " + v);
					JsonElement json = SosisonUtils.parseJson(v, JsonElement.class);
					//System.out.println("write " + json);
					return DataResult.success(new Dynamic<>(SosisonJsonOps.INSTANCE, json));
				}
		);
	}

	public static <O, R> void registerObjectConverterToSosison(Class<O> originalClass, Class<R> replacedClass, ObjectWrapper.Converter<O, R> originalConverter, ObjectWrapper.Converter<R, O> replacedConverter) {
		ObjectWrapper.registerToSosison(originalClass, replacedClass, originalConverter, replacedConverter);
	}

	public static void registerObjectConverterToSosison(ObjectWrapper<?, ?> converter) {
		ObjectWrapper.registerToSosison(converter);
	}

	public static <T> void registerMinecraftCodecToSosison(Class<T> cl, Codec<T> codec) {
		SosisonUtils.addFactory(cl, (type, registry) -> new MinecraftCodecWrapper<>(type, codec, registry));
	}

	public static <T> StreamCodec<ByteBuf, T> createStreamCodec(Class<T> clazz) {
		return new MinecraftStreamCodec<>(clazz);
	}

	public static void onInitialize() {

	}

}

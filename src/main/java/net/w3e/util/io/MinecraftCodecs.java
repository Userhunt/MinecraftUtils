package net.w3e.util.io;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
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
import net.w3e.util.io.codec.MinecraftCodecWrapper;
import net.w3e.util.io.codec.MinecraftStreamCodec;

public class MinecraftCodecs {

	static {
		registerCodec(String.class, Identifier.class, Identifier::parse, Identifier::toString);
		registerCodec(String.class, EntitySelector.class, e -> EntityArgument.entities().parse(new StringReader(e)), ObjectWrapper.throwUnimplementedMethodWrite());

		// item
		// block
		// command

		registerCodec(JsonElement.class, com.google.gson.JsonElement.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerCodec(JsonElement.class, com.google.gson.JsonPrimitive.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerCodec(JsonElement.JsonNull.class, com.google.gson.JsonNull.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerCodec(JsonObject.class, com.google.gson.JsonObject.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
		registerCodec(JsonArray.class, com.google.gson.JsonArray.class, MinecraftGsonUtil::fromJson, MinecraftGsonUtil::toJson);
	}

	public static <O, R> void registerCodec(Class<O> originalClass, Class<R> replacedClass, ObjectWrapper.Converter<O, R> originalConverter, ObjectWrapper.Converter<R, O> replacedConverter) {
		ObjectWrapper.registerToSosison(originalClass, replacedClass, originalConverter, replacedConverter);
	}

	public static void registerCodec(ObjectWrapper<?, ?> converter) {
		ObjectWrapper.registerToSosison(converter);
	}

	public static <T> void registerMinecraftCodec(Class<T> cl, Codec<T> codec) {
		SosisonUtils.addFactory(cl, (type, registry) -> new MinecraftCodecWrapper<>(type, codec, registry));
	}

	public static <T> StreamCodec<ByteBuf, T> createStreamCodec(Class<T> clazz) {
		return new MinecraftStreamCodec<>(clazz);
	}

	public static void onInitialize() {

	}

}

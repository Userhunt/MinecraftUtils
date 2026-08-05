package net.w3e.util.common.io;

import java.util.TreeMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;

import net.skds.lib2.io.json.elements.JsonArray;
import net.skds.lib2.io.json.elements.JsonBoolean;
import net.skds.lib2.io.json.elements.JsonElement;
import net.skds.lib2.io.json.elements.JsonNumber;
import net.skds.lib2.io.json.elements.JsonObject;
import net.skds.lib2.io.json.elements.JsonString;

public class MinecraftGsonUtil {

	public static Gson GSON = new Gson();

	@SuppressWarnings("unchecked")
	public static <S extends JsonElement, G extends com.google.gson.JsonElement> S toJson(G element) {
		if (element instanceof com.google.gson.JsonObject json) {
			JsonObject result = new JsonObject();
			for (Entry<String, com.google.gson.JsonElement> entry : json.entrySet()) {
				result.put(entry.getKey(), (JsonElement) toJson(entry.getValue()));
			}
			return (S) result;
		}
		if (element instanceof com.google.gson.JsonArray json) {
			JsonArray result = new JsonArray();
			for (com.google.gson.JsonElement entry : json) {
				result.add((JsonElement) toJson(entry));
			}
			return (S) result;
		}
		if (element instanceof com.google.gson.JsonNull || element == null) {
			return (S) JsonElement.NULL;
		}
		if (element instanceof JsonPrimitive primitive) {
			if (primitive.isString()) {
				return (S) new JsonString(primitive.getAsString());
			}
			if (primitive.isBoolean()) {
				return (S) JsonBoolean.valueOf(primitive.getAsBoolean());
			}
			if (primitive.isNumber()) {
				return (S) new JsonNumber(primitive.getAsNumber());
			}
		}

		throw new IllegalStateException(element.getClass() + " is unknown type");
	}

	@SuppressWarnings("unchecked")
	public static <S extends JsonElement, G extends com.google.gson.JsonElement> G fromJson(S element) {
		if (element instanceof JsonObject json) {
			com.google.gson.JsonObject result = new com.google.gson.JsonObject();
			TreeMap<String, com.google.gson.JsonElement> map = new TreeMap<>();
			for (Entry<String, JsonElement> entry : json.entrySet()) {
				map.put(entry.getKey(), fromJson(entry.getValue()));
			}
			map.forEach(result::add);
			return (G) result;
		}
		if (element instanceof JsonArray json) {
			com.google.gson.JsonArray result = new com.google.gson.JsonArray();
			for (JsonElement entry : json) {
				result.add(fromJson(entry));
			}
			return (G) result;
		}
		if (element == null || element == JsonElement.NULL) {
			return (G) com.google.gson.JsonNull.INSTANCE;
		}
		if (element instanceof JsonString) {
			return (G) new JsonPrimitive(element.getAsString());
		}
		if (element instanceof JsonBoolean) {
			return (G) new JsonPrimitive(element.getAsBoolean());
		}
		if (element instanceof JsonNumber) {
			return (G) new JsonPrimitive(element.getAsNumber());
		}

		throw new IllegalStateException(element.getClass() + " is unknown type");
	}

}

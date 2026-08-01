package net.w3e.util.io;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import net.skds.lib2.io.codec.SosisonUtils;
import net.skds.lib2.io.json.elements.JsonArray;
import net.skds.lib2.io.json.elements.JsonBoolean;
import net.skds.lib2.io.json.elements.JsonElement;
import net.skds.lib2.io.json.elements.JsonNumber;
import net.skds.lib2.io.json.elements.JsonObject;
import net.skds.lib2.io.json.elements.JsonString;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

public record SKDSJsonOps(boolean compressed) implements DynamicOps<JsonElement> {

	public static final Codec<JsonElement> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				JsonElement json = dynamic.convert(SKDSJsonOps.INSTANCE).getValue();
				return DataResult.success(json == dynamic.getValue() ? json.deepCopy() : json);
			},
			json -> new Dynamic<>(SKDSJsonOps.INSTANCE, json.deepCopy())
	);

	public static <T> Codec<T> createCodec(Class<T> clazz) {
		return CODEC.xmap(
				data -> SosisonUtils.parseJson(data, clazz),
				marker -> SosisonUtils.parseJson(SosisonUtils.toJson(marker), JsonElement.class)
		);
	}

	public static final SKDSJsonOps INSTANCE = new SKDSJsonOps(false);
	public static final SKDSJsonOps COMPRESSED = new SKDSJsonOps(true);

	@Override
	public JsonElement empty() {
		return JsonElement.NULL;
	}

	@Override
	public <U> U convertTo(final DynamicOps<U> outOps, final JsonElement input) {
		if (input instanceof JsonObject) {
			return convertMap(outOps, input);
		}
		if (input instanceof JsonArray) {
			return convertList(outOps, input);
		}
		if (input == JsonElement.NULL) {
			return outOps.empty();
		}
		if (input instanceof JsonString) {
			return outOps.createString(input.getAsString());
		}
		if (input instanceof JsonBoolean) {
			return outOps.createBoolean(input.getAsBoolean());
		}
		if (input instanceof JsonNumber jsonNumber) {
			Number value = jsonNumber.value().longValue();
			long l = value.longValue();
			if (value.byteValue() == l) {
				return outOps.createByte((byte) l);
			}
			if (value.shortValue() == l) {
				return outOps.createShort((short) l);
			}
			if (value.intValue() == l) {
				return outOps.createInt((int) l);
			}
			if (value instanceof Long) {
				return outOps.createLong((int) l);
			}
			final double d = value.doubleValue();
			if ((float) d == d) {
				return outOps.createFloat((float) d);
			}
			return outOps.createDouble(d);
		}
		return outOps.empty();
	}

	@Override
	public DataResult<Number> getNumberValue(final JsonElement input) {
		if (input instanceof JsonNumber) {
			return DataResult.success(input.getAsNumber());
		}
		if (compressed && input instanceof JsonString) {
			try {
				return DataResult.success(Integer.parseInt(input.getAsString()));
			} catch (final NumberFormatException e) {
				return DataResult.error(() -> "Not a number: " + e + " " + input);
			}
		}
		return DataResult.error(() -> "Not a number: " + input);
	}

	@Override
	public JsonElement createNumeric(final Number i) {
		return new JsonNumber(i);
	}

	@Override
	public DataResult<Boolean> getBooleanValue(final JsonElement input) {
		if (input instanceof JsonBoolean) {
			return DataResult.success(input.getAsBoolean());
		}
		return DataResult.error(() -> "Not a boolean: " + input);
	}

	@Override
	public JsonElement createBoolean(final boolean value) {
		return new JsonBoolean(value);
	}

	@Override
	public DataResult<String> getStringValue(final JsonElement input) {
		if (input instanceof JsonString) {
			return DataResult.success(input.getAsString());
		}
		if (input instanceof JsonNumber jsonNumber && compressed) {
			return DataResult.success(String.valueOf(jsonNumber.getAsNumber()));
		}
		return DataResult.error(() -> "Not a string: " + input);
	}

	@Override
	public JsonElement createString(final String value) {
		return new JsonString(value);
	}

	@Override
	public DataResult<JsonElement> mergeToList(final JsonElement list, final JsonElement value) {
		if (!(list instanceof JsonArray) && list != empty()) {
			return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
		}

		final JsonArray result = new JsonArray();
		if (list != empty()) {
			result.addAll(list.getAsJsonArray());
		}
		result.add(value);
		return DataResult.success(result);
	}

	@Override
	public DataResult<JsonElement> mergeToList(final JsonElement list, final List<JsonElement> values) {
		if (!(list instanceof JsonArray) && list != empty()) {
			return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
		}

		final JsonArray result = new JsonArray();
		if (list != empty()) {
			result.addAll(list.getAsJsonArray());
		}
		result.addAll(values);
		return DataResult.success(result);
	}

	@Override
	public DataResult<JsonElement> mergeToMap(final JsonElement map, final JsonElement key, final JsonElement value) {
		if (!(map instanceof JsonObject) && map != empty()) {
			return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
		}
		if (!(key instanceof JsonString) || !compressed) {
			return DataResult.error(() -> "key is not a string: " + key, map);
		}

		final JsonObject output = new JsonObject();
		if (map != empty()) {
			output.putAll(map.getAsJsonObject());
		}
		output.put(key.getAsString(), value);

		return DataResult.success(output);
	}

	@Override
	public DataResult<JsonElement> mergeToMap(final JsonElement map, final MapLike<JsonElement> values) {
		if (!(map instanceof JsonObject) && map != empty()) {
			return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
		}

		final JsonObject output = new JsonObject();
		if (map != empty()) {
			output.putAll(map.getAsJsonObject());
		}

		final List<JsonElement> missed = Lists.newArrayList();

		values.entries().forEach(entry -> {
			final JsonElement key = entry.getFirst();
			if (!(key instanceof JsonString) || !compressed) {
				missed.add(key);
				return;
			}
			output.put(key.getAsString(), entry.getSecond());
		});

		if (!missed.isEmpty()) {
			return DataResult.error(() -> "some keys are not strings: " + missed, output);
		}

		return DataResult.success(output);
	}

	@Override
	public DataResult<Stream<Pair<JsonElement, JsonElement>>> getMapValues(final JsonElement input) {
		if (!(input instanceof JsonObject)) {
			return DataResult.error(() -> "Not a JSON object: " + input);
		}
		return DataResult.success(input.getAsJsonObject().entrySet().stream().map(entry -> Pair.of(new JsonString(entry.getKey()), entry.getValue())));
	}

	@Override
	public DataResult<Consumer<BiConsumer<JsonElement, JsonElement>>> getMapEntries(final JsonElement input) {
		if (!(input instanceof JsonObject)) {
			return DataResult.error(() -> "Not a JSON object: " + input);
		}
		return DataResult.success(c -> {
			for (final Map.Entry<String, JsonElement> entry : input.getAsJsonObject().entrySet()) {
				c.accept(createString(entry.getKey()), entry.getValue());
			}
		});
	}

	@Override
	public DataResult<MapLike<JsonElement>> getMap(final JsonElement input) {
		if (!(input instanceof JsonObject)) {
			return DataResult.error(() -> "Not a JSON object: " + input);
		}
		final JsonObject object = input.getAsJsonObject();
		return DataResult.success(new MapLike<>() {
			@Nullable
			@Override
			public JsonElement get(final JsonElement key) {
				return object.get(key.getAsString());
			}

			@Nullable
			@Override
			public JsonElement get(final String key) {
				return object.get(key);
			}

			@Override
			public Stream<Pair<JsonElement, JsonElement>> entries() {
				return object.entrySet().stream().map(e -> Pair.of(new JsonString(e.getKey()), e.getValue()));
			}

			@Override
			public String toString() {
				return "MapLike[" + object + "]";
			}
		});
	}

	@Override
	public JsonElement createMap(final Stream<Pair<JsonElement, JsonElement>> map) {
		final JsonObject result = new JsonObject();
		map.forEach(p -> result.put(p.getFirst().getAsString(), p.getSecond()));
		return result;
	}

	@Override
	public DataResult<Stream<JsonElement>> getStream(final JsonElement input) {
		if (input instanceof JsonArray) {
			return DataResult.success(input.getAsJsonArray().stream());
		}
		return DataResult.error(() -> "Not a json array: " + input);
	}

	@Override
	public DataResult<Consumer<Consumer<JsonElement>>> getList(final JsonElement input) {
		if (input instanceof JsonArray) {
			return DataResult.success(c -> {
				for (final JsonElement element : input.getAsJsonArray()) {
					c.accept(element);
				}
			});
		}
		return DataResult.error(() -> "Not a json array: " + input);
	}

	@Override
	public JsonElement createList(final Stream<JsonElement> input) {
		final JsonArray result = new JsonArray();
		input.forEach(result::add);
		return result;
	}

	@Override
	public JsonElement remove(final JsonElement input, final String key) {
		if (input instanceof JsonObject) {
			final JsonObject result = new JsonObject();
			input.getAsJsonObject().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), key)).forEach(entry -> result.put(entry.getKey(), entry.getValue()));
			return result;
		}
		return input;
	}

	@Override
	public String toString() {
		return "JSON";
	}

	@Override
	public ListBuilder<JsonElement> listBuilder() {
		return new ArrayBuilder();
	}

	private static final class ArrayBuilder implements ListBuilder<JsonElement> {
		private DataResult<JsonArray> builder = DataResult.success(new JsonArray(), Lifecycle.stable());

		@Override
		public DynamicOps<JsonElement> ops() {
			return INSTANCE;
		}

		@Override
		public ListBuilder<JsonElement> add(final JsonElement value) {
			builder = builder.map(b -> {
				b.add(value);
				return b;
			});
			return this;
		}

		@Override
		public ListBuilder<JsonElement> add(final DataResult<JsonElement> value) {
			builder = builder.apply2stable((b, element) -> {
				b.add(element);
				return b;
			}, value);
			return this;
		}

		@Override
		public ListBuilder<JsonElement> withErrorsFrom(final DataResult<?> result) {
			builder = builder.flatMap(r -> result.map(_ -> r));
			return this;
		}

		@Override
		public ListBuilder<JsonElement> mapError(final UnaryOperator<String> onError) {
			builder = builder.mapError(onError);
			return this;
		}

		@Override
		public DataResult<JsonElement> build(final JsonElement prefix) {
			final DataResult<JsonElement> result = builder.flatMap(b -> {
				if (!(prefix instanceof JsonArray) && prefix != ops().empty()) {
					return DataResult.error(() -> "Cannot append a list to not a list: " + prefix, prefix);
				}

				final JsonArray array = new JsonArray();
				if (prefix != ops().empty()) {
					array.addAll(prefix.getAsJsonArray());
				}
				array.addAll(b);
				return DataResult.success(array, Lifecycle.stable());
			});

			builder = DataResult.success(new JsonArray(), Lifecycle.stable());
			return result;
		}
	}

	@Override
	public boolean compressMaps() {
		return compressed;
	}

	@Override
	public RecordBuilder<JsonElement> mapBuilder() {
		return new JsonRecordBuilder();
	}

	private class JsonRecordBuilder extends RecordBuilder.AbstractStringBuilder<JsonElement, JsonObject> {
		protected JsonRecordBuilder() {
			super(SKDSJsonOps.this);
		}

		@Override
		protected JsonObject initBuilder() {
			return new JsonObject();
		}

		@Override
		protected JsonObject append(final String key, final JsonElement value, final JsonObject builder) {
			builder.put(key, value);
			return builder;
		}

		@Override
		protected DataResult<JsonElement> build(final JsonObject builder, final JsonElement prefix) {
			if (prefix == null || prefix == JsonElement.NULL) {
				return DataResult.success(builder);
			}
			if (prefix instanceof JsonObject) {
				final JsonObject result = new JsonObject();
				result.putAll(prefix.getAsJsonObject());
				result.putAll(builder);
				return DataResult.success(result);
			}
			return DataResult.error(() -> "mergeToMap called with not a map: " + prefix, prefix);
		}
	}
}

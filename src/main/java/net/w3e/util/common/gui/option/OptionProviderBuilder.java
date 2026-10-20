package net.w3e.util.common.gui.option;

import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.Component;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.vec2.Vec2;
import net.skds.lib2.mat.vec3.Direction;
import net.skds.lib2.mat.vec3.Vec3;
import net.skds.lib2.shapes.AABB;
import net.w3e.util.common.gui.option.container.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
@RequiredArgsConstructor
public class OptionProviderBuilder<OBJECT, VALUE_HOLDER, BUILDER extends OptionProviderBuilder<OBJECT, VALUE_HOLDER, BUILDER>> {

	public final Function<OBJECT, VALUE_HOLDER> converter;

	private final List<OptionProvider<?, OBJECT, ?, ?>> list = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public BUILDER child() {
		return (BUILDER) new OptionProviderBuilder<>(this.converter);
	}

	public <VH> OptionProviderBuilder<OBJECT, VH, ?> child(Function<OBJECT, VH> converter) {
		return new OptionProviderBuilder<>(converter);
	}

	@SuppressWarnings("unchecked")
	public BUILDER add(OptionProvider<?, OBJECT, ?, ?> option) {
		this.list.add(option);
		return (BUILDER) this;
	}

	public BUILDER addIntRange(Component title, int min, int max, Function<VALUE_HOLDER, Integer> getter, BiConsumer<VALUE_HOLDER, Integer> setter) {
		return this.add(createIntRange(title, min, max, getter, setter));
	}

	public BUILDER addIntRange(Component title, RangeOption<Integer, IntRangeOption> rangeOption, Function<VALUE_HOLDER, Integer> getter, BiConsumer<VALUE_HOLDER, Integer> setter) {
		return this.add(createIntRange(title, rangeOption, getter, setter));
	}

	public OptionProvider<RangeOption<Integer, IntRangeOption>, OBJECT, VALUE_HOLDER, Integer> createIntRange(Component title, int min, int max, Function<VALUE_HOLDER, Integer> getter, BiConsumer<VALUE_HOLDER, Integer> setter) {
		return this.createIntRange(title, RangeOption.ofRange(new IntRangeOption(min, max, 1)), getter, setter);
	}

	public OptionProvider<RangeOption<Integer, IntRangeOption>, OBJECT, VALUE_HOLDER, Integer> createIntRange(Component title, RangeOption<Integer, IntRangeOption> rangeOption, Function<VALUE_HOLDER, Integer> getter, BiConsumer<VALUE_HOLDER, Integer> setter) {
		return new OptionProvider<>(OptionProviderType.INT_RANGE, title, rangeOption, this.converter, getter, setter);
	}

	public BUILDER addFloatRange(Component title, float min, float max, float step, Function<VALUE_HOLDER, Float> getter, BiConsumer<VALUE_HOLDER, Float> setter) {
		return this.add(createFloatRange(title, min, max, step, getter, setter));
	}

	public BUILDER addFloatRange(Component title, RangeOption<Float, FloatRangeOption> rangeOption, Function<VALUE_HOLDER, Float> getter, BiConsumer<VALUE_HOLDER, Float> setter) {
		return this.add(createFloatRange(title, rangeOption, getter, setter));
	}

	public OptionProvider<RangeOption<Float, FloatRangeOption>, OBJECT, VALUE_HOLDER, Float> createFloatRange(Component title, float min, float max, float step, Function<VALUE_HOLDER, Float> getter, BiConsumer<VALUE_HOLDER, Float> setter) {
		return this.createFloatRange(title, RangeOption.ofRange(new FloatRangeOption(min, max, step)), getter, setter);
	}

	public OptionProvider<RangeOption<Float, FloatRangeOption>, OBJECT, VALUE_HOLDER, Float> createFloatRange(Component title, RangeOption<Float, FloatRangeOption> rangeOption, Function<VALUE_HOLDER, Float> getter, BiConsumer<VALUE_HOLDER, Float> setter) {
		return new OptionProvider<>(OptionProviderType.FLOAT_RANGE, title, rangeOption, this.converter, getter, setter);
	}

	public <E> BUILDER addValueRange(Component title, List<E> rangeOption, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.add(createValueRange(title, rangeOption, getter, setter));
	}

	public <E> BUILDER addValueRange(Component title, RangeOption<E, List<E>> values, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.add(createValueRange(title, values, getter, setter));
	}

	public <E> OptionProvider<RangeOption<E, List<E>>, OBJECT, VALUE_HOLDER, E> createValueRange(Component title, List<E> rangeOption, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.createValueRange(title, RangeOption.ofRange(rangeOption), getter, setter);
	}

	public <E> OptionProvider<RangeOption<E, List<E>>, OBJECT, VALUE_HOLDER, E> createValueRange(Component title, RangeOption<E, List<E>> values, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return new OptionProvider<>(OptionProviderType.getValueRange(), title, values, this.converter, getter, setter);
	}

	public <E extends Enum<E>> BUILDER addEnum(Component title, Class<E> cl, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.add(createEnum(title, List.of(cl.getEnumConstants()), getter, setter));
	}

	public <E> BUILDER addEnum(Component title, List<E> constants, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.add(createEnum(title, constants, getter, setter));
	}

	public <E extends Enum<E>> OptionProvider<RangeOption<E, List<E>>, OBJECT, VALUE_HOLDER, E> createEnum(Component title, Class<E> cl, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.createEnum(title, List.of(cl.getEnumConstants()), getter, setter);
	}

	public <E> OptionProvider<RangeOption<E, List<E>>, OBJECT, VALUE_HOLDER, E> createEnum(Component title, List<E> constants, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return new OptionProvider<>(OptionProviderType.getEnum(), title, RangeOption.ofCycle(constants), this.converter, getter, setter);
	}

	public BUILDER addString(Component title, Function<VALUE_HOLDER, String> getter, BiConsumer<VALUE_HOLDER, String> setter) {
		return this.add(createString(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, String> createString(Component title, Function<VALUE_HOLDER, String> getter, BiConsumer<VALUE_HOLDER, String> setter) {
		return new OptionProvider<>(OptionProviderType.STRING, title, null, this.converter, getter, setter);
	}

	public BUILDER addBoolean(Component title, Function<VALUE_HOLDER, Boolean> getter, BiConsumer<VALUE_HOLDER, Boolean> setter) {
		return this.add(createBoolean(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, Boolean> createBoolean(Component title, Function<VALUE_HOLDER, Boolean> getter, BiConsumer<VALUE_HOLDER, Boolean> setter) {
		return new OptionProvider<>(OptionProviderType.BOOLEAN, title, null, this.converter, getter, setter);
	}

	public BUILDER addQuat(Component title, Function<VALUE_HOLDER, Quat> getter, BiConsumer<VALUE_HOLDER, Quat> setter) {
		return this.add(createQuat(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, Quat> createQuat(Component title, Function<VALUE_HOLDER, Quat> getter, BiConsumer<VALUE_HOLDER, Quat> setter) {
		return new OptionProvider<>(OptionProviderType.QUAT, title, null, this.converter, getter, setter);
	}

	public BUILDER addVec3(Component title, Function<VALUE_HOLDER, Vec3> getter, BiConsumer<VALUE_HOLDER, Vec3> setter) {
		return this.add(createVec3(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, Vec3> createVec3(Component title, Function<VALUE_HOLDER, Vec3> getter, BiConsumer<VALUE_HOLDER, Vec3> setter) {
		return new OptionProvider<>(OptionProviderType.VEC3, title, null, this.converter, getter, setter);
	}

	public BUILDER addVec2(Component title, Function<VALUE_HOLDER, Vec2> getter, BiConsumer<VALUE_HOLDER, Vec2> setter) {
		return this.add(createVec2(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, Vec2> createVec2(Component title, Function<VALUE_HOLDER, Vec2> getter, BiConsumer<VALUE_HOLDER, Vec2> setter) {
		return new OptionProvider<>(OptionProviderType.VEC2, title, null, this.converter, getter, setter);
	}

	public BUILDER addRotation(Component title, Function<VALUE_HOLDER, RotationData> getter, BiConsumer<VALUE_HOLDER, RotationData> setter) {
		return this.add(createRotation(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, RotationData> createRotation(Component title, Function<VALUE_HOLDER, RotationData> getter, BiConsumer<VALUE_HOLDER, RotationData> setter) {
		return new OptionProvider<>(OptionProviderType.ROTATION, title, null, this.converter, getter, setter);
	}

	public BUILDER addDirection(Component title, Function<VALUE_HOLDER, Direction> getter, BiConsumer<VALUE_HOLDER, Direction> setter) {
		return this.add(this.createDirection(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, Direction> createDirection(Component title, Function<VALUE_HOLDER, Direction> getter, BiConsumer<VALUE_HOLDER, Direction> setter) {
		return this.createEnum(title, Direction.class, getter, setter);
	}

	public BUILDER addAABB(Component title, Function<VALUE_HOLDER, AABB> getter, BiConsumer<VALUE_HOLDER, AABB> setter) {
		return this.add(createAABB(title, getter, setter));
	}

	public OptionProvider<?, OBJECT, VALUE_HOLDER, AABB> createAABB(Component title, Function<VALUE_HOLDER, AABB> getter, BiConsumer<VALUE_HOLDER, AABB> setter) {
		return new OptionProvider<>(OptionProviderType.AABB, title, null, this.converter, getter, setter);
	}

	public <E> BUILDER addFlags(Component title, List<E> flags, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.add(createFlags(title, RangeOption.ofFlags(flags), getter, setter));
	}

	public <E> BUILDER addFlags(Component title, RangeOption<E, List<E>> flags, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.add(createFlags(title, flags, getter, setter));
	}

	public <E> OptionProvider<RangeOption<E, List<E>>, OBJECT, VALUE_HOLDER, List<E>> createFlags(Component title, List<E> flags, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.createFlags(title, RangeOption.ofFlags(flags), getter, setter);
	}

	public <E> OptionProvider<RangeOption<E, List<E>>, OBJECT, VALUE_HOLDER, List<E>> createFlags(Component title, RangeOption<E, List<E>> flags, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return new OptionProvider<>(OptionProviderType.getFlags(), title, flags, this.converter, getter, setter);
	}

	public <E> BUILDER addCollection(Component title, ListData<E, OBJECT> listData, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.add(createCollection(title, listData, getter, setter));
	}

	public <E> OptionProvider<ListData<E, OBJECT>, OBJECT, VALUE_HOLDER, List<E>> createCollection(Component title, ListData<E, OBJECT> listData, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return new OptionProvider<>(OptionProviderType.getCollection(), title, listData, this.converter, getter, setter);
	}

	public <E extends Enum<E>, V> BUILDER addEnumPage(Component title, Class<E> cl, Function<VALUE_HOLDER, PageData.PageKey<E, V>> getter, BiConsumer<VALUE_HOLDER, PageData.PageKey<E, V>> setter, Function<BUILDER, Map<E, PageData.PageFactoryData<OBJECT, VALUE_HOLDER>>> factoryDataMap) {
		return this.add(createEnumPage(title, cl, getter, setter, factoryDataMap));
	}

	public <E, V> BUILDER addEnumPage(Component title, List<E> constants, Function<VALUE_HOLDER, PageData.PageKey<E, V>> getter, BiConsumer<VALUE_HOLDER, PageData.PageKey<E, V>> setter, Function<BUILDER, Map<E, PageData.PageFactoryData<OBJECT, VALUE_HOLDER>>> factoryDataMap) {
		return this.add(createEnumPage(title, constants, getter, setter, factoryDataMap));
	}

	public <E extends Enum<E>, V> OptionProvider<?, OBJECT, VALUE_HOLDER, PageData.PageKey<E, V>> createEnumPage(Component title, Class<E> cl, Function<VALUE_HOLDER, PageData.PageKey<E, V>> getter, BiConsumer<VALUE_HOLDER, PageData.PageKey<E, V>> setter, Function<BUILDER, Map<E, PageData.PageFactoryData<OBJECT, VALUE_HOLDER>>> factoryDataMap) {
		return this.createEnumPage(title, List.of(cl.getEnumConstants()), getter, setter, factoryDataMap);
	}

	public <E, V> OptionProvider<?, OBJECT, VALUE_HOLDER, PageData.PageKey<E, V>> createEnumPage(Component title, List<E> constants, Function<VALUE_HOLDER, PageData.PageKey<E, V>> getter, BiConsumer<VALUE_HOLDER, PageData.PageKey<E, V>> setter, Function<BUILDER, Map<E, PageData.PageFactoryData<OBJECT, VALUE_HOLDER>>> factoryDataMap) {
		return new OptionProvider<>(OptionProviderType.getEnumPage(), title, PageData.of(RangeOption.ofCycle(constants), factoryDataMap != null ? factoryDataMap.apply(this.child()) : null), this.converter, getter, setter);
	}

	// TODO typed

	@SuppressWarnings("unchecked")
	public BUILDER apply(Consumer<BUILDER> consumer) {
		consumer.accept((BUILDER) this);
		return (BUILDER) this;
	}

	@SuppressWarnings("unchecked")
	public <W> BUILDER addBuilder(OptionProviderBuilder<OBJECT, W, ?> other) {
		return addBuilder(other, e -> (W) e);
	}

	@SuppressWarnings("unchecked")
	public <W> BUILDER addBuilder(OptionProviderBuilder<OBJECT, W, ?> other, Function<VALUE_HOLDER, W> wrapper) {
		for (OptionProvider<?, OBJECT, ?, ?> OptionProvider : other.list) {
			addWrapped(wrapper, OptionProvider);
		}
		return (BUILDER) this;
	}

	@SuppressWarnings("unchecked")
	private <W, V> void addWrapped(Function<VALUE_HOLDER, W> wrapper, OptionProvider<?, ?, ?, ?> args) {
		OptionProvider<?, VALUE_HOLDER, W, V> provider = (OptionProvider<?, VALUE_HOLDER, W, V>) args;

		OptionProvider<?, OBJECT, W, V> option = new OptionProvider<>(
				provider.getType(), provider.getOrdinal(), provider.getTitle(), provider.getArgs(),
				e -> wrapper.apply(this.converter.apply(e)),
				provider.getGetter(), provider.getSetter(), provider.getEquals()
		);

		this.list.add(option);
	}

	public List<OptionProvider<?, OBJECT, ?, ?>> build() {
		return new ArrayList<>(this.list);
	}

}

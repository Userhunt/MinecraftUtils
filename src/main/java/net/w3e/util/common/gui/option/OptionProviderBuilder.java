package net.w3e.util.common.gui.option;

import net.minecraft.network.chat.Component;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.vec2.Vec2;
import net.skds.lib2.mat.vec3.Direction;
import net.skds.lib2.mat.vec3.Vec3;
import net.skds.lib2.shapes.AABB;
import net.w3e.util.common.gui.option.container.FloatRangeOption;
import net.w3e.util.common.gui.option.container.IntRangeOption;
import net.w3e.util.common.gui.option.container.ListData;
import net.w3e.util.common.gui.option.container.RotationData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class OptionProviderBuilder<OBJECT, VALUE_HOLDER, BUILDER extends OptionProviderBuilder<OBJECT, VALUE_HOLDER, BUILDER>> {

	public final Function<OBJECT, VALUE_HOLDER> converter;

	private final List<OptionProvider<?, OBJECT, ?, ?>> list = new ArrayList<>();

	public OptionProviderBuilder(Function<OBJECT, VALUE_HOLDER> converter) {
		this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public BUILDER add(OptionProvider<?, OBJECT, ?, ?> option) {
		this.list.add(option);
		return (BUILDER) this;
	}

	public BUILDER addIntRange(Component title, int min, int max, Function<VALUE_HOLDER, Integer> getter, BiConsumer<VALUE_HOLDER, Integer> setter) {
		return this.addIntRange(title, RangeOption.ofRange(new IntRangeOption(min, max, 1)), getter, setter);
	}

	public BUILDER addIntRange(Component title, RangeOption<Integer, IntRangeOption> rangeOption, Function<VALUE_HOLDER, Integer> getter, BiConsumer<VALUE_HOLDER, Integer> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.INT_RANGE, title, rangeOption, this.converter, getter, setter));
	}

	public BUILDER addFloatRange(Component title, float min, float max, float step, Function<VALUE_HOLDER, Float> getter, BiConsumer<VALUE_HOLDER, Float> setter) {
		return this.addFloatRange(title, RangeOption.ofRange(new FloatRangeOption(min, max, step)), getter, setter);
	}

	public BUILDER addFloatRange(Component title, RangeOption<Float, FloatRangeOption> rangeOption, Function<VALUE_HOLDER, Float> getter, BiConsumer<VALUE_HOLDER, Float> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.FLOAT_RANGE, title, rangeOption, this.converter, getter, setter));
	}

	public <E> BUILDER addValueRange(Component title, List<E> rangeOption, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.addValueRange(title, RangeOption.ofRange(rangeOption), getter, setter);
	}

	public <E> BUILDER addValueRange(Component title, RangeOption<E, List<E>> values, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.getValueRange(), title, values, this.converter, getter, setter));
	}

	public <E> BUILDER addEnum(Component title, List<E> constants, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.getEnum(), title, RangeOption.ofCycle(constants), this.converter, getter, setter));
	}

	public <E extends Enum<E>> BUILDER addEnum(Component title, Class<E> cl, Function<VALUE_HOLDER, E> getter, BiConsumer<VALUE_HOLDER, E> setter) {
		return this.addEnum(title, List.of(cl.getEnumConstants()), getter, setter);
	}

	public BUILDER addString(Component title, Function<VALUE_HOLDER, String> getter, BiConsumer<VALUE_HOLDER, String> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.STRING, title, null, this.converter, getter, setter));
	}

	public BUILDER addQuat(Component title, Function<VALUE_HOLDER, Quat> getter, BiConsumer<VALUE_HOLDER, Quat> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.QUAT, title, null, this.converter, getter, setter));
	}

	public BUILDER addBoolean(Component title, Function<VALUE_HOLDER, Boolean> getter, BiConsumer<VALUE_HOLDER, Boolean> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.BOOLEAN, title, null, this.converter, getter, setter));
	}

	public BUILDER addVec3(Component title, Function<VALUE_HOLDER, Vec3> getter, BiConsumer<VALUE_HOLDER, Vec3> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.VEC3, title, null, this.converter, getter, setter));
	}

	public BUILDER addVec2(Component title, Function<VALUE_HOLDER, Vec2> getter, BiConsumer<VALUE_HOLDER, Vec2> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.VEC2, title, null, this.converter, getter, setter));
	}

	public BUILDER addRotation(Component title, Function<VALUE_HOLDER, RotationData> getter, BiConsumer<VALUE_HOLDER, RotationData> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.ROTATION, title, null, this.converter, getter, setter));
	}

	public BUILDER addDirection(Component title, Function<VALUE_HOLDER, Direction> getter, BiConsumer<VALUE_HOLDER, Direction> setter) {
		return this.addEnum(title, List.of(Direction.VALUES), getter, setter);
	}

	public BUILDER addAABB(Component title, Function<VALUE_HOLDER, AABB> getter, BiConsumer<VALUE_HOLDER, AABB> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.AABB, title, null, this.converter, getter, setter));
	}

	public <E> BUILDER addFlags(Component title, List<E> flags, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.addFlags(title, RangeOption.ofFlags(flags), getter, setter);
	}

	public <E> BUILDER addFlags(Component title, RangeOption<E, List<E>> flags, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.getFlags(), title, flags, this.converter, getter, setter));
	}

	public <E> BUILDER addCollection(Component title, ListData<E, OBJECT> listData, Function<VALUE_HOLDER, List<E>> getter, BiConsumer<VALUE_HOLDER, List<E>> setter) {
		return this.add(new OptionProvider<>(OptionProviderType.geCollection(), title, listData, this.converter, getter, setter));
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

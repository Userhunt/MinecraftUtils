package net.w3e.util.common.gui.option;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.Identifier;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.vec3.Vec3;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.types.*;

@RequiredArgsConstructor
public abstract class OptionProviderType<VALUE> {

	@Getter
	private final Identifier key;

	public OptionProviderType(String value) {
		this(Identifier.parse(value));
	}

	public abstract <OBJECT> OptionProviderContainer<?, OBJECT, VALUE> createOption(OptionProvider<?, OBJECT, ?, VALUE> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font);

	@SuppressWarnings("unused")
	public static final OptionProviderType<Vec3> VEC3 = new OptionProviderType<>("vec3") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Vec3> createOption(OptionProvider<?, OBJECT, ?, Vec3> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new Vec3OptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unused")
	public static final OptionProviderType<?> ENUM = new OptionProviderType<>("enum") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Object> createOption(OptionProvider<?, OBJECT, ?, Object> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new EnumOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> OptionProviderType<E> getEnum() {
		return (OptionProviderType<E>) ENUM;
	}

	@SuppressWarnings("unused")
	public static final OptionProviderType<Integer> INT_RANGE = new OptionProviderType<>("int_range") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Integer> createOption(OptionProvider<?, OBJECT, ?, Integer> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new IntRangeOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unused")
	public static final OptionProviderType<Float> FLOAT_RANGE = new OptionProviderType<>("float_range") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Float> createOption(OptionProvider<?, OBJECT, ?, Float> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new FloatRangeOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unused")
	public static final OptionProviderType<?> VALUE_RANGE = new OptionProviderType<>("value_range") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Object> createOption(OptionProvider<?, OBJECT, ?, Object> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new ValueRangeOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> OptionProviderType<E> getValueRange() {
		return (OptionProviderType<E>) VALUE_RANGE;
	}

	@SuppressWarnings("unused")
	public static final OptionProviderType<RotationOptionProviderContainer.RotationData> ROTATION = new OptionProviderType<>("rotation") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, RotationOptionProviderContainer.RotationData> createOption(OptionProvider<?, OBJECT, ?, RotationOptionProviderContainer.RotationData> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new RotationOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unused")
	public static final OptionProviderType<String> STRING = new OptionProviderType<>("string") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, String> createOption(OptionProvider<?, OBJECT, ?, String> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new StringOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unused")
	public static final OptionProviderType<Quat> QUAT = new OptionProviderType<>("quat") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Quat> createOption(OptionProvider<?, OBJECT, ?, Quat> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new QuatOptionProviderContainer<>(provider, container, font);
		}
	};

	@SuppressWarnings("unused")
	public static final OptionProviderType<Boolean> BOOLEAN = new OptionProviderType<>("boolean") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Boolean> createOption(OptionProvider<?, OBJECT, ?, Boolean> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
			return new BooleanOptionProviderContainer<>(provider, container, font);
		}
	};

}

package net.w3e.util.common.gui.option;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.vec2.Vec2;
import net.skds.lib2.mat.vec3.Vec3;
import net.skds.lib2.shapes.AABB;
import net.w3e.util.client.gui.option.types.*;
import net.w3e.util.common.gui.option.container.ContainerPair;
import net.w3e.util.common.gui.option.container.OptionProviderContainer;
import net.w3e.util.common.gui.option.container.RotationData;

import java.util.List;

@RequiredArgsConstructor
public abstract class OptionProviderType<VALUE> {

	@Getter
	private final Identifier key;

	public OptionProviderType(String value) {
		this(Identifier.parse(value));
	}

	public abstract <OBJECT> OptionProviderContainer<?, OBJECT, VALUE> createOption(OptionProvider<?, OBJECT, ?, VALUE> provider, ContainerPair<OBJECT> container, Screen screen);

	public static final OptionProviderType<Vec3> VEC3 = new OptionProviderType<>("vec3") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Vec3> createOption(OptionProvider<?, OBJECT, ?, Vec3> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new Vec3OptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<Vec2> VEC2 = new OptionProviderType<>("vec2") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Vec2> createOption(OptionProvider<?, OBJECT, ?, Vec2> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new Vec2OptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<?> ENUM = new OptionProviderType<>("enum") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Object> createOption(OptionProvider<?, OBJECT, ?, Object> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new EnumOptionProviderContainer<>(provider, container, screen);
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> OptionProviderType<E> getEnum() {
		return (OptionProviderType<E>) ENUM;
	}

	public static final OptionProviderType<Integer> INT_RANGE = new OptionProviderType<>("int_range") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Integer> createOption(OptionProvider<?, OBJECT, ?, Integer> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new IntRangeOptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<Float> FLOAT_RANGE = new OptionProviderType<>("float_range") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Float> createOption(OptionProvider<?, OBJECT, ?, Float> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new FloatRangeOptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<?> VALUE_RANGE = new OptionProviderType<>("value_range") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Object> createOption(OptionProvider<?, OBJECT, ?, Object> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new ValueRangeOptionProviderContainer<>(provider, container, screen);
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> OptionProviderType<E> getValueRange() {
		return (OptionProviderType<E>) VALUE_RANGE;
	}

	public static final OptionProviderType<RotationData> ROTATION = new OptionProviderType<>("rotation") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, RotationData> createOption(OptionProvider<?, OBJECT, ?, RotationData> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new RotationOptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<String> STRING = new OptionProviderType<>("string") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, String> createOption(OptionProvider<?, OBJECT, ?, String> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new StringOptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<Quat> QUAT = new OptionProviderType<>("quat") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Quat> createOption(OptionProvider<?, OBJECT, ?, Quat> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new QuatOptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<Boolean> BOOLEAN = new OptionProviderType<>("boolean") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, Boolean> createOption(OptionProvider<?, OBJECT, ?, Boolean> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new BooleanOptionProviderContainer<>(provider, container, screen);
		}
	};

	public static final OptionProviderType<List<?>> FLAGS = new OptionProviderType<>("flags") {
		@SuppressWarnings("unchecked")
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, List<?>> createOption(OptionProvider<?, OBJECT, ?, List<?>> provider, ContainerPair<OBJECT> container, Screen screen) {
			return (OptionProviderContainer<?, OBJECT, List<?>>) (OptionProviderContainer<?, ?, ?>) new FlagsOptionProviderContainer<>(provider, container, screen);
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> OptionProviderType<List<E>> getFlags() {
		return (OptionProviderType<List<E>>) (OptionProviderType<?>) FLAGS;
	}

	public static final OptionProviderType<List<?>> COLLECTION = new OptionProviderType<>("collection") {
		@SuppressWarnings("unchecked")
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, List<?>> createOption(OptionProvider<?, OBJECT, ?, List<?>> provider, ContainerPair<OBJECT> container, Screen screen) {
			return (OptionProviderContainer<?, OBJECT, List<?>>) (OptionProviderContainer<?, ?, ?>) new CollectionOptionProviderContainer<>(provider, container, screen);
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> OptionProviderType<List<E>> geCollection() {
		return (OptionProviderType<List<E>>) (OptionProviderType<?>) COLLECTION;
	}

	public static final OptionProviderType<AABB> AABB = new OptionProviderType<>("aabb") {
		@Override
		public <OBJECT> OptionProviderContainer<?, OBJECT, AABB> createOption(OptionProvider<?, OBJECT, ?, AABB> provider, ContainerPair<OBJECT> container, Screen screen) {
			return new AABBOptionProviderContainer<>(provider, container, screen);
		}
	};
}

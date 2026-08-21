package net.w3e.util.common.gui.option.container;

public record RotationData(float yaw, float pitch, boolean hasPitch) {
	public RotationData withYaw(float yaw) {
		return new RotationData(yaw, this.pitch, this.hasPitch);
	}

	public RotationData withPitch(float pitch) {
		return new RotationData(this.yaw, pitch, this.hasPitch);
	}
}
package net.w3e.util.client.minimap;

public interface MapRadiusPredicate {
	RadiusState getState(int imgX, int imgY);

	MapRadiusPredicate INFINITY = (_, _) -> RadiusState.DEFAULT;

	record RadiusState(boolean isInRadius, boolean ditherBlack) {
		public static final RadiusState DEFAULT = new RadiusState(true, false);
	}

}

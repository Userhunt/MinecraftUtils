package net.w3e.util.client.minimap;

import java.util.List;

public interface MapUpdateListener {
	void markDirty(MapTextureSegment instance, List<DirtyData> dirtyData);

	record DirtyData(int x, int y, byte color) {
	}
}

package net.w3e.util.client.gui.layout;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.w3e.util.client.gui.container.WLayoutSimpleContainer;

import java.util.ArrayList;
import java.util.List;

public class WFlowLayout extends WLayoutSimpleContainer {

	@Setter
	@Accessors(chain = true)
	private float maxWidth = 250;
	@Setter
	@Accessors(chain = true)
	private float maxHeight = Integer.MAX_VALUE;

	@Setter
	@Accessors(chain = true)
	private int horizontalSpacing = 5;
	@Setter
	@Accessors(chain = true)
	private int verticalSpacing = 5;

	@Deprecated
	@Override
	public void add(WLayoutElementData<?> element) {
		super.add(element);
	}

	@Deprecated
	@Override
	public <E extends LayoutElement> WLayoutElementData<E> add(int x, int y, E element) {
		return super.add(x, y, element);
	}

	public <T extends LayoutElement> T add(T element) {
		this.addElement(element);
		return element;
	}

	@Override
	public void arrangeElements() {
		List<LayoutElement> list = new ArrayList<>(this.elements.stream().map(WLayoutElementData::asElement).toList());
		this.elements.clear();

		this.clear();

		final int layoutX = this.getX();
		final int layoutY = this.getY();
		this.setX(0);
		this.setY(0);

		int currentX = 0;
		int currentY = 0;

		for (LayoutElement layoutElement : list) {
			if (layoutElement instanceof Layout layout) {
				layout.arrangeElements();
			}
			int h = layoutElement.getHeight();
			if (currentY + h < this.maxHeight) {
				int w = layoutElement.getWidth();

				if (currentX + w < this.maxWidth) {
					this.addElement(new WLayoutElementData<>(currentX, currentY, layoutElement));
					currentX += w + this.horizontalSpacing;
					continue;
				}
				currentX = 0;
				this.setHeight(-1);
				currentY = this.getHeight() + this.verticalSpacing;
				if (currentX + w < this.maxWidth) {
					this.addElement(new WLayoutElementData<>(currentX, currentY, layoutElement));
					currentX += w + this.horizontalSpacing;
					continue;
				}
			}

			layoutElement = new WLayoutElementData<>(0, 0, layoutElement, false);
			this.addElement(layoutElement);
		}

		this.resetSize();
		setX(layoutX);
		setY(layoutY);

		super.repositionElements();
	}

	public WFlowLayout spacing(int spacing) {
		this.setHorizontalSpacing(spacing);
		this.setVerticalSpacing(spacing);
		return this;
	}
}

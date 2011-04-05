package com.traveldiary.controller;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class Overlay extends ItemizedOverlay<OverlayItem> {
	private static int maxNumberOfOverlaysToShow = 3;
	private final OverlayItem overlays[] = new OverlayItem[maxNumberOfOverlaysToShow];
	private int index = 0;
	private boolean full = false;

	public Overlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays[i];
	}

	@Override
	public int size() {
		if (full) {
			return overlays.length;
		} else {
			return index;
		}
	}

	public void addOverlay(OverlayItem overlay) {
		if (index < maxNumberOfOverlaysToShow) {
			overlays[index] = overlay;
		} else {
			index = 0;
			full = true;
			overlays[index] = overlay;
		}
		index++;
		populate();
	}
}

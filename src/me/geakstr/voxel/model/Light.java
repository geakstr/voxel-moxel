package me.geakstr.voxel.model;

import java.awt.Color;

public class Light {
	
	public static int brightness(Color c) {
		return brightness(c.getRed(), c.getGreen(), c.getBlue());
	}
	
	public static int brightness(int r, int g, int b) {
		return r * 299 + g * 587 + b * 114 / 1000;
	}
}

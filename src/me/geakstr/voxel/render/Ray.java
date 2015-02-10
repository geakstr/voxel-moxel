package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Vector3f;

public class Ray {
	public Vector3f origin;
	public Vector3f direction;
	public Vector3f inv_direction;
	public boolean[] sign;
	
	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction;
		this.inv_direction = new Vector3f(1 / direction.x, 1 / direction.y, 1 / direction.z);
		
		this.sign = new boolean[3];
		this.sign[0] = (inv_direction.x < 0);
		this.sign[1] = (inv_direction.y < 0);
		this.sign[2] = (inv_direction.z < 0);
	}
	
	public String toString() {
		return "Origin (" + origin.x + " " + origin.y + " " + origin.z + "); Direction: (" + direction.x + " " + direction.y + " " + direction.z + ")";
	}
}

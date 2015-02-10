package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Vector3f;

public class Ray {
    public Vector3f origin;
    public Vector3f direction;
    public Vector3f inv_direction;
    public int[] sign;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = new Vector3f(origin.x, origin.y, origin.z);
        this.direction = new Vector3f(direction.x, direction.y, direction.z);
        this.inv_direction = new Vector3f(1 / direction.x, 1 / direction.y, 1 / direction.z);

        this.sign = new int[3];
        this.sign[0] = (inv_direction.x < 0) ? 1 : 0;
        this.sign[1] = (inv_direction.y < 0) ? 1 : 0;
        this.sign[2] = (inv_direction.z < 0) ? 1 : 0;
    }

    public String toString() {
        return "Origin (" + origin.x + " " + origin.y + " " + origin.z + "); Direction: (" + direction.x + " " + direction.y + " " + direction.z + ")";
    }
}

package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.render.Ray;

public class Block extends Box {
    public int type;
    public Vector3f[] corners;

    public Block(int type, Vector3f min, Vector3f max) {
        this.type = type;
        this.corners = new Vector3f[]{min, max};
    }

    public Block() {
        this(0);
    }

    public Block(int type) {
        this(type, new Vector3f(), new Vector3f());
    }

    public Block(Vector3f min, Vector3f max) {
        this(0, min, max);
    }

    public boolean intersect(Ray ray, float t0, float t1) {
        float tmin = (corners[ray.sign[0]].x - ray.origin.x) * ray.inv_direction.x;
        float tmax = (corners[1 - ray.sign[0]].x - ray.origin.x) * ray.inv_direction.x;
        float tymin = (corners[ray.sign[1]].y - ray.origin.y) * ray.inv_direction.y;
        float tymax = (corners[1 - ray.sign[1]].y - ray.origin.y) * ray.inv_direction.y;
        if ((tmin > tymax) || (tymin > tmax)) {
            return false;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        float tzmin = (corners[ray.sign[2]].z - ray.origin.z) * ray.inv_direction.z;
        float tzmax = (corners[1 - ray.sign[2]].z - ray.origin.z) * ray.inv_direction.z;
        if ((tmin > tzmax) || (tzmin > tmax)) {
            return false;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        return ((tmin < t1) && (tmax > t0));
    }
}
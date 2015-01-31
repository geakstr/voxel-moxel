package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

public class Vertex {
    public static final int SIZE = 3;

    public Vector3f coord;

    public Vertex(Vector3f coord) {
        this.coord = coord;
    }
}

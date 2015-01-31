package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

public class Cube {
    public static final Vertex[] vertices = new Vertex[]{
            new Vertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
            new Vertex(new Vector3f( 1.0f, -1.0f, -1.0f)),
            new Vertex(new Vector3f( 1.0f,  1.0f, -1.0f)),
            new Vertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
            new Vertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
            new Vertex(new Vector3f( 1.0f, -1.0f,  1.0f)),
            new Vertex(new Vector3f( 1.0f,  1.0f,  1.0f)),
            new Vertex(new Vector3f(-1.0f,  1.0f,  1.0f))
    };

    // TODO may by can reorganized
    public static final int[] indices = new int[] {
            0, 4, 7, 0, 7, 3,
            5, 7, 4, 6, 7, 5 ,
            3, 7, 6, 6, 2, 3,
            6, 5, 1, 6, 1, 2,
            3, 1, 0, 3, 2, 1,
            5, 4, 0, 0, 1, 5
    };

    public static final int[] normals = new int[]{
             0, -1,  0,
             1,  0,  0,
             1,  0,  0,
             0,  1,  0,
             0,  0,  1,
             0,  0,  1,
             0,  1,  0,
            -1,  0,  0
    };
}

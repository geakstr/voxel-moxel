package me.geakstr.voxel.model;

import java.util.Arrays;

public class CubeManager {
    public static final float[] vertices = new float[]{
//            -1.0f, -1.0f, -1.0f,
//             1.0f, -1.0f, -1.0f,
//             1.0f,  1.0f, -1.0f,
//            -1.0f,  1.0f, -1.0f,
//            -1.0f, -1.0f,  1.0f,
//             1.0f, -1.0f,  1.0f,
//             1.0f,  1.0f,  1.0f,
//            -1.0f,  1.0f,  1.0f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f
    };

    public static final int size = vertices.length;

//    public static final int[] indices = new int[]{
//            0, 4, 7, 0, 7, 3,
//            5, 7, 4, 6, 7, 5,
//            3, 7, 6, 6, 2, 3,
//            6, 5, 1, 6, 1, 2,
//            3, 1, 0, 3, 2, 1,
//            5, 4, 0, 0, 1, 5
//    };

    public static Cube get(int x, int z, int y/*, int offset*/) {
        Cube cube = new Cube(Arrays.copyOf(vertices, vertices.length)/*, Arrays.copyOf(indices, vertices.length)*/);

        for (int i = 0; i < cube.vertices.length; i += 3) {
            cube.vertices[i] += x;
            cube.vertices[i + 1] += z;
            cube.vertices[i + 2] += y;
        }
//        for (int i = 0; i < 36; i++) {
//            cube.indices[i] += offset;
//        }

        return cube;
    }
}

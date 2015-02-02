package me.geakstr.voxel.model;

import java.util.Arrays;

public class CubeManager {
    public static float[] get_side(int side_idx, int x, int z, int y) {
        float[] side = get_side(side_idx);

        for (int i = 0; i < cube_side_vertices_size; i += 3) {
            side[i] += x;
            side[i + 1] += z;
            side[i + 2] += y;
        }

        return side;
    }

    public static float[] get_side(int side_idx, int x0, int y0, int x1, int y1, int z) {
        float[] side = get_side(side_idx);

        if (side_idx == 0 || side_idx == 1) {
            if (side_idx == 1) {
                x0 = x1;
            }
            side[0] += x0;
            side[1] += z;
            side[2] += y0;

            side[3] += x0;
            side[4] += z;
            side[5] += y1;

            side[6] += x0;
            side[7] += z;
            side[8] += y1;

            side[9] += x0;
            side[10] += z;
            side[11] += y0;

            side[12] += x0;
            side[13] += z;
            side[14] += y1;

            side[15] += x0;
            side[16] += z;
            side[17] += y0;
        } else if (side_idx == 2 || side_idx == 3) {
            if (side_idx == 3) {
                y1 = y0;
            }
            side[0] += x0;
            side[1] += z;
            side[2] += y1;

            side[3] += x0;
            side[4] += z;
            side[5] += y1;

            side[6] += x1;
            side[7] += z;
            side[8] += y1;

            side[9] += x1;
            side[10] += z;
            side[11] += y1;

            side[12] += x0;
            side[13] += z;
            side[14] += y1;

            side[15] += x1;
            side[16] += z;
            side[17] += y1;
        } else if (side_idx == 4 || side_idx == 5) {
            x0--;
            y0--;
            x1++;
            y1++;
            side[0] += x0;
            side[1] += z;
            side[2] += y0;

            side[3] += x1;
            side[4] += z;
            side[5] += y1;

            side[6] += x0;
            side[7] += z;
            side[8] += y1;

            side[9] += x0;
            side[10] += z;
            side[11] += y0;

            side[12] += x1;
            side[13] += z;
            side[14] += y0;

            side[15] += x1;
            side[16] += z;
            side[17] += y1;
        }

        return side;
    }

    public static float[] get_side(int side_idx) {
        float[] side = null;
        switch (side_idx) {
            case 0:
                side = Arrays.copyOf(back_vertices, cube_side_vertices_size);
                break;
            case 1:
                side = Arrays.copyOf(front_vertices, cube_side_vertices_size);
                break;
            case 2:
                side = Arrays.copyOf(right_vertices, cube_side_vertices_size);
                break;
            case 3:
                side = Arrays.copyOf(left_vertices, cube_side_vertices_size);
                break;
            case 4:
                side = Arrays.copyOf(bottom_vertices, cube_side_vertices_size);
                break;
            case 5:
                side = Arrays.copyOf(top_vertices, cube_side_vertices_size);
                break;
        }
        return side;
    }

    public static Cube get_cube(int x, int z, int y/*, int offset*/) {
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

    // Docs for pack/unpack function placed in Cube class
    public static int unpack_type(int val) {
        return ((val >> 16) & 511);
    }

    public static int pack_type(int val, int type) {
        return val | ((type & 511) << 16);
    }

    public static int unpack_visibility(int val) {
        return ((val >> 15) & 511) & 1;
    }

    public static int pack_visibility(int val, boolean visibility) {
        int v = visibility ? 1 : 0;
        return val | ((v & 511) << 15);
    }

    public static final float[] vertices = new float[]{
//            -1.0f, -1.0f, -1.0f,
//             1.0f, -1.0f, -1.0f,
//             1.0f,  1.0f, -1.0f,
//            -1.0f,  1.0f, -1.0f,
//            -1.0f, -1.0f,  1.0f,
//             1.0f, -1.0f,  1.0f,
//             1.0f,  1.0f,  1.0f,
//            -1.0f,  1.0f,  1.0f,

            // TOP
            // 7 - 4 - 5
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            // 6 - 7 - 5
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,

            // BOTTOM
            // 2 - 1 - 0
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            // 2 - 0 - 3
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,

            // FRONT
            // 6 - 1 - 2
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            // 1 - 6 - 5
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,

            // BACK
            // 0 - 4 - 7
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            // 0 - 7 - 3
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,

            // LEFT
            // 5 - 0 - 1
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            // 5 - 4 - 0
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,

            // RIGHT
            // 6 - 2 - 3
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            // 6 - 3 - 7
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f
    };

    public static final float[] right_vertices = new float[]{
            // 2 - 1 - 7
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            // 4 - 2 - 7
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
    };

    public static final float[] left_vertices = new float[]{
            // 3 - 0 - 5
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,

            // 6 - 3 - 5
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f

            // 2 - 1 - 0
//            0.5f, 0.5f, -0.5f,
//            0.5f, -0.5f, -0.5f,
//            -0.5f, -0.5f, -0.5f,
//            // 2 - 0 - 3
//            0.5f, 0.5f, -0.5f,
//            -0.5f, -0.5f, -0.5f,
//            -0.5f, 0.5f, -0.5f,
    };

    public static final float[] front_vertices = new float[]{
            // 5 - 7 - 4
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,

            // 5 - 4 - 6
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
    };

    public static final float[] back_vertices = new float[]{
            // 0 - 1 - 2
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,

            // 0 - 2 - 3
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
    };

    public static final float[] bottom_vertices = new float[]{
            // 7 - 0 - 5
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            // 7 - 1 - 0
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
    };

    public static final float[] top_vertices = new float[] {
            // 4 - 3 - 6
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,

            // 4 - 2 - 3
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f
    };

    public static final int cube_vertices_size = vertices.length;
    public static final int cube_side_vertices_size = right_vertices.length;

//    public static final int[] indices = new int[]{
//            0, 4, 7, 0, 7, 3,
//            5, 7, 4, 6, 7, 5,
//            3, 7, 6, 6, 2, 3,
//            6, 5, 1, 6, 1, 2,
//            3, 1, 0, 3, 2, 1,
//            5, 4, 0, 0, 1, 5
//    };
}
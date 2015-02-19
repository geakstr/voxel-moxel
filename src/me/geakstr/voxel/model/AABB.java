package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

import java.util.Arrays;

/*
Box represents as:

     5 {0,1,0}+-------+6 {1,1,0}
           .' |     .'|
 1 {0,1,1}+-------+'4 {1,1,1}
          |   |   |   |
     8 {0,0,0}+---|---+7 {1,0,0}
          | .'    | .'
 2 {0,0,1}+-------+'3 {1,0,1}

All sides was defined in CCW order
*/

public class AABB extends MeshIndexed {
    public static final int side_size = 12, sides_size = 72;

    public Vector3f[] corners;

    public AABB() {
        super();

        this.corners = new Vector3f[2];

        this.update_data(SIDE.ALL.get());
        this.update_gl_buffers();
    }

    public AABB(Vector3f min, Vector3f max) {
        this.corners = new Vector3f[]{min, max};
    }

    public static enum SIDE {
        FRONT {
            public float[] get() {
                return Arrays.copyOf(front_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        },
        BACK {
            public float[] get() {
                return Arrays.copyOf(back_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        },
        LEFT {
            public float[] get() {
                return Arrays.copyOf(left_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        },
        RIGHT {
            public float[] get() {
                return Arrays.copyOf(right_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        },
        TOP {
            public float[] get() {
                return Arrays.copyOf(top_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        },
        BOTTOM {
            public float[] get() {
                return Arrays.copyOf(bottom_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        },
        ALL {
            public float[] get() {
                return Arrays.copyOf(sides_vertices, sides_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(get(), x, y, z);
            }

            public float[] scale(int x0, int y0, int z0, int x1, int y1, int z1) {
                return scale_side(get(), x0, y0, z0, x1, y1, z1);
            }
        };

        public abstract float[] get();

        public abstract float[] translate(int x, int y, int z);

        public abstract float[] scale(int x0, int y0, int z0, int x1, int y1, int z1);
    }

    private static float[] translate_side(float[] side, int x, int y, int z) {
        int size = side.length;

        float[] ret = new float[size];
        System.arraycopy(side, 0, ret, 0, size);

        for (int i = 0; i < size; i += 3) {
            ret[i] += x;
            ret[i + 1] += y;
            ret[i + 2] += z;
        }

        return ret;
    }

    private static float[] scale_side(float[] side, int x0, int y0, int z0, int x1, int y1, int z1) {
        float[] ret = translate_side(side, x0, y0, z0);
        return null;
    }

    private static final float[] sides_vertices = new float[]{
            // front
            0, 0, 1, // v2
            1, 0, 1, // v3
            0, 1, 1, // v1
            1, 1, 1, // v4

            // back
            1, 0, 0, // v7
            0, 0, 0, // v8
            1, 1, 0, // v6
            0, 1, 0, // v5

            // left
            0, 0, 0, // v8
            0, 0, 1, // v2
            0, 1, 0, // v5
            0, 1, 1, // v1

            // right
            1, 0, 1, // v3
            1, 0, 0, // v7
            1, 1, 1, // v4
            1, 1, 0, // v6

            // top
            0, 1, 1, // v1
            1, 1, 1, // v4
            0, 1, 0, // v5
            1, 1, 0, // v6

            // bottom
            0, 0, 0, // v8
            1, 0, 0, // v7
            0, 0, 1, // v2
            1, 0, 1, // v3
    };

    private static final float[] front_side_vertices = new float[]{
            0, 0, 1, // v2
            1, 0, 1, // v3
            0, 1, 1, // v1
            1, 1, 1, // v4
    };

    private static final float[] back_side_vertices = new float[]{
            1, 0, 0, // v7
            0, 0, 0, // v8
            1, 1, 0, // v6
            0, 1, 0, // v5
    };

    private static final float[] left_side_vertices = new float[]{
            0, 0, 0, // v8
            0, 0, 1, // v2
            0, 1, 0, // v5
            0, 1, 1, // v1
    };

    private static final float[] right_side_vertices = new float[]{
            1, 0, 1, // v3
            1, 0, 0, // v7
            1, 1, 1, // v4
            1, 1, 0, // v6
    };

    private static final float[] top_side_vertices = new float[]{
            0, 1, 1, // v1
            1, 1, 1, // v4
            0, 1, 0, // v5
            1, 1, 0, // v6
    };

    private static final float[] bottom_side_vertices = new float[]{
            0, 0, 0, // v8
            1, 0, 0, // v7
            0, 0, 1, // v2
            1, 0, 1, // v3
    };
}

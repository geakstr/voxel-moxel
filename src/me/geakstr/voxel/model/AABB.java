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
    public static final int side_size = 12;
    public static final int sides_size = 72;

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

            public float[] get(int x, int y, int z) {
                return null;
            }
        },
        BACK {
            public float[] get() {
                return Arrays.copyOf(back_side_vertices, side_size);
            }

            public float[] get(int x, int y, int z) {
                return null;
            }
        },
        LEFT {
            public float[] get() {
                return Arrays.copyOf(left_side_vertices, side_size);
            }

            public float[] get(int x, int y, int z) {
                return null;
            }
        },
        RIGHT {
            public float[] get() {
                return Arrays.copyOf(right_side_vertices, side_size);
            }

            public float[] get(int x, int y, int z) {
                return null;
            }
        },
        TOP {
            public float[] get() {
                return Arrays.copyOf(top_side_vertices, side_size);
            }

            public float[] get(int x, int y, int z) {
                return null;
            }
        },
        BOTTOM {
            public float[] get() {
                return Arrays.copyOf(bottom_side_vertices, side_size);
            }

            public float[] get(int x, int y, int z) {
                return null;
            }
        },
        ALL {
            public float[] get() {
                return Arrays.copyOf(sides_vertices, sides_size);
            }

            public float[] get(int x, int y, int z) {
                return null;
            }
        };

        public abstract float[] get();

        public abstract float[] get(int x, int y, int z);
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

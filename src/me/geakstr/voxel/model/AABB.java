package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

import java.util.Arrays;

/*
Box represents as vertices with {x,y,z} coords in OpenGL coordinate system:

              ↑ Y
              |
              |
              |
              |
     4 {0,1,0}+-------+5 {1,1,0}
           .' |     .'|
 0 {0,1,1}+-------+'3 {1,1,1}
          |   |   |   |
     7 {0,0,0}+---|---+6 {1,0,0}-------------→ X
          | .'    | .'
 1 {0,0,1}+-------+'2 {1,0,1}
       .'
     .'
   .'
 ↙ Z

All works in CCW order
*/

public class AABB {
    public static final int side_vertices_size = 12, side_tex_coords_size = 8, sides_vertices_size = 72;

    public Vector3f[] corners;

    public AABB(float x, float y, float z, float width, float height, float length) {
        this.corners = new Vector3f[]{
                new Vector3f(x, y, z),
                new Vector3f(x + width, y + height, z + length)
        };
    }

    public AABB() {
        this(0, 0, 0, 1, 1, 1);
    }

    public static enum SIDE {
        FRONT {
            public float[] vertices_coords() {
                return Arrays.copyOf(front_side_vertices_coords, side_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{0, 1, u, 1, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                side[3] += x_expand;
                side[9] += x_expand;

                side[7] += y_expand;
                side[10] += y_expand;

                side[2] += z_expand;
                side[5] += z_expand;
                side[8] += z_expand;
                side[11] += z_expand;

                return side;
            }
        },
        BACK {
            public float[] vertices_coords() {
                return Arrays.copyOf(back_side_vertices_coords, side_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{0, 1, u, 1, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                side[0] += x_expand;
                side[6] += x_expand;

                side[7] += y_expand;
                side[10] += y_expand;

                return side;
            }
        },
        LEFT {
            public float[] vertices_coords() {
                return Arrays.copyOf(left_side_vertices_coords, side_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{v, 1, 0, 1, v, 0, 0, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                side[7] += y_expand;
                side[10] += y_expand;

                side[5] += z_expand;
                side[11] += z_expand;

                return side;
            }
        },
        RIGHT {
            public float[] vertices_coords() {
                return Arrays.copyOf(right_side_vertices_coords, side_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{v, 1, 0, 1, v, 0, 0, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                side[0] += x_expand;
                side[3] += x_expand;
                side[6] += x_expand;
                side[9] += x_expand;

                side[7] += y_expand;
                side[10] += y_expand;

                side[2] += z_expand;
                side[8] += z_expand;

                return side;
            }
        },
        TOP {
            public float[] vertices_coords() {
                return Arrays.copyOf(top_side_vertices_coords, side_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{0, v, u, v, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                side[3] += x_expand;
                side[9] += x_expand;

                side[1] += y_expand;
                side[4] += y_expand;
                side[7] += y_expand;
                side[10] += y_expand;

                side[2] += z_expand;
                side[5] += z_expand;

                return side;
            }
        },
        BOTTOM {
            public float[] vertices_coords() {
                return Arrays.copyOf(bottom_side_vertices_coords, side_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{0, v, u, v, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                side[3] += x_expand;
                side[9] += x_expand;

                side[8] += z_expand;
                side[11] += z_expand;

                return side;
            }
        },
        ALL {
            public float[] vertices_coords() {
                return Arrays.copyOf(all_sides_vertices_coords, sides_vertices_size);
            }

            public float[] texture_coords(int u, int v) {
                return new float[]{
                        // front
                        0, 1, u, 1, 0, 0, u, 0,
                        // back
                        0, 1, u, 1, 0, 0, u, 0,
                        // left
                        v, 1, 0, 1, v, 0, 0, 0,
                        // right
                        v, 1, 0, 1, v, 0, 0, 0,
                        // top
                        0, v, u, v, 0, 0, u, 0,
                        // bottom
                        0, v, u, v, 0, 0, u, 0
                };
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(vertices_coords(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
                x_expand--;
                y_expand--;
                z_expand--;
                float[] side = translate(x_pos, y_pos, z_pos);

                // front
                side[3] += x_expand;
                side[9] += x_expand;

                side[7] += y_expand;
                side[10] += y_expand;

                side[2] += z_expand;
                side[5] += z_expand;
                side[8] += z_expand;
                side[11] += z_expand;

                // back
                side[12] += x_expand;
                side[18] += x_expand;

                side[19] += y_expand;
                side[22] += y_expand;

                // left
                side[31] += y_expand;
                side[34] += y_expand;

                side[29] += z_expand;
                side[35] += z_expand;

                // right
                side[36] += x_expand;
                side[39] += x_expand;
                side[42] += x_expand;
                side[45] += x_expand;

                side[43] += y_expand;
                side[46] += y_expand;

                side[38] += z_expand;
                side[44] += z_expand;

                // top
                side[51] += x_expand;
                side[57] += x_expand;

                side[49] += y_expand;
                side[52] += y_expand;
                side[55] += y_expand;
                side[58] += y_expand;

                side[50] += z_expand;
                side[53] += z_expand;

                // bottom
                side[63] += x_expand;
                side[69] += x_expand;

                side[68] += z_expand;
                side[71] += z_expand;

                return side;
            }
        };

        public abstract float[] vertices_coords();

        public abstract float[] texture_coords(int u, int v);

        public abstract float[] translate(int x_pos, int y_pos, int z_pos);

        public abstract float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand);

        public static final SIDE[] values = values();
    }

    public static enum VERTEX {
        V0 {
            public float[] coords() {
                return Arrays.copyOf(v0_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V1 {
            public float[] coords() {
                return Arrays.copyOf(v1_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V2 {
            public float[] coords() {
                return Arrays.copyOf(v2_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V3 {
            public float[] coords() {
                return Arrays.copyOf(v3_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V4 {
            public float[] coords() {
                return Arrays.copyOf(v4_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V5 {
            public float[] coords() {
                return Arrays.copyOf(v5_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V6 {
            public float[] coords() {
                return Arrays.copyOf(v6_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        V7 {
            public float[] coords() {
                return Arrays.copyOf(v7_vertex_coords, 3);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        },
        ALL {
            public float[] coords() {
                return Arrays.copyOf(all_vertices_coords, 18);
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return AABB.translate(coords(), x_pos, y_pos, z_pos);
            }
        };

        public abstract float[] coords();

        public abstract float[] translate(int x_pos, int y_pos, int z_pos);

        public static final VERTEX[] values = values();
    }

    private static float[] translate(float[] vertices_coords, int x_pos, int y_pos, int z_pos) {
        int size = vertices_coords.length;

        for (int i = 0; i < size; i += 3) {
            vertices_coords[i] += x_pos;
            vertices_coords[i + 1] += y_pos;
            vertices_coords[i + 2] += z_pos;
        }

        return vertices_coords;
    }

    private static final float[] all_sides_vertices_coords = new float[]{
            // front
            0, 0, 1, // v1
            1, 0, 1, // v2
            0, 1, 1, // v0
            1, 1, 1, // v3

            // back
            1, 0, 0, // v6
            0, 0, 0, // v7
            1, 1, 0, // v5
            0, 1, 0, // v4

            // left
            0, 0, 0, // v7
            0, 0, 1, // v1
            0, 1, 0, // v4
            0, 1, 1, // v0

            // right
            1, 0, 1, // v2
            1, 0, 0, // v6
            1, 1, 1, // v3
            1, 1, 0, // v5

            // top
            0, 1, 1, // v0
            1, 1, 1, // v3
            0, 1, 0, // v4
            1, 1, 0, // v5

            // bottom
            0, 0, 0, // v7
            1, 0, 0, // v6
            0, 0, 1, // v1
            1, 0, 1, // v2
    };

    private static final float[] front_side_vertices_coords = new float[]{
            0, 0, 1, // v1
            1, 0, 1, // v2
            0, 1, 1, // v0
            1, 1, 1, // v3
    };

    private static final int[] front_side_vertices_idx = new int[]{
            1, 2, 0, 3
    };

    private static final float[] back_side_vertices_coords = new float[]{
            1, 0, 0, // v6
            0, 0, 0, // v7
            1, 1, 0, // v5
            0, 1, 0, // v4
    };

    private static final int[] back_side_vertices_idx = new int[]{
            6, 7, 5, 4
    };

    private static final float[] left_side_vertices_coords = new float[]{
            0, 0, 0, // v7
            0, 0, 1, // v1
            0, 1, 0, // v4
            0, 1, 1, // v0
    };

    private static final int[] left_side_vertices_idx = new int[]{
            7, 1, 4, 0
    };

    private static final float[] right_side_vertices_coords = new float[]{
            1, 0, 1, // v2
            1, 0, 0, // v6
            1, 1, 1, // v3
            1, 1, 0, // v5
    };

    private static final int[] right_side_vertices_idx = new int[]{
            2, 6, 3, 5
    };

    private static final float[] top_side_vertices_coords = new float[]{
            0, 1, 1, // v0
            1, 1, 1, // v3
            0, 1, 0, // v4
            1, 1, 0, // v5
    };

    private static final int[] top_side_vertices_idx = new int[]{
            0, 3, 4, 5
    };

    private static final float[] bottom_side_vertices_coords = new float[]{
            0, 0, 0, // v7
            1, 0, 0, // v6
            0, 0, 1, // v1
            1, 0, 1, // v2
    };

    private static final int[] bottom_side_vertices_idx = new int[]{
            7, 6, 1, 2
    };

    private static final float[] all_vertices_coords = new float[]{
            0, 1, 1, // v0
            0, 0, 1, // v1
            1, 0, 1, // v2
            1, 1, 1, // v3
            0, 1, 0, // v4
            1, 1, 0, // v5
            1, 0, 0, // v6
            0, 0, 0, // v7
    };

    private static final float[] v0_vertex_coords = new float[]{
            0, 1, 1, // v0
    };

    private static final float[] v1_vertex_coords = new float[]{
            0, 0, 1, // v1
    };

    private static final float[] v2_vertex_coords = new float[]{
            1, 0, 1, // v2
    };

    private static final float[] v3_vertex_coords = new float[]{
            1, 1, 1, // v3
    };

    private static final float[] v4_vertex_coords = new float[]{
            0, 1, 0, // v4
    };

    private static final float[] v5_vertex_coords = new float[]{
            1, 1, 0, // v5
    };

    private static final float[] v6_vertex_coords = new float[]{
            1, 0, 0, // v6
    };

    private static final float[] v7_vertex_coords = new float[]{
            0, 0, 0, // v7
    };
}

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
     5 {0,1,0}+-------+6 {1,1,0}
           .' |     .'|
 1 {0,1,1}+-------+'4 {1,1,1}
          |   |   |   |
     8 {0,0,0}+---|---+7 {1,0,0}-------------→ X
          | .'    | .'
 2 {0,0,1}+-------+'3 {1,0,1}
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
            public float[] verts() {
                return Arrays.copyOf(front_side_vertices, side_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
                return new float[]{0, 1, u, 1, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
            public float[] verts() {
                return Arrays.copyOf(back_side_vertices, side_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
                return new float[]{0, 1, u, 1, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
            public float[] verts() {
                return Arrays.copyOf(left_side_vertices, side_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
                return new float[]{v, 1, 0, 1, v, 0, 0, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
            public float[] verts() {
                return Arrays.copyOf(right_side_vertices, side_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
                return new float[]{v, 1, 0, 1, v, 0, 0, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
            public float[] verts() {
                return Arrays.copyOf(top_side_vertices, side_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
                return new float[]{0, v, u, v, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
            public float[] verts() {
                return Arrays.copyOf(bottom_side_vertices, side_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
                return new float[]{0, v, u, v, 0, 0, u, 0};
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
            public float[] verts() {
                return Arrays.copyOf(all_sides_vertices, sides_vertices_size);
            }

            public float[] tex_coords(int u, int v) {
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
                return translate_side(verts(), x_pos, y_pos, z_pos);
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
                side[0 + 12] += x_expand;
                side[6 + 12] += x_expand;

                side[7 + 12] += y_expand;
                side[10 + 12] += y_expand;

                // left
                side[7 + 24] += y_expand;
                side[10 + 24] += y_expand;

                side[5 + 24] += z_expand;
                side[11 + 24] += z_expand;

                // right
                side[0 + 36] += x_expand;
                side[3 + 36] += x_expand;
                side[6 + 36] += x_expand;
                side[9 + 36] += x_expand;

                side[7 + 36] += y_expand;
                side[10 + 36] += y_expand;

                side[2 + 36] += z_expand;
                side[8 + 36] += z_expand;

                // top
                side[3 + 48] += x_expand;
                side[9 + 48] += x_expand;

                side[1 + 48] += y_expand;
                side[4 + 48] += y_expand;
                side[7 + 48] += y_expand;
                side[10 + 48] += y_expand;

                side[2 + 48] += z_expand;
                side[5 + 48] += z_expand;

                // bottom
                side[3 + 60] += x_expand;
                side[9 + 60] += x_expand;

                side[8 + 60] += z_expand;
                side[11 + 60] += z_expand;

                return side;
            }
        };

        public abstract float[] verts();

        public abstract float[] tex_coords(int u, int v);

        public abstract float[] translate(int x_pos, int y_pos, int z_pos);

        public abstract float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand);

        public static final SIDE[] values = values();
    }

    private static float[] translate_side(float[] side, int x_pos, int y_pos, int z_pos) {
        int size = side.length;

        for (int i = 0; i < size; i += 3) {
            side[i] += x_pos;
            side[i + 1] += y_pos;
            side[i + 2] += z_pos;
        }

        return side;
    }

    private static final float[] all_sides_vertices = new float[]{
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

    private static final float[] v1_vertex = new float[]{
            0, 1, 1, // v1
    };

    private static final float[] v2_vertex = new float[]{
            0, 0, 1, // v2
    };

    private static final float[] v3_vertex = new float[]{
            1, 0, 1, // v3
    };

    private static final float[] v4_vertex = new float[]{
            1, 1, 1, // v4
    };

    private static final float[] v5_vertex = new float[]{
            0, 1, 0, // v5
    };

    private static final float[] v6_vertex = new float[]{
            1, 1, 0, // v6
    };

    private static final float[] v7_vertex = new float[]{
            1, 0, 0, // v7
    };

    private static final float[] v8_vertex = new float[]{
            0, 0, 0, // v8
    };
}

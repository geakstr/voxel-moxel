package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

import java.util.Arrays;

/*
Box represents as vertices with {x,y,z} coords:

     5 {0,1,0}+-------+6 {1,1,0}
           .' |     .'|
 1 {0,1,1}+-------+'4 {1,1,1}
          |   |   |   |
     8 {0,0,0}+---|---+7 {1,0,0}
          | .'    | .'
 2 {0,0,1}+-------+'3 {1,0,1}

This is OpenGL coordinate system. All coordinates in project applied this
                   ↑ Y
                   |     /
                   |    /
                   |   /
                   |  /
                   | /
                   |/
-------------------•-------------------→ X
                  /|
                 / |
                /  |
               /   |
              /    |
             /     |
                           ↙  Z
*/

public class AABB extends MeshIndexed {
    public static final int side_size = 12, sides_size = 72;

    public Vector3f[] corners;

    public AABB(int x, int y, int z, int width, int height, int length) {
        super();

        this.corners = new Vector3f[] {
        		new Vector3f(x, y, z),
        		new Vector3f(x + width, y + height, z + length)
        };
        
        float[] d = new float[side_size * 1];
//        System.arraycopy(SIDE.BACK.translate_and_expand(x, y, z, width, height, length), 0, d, side_size * 0, side_size);
//        System.arraycopy(SIDE.FRONT.translate_and_expand(x, y, z, width, height, length), 0, d, side_size * 1, side_size);
//        System.arraycopy(SIDE.LEFT.translate_and_expand(x, y, z, width, height, length), 0, d, side_size * 2, side_size);
//        System.arraycopy(SIDE.RIGHT.translate_and_expand(x, y, z, width, height, length), 0, d, side_size * 3, side_size);
        System.arraycopy(SIDE.TOP.translate_and_expand(x, y, z, width, height, length), 0, d, side_size * 0, side_size);
//        System.arraycopy(SIDE.BOTTOM.translate_and_expand(x, y, z, width, height, length), 0, d, side_size * 5, side_size);
        this.update_data(d);
        this.update_gl_buffers();
    }

    public static enum SIDE {
        FRONT {
            public float[] verts() {
                return Arrays.copyOf(front_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(verts(), x, y, z);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[3] += x_expand;
            	side[9] += x_expand;
            	
            	side[7] += y_expand;
            	side[10] += y_expand;
            	
                return side;
            }
        },
        BACK {
            public float[] verts() {
                return Arrays.copyOf(back_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(verts(), x, y, z);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[0] += x_expand;
            	side[6] += x_expand;
            	
            	side[7] += y_expand;
            	side[10] += y_expand;
            	
            	side[2] += z_expand;
            	side[5] += z_expand;
            	side[8] += z_expand;
            	side[11] += z_expand;
            	
                return side;
            }
        },
        LEFT {
            public float[] verts() {
                return Arrays.copyOf(left_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(verts(), x, y, z);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_offset) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[7] += y_expand;
            	side[10] += y_expand;
            	
            	side[2] += z_offset;
            	side[8] += z_offset;
            	
            	return side;
            }
        },
        RIGHT {
            public float[] verts() {
                return Arrays.copyOf(right_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(verts(), x, y, z);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[0] += x_expand;
            	side[3] += x_expand;
            	side[6] += x_expand;
            	side[9] += x_expand;
            	
            	side[7] += y_expand;
            	side[10] += y_expand;
            	
            	side[5] += z_expand;
            	side[11] += z_expand;
            	
            	return side;
            }
        },
        TOP {
            public float[] verts() {
                return Arrays.copyOf(top_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(verts(), x, y, z);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[3] += x_expand;
            	side[9] += x_expand;
            	
            	side[1] += y_expand;
            	side[4] += y_expand;
            	side[7] += y_expand;
            	side[10] += y_expand;
            	
            	side[8] += z_expand;
            	side[11] += z_expand;
            	
            	return side;
            }
        },
        BOTTOM {
            public float[] verts() {
                return Arrays.copyOf(bottom_side_vertices, side_size);
            }

            public float[] translate(int x, int y, int z) {
                return translate_side(verts(), x, y, z);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[3] += x_expand;
            	side[9] += x_expand;
            	
            	side[2] += z_expand;
            	side[5] += z_expand;
            	
            	return side;
            }
        };

        public abstract float[] verts();

        public abstract float[] translate(int x_pos, int y_pos, int z_pos);

        public abstract float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand);
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

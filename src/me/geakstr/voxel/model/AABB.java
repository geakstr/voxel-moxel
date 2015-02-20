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
 
 
All works in CCW order


This is OpenGL coordinate system. All coordinates applied this
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
    public static final int side_vertices_size = 12, side_tex_coords_size = 8, sides_size = 72;

    public Vector3f[] corners;

    public AABB(int x, int y, int z, int width, int height, int length) {
        super();
        
        width--;
        height--;
        length--;

        this.corners = new Vector3f[] {
        		new Vector3f(x, y, z),
        		new Vector3f(x + width, y + height, z + length)
        };
        
        float[] d = new float[side_vertices_size * 6];
        System.arraycopy(SIDE.BACK.translate_and_expand(x, y, z, width, height, length), 0, d, side_vertices_size * 0, side_vertices_size);
        System.arraycopy(SIDE.FRONT.translate_and_expand(x, y, z, width, height, length), 0, d, side_vertices_size * 1, side_vertices_size);
        System.arraycopy(SIDE.LEFT.translate_and_expand(x, y, z, width, height, length), 0, d, side_vertices_size * 2, side_vertices_size);
        System.arraycopy(SIDE.RIGHT.translate_and_expand(x, y, z, width, height, length), 0, d, side_vertices_size * 3, side_vertices_size);
        System.arraycopy(SIDE.TOP.translate_and_expand(x, y, z, width, height, length), 0, d, side_vertices_size * 4, side_vertices_size);
        System.arraycopy(SIDE.BOTTOM.translate_and_expand(x, y, z, width, height, length), 0, d, side_vertices_size * 5, side_vertices_size);
        this.update_data(d);
        this.update_gl_buffers();
    }

    public static enum SIDE {
        FRONT {
            public float[] verts() {
                return Arrays.copyOf(front_side_vertices, side_vertices_size);
            }
            
            public float[] tex_coords(int u, int v) {
            	float[] texture = new float[side_tex_coords_size];
            	
            	texture[0] = -v;
                texture[5] = -1;

                texture[7] = 1;
                texture[8] = v;
                
                return texture;
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
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
            	float[] texture = new float[side_tex_coords_size];
            	
            	texture[0] = -v;
                texture[5] = -1;

                texture[7] = 1;
                texture[8] = v;
                
                return texture;
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
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
            	float[] texture = new float[side_tex_coords_size];
            	
            	texture[3] = 1;
                texture[4] = u;
                texture[5] = 1;

                texture[7] = -1;
                texture[8] = -u;
                texture[9] = -1;
                
                return texture;
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_offset) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[7] += y_expand;
            	side[10] += y_expand;
            	
            	side[5] += z_offset;
            	side[11] += z_offset;
            	
            	return side;
            }
        },
        RIGHT {
            public float[] verts() {
                return Arrays.copyOf(right_side_vertices, side_vertices_size);
            }
            
            public float[] tex_coords(int u, int v) {
            	float[] texture = new float[side_tex_coords_size];
            	
            	texture[3] = 1;
                texture[4] = u;
                texture[5] = 1;

                texture[7] = -1;
                texture[8] = -u;
                texture[9] = -1;
                
                return texture;
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
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
            	float[] texture = new float[side_tex_coords_size];
            	
            	texture[1] = v;
                texture[2] = u;

                texture[6] = -u;
                texture[11] = -v;
            	
            	return texture;
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
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
            	float[] texture = new float[side_tex_coords_size];
            	
            	texture[1] = v;
                texture[2] = u;

                texture[6] = -u;
                texture[11] = -v;
            	
            	return texture;
            }

            public float[] translate(int x_pos, int y_pos, int z_pos) {
                return translate_side(verts(), x_pos, y_pos, z_pos);
            }

            public float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand) {
            	float[] side = translate(x_pos, y_pos, z_pos);
            	
            	side[3] += x_expand;
            	side[9] += x_expand;
            	
            	side[8] += z_expand;
            	side[11] += z_expand;
            	
            	return side;
            }
        };

        public abstract float[] verts();
        
        public abstract float[] tex_coords(int u, int v);

        public abstract float[] translate(int x_pos, int y_pos, int z_pos);

        public abstract float[] translate_and_expand(int x_pos, int y_pos, int z_pos, int x_expand, int y_expand, int z_expand);
        
       // public static final SIDE values[] = values();
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

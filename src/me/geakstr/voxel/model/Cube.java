package me.geakstr.voxel.model;

import java.util.Arrays;

/*
Cube in our world is 32 bit integer

First 16 bits (from left to right) is a type of block (511 possible values)
Next 1 bit is a visibility (1 - vis, 0 - invis)
 */
public class Cube {
    public static final int cube_side_vertices_size = 18;
    public static final int cube_side_texture_size = 12;

    public static Integer[] get_side(int side_idx, int x0, int y0, int x1, int y1, int z) {
        Integer[] side = get_side(side_idx);

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

    public static Integer[] get_side(int side_idx) {
        Integer[] side = new Integer[cube_side_vertices_size];
        switch (side_idx) {
            case 0:
                System.arraycopy(back_vertices, 0, side, 0, cube_side_vertices_size);
                break;
            case 1:
                System.arraycopy(front_vertices, 0, side, 0, cube_side_vertices_size);
                break;
            case 2:
                System.arraycopy(right_vertices, 0, side, 0, cube_side_vertices_size);
                break;
            case 3:
                System.arraycopy(left_vertices, 0, side, 0, cube_side_vertices_size);
                break;
            case 4:
                System.arraycopy(bottom_vertices, 0, side, 0, cube_side_vertices_size);
                break;
            case 5:
                System.arraycopy(top_vertices, 0, side, 0, cube_side_vertices_size);
                break;
        }
        return side;
    }

    public static Integer[] get_texture(int side_idx, int x0, int y0, int x1, int y1) {
        Integer[] texture = new Integer[cube_side_texture_size];
        Arrays.fill(texture, 0);

        int xx = x1 - x0 + 1;
        int yy = y1 - y0 + 1;
        if (side_idx == 4 || side_idx == 5) {
            texture[1] = yy;
            texture[2] = xx;

            texture[6] = -xx;
            texture[11] = -yy;
        } else if (side_idx == 0 || side_idx == 1) {
            texture[0] = -yy;
            texture[5] = -1;

            texture[7] = 1;
            texture[8] = yy;
        } else if (side_idx == 2 || side_idx == 3) {
            texture[3] = 1;
            texture[4] = xx;
            texture[5] = 1;

            texture[7] = -1;
            texture[8] = -xx;
            texture[9] = -1;
        }
        return texture;
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

    public static final Integer[] right_vertices = new Integer[]{
            // 2 - 1 - 7
            0, 1, 1,
            0, 0, 1,
            1, 0, 1,

            // 4 - 2 - 7
            1, 1, 1,
            0, 1, 1,
            1, 0, 1
    };

    public static final Integer[] left_vertices = new Integer[]{
            // 3 - 0 - 5
            0, 1, 0,
            0, 0, 0,
            1, 0, 0,

            // 6 - 3 - 5
            1, 1, 0,
            0, 1, 0,
            1, 0, 0
    };

    public static final Integer[] front_vertices = new Integer[]{
            // 5 - 7 - 4
            1, 0, 0,
            1, 0, 1,
            1, 1, 1,

            // 5 - 4 - 6
            1, 0, 0,
            1, 1, 1,
            1, 1, 0
    };

    public static final Integer[] back_vertices = new Integer[]{
            // 0 - 1 - 2
            0, 0, 0,
            0, 0, 1,
            0, 1, 1,

            // 0 - 2 - 3
            0, 0, 0,
            0, 1, 1,
            0, 1, 0
    };

    public static final Integer[] bottom_vertices = new Integer[]{
            // 7 - 0 - 5
            1, 0, 1,
            0, 0, 0,
            1, 0, 0,

            // 7 - 1 - 0
            1, 0, 1,
            0, 0, 1,
            0, 0, 0
    };

    public static final Integer[] top_vertices = new Integer[]{
            // 4 - 3 - 6
            1, 1, 1,
            0, 1, 0,
            1, 1, 0,

            // 4 - 2 - 3
            1, 1, 1,
            0, 1, 1,
            0, 1, 0
    };
}
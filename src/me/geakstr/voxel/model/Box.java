package me.geakstr.voxel.model;

import java.util.Arrays;

public class Box {
    public static final int box_vertices_size = 108;
    public static final int box_side_vertices_size = 18;
    public static final int box_side_texture_size = 12;

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
        Integer[] side = new Integer[box_side_vertices_size];
        switch (side_idx) {
            case 0:
                System.arraycopy(back_vertices, 0, side, 0, box_side_vertices_size);
                break;
            case 1:
                System.arraycopy(front_vertices, 0, side, 0, box_side_vertices_size);
                break;
            case 2:
                System.arraycopy(right_vertices, 0, side, 0, box_side_vertices_size);
                break;
            case 3:
                System.arraycopy(left_vertices, 0, side, 0, box_side_vertices_size);
                break;
            case 4:
                System.arraycopy(bottom_vertices, 0, side, 0, box_side_vertices_size);
                break;
            case 5:
                System.arraycopy(top_vertices, 0, side, 0, box_side_vertices_size);
                break;
        }
        return side;
    }

    public static Integer[] get_box(int x, int y, int z, int width, int length, int height) {
        Integer[] box = new Integer[box_vertices_size];

        // 2 - 1 - 7
        box[0] = x * width;
        box[1] = z * height + height;
        box[2] = y * length + height;
        box[3] = x * width;
        box[4] = z * height;
        box[5] = y * length + height;
        box[6] = x * width + width;
        box[7] = z * height;
        box[8] = y * length + height;

        // 4 - 2 - 7
        box[9] = x * width + width;
        box[10] = z * height + height;
        box[11] = y * length + height;
        box[12] = x * width;
        box[13] = z * height + height;
        box[14] = y * length + height;
        box[15] = x * width + width;
        box[16] = z * height;
        box[17] = y * length + height;

        // 3 - 0 - 5
        box[18] = x * width;
        box[19] = z * height + height;
        box[20] = y * length;
        box[21] = x * width;
        box[22] = z * height;
        box[23] = y * length;
        box[24] = x * width + width;
        box[25] = z * height;
        box[26] = y * length;

        // 6 - 3 - 5
        box[27] = x * width + width;
        box[28] = z * height + height;
        box[29] = y * length;
        box[30] = x * width;
        box[31] = z * height + height;
        box[32] = y * length;
        box[33] = x * width + width;
        box[34] = z * height;
        box[35] = y * length;

        // 5 - 7 - 4
        box[36] = x * width + width;
        box[37] = z * height;
        box[38] = y * length;
        box[39] = x * width + width;
        box[40] = z * height;
        box[41] = y * length + height;
        box[42] = x * width + width;
        box[43] = z * height + height;
        box[44] = y * length + height;

        // 5 - 4 - 6
        box[45] = x * width + width;
        box[46] = z * height;
        box[47] = y * length;
        box[48] = x * width + width;
        box[49] = z * height + height;
        box[50] = y * length + height;
        box[51] = x * width + width;
        box[52] = z * height + height;
        box[53] = y * length;

        // 0 - 1 - 2
        box[54] = x * width;
        box[55] = z * height;
        box[56] = y * length;
        box[57] = x * width;
        box[58] = z * height;
        box[59] = y * length + height;
        box[60] = x * width;
        box[61] = z * height + height;
        box[62] = y * length + height;

        // 0 - 2 - 3
        box[63] = x * width;
        box[64] = z * height;
        box[65] = y * length;
        box[66] = x * width;
        box[67] = z * height + height;
        box[68] = y * length + height;
        box[69] = x * width;
        box[70] = z * height + height;
        box[71] = y * length;

        // 7 - 0 - 5
        box[72] = x * width + width;
        box[73] = z * height;
        box[74] = y * length + height;
        box[75] = x * width;
        box[76] = z * height;
        box[77] = y * length;
        box[78] = x * width + width;
        box[79] = z * height;
        box[80] = y * length;

        // 7 - 1 - 0
        box[81] = x * width + width;
        box[82] = z * height;
        box[83] = y * length + height;
        box[84] = x * width;
        box[85] = z * height;
        box[86] = y * length + height;
        box[87] = x * width;
        box[88] = z * height;
        box[89] = y * length;

        // 4 - 3 - 6
        box[90] = x * width + width;
        box[91] = z * height + height;
        box[92] = y * length + height;
        box[93] = x * width;
        box[94] = z * height + height;
        box[95] = y * length;
        box[96] = x * width + width;
        box[97] = z * height + height;
        box[98] = y * length;

        // 4 - 2 - 3
        box[99] = x * width + width;
        box[100] = z * height + height;
        box[101] = y * length + height;
        box[102] = x * width;
        box[103] = z * height + height;
        box[104] = y * length + height;
        box[105] = x * width;
        box[106] = z * height + height;
        box[107] = y * length;

        return box;
    }

    public static Integer[] get_texture(int side_idx, int x0, int y0, int x1, int y1) {
        Integer[] texture = new Integer[box_side_texture_size];
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

    public static final Integer[] box_vertices = new Integer[]{
            // 2 - 1 - 7
            0, 1, 1,
            0, 0, 1,
            1, 0, 1,

            // 4 - 2 - 7
            1, 1, 1,
            0, 1, 1,
            1, 0, 1,

            // 3 - 0 - 5
            0, 1, 0,
            0, 0, 0,
            1, 0, 0,

            // 6 - 3 - 5
            1, 1, 0,
            0, 1, 0,
            1, 0, 0,

            // 5 - 7 - 4
            1, 0, 0,
            1, 0, 1,
            1, 1, 1,

            // 5 - 4 - 6
            1, 0, 0,
            1, 1, 1,
            1, 1, 0,

            // 0 - 1 - 2
            0, 0, 0,
            0, 0, 1,
            0, 1, 1,

            // 0 - 2 - 3
            0, 0, 0,
            0, 1, 1,
            0, 1, 0,

            // 7 - 0 - 5
            1, 0, 1,
            0, 0, 0,
            1, 0, 0,

            // 7 - 1 - 0
            1, 0, 1,
            0, 0, 1,
            0, 0, 0,

            // 4 - 3 - 6
            1, 1, 1,
            0, 1, 0,
            1, 1, 0,

            // 4 - 2 - 3
            1, 1, 1,
            0, 1, 1,
            0, 1, 0
    };

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

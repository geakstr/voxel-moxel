package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.model.World;

public class Frustum {
    private static final float frustum[][] = new float[6][4];

    public static void update() {
        Matrix4f a = Matrix4f.mul(Camera.projection, Camera.view, null);

        // Extract the numbers for the Right plane
        frustum[0][0] = a.m03 - a.m00;
        frustum[0][1] = a.m13 - a.m10;
        frustum[0][2] = a.m23 - a.m20;
        frustum[0][3] = a.m33 - a.m30;

        // Left
        frustum[1][0] = a.m03 + a.m00;
        frustum[1][1] = a.m13 + a.m10;
        frustum[1][2] = a.m23 + a.m20;
        frustum[1][3] = a.m33 + a.m30;

        // Bottom
        frustum[2][0] = a.m03 + a.m01;
        frustum[2][1] = a.m13 + a.m11;
        frustum[2][2] = a.m23 + a.m21;
        frustum[2][3] = a.m33 + a.m31;

        // Top
        frustum[3][0] = a.m03 - a.m01;
        frustum[3][1] = a.m13 - a.m11;
        frustum[3][2] = a.m23 - a.m21;
        frustum[3][3] = a.m33 - a.m31;

        // Far
        frustum[4][0] = a.m03 - a.m02;
        frustum[4][1] = a.m13 - a.m12;
        frustum[4][2] = a.m23 - a.m22;
        frustum[4][3] = a.m33 - a.m32;

        // Near
        frustum[5][0] = a.m03 + a.m02;
        frustum[5][1] = a.m13 + a.m12;
        frustum[5][2] = a.m23 + a.m22;
        frustum[5][3] = a.m33 + a.m32;

        // Normalize
        for (int i = 0; i < 6; i++) {
            float t = (float) Math.sqrt(frustum[i][0] * frustum[i][0] + frustum[i][1] * frustum[i][1] + frustum[i][2] * frustum[i][2]);
            for (int j = 0; j < 4; j++) {
                frustum[i][j] /= t;
            }
        }
    }

    public static boolean pointInFrustum(float x, float y, float z) {
        for (int i = 0; i < 6; i++) {
            if (frustum[i][0] * x + frustum[i][1] * y + frustum[i][2] * z + frustum[i][3] <= 0.0F) {
                return false;
            }
        }

        return true;
    }

    public static boolean chunkInFrustum(float x, float y, float z) {

        float rot_y = Math.abs(Camera.rotation.y % 360);
        float rot_x = Camera.rotation.x;

        System.out.println(rot_x);

        int pos_x = (int) Camera.position.x;
        int pos_y = (int) Camera.position.y;
        int pos_z = (int) Camera.position.z;

        // 0 - right
        // 1 - top
        // 2 - left
        // 3 - bottom
        boolean[] hor_dirs = new boolean[4];
        if (rot_y >= 315 && rot_y <= 45) {
            hor_dirs[0] = true;
        } else if (rot_y >= 45 && rot_y <= 135) {
            hor_dirs[1] = true;
        } else if (rot_y >= 135 && rot_y <= 225) {
            hor_dirs[2] = true;
        } else if (rot_y >= 225 && rot_y <= 315) {
            hor_dirs[3] = true;
        }

        boolean[] vert_dirs = new boolean[2];


        int width = World.chunk_width;
        int length = World.chunk_length;
        int height = World.chunk_height;

        int half_width = width / 2;
        int half_length = length / 2;
        int half_height = height / 2;
        int half_half_height = half_height / 2;

        float x_mul_width = x * width;
        float y_mul_length = y * length;
        float z_mul_height = z * height;

        return pointInFrustum(x_mul_width - 1, z_mul_height - 1, y_mul_length - 1) ||
                pointInFrustum(x_mul_width - 1, z_mul_height - 1, y_mul_length + length + 1) ||
                pointInFrustum(x_mul_width + width + 1, z_mul_height - 1, y_mul_length - 1) ||
                pointInFrustum(x_mul_width + width + 1, z_mul_height - 1, y_mul_length + length + 1) ||

                pointInFrustum(x_mul_width - 1, half_height, y_mul_length - 1) ||
                pointInFrustum(x_mul_width - 1, half_height, y_mul_length + length + 1) ||
                pointInFrustum(x_mul_width + width + 1, half_height, y_mul_length - 1) ||
                pointInFrustum(x_mul_width + width + 1, half_height, y_mul_length + length + 1) ||

                pointInFrustum(x_mul_width - 1, z_mul_height + height + 1, y_mul_length - 1) ||
                pointInFrustum(x_mul_width - 1, z_mul_height + height + 1, y_mul_length + length + 1) ||
                pointInFrustum(x_mul_width + width + 1, z_mul_height + height + 1, y_mul_length - 1) ||
                pointInFrustum(x_mul_width + width + 1, z_mul_height + height + 1, y_mul_length + length + 1);
    }
}
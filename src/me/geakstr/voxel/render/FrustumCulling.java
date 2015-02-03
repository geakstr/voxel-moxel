package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.model.World;

public class FrustumCulling {
    private float frustum[][];

    public FrustumCulling() {
        frustum = new float[6][4];
    }

    public void update(final Matrix4f projection, final Matrix4f view) {
        Matrix4f a = Matrix4f.mul(projection, view, null);

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

    public boolean pointInFrustum(float x, float y, float z) {
        for (int i = 0; i < 6; i++) {
            if (frustum[i][0] * x + frustum[i][1] * y + frustum[i][2] * z + frustum[i][3] <= 0.0F) {
                return false;
            }
        }

        return true;
    }

    public boolean chunkInFrustum(float x, float y, float z) {
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

//                pointInFrustum(x_mul_width - 1, half_half_height, y_mul_length - 1) ||
//                pointInFrustum(x_mul_width - 1, half_half_height, y_mul_length + length + 1) ||
//                pointInFrustum(x_mul_width + width + 1, half_half_height, y_mul_length - 1) ||
//                pointInFrustum(x_mul_width + width + 1, half_half_height, y_mul_length + length + 1) ||
//
//                pointInFrustum(x_mul_width - 1, half_height, y_mul_length - 1) ||
//                pointInFrustum(x_mul_width - 1, half_height, y_mul_length + length + 1) ||
//                pointInFrustum(x_mul_width + width + 1, half_height, y_mul_length - 1) ||
//                pointInFrustum(x_mul_width + width + 1, half_height, y_mul_length + length + 1) ||
//
//                pointInFrustum(x_mul_width - 1, half_height + half_half_height, y_mul_length - 1) ||
//                pointInFrustum(x_mul_width - 1, half_height + half_half_height, y_mul_length + length + 1) ||
//                pointInFrustum(x_mul_width + width + 1, half_height + half_half_height, y_mul_length - 1) ||
//                pointInFrustum(x_mul_width + width + 1, half_height + half_half_height, y_mul_length + length + 1) ||

                pointInFrustum(x_mul_width - 1, z_mul_height + height + 1, y_mul_length - 1) ||
                pointInFrustum(x_mul_width - 1, z_mul_height + height + 1, y_mul_length + length + 1) ||
                pointInFrustum(x_mul_width + width + 1, z_mul_height + height + 1, y_mul_length - 1) ||
                pointInFrustum(x_mul_width + width + 1, z_mul_height + height + 1, y_mul_length + length + 1);
    }
}
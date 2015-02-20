package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.model.Chunk;
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
        if (World.nearest_chunks.size() == 0) {
            return true;
        }
        for (Chunk chunk : World.nearest_chunks) {
            if (chunk.x_chunk_pos == x && chunk.y_chunk_pos == y && chunk.z_chunk_pos == z) {
                return true;
            }
        }

        int size = World.chunk_size;
        int height = World.chunk_height;

        int half_height = height / 2;

        float x_mul_size = x * size;
        float y_mul_height = y * height;
        float z_mul_size = z * size;

        return pointInFrustum(x_mul_size - 1, y_mul_height - 1, z_mul_size - 1) ||
                pointInFrustum(x_mul_size - 1, y_mul_height - 1, z_mul_size + size + 1) ||
                pointInFrustum(x_mul_size + size + 1, y_mul_height - 1, z_mul_size - 1) ||
                pointInFrustum(x_mul_size + size + 1, y_mul_height - 1, z_mul_size + size + 1) ||

                pointInFrustum(x_mul_size - 1, y_mul_height + half_height, z_mul_size - 1) ||
                pointInFrustum(x_mul_size - 1, y_mul_height + half_height, z_mul_size + size + 1) ||
                pointInFrustum(x_mul_size + size + 1, y_mul_height + half_height, z_mul_size - 1) ||
                pointInFrustum(x_mul_size + size + 1, y_mul_height + half_height, z_mul_size + size + 1) ||

                pointInFrustum(x_mul_size - 1, y_mul_height + height + 1, z_mul_size - 1) ||
                pointInFrustum(x_mul_size - 1, y_mul_height + height + 1, z_mul_size + size + 1) ||
                pointInFrustum(x_mul_size + size + 1, y_mul_height + height + 1, z_mul_size - 1) ||
                pointInFrustum(x_mul_size + size + 1, y_mul_height + height + 1, z_mul_size + size + 1);
    }
}
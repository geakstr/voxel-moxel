package me.geakstr.voxel.util;

import me.geakstr.voxel.math.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class MatrixUtil {
    public static FloatBuffer toFloatBuffer(Matrix4f mat) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.store(buffer);
        buffer.flip();
        return buffer;
    }
}

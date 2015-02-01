package me.geakstr.voxel.util;

import me.geakstr.voxel.math.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ExtendedBufferUtil {
    public static FloatBuffer create_flipped_buffer(float[] values) {
        FloatBuffer ret = BufferUtils.createFloatBuffer(values.length);
        ret.put(values);
        ret.flip();
        return ret;
    }

    public static IntBuffer create_flipped_buffer(int[] values) {
        IntBuffer ret = BufferUtils.createIntBuffer(values.length);
        ret.put(values);
        ret.flip();
        return ret;
    }

    public static FloatBuffer create_flipped_buffer(Matrix4f mat) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.store(buffer);
        buffer.flip();
        return buffer;
    }
}

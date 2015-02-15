package me.geakstr.voxel.util;

import me.geakstr.voxel.math.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class ExtendedBufferUtil {
    public static FloatBuffer create_flipped_buffer(float[] values) {
        FloatBuffer ret = BufferUtils.createFloatBuffer(values.length);
        for (Float val : values) {
            ret.put(val);
        }
        ret.flip();
        return ret;
    }

    public static IntBuffer create_flipped_buffer(int[] values) {
        IntBuffer ret = BufferUtils.createIntBuffer(values.length);
        for (Integer val : values) {
            ret.put(val);
        }
        ret.flip();
        return ret;
    }

    public static ByteBuffer create_flipped_byte_buffer(List<Float> values) {
        ByteBuffer ret = BufferUtils.createByteBuffer(values.size() * 4);
        for (Float val : values) {
            ret.putFloat(val);
        }
        ret.flip();
        return ret;
    }

    public static ByteBuffer create_flipped_byte_buffer(float[] values) {
        ByteBuffer ret = BufferUtils.createByteBuffer(values.length * 4);
        for (Float val : values) {
            ret.putFloat(val);
        }
        ret.flip();
        return ret;
    }

    public static ByteBuffer create_flipped_byte_buffer(int[] values) {
        ByteBuffer ret = BufferUtils.createByteBuffer(values.length * 4);
        for (Integer val : values) {
            ret.putInt(val);
        }
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

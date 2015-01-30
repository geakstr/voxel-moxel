package me.geakstr.voxel.util;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.model.Vertex;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderUtil {
    public static FloatBuffer create_flipped_buffer(Vertex[] vertices) {
        FloatBuffer ret = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);
        for (Vertex v : vertices) {
            ret.put(v.coord.x);
            ret.put(v.coord.y);
            ret.put(v.coord.z);
        }
        ret.flip();
        return ret;
    }

    public static IntBuffer create_flipped_buffer(int[] values) {
        IntBuffer ret = BufferUtils.createIntBuffer(values.length);
        ret.put(values);
        ret.flip();
        return ret;
    }

    public static FloatBuffer create_flipped_buffer(Matrix4f value) {
        FloatBuffer ret = BufferUtils.createFloatBuffer(16);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ret.put(value.get(i, j));
            }
        }
        ret.flip();
        return ret;
    }
}

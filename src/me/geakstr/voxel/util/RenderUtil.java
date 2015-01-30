package me.geakstr.voxel.util;

import me.geakstr.voxel.model.Vertex;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

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
}

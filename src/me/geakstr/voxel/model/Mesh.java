package me.geakstr.voxel.model;

import me.geakstr.voxel.core.Configurator;
import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

@Deprecated
public class Mesh {
    public ByteBuffer data;
    public int verts_size;

    private static final int mapping_flags = GL_MAP_WRITE_BIT | (Configurator.unsynchronized_buffering ? GL_MAP_UNSYNCHRONIZED_BIT : GL_MAP_INVALIDATE_RANGE_BIT);
    private static final int initial_capacity = 512;

    private int capacity;
    private int vao, vbo;

    public Mesh() {
        this.verts_size = 0;

        this.capacity = initial_capacity;

        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();

        this.init_vbo();
        this.init_vao();
    }

    public static void bind_texture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void update_vbo() {
        int data_size = data.capacity();

        boolean orphan = false;
        while (data_size > capacity) {
            capacity *= 2;
            orphan = true;
        }
        if (orphan) {
            init_vbo();
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glMapBufferRange(GL_ARRAY_BUFFER, 0, data_size, mapping_flags).put(data);
        glUnmapBuffer(GL_ARRAY_BUFFER);
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, verts_size);
        glBindVertexArray(0);
    }

    public void update_data(float[] verts, float[] tex_coords, float[] tex_coords_offsets, float[] colors) {
        verts_size = verts.length / 3;
        data = BufferUtils.createByteBuffer(verts.length * 8 + tex_coords.length * 4 + tex_coords_offsets.length * 4 + colors.length * 4);
        for (int v = 0, t = 0, to = 0, c = 0; v < verts.length; v += 3, t += 2, to += 2, c += 3) {
            data.putFloat(verts[v]);
            data.putFloat(verts[v + 1]);
            data.putFloat(verts[v + 2]);

            data.putFloat(tex_coords[t]);
            data.putFloat(tex_coords[t + 1]);

            data.putFloat(tex_coords_offsets[to]);
            data.putFloat(tex_coords_offsets[to + 1]);

            data.putFloat(colors[c]);
            data.putFloat(colors[c + 1]);
            data.putFloat(colors[c + 2]);
        }
        data.flip();
    }

    public void update_data(List<Float> verts, List<Float> tex_coords, List<Float> tex_coords_offsets, List<Float> colors) {
        verts_size = verts.size() / 3;
        data = BufferUtils.createByteBuffer(verts.size() * 8 + tex_coords.size() * 4 + tex_coords_offsets.size() * 4 + colors.size() * 4);
        for (int v = 0, t = 0, to = 0, c = 0; v < verts.size(); v += 3, t += 2, to += 2, c += 3) {
            data.putFloat(verts.get(v));
            data.putFloat(verts.get(v + 1));
            data.putFloat(verts.get(v + 2));

            data.putFloat(tex_coords.get(t));
            data.putFloat(tex_coords.get(t + 1));

            data.putFloat(tex_coords_offsets.get(to));
            data.putFloat(tex_coords_offsets.get(to + 1));

            data.putFloat(colors.get(c));
            data.putFloat(colors.get(c + 1));
            data.putFloat(colors.get(c + 2));
        }
        data.flip();
    }

    public void update_data(List<int[]> faces, List<Vector3f> verts, List<Vector2f> tex_coords) {
        verts_size = faces.size() * 3;
        data = BufferUtils.createByteBuffer(faces.size() * 30 * 4);
        for (int[] face : faces) {
            for (int i = 0; i < 3; i++) {
                Vector3f vert = verts.get(face[i]);
                Vector2f tex_coord = tex_coords.get(face[i + 3]);

                data.putFloat(vert.x);
                data.putFloat(vert.y);
                data.putFloat(vert.z);

                data.putFloat(tex_coord.x);
                data.putFloat(tex_coord.y);

                data.putFloat(0);
                data.putFloat(0);

                data.putFloat(1);
                data.putFloat(1);
                data.putFloat(1);
            }
        }
        data.flip();
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }

    private void init_vbo() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, capacity, null, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void init_vao() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_coord"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_offset"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));

        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_FLOAT, false, 40, 0);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_coord"), 2, GL_FLOAT, false, 40, 12);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 40, 20);
        glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, 40, 28);

        glBindVertexArray(0);
    }
}

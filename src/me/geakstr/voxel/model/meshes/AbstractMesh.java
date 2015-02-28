package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.core.Configurator;
import me.geakstr.voxel.game.Game;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public abstract class AbstractMesh {
    public int faces_counter;

    public ByteBuffer vbo_data;

    protected int vbo_capacity;
    protected int vao, vbo;

    protected static final int mapping_flags = GL_MAP_WRITE_BIT | (Configurator.unsynchronized_buffering ? GL_MAP_UNSYNCHRONIZED_BIT : GL_MAP_INVALIDATE_RANGE_BIT);
    protected static final int initial_capacity = 512;

    public AbstractMesh() {
        this.faces_counter = 0;

        this.vbo_capacity = initial_capacity;

        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();

        this.init_vbo();
    }

    public static void bind_texture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void update_gl_buffers() {
        int vbo_data_size = vbo_data.capacity();
        boolean vbo_orphan = false;
        while (vbo_data_size > vbo_capacity) {
            vbo_capacity *= 2;
            vbo_orphan = true;
        }
        if (vbo_orphan) {
            init_vbo();
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glMapBufferRange(GL_ARRAY_BUFFER, 0, vbo_data_size, mapping_flags).put(vbo_data);
        glUnmapBuffer(GL_ARRAY_BUFFER);
    }

    public void update_gl_data(int[] vertices, int[] tex_coords, float[] tex_coords_offsets, float[] colors) {
        this.vbo_data = BufferUtils.createByteBuffer(vertices.length * 4 + tex_coords.length * 4 + tex_coords_offsets.length * 4 + colors.length * 4);
        for (int v = 0, t = 0, to = 0, c = 0; v < vertices.length; v += 3, t += 2, to += 2, c += 3) {
            vbo_data.putInt(vertices[v]);
            vbo_data.putInt(vertices[v + 1]);
            vbo_data.putInt(vertices[v + 2]);

            vbo_data.putInt(tex_coords[t]);
            vbo_data.putInt(tex_coords[t + 1]);

            vbo_data.putFloat(tex_coords_offsets[to]);
            vbo_data.putFloat(tex_coords_offsets[to + 1]);

            vbo_data.putFloat(colors[c]);
            vbo_data.putFloat(colors[c + 1]);
            vbo_data.putFloat(colors[c + 2]);
        }
        vbo_data.flip();
    }

    public void update_gl_data(List<Integer> vertices, List<Integer> tex_coords, List<Float> tex_coords_offsets, List<Float> colors) {
        this.vbo_data = BufferUtils.createByteBuffer(vertices.size() * 4 + tex_coords.size() * 4 + tex_coords_offsets.size() * 4 + colors.size() * 4);
        for (int v = 0, t = 0, to = 0, c = 0; v < vertices.size(); v += 3, t += 2, to += 2, c += 3) {
            vbo_data.putInt(vertices.get(v));
            vbo_data.putInt(vertices.get(v + 1));
            vbo_data.putInt(vertices.get(v + 2));

            vbo_data.putInt(tex_coords.get(t));
            vbo_data.putInt(tex_coords.get(t + 1));

            vbo_data.putFloat(tex_coords_offsets.get(to));
            vbo_data.putFloat(tex_coords_offsets.get(to + 1));

            vbo_data.putFloat(colors.get(c));
            vbo_data.putFloat(colors.get(c + 1));
            vbo_data.putFloat(colors.get(c + 2));
        }
        vbo_data.flip();
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }

    public abstract void draw();

    protected void init_vbo() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vbo_capacity, null, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    protected void init_vao() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        int bytes = 0;
        if (Game.current_shader.has_attr("attr_pos")) {
            glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
            bytes += 12;
        }
        if (Game.current_shader.has_attr("attr_tex_coord")) {
            glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_coord"));
            bytes += 8;
        }
        if (Game.current_shader.has_attr("attr_tex_offset")) {
            glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_offset"));
            bytes += 8;
        }
        if (Game.current_shader.has_attr("attr_color")) {
            glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));
            bytes += 12;
        }

        int bytes_offset = 0;
        if (Game.current_shader.has_attr("attr_pos")) {
            glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_INT, false, bytes, bytes_offset);
            bytes_offset += 12;
        }
        if (Game.current_shader.has_attr("attr_tex_coord")) {
            glVertexAttribPointer(Game.current_shader.attr("attr_tex_coord"), 2, GL_INT, false, bytes, bytes_offset);
            bytes_offset += 8;
        }
        if (Game.current_shader.has_attr("attr_tex_offset")) {
            glVertexAttribPointer(Game.current_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, bytes, bytes_offset);
            bytes_offset += 8;
        }
        if (Game.current_shader.has_attr("attr_color")) {
            glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, bytes, bytes_offset);
            bytes_offset += 12;
        }


    }
}

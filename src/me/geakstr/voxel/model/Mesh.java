package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    public ByteBuffer data;
    public int verts_size;

    private static final int mapping_flags = GL_MAP_WRITE_BIT | GL_MAP_INVALIDATE_RANGE_BIT;
    private static final int initial_capacity = 512;

    private int capacities[];
    private int buffer_count, cur_buffer_idx;

    private int[] vaos, vbos;

    public Mesh() {
        this.buffer_count = 1;
        this.cur_buffer_idx = 0;

        this.verts_size = 0;
        this.capacities = new int[buffer_count];

        this.vaos = new int[buffer_count];
        this.vbos = new int[buffer_count];

        for (int buffer_idx = 0; buffer_idx < buffer_count; buffer_idx++) {
            capacities[buffer_idx] = initial_capacity;
            vaos[buffer_idx] = glGenVertexArrays();
            vbos[buffer_idx] = glGenBuffers();
            this.init_vbo(buffer_idx);
            this.init_vao(buffer_idx);
        }
    }

    public static void bind_texture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void update_vbo() {
        int buffer_idx = (cur_buffer_idx + buffer_count - 1) % buffer_count;

        int data_size = data.capacity();

        boolean orphan = false;
        while (data_size > capacities[buffer_idx]) {
            capacities[buffer_idx] *= 2;
            orphan = true;
        }
        if (orphan) {
            init_vbo(buffer_idx);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbos[buffer_idx]);
        glMapBufferRange(GL_ARRAY_BUFFER, 0, capacities[buffer_idx], mapping_flags).put(data);
        glUnmapBuffer(GL_ARRAY_BUFFER);
    }

    public void draw() {
        glBindVertexArray(vaos[cur_buffer_idx]);
        glDrawArrays(GL_TRIANGLES, 0, verts_size);
        glBindVertexArray(0);
        cur_buffer_idx = (cur_buffer_idx + 1) % buffer_count;
    }

    public void update_data(int[] verts, int[] tex, float[] tex_off, float[] colors) {
        verts_size = verts.length / 3;
        data = BufferUtils.createByteBuffer(verts.length * 4 + tex.length * 4 + tex_off.length * 4 + colors.length * 4);
        for (int v = 0, t = 0, to = 0, c = 0; v < verts.length; v += 3, t += 2, to += 2, c += 3) {
            data.putFloat(verts[v]);
            data.putFloat(verts[v + 1]);
            data.putFloat(verts[v + 2]);

            data.putFloat(tex[t]);
            data.putFloat(tex[t + 1]);

            data.putFloat(tex_off[to]);
            data.putFloat(tex_off[to + 1]);
            data.putFloat(colors[c]);
            data.putFloat(colors[c + 1]);
            data.putFloat(colors[c + 2]);
        }
        data.flip();
    }

    public void update_data(List<Integer> verts, List<Integer> tex, List<Float> tex_off, List<Float> colors) {
        verts_size = verts.size() / 3;
        data = BufferUtils.createByteBuffer(verts.size() * 4 + tex.size() * 4 + tex_off.size() * 4 + colors.size() * 4);
        for (int v = 0, t = 0, to = 0, c = 0; v < verts.size(); v += 3, t += 2, to += 2, c += 3) {
            data.putFloat(verts.get(v));
            data.putFloat(verts.get(v + 1));
            data.putFloat(verts.get(v + 2));

            data.putFloat(tex.get(t));
            data.putFloat(tex.get(t + 1));

            data.putFloat(tex_off.get(to));
            data.putFloat(tex_off.get(to + 1));
            data.putFloat(colors.get(c));
            data.putFloat(colors.get(c + 1));
            data.putFloat(colors.get(c + 2));
        }
        data.flip();
    }

    public void destroy() {
        for (int buffer_idx = 0; buffer_idx < buffer_count; buffer_idx++) {
            glDeleteVertexArrays(vaos[buffer_idx]);
            glDeleteBuffers(vbos[buffer_idx]);
        }
    }

    private void init_vbo(int buffer_idx) {
        glBindBuffer(GL_ARRAY_BUFFER, vbos[buffer_idx]);
        glBufferData(GL_ARRAY_BUFFER, capacities[buffer_idx], null, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void init_vao(int buffer_idx) {
        glBindVertexArray(vaos[buffer_idx]);
        glBindBuffer(GL_ARRAY_BUFFER, vbos[buffer_idx]);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_coord"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_offset"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));

        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_FLOAT, false, 40, 0);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_coord"), 2, GL_FLOAT, false, 40, 12);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 40, 20);
        glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, 40, 28);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}

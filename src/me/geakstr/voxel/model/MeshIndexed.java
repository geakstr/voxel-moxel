package me.geakstr.voxel.model;

import me.geakstr.voxel.core.Configurator;
import me.geakstr.voxel.game.Game;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class MeshIndexed {
    public int count;

    public ByteBuffer vbo_data, ibo_data;

    private static final int mapping_flags = GL_MAP_WRITE_BIT | (Configurator.unsynchronized_buffering ? GL_MAP_UNSYNCHRONIZED_BIT : GL_MAP_INVALIDATE_RANGE_BIT);
    private static final int vbo_initial_capacity = 512, ibo_initial_capacity = 256;

    private int vbo_capacity, ibo_capacity;
    private int vao, vbo, ibo;

    public MeshIndexed() {
        this.count = 0;

        this.vbo_capacity = vbo_initial_capacity;
        this.ibo_capacity = ibo_initial_capacity;

        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();
        this.ibo = glGenBuffers();

        this.init_vbo();
        this.init_ibo();
        this.init_vao();
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

        int ibo_data_size = ibo_data.capacity();
        boolean ibo_orphan = false;
        while (ibo_data_size > ibo_capacity) {
            ibo_capacity *= 2;
            ibo_orphan = true;
        }
        if (ibo_orphan) {
            init_ibo();
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glMapBufferRange(GL_ARRAY_BUFFER, 0, vbo_data_size, mapping_flags).put(vbo_data);
        glUnmapBuffer(GL_ARRAY_BUFFER);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glMapBufferRange(GL_ELEMENT_ARRAY_BUFFER, 0, ibo_data_size, mapping_flags).put(ibo_data);
        glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
    }

    public void update_data(float[] vertices) {
        this.count = vertices.length / 2;
        this.vbo_data = BufferUtils.createByteBuffer(vertices.length * 4);
        this.ibo_data = BufferUtils.createByteBuffer(count * 4);

        for (int i = 0, bytes = 0; bytes < count; i += 4, bytes += 6) {
            ibo_data.putInt(i);
            ibo_data.putInt(i + 1);
            ibo_data.putInt(i + 2);

            ibo_data.putInt(i + 1);
            ibo_data.putInt(i + 3);
            ibo_data.putInt(i + 2);
        }

        System.out.println(ibo_data.toString());
        for (float coord : vertices) {
            vbo_data.putFloat(coord);
        }
        ibo_data.flip();
        vbo_data.flip();
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }

    private void init_vbo() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vbo_capacity, null, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void init_ibo() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo_capacity, null, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void init_vao() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_FLOAT, false, 12, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

        glBindVertexArray(0);
    }
}

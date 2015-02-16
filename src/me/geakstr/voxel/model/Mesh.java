package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private static final int mapping_flags = GL_MAP_WRITE_BIT | GL_MAP_UNSYNCHRONIZED_BIT;
    private static final int initial_capacity = 512;

    private int capacities[];
    private int buffer_count, cur_buffer_idx;

    private int[] vaos, vbos;

    public Mesh() {
        this.buffer_count = 1;
        this.cur_buffer_idx = 0;

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

    public void update_vbo(float[] data) {
        int buffer_idx = (cur_buffer_idx + buffer_count - 1) % buffer_count;

        int data_size = data.length * 4;

        boolean orphan = false;
        while (data_size > capacities[buffer_idx]) {
            capacities[buffer_idx] *= 2;
            orphan = true;
        }
        if (orphan) {
            init_vbo(buffer_idx);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbos[buffer_idx]);
        glMapBufferRange(GL_ARRAY_BUFFER, 0, data_size, mapping_flags).put(ExtendedBufferUtil.create_flipped_byte_buffer(data));
        glUnmapBuffer(GL_ARRAY_BUFFER);
    }

    public void draw(int count) {
        glBindVertexArray(vaos[cur_buffer_idx]);
        glDrawArrays(GL_TRIANGLES, 0, count);
        glBindVertexArray(0);
        cur_buffer_idx = (cur_buffer_idx + 1) % buffer_count;
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

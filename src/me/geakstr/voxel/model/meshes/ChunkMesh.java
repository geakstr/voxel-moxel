package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class ChunkMesh {
    public float[] data;

    public int vao;
    public int vbo;

    private int capacity = 64;

    public ChunkMesh() {
        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();

        this.init_vbo();
        this.init_vao();
    }

    public void update(float[] data) {
        int bytes = data.length * 4;
        boolean was = false;
        while (bytes > capacity) {
            capacity *= 2;
            was = true;
        }
        if (was) {
            init_vbo();
        }
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, bytes, ExtendedBufferUtil.create_flipped_byte_buffer(data));
    }

    private void init_vbo() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, capacity, null, GL_DYNAMIC_DRAW);
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

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }


    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }
}

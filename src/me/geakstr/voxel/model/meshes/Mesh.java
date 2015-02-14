package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    public int[] verts;

    public int vao;
    public int vbo; // vertices buffer

    public Mesh() {
        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();
    }

    public Mesh prepare(int[] verts) {
        this.init_vbo(verts);
        return this;
    }

    public Mesh init_vbo(int[] verts) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verts.length * 4, ExtendedBufferUtil.create_flipped_byte_buffer(verts), GL_STREAM_DRAW);

        this.bind_vao();
        this.init_vao();
        this.unbind_vao();

        return this;
    }

    public Mesh init_vao() {
        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);

        return this;
    }

    public void bind_vao(int vao) {
        glBindVertexArray(vao);
    }

    public void bind_vao() {
        this.bind_vao(vao);
    }

    public void unbind_vao() {
        glBindVertexArray(0);
    }

    public static void bind_texture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }


    public void destroy() {}
}

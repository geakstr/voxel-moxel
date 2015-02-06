package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;
import me.geakstr.voxel.util.ResourceUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    public int[] vertices;
    public int[] textures;
    public float[] textures_offsets;

    public int vertices_size;
    public int textures_size;
    public int textures_offsets_size;

    public int vao;
    public int vbo; // vertex buffer
    public int tbo; // texture buffer
    public int tobo; // texture offset buffer

    public Mesh() {}

    public Mesh(int[] vertices, int[] textures, float[] textures_offsets) {
        this.vertices = vertices;
        this.vertices_size = vertices.length;
        this.textures = textures;
        this.textures_size = textures.length;
        this.textures_offsets = textures_offsets;
        this.textures_offsets_size = textures_offsets.length;
    }

    public void gen_buffers() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        tbo = glGenBuffers();
        tobo = glGenBuffers();
    }

    public void prepare_render() {
        init_vbo();
        init_vao();
    }

    public void init_vbo() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(vertices), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(textures), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tobo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(textures_offsets), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void init_vao() {
        glBindVertexArray(vao);

        glBindTexture(GL_TEXTURE_2D, ResourceUtil.texture_id("atlas.png"));

        glEnableVertexAttribArray(Game.world_shader.attr("attr_pos"));
        glEnableVertexAttribArray(Game.world_shader.attr("attr_tex_offset"));
        glEnableVertexAttribArray(Game.world_shader.attr("attr_tex_coord"));

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(Game.world_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glVertexAttribPointer(Game.world_shader.attr("attr_tex_coord"), 2, GL_INT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tobo);
        glVertexAttribPointer(Game.world_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertices_size);
        glBindVertexArray(0);
    }
}

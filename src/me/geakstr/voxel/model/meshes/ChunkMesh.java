package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.World;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class ChunkMesh {
    public static final int verts_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 108 * 4 + 4;
    public static final int tex_coords_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 72 * 4 + 4;
    public static final int tex_coords_offsets_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 72 * 4 + 4;
    public static final int colors_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 108 * 4 + 4;

    public ByteBuffer verts;
    public ByteBuffer colors;
    public ByteBuffer tex_coords;
    public ByteBuffer tex_coords_offsets;

    public int vao;
    public int verts_vbo;
    public int colors_vbo;
    public int tex_coords_vbo;
    public int tex_coords_offsets_vbo;

    public ChunkMesh() {
        this.init_vbos();
        this.init_vao();
    }

    private void init_vbos() {
        this.verts_vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, verts_vbo);
        glBufferData(GL_ARRAY_BUFFER, verts_size, null, GL_DYNAMIC_DRAW);
        this.verts = glMapBufferRange(GL_ARRAY_BUFFER, 0, verts_size, GL_MAP_WRITE_BIT | GL_MAP_UNSYNCHRONIZED_BIT);

        this.colors_vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colors_vbo);
        glBufferData(GL_ARRAY_BUFFER, colors_size, null, GL_DYNAMIC_DRAW);
        this.colors = glMapBufferRange(GL_ARRAY_BUFFER, 0, colors_size, GL_MAP_WRITE_BIT | GL_MAP_UNSYNCHRONIZED_BIT);

        this.tex_coords_vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_vbo);
        glBufferData(GL_ARRAY_BUFFER, tex_coords_size, null, GL_DYNAMIC_DRAW);
        this.tex_coords = glMapBufferRange(GL_ARRAY_BUFFER, 0, tex_coords_size, GL_MAP_WRITE_BIT | GL_MAP_UNSYNCHRONIZED_BIT);

        this.tex_coords_offsets_vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_offsets_vbo);
        glBufferData(GL_ARRAY_BUFFER, tex_coords_offsets_size, null, GL_DYNAMIC_DRAW);
        this.tex_coords_offsets = glMapBufferRange(GL_ARRAY_BUFFER, 0, tex_coords_offsets_size, GL_MAP_WRITE_BIT | GL_MAP_UNSYNCHRONIZED_BIT);

        //glUnmapBuffer(GL_ARRAY_BUFFER);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void init_vao() {
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glBindBuffer(GL_ARRAY_BUFFER, verts_vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));
        glBindBuffer(GL_ARRAY_BUFFER, colors_vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_coord"));
        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_coord"), 2, GL_INT, false, 0, 0);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_offset"));
        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_offsets_vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);

        glDeleteBuffers(verts_vbo);
        glDeleteBuffers(colors_vbo);
        glDeleteBuffers(colors_vbo);
        glDeleteBuffers(tex_coords_offsets_vbo);
    }
}

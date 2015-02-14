package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class ChunkMesh {
    public static final int verts_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 108 * 4;
    public static final int tex_coords_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 72 * 4;
    public static final int tex_coords_offsets_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 72 * 4;
    public static final int colors_size = (World.chunk_width / 2 * World.chunk_length / 2 * World.chunk_height / 2) * 108 * 4;

    public int[] verts;
    public float[] colors;
    public int[] tex_coords;
    public float[] tex_coords_offsets;

    public int vao;
    public int verts_vbo;
    public int colors_vbo;
    public int tex_coords_vbo;
    public int tex_coords_offsets_vbo;

    public ChunkMesh() {
        this.verts = new int[0];
        this.colors = new float[0];
        this.tex_coords = new int[0];
        this.tex_coords_offsets = new float[0];


        this.vao = glGenVertexArrays();

        this.verts_vbo = glGenBuffers();
        this.colors_vbo = glGenBuffers();
        this.tex_coords_vbo = glGenBuffers();
        this.tex_coords_offsets_vbo = glGenBuffers();


        glBindBuffer(GL_ARRAY_BUFFER, verts_vbo);
        glBufferData(GL_ARRAY_BUFFER, verts_size, null, GL_STREAM_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, colors_vbo);
        glBufferData(GL_ARRAY_BUFFER, colors_size, null, GL_STREAM_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_vbo);
        glBufferData(GL_ARRAY_BUFFER, tex_coords_size, null, GL_STREAM_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_offsets_vbo);
        glBufferData(GL_ARRAY_BUFFER, tex_coords_offsets_size, null, GL_STREAM_DRAW);


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

        glBindVertexArray(0);
    }

    public void update(int[] verts, float[] colors, int[] tex, float[] tex_off) {
        glBindBuffer(GL_ARRAY_BUFFER, verts_vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, ExtendedBufferUtil.create_flipped_byte_buffer(verts));

        glBindBuffer(GL_ARRAY_BUFFER, colors_vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, ExtendedBufferUtil.create_flipped_byte_buffer(colors));

        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, ExtendedBufferUtil.create_flipped_byte_buffer(tex));

        glBindBuffer(GL_ARRAY_BUFFER, tex_coords_offsets_vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, ExtendedBufferUtil.create_flipped_byte_buffer(tex_off));
    }

    public void destroy() {
        glDeleteVertexArrays(vao);

        glDeleteBuffers(verts_vbo);
        glDeleteBuffers(colors_vbo);
        glDeleteBuffers(colors_vbo);
        glDeleteBuffers(tex_coords_offsets_vbo);
    }
}

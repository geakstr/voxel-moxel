package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;
import me.geakstr.voxel.util.ResourceUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    public Integer[] vertices;
    public Integer[] textures;
    public Float[] textures_offsets;
    public Float[] colors;

    public int vertices_size;
    public int textures_size;
    public int textures_offsets_size;
    public int colors_size;

    public int terrain_vao;
    public int terrain_vert_buf;
    public int terrain_tex_buf;
    public int terrain_tex_offset_buf;
    public int terrain_color_buf;

    public int occlusion_vao;
    public int occlusion_vert_buf;

    public int occlusion_query;

    public Mesh() {
        this.vertices = new Integer[]{};
        this.textures = new Integer[]{};
        this.textures_offsets = new Float[]{};
        this.colors = new Float[]{};

        this.vertices_size = 0;
        this.textures_size = 0;
        this.textures_offsets_size = 0;
        this.colors_size = 0;
    }

    public void gen_buffers() {
        terrain_vao = glGenVertexArrays();
        terrain_vert_buf = glGenBuffers();
        terrain_tex_buf = glGenBuffers();
        terrain_tex_offset_buf = glGenBuffers();
        terrain_color_buf = glGenBuffers();

        if (Game.occlusion) {
            occlusion_vao = glGenVertexArrays();
            occlusion_vert_buf = glGenBuffers();
            occlusion_query = glGenQueries();
        }
    }

    public void prepare_render(Integer[] terrain_vert, Integer[] terrain_tex, Float[] terrain_tex_offset, Float[] terrain_color, Integer[] box) {
        init_terrain_vbo(terrain_vert, terrain_tex, terrain_tex_offset, terrain_color);
        init_terrain_vao();

        if (Game.occlusion) {
            init_occlusion_vbo(box);
            init_occlusion_vao();
        }
    }

    public void init_terrain_vbo(Integer[] vertices, Integer[] textures, Float[] textures_offsets, Float[] colors) {
        vertices_size = vertices.length;
        textures_size = textures.length;
        textures_offsets_size = textures_offsets.length;
        colors_size = colors.length;

        glBindBuffer(GL_ARRAY_BUFFER, terrain_vert_buf);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(vertices), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, terrain_tex_buf);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(textures), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, terrain_tex_offset_buf);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(textures_offsets), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, terrain_color_buf);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(colors), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void init_terrain_vao() {
        glBindVertexArray(terrain_vao);

        glBindTexture(GL_TEXTURE_2D, ResourceUtil.texture_id("atlas.png"));

        glEnableVertexAttribArray(Game.terrain_shader.attr("attr_pos"));
        glEnableVertexAttribArray(Game.terrain_shader.attr("attr_tex_offset"));
        glEnableVertexAttribArray(Game.terrain_shader.attr("attr_tex_coord"));
        glEnableVertexAttribArray(Game.terrain_shader.attr("attr_color"));

        glBindBuffer(GL_ARRAY_BUFFER, terrain_vert_buf);
        glVertexAttribPointer(Game.terrain_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, terrain_tex_buf);
        glVertexAttribPointer(Game.terrain_shader.attr("attr_tex_coord"), 2, GL_INT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, terrain_tex_offset_buf);
        glVertexAttribPointer(Game.terrain_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, terrain_color_buf);
        glVertexAttribPointer(Game.terrain_shader.attr("attr_color"), 3, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);
    }

    public void init_occlusion_vbo(Integer[] box) {
        glBindBuffer(GL_ARRAY_BUFFER, occlusion_vert_buf);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(box), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void init_occlusion_vao() {
        glBindVertexArray(occlusion_vao);

        glEnableVertexAttribArray(Game.occlusion_shader.attr("attr_pos"));
        glBindBuffer(GL_ARRAY_BUFFER, occlusion_vert_buf);
        glVertexAttribPointer(Game.occlusion_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);
        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteQueries(occlusion_query);
    }
}

package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TexturedMesh extends ColoredMesh {
    public Integer[] tex;
    public Float[] tex_off;

    public int colored_vao;
    public int tbo; // textures buffer
    public int tobo; // textures offset buffer

    public TexturedMesh() {
        super();

        this.colored_vao = glGenVertexArrays();

        this.tbo = glGenBuffers();
        this.tobo = glGenBuffers();
    }

    public Mesh prepare(Integer[] verts, Float[] colors, Integer[] tex, Float[] tex_off) {
        this.init_vbo(verts, colors,tex, tex_off);
        this.bind_vao();
        this.init_vao();
        this.unbind_vao();
        return this;
    }

    public Mesh init_vbo(Integer[] verts, Float[] colors, Integer[] tex, Float[] tex_off) {
        super.init_vbo(verts, colors);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(tex), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tobo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(tex_off), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return this;
    }

    public Mesh init_vao() {
        super.init_vao();

        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_coord"));
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_coord"), 2, GL_INT, false, 0, 0);

        glEnableVertexAttribArray(Game.current_shader.attr("attr_tex_offset"));
        glBindBuffer(GL_ARRAY_BUFFER, tobo);
        glVertexAttribPointer(Game.current_shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 0, 0);

        return this;
    }

}

package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class ColoredMesh extends Mesh {
    public Float[] colors;
    public int cbo; // colors buffer

    public ColoredMesh() {
        super();

        this.cbo = glGenBuffers();

        this.bind_vao();
        this.init_vao();
        this.unbind_vao();
    }

    public Mesh prepare(Integer[] verts, Float[] colors) {
        this.init_vbo(verts, colors);
        return this;
    }

    public Mesh init_vbo(Integer[] verts, Float[] colors) {
        super.init_vbo(verts);

        glBindBuffer(GL_ARRAY_BUFFER, cbo);
        glBufferData(GL_ARRAY_BUFFER, Chunk.size, null, GL_DYNAMIC_DRAW);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(colors), GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return this;
    }

    public Mesh init_vao() {
        super.init_vao();

        glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));
        glBindBuffer(GL_ARRAY_BUFFER, cbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, 0, 0);

        return this;
    }
}

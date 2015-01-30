package me.geakstr.voxel.model;

import me.geakstr.voxel.util.RenderUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {
    public int size;

    public int vbo;
    public int ibo;

    public Mesh() {
        this.size = 0;
        this.vbo = glGenBuffers();
        this.ibo = glGenBuffers();
    }

    public void add_verts(Vertex[] vertices, int[] indices) {
        size = indices.length;

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, RenderUtil.create_flipped_buffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, RenderUtil.create_flipped_buffer(indices), GL_STATIC_DRAW);
    }

    public void draw() {
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_LINE_LOOP, size, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
    }
}

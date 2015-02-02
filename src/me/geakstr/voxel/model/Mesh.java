package me.geakstr.voxel.model;

import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {
    public float[] vertices;
    //public int[] indices;

    public int vertices_size;
    //public int indices_size;

    public int vbo;
//    public int ibo;

    public Mesh() {}

    public Mesh(float[] vertices/*, int[] indices*/) {
        this.vertices = vertices;
//        this.indices = indices;
        this.vertices_size = vertices.length;
//        this.indices_size = indices.length;
    }

    public void gen_buffers() {
        this.vbo = glGenBuffers();
//        this.ibo = glGenBuffers();
    }

    public void fill_buffers() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(vertices), GL_STATIC_DRAW);

//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(indices), GL_STATIC_DRAW);
    }

    public void render() {
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_LINE_LOOP, 0, vertices_size);

//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glDrawElements(GL_TRIANGLES, indices_size, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
    }
}

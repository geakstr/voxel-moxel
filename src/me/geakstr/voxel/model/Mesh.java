package me.geakstr.voxel.model;

import me.geakstr.voxel.util.ExtendedBufferUtil;
import me.geakstr.voxel.util.ResourceUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Mesh {
    public float[] vertices;
    public float[] texture_coords;

    public int vertices_size;
    public int texture_coords_size;

    public int vbo;
    public int tbo;

    public Mesh() {}

    public Mesh(float[] vertices, float[] texture_coords) {
        this.vertices = vertices;
        this.vertices_size = vertices.length;
        this.texture_coords = texture_coords;
        this.texture_coords_size = texture_coords.length;
    }

    public void gen_buffers() {
        vbo = glGenBuffers();
        tbo = glGenBuffers();
    }

    public void fill_buffers() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(texture_coords), GL_STATIC_DRAW);
    }

    public void render() {
        glBindTexture(GL_TEXTURE_2D, ResourceUtil.getTexturesID("stone.png"));

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, vertices_size);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}

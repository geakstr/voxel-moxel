package me.geakstr.voxel.model;

import me.geakstr.voxel.util.ExtendedBufferUtil;
import me.geakstr.voxel.util.ResourceUtil;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {
    public float[] vertices;
    public float[] texture_coords;
    //public int[] indices;

    public int vertices_size;
    public int texture_coords_size;
    //public int indices_size;

    public int vbo;
    public int tbo;
//    public int ibo;
    
    public Mesh() {}

    public Mesh(float[] vertices, float[] texture_coords/*, int[] indices*/) {
        this.vertices = vertices;
//        this.indices = indices;
        this.vertices_size = vertices.length;
//        this.indices_size = indices.length;
        this.texture_coords = texture_coords;
        this.texture_coords_size = texture_coords.length;
    }

    public void gen_buffers() {
        vbo = glGenBuffers();
        tbo = glGenBuffers();
//        this.ibo = glGenBuffers();
    }

    public void fill_buffers() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(vertices), GL_STATIC_DRAW);

//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(indices), GL_STATIC_DRAW);
        
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(texture_coords), GL_STATIC_DRAW);
    }

    public void render() {
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, vertices_size);

//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glDrawElements(GL_TRIANGLES, indices_size, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glBindTexture(GL_TEXTURE_2D, ResourceUtil.getTexturesID(1));
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);
    }
}

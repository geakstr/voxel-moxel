package me.geakstr.voxel.model.meshes;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glMapBufferRange;

public class IndexedMesh extends AbstractMesh {
    public int indices_counter;

    public ByteBuffer ibo_data;

    protected int ibo_capacity;
    protected int ibo;

    public IndexedMesh() {
        super();

        this.ibo_capacity = initial_capacity;
        this.ibo = glGenBuffers();
        this.init_ibo();

        this.init_vao();
    }

    public void update_gl_buffers() {
        super.update_gl_buffers();

        int ibo_data_size = ibo_data.capacity();
        boolean ibo_orphan = false;
        while (ibo_data_size > ibo_capacity) {
            ibo_capacity *= 2;
            ibo_orphan = true;
        }
        if (ibo_orphan) {
            init_ibo();
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glMapBufferRange(GL_ELEMENT_ARRAY_BUFFER, 0, ibo_data_size, mapping_flags).put(ibo_data);
        glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
    }

    public void update_gl_data(float[] vertices, float[] tex_coords, float[] tex_coords_offsets, float[] colors) {
        super.update_gl_data(vertices, tex_coords, tex_coords_offsets, colors);
        this.update_ibo(vertices.length / 2);
    }

    public void update_gl_data(List<Float> vertices, List<Float> tex_coords, List<Float> tex_coords_offsets, List<Float> colors) {
        super.update_gl_data(vertices, tex_coords, tex_coords_offsets, colors);
        this.update_ibo(vertices.size() / 2);
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices_counter, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void destroy() {
        super.destroy();
        glDeleteBuffers(ibo);
    }

    protected void init_ibo() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo_capacity, null, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    protected void update_ibo(int count) {
        this.indices_counter = count;
        this.ibo_data = BufferUtils.createByteBuffer(count * 4);

        for (int i = 0, bytes = 0; bytes < count; i += 4, bytes += 6) {
            ibo_data.putInt(i);
            ibo_data.putInt(i + 1);
            ibo_data.putInt(i + 2);

            ibo_data.putInt(i + 1);
            ibo_data.putInt(i + 3);
            ibo_data.putInt(i + 2);
        }
        ibo_data.flip();
    }

    protected void init_vao() {
        glBindVertexArray(vao);
        super.init_vao();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBindVertexArray(0);
    }
}

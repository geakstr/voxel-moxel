package me.geakstr.voxel.model.meshes;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import org.lwjgl.BufferUtils;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ModelMesh extends AbstractMesh {
    public ModelMesh() {
        super();
        this.init_vao();
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, faces_counter);
        glBindVertexArray(0);
    }

    public void update_gl_data(List<int[]> faces, List<Vector3f> verts, List<Vector2f> tex_coords) {
        faces_counter = faces.size() * 3;
        vbo_data = BufferUtils.createByteBuffer(faces.size() * 30 * 4);
        for (int[] face : faces) {
            for (int i = 0; i < 3; i++) {
                Vector3f vert = verts.get(face[i]);
                Vector2f tex_coord = tex_coords.get(face[i + 3]);

                vbo_data.putFloat(vert.x);
                vbo_data.putFloat(vert.y);
                vbo_data.putFloat(vert.z);

                vbo_data.putFloat(tex_coord.x);
                vbo_data.putFloat(tex_coord.y);

                vbo_data.putFloat(0);
                vbo_data.putFloat(0);

                vbo_data.putFloat(1);
                vbo_data.putFloat(1);
                vbo_data.putFloat(1);
            }
        }
        vbo_data.flip();
    }

    protected void init_vao() {
        glBindVertexArray(vao);
        super.init_vao();
        glBindVertexArray(0);
    }
}

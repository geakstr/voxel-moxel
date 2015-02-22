package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.meshes.ModelMesh;
import org.lwjgl.BufferUtils;

import java.util.List;

public class Model extends ModelMesh {
    public Model(List<int[]> faces, List<Vector3f> verts, List<Vector2f> tex_coords) {
        super();
        update_gl_data(faces, verts, tex_coords);
        update_gl_buffers();
    }
}

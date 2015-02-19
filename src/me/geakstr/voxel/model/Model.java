package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;

import java.util.List;

public class Model extends Mesh {
    public Model(List<int[]> faces, List<Vector3f> verts, List<Vector2f> tex_coords) {
    	super();
        update_data(faces, verts, tex_coords);
        update_vbo();
    }
}

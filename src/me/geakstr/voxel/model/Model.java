package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;

import java.util.List;

public class Model extends Mesh {
    private List<int[]> faces;
    private List<Vector3f> verts;
    private List<Vector2f> tex_coords;
    private List<Vector3f> normals;

    public Model(List<int[]> faces, List<Vector3f> verts, List<Vector2f> tex_coords) {
        this.faces = faces;
        this.verts = verts;
        this.tex_coords = tex_coords;

        update_data(faces, verts, tex_coords);
    }
}

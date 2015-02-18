package me.geakstr.voxel.model;

import java.util.List;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;

public class Model extends Mesh {
	public List<int[]> faces;
	private List<Vector3f> verts;
    private List<Vector2f> tex_coords;
    private List<Vector3f> normals;
    
    public Model(List<int[]> faces, List<Vector3f> verts, List<Vector2f> tex_coords, List<Vector3f> normals) {
    	this.faces = faces;
    	this.verts = verts;
    	this.tex_coords = tex_coords;
    	this.normals = normals;
    }
}

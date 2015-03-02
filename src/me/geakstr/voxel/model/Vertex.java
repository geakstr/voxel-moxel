package me.geakstr.voxel.model;

public class Vertex {
	public int pos_x, pos_y, pos_z;
	public int tex_coord_x, tex_coord_y;
	public float tex_coord_offset_x, tex_coord_offset_y;
	public float color_r, color_g, color_b;
	
	public Vertex(int pos_x, int pos_y, int pos_z,
			      int tex_coord_x, int tex_coord_y,
			      float tex_coord_offset_x, float tex_coord_offset_y,
			      float color_r, float color_g, float color_b) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.pos_z = pos_z;
		
		this.tex_coord_x = tex_coord_x;
		this.tex_coord_y = tex_coord_y;
		
		this.tex_coord_offset_x = tex_coord_offset_x;
		this.tex_coord_offset_y = tex_coord_offset_y;
		
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + pos_x;
	    result = prime * result + pos_y;
	    result = prime * result + pos_z;
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    Vertex other = (Vertex) obj;
	    if (pos_x != other.pos_x)
		    return false;
	    if (pos_y != other.pos_y)
		    return false;
	    if (pos_z != other.pos_z)
		    return false;
	    return true;
    }
	
	

	
}

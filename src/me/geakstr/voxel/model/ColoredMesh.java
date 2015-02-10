package me.geakstr.voxel.model;

import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.util.ExtendedBufferUtil;

public class ColoredMesh extends AbstractMesh {
	public int cbo; // colors buffer
	
	public ColoredMesh() {
		super();
		
		this.cbo = glGenBuffers();
	}
	
	public AbstractMesh prepare(Shader shader, Integer[] verts, Float[] colors) {
		this.init_vbo(shader, verts, colors);
		
		this.bind_vao();
		this.init_vao(shader);
		this.unbind_vao();
		
		return this;
	}
	
	public AbstractMesh init_vbo(Shader shader, Integer[] verts, Float[] colors) {
		super.init_vbo(shader, verts);
		
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(colors), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return this;
	}
	
	public AbstractMesh init_vao(Shader shader) {
		super.init_vao(shader);
		
		glEnableVertexAttribArray(shader.attr("attr_color"));
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
        glVertexAttribPointer(shader.attr("attr_color"), 3, GL_INT, false, 0, 0);
        
        return this;
	}
}

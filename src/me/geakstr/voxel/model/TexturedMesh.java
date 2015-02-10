package me.geakstr.voxel.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
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

public class TexturedMesh extends ColoredMesh {
	public int tbo; // textures buffer
	public int tobo; // textures offset buffer
	
	public TexturedMesh() {
		super();
		
		this.tbo = glGenBuffers();
		this.tobo = glGenBuffers();
	}
	
	public AbstractMesh prepare(Shader shader, Integer[] verts, Float[] colors, Integer[] tex, Float[] tex_off) {
		this.init_vbo(shader, verts, colors, tex, tex_off);
		
		this.bind_vao();
		this.init_vao(shader);
		this.unbind_vao();
		
		return this;
	}
	
	public AbstractMesh init_vbo(Shader shader, Integer[] verts, Float[] colors, Integer[] tex, Float[] tex_off) {
		super.init_vbo(shader, verts, colors);
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(tex), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, tobo);
		glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(tex_off), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return this;
	}
	
	public AbstractMesh init_vao(Shader shader) {
		super.init_vao(shader);
		
		glEnableVertexAttribArray(shader.attr("attr_tex_coord"));
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glVertexAttribPointer(shader.attr("attr_tex_coord"), 3, GL_INT, false, 0, 0);
        
        glEnableVertexAttribArray(shader.attr("attr_tex_offset"));
        glBindBuffer(GL_ARRAY_BUFFER, tobo);
        glVertexAttribPointer(shader.attr("attr_tex_offset"), 2, GL_FLOAT, false, 0, 0);
        
        return this;
	}
}

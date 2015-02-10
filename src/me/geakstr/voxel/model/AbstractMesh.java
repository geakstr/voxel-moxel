package me.geakstr.voxel.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.util.ExtendedBufferUtil;

public class AbstractMesh {
	public Integer[] verts;
	public int size;
	
	public int vao;
	public int vbo; // vertices buffer
	
	public AbstractMesh() {
		this.size = 0;
		
		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();
	}
	
	public AbstractMesh prepare(Integer[] verts) {
		this.init_vbo(verts);
		
		this.bind_vao();
		this.init_vao();
		this.unbind_vao();
		
		return this;
	}
	
	public AbstractMesh init_vbo(Integer[] verts) {
		this.size = verts.length;
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(verts), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return this;
	}
	
	public AbstractMesh init_vao() {
		glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);
        
        return this;
	}
	
	public void bind_vao(int vao) {
		glBindVertexArray(vao);
	}
	
	public void bind_vao() {
		this.bind_vao(vao);
	}
	
	public void unbind_vao() {
		glBindVertexArray(0);
	}
	
	public void render(Shader shader) {
		this.bind_vao();
        glDrawArrays(GL_TRIANGLES, 0, size);
        this.unbind_vao();
        World.faces_in_frame += size / 3;
	}
	
	public void destroy() {}
}

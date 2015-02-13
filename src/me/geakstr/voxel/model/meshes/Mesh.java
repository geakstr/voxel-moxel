package me.geakstr.voxel.model.meshes;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.util.ExtendedBufferUtil;

public class Mesh {
	public Integer[] verts;
	
	public int vao;
	public int vbo; // vertices buffer
	
	public Mesh() {
		
		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();

		this.bind_vao();
		this.init_vao();
		this.unbind_vao();
	}
	
	public Mesh prepare(Integer[] verts) {
		this.init_vbo(verts);
		return this;
	}
	
	public Mesh init_vbo(Integer[] verts) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Chunk.size, null, GL_DYNAMIC_DRAW);
		glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(verts), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		
		return this;
	}
	
	public Mesh init_vao() {
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

	public static void bind_texture(int id) {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	
	public void destroy() {}
}

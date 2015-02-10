package me.geakstr.voxel.model;

import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteQueries;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glGenQueries;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;

public class OccludedTexturedMesh extends TexturedMesh {
	public int o_vao;
	public int o_vbo;
	
	public int o_query;
	
	public OccludedTexturedMesh() {
		super();
		
		this.o_vao = glGenVertexArrays();
		this.o_vbo = glGenBuffers();
		
		this.o_query = glGenQueries();
	}
	
	public AbstractMesh prepare(Integer[] verts, Float[] colors, Integer[] tex, Float[] tex_off, Integer[] box) {
		this.init_vbo(verts, colors, tex, tex_off, box);
		
		this.bind_vao();
		this.init_vao();
		this.unbind_vao();
		
		this.bind_vao(o_vao);
		this.init_o_vao();
		this.unbind_vao();
		
		return this;
	}
	
	public AbstractMesh init_vbo(Integer[] verts, Float[] colors, Integer[] tex, Float[] tex_off, Integer[] box) {
		super.init_vbo(verts, colors, tex, tex_off);
		glBindBuffer(GL_ARRAY_BUFFER, o_vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(box), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return this;
	}
	
	public AbstractMesh init_o_vao() {
		glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glBindBuffer(GL_ARRAY_BUFFER, o_vbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 3, GL_INT, false, 0, 0);
        
        return this;
	}
	
	public void destroy() {
		super.destroy();
		glDeleteQueries(o_query);
	}
}

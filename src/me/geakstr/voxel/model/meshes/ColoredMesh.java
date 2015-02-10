package me.geakstr.voxel.model.meshes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.util.ExtendedBufferUtil;

public class ColoredMesh extends AbstractMesh {
	public Float[] colors;
	public int cbo; // colors buffer
	
	public ColoredMesh() {
		super();
		
		this.cbo = glGenBuffers();
	}
	
	public AbstractMesh prepare(Integer[] verts, Float[] colors) {
		this.init_vbo(verts, colors);
		
		this.bind_vao();
		this.init_vao();
		this.unbind_vao();
		
		return this;
	}
	
	public AbstractMesh init_vbo(Integer[] verts, Float[] colors) {
		super.init_vbo(verts);
		
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(colors), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return this;
	}
	
	public AbstractMesh init_vao() {
		super.init_vao();
		
		glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
        glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, 0, 0);
        
        return this;
	}
}

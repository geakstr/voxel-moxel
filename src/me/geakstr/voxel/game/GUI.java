package me.geakstr.voxel.game;

import me.geakstr.voxel.util.ExtendedBufferUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class GUI {
    private static int vbo;
    private static int vao;

    public static void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        float[] cross_hair = new float[]{-0.01f, 0, 0.01f, 0, 0, -0.01f, 0, 0.01f};
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(cross_hair), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 2, GL_FLOAT, false, 8, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public static void draw() {
        glBindVertexArray(vao);
        glDrawArrays(GL_LINES, 0, 8);
        glBindVertexArray(0);
    }
}

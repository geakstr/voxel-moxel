package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.util.ExtendedBufferUtil;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.IntBuffer;

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

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glEnableVertexAttribArray(Game.current_shader.attr("attr_pos"));
        glEnableVertexAttribArray(Game.current_shader.attr("attr_color"));

        glVertexAttribPointer(Game.current_shader.attr("attr_pos"), 2, GL_FLOAT, false, 20, 0);
        glVertexAttribPointer(Game.current_shader.attr("attr_color"), 3, GL_FLOAT, false, 20, 8);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public static void draw() {
        IntBuffer p = BufferUtils.createIntBuffer(1);
        glReadPixels(Window.width / 2, Window.height / 2, 1, 1, GL_RGB, GL_UNSIGNED_BYTE, p);

        Color c = new Color(p.get(0));
        int yiq = ((c.getRed() * 299) + (c.getGreen() * 587) + (c.getBlue() * 114)) / 1000;
        Color inv = yiq >= 128 ? Color.black : Color.white;
        float[] cross_hair = new float[]{0, 0, inv.getRed(), inv.getGreen(), inv.getBlue()};

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, ExtendedBufferUtil.create_flipped_buffer(cross_hair), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(vao);
        glPointSize(1.25f);
        glDrawArrays(GL_POINTS, 0, 1);
        glBindVertexArray(0);
    }
}

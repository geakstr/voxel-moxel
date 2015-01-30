package me.geakstr.voxel.game;

import static org.lwjgl.opengl.GL11.*;

public class Render {
    public static void cube() {
        cube(GL_LINE_LOOP);
    }

    public static void cube(int type) {
        glBegin(type);

        // Top
        glVertex3f( 1.0f,  1.0f, -1.0f); // Top-Right
        glVertex3f(-1.0f,  1.0f, -1.0f); // Top-Left
        glVertex3f(-1.0f,  1.0f,  1.0f); // Bottom-Left
        glVertex3f( 1.0f,  1.0f,  1.0f); // Bottom-Right

        // Bottom
        glVertex3f( 1.0f, -1.0f,  1.0f);
        glVertex3f(-1.0f, -1.0f,  1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f( 1.0f, -1.0f, -1.0f);

        // Front
        glVertex3f( 1.0f,  1.0f,  1.0f);
        glVertex3f(-1.0f,  1.0f,  1.0f);
        glVertex3f(-1.0f, -1.0f,  1.0f);
        glVertex3f( 1.0f, -1.0f,  1.0f);

        // Back
        glVertex3f( 1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f,  1.0f, -1.0f);
        glVertex3f( 1.0f,  1.0f, -1.0f);

        // Left
        glVertex3f(-1.0f,  1.0f,  1.0f);
        glVertex3f(-1.0f,  1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f,  1.0f);

        // Right
        glVertex3f( 1.0f,  1.0f, -1.0f);
        glVertex3f( 1.0f,  1.0f,  1.0f);
        glVertex3f( 1.0f, -1.0f,  1.0f);
        glVertex3f( 1.0f, -1.0f, -1.0f);

        glEnd();
    }
}

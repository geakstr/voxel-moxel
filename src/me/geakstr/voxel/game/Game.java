package me.geakstr.voxel.game;

import me.geakstr.voxel.render.Render;

import static org.lwjgl.opengl.GL11.*;

public class Game {
    private static float offset = 0;

    public static void render() {
        glLoadIdentity();
        glTranslatef(-25, -15, -100);
        glRotatef(45f + offset, 0.4f, 1.0f, 0.1f);
        glColor3f(1, 0, 0);

        offset += 5;

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                for (int z = 0; z < 20; z++) {
                    Render.cube();
                    glTranslatef(0f, 0.0f, 2f);
                }
                glTranslatef(0f, 2f, -40f);
            }
            glTranslatef(2f, -40f, 0);
        }

        if (offset >= 360) {
            offset = 0;
        }
    }
}

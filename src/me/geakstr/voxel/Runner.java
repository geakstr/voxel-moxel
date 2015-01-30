package me.geakstr.voxel;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Runner {
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    private long window;

    public void run() {
        try {
            init();
            loop();
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        int WIDTH = 300;
        int HEIGHT = 300;

        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, GL_TRUE);
                }
            }
        });

        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GLContext.createFromCurrent();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-10, 10, -10, 10, -2, 1);
        glMatrixMode(GL_MODELVIEW);

        float x = 0;
        while (glfwWindowShouldClose(window) == GL_FALSE) {
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glColor3f(1, 0, 0);
            glRotatef(x += 1f, 1, 1, 1);
            glBegin(GL_QUADS);
            glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
            glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
            glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
            glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
            glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)
            glColor3f(1.0f, 0.5f, 0.0f); // Set The Color To Orange
            glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad (Bottom)
            glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad (Bottom)
            glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Bottom)
            glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Bottom)
            glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
            glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
            glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
            glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Front)
            glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Front)
            glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
            glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Back)
            glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Back)
            glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
            glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)
            glColor3f(0.0f, 0.0f, 1.0f); // Set The Color To Blue
            glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
            glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
            glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Left)
            glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Left)
            glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
            glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
            glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Right)
            glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Right)
            glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Right)
            glEnd();
            glLoadIdentity();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Runner().run();
    }
}

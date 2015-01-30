package me.geakstr.voxel.core;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static int WIDTH = 800;
    public static int HEIGHT = 800;
    public static boolean VSYNC = false;

    private static double last_time;
    private static int fps;

    private static long window;

    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback keyCallback;

    public static void init(int width, int height, boolean vsync) {
        WIDTH = width;
        HEIGHT = height;
        VSYNC = vsync;

        last_time = glfwGetTime();
        fps = 0;

        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, 100);

        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, GL11.GL_TRUE);
                }
            }
        });

        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(VSYNC ? 1 : 0);
        glfwShowWindow(window);

        GLContext.createFromCurrent();

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_FRAMEBUFFER_SRGB);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45.0f, WIDTH / HEIGHT, 0.1f, 1000.0f);

        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    public static void destroy() {
        glfwDestroyWindow(window);
        keyCallback.release();
    }

    public static void terminate() {
        glfwTerminate();
        errorCallback.release();
    }

    public static boolean should_close() {
        return glfwWindowShouldClose(window) == GL11.GL_TRUE;
    }

    public static void after_render() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static void before_render() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    }

    public static boolean fps() {
        double current_time = glfwGetTime();
        fps++;
        if (current_time - last_time >= 1.0) {
            System.out.println(fps + " FPS");
            fps = 0;
            last_time++;
            return true;
        }
        return false;
    }
}

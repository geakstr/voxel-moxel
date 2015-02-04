package me.geakstr.voxel.core;

import me.geakstr.voxel.model.World;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static int width = 800;
    public static int height = 800;
    public static boolean vsync = false;

    public static long window;

    private static double last_time;
    private static int fps;

    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback keyCallback;
    private static GLFWWindowSizeCallback resizeCallback;

    public static boolean was_resize = false;

    public static void init(int width, int height, boolean vsync) {
        Window.width = width;
        Window.height = height;
        Window.vsync = vsync;

        Window.last_time = glfwGetTime();
        Window.fps = 0;

        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);

        window = glfwCreateWindow(Window.width, Window.height, "Voxel Moxel", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - Window.width) / 2, (GLFWvidmode.height(vidmode) - Window.height) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(Window.vsync ? 1 : 0);
        glfwShowWindow(window);

        Input.setCursorDisabled(true);

        GLContext.createFromCurrent();

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_FRAMEBUFFER_SRGB);

        glViewport(0, 0, Math.max(Window.width, Window.height), Math.max(Window.width, Window.height));

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

            }
        });

        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if (Window.width != width || Window.height != height) {
                    Window.width = width;
                    Window.height = height;
                    Window.was_resize = true;
                }
            }
        });
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

    public static void before_render() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    }

    public static void after_render() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static void setup_aspect_ratio() {
        int size = Math.max(Window.width, Window.height);
        glViewport(0, 0, size, size);
    }

    public static boolean set_title() {
        double current_time = glfwGetTime();
        fps++;
        if (current_time - last_time >= 1.0) {
            glfwSetWindowTitle(window,
                    "Voxel Moxel | " +
                            fps + " fps; " +
                            World.chunks_in_frame + "/" + World.world_volume + " chunks");
            fps = 0;
            last_time++;
            return true;
        }
        return false;
    }
}

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

    public static boolean was_resize = false;

    public static void init() {
        Window.last_time = glfwGetTime();
        Window.fps = 0;

        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
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

        glClearColor(1f, 1f, 1f, 1.0f);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);

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
        if (keyCallback != null) {
            keyCallback.release();
        }
    }

    public static void terminate() {
        glfwTerminate();
        if (errorCallback != null) {
            errorCallback.release();
        }
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
                            World.chunks_in_frame + "/" + World.volume + " chunks; " +
                            World.faces_in_frame + " triangles"
            );
            fps = 0;
            last_time++;
            return true;
        }
        return false;
    }
}


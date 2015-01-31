package me.geakstr.voxel.core;

import me.geakstr.voxel.math.Vec2f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    public static final int NUM_KEYCODES = 348;
    public static final int NUM_MOUSEBUTTONS = 7;

    private static long window;
    private static boolean[] lastKeys = new boolean[NUM_KEYCODES];
    private static boolean[] lastMouse = new boolean[NUM_MOUSEBUTTONS];

    public static void init(long window) {
        Input.window = window;
        glfwPollEvents();
    }

    public static void update() {
        for (int i = 0; i < NUM_KEYCODES; i++)
            lastKeys[i] = getKey(i);

        for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
            lastMouse[i] = getMouse(i);
    }

    public static boolean getKey(int keyCode) {
        return glfwGetKey(window, keyCode) == 1;
    }

    public static boolean getKeyDown(int keyCode) {
        return getKey(keyCode) && !lastKeys[keyCode];
    }

    public static boolean getKeyUp(int keyCode) {
        return !getKey(keyCode) && lastKeys[keyCode];
    }

    public static boolean getMouse(int mouseButton) {
        return glfwGetMouseButton(window, mouseButton) == 1;
    }

    public static boolean getMouseDown(int mouseButton) {
        return getMouse(mouseButton) && !lastMouse[mouseButton];
    }

    public static boolean getMouseUp(int mouseButton) {
        return !getMouse(mouseButton) && lastMouse[mouseButton];
    }

    public static Vec2f getMousePosition() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1), y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, x, y);
        return new Vec2f((float) x.get(0), (float) y.get(0));
    }

    public static void setMousePosition(Vec2f pos) {
        glfwSetCursorPos(window, pos.getX(), pos.getY());
    }

}
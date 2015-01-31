package me.geakstr.voxel;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.math.Vec2f;

import static org.lwjgl.glfw.GLFW.*;

public class Runner {
    private Runner() {}

    private void run() {
        try {
            init();
            loop();
            destroy();
        } finally {
            terminate();
        }
    }

    private void init() {
        Window.init(650, 650, false);
        Input.init(Window.getWindow());
    }

    private void destroy() {
        Window.destroy();
    }

    private void terminate() {
        Window.terminate();
    }

    private void loop() {
        while (!Window.should_close()) {
            Window.fps();
            Window.before_render();
            Game.render();
            Window.after_render();
            if(Input.getKey(GLFW_KEY_W)){
                System.out.println("W");
            }
            if(Input.getMouse(GLFW_MOUSE_BUTTON_1)) {
                System.out.println("Click at " + Input.getMousePosition());
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Runner().run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

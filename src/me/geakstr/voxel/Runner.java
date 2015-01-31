package me.geakstr.voxel;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.game.Game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

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
        Window.init(650, 650, true);
        Game.init();
        Input.init(Window.getWindow());
    }

    private void loop() {
        while (!Window.should_close()) {
            Window.fps();
            Window.before_render();
            if (Window.was_resize) {
                Window.was_resize = false;
                Window.setup_aspect_ratio();
            }
            Game.update();
            Game.render();
            Window.after_render();
            if (Input.getKey(GLFW_KEY_W)) {
                System.out.println("W");
            }
            if (Input.getMouse(GLFW_MOUSE_BUTTON_1)) {
                System.out.println("Click at " + Input.getMousePosition());
            }
        }
    }

    private void destroy() {
        Window.destroy();
    }

    private void terminate() {
        Window.terminate();
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

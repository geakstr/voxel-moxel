package me.geakstr.voxel;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.game.Game;

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
        Window.init(800, 800, false);
        Game.init();
    }

    private void loop() {
        while (!Window.should_close()) {
            Window.fps();
            Window.before_render();
            Game.render();
            Window.after_render();
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

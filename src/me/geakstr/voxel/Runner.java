package me.geakstr.voxel;

import me.geakstr.voxel.core.Input;
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
        Window.init(800, 800, true);
        Input.init();
        Game.init();
    }

    private void loop() {
        while (!Window.should_close()) {
            Window.set_title();
            Window.before_render();
            if (Window.was_resize) {
                Window.was_resize = false;
                Window.setup_aspect_ratio();
            }
            Game.before_render();
            Game.render();
            Game.after_render();
            Window.after_render();
        }
    }

    private void destroy() {
        Window.destroy();
        if (null != Game.chunks_workers_executor_service) {
            Game.chunks_workers_executor_service.es.shutdown();
        }
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

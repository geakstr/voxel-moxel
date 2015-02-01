package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;

public class Game {
    private static Transform transform;
    private static Shader shader;
    private static Camera camera;
    private static World world;

    public static void init() {
        shader = new Shader("simple.vs", "simple.fs").compile();

        transform = new Transform();

        camera = new Camera(70, (float) Window.width / (float) Window.height, 0.1f, 70f);

        world = new World(1, 1, 1, 16).gen();
    }

    public static void update() {
        camera.input();
    }

    public static void render() {
        camera.apply();

        shader.bind();

        shader.set_uniform("uniform_color", 1);
        shader.set_uniform("uniform_transform", transform.getTransform());
        shader.set_uniform("uniform_camera_projection", camera.getProjectionMatrix());
        shader.set_uniform("uniform_camera_view", camera.getViewMatrix());

        world.render();

        shader.unbind();
    }
}

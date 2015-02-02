package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.FrustumCulling;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;

public class Game {
    public static Transform transform;
    public static Shader shader;
    public static Camera camera;
    public static FrustumCulling frustum;

    public static void init() {
        shader = new Shader("simple.vs", "simple.fs").compile();

        transform = new Transform();
        camera = new Camera(100, (float) Window.width / (float) Window.height, 0.01f, 100f);
        frustum = new FrustumCulling();

        World.init(1, 3, 3, 3);
        World.gen();
    }

    public static void update() {
        camera.input();

    }

    public static void render() {
        camera.apply();
        frustum.calculateFrustum(camera.getProjectionMatrix(), camera.getViewMatrix());

        shader.bind();

        shader.set_uniform("uniform_color", 1);
        shader.set_uniform("uniform_transform", transform.getTransform());
        shader.set_uniform("uniform_camera_projection", camera.getProjectionMatrix());
        shader.set_uniform("uniform_camera_view", camera.getViewMatrix());

        World.render();

        shader.unbind();
    }
}

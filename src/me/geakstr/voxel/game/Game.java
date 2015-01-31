package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.model.Cube;
import me.geakstr.voxel.model.Mesh;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;

import static org.lwjgl.opengl.GL11.*;

public class Game {
    private static Mesh mesh;
    private static Transform transform;
    private static Shader simple_shader;
    private static Camera camera;

    public static void init() {
        simple_shader = new Shader("simple.vs", "simple.fs");
        simple_shader.compile();

        transform = new Transform();

        camera = new Camera(100, (float) Window.width / (float) Window.height, 0.1f, 70f);

        mesh = new Mesh();
        mesh.add_verts(Cube.vertices, Cube.indices);
    }

    static float temp = 0.0f;

    public static void update() {
        temp += 0.01;
        //transform.translate((float) Math.sin(temp), 0, 0);
        camera.input();
    }

    public static void render() {
        camera.apply();

        simple_shader.set_uniform("uniform_color", 1);
        simple_shader.set_uniform("uniform_transform", transform.getTransform());
        simple_shader.set_uniform("uniform_camera_projection", camera.getProjectionMatrix());
        simple_shader.set_uniform("uniform_camera_view", camera.getViewMatrix());

        simple_shader.bind();
        mesh.draw();
    }
}

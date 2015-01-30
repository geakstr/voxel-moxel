package me.geakstr.voxel.game;

import me.geakstr.voxel.model.Cube;
import me.geakstr.voxel.model.Mesh;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;

public class Game {
    private static Mesh mesh;
    private static Transform transform;

    public static void init() {
        Shader.create();
        Shader.attach_vertex_shader("simple.vs");
        Shader.attach_fragment_shader("simple.fs");
        Shader.compile();

        transform = new Transform();
        transform.rotate(15, -7, 0);
        transform.scale(0.33f, 0.33f, 0.33f);

        mesh = new Mesh();
        mesh.add_verts(Cube.vertices, Cube.indices);
    }

    static float temp = 0.0f;

    public static void update() {
        temp += 0.01;
        transform.translate((float) Math.sin(temp), 0, 0);


        Shader.set_uniform("uniform_color", 1);
        Shader.set_uniform("uniform_transform", transform.get_transform());
    }

    public static void render() {
        Shader.bind();
        mesh.draw();
    }
}

package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Shader;
import me.geakstr.voxel.model.Cube;
import me.geakstr.voxel.model.Mesh;

public class Game {
    private static float offset = 0;
    private static Mesh mesh;

    public static void init() {
        Shader.init();
        Shader.attach_vertex_shader("simple.vs");
        Shader.attach_fragment_shader("simple.fs");
        Shader.link();

        mesh = new Mesh();
        mesh.add_verts(Cube.vertices);
    }

    public static void render() {
        Shader.bind();
        mesh.draw();
    }
}

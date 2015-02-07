package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;
import me.geakstr.voxel.util.ResourceUtil;

public class Game {
    public static Transform world_transform;
    public static Shader world_shader;

    public static void init() {
        ResourceUtil.load_textures("atlas.png");

        Camera.init(100, (float) Window.width / (float) Window.height, 0.01f, 100f);

        world_shader = new Shader("simple.vs", "simple.fs").compile();
        world_shader.save_attr("attr_pos").save_attr("attr_tex_offset").save_attr("attr_tex_coord");

        world_transform = new Transform();

        World.init(1, 1, 8, 8, 4);
        World.new_gen();
    }

    public static void before_render() {
        Camera.input();
        Camera.apply();

        Frustum.update();

        world_shader.bind();

        world_shader.set_uniform("uniform_transform", world_transform.getTransform());
        world_shader.set_uniform("uniform_camera_projection", Camera.projection);
        world_shader.set_uniform("uniform_camera_view", Camera.view);
        world_shader.set_uniform("uniform_texture", 0);
    }

    public static void render() {
        World.render();
    }

    public static void after_render() {
        world_shader.unbind();
    }
}

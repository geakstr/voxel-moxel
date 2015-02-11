package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.model.TextureAtlas;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.model.meshes.Mesh;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;
import me.geakstr.voxel.util.ResourceUtil;
import me.geakstr.voxel.workers.ChunksWorkersExecutorService;

public class Game {
    public static boolean frustum, occlusion;

    public static Transform world_transform;
    public static ChunksWorkersExecutorService chunks_workers_executor_service;
    public static Shader current_shader, world_shader, colored_world_shader;

    public static Vector2f textured_world_shader_texture_info = new Vector2f(TextureAtlas.atlas_size, TextureAtlas.crop_size);
    public static Vector2f colored_world_shader_texture_info = new Vector2f(256, 1);

    public static void init() {
        ResourceUtil.gen_colored_texture();
        ResourceUtil.load_textures("atlas.png");

        Camera.init(100, (float) Window.width / (float) Window.height, 0.01f, 512f);

        world_shader = new Shader("world.vs", "world.fs").compile();
        world_shader
                .save_attr("attr_pos")
                .save_attr("attr_tex_offset")
                .save_attr("attr_tex_coord")
                .save_attr("attr_color")
                .save_attr("attr_colored_tex_off")
                .save_attr("attr_colored_tex_repeat_number");

        colored_world_shader = new Shader("world.vs", "colored_world.fs").compile();
        colored_world_shader
                .save_attr("attr_pos")
                .save_attr("attr_tex_offset")
                .save_attr("attr_tex_coord")
                .save_attr("attr_color")
                .save_attr("attr_colored_tex_off")
                .save_attr("attr_colored_tex_repeat_number");

        world_transform = new Transform();

        chunks_workers_executor_service = new ChunksWorkersExecutorService();

        World.init(1, 1, 16, 16, 16);
        World.gen();
    }

    public static void before_render() {
        Camera.input();
        Camera.apply();

        Frustum.update();
    }

    public static void render() {
        current_shader = colored_world_shader;
        current_shader.bind();
        current_shader.set_uniform("uniform_transform", world_transform.getTransform());
        current_shader.set_uniform("uniform_camera_projection", Camera.projection);
        current_shader.set_uniform("uniform_camera_view", Camera.view);
        current_shader.set_uniform("uniform_texture", 0);
        current_shader.set_uniform("uniform_texture_info", textured_world_shader_texture_info);
        Mesh.bind_texture(ResourceUtil.texture_id("_colors"));
        World.colored_render();
        current_shader.unbind();
    }

    public static void after_render() {}

    public static void destroy() {
        World.destroy();
        if (null != world_shader) {
            world_shader.dispose();
        }
        if (null != chunks_workers_executor_service) {
            chunks_workers_executor_service.es.shutdown();
        }
    }
}

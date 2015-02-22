package me.geakstr.voxel.game;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.model.Player;
import me.geakstr.voxel.model.TextureAtlas;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.model.meshes.AbstractMesh;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;
import me.geakstr.voxel.util.ResourceUtil;
import me.geakstr.voxel.workers.ChunksWorkersExecutorService;

public class Game {
    public static boolean frustum;

    public static Transform world_transform;
    public static ChunksWorkersExecutorService chunks_workers_executor_service;
    public static Shader current_shader, world_shader, gui_shader;

    public static Player player;

    public static Vector2f chunk_shader_texture_info = new Vector2f(TextureAtlas.atlas_size, TextureAtlas.crop_size);
    public static Vector2f model_shader_texture_info = new Vector2f(0, 0);

    public static void init() {
        chunks_workers_executor_service = new ChunksWorkersExecutorService();

        gui_shader = new Shader("gui").compile();
        gui_shader.save_attrs("attr_pos", "attr_color");

        world_shader = new Shader("world").compile();
        world_shader.save_attrs("attr_pos", "attr_tex_offset", "attr_tex_coord", "attr_color", "attr_normal");

        current_shader = world_shader;
        ResourceUtil.load_textures("atlas.png", "axe.png");
        ResourceUtil.load_models("axe");

        world_transform = new Transform();

        Camera.init();

        current_shader = world_shader;
        World.init();
        world_transform = new Transform();
        player = new Player();

        current_shader = gui_shader;
        GUI.init();
    }

    public static void before_render() {
        if (Camera.input()) {
            Camera.update();
            World.update();
            Frustum.update();
            player.update();
        }
    }

    public static void render() {
        current_shader = world_shader;
        current_shader.bind();
        current_shader.set_uniform("uniform_transform", world_transform.getTransform());
        current_shader.set_uniform("uniform_camera_projection", Camera.projection);
        current_shader.set_uniform("uniform_camera_view", Camera.view);
        current_shader.set_uniform("uniform_texture", 0);
        current_shader.set_uniform("uniform_texture_info", chunk_shader_texture_info);

        AbstractMesh.bind_texture(ResourceUtil.texture_id("atlas.png"));
        World.render();

//        AbstractMesh.bind_texture(ResourceUtil.texture_id("axe.png"));
//        current_shader.set_uniform("uniform_texture_info", model_shader_texture_info);
//        ResourceUtil.models.get("axe").draw();
//
//        current_shader.set_uniform("uniform_transform", player.getTransform());
//        player.render();
        current_shader.unbind();

        current_shader = gui_shader;
        current_shader.bind();
        GUI.draw();
        current_shader.unbind();
    }

    public static void after_render() {}

    public static void destroy() {
        World.destroy();
        if (null != world_shader) {
            world_shader.destroy();
        }
        if (null != chunks_workers_executor_service) {
            chunks_workers_executor_service.es.shutdown();
        }
    }
}

package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.Block;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.model.Mesh;
import me.geakstr.voxel.model.Player;
import me.geakstr.voxel.model.TextureAtlas;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.*;
import me.geakstr.voxel.util.ResourceUtil;
import me.geakstr.voxel.workers.ChunksWorkersExecutorService;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Game {
    public static boolean frustum, occlusion;

    public static Transform world_transform;
    public static ChunksWorkersExecutorService chunks_workers_executor_service;
    public static Shader current_shader, world_shader;
    
    public static Player player;

    public static Vector2f world_shader_texture_info = new Vector2f(TextureAtlas.atlas_size, TextureAtlas.crop_size);

    public static void init() {
        ResourceUtil.load_textures("atlas.png");

        Camera.init(70, (float) Window.width / (float) Window.height, 0.01f, 512f);

        world_shader = new Shader("world.vs", "world.fs").compile();
        world_shader.save_attrs("attr_pos", "attr_tex_offset", "attr_tex_coord", "attr_color");
        current_shader = world_shader;

        world_transform = new Transform();

        chunks_workers_executor_service = new ChunksWorkersExecutorService();

        World.init(16, 4, 16, 16, 16);
        World.gen();
        player = new Player();
    }

    private static boolean was_input = false;
    public static void before_render() {
        was_input = Camera.input();
        
        if (was_input) {
	        Camera.apply();
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
        current_shader.set_uniform("uniform_texture_info", world_shader_texture_info);
        Mesh.bind_texture(ResourceUtil.texture_id("atlas.png"));
        World.render();
        
        if (was_input) {
        	current_shader.set_uniform("uniform_transform", player.getTransform());
        }
        player.render();
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

package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.render.Ray;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;
import me.geakstr.voxel.util.ResourceUtil;
import me.geakstr.voxel.workers.ChunksWorkersExecutorService;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    public static boolean occlusion;

    public static Transform world_transform;
    public static ChunksWorkersExecutorService chunks_workers_executor_service;
    public static Shader current_shader, terrain_shader, occlusion_shader;
    public static Ray ray;

    public static void init() {
        ResourceUtil.load_textures("atlas.png");

        Camera.init(100, (float) Window.width / (float) Window.height, 0.01f, 512f);

        terrain_shader = new Shader("terrain.vs", "terrain.fs").compile();
        terrain_shader.save_attr("attr_pos").save_attr("attr_tex_offset").save_attr("attr_tex_coord").save_attr("attr_color");

        if (occlusion) {
            occlusion_shader = new Shader("occlusion.vs", "occlusion.fs").compile();
            occlusion_shader.save_attr("attr_pos");
        }
        
        current_shader = terrain_shader;

        world_transform = new Transform();

        chunks_workers_executor_service = new ChunksWorkersExecutorService();
        
        ray = new Ray(Camera.position, Camera.rotation);

        World.init(16, 1, 16, 16, 32);
        World.gen();
    }

    public static void before_render() {
        Camera.input();
        Camera.apply();

        Frustum.update();
    }

    public static void render() {
        if (occlusion) {
            occlusion_shader.bind();
            occlusion_shader.set_uniform("uniform_transform", world_transform.getTransform());
            occlusion_shader.set_uniform("uniform_camera_projection", Camera.projection);
            occlusion_shader.set_uniform("uniform_camera_view", Camera.view);
            glDisable(GL_DEPTH_TEST);
            glColorMask(false, false, false, false);
            glDepthMask(false);
            World.occlusion_render();
            occlusion_shader.unbind();
        }

        current_shader.bind();
        current_shader.set_uniform("uniform_transform", world_transform.getTransform());
        current_shader.set_uniform("uniform_camera_projection", Camera.projection);
        current_shader.set_uniform("uniform_camera_view", Camera.view);
        current_shader.set_uniform("uniform_texture", 0);
        World.render();
        current_shader.unbind();
    }

    public static void after_render() {}

    public static void destroy() {
        World.destroy();
        if (null != terrain_shader) {
            terrain_shader.dispose();
        }
        if (occlusion && null != occlusion_shader) {
            occlusion_shader.dispose();
        }
        if (null != chunks_workers_executor_service) {
            chunks_workers_executor_service.es.shutdown();
        }
    }
}

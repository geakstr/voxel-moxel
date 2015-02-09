package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;
import me.geakstr.voxel.util.ResourceUtil;
import me.geakstr.voxel.workers.ChunksWorkersExecutorService;

import static org.lwjgl.opengl.GL11.*;

public class Game {
    public static Transform world_transform;
    public static ChunksWorkersExecutorService chunks_workers_executor_service;
    public static Shader terrain_shader, occlusion_shader;

    public static void init() {
        ResourceUtil.load_textures("atlas.png");

        Camera.init(100, (float) Window.width / (float) Window.height, 0.01f, 1000f);

        terrain_shader = new Shader("terrain.vs", "terrain.fs").compile();
        terrain_shader.save_attr("attr_pos").save_attr("attr_tex_offset").save_attr("attr_tex_coord").save_attr("attr_color");

        occlusion_shader = new Shader("occlusion.vs", "occlusion.fs").compile();
        occlusion_shader.save_attr("attr_pos");

        world_transform = new Transform();

        chunks_workers_executor_service = new ChunksWorkersExecutorService();

        World.init(64, 1, 16, 16, 256);
        World.gen();
    }

    public static void before_render() {
        Camera.input();
        Camera.apply();

        Frustum.update();
    }

    static boolean odd_frame = true;

    public static void render() {
        occlusion_shader.bind();
        occlusion_shader.set_uniform("uniform_transform", world_transform.getTransform());
        occlusion_shader.set_uniform("uniform_camera_projection", Camera.projection);
        occlusion_shader.set_uniform("uniform_camera_view", Camera.view);
        glDisable(GL_DEPTH_TEST);
        glColorMask(false, false, false, false);
        glDepthMask(false);
        World.occlusion_render();
        occlusion_shader.unbind();


        terrain_shader.bind();
        terrain_shader.set_uniform("uniform_transform", world_transform.getTransform());
        terrain_shader.set_uniform("uniform_camera_projection", Camera.projection);
        terrain_shader.set_uniform("uniform_camera_view", Camera.view);
        terrain_shader.set_uniform("uniform_texture", 0);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glColorMask(true, true, true, true);
        World.render();
        terrain_shader.unbind();
    }

    public static void after_render() {}

    public static void destroy() {
        World.destroy();
        if (null != terrain_shader) {
            terrain_shader.dispose();
        }
        if (null != occlusion_shader) {
            occlusion_shader.dispose();
        }
        if (null != chunks_workers_executor_service) {
            chunks_workers_executor_service.es.shutdown();
        }
    }
}

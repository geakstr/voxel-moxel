package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.math.Vector4f;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.*;
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
            occlusion_shader.save_attr("attr_pos").save_attr("attr_color").save_attr("attr_tex_coord").save_attr("attr_tex_offset");
        }

        world_transform = new Transform();

        chunks_workers_executor_service = new ChunksWorkersExecutorService();

        World.init(1, 1, 1, 1, 2);
        World.gen();
    }

    public static void before_render() {
        Camera.input();
        Camera.apply();

        Frustum.update();

        Vector2f mouse = Input.getMousePosition();
        mouse.x = mouse.x / Window.width * 2f - 1f;
        mouse.y = mouse.y / Window.height * 2f - 1f;

        Matrix4f proj = new Matrix4f(Camera.projection);
        Matrix4f model = new Matrix4f(Camera.view);

        Matrix4f inverse_mvp = new Matrix4f();
        Matrix4f.mul(proj, model, inverse_mvp);
        inverse_mvp.invert();

        Vector3f ray_origin = new Vector3f(
                -model.m30,
                -model.m31,
                -model.m32
        );

        Vector4f p0 = Vector4f.mul(new Vector4f(inverse_mvp), new Vector4f(mouse.x, mouse.y, -1f, 1f), null);
        Vector4f p1 = Vector4f.mul(new Vector4f(inverse_mvp), new Vector4f(mouse.x, mouse.y, 1f, 1f), null);

        Vector3f p0_norm = new Vector3f(
                p0.x / p0.w,
                p0.y / p0.w,
                p0.z / p0.w
        );

        Vector3f p1_norm = new Vector3f(
                p1.x / p0.w,
                p1.y / p0.w,
                p1.z / p0.w
        );

        Vector3f ray_direction = Vector3f.sub(new Vector3f(p1.x, p1.y, p1.z), new Vector3f(p0.x, p0.y, p0.z), null);
        //ray_direction = ray_direction.normalise(ray_direction);

        ray = new Ray(ray_origin, ray_direction);

        if (World.chunks[0][0][0].blocks[0][0][0].intersect(ray, -1, 1)) {
            System.out.println(World.chunks[0][0][0].blocks[0][0][0].intersect(ray, -1, 1));
        }
    }

    public static void render() {
        if (occlusion) {
            current_shader = occlusion_shader;
            current_shader.bind();
            current_shader.set_uniform("uniform_transform", world_transform.getTransform());
            current_shader.set_uniform("uniform_camera_projection", Camera.projection);
            current_shader.set_uniform("uniform_camera_view", Camera.view);
            glDisable(GL_DEPTH_TEST);
            glColorMask(false, false, false, false);
            glDepthMask(false);
            World.occlusion_render();
            current_shader.unbind();
        }

        current_shader = terrain_shader;
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

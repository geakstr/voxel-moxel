package me.geakstr.voxel.game;

import me.geakstr.voxel.core.Window;
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

import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Game {
    public static boolean frustum;

    public static Transform world_transform;
    public static ChunksWorkersExecutorService chunks_workers_executor_service;
    public static Shader current_shader, world_shader, gui_shader, shadow_shader;

    public static Player player;

    public static Vector2f chunk_shader_texture_info = new Vector2f(TextureAtlas.atlas_size, TextureAtlas.crop_size);
    public static Vector2f model_shader_texture_info = new Vector2f(0, 0);

    public static void init() {
        chunks_workers_executor_service = new ChunksWorkersExecutorService();

        gui_shader = new Shader("gui").compile();
        gui_shader.save_attrs("attr_pos", "attr_color");

        world_shader = new Shader("world").compile();
        world_shader.save_attrs("attr_pos", "attr_tex_offset", "attr_tex_coord", "attr_color", "attr_normal");

        shadow_shader = new Shader("shadow").compile();
        shadow_shader.save_attrs("attrs_pos", "attr_tex_offset", "attr_tex_coord", "attr_color", "attr_normal");

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

        generateShadowFBO();
    }

    private static int fboId;
    private static int depthTextureId;
    private static float[] p_light = new float[]{3, 20, 0};

    private static void generateShadowFBO() {
        int shadowMapWidth = Window.width * 2;
        int shadowMapHeight = Window.height * 2;

        int FBOstatus;

        // Try to use a texture depth component
        depthTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTextureId);

        // GL_LINEAR does not make sense for depth texture. However, next tutorial shows usage of GL_LINEAR and PCF
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Remove artifact on the edges of the shadowmap
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

        // No need to force GL_DEPTH_COMPONENT24, drivers usually give you the max precision if available
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, shadowMapWidth, shadowMapHeight, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        // create a framebuffer object
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);

        // Instruct openGL that we won't bind a color texture with the currently bound FBO
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        // attach the texture to FBO depth attachment point
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTextureId, 0);

        // check FBO status
        FBOstatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (FBOstatus != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("GL_FRAMEBUFFER_COMPLETE_EXT failed, CANNOT use FBO\n");
        }

        // switch back to window-system-provided framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
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
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glUseProgramObjectARB(0);
        glViewport(0, 0, Window.width * 2, Window.height * 2);
        glClear(GL_DEPTH_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glCullFace(GL_FRONT);
        current_shader = shadow_shader;
        current_shader.bind();
        current_shader.set_uniform("uniform_transform", world_transform.getTransform());
        current_shader.set_uniform("uniform_camera_projection", Camera.projection);
        current_shader.set_uniform("uniform_camera_view", Camera.view);

        current_shader.unbind();

//        glColorMask(false, false, false, false);
//        glPolygonOffset(8.0f, 4.0f);
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

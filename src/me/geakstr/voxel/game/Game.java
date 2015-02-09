package me.geakstr.voxel.game;

import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDepthMask;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.render.Shader;
import me.geakstr.voxel.render.Transform;
import me.geakstr.voxel.util.ResourceUtil;

public class Game {
    public static Transform world_transform;
    public static Shader terrain_shader, occlusion_shader;

    public static void init() {
        ResourceUtil.load_textures("atlas.png");

        Camera.init(100, (float) Window.width / (float) Window.height, 0.01f, 500f);

        terrain_shader = new Shader("terrain.vs", "terrain.fs").compile();
        terrain_shader.save_attr("attr_pos").save_attr("attr_tex_offset").save_attr("attr_tex_coord").save_attr("attr_color");
        
        occlusion_shader = new Shader("occlusion.vs", "occlusion.fs").compile();
        occlusion_shader.save_attr("attr_pos");

        world_transform = new Transform();

        World.init(16, 1, 16, 16, 64);
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
    	glColorMask(false, false, false, false);
		glDepthMask(false);
        World.occlusion_render();
        glDepthMask(true);
        glColorMask(true, true, true, true);
        occlusion_shader.unbind();

    	terrain_shader.bind();
        terrain_shader.set_uniform("uniform_transform", world_transform.getTransform());
        terrain_shader.set_uniform("uniform_camera_projection", Camera.projection);
        terrain_shader.set_uniform("uniform_camera_view", Camera.view);
        terrain_shader.set_uniform("uniform_texture", 0);
        World.render();
        terrain_shader.unbind();
    }

    public static void after_render() {
    }
}

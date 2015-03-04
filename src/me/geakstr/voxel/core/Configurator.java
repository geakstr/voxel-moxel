package me.geakstr.voxel.core;

import java.io.FileReader;
import java.io.IOException;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.AABB;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.model.TextureAtlas;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class Configurator {
    public static boolean unsynchronized_buffering = true;

    public static void init(String path_to_config) {
        JsonObject json = null;
        try {
            json = JsonObject.readFrom(new FileReader(path_to_config));

            JsonArray screen_resolution = json.get("screen_resolution").asArray();
            Window.width = screen_resolution.get(0).asInt();
            Window.height = screen_resolution.get(1).asInt();
            Window.one_pixel_size = 2.0f / Math.min(Window.width, Window.height);
            Window.vsync = json.get("vsync").asBoolean();

            Camera.fov = json.get("camera_fov").asFloat();
            Camera.z_far = json.get("camera_z_far").asFloat();

            JsonArray world_sizes = json.get("world_sizes").asArray();
            World.size = world_sizes.get(0).asInt();
            World.height = world_sizes.get(1).asInt();
            Chunk.size = world_sizes.get(2).asInt();

            Game.frustum = json.get("frustum").asBoolean();
            Configurator.unsynchronized_buffering = json.get("unsynchronized_buffering").asBoolean();

            TextureAtlas.fill(json.get("texture_atlas").asObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

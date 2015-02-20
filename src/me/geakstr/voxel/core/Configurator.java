package me.geakstr.voxel.core;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.TextureAtlas;
import me.geakstr.voxel.model.World;
import me.geakstr.voxel.render.Camera;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Configurator {
    public static boolean unsynchronized_buffering = true;

    public static void init(String path_to_config) {
        JSONObject json = null;
        try {
            json = (JSONObject) JSONValue.parse(new FileReader(path_to_config));

            JSONArray screen_resolution = (JSONArray) json.get("screen_resolution");
            Window.width = (int) ((long) screen_resolution.get(0));
            Window.height = (int) ((long) screen_resolution.get(1));
            Window.vsync = (boolean) json.get("vsync");

            Camera.fov = (float) ((long) json.get("camera_fov"));
            Camera.z_far = (float) ((long) json.get("camera_z_far"));

            JSONArray world_sizes = (JSONArray) json.get("world_sizes");
            World.world_size = (int) ((long) world_sizes.get(0));
            World.world_height = (int) ((long) world_sizes.get(1));
            World.chunk_size = (int) ((long) world_sizes.get(2));
            World.chunk_height = (int) ((long) world_sizes.get(3));

            Game.frustum = (boolean) json.get("frustum");
            Configurator.unsynchronized_buffering = (boolean) json.get("unsynchronized_buffering");

            TextureAtlas.fill((JSONObject) json.get("texture_atlas"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

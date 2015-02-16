package me.geakstr.voxel.core;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.model.TextureAtlas;
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

            Game.frustum = (boolean) json.get("frustum");
            Configurator.unsynchronized_buffering = (boolean) json.get("unsynchronized_buffering");

            TextureAtlas.fill((JSONObject) json.get("texture_atlas"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

package me.geakstr.voxel.model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import me.geakstr.voxel.math.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class TextureAtlas {
    public static final float atlas_size = 80;
    public static final float crop_size = 16;

    public static final float gl_pixel_size = 1.0f / atlas_size;
    public static final float gl_crop_size = gl_pixel_size * crop_size;

    public static final Map<String, Integer> atlas_title_id = new HashMap<>();
    public static final Map<String, Vector2f> atlas_title_coords = new HashMap<>();

    public static void fill(JsonObject atlas) {
        int idx = 1;
        for (JsonObject.Member atlas_entry : atlas) {
            String key = atlas_entry.getName();
            JsonArray coords = atlas_entry.getValue().asArray();

            atlas_title_coords.put(key, new Vector2f(coords.get(0).asFloat() * gl_crop_size, coords.get(1).asFloat() * gl_crop_size));
            atlas_title_id.put(key, idx++);
        }
    }

    public static Vector2f get_coord(String title) {
        return atlas_title_coords.get(title);
    }
}

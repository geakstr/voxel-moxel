package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TextureAtlas {
    public static final int atlas_size = 80;
    public static final int crop_size = 16;

    public static final float gl_pixel_size = (float) 1 / (float) atlas_size;
    public static final float gl_crop_size = gl_pixel_size * crop_size;

    public static final Map<String, Integer> atlas_title_id = new HashMap<>();
    public static final Map<String, Vector2f> atlas_title_coords = new HashMap<>();

    public static void fill(JSONObject atlas) {
        int idx = 1;
        for (Object block_type : atlas.keySet()) {
            String key = (String) block_type;
            JSONArray coords = (JSONArray) atlas.get(key);

            atlas_title_coords.put(key, new Vector2f((int) ((long) coords.get(0)) * gl_crop_size, (int) ((long) coords.get(1)) * gl_crop_size));
            atlas_title_id.put(key, idx++);
        }
    }

    public static Vector2f get_coord(String title) {
        return atlas_title_coords.get(title);
    }
}

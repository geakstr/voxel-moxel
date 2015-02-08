package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class TextureAtlas {
    public static final int atlas_size = 80;
    public static final int crop_size = 16;

    public static final float gl_pixel_size = (float) 1 / (float) atlas_size;
    public static final float gl_crop_size = gl_pixel_size * crop_size;

    public static final Map<String, Integer> title_to_id = new HashMap<String, Integer>() {{
        put("grass", 1);
        put("stone", 2);
    }};

    public static final Map<String, Vector2f> atlas = new HashMap<String, Vector2f>() {{
        put("grass", new Vector2f(0, 0));
        put("stone", new Vector2f(gl_crop_size, 0));
        put("dirt", new Vector2f(gl_crop_size * 2, 0));
        put("dirt_with_grass", new Vector2f(gl_crop_size * 3, 0));
        put("wood_0", new Vector2f(gl_crop_size * 4, 0));

        put("cobblestone", new Vector2f(0, gl_crop_size));
    }};

}

package me.geakstr.voxel.util;

public class ResourceUtil {
    public static String load_shader(String shader_name) {
        return FileUtil.readFromFile("res/shaders/" + shader_name);
    }
}

package me.geakstr.voxel.util;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;


public class ResourceUtil {
    private static Map<String, Integer> textures = new HashMap<>();

    public static String load_shader(String shader_name) {
        return FileUtil.readFromFile("res/shaders/" + shader_name);
    }

    public static void load_textures(String... names) {
        for (String name : names) load_texture(name);
    }

    public static void gen_colored_texture() {
        int width = 4096, height = 4096;

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 3);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int r = 0, g = 0, b = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buffer.put((byte) r);
                buffer.put((byte) g);
                buffer.put((byte) b);

                bufferedImage.setRGB(x, y, (r << 16) | (g << 8) | b);

                r++;
                if (r > 255) {
                    r = 0;
                    g++;
                    if (g > 255) {
                        g = 0;
                        b++;
                    }
                }
            }
        }
        buffer.flip();

        int texture_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture_id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);

        textures.put("_colors", texture_id);
    }

    public static int load_texture(String texture_name) {
        if (textures.containsKey(texture_name)) {
            return textures.get(texture_name);
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("res/textures/" + texture_name));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load Texture: " + texture_name);
            System.exit(1);
        }

        int[] pixels = new int[img.getWidth() * img.getHeight()];
        img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = pixels[y * img.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        int texture_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture_id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        textures.put(texture_name, texture_id);
        return texture_id;
    }

    public static int texture_id(String texture_name) {
        return textures.get(texture_name);
    }

}

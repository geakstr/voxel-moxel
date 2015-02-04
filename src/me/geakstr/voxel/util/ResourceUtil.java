package me.geakstr.voxel.util;

import me.geakstr.voxel.model.Texture;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

public class ResourceUtil {
	
	private static Set<String> loadedTextures = new HashSet<String>();
	private static Map<Integer, Texture> textures = new HashMap<Integer, Texture>();
	
    public static String load_shader(String shader_name) {
        return FileUtil.readFromFile("res/shaders/" + shader_name);
    }
    
    public static void loadTextures(String... names) {
        for (String name : names) load_texture(name);
    }

    public static Texture load_texture(String texture_name) {
        if (loadedTextures.contains(texture_name)) {
            for (Texture texture : textures.values()) {
                if (texture.name.equals(texture_name)) return texture;
            }
        }
        loadedTextures.add(texture_name);

        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File("res/textures/" + texture_name));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load Texture: " + texture_name);
            System.exit(1);
        }

        int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
        bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0, bimg.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(bimg.getWidth() * bimg.getHeight() * 4);
        
        // Iterate through all the pixels and add them to the ByteBuffer
        for (int y = 0; y < bimg.getHeight(); y++) {
            for (int x = 0; x < bimg.getWidth(); x++) {
                // Select the pixel
                int pixel = pixels[y * bimg.getWidth() + x];
                // Add the RED component
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                // Add the GREEN component
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                // Add the BLUE component
                buffer.put((byte) (pixel & 0xFF));
                // Add the ALPHA component
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        Texture newTexture = new Texture(textureID, bimg.getWidth(), bimg.getHeight(), texture_name);
        textures.put(textureID, newTexture);
        return newTexture;
    }
    
    public static int getTexturesID(int id) {
        return textures.get(id).id;
    }
    
}

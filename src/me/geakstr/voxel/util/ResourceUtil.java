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

public class ResourceUtil {

	private static Map<String, Integer> textures = new HashMap<String, Integer>();
	
    public static String load_shader(String shader_name) {
        return FileUtil.readFromFile("res/shaders/" + shader_name);
    }
    
    public static void loadTextures(String... names) {
        for (String name : names) load_texture(name);
    }

    public static int load_texture(String texture_name) {
        if (textures.get(texture_name) != null) {
            return textures.get(texture_name);
        }

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

        textures.put(texture_name, textureID);
        return textureID;
    }
    
    public static int getTexturesID(String texture_name) {
        return textures.get(texture_name);
    }
    
}

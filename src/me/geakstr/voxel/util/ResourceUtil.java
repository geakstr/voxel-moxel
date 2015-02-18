package me.geakstr.voxel.util;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.Model;

import org.lwjgl.BufferUtils;


public class ResourceUtil {
    public static final Map<String, Integer> textures = new HashMap<>();
    public static final Map<String, Model> models = new HashMap<>(); 

    public static String load_shader(String shader_name) {
        return FileUtil.readFromFile("res/shaders/" + shader_name);
    }

    public static void load_textures(String... names) {
        for (String name : names) load_texture(name);
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
    
    public static void load_models(String... models) {
    	for (String model : models) {
    		load_model(model);
    	}
    }
    
    public static void load_model(String model_name) {
    	FileUtil.Reader reader = new FileUtil.Reader("res/models/" + model_name + "/" + model_name + ".obj");

    	List<int[]> faces = new ArrayList<>();
    	List<Vector3f> verts = new ArrayList<>();
        List<Vector2f> tex_coords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        while (reader.ready()) {
        	String[] tokens = reader.tokens();

        	if ("v".equals(tokens[0])) {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                verts.add(new Vector3f(x, y, z));
            } else if ("f".equals(tokens[0])) {
                String[] t1 = tokens[1].split("/");
                String[] t2 = tokens[2].split("/");
                String[] t3 = tokens[3].split("/");

                int v1 = Integer.parseInt(t1[0]) - 1;
                int v2 = Integer.parseInt(t2[0]) - 1;
                int v3 = Integer.parseInt(t3[0]) - 1;

                int u1 = Integer.parseInt(t1[1]) - 1;
                int u2 = Integer.parseInt(t2[1]) - 1;
                int u3 = Integer.parseInt(t3[1]) - 1;

                int n1 = Integer.parseInt(t1[2]) - 1;
                int n2 = Integer.parseInt(t2[2]) - 1;
                int n3 = Integer.parseInt(t3[2]) - 1;

                faces.add(new int[]{v1, v2, v3, u1, u2, u3, n1, n2, n3});
            } else if ("vt".equals(tokens[0])) {
                float u = Float.parseFloat(tokens[1]);
                float v = Float.parseFloat(tokens[2]);
                tex_coords.add(new Vector2f(u, v));
            } else if ("vn".equals(tokens[0])) {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                normals.add(new Vector3f(x, y, z));
            }
        }
        reader.close();
        
        models.put(model_name.split("/")[0], new Model(faces, verts, tex_coords, normals));
    }

}

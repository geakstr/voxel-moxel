package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.util.OpenSimplexNoise;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class World {
    public static int size, height, volume, vert_square;

    public static int chunks_in_frame = 0;
    public static int faces_in_frame = 0;

    public static final int near_chunks_radius = 2;

    public static Set<Chunk> nearest_chunks = new HashSet<>();

    private static Chunk[] chunks;

    public static void init() {
    	vert_square = size * height;
        volume = size * vert_square;
        
        Chunk.vert_square = Chunk.size * Chunk.height;
        Chunk.volume = Chunk.size * Chunk.vert_square;

        chunks = new Chunk[volume];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    chunk(new Chunk(x, y, z), x, y, z);
                }
            }
        }

        gen();
    }

    public static Chunk chunk(int x, int y, int z) {
        return chunks[vert_square * z + size * y + x];
    }

    public static void chunk(Chunk chunk, int x, int y, int z) {
        chunks[vert_square * z + size * y + x] = chunk;
    }

    public static Chunk chunk_by_global_coords(int global_x, int global_y, int global_z) {
        if (global_x < 0 || global_x >= World.size * Chunk.size ||
                global_y < 0 || global_y >= World.height * Chunk.height ||
                global_z < 0 || global_z >= World.size * Chunk.size) {
            return null;
        }

        int chunk_x = global_x / (Chunk.size);
        int chunk_y = global_y / (Chunk.height);
        int chunk_z = global_z / (Chunk.size);

        return chunk(chunk_x, chunk_y, chunk_z);
    }

    public static void update() {
        nearest_chunks = nearest_chunks(near_chunks_radius);
    }

    public static void render() {
        chunks_in_frame = 0;
        faces_in_frame = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    if (!Game.frustum || Frustum.chunkInFrustum(x, y, z)) {
                        chunk(x, y, z).render();
                    }
                }
            }
        }
    }

    public static void destroy() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    if (null != chunk(x, y, z)) {
                        chunk(x, y, z).destroy();
                    }
                }
            }
        }
    }

    private static void gen() {
        Random rnd = new Random();
        OpenSimplexNoise noise = new OpenSimplexNoise(0);

        for (int global_x = 0; global_x < size * Chunk.size; global_x++) {
            for (int global_z = 0; global_z < size * Chunk.size; global_z++) {
                double global_y = ((noise.eval(global_x / 256.0f, global_z / 256.0f, 1.0) + 1)) * height * Chunk.height / 2;

                int chunk_x = global_x / (Chunk.size);
                int chunk_z = global_z / (Chunk.size);

                int cube_x = global_x % Chunk.size;
                int cube_z = global_z % Chunk.size;

                int chunk_vert_size = (int) (Math.ceil(global_y / Chunk.height));
                for (int chunk_y = 0; chunk_y < chunk_vert_size; chunk_y++) {
                    Chunk chunk = chunk(chunk_x, chunk_y, chunk_z);

                    int height = chunk_y == chunk_vert_size - 1 ? (int) (global_y % Chunk.height) : Chunk.height;

                    for (int cube_y = 0; cube_y < height; cube_y++) {
                        chunk.block_type(1, cube_x, cube_y, cube_z);
                    }
                }
            }
        }
    }

    private static Set<Chunk> nearest_chunks(int chunk_x, int chunk_z, int r) {
        Set<Chunk> ret = new HashSet<>();

        for (int yy = 0; yy < World.height; yy++) {
            for (int xx = Math.max(chunk_x - r, 0); xx <= Math.min(chunk_x + r, World.size - 1); xx++) {
                for (int zz = Math.max(chunk_z - r, 0); zz <= Math.min(chunk_z + r, World.size - 1); zz++) {
                    ret.add(chunk(xx, yy, zz));
                }
            }
        }

        return ret;
    }

    private static Set<Chunk> nearest_chunks(int r) {
        Chunk cur_chunk = chunk_by_global_coords((int) -Camera.position.x, (int) -Camera.position.y, (int) -Camera.position.z);
        if (null == cur_chunk) {
            return new HashSet<>();
        }
        return nearest_chunks(cur_chunk.x_chunk_pos, cur_chunk.z_chunk_pos, r);
    }
}

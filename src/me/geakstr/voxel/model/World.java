package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.util.OpenSimplexNoise;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class World {
    public static int world_size;
    public static int world_height;
    public static int world_volume; // world_size^2 * world_height loaded chunks

    public static int chunk_size;
    public static int chunk_height;
    public static int chunk_volume; // chunk_size^2 * chunk_height blocks in chunk

    public static Chunk[][][] chunks;

    public static int chunks_in_frame = 0;
    public static int faces_in_frame = 0;

    public static final int near_chunks_radius = 2;

    public static Set<Chunk> nearest_chunks = new HashSet<>();

    public static void init() {
        world_volume = world_size * world_size * world_height;
        chunk_volume = chunk_size * chunk_size * chunk_height;

        chunks = new Chunk[world_height][world_size][world_size];

        for (int y = 0; y < world_height; y++) {
            for (int x = 0; x < world_size; x++) {
                for (int z = 0; z < world_size; z++) {
                    chunks[y][x][z] = new Chunk(x, y, z);
                }
            }
        }

        gen();
    }

    public static Chunk chunk_by_global_coords(int global_x, int global_y, int global_z) {
        if (global_x < 0 || global_x >= World.world_size * World.chunk_size ||
                global_y < 0 || global_y >= World.world_height * World.chunk_height ||
                global_z < 0 || global_z >= World.world_size * World.chunk_size) {
            return null;
        }

        int chunk_x = global_x / (chunk_size);
        int chunk_y = global_y / (chunk_height);
        int chunk_z = global_z / (chunk_size);

        return chunks[chunk_y][chunk_x][chunk_z];
    }

    public static void update() {
        nearest_chunks = nearest_chunks(near_chunks_radius);
    }

    public static void render() {
        chunks_in_frame = 0;
        faces_in_frame = 0;

        for (int y = 0; y < world_height; y++) {
            for (int x = 0; x < world_size; x++) {
                for (int z = 0; z < world_size; z++) {
                    if (!Game.frustum || Frustum.chunkInFrustum(x, y, z)) {
                        chunks[y][x][z].render();
                    }
                }
            }
        }
    }

    public static void destroy() {
        for (int y = 0; y < world_height; y++) {
            for (int x = 0; x < world_size; x++) {
                for (int z = 0; z < world_size; z++) {
                    if (null != chunks[y][x][z]) {
                        chunks[y][x][z].destroy();
                    }
                }
            }
        }
    }

    private static void gen() {
        Random rnd = new Random();
        OpenSimplexNoise noise = new OpenSimplexNoise(0);

        for (int global_x = 0; global_x < world_size * chunk_size; global_x++) {
            for (int global_y = 0; global_y < world_size * chunk_size; global_y++) {
                double global_z = ((noise.eval(global_x / 256.0f, global_y / 256.0f, 1.0) + 1)) * world_height * chunk_height / 2;

                int chunk_x = global_x / (chunk_size);
                int chunk_y = global_y / (chunk_size);

                int cube_x = global_x % chunk_size;
                int cube_y = global_y % chunk_size;

                int chunk_vert_size = (int) (Math.ceil(global_z / chunk_height));
                for (int chunk_z = 0; chunk_z < chunk_vert_size; chunk_z++) {
                    Chunk chunk = chunks[chunk_z][chunk_x][chunk_y];

                    int height = chunk_z == chunk_vert_size - 1 ? (int) (global_z % chunk_height) : chunk_height;

                    for (int cube_z = 0; cube_z < height; cube_z++) {
                        chunk.blocks[cube_x][cube_y][cube_z] = Block.pack_type(0, 1);
                    }
                }
            }
        }
    }

    private static Set<Chunk> nearest_chunks(int chunk_x, int chunk_z, int r) {
        Set<Chunk> ret = new HashSet<>();

        for (int yy = 0; yy < World.world_height; yy++) {
            for (int xx = Math.max(chunk_x - r, 0); xx <= Math.min(chunk_x + r, World.world_size - 1); xx++) {
                for (int zz = Math.max(chunk_z - r, 0); zz <= Math.min(chunk_z + r, World.world_size - 1); zz++) {
                    ret.add(chunks[yy][xx][zz]);
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

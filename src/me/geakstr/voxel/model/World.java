package me.geakstr.voxel.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.util.OpenSimplexNoise;

public class World {
    public static int world_size;
    public static int world_height;
    public static int world_volume; // world_size * world_size * world_height chunks

    public static int chunk_height; // z axis size
    public static int chunk_width; // x axis size
    public static int chunk_length; // y axis size
    public static int chunk_volume; // height * width * length

    public static Chunk[][][] chunks;

    public static int chunks_in_frame = 0;
    public static int faces_in_frame = 0;

    public static final int near_chunks_radius = 2;
    
    public static void init(int _world_size, int _world_height,
                            int _chunk_width, int _chunk_length, int _chunk_height) {
        world_size = _world_size;
        world_height = _world_height;
        world_volume = world_size * world_size * world_height;

        chunks = new Chunk[world_height][world_size][world_size];

        chunk_width = _chunk_width;
        chunk_length = _chunk_length;
        chunk_height = _chunk_height;
        chunk_volume = _chunk_height * _chunk_width * _chunk_length;

        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                for (int z = 0; z < world_height; z++) {
                    chunks[z][x][y] = new Chunk(x, y, z);
                }
            }
        }
    }

    public static void gen() {
        Random rnd = new Random();
        OpenSimplexNoise noise = new OpenSimplexNoise(0);

        for (int global_x = 0; global_x < world_size * chunk_width; global_x++) {
            for (int global_y = 0; global_y < world_size * chunk_length; global_y++) {
                double global_z = ((noise.eval(global_x / 256.0f, global_y / 256.0f, 1.0) + 1)) * world_height * chunk_height / 2;

                int chunk_x = global_x / (chunk_width);
                int chunk_y = global_y / (chunk_length);

                int cube_x = global_x % chunk_width;
                int cube_y = global_y % chunk_length;

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
    
    public static Chunk get_current_chunk(int global_x, int global_y, int global_z) {
    	if (global_x < 0 || global_x >= World.world_size * World.chunk_width ||
    			global_y < 0 || global_y >= World.world_size * World.chunk_length ||
    			global_z < 0 || global_z >= World.world_height * World.chunk_height) {
    		return null;
    	}
    	
    	int chunk_x = global_x / (chunk_width);
        int chunk_y = global_y / (chunk_length);
        int chunk_z = global_z / (chunk_height);
    	
    	return chunks[chunk_z][chunk_x][chunk_y];
    }
    
    public static Set<Chunk> get_nearest_chunks(int chunk_x, int chunk_y, int chunk_z, int r) {
    	Set<Chunk> ret = new HashSet<Chunk>();
    	
    	for (int xx = Math.max(chunk_x - r, 0); xx <= Math.min(chunk_x + r, World.world_size - 1); xx++) {
    		for (int yy = Math.max(chunk_y - r, 0); yy <= Math.min(chunk_y + r, World.world_size - 1 ); yy++) {
    			for (int hh = 0; hh < World.world_height; hh++) {
    				ret.add(chunks[hh][xx][yy]);
    			}
    		}
    	}
    	
    	return ret;
    }

    public static void render() {
        chunks_in_frame = 0;
        faces_in_frame = 0;

        for (int z = 0; z < world_height; z++) {
            for (int x = 0; x < world_size; x++) {
                for (int y = 0; y < world_size; y++) {
                    if (!Game.frustum || Frustum.chunkInFrustum(x, y, z)) {
                        chunks[z][x][y].render();
                    }
                }
            }
        }
    }

    public static void destroy() {
        for (int z = 0; z < world_height; z++) {
            for (int x = 0; x < world_size; x++) {
                for (int y = 0; y < world_size; y++) {
                    if (null != chunks[z][x][y]) {
                        chunks[z][x][y].destroy();
                    }
                }
            }
        }
    }
}

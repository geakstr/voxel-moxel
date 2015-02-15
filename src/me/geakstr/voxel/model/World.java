package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Frustum;
import me.geakstr.voxel.util.OpenSimplexNoise;

import java.util.Random;

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
                double global_z = ((noise.eval(global_x / 128.0f, global_y / 128.0f, 1.0) + 1)) * world_height * chunk_height / 2;

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

    public static Block mouse_selection() {
        Chunk selection_chunk = null;

        final Vector3f camera_position = Camera.position.negate(null);

        float min_dist = Float.MAX_VALUE;
        for (int chunk_z = 0; chunk_z < World.world_height; chunk_z++) {
            for (int chunk_x = 0; chunk_x < World.world_size; chunk_x++) {
                for (int chunk_y = 0; chunk_y < World.world_size; chunk_y++) {
                    Vector3f min = new Vector3f(World.chunk_width * chunk_x, World.chunk_height * chunk_z, World.chunk_length * chunk_y);
                    Vector3f max = new Vector3f(min.x + World.chunk_width, min.y + World.chunk_height, min.z + World.chunk_length);
                    if (Game.ray.intersect(new Box(min, max), -100, -1)) {
                        float dist = Vector3f.dist(min, camera_position);
                        if (dist < min_dist) {
                            selection_chunk = World.chunks[chunk_z][chunk_x][chunk_y];
                            min_dist = dist;
                        }
                    }
                }
            }
        }

        Block selection_block = null;
        if (selection_chunk != null) {
            min_dist = Float.MAX_VALUE;
            for (int block_x = 0; block_x < World.chunk_width; block_x++) {
                for (int block_y = 0; block_y < World.chunk_length; block_y++) {
                    for (int block_z = 0; block_z < World.chunk_height; block_z++) {
                        Block block = new Block(new Vector3f(block_x, block_y, block_z), selection_chunk);
                        if (Block.unpack_type(selection_chunk.blocks[block_x][block_y][block_z]) != 0 && Game.ray.intersect(block, -100, -1)) {
                            float dist = Vector3f.dist(block.corners[0], camera_position);
                            if (dist < min_dist) {
                                selection_block = block;
                                min_dist = dist;
                            }
                        }
                    }
                }
            }
        }
        return selection_block;
    }
}

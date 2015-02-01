package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;

import java.util.Random;

public class World {
    public static int world_size; // world_size * world_size chunks

    public static int chunk_height; // z axis size
    public static int chunk_width; // x axis size
    public static int chunk_length; // y axis size
    public static int chunk_volume; // height * width * length

    public static Chunk[][] chunks;

    public static int chunks_in_frame = 0;

    public static void init(int _world_size, int _chunk_width, int _chunk_length, int _chunk_height) {
        world_size = _world_size;
        chunks = new Chunk[world_size][world_size];

        chunk_width = _chunk_width;
        chunk_length = _chunk_length;
        chunk_height = _chunk_height;
        chunk_volume = _chunk_height * _chunk_width * _chunk_length;

        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                chunks[x][y] = new Chunk(x, y);
            }
        }
    }

    public static void gen() {
        Random rnd = new Random();
        for (int i = 0; i < world_size; i++) {
            for (int j = 0; j < world_size; j++) {
                Chunk chunk = chunks[i][j];
                chunk.gen_buffers();

                for (int y = 0; y < World.chunk_length; y++) {
                    for (int x = 0; x < World.chunk_width; x++) {
                        for (int z = 0; z < World.chunk_height; z++) {
                            chunk.cubes[x][y][z] = Cube.pack_type(0, rnd.nextInt(2));
                        }
                    }
                }
            }
        }
    }

    public static void render() {
        chunks_in_frame = 0;
        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                if (Game.frustum.chunkInFrustum(x, y)) {
                    chunks_in_frame++;
                    chunks[x][y].render();
                }
            }
        }
    }
}

package me.geakstr.voxel.model;

import java.util.Random;

public class World {
    public int world_size; // world_size * world_size chunks

    public static int chunk_height; // z axis size
    public static int chunk_width; // x axis size
    public static int chunk_length; // y axis size
    public static int chunk_volume; // height * width * length

    public Chunk[][] chunks;

    public World(int world_size, int _chunk_height, int _chunk_width, int _chunk_length) {
        this.world_size = world_size;
        this.chunks = new Chunk[world_size][world_size];

        chunk_height = _chunk_height;
        chunk_width = _chunk_width;
        chunk_length = _chunk_length;
        chunk_volume = _chunk_height * _chunk_width * _chunk_length;

        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                chunks[x][y] = new Chunk(x * chunk_width, y * chunk_length);
            }
        }
    }

    public World gen() {
        Random rnd = new Random();
        for (int i = 0; i < world_size; i++) {
            for (int j = 0; j < world_size; j++) {
                Chunk chunk = chunks[i][j];
                chunk.gen_buffers();

                for (int z = 0; z < World.chunk_height; z++) {
                    for (int x = 0; x < World.chunk_width; x++) {
                        for (int y = 0; y < World.chunk_length; y++) {
                            chunk.cubes[z][x][y] = rnd.nextInt(2);
                        }
                    }
                }
            }
        }
        return this;
    }

    public void render() {
        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                chunks[x][y].render();
            }
        }
    }
}

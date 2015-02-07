package me.geakstr.voxel.model;

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

    public static void init(int _world_size, int _world_height, int _chunk_width, int _chunk_length, int _chunk_height) {
        world_size = _world_size;
        world_height = _world_height;
        world_volume = world_size * world_size * world_height;

        chunks = new Chunk[world_size][world_size][world_height];

        chunk_width = _chunk_width;
        chunk_length = _chunk_length;
        chunk_height = _chunk_height;
        chunk_volume = _chunk_height * _chunk_width * _chunk_length;

        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                for (int z = 0; z < world_height; z++) {
                    chunks[x][y][z] = new Chunk(x, y, z);
                }
            }
        }
    }

    public static void gen() {
        Random rnd = new Random();
        for (int i = 0; i < world_size; i++) {
            for (int j = 0; j < world_size; j++) {
                for (int k = 0; k < world_height; k++) {
                    Chunk chunk = chunks[i][j][k];
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
    }

    public static void new_gen() {
        /*
        OpenSimplexNoise noise = new OpenSimplexNoise(100500);
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        for (int z = 0; z < HEIGHT; z++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, z / FEATURE_SIZE);
                    int rgb = 0x010101 * (int) ((value + 1) * 127.5);
                    image.setRGB(x, y, rgb);
                }
            }
        }
        ImageIO.write(image, "png", new File("noise.png"));
         */
        Random rnd = new Random();
        OpenSimplexNoise noise = new OpenSimplexNoise();
        Chunk chunk = chunks[0][0][0];
        chunk.gen_buffers();
        for (int x = 0; x < 8; x++) {
            int chunk_x = x % world_size;
            for (int y = 0; y < 8; y++) {
                int chunk_y = y % world_size;

                int z = rnd.nextInt(4);
                int chunk_z = z % world_height;




//                int cube_x = x % chunk_width;
//                int cube_y = y % chunk_length;
//                int cube_z = z % chunk_height;

                for (int cube_zz = 0; cube_zz < z; cube_zz++) {
                    //chunk.cubes[x][y][cube_zz] = Cube.pack_type(0, 1);
                }
            }
        }

        chunk.cubes[0][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[1][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[3][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[6][0][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][0][0] = Cube.pack_type(0, 1);

        chunk.cubes[0][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[1][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[3][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[6][1][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][1][0] = Cube.pack_type(0, 0);

        chunk.cubes[0][2][0] = Cube.pack_type(0, 1);
        chunk.cubes[1][2][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][2][0] = Cube.pack_type(0, 1);
        chunk.cubes[3][2][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][2][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][2][0] = Cube.pack_type(0, 0);
        chunk.cubes[6][2][0] = Cube.pack_type(0, 0);
        chunk.cubes[7][2][0] = Cube.pack_type(0, 1);

        chunk.cubes[0][3][0] = Cube.pack_type(0, 0);
        chunk.cubes[1][3][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][3][0] = Cube.pack_type(0, 1);
        chunk.cubes[3][3][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][3][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][3][0] = Cube.pack_type(0, 1);
        chunk.cubes[6][3][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][3][0] = Cube.pack_type(0, 1);

        chunk.cubes[0][4][0] = Cube.pack_type(0, 0);
        chunk.cubes[1][4][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][4][0] = Cube.pack_type(0, 0);
        chunk.cubes[3][4][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][4][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][4][0] = Cube.pack_type(0, 1);
        chunk.cubes[6][4][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][4][0] = Cube.pack_type(0, 1);

        chunk.cubes[0][5][0] = Cube.pack_type(0, 1);
        chunk.cubes[1][5][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][5][0] = Cube.pack_type(0, 0);
        chunk.cubes[3][5][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][5][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][5][0] = Cube.pack_type(0, 1);
        chunk.cubes[6][5][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][5][0] = Cube.pack_type(0, 0);

        chunk.cubes[0][6][0] = Cube.pack_type(0, 1);
        chunk.cubes[1][6][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][6][0] = Cube.pack_type(0, 1);
        chunk.cubes[3][6][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][6][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][6][0] = Cube.pack_type(0, 0);
        chunk.cubes[6][6][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][6][0] = Cube.pack_type(0, 1);

        chunk.cubes[0][7][0] = Cube.pack_type(0, 1);
        chunk.cubes[1][7][0] = Cube.pack_type(0, 1);
        chunk.cubes[2][7][0] = Cube.pack_type(0, 0);
        chunk.cubes[3][7][0] = Cube.pack_type(0, 1);
        chunk.cubes[4][7][0] = Cube.pack_type(0, 1);
        chunk.cubes[5][7][0] = Cube.pack_type(0, 1);
        chunk.cubes[6][7][0] = Cube.pack_type(0, 1);
        chunk.cubes[7][7][0] = Cube.pack_type(0, 0);
    }

    public static void render() {
        chunks_in_frame = 0;
        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                for (int z = 0; z < world_height; z++) {
                    //if (Frustum.chunkInFrustum(x, y, z)) {
                    chunks_in_frame++;
                    chunks[x][y][z].render();
                    //}
                }
            }
        }
    }
}

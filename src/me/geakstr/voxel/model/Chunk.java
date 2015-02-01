package me.geakstr.voxel.model;

import java.util.Arrays;

public class Chunk extends Mesh {
    public int[][][] cubes; // [x][y][z]

    public boolean changed;

    public int x_chunk_pos, y_chunk_pos;
    public int x_offset, y_offset;

    public Chunk(int x_chunk_pos, int y_chunk_pos) {
        super();

        this.x_chunk_pos = x_chunk_pos;
        this.y_chunk_pos = y_chunk_pos;

        this.x_offset = x_chunk_pos * World.chunk_width;
        this.y_offset = y_chunk_pos * World.chunk_length;

        this.vertices_size = World.chunk_volume * Cube.cube_vertices_size;
        //this.indices_size = volume * 36 + width;

        this.vertices = new float[vertices_size];
        //this.indices = new int[indices_size];

        this.cubes = new int[World.chunk_width][World.chunk_length][World.chunk_height];
        this.changed = true;
    }

    public void update() {
        changed = false;

        int vertices_offset = 0/*, indices_offset = 0*/;
        for (int z = 0; z < World.chunk_height; z++) {
            for (int y = 0; y < World.chunk_length; y++) {
                for (int x = 0; x < World.chunk_width; x++) {
                    int val = cubes[x][y][z];

                    if (Cube.unpack_type(val) == 0) {
                        continue;
                    }

                    boolean[] sides = renderable_sides(x, y, z);

                    for (int side = 0; side < 6; side++) {
                        boolean is_side = sides[side];
                        if (is_side) {
                            float[] side_vertices = Cube.get_side(side, x_offset + x, z, y + y_offset/*, vertices_offset*/);
                            System.arraycopy(side_vertices, 0, vertices, vertices_offset, Cube.cube_side_vertices_size);
                            vertices_offset += Cube.cube_side_vertices_size;
                        }
                    }
                }
            }
        }

        vertices = Arrays.copyOfRange(vertices, 0, vertices_offset);
        vertices_size = vertices.length;

        fill_buffers();
    }

    public boolean[] renderable_sides(int x, int y, int z) {
        boolean[] sides = new boolean[6];

        // Blocks inside chunk
        if (x == 0 || Cube.unpack_type(cubes[x - 1][y][z]) == 0) {
            sides[0] = true; // Back
        }
        if (x == World.chunk_width - 1 || Cube.unpack_type(cubes[x + 1][y][z]) == 0) {
            sides[1] = true; // Front
        }
        if (y == World.chunk_length - 1 || Cube.unpack_type(cubes[x][y + 1][z]) == 0) {
            sides[2] = true; // Left
        }
        if (y == 0 || Cube.unpack_type(cubes[x][y - 1][z]) == 0) {
            sides[3] = true; // Right
        }
        if (z == 0 || Cube.unpack_type(cubes[x][y][z - 1]) == 0) {
            sides[4] = true; // Bottom
        }
        if (z == World.chunk_height - 1 || Cube.unpack_type(cubes[x][y][z + 1]) == 0) {
            sides[5] = true; // Top
        }

        // Blocks on edge of chunk
        if (sides[0] &&
                x == 0 &&
                x_chunk_pos != 0 &&
                Cube.unpack_type(World.chunks[x_chunk_pos - 1][y_chunk_pos].cubes[World.chunk_width - 1][y][x]) != 0) {
            sides[0] = false;
        }
        if (sides[1] &&
                x == World.chunk_width - 1 &&
                x_chunk_pos != World.world_size - 1 &&
                Cube.unpack_type(World.chunks[x_chunk_pos + 1][y_chunk_pos].cubes[0][y][z]) != 0) {
            sides[1] = false;
        }
        if (sides[2] &&
                y == World.chunk_length - 1 &&
                y_chunk_pos != World.world_size - 1 &&
                Cube.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos + 1].cubes[x][0][x]) != 0) {
            sides[2] = false;
        }
        if (sides[3] &&
                y == 0 &&
                y_chunk_pos != 0 &&
                Cube.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos - 1].cubes[x][World.chunk_length - 1][z]) != 0) {
            sides[3] = false;
        }

        return sides;
    }

    public void render() {
        if (changed) {
            update();
        }
        super.render();
    }
}

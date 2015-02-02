package me.geakstr.voxel.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        this.vertices_size = World.chunk_volume * CubeManager.cube_vertices_size;
        //this.indices_size = volume * 36 + width;

        this.vertices = new float[vertices_size];
        //this.indices = new int[indices_size];

        this.cubes = new int[World.chunk_width][World.chunk_length][World.chunk_height];
        this.changed = true;
    }

    public void update() {
        changed = false;

        int next_color = 512;
        int vertices_offset = 0/*, indices_offset = 0*/;
        for (int z = 0; z < World.chunk_height; z++) {
            int[][] mark = new int[World.chunk_length][World.chunk_width];
            int[] proj = new int[mark[0].length];
            Arrays.fill(proj, -1);
            int len = 0;
            Map<Integer, int[]> coords_map = new HashMap<>();
            for (int y = 0; y < World.chunk_length; y++) {
                boolean canDown = false;
                int projFlag = -1;
                for (int x = 0; x < World.chunk_width; x++) {
                    int val = cubes[x][y][z];
                    int type = CubeManager.unpack_type(val);

                    int i = y, j = x;

                    if (type == 0) {
                        continue;
                    }

                    boolean update_coords = false;

                    if (proj[j] != -1) {
                        mark[i][j] = mark[i - 1][j];
                        projFlag = j;
                        len = 0;
                        update_coords = true;
                    } else if (((j > 0) && (CubeManager.unpack_type(cubes[j - 1][i][z]) == type) && (projFlag != j - 1))) {
                        mark[i][j] = mark[i][j - 1];
                        update_coords = true;
                    } else {
                        mark[i][j] = ++next_color;
                        coords_map.put(next_color, new int[]{j, i, j, i});
                        len = 0;
                    }

                    if (update_coords) {
                        int[] tmp = null;
                        int face = proj[j] != -1 ? proj[j] : mark[i][j - 1];
                        tmp = coords_map.get(face);
                        tmp[2] = j;
                        tmp[3] = i;
                        coords_map.put(face, tmp);
                    }

                    if (j > 0 && mark[i][j - 1] == mark[i][j]) {
                        len++;
                    }

                    canDown = !(len > 0 && !canDown) && (i < (World.chunk_length - 1) && (CubeManager.unpack_type(cubes[j][i + 1][z]) == type));

                    if (canDown) {
                        proj[j] = mark[i][j];
                    } else {
                        int tmp = len;
                        while (tmp >= 0) {
                            proj[j - tmp] = -1;
                            tmp--;
                        }
                    }
                }
            }

            for (Map.Entry<Integer, int[]> e : coords_map.entrySet()) {
                int[] coords = e.getValue();
                int x0 = x_offset + coords[0], y0 = y_offset + coords[1];
                int x1 = x_offset + coords[2], y1 = y_offset + coords[3];

                // boolean[] renderable_sides = renderable_sides(x0, y0, x1, y1);
                for (int side_idx = 0; side_idx < 6; side_idx++) {
                    // if (renderable_sides[side_idx]) {
                    float[] side = CubeManager.get_side(side_idx, x0, y0, x1, y1, z);

                    System.arraycopy(side, 0, vertices, vertices_offset, CubeManager.cube_side_vertices_size);
                    vertices_offset += CubeManager.cube_side_vertices_size;
                    // }
                }
            }
        }

        vertices = Arrays.copyOfRange(vertices, 0, vertices_offset);
        vertices_size = vertices_offset;

        fill_buffers();
    }

    public boolean[] renderable_sides(int x, int y, int z) {
        boolean[] sides = new boolean[6];

        // Blocks inside chunk
        if (x == 0 || CubeManager.unpack_type(cubes[x - 1][y][z]) == 0) {
            sides[0] = true; // Back
        }
        if (x == World.chunk_width - 1 || CubeManager.unpack_type(cubes[x + 1][y][z]) == 0) {
            sides[1] = true; // Front
        }
        if (y == World.chunk_length - 1 || CubeManager.unpack_type(cubes[x][y + 1][z]) == 0) {
            sides[2] = true; // Right
        }
        if (y == 0 || CubeManager.unpack_type(cubes[x][y - 1][z]) == 0) {
            sides[3] = true; // Left
        }
        if (z == World.chunk_height - 1 || CubeManager.unpack_type(cubes[x][y][z + 1]) == 0) {
            sides[4] = true; // Bottom
        }
        if (z == 0 || CubeManager.unpack_type(cubes[x][y][z - 1]) == 0) {
            sides[5] = true; // Top
        }

        // Blocks on edge of chunk
        if (sides[0] &&
                x == 0 &&
                x_chunk_pos != 0 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos - 1][y_chunk_pos].cubes[World.chunk_width - 1][y][x]) != 0) {
            sides[0] = false;
        }
        if (sides[1] &&
                x == World.chunk_width - 1 &&
                x_chunk_pos != World.world_size - 1 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos + 1][y_chunk_pos].cubes[0][y][z]) != 0) {
            sides[1] = false;
        }
        if (sides[2] &&
                y == World.chunk_length - 1 &&
                y_chunk_pos != World.world_size - 1 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos + 1].cubes[x][0][x]) != 0) {
            sides[2] = false;
        }
        if (sides[3] &&
                y == 0 &&
                y_chunk_pos != 0 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos - 1].cubes[x][World.chunk_length - 1][z]) != 0) {
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

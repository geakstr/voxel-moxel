package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector2f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Chunk extends Mesh {
    public int[][][] cubes; // [x][y][z]

    public boolean changed;

    public int x_chunk_pos, y_chunk_pos, z_chunk_pos;
    public int x_offset, y_offset, z_offset;

    public Chunk(int x_chunk_pos, int y_chunk_pos, int z_chunk_pos) {
        super();

        this.x_chunk_pos = x_chunk_pos;
        this.y_chunk_pos = y_chunk_pos;
        this.z_chunk_pos = z_chunk_pos;

        this.x_offset = x_chunk_pos * World.chunk_width;
        this.y_offset = y_chunk_pos * World.chunk_length;
        this.z_offset = z_chunk_pos * World.chunk_height;

        this.vertices_size = World.chunk_volume * Cube.cube_side_vertices_size * 6;
        this.textures_size = World.chunk_volume * Cube.cube_side_texture_size * 6;
        this.textures_offsets_size = World.chunk_volume * Cube.cube_side_texture_size * 6;

        this.vertices = new int[vertices_size];
        this.textures = new int[textures_size];
        this.textures_offsets = new float[textures_offsets_size];

        this.cubes = new int[World.chunk_width][World.chunk_length][World.chunk_height];
        this.changed = true;
    }

    public void update() {
        changed = false;

        Random rnd = new Random();

        int next_color = 512;
        int vertices_offset = 0, texture_offset = 0, offsets_offset = 0;
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
                    int type = Cube.unpack_type(val);

                    if (type == 0) {
                        continue;
                    }

                    boolean update_coords = false;

                    if (proj[x] != -1) {
                        mark[y][x] = mark[y - 1][x];
                        projFlag = x;
                        len = 0;
                        update_coords = true;
                    } else if (((x > 0) && (Cube.unpack_type(cubes[x - 1][y][z]) == type) && (projFlag != x - 1))) {
                        mark[y][x] = mark[y][x - 1];
                        update_coords = true;
                    } else {
                        mark[y][x] = ++next_color;
                        coords_map.put(next_color, new int[]{x, y, x, y});
                        len = 0;
                    }

                    if (update_coords) {
                        int[] tmp = null;
                        int face = proj[x] != -1 ? proj[x] : mark[y][x - 1];
                        tmp = coords_map.get(face);
                        tmp[2] = x;
                        tmp[3] = y;
                        coords_map.put(face, tmp);
                    }

                    if (x > 0 && mark[y][x - 1] == mark[y][x]) {
                        len++;
                    }

                    canDown = !(len > 0 && !canDown) && (y < (World.chunk_length - 1) && (Cube.unpack_type(cubes[x][y + 1][z]) == type));

                    if (canDown) {
                        proj[x] = mark[y][x];
                    } else {
                        int tmp = len;
                        while (tmp >= 0) {
                            proj[x - tmp] = -1;
                            tmp--;
                        }
                    }
                }
            }

            for (Map.Entry<Integer, int[]> e : coords_map.entrySet()) {
                int[] coords = e.getValue();
                int x0 = coords[0], y0 = coords[1];
                int x1 = coords[2], y1 = coords[3];

                boolean[] renderable_sides = renderable_sides(x0, y0, x1, y1, z);

                Vector2f tex = rnd.nextBoolean() ? TextureAtlas.atlas.get("cobblestone") : TextureAtlas.atlas.get("grass");
                for (int side_idx = 0; side_idx < 6; side_idx++) {
                    if (renderable_sides[side_idx]) {
                        int[] side = Cube.get_side(side_idx, x0 + x_offset, y0 + y_offset, x1 + x_offset, y1 + y_offset, z + z_offset);
                        System.arraycopy(side, 0, vertices, vertices_offset, Cube.cube_side_vertices_size);
                        vertices_offset += Cube.cube_side_vertices_size;

                        int[] texture = Cube.get_texture(side_idx, x0, y0, x1, y1);
                        System.arraycopy(texture, 0, textures, texture_offset, Cube.cube_side_texture_size);
                        texture_offset += Cube.cube_side_texture_size;

                        float[] offset = new float[]{tex.x, tex.y, tex.x, tex.y, tex.x, tex.y, tex.x, tex.y, tex.x, tex.y, tex.x, tex.y,};
                        System.arraycopy(offset, 0, textures_offsets, offsets_offset, Cube.cube_side_texture_size);
                        offsets_offset += Cube.cube_side_texture_size;
                    }
                }
            }
        }

        vertices = Arrays.copyOfRange(vertices, 0, vertices_offset);
        vertices_size = vertices_offset;

        textures = Arrays.copyOfRange(textures, 0, texture_offset);
        textures_size = texture_offset;

        textures_offsets = Arrays.copyOfRange(textures_offsets, 0, offsets_offset);
        textures_offsets_size = offsets_offset;

        prepare_render();
    }

    public boolean[] renderable_sides(int x0, int y0, int x1, int y1, int z) {
        boolean[] sides = new boolean[6];

        if (x0 == 0) {
            sides[0] = true;
        } else if (x0 > 0) {
            for (int y = y0; y <= y1; y++) {
                if (Cube.unpack_type(cubes[x0 - 1][y][z]) == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }
        if (x1 == World.chunk_width - 1) {
            sides[1] = true;
        } else if (x1 < World.chunk_width - 1) {
            for (int y = y0; y <= y1; y++) {
                if (Cube.unpack_type(cubes[x1 + 1][y][z]) == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }

        if (y0 == 0) {
            sides[3] = true;
        } else if (y0 > 0) {
            for (int x = x0; x <= x1; x++) {
                if (Cube.unpack_type(cubes[x][y0 - 1][z]) == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }

        if (y1 == World.chunk_length - 1) {
            sides[2] = true;
        } else if (y1 < World.chunk_length - 1) {
            for (int x = x0; x <= x1; x++) {
                if (Cube.unpack_type(cubes[x][y1 + 1][z]) == 0) {
                    sides[2] = true;
                    break;
                }
            }
        }

        if (z == 0) {
            sides[4] = true;
        } else if (z > 0) {
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Cube.unpack_type(cubes[x][y][z - 1]) == 0) {
                        sides[4] = true;
                        break;
                    }
                }
            }
        }

        if (z == World.chunk_height - 1) {
            sides[5] = true;
        } else if (z < World.chunk_height - 1) {
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Cube.unpack_type(cubes[x][y][z + 1]) == 0) {
                        sides[5] = true;
                        break;
                    }
                }
            }
        }

        if (sides[0] &&
                x0 == 0 &&
                x_chunk_pos != 0) {
            sides[0] = false;
            for (int y = y0; y <= y1; y++) {
                if (Cube.unpack_type(World.chunks[x_chunk_pos - 1][y_chunk_pos][z_chunk_pos].cubes[World.chunk_width - 1][y][z]) == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }

        if (sides[1] &&
                x1 == World.chunk_width - 1 &&
                x_chunk_pos != World.world_size - 1) {
            sides[1] = false;
            for (int y = y0; y <= y1; y++) {
                if (Cube.unpack_type(World.chunks[x_chunk_pos + 1][y_chunk_pos][z_chunk_pos].cubes[0][y][z]) == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }

        if (sides[2] &&
                y1 == World.chunk_length - 1 &&
                y_chunk_pos != World.world_size - 1) {
            sides[2] = false;
            for (int x = x0; x <= x1; x++) {
                if (Cube.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos + 1][z_chunk_pos].cubes[x][0][z]) == 0) {
                    sides[2] = true;
                    break;
                }
            }
        }

        if (sides[3] &&
                y0 == 0 &&
                y_chunk_pos != 0) {
            sides[3] = false;
            for (int x = x0; x <= x1; x++) {
                if (Cube.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos - 1][z_chunk_pos].cubes[x][World.chunk_length - 1][z]) == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }

        if (sides[4] &&
                z == 0 &&
                z_chunk_pos != 0) {
            sides[4] = false;
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Cube.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos][z_chunk_pos - 1].cubes[x][y][World.chunk_height - 1]) == 0) {
                        sides[4] = true;
                        break;
                    }
                }
            }
        }

        if (sides[5] &&
                z == World.chunk_height - 1 &&
                z_chunk_pos != World.world_height - 1) {
            sides[5] = false;
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Cube.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos][z_chunk_pos + 1].cubes[x][y][0]) == 0) {
                        sides[5] = true;
                        break;
                    }
                }
            }

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

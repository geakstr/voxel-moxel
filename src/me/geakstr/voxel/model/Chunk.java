package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.util.ArraysUtil;
import me.geakstr.voxel.workers.ChunkWorker;

import java.util.*;

public class Chunk extends MeshIndexed {
    public int[][][] blocks; // [x][y][z]

    public boolean changed, updating, updated, empty, waiting, visible;

    public int x_chunk_pos, y_chunk_pos, z_chunk_pos;
    public int x_offset, y_offset, z_offset;

    public Chunk(int x_chunk_pos, int y_chunk_pos, int z_chunk_pos) {
        super();

        this.x_chunk_pos = x_chunk_pos;
        this.y_chunk_pos = y_chunk_pos;
        this.z_chunk_pos = z_chunk_pos;

        this.x_offset = x_chunk_pos * World.chunk_size;
        this.y_offset = y_chunk_pos * World.chunk_height;
        this.z_offset = z_chunk_pos * World.chunk_size;

        this.blocks = new int[World.chunk_size][World.chunk_size][World.chunk_height];

        this.changed = true;
        this.waiting = false;
        this.visible = true;

        this.updating = false;
        this.updated = true;

        this.empty = true;
    }

    public void rebuild() {
        this.updating = true;

        List<Float> verts = new ArrayList<>();
        List<Float> tex = new ArrayList<>();
        List<Float> tex_off = new ArrayList<>();
        List<Float> colors = new ArrayList<>();

        Queue<Vector2f> texs = new LinkedList<>();
        texs.add(TextureAtlas.get_coord("grass"));
        texs.add(TextureAtlas.get_coord("moss_stone"));
        texs.add(TextureAtlas.get_coord("stone"));
        texs.add(TextureAtlas.get_coord("wood_0"));
        texs.add(TextureAtlas.get_coord("sand"));

        int next_color = 512;
        for (int z = 0; z < World.chunk_height; z++) {
            int[][] mark = new int[World.chunk_size][World.chunk_size];
            int[] proj = new int[mark[0].length];
            Arrays.fill(proj, -1);
            int len = 0;
            Map<Integer, int[]> coords_map = new HashMap<>();
            for (int y = 0; y < World.chunk_size; y++) {
                boolean canDown = false;
                int projFlag = -1;
                for (int x = 0; x < World.chunk_size; x++) {
                    if (Block.unpack_type(blocks[x][y][z]) == 0) {
                        continue;
                    }

                    int type = 1;
                    boolean update_coords = false;

                    if (proj[x] != -1) {
                        mark[y][x] = mark[y - 1][x];
                        projFlag = x;
                        if (x > 0 && mark[y][x - 1] == mark[y][x]) {
                            len++;
                        } else {
                            len = 0;
                        }
                        update_coords = true;
                    } else if (((x > 0) && (Block.unpack_type(blocks[x - 1][y][z]) == type) && (projFlag != x - 1))) {
                        mark[y][x] = mark[y][x - 1];
                        update_coords = true;
                        len++;
                    } else {
                        mark[y][x] = ++next_color;
                        coords_map.put(next_color, new int[]{x, y, x, y});
                        len = 0;
                    }

                    if (update_coords) {
                        int face = proj[x] != -1 ? proj[x] : mark[y][x - 1];
                        int[] tmp = coords_map.get(face);
                        tmp[2] = x;
                        tmp[3] = y;
                        coords_map.put(face, tmp);
                    }

                    canDown = !(len > 0 && !canDown) && (y < (World.chunk_size - 1) && (Block.unpack_type(blocks[x][y + 1][z]) == type));

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

            Vector2f texture = texs.poll();
            texs.add(texture);

            for (Map.Entry<Integer, int[]> e : coords_map.entrySet()) {
                int[] coords = e.getValue();
                int x0 = coords[0], y0 = coords[1];
                int x1 = coords[2], y1 = coords[3];
                int xx = x1 - x0, yy = y1 - y0;

                boolean[] renderable_sides = renderable_sides(x0, y0, x1, y1, z);

                for (int side_idx = 0; side_idx < 6; side_idx++) {
                    if (renderable_sides[side_idx]) {
                        verts.addAll(ArraysUtil.copy_floats(
                                AABB.SIDE.values[side_idx].translate_and_expand(x0 + x_offset, z + y_offset, y0 + z_offset, xx, 0, yy)));

                        tex.addAll(ArraysUtil.copy_floats(
                                AABB.SIDE.values[side_idx].tex_coords(xx + 1, yy + 1)));

                        tex_off.addAll(Arrays.asList(
                                texture.x, texture.y,
                                texture.x, texture.y,
                                texture.x, texture.y,
                                texture.x, texture.y));

                        float r = 1.0f, g = 1.0f, b = 1.0f;
                        if (side_idx >= 0 && side_idx <= 3) {
                            r = 0.7f;
                            g = 0.7f;
                            b = 0.7f;
                        }
                        colors.addAll(Arrays.asList(r, g, b, r, g, b, r, g, b, r, g, b));
                    }
                }
            }
        }

        update_gl_buffers(verts, tex, tex_off, colors);

        this.updated = true;
        this.empty = verts.size() == 0;
    }

    public boolean[] renderable_sides(int x0, int y0, int x1, int y1, int z) {
        boolean[] sides = new boolean[6];

        if (y0 == 0) {
            sides[1] = true;
        } else if (y0 > 0) {
            for (int x = x0; x <= x1; x++) {
                if (Block.unpack_type(blocks[x][y0 - 1][z]) == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }
        if (y1 == World.chunk_size - 1) {
            sides[0] = true;
        } else if (y1 < World.chunk_size - 1) {
            for (int x = x0; x <= x1; x++) {
                if (Block.unpack_type(blocks[x][y1 + 1][z]) == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }

        if (x0 == 0) {
            sides[2] = true;
        } else if (x0 > 0) {
            for (int y = y0; y <= y1; y++) {
                if (Block.unpack_type(blocks[x0 - 1][y][z]) == 0) {
                    sides[2] = true;
                    break;
                }
            }
        }
        if (x1 == World.chunk_size - 1) {
            sides[3] = true;
        } else if (x1 < World.chunk_size - 1) {
            for (int y = y0; y <= y1; y++) {
                if (Block.unpack_type(blocks[x1 + 1][y][z]) == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }


        if (z == World.chunk_height - 1) {
            sides[4] = true;
        } else if (z < World.chunk_height - 1) {
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Block.unpack_type(blocks[x][y][z + 1]) == 0) {
                        sides[4] = true;
                        break;
                    }
                }
            }
        }
        if (z == 0) {
            sides[5] = true;
        } else if (z > 0) {
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Block.unpack_type(blocks[x][y][z - 1]) == 0) {
                        sides[5] = true;
                        break;
                    }
                }
            }
        }


        if (sides[0] &&
                y1 == World.chunk_size - 1 &&
                z_chunk_pos != World.world_size - 1) {
            sides[0] = false;
            for (int x = x0; x <= x1; x++) {
                if (Block.unpack_type(World.chunks[y_chunk_pos][x_chunk_pos][z_chunk_pos + 1].blocks[x][0][z]) == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }
        if (sides[1] &&
                y0 == 0 &&
                z_chunk_pos != 0) {
            sides[1] = false;
            for (int x = x0; x <= x1; x++) {
                if (Block.unpack_type(World.chunks[y_chunk_pos][x_chunk_pos][z_chunk_pos - 1].blocks[x][World.chunk_size - 1][z]) == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }

        if (sides[2] &&
                x0 == 0 &&
                x_chunk_pos != 0) {
            sides[2] = false;
            for (int y = y0; y <= y1; y++) {
                if (Block.unpack_type(World.chunks[y_chunk_pos][x_chunk_pos - 1][z_chunk_pos].blocks[World.chunk_size - 1][y][z]) == 0) {
                    sides[2] = true;
                    break;
                }
            }
        }
        if (sides[3] &&
                x1 == World.chunk_size - 1 &&
                x_chunk_pos != World.world_size - 1) {
            sides[3] = false;
            for (int y = y0; y <= y1; y++) {
                if (Block.unpack_type(World.chunks[y_chunk_pos][x_chunk_pos + 1][z_chunk_pos].blocks[0][y][z]) == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }

        if (sides[4] &&
                z == World.chunk_height - 1 &&
                y_chunk_pos != World.world_height - 1) {
            sides[4] = false;
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Block.unpack_type(World.chunks[y_chunk_pos + 1][x_chunk_pos][z_chunk_pos].blocks[x][y][0]) == 0) {
                        sides[4] = true;
                        break;
                    }
                }
            }
        }
        if (sides[5] &&
                z == 0 &&
                y_chunk_pos != 0) {
            sides[5] = false;
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    if (Block.unpack_type(World.chunks[y_chunk_pos - 1][x_chunk_pos][z_chunk_pos].blocks[x][y][World.chunk_height - 1]) == 0) {
                        sides[5] = true;
                        break;
                    }
                }
            }
        }


        return sides;
    }

    public void update() {
        if (changed && !updating && updated) {
            updated = false;
            Game.chunks_workers_executor_service.add_worker(new ChunkWorker(this));
        }

        if (updated && updating) {
            updating = false;
            update_gl_buffers();
        }
        changed = false;
    }

    public void render() {
        if (changed || updating) {
            update();
        }
        if (!empty) {
            draw();
            World.chunks_in_frame++;
            World.faces_in_frame += count;
        }
    }

}

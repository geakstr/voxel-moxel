package me.geakstr.voxel.model;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.model.meshes.OccludedTexturedMesh;
import me.geakstr.voxel.workers.ChunkWorker;

import java.util.*;

import static org.lwjgl.opengl.ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.glBeginQuery;
import static org.lwjgl.opengl.GL15.glEndQuery;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Chunk extends OccludedTexturedMesh {
    public Block[][][] blocks; // [x][y][z]

    public boolean changed, updating, updated, empty, waiting, visible;
    public Integer[] box;

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

        this.blocks = new Block[World.chunk_width][World.chunk_length][World.chunk_height];
        this.box = Box.get_box(x_chunk_pos, y_chunk_pos, z_chunk_pos, World.chunk_width, World.chunk_length, World.chunk_height);

        this.changed = true;
        this.waiting = false;
        this.visible = true;

        this.updating = false;
        this.updated = true;

        this.empty = true;
    }

    public void rebuild() {
        this.updating = true;

        Random rnd = new Random();

        List<Integer> vertices = new ArrayList<>();
        List<Integer> textures = new ArrayList<>();
        List<Float> textures_offsets = new ArrayList<>();
        List<Float> colors = new ArrayList<>();

        int next_color = 512;
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
                    if (blocks[x][y][z].type == 0) {
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
                    } else if (((x > 0) && (blocks[x - 1][y][z].type == type) && (projFlag != x - 1))) {
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

                    canDown = !(len > 0 && !canDown) && (y < (World.chunk_length - 1) && (blocks[x][y + 1][z].type == type));

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

                Vector2f tex = rnd.nextBoolean() ? TextureAtlas.get_coord("grass") : TextureAtlas.get_coord("dirt");
                for (int side_idx = 0; side_idx < 6; side_idx++) {
                    if (renderable_sides[side_idx]) {
                        vertices.addAll(Arrays.asList(Block.get_side(
                                side_idx,
                                x0 + x_offset, y0 + y_offset,
                                x1 + x_offset, y1 + y_offset,
                                z + z_offset)));

                        textures.addAll(Arrays.asList(Block.get_texture(
                                side_idx, x0, y0, x1, y1)));
                        textures_offsets.addAll(Arrays.asList(
                                tex.x, tex.y,
                                tex.x, tex.y,
                                tex.x, tex.y,
                                tex.x, tex.y,
                                tex.x, tex.y,
                                tex.x, tex.y));

                        float r = 1.0f, g = 1.0f, b = 1.0f;
                        if (side_idx >= 0 && side_idx <= 3) {
                            r = 0.7f;
                            g = 0.7f;
                            b = 0.7f;
                        }
                        colors.addAll(Arrays.asList(r, g, b, r, g, b, r, g, b, r, g, b, r, g, b, r, g, b));
                    }
                }
            }
        }

        this.verts = vertices.toArray(new Integer[vertices.size()]);
        this.tex = textures.toArray(new Integer[textures.size()]);
        this.tex_off = textures_offsets.toArray(new Float[textures_offsets.size()]);
        this.colors = colors.toArray(new Float[colors.size()]);

        this.size = this.verts.length;

        this.updated = true;

        this.empty = this.size == 0;
    }

    public boolean[] renderable_sides(int x0, int y0, int x1, int y1, int z) {
        boolean[] sides = new boolean[6];

        if (x0 == 0) {
            sides[0] = true;
        } else if (x0 > 0) {
            for (int y = y0; y <= y1; y++) {
                if (blocks[x0 - 1][y][z].type == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }
        if (x1 == World.chunk_width - 1) {
            sides[1] = true;
        } else if (x1 < World.chunk_width - 1) {
            for (int y = y0; y <= y1; y++) {
                if (blocks[x1 + 1][y][z].type == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }

        if (y0 == 0) {
            sides[3] = true;
        } else if (y0 > 0) {
            for (int x = x0; x <= x1; x++) {
                if (blocks[x][y0 - 1][z].type == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }

        if (y1 == World.chunk_length - 1) {
            sides[2] = true;
        } else if (y1 < World.chunk_length - 1) {
            for (int x = x0; x <= x1; x++) {
                if (blocks[x][y1 + 1][z].type == 0) {
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
                    if (blocks[x][y][z - 1].type == 0) {
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
                    if (blocks[x][y][z + 1].type == 0) {
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
                if (World.chunks[z_chunk_pos][x_chunk_pos - 1][y_chunk_pos].blocks[World.chunk_width - 1][y][z].type == 0) {
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
                if (World.chunks[z_chunk_pos][x_chunk_pos + 1][y_chunk_pos].blocks[0][y][z].type == 0) {
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
                if (World.chunks[z_chunk_pos][x_chunk_pos][y_chunk_pos + 1].blocks[x][0][z].type == 0) {
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
                if (World.chunks[z_chunk_pos][x_chunk_pos][y_chunk_pos - 1].blocks[x][World.chunk_length - 1][z].type == 0) {
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
                    if (World.chunks[z_chunk_pos - 1][x_chunk_pos][y_chunk_pos].blocks[x][y][World.chunk_height - 1].type == 0) {
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
                    if (World.chunks[z_chunk_pos + 1][x_chunk_pos][y_chunk_pos].blocks[x][y][0].type == 0) {
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

        if (updated && updating && !empty) {
            updating = false;
            prepare(verts, colors, tex, tex_off, box);
        }

        changed = false;
    }

    public void occlusion_render() {
        if (changed || updating) {
            update();
        }
        if (!empty && !waiting) {
            this.waiting = true;
            glBeginQuery(GL_SAMPLES_PASSED_ARB, o_query);
            glBindVertexArray(o_vao);
            glDrawArrays(GL_TRIANGLES, 0, Box.box_vertices_size);
            glBindVertexArray(0);
            glEndQuery(GL_SAMPLES_PASSED_ARB);
        }
    }

    public void terrain_render() {
        if (changed || updating) {
            update();
        }
        if (!empty) {
            bind_vao();
            glDrawArrays(GL_TRIANGLES, 0, size);
            unbind_vao();
            World.faces_in_frame += size / 3;
        }
    }

}

package me.geakstr.voxel.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import me.geakstr.voxel.game.Game;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.model.meshes.IndexedMesh;
import me.geakstr.voxel.workers.ChunkWorker;

public class Chunk extends IndexedMesh {
    public static int size, volume, square;

    private int[] blocks;
    private byte[] light_map;
    private Queue<LightNode> light_bfs_queue;

    public boolean changed, updating, updated, empty, waiting, visible;

    public int x_chunk_pos, y_chunk_pos, z_chunk_pos;
    public int x_offset, y_offset, z_offset;

    public Chunk(int x_chunk_pos, int y_chunk_pos, int z_chunk_pos) {
        super();

        this.x_chunk_pos = x_chunk_pos;
        this.y_chunk_pos = y_chunk_pos;
        this.z_chunk_pos = z_chunk_pos;

        this.x_offset = x_chunk_pos * size;
        this.y_offset = y_chunk_pos * size;
        this.z_offset = z_chunk_pos * size;

        this.blocks = new int[volume];
        this.light_map = new byte[volume];
        this.light_bfs_queue = new LinkedList<>();

        this.changed = true;
        this.waiting = false;
        this.visible = true;

        this.updating = false;
        this.updated = true;

        this.empty = true;
    }

    public int block(int x, int y, int z) {
        return blocks[World.idx(x, y, z, square, size)];
    }

    public void block(int val, int x, int y, int z) {
        blocks[World.idx(x, y, z, square, size)] = val;
    }

    public void block_type(int new_block_type, int x, int y, int z) {
        block(Block.pack_type(block(x, y, z), new_block_type), x, y, z);
    }

    public int block_type(int x, int y, int z) {
        return Block.unpack_type(block(x, y, z));
    }

    public int sunlight(int x, int y, int z) {
        return (light_map[World.idx(x, y, z, square, size)] >> 4) & 0xF;
    }

    public void sunlight(int val, int x, int y, int z) {
        int idx = World.idx(x, y, z, square, size);
        light_map[idx] = (byte) ((light_map[idx] & 0xF) | (val << 4));
    }

    public int torchlight(int x, int y, int z) {
        return light_map[World.idx(x, y, z, square, size)] & 0xF;
    }

    public void torchlight(int val, int x, int y, int z) {
        int idx = World.idx(x, y, z, square, size);
        light_map[idx] = (byte) ((light_map[idx] & 0xF) | val);
    }

    private class LightNode {
        public int idx;
        public Chunk chunk;

        public LightNode(int idx, Chunk chunk) {
            this.idx = idx;
            this.chunk = chunk;
        }
    }

    public void lighting() {
        while (!light_bfs_queue.isEmpty()) {
            LightNode node = light_bfs_queue.poll();

            int xx = node.idx % size;
            int yy = (node.idx % (size * size)) / size;
            int zz = node.idx / (size * size);

            int light_level = node.chunk.torchlight(xx, yy, zz);

            if (xx - 1 >= 0) {
                if (node.chunk.block_type(xx - 1, yy, zz) != 0 && node.chunk.torchlight(xx - 1, yy, zz) + 2 <= light_level) {
                    node.chunk.torchlight(light_level - 1, xx - 1, yy, zz);
                    light_bfs_queue.add(new LightNode(World.idx(xx - 1, yy, zz, square, size), node.chunk));
                }
            }
            if (xx + 1 < size) {
                if (node.chunk.block_type(xx + 1, yy, zz) != 0 && node.chunk.torchlight(xx + 1, yy, zz) + 2 <= light_level) {
                    node.chunk.torchlight(light_level - 1, xx + 1, yy, zz);
                    light_bfs_queue.add(new LightNode(World.idx(xx + 1, yy, zz, square, size), node.chunk));
                }
            }

            if (yy - 1 >= 0) {
                if (node.chunk.block_type(xx, yy - 1, zz) != 0 && node.chunk.torchlight(xx, yy - 1, zz) + 2 <= light_level) {
                    node.chunk.torchlight(light_level - 1, xx, yy - 1, zz);
                    light_bfs_queue.add(new LightNode(World.idx(xx, yy - 1, zz, square, size), node.chunk));
                }
            }
            if (yy + 1 < size) {
                if (node.chunk.block_type(xx, yy + 1, zz) != 0 && node.chunk.torchlight(xx, yy + 1, zz) + 2 <= light_level) {
                    node.chunk.torchlight(light_level - 1, xx, yy + 1, zz);
                    light_bfs_queue.add(new LightNode(World.idx(xx, yy + 1, zz, square, size), node.chunk));
                }
            }

            if (zz - 1 >= 0) {
                if (node.chunk.block_type(xx, yy, zz - 1) != 0 && node.chunk.torchlight(xx, yy, zz - 1) + 2 <= light_level) {
                    node.chunk.torchlight(light_level - 1, xx, yy, zz - 1);
                    light_bfs_queue.add(new LightNode(World.idx(xx, yy, zz - 1, square, size), node.chunk));
                }
            }
            if (zz + 1 < size) {
                if (node.chunk.block_type(xx, yy, zz + 1) != 0 && node.chunk.torchlight(xx, yy, zz + 1) + 2 <= light_level) {
                    node.chunk.torchlight(light_level - 1, xx, yy, zz + 1);
                    light_bfs_queue.add(new LightNode(World.idx(xx, yy, zz + 1, square, size), node.chunk));
                }
            }
        }
    }

    public void place_torch(int x, int y, int z) {
        torchlight(15, x, y, z);
        light_bfs_queue.add(new LightNode(World.idx(x, y, z, square, size), this));
    }

    public void rebuild() {
        this.updating = true;

        lighting();

        Queue<Vector2f> texs = new LinkedList<>();
        texs.add(TextureAtlas.get_coord("grass"));
        texs.add(TextureAtlas.get_coord("moss_stone"));
        texs.add(TextureAtlas.get_coord("stone"));
        texs.add(TextureAtlas.get_coord("wood_0"));
        texs.add(TextureAtlas.get_coord("sand"));
        
        Map<Vertex, Integer> vertex2index = new LinkedHashMap<>();
        List<Integer> indices = new ArrayList<>();
        int cur_vertex_idx = 0;

        int next_color = 512;
        int[] proj = new int[size];
        Map<Integer, int[]> coords_map = new HashMap<>();
        
        this.faces_counter = 0;
        for (int y = 0; y < size; y++) {
            int[][] mark = new int[size][size];
            Arrays.fill(proj, -1);
            int len = 0;
            coords_map.clear();
            for (int z = 0; z < size; z++) {
                boolean canDown = false;
                int projFlag = -1;
                for (int x = 0; x < size; x++) {
                	int type = block_type(x, y, z);
                    if (type == 0) {
                        continue;
                    }
                    
                    int light = torchlight(x, y, z);
                    boolean update_coords = false;

                    if (proj[x] != -1) {
                        mark[z][x] = mark[z - 1][x];
                        projFlag = x;
                        if (x > 0 && mark[z][x - 1] == mark[z][x]) {
                            len++;
                        } else {
                            len = 0;
                        }
                        update_coords = true;
                    } else if (x > 0 && block_type(x - 1, y, z) == type && torchlight(x - 1, y, z) == light && projFlag != x - 1) {
                        mark[z][x] = mark[z][x - 1];
                        update_coords = true;
                        len++;
                    } else {
                        mark[z][x] = ++next_color;
                        coords_map.put(next_color, new int[]{x, z, x, z});
                        len = 0;
                    }

                    if (update_coords) {
                        int face = proj[x] != -1 ? proj[x] : mark[z][x - 1];
                        int[] tmp = coords_map.get(face);
                        tmp[2] = x;
                        tmp[3] = z;
                        coords_map.put(face, tmp);
                    }

                    canDown = !(len > 0 && !canDown) && z < size - 1 && block_type(x, y, z + 1) == type && torchlight(x, y, z + 1) == light;

                    if (canDown) {
                        proj[x] = mark[z][x];
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

            for (int[] coords : coords_map.values()) {
                int x0 = coords[0], z0 = coords[1];
                int x1 = coords[2], z1 = coords[3];
                int xx = x1 - x0 + 1, zz = z1 - z0 + 1;

                boolean[] renderable_sides = renderable_sides(x0, z0, x1, z1, y);

                for (int side_idx = 0; side_idx < 6; side_idx++) {
                    if (renderable_sides[side_idx]) {
                    	float[] vertices   = AABB.SIDE.values[side_idx].translate_and_expand(x0 + x_offset, y + y_offset, z0 + z_offset, xx, 1, zz);
                    	float[] tex_coords = AABB.SIDE.values[side_idx].texture_coords(xx, zz);

                        float r = 1.0f, g = 1.0f, b = 1.0f;
                        if (side_idx >= 0 && side_idx <= 3) {
                            r = 0.7f;
                            g = 0.7f;
                            b = 0.7f;
                        }

//                        for (int xxx = x0; xxx <= x1; xxx++) {
//                            for (int zzz = z0; zzz <= z1; zzz++) {
//                                float light = light_map[World.idx(xxx, y, zzz, square, size)] / 15.0f;
//
//                                r *= light;
//                                g *= light;
//                                b *= light;
//                            }
//                        }
//                        if (r < 0.02f && g < 0.02f && b < 0.02f) {
//                        	r = 0.02f;
//                        	g = 0.02f;
//                        	b = 0.02f;
//                        }
                        Integer index; 
                        
                        // 0
                        Vertex vertex0 = new Vertex(vertices[0], vertices[1], vertices[2],
 							   tex_coords[0], tex_coords[1],
 							   texture.x, texture.y,
 							   r, g, b);
					 	index = vertex2index.get(vertex0);
					 	if (index == null) {
					 		index = cur_vertex_idx++;
					 		vertex2index.put(vertex0, index);
					 	}
					 	indices.add(index);
					 	
					 	// 1
					 	Vertex vertex1 = new Vertex(vertices[3], vertices[4], vertices[5],
	 							   tex_coords[2], tex_coords[3],
	 							   texture.x, texture.y,
	 							   r, g, b);
					 	index = vertex2index.get(vertex1);
					 	if (index == null) {
					 		index = cur_vertex_idx++;
					 		vertex2index.put(vertex1, index);
					 	}
					 	indices.add(index);
					 	
					 	// 2
					 	Vertex vertex2 = new Vertex(vertices[6], vertices[7], vertices[8],
	 							   tex_coords[4], tex_coords[5],
	 							   texture.x, texture.y,
	 							   r, g, b);
					 	index = vertex2index.get(vertex2);
					 	if (index == null) {
					 		index = cur_vertex_idx++;
					 		vertex2index.put(vertex2, index);
					 	}
					 	indices.add(index);
					 	
					 	// 1
					 	indices.add(vertex2index.get(vertex1));
					 	
					 	// 3
					 	Vertex vertex3 = new Vertex(vertices[9], vertices[10], vertices[11],
	 							   tex_coords[6], tex_coords[7],
	 							   texture.x, texture.y,
	 							   r, g, b);
					 	index = vertex2index.get(vertex3);
					 	if (index == null) {
					 		index = cur_vertex_idx++;
					 		vertex2index.put(vertex3, index);
					 	}
					 	indices.add(index);
					 	 
					 	// 2
					 	indices.add(vertex2index.get(vertex2));
					 	
					 	this.faces_counter += 2;
                    }
                }
            }
        }

        this.updated = true;
        this.empty = faces_counter == 0;
        
        synchronized (this) {
           if (!empty) {
            	update_gl_data(vertex2index.keySet(), indices);
           }
        }
    }

    public boolean[] renderable_sides(int x0, int z0, int x1, int z1, int y) {
        boolean[] sides = new boolean[6];

        if (z1 == size - 1) {
            sides[0] = true;
        } else if (z1 < size - 1) {
            for (int x = x0; x <= x1; x++) {
                if (block_type(x, y, z1 + 1) == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }
        if (z0 == 0) {
            sides[1] = true;
        } else if (z0 > 0) {
            for (int x = x0; x <= x1; x++) {
                if (block_type(x, y, z0 - 1) == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }

        if (x0 == 0) {
            sides[2] = true;
        } else if (x0 > 0) {
            for (int z = z0; z <= z1; z++) {
                if (block_type(x0 - 1, y, z) == 0) {
                    sides[2] = true;
                    break;
                }
            }
        }
        if (x1 == size - 1) {
            sides[3] = true;
        } else if (x1 < size - 1) {
            for (int z = z0; z <= z1; z++) {
                if (block_type(x1 + 1, y, z) == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }

        if (y == size - 1) {
            sides[4] = true;
        } else if (y < size - 1) {
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    if (block_type(x, y + 1, z) == 0) {
                        sides[4] = true;
                        break;
                    }
                }
            }
        }
        if (y == 0) {
            sides[5] = true;
        } else if (y > 0) {
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    if (block_type(x, y - 1, z) == 0) {
                        sides[5] = true;
                        break;
                    }
                }
            }
        }


        if (sides[0] &&
                z1 == size - 1 &&
                z_chunk_pos != World.size - 1) {
            sides[0] = false;
            for (int x = x0; x <= x1; x++) {
                if (World.chunk(x_chunk_pos, y_chunk_pos, z_chunk_pos + 1).block_type(x, y, 0) == 0) {
                    sides[0] = true;
                    break;
                }
            }
        }
        if (sides[1] &&
                z0 == 0 &&
                z_chunk_pos != 0) {
            sides[1] = false;
            for (int x = x0; x <= x1; x++) {
                if (World.chunk(x_chunk_pos, y_chunk_pos, z_chunk_pos - 1).block_type(x, y, size - 1) == 0) {
                    sides[1] = true;
                    break;
                }
            }
        }

        if (sides[2] &&
                x0 == 0 &&
                x_chunk_pos != 0) {
            sides[2] = false;
            for (int z = z0; z <= z1; z++) {
                if (World.chunk(x_chunk_pos - 1, y_chunk_pos, z_chunk_pos).block_type(size - 1, y, z) == 0) {
                    sides[2] = true;
                    break;
                }
            }
        }
        if (sides[3] &&
                x1 == size - 1 &&
                x_chunk_pos != World.size - 1) {
            sides[3] = false;
            for (int z = z0; z <= z1; z++) {
                if (World.chunk(x_chunk_pos + 1, y_chunk_pos, z_chunk_pos).block_type(0, y, z) == 0) {
                    sides[3] = true;
                    break;
                }
            }
        }

        if (sides[4] &&
                y == size - 1 &&
                y_chunk_pos != World.height - 1) {
            sides[4] = false;
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    if (World.chunk(x_chunk_pos, y_chunk_pos + 1, z_chunk_pos).block_type(x, 0, z) == 0) {
                        sides[4] = true;
                        break;
                    }
                }
            }
        }
        if (sides[5] &&
                y == 0 &&
                y_chunk_pos != 0) {
            sides[5] = false;
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    if (World.chunk(x_chunk_pos, y_chunk_pos - 1, z_chunk_pos).block_type(x, size - 1, z) == 0) {
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
            synchronized (this) {
	            if (!empty) {
	                update_gl_buffers();
	            }
            }
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
            World.faces_in_frame += faces_counter;
        }
    }
}

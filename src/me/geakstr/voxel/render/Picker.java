package me.geakstr.voxel.render;

import me.geakstr.voxel.helpers.Pair;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.Block;
import me.geakstr.voxel.model.Box;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.model.World;

import java.util.HashSet;
import java.util.Set;

public class Picker {
    public static final int picker_length = -(World.chunk_size * (World.near_chunks_radius + 1));

    public static Pair<Block, Block.TYPE> select(Ray ray, boolean pick_side) {
        final Vector3f camera_position = Camera.position.negate(null);

        Set<Chunk> selection_chunks = new HashSet<>();
        for (Chunk chunk : World.nearest_chunks) {
            int chunk_x = chunk.x_chunk_pos;
            int chunk_y = chunk.y_chunk_pos;
            int chunk_z = chunk.z_chunk_pos;
            Vector3f min = new Vector3f(chunk_x * World.chunk_size, chunk_y * World.chunk_height, chunk_z * World.chunk_size);
            Vector3f max = new Vector3f(min.x + World.chunk_size, min.y + World.chunk_height, min.z + World.chunk_size);
            if (ray.intersect(new Box(min, max), picker_length, -1)) {
                selection_chunks.add(World.chunks[chunk_y][chunk_x][chunk_z]);
            }
        }

        Pair<Block, Block.TYPE> selection = new Pair<>();
        if (selection_chunks.size() != 0) {
            float min_dist = Float.MAX_VALUE;
            for (Chunk selection_chunk : selection_chunks) {
                for (int block_x = 0; block_x < World.chunk_size; block_x++) {
                    for (int block_y = 0; block_y < World.chunk_size; block_y++) {
                        for (int block_z = 0; block_z < World.chunk_height; block_z++) {
                            Block block = new Block(new Vector3f(block_x, block_y, block_z), selection_chunk);
                            if (Block.unpack_type(selection_chunk.blocks[block_x][block_y][block_z]) != 0 && ray.intersect(block, picker_length, -1)) {
                                float dist = Vector3f.dist(block.corners[0], camera_position);
                                if (dist < min_dist) {
                                    selection.first = block;
                                    min_dist = dist;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("--------");

        if (pick_side) {
            if (null != selection.first) {
                Vector3f min_c = selection.first.corners[0];

                Box box;
                Vector3f min, max;

                min = new Vector3f(min_c.x, min_c.y + 1, min_c.z);
                max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z + 1);
                if (-Camera.position.y >= max.y - 0.5) {
                    box = new Box(min, max);
                    if (ray.intersect(box, picker_length, -1)) {
                        selection.second = Block.TYPE.TOP;
                    }
                } else {
                    min = new Vector3f(min_c.x, min_c.y, min_c.z);
                    max = new Vector3f(min_c.x + 1, min_c.y, min_c.z + 1);
                    box = new Box(min, max);
                    if (ray.intersect(box, picker_length, -1)) {
                        selection.second = Block.TYPE.BOTTOM;
                    }
                }
                if (null == selection.second) {
                    min = new Vector3f(min_c.x, min_c.y, min_c.z);
                    max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z);
                    if (-Camera.position.z < max.z) {
                        box = new Box(min, max);
                        if (ray.intersect(box, picker_length, -1)) {
                            selection.second = Block.TYPE.FRONT;
                        }
                    } else {
                        min = new Vector3f(min_c.x, min_c.y, min_c.z + 1);
                        max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z + 1);
                        if (-Camera.position.z > max.z) {
                            box = new Box(min, max);
                            if (ray.intersect(box, picker_length, -1)) {
                                selection.second = Block.TYPE.BACK;
                            }
                        }
                    }
                    if (null == selection.second) {
                        min = new Vector3f(min_c.x, min_c.y, min_c.z);
                        max = new Vector3f(min_c.x, min_c.y + 1, min_c.z + 1);
                        if (-Camera.position.x < max.x) {
                            box = new Box(min, max);
                            if (ray.intersect(box, picker_length, -1)) {
                                selection.second = Block.TYPE.RIGHT;
                            }
                        } else {
                            min = new Vector3f(min_c.x + 1, min_c.y, min_c.z);
                            max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z + 1);
                            box = new Box(min, max);
                            if (ray.intersect(box, picker_length, -1)) {
                                selection.second = Block.TYPE.LEFT;
                            }
                        }
                    }
                }
            }
        }

        return selection;
    }

    public static boolean remove(Ray ray) {
        Block selected_block = select(ray, false).first;

        if (selected_block != null) {
            Chunk chunk = selected_block.chunk;
            if (chunk != null && !chunk.updating) {
                Vector3f pos = selected_block.corners[0];

                int x = (int) pos.x % World.chunk_size;
                int y = (int) pos.z % World.chunk_size;
                int z = (int) pos.y % World.chunk_height;
                chunk.blocks[x][y][z] = 0;

                chunk.changed = true;

                if (x == 0 && chunk.x_chunk_pos != 0) {
                    World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos - 1][chunk.z_chunk_pos].changed = true;
                }
                if (x == World.chunk_size - 1 && chunk.x_chunk_pos != World.world_size - 1) {
                    World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos + 1][chunk.z_chunk_pos].changed = true;
                }
                if (z == 0 && chunk.y_chunk_pos != 0) {
                    World.chunks[chunk.y_chunk_pos - 1][chunk.x_chunk_pos][chunk.z_chunk_pos].changed = true;
                }
                if (z == World.chunk_height - 1 && chunk.y_chunk_pos != World.world_height - 1) {
                    World.chunks[chunk.y_chunk_pos + 1][chunk.x_chunk_pos][chunk.z_chunk_pos].changed = true;
                }
                if (y == 0 && chunk.z_chunk_pos != 0) {
                    World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos][chunk.z_chunk_pos - 1].changed = true;
                }
                if (y == World.chunk_size - 1 && chunk.z_chunk_pos != World.world_size - 1) {
                    World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos][chunk.z_chunk_pos + 1].changed = true;
                }

                return true;
            }
        }

        return false;
    }

    public static boolean insert(Ray ray) {
        Pair<Block, Block.TYPE> selection = select(ray, true);

        if (null != selection.first && null != selection.second) {
            Chunk chunk = selection.first.chunk;
            if (chunk != null && !chunk.updating) {
                Vector3f pos = selection.first.corners[0];

                int x = (int) pos.x % World.chunk_size;
                int y = (int) pos.z % World.chunk_size;
                int z = (int) pos.y % World.chunk_height;

                chunk.changed = true;
                switch (selection.second) {
                    case FRONT:
                        if (y - 1 < 0) {
                            if (chunk.z_chunk_pos == 0) {
                                return false;
                            }
                            chunk = World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos][chunk.z_chunk_pos - 1];
                            chunk.blocks[x][World.chunk_size - 1][z] = Block.pack_type(0, 1);
                        } else {
                            chunk.blocks[x][y - 1][z] = Block.pack_type(0, 1);
                        }
                        break;
                    case BACK:
                        if (y + 1 >= World.chunk_size) {
                            if (chunk.z_chunk_pos == World.world_size - 1) {
                                return false;
                            }
                            chunk = World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos][chunk.z_chunk_pos + 1];
                            chunk.blocks[x][0][z] = Block.pack_type(0, 1);
                        } else {
                            chunk.blocks[x][y + 1][z] = Block.pack_type(0, 1);
                        }
                        break;
                    case RIGHT:
                        if (x - 1 < 0) {
                            if (chunk.x_chunk_pos == 0) {
                                return false;
                            }
                            chunk = World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos - 1][chunk.z_chunk_pos];
                            chunk.blocks[World.chunk_size - 1][y][z] = Block.pack_type(0, 1);
                        } else {
                            chunk.blocks[x - 1][y][z] = Block.pack_type(0, 1);
                        }
                        break;
                    case LEFT:
                        if (x + 1 >= World.chunk_size) {
                            if (chunk.x_chunk_pos == World.world_size - 1) {
                                return false;
                            }
                            chunk = World.chunks[chunk.y_chunk_pos][chunk.x_chunk_pos + 1][chunk.z_chunk_pos];
                            chunk.blocks[0][y][z] = Block.pack_type(0, 1);
                        } else {
                            chunk.blocks[x + 1][y][z] = Block.pack_type(0, 1);
                        }
                        break;
                    case BOTTOM:
                        if (z - 1 < 0) {
                            if (chunk.y_chunk_pos == 0) {
                                return false;
                            }
                            chunk = World.chunks[chunk.y_chunk_pos - 1][chunk.x_chunk_pos][chunk.z_chunk_pos];
                            chunk.blocks[x][y][World.chunk_height - 1] = Block.pack_type(0, 1);
                        } else {
                            chunk.blocks[x][y][z - 1] = Block.pack_type(0, 1);
                        }
                        break;
                    case TOP:
                        if (z + 1 >= World.chunk_height) {
                            if (chunk.y_chunk_pos == World.world_height - 1) {
                                return false;
                            }
                            chunk = World.chunks[chunk.y_chunk_pos + 1][chunk.x_chunk_pos][chunk.z_chunk_pos];
                            chunk.blocks[x][y][0] = Block.pack_type(0, 1);
                        } else {
                            chunk.blocks[x][y][z + 1] = Block.pack_type(0, 1);
                        }
                        break;
                }
                chunk.changed = true;
                return true;
            }
        }
        return false;
    }
}

package me.geakstr.voxel.render;

import me.geakstr.voxel.helpers.Pair;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.Block;
import me.geakstr.voxel.model.AABB;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.model.World;

import java.util.HashSet;
import java.util.Set;

public class Picker {
    public static final int picker_length = -(Chunk.size * (World.near_chunks_radius + 1));

    public static Pair<Block, Block.SIDE> select(Ray ray, boolean pick_side) {
        final Vector3f camera_position = Camera.position.negate(null);

        Set<Chunk> selection_chunks = new HashSet<>();
        for (Chunk chunk : World.nearest_chunks) {
            int chunk_x = chunk.x_chunk_pos;
            int chunk_y = chunk.y_chunk_pos;
            int chunk_z = chunk.z_chunk_pos;
            if (ray.intersect(new AABB(chunk_x * Chunk.size, chunk_y * Chunk.height, chunk_z * Chunk.size, Chunk.size, Chunk.height, Chunk.size), picker_length, -1)) {
                selection_chunks.add(World.chunk(chunk_x, chunk_y, chunk_z));
            }
        }

        Pair<Block, Block.SIDE> selection = new Pair<>();
        if (selection_chunks.size() != 0) {
            float min_dist = Float.MAX_VALUE;
            for (Chunk selection_chunk : selection_chunks) {
                for (int block_x = 0; block_x < Chunk.size; block_x++) {
                    for (int block_z = 0; block_z < Chunk.size; block_z++) {
                        for (int block_y = 0; block_y < Chunk.height; block_y++) {
                            Block block = new Block(new Vector3f(block_x, block_y, block_z), selection_chunk);
                            if (Block.unpack_type(selection_chunk.block(block_x, block_y, block_z)) != 0 && ray.intersect(block, picker_length, -1)) {
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

        if (pick_side) {
            if (null != selection.first) {
                Vector3f min_c = selection.first.corners[0];

                AABB box;
                if (-Camera.position.y >= min_c.y + 1 - 0.5) {
                    box = new AABB(min_c.x, min_c.y + 1, min_c.z, 1, 0, 1);
                    if (ray.intersect(box, picker_length, -1)) {
                        selection.second = Block.SIDE.TOP;
                    }
                } else {
                    box = new AABB(min_c.x, min_c.y, min_c.z, 1, 0, 1);
                    if (ray.intersect(box, picker_length, -1)) {
                        selection.second = Block.SIDE.BOTTOM;
                    }
                }
                if (null == selection.second) {
                    if (-Camera.position.z < min_c.z) {
                        box = new AABB(min_c.x, min_c.y, min_c.z, 1, 1, 0);
                        if (ray.intersect(box, picker_length, -1)) {
                            selection.second = Block.SIDE.FRONT;
                        }
                    } else {
                        if (-Camera.position.z > min_c.z + 1) {
                            box = new AABB(min_c.x, min_c.y, min_c.z + 1, 1, 1, 0);
                            if (ray.intersect(box, picker_length, -1)) {
                                selection.second = Block.SIDE.BACK;
                            }
                        }
                    }
                    if (null == selection.second) {
                        if (-Camera.position.x < min_c.x) {
                            box = new AABB(min_c.x, min_c.y, min_c.z, 0, 1, 1);
                            if (ray.intersect(box, picker_length, -1)) {
                                selection.second = Block.SIDE.RIGHT;
                            }
                        } else {
                            box = new AABB(min_c.x + 1, min_c.y, min_c.z, 0, 1, 1);
                            if (ray.intersect(box, picker_length, -1)) {
                                selection.second = Block.SIDE.LEFT;
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

                int x = (int) pos.x % Chunk.size;
                int y = (int) pos.y % Chunk.height;
                int z = (int) pos.z % Chunk.size;
                chunk.block(0, x, y, z);

                chunk.changed = true;

                if (x == 0 && chunk.x_chunk_pos != 0) {
                    World.chunk(chunk.x_chunk_pos - 1, chunk.y_chunk_pos, chunk.z_chunk_pos).changed = true;
                }
                if (x == Chunk.size - 1 && chunk.x_chunk_pos != World.size - 1) {
                    World.chunk(chunk.x_chunk_pos + 1, chunk.y_chunk_pos, chunk.z_chunk_pos).changed = true;
                }
                if (y == 0 && chunk.y_chunk_pos != 0) {
                    World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos - 1, chunk.z_chunk_pos).changed = true;
                }
                if (y == Chunk.height - 1 && chunk.y_chunk_pos != World.height - 1) {
                    World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos + 1, chunk.z_chunk_pos).changed = true;
                }
                if (z == 0 && chunk.z_chunk_pos != 0) {
                    World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos, chunk.z_chunk_pos - 1).changed = true;
                }
                if (z == Chunk.size - 1 && chunk.z_chunk_pos != World.size - 1) {
                    World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos, chunk.z_chunk_pos + 1).changed = true;
                }

                return true;
            }
        }

        return false;
    }

    public static boolean insert(Ray ray) {
        Pair<Block, Block.SIDE> selection = select(ray, true);

        if (null != selection.first && null != selection.second) {
            Chunk chunk = selection.first.chunk;
            if (chunk != null && !chunk.updating) {
                Vector3f pos = selection.first.corners[0];

                int x = (int) pos.x % Chunk.size;
                int y = (int) pos.y % Chunk.height;
                int z = (int) pos.z % Chunk.size;

                chunk.changed = true;
                switch (selection.second) {
                    case FRONT:
                        if (z - 1 < 0) {
                            if (chunk.z_chunk_pos == 0) {
                                return false;
                            }
                            chunk = World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos, chunk.z_chunk_pos - 1);
                            chunk.block(Block.pack_type(0, 1), x, y, Chunk.size - 1);
                        } else {
                            chunk.block(Block.pack_type(0, 1), x, y, z - 1);
                        }
                        break;
                    case BACK:
                        if (z + 1 >= Chunk.size) {
                            if (chunk.z_chunk_pos == World.size - 1) {
                                return false;
                            }
                            chunk = World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos, chunk.z_chunk_pos + 1);
                            chunk.block(Block.pack_type(0, 1), x, y, 0);
                        } else {
                            chunk.block(Block.pack_type(0, 1), x, y, z + 1);
                        }
                        break;
                    case RIGHT:
                        if (x - 1 < 0) {
                            if (chunk.x_chunk_pos == 0) {
                                return false;
                            }
                            chunk = World.chunk(chunk.x_chunk_pos - 1, chunk.y_chunk_pos, chunk.z_chunk_pos);
                            chunk.block(Block.pack_type(0, 1), Chunk.size - 1, y, z);
                        } else {
                            chunk.block(Block.pack_type(0, 1), x - 1, y, z);
                        }
                        break;
                    case LEFT:
                        if (x + 1 >= Chunk.size) {
                            if (chunk.x_chunk_pos == World.size - 1) {
                                return false;
                            }
                            chunk = World.chunk(chunk.x_chunk_pos + 1, chunk.y_chunk_pos, chunk.z_chunk_pos);
                            chunk.block(Block.pack_type(0, 1), 0, y, z);
                        } else {
                            chunk.block(Block.pack_type(0, 1), x + 1, y, z);
                        }
                        break;
                    case BOTTOM:
                        if (y - 1 < 0) {
                            if (chunk.y_chunk_pos == 0) {
                                return false;
                            }
                            chunk = World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos - 1, chunk.z_chunk_pos);
                            chunk.block(Block.pack_type(0, 1), x, Chunk.height - 1, z);
                        } else {
                            chunk.block(Block.pack_type(0, 1), x, y - 1, z);
                        }
                        break;
                    case TOP:
                        if (y + 1 >= Chunk.height) {
                            if (chunk.y_chunk_pos == World.height - 1) {
                                return false;
                            }
                            chunk = World.chunk(chunk.x_chunk_pos, chunk.y_chunk_pos + 1, chunk.z_chunk_pos);
                            chunk.block(Block.pack_type(0, 1), x, 0, z);
                        } else {
                            chunk.block(Block.pack_type(0, 1), x, y + 1, z);
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

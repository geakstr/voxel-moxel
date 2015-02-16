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
    public static Pair<Block, Block.TYPE> select(Ray ray) {
        final Vector3f camera_position = Camera.position.negate(null);

        Set<Chunk> selection_chunks = new HashSet<>();
        for (int chunk_z = 0; chunk_z < World.world_height; chunk_z++) {
            for (int chunk_x = 0; chunk_x < World.world_size; chunk_x++) {
                for (int chunk_y = 0; chunk_y < World.world_size; chunk_y++) {
                    Vector3f min = new Vector3f(World.chunk_width * chunk_x, World.chunk_height * chunk_z, World.chunk_length * chunk_y);
                    Vector3f max = new Vector3f(min.x + World.chunk_width, min.y + World.chunk_height, min.z + World.chunk_length);
                    if (ray.intersect(new Box(min, max), -100, -1)) {
                        selection_chunks.add(World.chunks[chunk_z][chunk_x][chunk_y]);
                    }
                }
            }
        }

        Pair<Block, Block.TYPE> selection = new Pair<>();
        if (selection_chunks.size() != 0) {
            float min_dist = Float.MAX_VALUE;
            for (Chunk selection_chunk : selection_chunks) {
                for (int block_x = 0; block_x < World.chunk_width; block_x++) {
                    for (int block_y = 0; block_y < World.chunk_length; block_y++) {
                        for (int block_z = 0; block_z < World.chunk_height; block_z++) {
                            Block block = new Block(new Vector3f(block_x, block_y, block_z), selection_chunk);
                            if (Block.unpack_type(selection_chunk.blocks[block_x][block_y][block_z]) != 0 && ray.intersect(block, -100, -1)) {
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

        if (null != selection.first) {
            Vector3f min_c = selection.first.corners[0];

            Box box;
            Vector3f min, max;

            min = new Vector3f(min_c.x, min_c.y + 1, min_c.z);
            max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z + 1);
            if (-Camera.position.y >= max.y - 0.5) {
                box = new Box(min, max);
                if (ray.intersect(box, -100, -1)) {
                    selection.second = Block.TYPE.TOP;
                }
            } else {
                min = new Vector3f(min_c.x, min_c.y, min_c.z);
                max = new Vector3f(min_c.x + 1, min_c.y, min_c.z + 1);
                box = new Box(min, max);
                if (ray.intersect(box, -100, -1)) {
                    selection.second = Block.TYPE.BOTTOM;
                }
            }
            if (null == selection.second) {
                min = new Vector3f(min_c.x, min_c.y, min_c.z);
                max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z);
                if (-Camera.position.z < max.z) {
                    box = new Box(min, max);
                    if (ray.intersect(box, -100, -1)) {
                        selection.second = Block.TYPE.FRONT;
                    }
                } else {
                    min = new Vector3f(min_c.x, min_c.y, min_c.z + 1);
                    max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z + 1);
                    if (-Camera.position.z > max.z) {
                        box = new Box(min, max);
                        if (ray.intersect(box, -100, -1)) {
                            selection.second = Block.TYPE.BACK;
                        }
                    }
                }
                if (null == selection.second) {
                    min = new Vector3f(min_c.x, min_c.y, min_c.z);
                    max = new Vector3f(min_c.x, min_c.y + 1, min_c.z + 1);
                    if (-Camera.position.x < max.x) {
                        box = new Box(min, max);
                        if (ray.intersect(box, -100, -1)) {
                            selection.second = Block.TYPE.RIGHT;
                        }
                    } else {
                        min = new Vector3f(min_c.x + 1, min_c.y, min_c.z);
                        max = new Vector3f(min_c.x + 1, min_c.y + 1, min_c.z + 1);
                        box = new Box(min, max);
                        if (ray.intersect(box, -100, -1)) {
                            selection.second = Block.TYPE.LEFT;
                        }
                    }
                }
            }
        }

        return selection;
    }

    public static boolean remove(Ray ray) {
        Block selected_block = select(ray).first;

        if (selected_block != null) {
            Chunk chunk = selected_block.chunk;
            if (chunk != null && !chunk.updating) {
                Vector3f pos = selected_block.corners[0];

                int x = (int) pos.x % World.chunk_width;
                int y = (int) pos.z % World.chunk_length;
                int z = (int) pos.y % World.chunk_height;
                chunk.blocks[x][y][z] = 0;

                chunk.changed = true;

                return true;
            }
        }

        return false;
    }

    public static boolean insert(Ray ray) {
        Pair<Block, Block.TYPE> selection = select(ray);

        if (selection.first != null && selection.second != null) {
            Chunk chunk = selection.first.chunk;
            if (chunk != null && !chunk.updating) {
                Vector3f pos = selection.first.corners[0];

                int x = (int) pos.x % World.chunk_width;
                int y = (int) pos.z % World.chunk_length;
                int z = (int) pos.y % World.chunk_height;

                chunk.changed = true;

                System.out.println(selection.second);
                switch (selection.second) {
                    case FRONT:
                        if (y - 1 < 0) {
                            return false;
                        }
                        chunk.blocks[x][y - 1][z] = Block.pack_type(0, 1);

                        break;
                    case BACK:
                        if (y + 1 >= World.chunk_length) {
                            return false;
                        }
                        chunk.blocks[x][y + 1][z] = Block.pack_type(0, 1);

                        break;
                    case RIGHT:
                        if (x - 1 < 0) {
                            return false;
                        }
                        chunk.blocks[x - 1][y][z] = Block.pack_type(0, 1);
                        break;
                    case LEFT:
                        if (x + 1 >= World.chunk_width) {
                            return false;
                        }
                        chunk.blocks[x + 1][y][z] = Block.pack_type(0, 1);
                        break;
                    case TOP:
                        if (z + 1 >= World.chunk_height) {
                            return false;
                        }
                        chunk.blocks[x][y][z + 1] = Block.pack_type(0, 1);
                        break;
                    case BOTTOM:
                        if (z - 1 < 0) {
                            return false;
                        }
                        chunk.blocks[x][y][z - 1] = Block.pack_type(0, 1);
                        break;
                    default:
                        chunk.changed = false;
                        break;
                }
                return chunk.changed;
            }
        }
        return false;
    }
}

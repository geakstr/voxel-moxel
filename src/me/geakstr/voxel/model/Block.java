package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.util.BitUtils;

/**
 * Block represented as 32 bit integer number.
 *
 * The first 9 bit (from left) is type of block. Possible 512 types (0..511).
 */
public class Block extends AABB {
    public int type;
    public Chunk chunk;

    public Block(int type, Vector3f pos, Chunk chunk) {
        super();

        int chunk_x = chunk.x_chunk_pos * Chunk.size;
        int chunk_y = chunk.y_chunk_pos * Chunk.height;
        int chunk_z = chunk.z_chunk_pos * Chunk.size;

        int x = (int) pos.x + chunk_x;
        int y = (int) pos.y + chunk_y;
        int z = (int) pos.z + chunk_z;

        this.type = type;
        this.corners[0] = new Vector3f(x, y, z);
        this.corners[1] = new Vector3f(x + 1, y + 1, z + 1);
        this.chunk = chunk;
    }

    public Block(Vector3f pos, Chunk chunk) {
        this(0, pos, chunk);
    }

    public static int pack_type(int val, int type) {
        return BitUtils.set_val_to_left_of_pos(BitUtils.clear_range(val, 9, 0), type, 23);
    }

    public static int unpack_type(int val) {
        return BitUtils.extract_range(val, 9, 23);
    }

}
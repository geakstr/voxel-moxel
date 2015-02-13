package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

public class Block extends Box {
    public int type;
    public Chunk chunk;

    public Block(int type, Vector3f pos, Chunk chunk) {
        super();

        int chunk_x = chunk.x_chunk_pos * World.chunk_width;
        int chunk_y = chunk.y_chunk_pos * World.chunk_length;
        int chunk_z = chunk.z_chunk_pos * World.chunk_height;

        int x = (int) pos.x + chunk_x;
        int y = (int) pos.y + chunk_y;
        int z = (int) pos.z + chunk_z;

        this.type = type;
        this.corners[0] = new Vector3f(x, z, y);
        this.corners[1] = new Vector3f(x + 1, z + 1, y + 1);
        this.chunk = chunk;
    }

    public Block(Vector3f pos, Chunk chunk) {
        this(0, pos, chunk);
    }
}
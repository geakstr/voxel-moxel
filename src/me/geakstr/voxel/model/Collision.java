package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

import java.util.Set;

public class Collision {

    public static void check(Set<Chunk> nearest_chunks, Box aabb) {
        for(Chunk chunk : nearest_chunks) {
        	int chunk_x = chunk.x_chunk_pos * World.chunk_width;
        	int chunk_y = chunk.z_chunk_pos * World.chunk_height;
        	int chunk_z = chunk.y_chunk_pos * World.chunk_length;
        	Box chunk_box = new Box(new Vector3f(chunk_x, chunk_y, chunk_z), new Vector3f(chunk_x + World.chunk_width, chunk_y + World.chunk_height, chunk_z + World.chunk_length));
        	if(detectCollision(aabb, chunk_box)) {
	            int[][][] blocks = chunk.blocks;
	            for (int x = 0; x < blocks.length; x++) {
	                for (int y = 0; y < blocks[x].length; y++) {
	                    for (int z = 0; z < blocks[x][y].length; z++) {
	                        int type = Block.unpack_type(chunk.blocks[x][y][z]);
	                        if (type != 0) {
	                            Block cube = new Block(type, new Vector3f(x, y, z), chunk);
	                            if (detectCollision(aabb, cube)) {
	                                System.out.println("chirk " + aabb.corners[0]);
	                            }
	                        }
	                    }
	                }
	            }
        	}
        }
    }
    
    public static boolean detectCollision(Box a, Box b)
    {
        if (a.corners[1].x < b.corners[0].x || a.corners[0].x > b.corners[1].x) return false;
        if (a.corners[1].y < b.corners[0].y || a.corners[0].y > b.corners[1].y) return false;
        if (a.corners[1].z < b.corners[0].z || a.corners[0].z > b.corners[1].z) return false;
        return true;
    }

}

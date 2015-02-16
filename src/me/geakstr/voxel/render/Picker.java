package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.Block;
import me.geakstr.voxel.model.Box;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.model.World;

public class Picker {
	public static Block select(Ray ray) {
		Chunk selection_chunk = null;

        final Vector3f camera_position = Camera.position.negate(null);

        float min_dist = Float.MAX_VALUE;
        for (int chunk_z = 0; chunk_z < World.world_height; chunk_z++) {
            for (int chunk_x = 0; chunk_x < World.world_size; chunk_x++) {
                for (int chunk_y = 0; chunk_y < World.world_size; chunk_y++) {
                    Vector3f min = new Vector3f(World.chunk_width * chunk_x, World.chunk_height * chunk_z, World.chunk_length * chunk_y);
                    Vector3f max = new Vector3f(min.x + World.chunk_width, min.y + World.chunk_height, min.z + World.chunk_length);
                    if (ray.intersect(new Box(min, max), -100, -1)) {
                        float dist = Vector3f.dist(min, camera_position);
                        if (dist < min_dist) {
                            selection_chunk = World.chunks[chunk_z][chunk_x][chunk_y];
                            min_dist = dist;
                        }
                    }
                }
            }
        }

        Block selection_block = null;
        if (selection_chunk != null) {
            min_dist = Float.MAX_VALUE;
            for (int block_x = 0; block_x < World.chunk_width; block_x++) {
                for (int block_y = 0; block_y < World.chunk_length; block_y++) {
                    for (int block_z = 0; block_z < World.chunk_height; block_z++) {
                        Block block = new Block(new Vector3f(block_x, block_y, block_z), selection_chunk);
                        if (Block.unpack_type(selection_chunk.blocks[block_x][block_y][block_z]) != 0 && ray.intersect(block, -100, -1)) {
                            float dist = Vector3f.dist(block.corners[0], camera_position);
                            if (dist < min_dist) {
                                selection_block = block;
                                min_dist = dist;
                            }
                        }
                    }
                }
            }
        }
        
        return selection_block;
	}
	
	public static boolean remove(Ray ray) {
        Block selected_block = select(ray);
        
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
}

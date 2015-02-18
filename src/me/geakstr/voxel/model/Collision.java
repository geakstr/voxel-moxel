package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Vector3f;

import java.util.Set;

public class Collision {

    public static void check(Set<Chunk> nearest_chunks, Box aabb) {
        for(Chunk chunk : nearest_chunks) {
            int[][][] blocks = chunk.blocks;
            for (int x = 0; x < blocks.length; x++) {
                for (int y = 0; y < blocks[x].length; y++) {
                    for (int z = 0; z < blocks[x][y].length; z++) {
                        int type = Block.unpack_type(chunk.blocks[x][y][z]);
                        if (type != 0) {
                            Block cube = new Block(type, new Vector3f(x, y, z), chunk);
                            if (detectCollision(aabb, cube)) {
                                System.out.println("chirk");
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
        System.out.println(a.corners[0] + " : " + b.corners[0]);
        return true;
    }

}

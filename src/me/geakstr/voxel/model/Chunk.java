package me.geakstr.voxel.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chunk extends Mesh {
    public int[][][] cubes; // [x][y][z]

    public boolean changed;

    public int x_chunk_pos, y_chunk_pos;
    public int x_offset, y_offset;

    public Chunk(int x_chunk_pos, int y_chunk_pos) {
        super();

        this.x_chunk_pos = x_chunk_pos;
        this.y_chunk_pos = y_chunk_pos;

        this.x_offset = x_chunk_pos * World.chunk_width;
        this.y_offset = y_chunk_pos * World.chunk_length;

        this.vertices_size = World.chunk_volume * CubeManager.cube_vertices_size;
        //this.indices_size = volume * 36 + width;

        this.vertices = new float[vertices_size];
        //this.indices = new int[indices_size];

        this.cubes = new int[World.chunk_width][World.chunk_length][World.chunk_height];
        this.changed = true;
    }

    public void update() {
        changed = false;

        int next_color = 512;
        int vertices_offset = 0/*, indices_offset = 0*/;
        for (int z = 0; z < World.chunk_height; z++) {
        	int[][] mark = new int[World.chunk_length][World.chunk_width];
        	int[] proj = new int[mark[0].length];
            Arrays.fill(proj, -1); int len = 0;
            Map<Integer, int[]> coords_map = new HashMap<>();
            for (int y = 0; y < World.chunk_length; y++) {
            	boolean canDown = false; int projFlag = -1;
                for (int x = 0; x < World.chunk_width; x++) {
                    int val = cubes[x][y][z];
                    int type = CubeManager.unpack_type(val);
                    
                    int i = y, j = x;
                    
                    if (type == 0) {
                        continue;
                    }
                    
                    boolean update_coords = false;
                    
                    if (proj[j] != -1) {
                    	mark[i][j] = mark[i - 1][j];
                        projFlag = j;
                        len = 0;
                        update_coords = true;
                    } else if (((j > 0) && (CubeManager.unpack_type(cubes[j - 1][i][z]) == type) && (projFlag != j - 1))) {
                        mark[i][j] = mark[i][j - 1];
                        update_coords = true;
                    } else {
                    	mark[i][j] = ++next_color;
                    	coords_map.put(next_color, new int[] { j, i, j, i });
                        len = 0;
                    }
                    
                    if (update_coords) {
                    	int[] tmp = null; int face = 0;
                    	face = proj[j] != -1 ? proj[j] : mark[i][j - 1];
                    	tmp = coords_map.get(face);
                    	tmp[2] = j;
                    	tmp[3] = i;
                    	coords_map.put(face, tmp);
                    }
                    
                    if (j > 0 && mark[i][j - 1] == mark[i][j]) {
                        len++;
                    }
                    
                    if (len > 0 && !canDown) {
                        canDown = false;
                    } else {
                        canDown = (i < (World.chunk_length - 1) && (CubeManager.unpack_type(cubes[j][i + 1][z]) == type)) ? true : false;
                    }
                    
                    if (canDown) {
                        proj[j] = mark[i][j];
                    } else {
                        int tmp = len;
                        while (tmp >= 0) {
                            proj[j - tmp] = -1;
                            tmp--;
                        }
                    }    
                    
                  boolean[] sides = renderable_sides(x, y, z);
                  for (int side = 0; side < 6; side++) {
                      if (sides[side]) {
                          float[] side_vertices = CubeManager.get_side(side, x_offset + x, z, y + y_offset/*, vertices_offset*/);
                          System.arraycopy(side_vertices, 0, vertices, vertices_offset, CubeManager.cube_side_vertices_size);
                          vertices_offset += CubeManager.cube_side_vertices_size;
                      }
                  }
                }
            }
            
            for (Map.Entry<Integer, int[]> e : coords_map.entrySet()) {
            	System.err.println(e.getKey() + " " + Arrays.toString(e.getValue()));
            	
            	int[] coords = e.getValue();
            	int x0 = coords[0], y0 = coords[1], x1 = coords[2], y1 = coords[3];
            	
            	System.err.println(Arrays.toString(CubeManager.get_side(0, x0, y0, x1, y1)));
            }

            
            for (int[] arr : mark) {
            	System.err.println(Arrays.toString(arr));
            }
        }

        vertices = Arrays.copyOfRange(vertices, 0, vertices_offset);
        vertices_size = vertices_offset;

        fill_buffers();
    }

    public boolean[] renderable_sides(int x, int y, int z) {
        boolean[] sides = new boolean[6];

        // Blocks inside chunk
        if (x == 0 || CubeManager.unpack_type(cubes[x - 1][y][z]) == 0) {
            sides[0] = true; // Back
        }
        if (x == World.chunk_width - 1 || CubeManager.unpack_type(cubes[x + 1][y][z]) == 0) {
            sides[1] = true; // Front
        }
        if (y == World.chunk_length - 1 || CubeManager.unpack_type(cubes[x][y + 1][z]) == 0) {
            sides[2] = true; // Left
        }
        if (y == 0 || CubeManager.unpack_type(cubes[x][y - 1][z]) == 0) {
            sides[3] = true; // Right
        }
        if (z == 0 || CubeManager.unpack_type(cubes[x][y][z - 1]) == 0) {
            sides[4] = true; // Bottom
        }
        if (z == World.chunk_height - 1 || CubeManager.unpack_type(cubes[x][y][z + 1]) == 0) {
            sides[5] = true; // Top
        }

        // Blocks on edge of chunk
        if (sides[0] &&
                x == 0 &&
                x_chunk_pos != 0 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos - 1][y_chunk_pos].cubes[World.chunk_width - 1][y][x]) != 0) {
            sides[0] = false;
        }
        if (sides[1] &&
                x == World.chunk_width - 1 &&
                x_chunk_pos != World.world_size - 1 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos + 1][y_chunk_pos].cubes[0][y][z]) != 0) {
            sides[1] = false;
        }
        if (sides[2] &&
                y == World.chunk_length - 1 &&
                y_chunk_pos != World.world_size - 1 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos + 1].cubes[x][0][x]) != 0) {
            sides[2] = false;
        }
        if (sides[3] &&
                y == 0 &&
                y_chunk_pos != 0 &&
                CubeManager.unpack_type(World.chunks[x_chunk_pos][y_chunk_pos - 1].cubes[x][World.chunk_length - 1][z]) != 0) {
            sides[3] = false;
        }

        return sides;
    }

    public void render() {
        if (changed) {
            update();
        }
        super.render();
    }
}

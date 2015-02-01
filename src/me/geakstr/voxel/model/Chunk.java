package me.geakstr.voxel.model;

public class Chunk extends Mesh {
    public int[][][] cubes; // [z][x][y]

    public boolean changed;

    public int x_offset, y_offset;

    public Chunk(int x_offset, int y_offset) {
        super();

        this.x_offset = x_offset;
        this.y_offset = y_offset;

        this.vertices_size = World.chunk_volume * CubeManager.size;
        //this.indices_size = volume * 36 + width;

        this.vertices = new float[vertices_size];
        //this.indices = new int[indices_size];

        this.cubes = new int[World.chunk_height][World.chunk_width][World.chunk_length];
        this.changed = true;
    }

    public void update() {
        changed = false;

        int vertices_offset = 0/*, indices_offset = 0*/;
        for (int z = 0; z < World.chunk_height; z++) {
            for (int x = 0; x < World.chunk_width; x++) {
                for (int y = 0; y < World.chunk_length; y++) {
                    int type = cubes[z][x][y];

                    if (type == 0) {
                        continue;
                    }

                    Cube cube = CubeManager.get(x_offset + x, y, z + y_offset/*, vertices_offset*/);
                    System.arraycopy(cube.vertices, 0, vertices, vertices_offset, CubeManager.size);
                    vertices_offset += CubeManager.size;
                }
            }
        }

        fill_buffers();
    }

    public Mesh render() {
        if (changed) {
            update();
        }
        super.render();

        return this;
    }
}

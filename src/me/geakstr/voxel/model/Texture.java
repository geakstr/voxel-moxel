package me.geakstr.voxel.model;

public class Texture {

    public int id;
    public String name;
    public int width;
    public int height;

    public Texture(int id, int width, int height, String name) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.name = name;
    }

}

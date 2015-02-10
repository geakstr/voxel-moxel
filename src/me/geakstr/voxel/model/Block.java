package me.geakstr.voxel.model;

public class Block extends Box {
    public int type;

    public Block() {
        this.type = 0;
    }

    public Block(int type) {
        this.type = type;
    }
}
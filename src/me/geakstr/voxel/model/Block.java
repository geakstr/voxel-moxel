package me.geakstr.voxel.model;

public class Block extends Box {
    public int type;

    public Block(int type) {
        this.type = type;
    }

    public Block() {
        this(0);
    }
}
package me.geakstr.voxel.model;

import java.util.Arrays;

/*
Cube in our world is 32 bit integer

First 16 bits (from left to right) is a type of block (511 possible values)
Next 1 bit is a visibility (1 - vis, 0 - invis)
 */
public class Cube extends Mesh {
    public Cube(float[] vertices/*, int[] indices*/) {
        super(vertices/*, indices*/);
    }

    
}

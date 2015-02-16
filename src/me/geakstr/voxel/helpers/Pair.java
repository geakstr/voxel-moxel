package me.geakstr.voxel.helpers;

public class Pair<FIRST, SECOND> {
    public FIRST first;
    public SECOND second;

    public Pair() {
        this(null, null);
    }

    public Pair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }
}

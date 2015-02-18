package me.geakstr.voxel.util;

public class BitUtils {
    public static int get_bit(int val, int pos) {
        return (val >> pos) & 1;
    }

    public static int set_bit(int val, int pos) {
        return val | (1 << pos);
    }
}

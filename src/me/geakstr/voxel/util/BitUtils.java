package me.geakstr.voxel.util;

public class BitUtils {
    public static int clear_range(final int original, final int number_of_bits_to_right, final int offset_from_left) {
        return original & ~((~0 << (32 - number_of_bits_to_right)) >>> offset_from_left);
    }

    public static int set_val_to_left_of_pos(final int original, final int val, final int pos) {
        return original | (val << pos);
    }

    public static int extract_range(final int original, final int number_of_bits_to_left, final int offset_from_right) {
        final int rightShifted = original >>> offset_from_right;
        final int mask = (1 << number_of_bits_to_left) - 1;
        return rightShifted & mask;
    }

    public static String binary_view(final int x) {
        return String.format("%32s", Integer.toBinaryString(x)).replace(' ', '0');
    }
}

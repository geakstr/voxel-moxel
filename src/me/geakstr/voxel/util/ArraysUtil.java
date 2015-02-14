package me.geakstr.voxel.util;

import java.util.ArrayList;
import java.util.List;

public class ArraysUtil {
    public static int[] copy_ints(List<Integer> arr) {
        int size = arr.size();
        int[] ret = new int[size];

        for (int i = 0; i < size; i++) {
            ret[i] = arr.get(i);
        }

        return ret;
    }

    public static List<Integer> copy_ints(int[] arr) {
        int size = arr.length;
        List<Integer> ret = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            ret.add(arr[i]);
        }

        return ret;
    }

    public static float[] copy_floats(List<Float> arr) {
        int size = arr.size();
        float[] ret = new float[size];

        for (int i = 0; i < size; i++) {
            ret[i] = arr.get(i);
        }

        return ret;
    }

    public static List<Float> copy_floats(float[] arr) {
        int size = arr.length;
        List<Float> ret = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            ret.add(arr[i]);
        }

        return ret;
    }
}

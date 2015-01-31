package me.geakstr.voxel.util;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FileUtil {

    public static String getFileInSameLevelOf(String path, String name) {
        path = path.replaceAll("\\\\", "/");
        String file = path.substring(0, path.lastIndexOf("/") + 1) + name;
        return file;
    }

    public static String readFromFile(String name) {
        StringBuilder source = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));

            String line;
            while ((line = reader.readLine()) != null)
                source.append(line.trim()).append("\n");

            reader.close();
        } catch (Exception e) {
            System.err.println("Error loading source code: " + name);
            e.printStackTrace();
        }

        return source.toString();
    }

    public static String[] readAllLines(String name) {
        return readFromFile(name).split("\n");
    }

    public static String[] removeEmptyString(String[] data) {
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < data.length; i++)
            if (!data[i].equals("")) result.add(data[i]);

        String[] res = new String[result.size()];
        result.toArray(res);

        return res;
    }

}
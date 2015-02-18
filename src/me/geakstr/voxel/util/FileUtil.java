package me.geakstr.voxel.util;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileUtil {
	public static class Reader {
        private BufferedReader in;
        private StringTokenizer st;

        public Reader(String fileName) {
            try {
                this.in = new BufferedReader(new FileReader(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean ready() {
            boolean ready = false;
            try {
                ready = in.ready();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ready;
        }

        public String ns() {
            try {
                while (st == null || !st.hasMoreTokens()) {
                    st = new StringTokenizer(in.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return st.nextToken();
        }

        public int ni() {
            return Integer.parseInt(ns());
        }

        public long nl() {
            return Long.parseLong(ns());
        }

        public double nd() {
            return Double.parseDouble(ns());
        }

        public float nf() {
            return Float.parseFloat(ns());
        }

        public String line() {
            String line = "";
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line;
        }
        
        public String[] tokens() {
        	return line().trim().split("\\s+");
        }

        public void close() {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
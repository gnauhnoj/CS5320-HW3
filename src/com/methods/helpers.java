package com.methods;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jhh11 on 11/5/14.
 */
public class helpers {
    public static final Path getPath () {
        Path path = Paths.get(System.getProperty("user.dir")).resolve("data");
        return path;
    }

    public static final String mapPath (String arg) {
        return getPath().resolve(arg + "-map").toString();
    }

    public static final String dataPath (String arg) {
        return getPath().resolve(arg + "-data").toString();
    }

    public static void writeInt (RandomAccessFile raf, long location, int index) throws IOException {
        raf.seek(location);
        raf.writeInt(index);
    }

    public static int readMapHeader (RandomAccessFile raf) throws IOException {
        raf.seek(0);
        return raf.readInt();
    }

    public static int writeMap (RandomAccessFile raf, long start, long end) throws IOException {
        int index = helpers.readMapHeader(raf) + 1;
        writeInt(raf, 0, index);
        writeInt(raf, raf.length(), index);
        raf.writeLong(start);
        raf.writeLong(end);
        return index;
    }

    public static long[] writeData (RandomAccessFile raf, String[] arg) throws IOException {
        long[] out = new long[2];
        raf.seek(raf.length());
        out[0] = raf.getFilePointer();

        // total size for each measure = 5*4 (int,float) + 10*2 (date) + 8*2 (time) = 56
        for (String a : arg) {
            //raf.writeChars(a + " ");
            String[] values = a.split(",");
            raf.writeFloat(Float.parseFloat(values[0]));
            raf.writeFloat(Float.parseFloat(values[1]));
            raf.writeInt(Integer.parseInt(values[2]));
            raf.writeFloat(Float.parseFloat(values[3]));
            raf.writeFloat(Float.parseFloat(values[4]));
            raf.writeChars(values[5]);
            raf.writeChars(values[6]);
        }

        out[1] = raf.getFilePointer();
        System.out.println(out[0]);
        System.out.println(out[1]);
        return out;
    }

    public static boolean fileExists (File map, File data) throws IOException {
        return ((map.exists() && !map.isDirectory()) || (data.exists() && !data.isDirectory()));
    }
}

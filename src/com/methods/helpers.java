package com.methods;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

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

    public static final String freespacePath (String arg) {
        return getPath().resolve(arg + "-freespace").toString();
    }

    public static void writeInt (RandomAccessFile raf, long location, int index) throws IOException {
        raf.seek(location);
        raf.writeInt(index);
    }

    public static int readHeader (RandomAccessFile raf) throws IOException {
        raf.seek(0);
        return raf.readInt();
    }

    public static int writeMap (RandomAccessFile raf, long start, long end) throws IOException {
        int index = readHeader(raf) + 1;
        writeInt(raf, 0, index);
        writeInt(raf, raf.length(), index);
        raf.writeLong(start);
        raf.writeLong(end);
        return index;
    }

    // Writes details of deleted index into freespace file
    public static int writeFreespace (RandomAccessFile raf, long[] limits) throws IOException {
        int index = readHeader(raf) + 1;
        writeInt(raf, 0, index);
        writeInt(raf, raf.length(), (int) limits[2]);
        raf.writeLong(limits[0]);
        raf.writeLong(limits[1]);
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

    public static boolean fileExists (File file) throws IOException {
        return ((file.exists() && !file.isDirectory()));
    }

    // dataRaf should have been navigated
    public static String readEntry (RandomAccessFile dataRaf) throws IOException {
        float one = dataRaf.readFloat();
        float two = dataRaf.readFloat();
        int three = dataRaf.readInt();
        float four = dataRaf.readFloat();
        float five = dataRaf.readFloat();

        byte[] sixArr = new byte[20];
        dataRaf.readFully(sixArr);
        String six = new String(sixArr);

        byte[] sevenArr = new byte[16];
        dataRaf.readFully(sevenArr);
        String seven = new String(sevenArr);

        String entry = one + "," + two + "," + three + "," + four + "," + five + "," + six + "," + seven;
        return entry;
    }

    // Returns array (long) with start and end index of trajectory in data file as well as offset within map file
    public static long[] getLimits (RandomAccessFile raf, int id){
        long[] limits = new long[3];

        // search through for desired index
        int offset = 4;
        try{
            boolean nfound = true;
            while (nfound) {
                raf.seek(offset);
                int found = raf.readInt();
                //System.out.println("found: " +found);
                nfound = !(id == found);
                offset = (nfound) ? offset + 20 : offset;
                if (nfound && ((offset + 20) > raf.length())) {
                    throw new IndexOutOfBoundsException("Index does not exist");
                }
            }
            limits[0] = raf.readLong();
            limits[1] = raf.readLong();
            limits[2] = (long) offset;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return limits;
    }
}

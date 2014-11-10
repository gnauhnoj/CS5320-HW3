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
    public static Path getPath () {
        Path path = Paths.get(System.getProperty("user.dir")).resolve("data");
        return path;
    }

    public static String mapPath (String arg) {
        return getPath().resolve(arg + "-map").toString();
    }

    public static String dataPath (String arg) {
        return getPath().resolve(arg + "-data").toString();
    }

    public static String freespacePath (String arg) {
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

    public static int writeMap (RandomAccessFile raf, long start, long end, long pointer) throws IOException {
        int index = readHeader(raf) + 1;
        writeInt(raf, 0, index);
        writeInt(raf, pointer, index);
        raf.writeLong(start);
        raf.writeLong(end);
        return index;
    }

    // Writes details of deleted index into freespace file
    public static int writeFreespace (RandomAccessFile raf, long[] limits) throws IOException {
        int index = readHeader(raf) + 1;
        writeInt(raf, 0, index);
        raf.seek(raf.length());
        raf.writeLong(limits[2]);

        raf.writeLong(limits[0]);
        raf.writeLong(limits[1]);
        return index;
    }

    public static long[] writeData (RandomAccessFile raf, String[] arg, long startPointer) throws IOException {
        long[] out = new long[2];
        raf.seek(startPointer);
        out[0] = raf.getFilePointer();

        // total size for each measure = 5*4 (int,float) + 10*2 (date) + 8*2 (time) = 56
        for (String a : arg) {
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
    public static long[] getLimits (RandomAccessFile raf, int id) throws IOException{
        long[] limits = new long[3];

        // search through for desired index
        long offset = 4;

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
        limits[2] = offset;

        return limits;
    }

    public static boolean checkSpace (RandomAccessFile freespaceRaf) throws IOException{
        freespaceRaf.seek(0);
        int number = freespaceRaf.readInt();
        if (number > 0) return true;
        else return false;
    }

    // If space is found, rearranges freespace file. If not, returns -1,-1.
    // startPointer[0] -> position in data file where data should be written (otherwise -1)
    // startPointer[1] -> position where map file should be updated (-1 if at end)
    public static long[] getStartPointer (RandomAccessFile freespaceRaf, long requiredSize) throws IOException {
        long[] startPointer = new long[2];
        startPointer[0] = -1;
        startPointer[1] = -1;
        long offset = 12;
        boolean space = false;

        while (!space){
            freespaceRaf.seek(offset);
            long start = freespaceRaf.readLong();
            long end = freespaceRaf.readLong();
            long availableSize = end - start;

            // Check whether there is enough space for new traj
            if (availableSize >= requiredSize) {
                space = true;
                startPointer[0] = start;

                freespaceRaf.seek(offset - 8);
                startPointer[1] = freespaceRaf.readLong();

                // Update freespace file

                if (availableSize == requiredSize) {

                    //remove entry completely by shifting last row up and truncating file
                    freespaceRaf.seek(freespaceRaf.length() - 24);
                    long mapPointer = freespaceRaf.readLong();
                    start = freespaceRaf.readLong();
                    end = freespaceRaf.readLong();
                    freespaceRaf.seek(offset - 8);
                    freespaceRaf.writeLong(mapPointer);
                    freespaceRaf.writeLong(start);
                    freespaceRaf.writeLong(end);

                    // Truncate file
                    freespaceRaf.setLength(freespaceRaf.length() - 24);

                    // Reduce count by 1 since the free slot is now completely filled
                    int index = readHeader(freespaceRaf) - 1;
                    writeInt(freespaceRaf, 0, index);
                }
                else {
                    // New available start position shifted to end of inserted trajectory
                    // write -1 in for mapPointer location and rewrite start
                    freespaceRaf.seek(offset-8);
                    freespaceRaf.writeLong(-1);
                    start = start + requiredSize;
                    freespaceRaf.writeLong(start);
                }
            }
            offset = offset + 24;
            if ((offset + 24) > freespaceRaf.length() && !space) {
                break;
            }
        }
        return startPointer;
    }
}

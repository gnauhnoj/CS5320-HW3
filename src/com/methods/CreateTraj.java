package com.methods;
import java.io.File;
import java.io.RandomAccessFile;


public class CreateTraj {
    public static void create (String arg) {
        File map = new File(com.methods.helpers.mapPath(arg));
        File data = new File(com.methods.helpers.dataPath(arg));
        try {
            data.getParentFile().mkdirs();
            data.createNewFile();
            map.getParentFile().mkdirs();
            map.createNewFile();

            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");

            helpers.writeInt(mapRaf, 0, 0);

            mapRaf.close();
        } catch (Exception e) {e.printStackTrace();}
    }
}

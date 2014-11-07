package com.methods;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;


public class CreateTraj {
    public static void create (String arg) {
        File map = new File(com.methods.helpers.mapPath(arg));
        File data = new File(com.methods.helpers.dataPath(arg));

        // FORMAT - header: int, every row: long ,long,long
        File freespace = new File(com.methods.helpers.freespacePath(arg));

        try {
            if (helpers.fileExists(map) || helpers.fileExists(data) || helpers.fileExists(freespace)) {
                throw new FileNotFoundException("Trajectory Set Already Exists");
            }

            data.getParentFile().mkdirs();
            data.createNewFile();
            map.getParentFile().mkdirs();
            map.createNewFile();
            freespace.getParentFile().mkdirs();
            freespace.createNewFile();

            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile spaceRaf = new RandomAccessFile(freespace, "rw");
            helpers.writeInt(spaceRaf,0,0);
            helpers.writeInt(mapRaf, 0, 0);
            mapRaf.close();
            spaceRaf.close();

        } catch (Exception e) {e.printStackTrace();}
    }
}

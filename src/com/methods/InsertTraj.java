package com.methods;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by jhh11 on 11/5/14.
 */
public class InsertTraj {
    public static int insert (String name, String[] arg) {
        File map = new File(com.methods.helpers.mapPath(name));
        File data = new File(com.methods.helpers.dataPath(name));
        try {
            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile dataRaf = new RandomAccessFile(data, "rw");

            long[] dataLoc = helpers.writeData(dataRaf, arg);
            int index = helpers.writeMap(mapRaf, dataLoc[0], dataLoc[1]);

            mapRaf.close();
            dataRaf.close();
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

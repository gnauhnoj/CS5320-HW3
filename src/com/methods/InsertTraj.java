package com.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by jhh11 on 11/5/14.
 */
public class InsertTraj {
    public static int insert (String name, String[] arg) {
        File map = new File(com.methods.helpers.mapPath(name));
        File data = new File(com.methods.helpers.dataPath(name));
        File freespace =  new File(com.methods.helpers.freespacePath(name));
        try {
            if (!helpers.fileExists(map) || !helpers.fileExists(data) || !helpers.fileExists(freespace)) {
                throw new FileNotFoundException("Trajectory Set Does Not Exist");
            }

            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile dataRaf = new RandomAccessFile(data, "rw");
            RandomAccessFile freespaceRaf = new RandomAccessFile(freespace, "rw");

            long[] dataLoc;
            long mapPointer;
            // Check whether there is enough freespace to insert it in the middle

            // TODO: Refactor this - this is so un-DRY
            if (helpers.checkSpace(freespaceRaf)){
                // Check whether any gap is big enough to fit new trajectory, if not, write it at bottom of data file
                long requiredByteSize = arg.length * 56;
                long startPointer[] = helpers.getStartPointer(freespaceRaf, requiredByteSize);
                // TODO: Ternary
                if (startPointer[0] == -1) {
                    startPointer[0] = dataRaf.length();
                    mapPointer = mapRaf.length();
                } else {
                    // Check whether info about new trajectory is to be written at bottom of map file
                    if (startPointer[1] != -1) {
                        mapPointer = startPointer[1];
                    } else {
                        mapPointer = mapRaf.length();
                    }
                }
                dataLoc = helpers.writeData(dataRaf, arg, startPointer[0]);
            } else {
                dataLoc = helpers.writeData(dataRaf, arg, dataRaf.length());
                mapPointer = mapRaf.length();
            }

            int index = helpers.writeMap(mapRaf, dataLoc[0], dataLoc[1], mapPointer);

            mapRaf.close();
            dataRaf.close();
            freespaceRaf.close();
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

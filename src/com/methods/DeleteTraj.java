package com.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by Alap on 11/6/14.
 */
public class DeleteTraj {

    public static String delete (String tname, String tid){
        String result;

        File map = new File(com.methods.helpers.mapPath(tname));
        File freespace = new File(com.methods.helpers.freespacePath(tname));
        int id = Integer.parseInt(tid);

        try{
            if (!helpers.fileExists(map) || !helpers.fileExists(freespace)) {
                throw new FileNotFoundException("Trajectory Set Does Not Exist");
            }

            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile freespaceRaf = new RandomAccessFile(freespace, "rw");

            // Mark -1 on index (in map file) of trajectory to be deleted
            long[] limits;
            limits = helpers.getLimits(mapRaf,id);
            //mapRaf.seek(limits[2]);
            //int oldindex = mapRaf.readInt();

            helpers.writeInt(mapRaf,limits[2],-1);
            mapRaf.seek(limits[2]);
            //int i = mapRaf.readInt();
            //System.out.println("Is it -1: " + i);
            // Write deleted traj details into freespace file (location in map file, start pointer, end pointer)
            int deleted = helpers.writeFreespace(freespaceRaf,limits);

            freespaceRaf.seek(4);
            //int j = freespaceRaf.readInt();
            //System.out.println("Is it 4: " + j);
            result = "Deleted successfully";
        }catch (Exception e){
            result = "Error in deleting";
            e.printStackTrace();
        }

        return result;
    }
}

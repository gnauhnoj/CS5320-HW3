package com.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by Alap on 11/5/14.
 */
public class RetrieveTraj {

    // retrieve trajectory
    public static String retrieve (String tname, String tid){
        String trajectory = "";
        File map = new File(com.methods.helpers.mapPath(tname));
        File data = new File(com.methods.helpers.dataPath(tname));
        try {
            if (!helpers.fileExists(map) || !helpers.fileExists(data)) {
                throw new FileNotFoundException("Trajectory Set Does Not Exist");
            }

            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile dataRaf = new RandomAccessFile(data, "rw");

            // Get start and end points for specified trajectory from map file
            long[] limits;
            int id = Integer.parseInt(tid);

            // retrieve pointers for data / map
            limits = helpers.getLimits(mapRaf,id);

            dataRaf.seek(limits[0]);
            long length = limits[1] - limits[0];
            int count = (int) (length / 56);

            // retrieve trajectory as concatenated string
            StringBuilder sb = new StringBuilder();
            String entry;
            for(int x = 0; x < count-1; x++) {
                entry = helpers.readEntry(dataRaf);
                sb.append(entry).append(" ");
            }
            trajectory = trajectory + sb.toString() + helpers.readEntry(dataRaf);

            mapRaf.close();
            dataRaf.close();
        } catch (Exception e){
            e.printStackTrace();
            trajectory = "Could not fetch trajectory";
        }
        return trajectory;
    }

    // retrieve the length of the trajectory
    public static int getCount (String tname, String tid){
        int count;
        String trajectory = retrieve(tname, tid);
        String[] parts = trajectory.split("\\s+");
        count = parts.length;
        return count;
    }
}

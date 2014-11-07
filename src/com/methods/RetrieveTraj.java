package com.methods;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by Alap on 11/5/14.
 */
public class RetrieveTraj {

    public static String retrieve (String tname, String tid){
        String trajectory = "";
        File map = new File(com.methods.helpers.mapPath(tname));
        File data = new File(com.methods.helpers.dataPath(tname));

        try {
            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile dataRaf = new RandomAccessFile(data, "rw");

            // Get start and end points for specified trajectory from map file
            long start, end;
            int id = Integer.parseInt(tid);
            int offset = 8 + 20*(id - 1); // Depends on format of map file
            mapRaf.seek(offset);
            start = mapRaf.readLong();
            end = mapRaf.readLong();
            System.out.println(start + "," + end);

            dataRaf.seek(start);
            //String s2 = dataRaf.readLine();
            long length = end - start;
            byte[] arr = new byte[(int) length];
            dataRaf.readFully(arr);
            trajectory = new String(arr);
            //System.out.println(s2);

            mapRaf.close();
            dataRaf.close();

        } catch (Exception e){
            e.printStackTrace();
            trajectory = "Could not fetch trajectory";
        }
        return trajectory;
    }

    public static int getCount (String tname, String tid){
        int count;

        String trajectory = retrieve (tname, tid);
        String[] parts = trajectory.split("\\s+");
        count = parts.length;
        return count;
    }
}

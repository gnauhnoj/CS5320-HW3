package com.methods;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Created by jhh11 on 11/5/14.
 */
public class InsertTraj {
    public static int insert (String name, String[] arg) {
        System.out.println(Arrays.toString(arg));
        File map = new File(com.methods.helpers.mapPath(name));
        File data = new File(com.methods.helpers.dataPath(name));
        try {
            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
            RandomAccessFile dataRaf = new RandomAccessFile(data, "rw");

            mapRaf.seek(0);
            int index = mapRaf.readInt() + 1;
            long start, end;

            dataRaf.seek(dataRaf.length());
            start = dataRaf.getFilePointer();

            for (String a : arg) {
                System.out.println(a);
                dataRaf.writeChars(a + " ");
            }

//            optional depending on reading approach
            //dataRaf.writeChars("\n");

            end = dataRaf.getFilePointer();
            System.out.println(start);
            System.out.println(end);

            mapRaf.seek(0);
            mapRaf.writeInt(index);
            mapRaf.seek(mapRaf.length());
            mapRaf.writeLong(start);
            mapRaf.writeLong(end);

//            Reading Options (approach changes what we need to store)
////            read file as bytes - using start and end - doesn't require line 31
//            int start = 258;
//            int end = 516;
//            dataRaf.seek(start);
//            int length = end - start;
//            byte[] arr = new byte[(int) length];
//            dataRaf.readFully(arr, 0, length);
//            String s2 = new String(arr);
//            System.out.println(s2);
//
////            read file as line - only uses start - requires line separator in line 31
//            dataRaf.seek(258);
//            System.out.println(dataRaf.readLine());

            mapRaf.close();
            dataRaf.close();
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

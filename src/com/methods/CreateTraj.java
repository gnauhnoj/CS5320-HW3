package com.methods;
import java.io.File;


public class CreateTraj {
    public static void create (String arg) {
        File map = new File(com.methods.helpers.mapPath(arg));
        File data = new File(com.methods.helpers.dataPath(arg));
        try {
            data.getParentFile().mkdirs();
            data.createNewFile();

            map.getParentFile().mkdirs();
            map.createNewFile();

//            JSONObject obj = new JSONObject();
//            JSONObject list = new JSONObject();
//            obj.put("count", 0);
//            obj.put("trajectories", list);
//
//            FileWriter file = new FileWriter(map);
//            file.write(obj.toJSONString());
//            file.flush();
//            file.close();

//            Brute force approach
//            RandomAccessFile mapRaf = new RandomAccessFile(map, "rw");
//            mapRaf.seek(0);
//            mapRaf.writeInt(1);
//            System.out.println(mapRaf.readLine());
//            mapRaf.seek(mapRaf.length());
//            System.out.println(mapRaf.getFilePointer());
//            mapRaf.writeChars("testing\r\ntesting2");
//            System.out.println(mapRaf.getFilePointer());
//            mapRaf.seek(0);
//            System.out.println(mapRaf.readLine());
//            mapRaf.seek(0);
//            System.out.println(mapRaf.readInt());

//            JSONParser parser = new JSONParser();
//            Object obj = parser.parse(new FileReader(map));
//            JSONObject jsonObject = (JSONObject) obj;
//            Long count = (Long) jsonObject.get("count");
//            System.out.println(count);
//
//            Object trajs = (Object) jsonObject.get("trajectories");


        } catch (Exception e) {e.printStackTrace();}
    }
}

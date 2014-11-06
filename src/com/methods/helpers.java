package com.methods;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jhh11 on 11/5/14.
 */
public class helpers {
    public static final Path getPath () {
        Path path = Paths.get(System.getProperty("user.dir")).resolve("data");
        return path;
    }

    public static final String mapPath (String arg) {
        return getPath().resolve(arg + "-map").toString();
    }

    public static final String dataPath (String arg) {
        return getPath().resolve(arg + "-data").toString();
    }

}

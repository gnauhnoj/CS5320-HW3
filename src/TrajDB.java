import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
import sun.tools.java.SyntaxError;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by jhh11 on 11/4/14.
 */
public class TrajDB {
    public static void main (String[] args) throws IOException {
        boolean repl = true;
        while (repl) {
            try {
                Scanner scan = new Scanner(System.in).useDelimiter(";");
                System.out.print("TrajDB > ");
                String input = scan.next();
                input = input.replaceAll("\\s+"," ");
                String[] argu = input.split(" ");
                // parse regex
                if (input.matches("^(CREATE \\w+)$")) {
                    System.out.println("create");
                    System.out.println("args: " + argu[1]);
                } else if (input.matches("^(INSERT INTO \\w+ VALUES (([\\d\\.]+,[\\d\\.]+,[\\d\\.]+,[\\d\\.]+," +
                        "[\\d\\.]+,[\\d\\-]+,[\\d\\:]+)\\s?)+)$")) {
                    System.out.println("insert");
                    String print = "args: " + argu[2];
                    for(int i = 4; i < argu.length; i++) {
                        print = print + ", " + argu[i];
                    }
                    System.out.println(print);
                } else if (input.matches("^(DELETE FROM \\w+ TRAJECTORY \\d+)$")) {
                    System.out.println("delete");
                    System.out.println("args: " + argu[2] + ", " + argu[4]);
                } else if (input.matches("^(RETRIEVE FROM \\w+ TRAJECTORY \\d+)$")) {
                    System.out.println("retrieve trajectory set");
                    System.out.println("args: " + argu[2] + ", " + argu[4]);
                } else if (input.matches("^(RETRIEVE FROM \\w+ COUNT OF \\d+)$")) {
                    System.out.println("retrieve trajectory count");
                    System.out.println("args: " + argu[2] + ", " + argu[5]);
                } else if (input.matches("^(EXIT)$")) {
                    System.out.println("exit");
                    repl = false;
                } else {
                    throw new SyntaxError();
                }
            }
            catch (Exception e) {e.printStackTrace();}
        }
    }
}

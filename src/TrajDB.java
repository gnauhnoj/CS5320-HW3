import java.util.Scanner;

/**
 * Created by jhh11 on 11/4/14.
 */
public class TrajDB {
    public static void main (String[] args) {
//        something to keep track if "EXIT" has been called
//        while something is false
        while (true) {
            Scanner scan = new Scanner(System.in).useDelimiter(";");
//        parse scan for regex;
            System.out.print(">> ");
            while(scan.hasNext()) {
                scan.next().split
            }
            scan.close();gi
        }


        // Initialize Scanner object
        Scanner scan = new Scanner("Anna Mills/Female/18");
        // initialize the string delimiter
        scan.useDelimiter("/");
        // Printing the delimiter used
        System.out.println("The delimiter use is "+scan.delimiter());
        // Printing the tokenized Strings
        while(scan.hasNext()){
            System.out.println(scan.next());
        }
        // closing the scanner stream
        scan.close();
    }
}

package com.nuriddinislamov;

public class Main {

    // This program provides a simple solution to Redgate Summer Internship assessment.

    public static void main(final String[] args) throws Exception {

        // The call to mask() method below return JSONArray object.
        // For simplicity, this program only prints out the return value

        // However, you can save it to another file, send a POST request to the server,
        // or whatever the way you want to manipulate it.

        final String USAGE = "\nUsage: java -jar DataMasker.jar [path to file with data] [path to file with rules]";

        if (args.length < 2) {
            System.out.println(("Not enough arguments are passed to the program!" + USAGE));
            return;
        } else if (args.length > 2) {
            System.out.println(("Too many arguments!" + USAGE));
            return;
        }

        // Check if the rules and data files are valid.

        // Again, for simplicity and since there is too little amount of information in the files,
        // validation functions are not implemented.

        System.out.println(Masker.mask(args[0], args[1]));
    }
}

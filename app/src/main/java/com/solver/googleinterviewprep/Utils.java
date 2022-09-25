package com.solver.googleinterviewprep;

public class Utils {
    public static String createUrlString() {
        // This is the physical address of your laptop
        // final String IP_ADDRESS = "192.168.100.14";
        // This is for when running app in emulator
        final String IP_ADDRESS = "10.0.2.2";
        final int PORT = 3000;

        return String.format("http://%s:%s", IP_ADDRESS, PORT);
    }
}

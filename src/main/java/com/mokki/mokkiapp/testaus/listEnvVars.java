package com.mokki.mokkiapp.testaus;

import java.util.Map;


public class listEnvVars {
    public static void main(String[] args) {
        // Get all environment variables
        Map<String, String> envVariables = System.getenv();

        // Iterate over the environment variables and print them
        for (Map.Entry<String, String> entry : envVariables.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}


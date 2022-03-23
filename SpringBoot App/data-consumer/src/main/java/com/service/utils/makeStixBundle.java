package com.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class makeStixBundle {

    public Boolean makeStix(String command){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            if (exitCode == 0 || exitCode == 3){
                System.out.println("Scan complete!");
                return true;
            }
            else {
                System.out.println(exitCode);
                System.out.println("Something went wrong :(");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}


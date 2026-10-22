package com.diskWise;


import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {
    public static void writeReport(String report) {
        try (FileWriter writer =
                     new FileWriter("DiskWiseReport.txt")) {

            writer.write(report);

            System.out.println("Report saved successfully.");

        } catch (IOException e) {
            System.out.println("Failed to save report.");
            e.printStackTrace();
        }
    }
}

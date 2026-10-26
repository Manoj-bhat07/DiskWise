
package com.diskWise;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateFinder {

    public static void findDuplicates(File folder) {
        List<File> files = new ArrayList<>();

        collectFiles(folder, files);

        Map<Long, List<File>> filesBySize = new HashMap<>();

        for (File file : files) {
            filesBySize
                    .computeIfAbsent(file.length(), k -> new ArrayList<>())
                    .add(file);
        }

        boolean found = false;

        System.out.println("\n===== Duplicate Files =====");

        for (List<File> sameSizeFiles : filesBySize.values()) {

            if (sameSizeFiles.size() < 2) {
                continue;
            }

            for (int i = 0; i < sameSizeFiles.size(); i++) {
                for (int j = i + 1; j < sameSizeFiles.size(); j++) {

                    File first = sameSizeFiles.get(i);
                    File second = sameSizeFiles.get(j);

                    try {
                        if (Files.mismatch(
                                first.toPath(),
                                second.toPath()
                        ) == -1) {

                            System.out.println(
                                    first.getName() + " == "
                                            + second.getName()
                            );

                            found = true;
                        }
                    } catch (IOException e) {
                        System.out.println(
                                "Could not compare: " + first.getName()
                        );
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No duplicate files found.");
        }
    }


    private static void collectFiles(File folder, List<File> files) {
        File[] contents = folder.listFiles();

        if (contents == null) {
            return;
        }

        for (File file : contents) {

            if (Files.isSymbolicLink(file.toPath())) {
                System.out.println(
                        "Skipping symbolic link: "
                                + file.getAbsolutePath()
                );
                continue;
            }

            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                collectFiles(file, files);
            }
        }
    }

}

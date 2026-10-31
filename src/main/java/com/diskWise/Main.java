package com.diskWise;


import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class Main {

    private static final long LARGE_FILE_THRESHOLD = 1024L * 1024L;

    public static void main(String[] args) {
        String folderPath = "/C://Users//user-name/ file- path//";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path.");
            return;
        }
        DuplicateFinder.findDuplicates(folder);

        ScanStats stats = new ScanStats();

        System.out.println("===== DiskWise Scanner =====");
        scanFolder(folder, stats);

        ScanRepository repository = new ScanRepository();

        try {
            long scanId = repository.saveScanHistory(
                    folder.getAbsolutePath(),
                    stats
            );

            System.out.println("Scan history saved successfully!");
            System.out.println("Generated Scan ID: " + scanId);

            int savedFiles = repository.saveScannedFiles(scanId, stats);

            System.out.println("File records saved successfully!");
            System.out.println("Total file records saved: " + savedFiles);

        } catch (SQLException e) {
            System.out.println("Database operation failed.");
            e.printStackTrace();
        }


        stats.largeFiles.sort(
                (a, b) -> Long.compare(b.size, a.size)
        );

        int topN = 1;

        System.out.println("\n===== Top " + topN + " Largest Files =====");

        for (int i = 0; i < Math.min(topN, stats.largeFiles.size()); i++) {
            FileInfo info = stats.largeFiles.get(i);

            System.out.println(
                    info.file.getAbsolutePath() + " - "
                            + info.size + " bytes"
            );
        }

        System.out.println("\n===== Largest Files First =====");

        for (FileInfo info : stats.largeFiles) {
            System.out.println(
                    info.file.getAbsolutePath() + " - "
                            + info.size + " bytes"
            );
        }

        System.out.println("\n===== Scan Summary =====");
        System.out.println("Files: " + stats.fileCount);
        System.out.println("Subdirectories: " + stats.directoryCount);
        System.out.println("Total size: " + stats.totalSize);
        for (Map.Entry<String, Integer> entry
                : stats.extensionCount.entrySet()) {
            System.out.println(
                    entry.getKey() + " : " + entry.getValue() + " files"
            );
            }
        System.out.println("\n===== File Type Summary =====");

        for (Map.Entry<String, Long> entry
                : stats.extensionSize.entrySet()) {

            double sizeInKB = entry.getValue() / 1024.0;

            System.out.printf(
                    "%s - %.2f KB%n",
                    entry.getKey(),
                    sizeInKB
            );

        }

        System.out.println("\n===== Storage Insights =====");

// Display total storage in MB
        double totalSizeMB = stats.totalSize / (1024.0 * 1024.0);

        System.out.printf("Total Storage: %.2f MB%n", totalSizeMB);

        String largestExtension = null;
        long largestExtensionSize = -1;

        for (Map.Entry<String, Long> entry
                : stats.extensionSize.entrySet()) {

            if (entry.getValue() > largestExtensionSize) {
                largestExtensionSize = entry.getValue();
                largestExtension = entry.getKey();
            }
        }

        if (largestExtension != null) {
            System.out.println(
                    "Most Storage Used By: " + largestExtension
            );
        } else {
            System.out.println("Most Storage Used By: No files with extensions");
        }


// Display the largest file
        if (stats.largestFile != null) {
            System.out.println(
                    "Largest File: "
                            + stats.largestFile.file.getName()
                            + " - "
                            + stats.largestFile.size
                            + " bytes"
            );
        } else {
            System.out.println("Largest File: No files found");
        }

        StringBuilder report = new StringBuilder();

        report.append("========== DiskWise Scan Report ==========\n\n");

// Scan Summary
        report.append("===== Scan Summary =====\n");
        report.append("Files: ").append(stats.fileCount).append("\n");
        report.append("Subdirectories: ").append(stats.directoryCount).append("\n");
        report.append("Total size: ").append(stats.totalSize).append(" bytes\n\n");

// File extension counts
        report.append("===== File Count by Extension =====\n");

        for (Map.Entry<String, Integer> entry : stats.extensionCount.entrySet()) {
            report.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append(" files\n");
        }

// File Type Summary
        report.append("\n===== File Type Summary =====\n");

        for (Map.Entry<String, Long> entry : stats.extensionSize.entrySet()) {
            report.append(String.format(
                    "%s - %.2f KB%n",
                    entry.getKey(),
                    entry.getValue() / 1024.0
            ));
        }

// Storage Insights
        report.append("\n===== Storage Insights =====\n");

        double reportSizeMB = stats.totalSize / (1024.0 * 1024.0);
        report.append(String.format("Total Storage: %.2f MB%n", reportSizeMB));

        if (largestExtension != null) {
            report.append("Most Storage Used By: ")
                    .append(largestExtension)
                    .append("\n");
        } else {
            report.append("Most Storage Used By: No files with extensions\n");
        }

        if (stats.largestFile != null) {
            report.append("Largest File: ")
                    .append(stats.largestFile.file.getName())
                    .append(" - ")
                    .append(stats.largestFile.size)
                    .append(" bytes\n");
        } else {
            report.append("Largest File: No files found\n");
        }

// Export report
        ReportWriter.writeReport(report.toString());

    }

    public static void scanFolder(File folder, ScanStats stats) {

        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println(
                    "Unable to read directory "
                            + folder.getAbsolutePath()
            );
            return;
        }

        for (File file : files) {

            if (Files.isSymbolicLink(file.toPath())) {
                System.out.println(
                        "Skipping symbolic link: "
                                + file.getAbsolutePath()
                );
                continue;
            }

            if (file.isFile()) {

                if (!file.canRead()) {
                    System.out.println(
                            "Warning: Cannot read file: "
                                    + file.getAbsolutePath()
                    );
                    continue;
                }

                if (!file.exists()) {
                    System.out.println(
                            "Warning: File disappeared during scan: "
                                    + file.getAbsolutePath()
                    );
                    continue;
                }


                long fileSize;

                try {
                    fileSize = Files.size(file.toPath());
                } catch (IOException e) {
                    System.out.println(
                            "Warning: Cannot access file: "
                                    + file.getAbsolutePath()
                                    + " | Reason: "
                                    + e.getMessage()
                    );
                    continue;
                }

                stats.fileCount++;
                stats.totalSize += fileSize;
                stats.scannedFiles.add(new FileInfo(file, fileSize));
                if (stats.largestFile == null
                        || fileSize > stats.largestFile.size) {

                    stats.largestFile = new FileInfo(file, fileSize);
                }
                System.out.println(
                        file.getAbsolutePath() + " - "
                                + fileSize + " bytes"
                );

                if (fileSize > LARGE_FILE_THRESHOLD) {
                    stats.largeFiles.add(new FileInfo(file, fileSize));
                }

                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    String extension = fileName.substring(dotIndex).toLowerCase();
                    stats.extensionCount.put(extension, stats.extensionCount.getOrDefault(extension,0) +1);

                    stats.extensionSize.put(
                            extension,
                            stats.extensionSize.getOrDefault(extension, 0L) + fileSize
                    );
                }
            } else if (file.isDirectory()) {
                stats.directoryCount++;

                System.out.println(
                        file.getName() + " - Directory"
                );

                scanFolder(file, stats);


                }
            }
        }
    }


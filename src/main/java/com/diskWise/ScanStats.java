package com.diskWise;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ScanStats {
    int fileCount = 0;
    int directoryCount = 0;
    long totalSize = 0;
    FileInfo largestFile;
    String largestFileExtension;

    // for extension
    Map<String, Integer> extensionCount = new HashMap<>();

    List<FileInfo> largeFiles = new ArrayList<>();

    Map<String, Long> extensionSize = new HashMap<>();
}

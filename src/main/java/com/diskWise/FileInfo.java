package com.diskWise;
import java.io.File;
public class FileInfo {
    File file;
    long size;

    FileInfo(File file) {
        this.file = file;
        this.size = file.length();
    }
}

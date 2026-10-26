package com.diskWise;
import java.io.File;
public class FileInfo {
    File file;
    long size;

    FileInfo(File file, long size) {
        this.file = file;
        this.size = size;
    }
}

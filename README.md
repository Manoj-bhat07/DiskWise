# DiskWise — Java Disk Scanner

DiskWise is a Core Java file-system scanning utility that analyzes files and directories to help users understand storage usage. It provides file statistics, identifies duplicate files, highlights large files, and generates a scan report.

## Features

- **Recursive Scanning:** Scans files and nested directories.
- **File Statistics:** Calculates file counts, directory counts, and total file size.
- **Large File Detection:** Identifies files larger than 1 MB.
- **Largest File Analysis:** Displays large files in descending order and identifies the largest file.
- **Duplicate Detection:** Groups files by size and compares their contents to identify duplicates.
- **File Type Analysis:** Summarizes file counts and storage usage by extension.
- **Storage Insights:** Reports total storage used and the file extension consuming the most space.
- **Report Export:** Generates a text report named `DiskWiseReport.txt`.
- **Symbolic Link Handling:** Skips symbolic links during the main scan and duplicate-file collection.
- **Basic Error Handling:** Handles certain file-access and I/O errors without immediately terminating the scan.

## Tech Stack

- Java
- Maven
- Java File I/O (`File`, `Files`, `FileWriter`)
- Java Collections Framework (`List`, `Map`, `ArrayList`, `HashMap`)
- Java NIO file operations
- IntelliJ IDEA

## Project Structure

```text
DiskWise/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── diskWise/
│                   ├── Main.java
│                   ├── DuplicateFinder.java
│                   ├── FileInfo.java
│                   ├── ReportWriter.java
│                   └── ScanStats.java
├── pom.xml
├── .gitignore
└── README.md
```

## How to Run

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Configure a compatible JDK.
4. Reload or build the Maven project.
5. Open `Main.java` and set `folderPath` to an existing directory on your computer.
6. Run `Main.java`.

The application prints scan statistics and storage insights to the console and saves a text report as `DiskWiseReport.txt` in the program's working directory.

## Concepts Practiced

- Core Java
- Recursion and directory traversal
- File handling and Java NIO
- Collections and maps
- Sorting and comparisons
- Exception handling
- Modular code organization
- Testing with empty folders, nested directories, and duplicate files

## Current Limitations

- The scan directory is configured directly in the source code.
- File sizes and access permissions can change during scanning.
- Symbolic links are skipped, but the application does not currently provide configurable link-handling options.
- Duplicate detection compares file contents after grouping files by size, which can become expensive for large collections of files.
- The report is exported as a plain-text file.

## Project Status

Core scanning, storage analysis, duplicate detection, symbolic-link handling, and report export are implemented and tested. Further improvements may be added as the project evolves.

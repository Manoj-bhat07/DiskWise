# DiskWise — Java Disk Scanner

DiskWise is a Java-based file system scanning utility that analyzes files and directories to help users understand disk usage.

## Features

- Recursively scans files and subdirectories.
- Calculates file sizes and total storage usage.
- Detects large files (1 MB threshold).
- Displays the largest files in descending order.
- Identifies duplicate files.
- Generates a scan report.

## Tech Stack

- Java
- Maven
- Java File I/O

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
3. Make sure a compatible JDK is configured.
4. Build the Maven project.
5. Run `Main.java` and configure the directory path to scan.

## Learning Goals

This project is being developed to practice Core Java, file handling, recursion, collections, sorting, and practical software development.

## Status

Under active development.

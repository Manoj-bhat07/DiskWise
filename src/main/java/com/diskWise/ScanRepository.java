package com.diskWise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class ScanRepository {

        public long saveScanHistory(String folderPath, ScanStats stats)
                throws SQLException {

            String sql = """
                INSERT INTO scan_history
                (folder_path, file_count, directory_count, total_size_bytes)
                VALUES (?, ?, ?, ?)
                """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, folderPath);
                statement.setInt(2, stats.fileCount);
                statement.setInt(3, stats.directoryCount);
                statement.setLong(4, stats.totalSize);

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {

                    if (resultSet.next()) {
                        return resultSet.getLong(1);
                    }

                    throw new SQLException("Failed to retrieve generated scan ID.");
                }
            }
        }


        public int saveScannedFiles(long scanId, ScanStats stats)
                throws SQLException {

            String sql = """
            INSERT INTO scanned_files
            (scan_id, file_path, file_name, file_size_bytes, extension)
            VALUES (?, ?, ?, ?, ?)
            """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                for (FileInfo info : stats.scannedFiles) {

                    String fileName = info.file.getName();

                    int dotIndex = fileName.lastIndexOf('.');
                    String extension = null;

                    if (dotIndex > 0) {
                        extension = fileName.substring(dotIndex).toLowerCase();
                    }

                    statement.setLong(1, scanId);
                    statement.setString(2, info.file.getAbsolutePath());
                    statement.setString(3, fileName);
                    statement.setLong(4, info.size);
                    statement.setString(5, extension);

                    statement.addBatch();
                }

                int[] results = statement.executeBatch();

                return results.length;
            }
        }

    }

package org.example.url.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileCopyAndDelete {
    public static void change_file_location(String inputfilePath, String outputFilePath ) {
        // 복사할 파일 경로와 대상 폴더 경로 설정
        String sourceFilePath = inputfilePath;
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String targetFolderPath = outputFilePath + today + "/";

        try {
            // 파일 복사
            copyFile(sourceFilePath, targetFolderPath);

            // 기존 파일 삭제
            deleteFile(sourceFilePath);

            System.out.println("파일 복사 및 삭제가 완료되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("파일 복사 및 삭제 중 오류가 발생했습니다.");
        }
    }

    // 파일을 지정된 폴더로 복사하는 메서드
    private static void copyFile(String sourceFilePath, String targetFolderPath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path targetPath = Paths.get(targetFolderPath, sourcePath.getFileName().toString());

        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    // 파일을 삭제하는 메서드
    private static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        Files.delete(path);
    }
}


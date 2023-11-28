package org.example.url.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RecentFolderFinder {
    public static String FolderFinder(String outputFilePath) {
        // 현재 날짜
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormat.format(currentDate);
        
        // 폴더 경로
        String folderPath = outputFilePath;

        // 폴더 목록을 가져옴
        File folder = new File(folderPath);
        File[] folderList = folder.listFiles();
        System.out.println(folderList);

        // 오늘 날짜 폴더를 제외한 폴더 목록을 만듦
        List<File> filteredFolders = new ArrayList<>(Arrays.asList(folderList));
        filteredFolders.removeIf(f -> !f.isDirectory() || f.getName().equals(today));

        // 가장 최근 폴더를 찾음
        File mostRecentFolder = null;
        for (File f : filteredFolders) {
            if (mostRecentFolder == null || f.lastModified() > mostRecentFolder.lastModified()) {
                mostRecentFolder = f;
            }
        }

        if (mostRecentFolder != null) {
            System.out.println("가장 최근의 폴더: " + mostRecentFolder.getAbsolutePath());
            return mostRecentFolder.getAbsolutePath();
        } else {
            System.out.println("이전에 작업한 폴더가 없습니다.");
            String Error = "error";
            return Error;
        }
    }
}

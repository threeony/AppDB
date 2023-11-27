package org.example.util;

import java.io.*;

public class CSV {
    public void toCSV(File filePath, String[][] arr) {
        // CSV 파일 쓰기
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] rowData : arr) {
                // 한 행의 데이터를 CSV 형식으로 변환하여 파일에 쓰기
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < rowData.length; i++) {
                    line.append(rowData[i]);
                    if (i < rowData.length - 1) {
                        line.append(",");
                    }
                }
                line.append("\n");
                writer.write(line.toString());
            }
            System.out.println("CSV 파일이 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

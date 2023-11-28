package org.example.url;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

    // 파일 경로를 인자로 받아 데이터를 파싱하고 엑셀 파일로 저장하는 함수
    // 매개변수로, input 경로, output 경로를 설정 필요
    public static void parseAndSaveData(String inputfilePath, String outputFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputfilePath));

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Parsed Data");
        AtomicInteger rowNum = new AtomicInteger(1);

        createHeader(sheet);

        String line;
        String currentSourceCodeName = "";
        String currentUrl = "";
        String currentPatternName = ""; // 현재 패턴 이름을 저장할 변수 추가
        Set<String> existingUrls = new HashSet<>();
        Map<String, String[]> urlToSourceCodeMap = new HashMap<>(); // 패턴 이름도 저장하기 위해 Map의 value 타입을 String 배열로 변경

        Pattern patternRegex = Pattern.compile("pattern = (.+)");
        Pattern domainPattern = Pattern.compile("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("activate lua detector. name=")) {
                if (!currentUrl.isEmpty()) {
                    currentUrl = normalizeUrl(currentUrl);
                    urlToSourceCodeMap.put(currentUrl, new String[]{currentSourceCodeName, currentPatternName});
                    currentUrl = "";
                    currentPatternName = ""; // 패턴 이름 초기화
                }
                currentSourceCodeName = line.substring(line.indexOf('=') + 1).trim();
                continue;
            }

            if (line.startsWith("###")) {
                if (!currentUrl.isEmpty()) {
                    currentUrl = normalizeUrl(currentUrl);
                    urlToSourceCodeMap.put(currentUrl, new String[]{currentSourceCodeName, currentPatternName});
                    currentUrl = "";
                    currentPatternName = ""; // 패턴 이름 초기화
                }
                continue;
            }

            if (line.contains("pattern =")) {
                Matcher patternMatcher = patternRegex.matcher(line);
                if (patternMatcher.find()) {
                    String pattern = patternMatcher.group(1).trim();
                    currentPatternName = line.split("=")[0].trim(); // '=' 전까지의 값을 저장
                    if (!currentUrl.isEmpty() && currentUrl.endsWith("/") && pattern.startsWith("/")) {
                        pattern = pattern.substring(1);
                    }
                    currentUrl += pattern;
                }
            }
        }

        // URL, 소스코드 이름, 패턴 이름을 엑셀에 저장
        for (Map.Entry<String, String[]> entry : urlToSourceCodeMap.entrySet()) {
            saveUrl(sheet, rowNum, entry.getValue()[0], entry.getKey(), existingUrls, domainPattern, entry.getValue()[1]);
        }

        reader.close();

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String savePath = outputFilePath + today + "\\";

        File directory = new File(savePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(savePath + "ParsedData.xlsx")) {
            workbook.write(outputStream);
        }
        workbook.close();

        System.out.println("Processing completed.");
    }

    private static String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"URL", "Last Check Date", "New", "Success/Failure", "Response Code/Error",
                "Redirection URL", "Redirection Response/Error", "Source Code Name", "Pattern Name"}; // "Pattern Name" 헤더 추가
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private static void saveUrl(Sheet sheet, AtomicInteger rowNum, String sourceCodeName, String url,
                                Set<String> existingUrls, Pattern domainPattern, String patternName) { // patternName 매개변수 추가
        Matcher domainMatcher = domainPattern.matcher(url);

        if (domainMatcher.find() && existingUrls.add(url)) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            Row row = sheet.createRow(rowNum.getAndIncrement());
            row.createCell(0).setCellValue(url);

            for (int i = 1; i <= 6; i++) {
                row.createCell(i).setCellValue("");
            }

            row.createCell(7).setCellValue(sourceCodeName);
            row.createCell(8).setCellValue(patternName); // Pattern Name 열에 값을 설정
        }
    }
}

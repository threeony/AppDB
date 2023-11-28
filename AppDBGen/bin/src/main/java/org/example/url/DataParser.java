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
    public static void parseAndSaveData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Parsed Data");
        AtomicInteger rowNum = new AtomicInteger(1);

        createHeader(sheet); // 엑셀 파일의 헤더를 생성하는 함수 호출

        // 변수 선언 부분
        String line; // 파일에서 읽은 각 줄을 저장
        String currentSourceCodeName = ""; // 현재 소스 코드 이름
        String currentUrl = ""; // 현재 파싱 중인 URL
        String currentPatternName = ""; // 현재 파싱 중인 패턴 이름
        Set<String> existingUrls = new HashSet<>(); // 이미 처리된 URL을 저장하는 집합
        Map<String, String[]> urlToSourceCodeMap = new HashMap<>(); // URL과 소스 코드 이름을 매핑하는 해시맵

        Pattern patternRegex = Pattern.compile("pattern = (.+)"); // 패턴 매칭을 위한 정규식
        Pattern domainPattern = Pattern.compile("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); // 도메인 매칭을 위한 정규식

        // 파일을 한 줄씩 읽으면서 처리
        while ((line = reader.readLine()) != null) {
            // 'activate lua detector'로 시작하는 줄을 만났을 때 모든 값을 초기화
            if (line.startsWith("activate lua detector. name=")) {
                if (!currentUrl.isEmpty()) {
                    currentUrl = normalizeUrl(currentUrl);
                    urlToSourceCodeMap.put(currentUrl, new String[]{currentSourceCodeName, currentPatternName});
                    currentUrl = "";
                    currentPatternName = "";
                }
                currentSourceCodeName = line.substring(line.indexOf('=') + 1).trim();
                continue;
            }
            // ###으로 시작하는 줄을 만났을 때, 새로운 URL 패턴 처리 할 수 있도록 일부 값 초기화
            if (line.startsWith("###")) {
                if (!currentUrl.isEmpty()) {
                    currentUrl = normalizeUrl(currentUrl);
                    urlToSourceCodeMap.put(currentUrl, new String[]{currentSourceCodeName, currentPatternName});
                    currentUrl = "";
                    currentPatternName = "";
                }
                continue;
            }

            // 'pattern ='을 포함하는 줄을 찾을 경우 패턴 저장하는 로직
            if (line.contains("pattern =")) {
                Matcher patternMatcher = patternRegex.matcher(line);
                if (patternMatcher.find()) {
                    String pattern = patternMatcher.group(1).trim();
                    currentPatternName = line.split("=")[0].trim(); // '=' 전까지의 값을 패턴 이름으로 저장
                    if (!currentUrl.isEmpty() && currentUrl.endsWith("/") && pattern.startsWith("/")) {
                        pattern = pattern.substring(1);
                    }
                    currentUrl += pattern; // URL에 패턴을 추가
                }
            }
        }

        // 추출된 URL과 소스코드 이름을 엑셀에 저장
        for (Map.Entry<String, String[]> entry : urlToSourceCodeMap.entrySet()) {
            saveUrl(sheet, rowNum, entry.getValue()[0], entry.getKey(), existingUrls, domainPattern, entry.getValue()[1]);
        }

        reader.close();

        // 엑셀 파일 저장 경로 설정
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String savePath = "../output/" + today + "/";

        // 저장 경로가 없으면 생성
        File directory = new File(savePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 엑셀 파일을 저장
        try (FileOutputStream outputStream = new FileOutputStream(savePath + "ParsedData.xlsx")) {
            workbook.write(outputStream);
        }
        workbook.close();

        System.out.println("Processing completed.");
    }

    // URL을 정규화하는 함수
    private static String normalizeUrl(String url) {
        // URL이 '/'로 끝나면 그 부분을 제거
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    // 엑셀 파일의 헤더를 생성하는 함수
    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        // 엑셀 헤더 내용
        String[] headers = {"URL", "Last Check Date", "New", "Success/Failure", "Response Code/Error",
                "Redirection URL", "Redirection Response/Error", "Source Code Name", "Pattern Name"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    // URL을 엑셀 파일에 저장하는 함수
    private static void saveUrl(Sheet sheet, AtomicInteger rowNum, String sourceCodeName, String url,
                                Set<String> existingUrls, Pattern domainPattern, String patternName) {
        Matcher domainMatcher = domainPattern.matcher(url);

        // URL이 새로운 경우에만 처리
        if (domainMatcher.find() && existingUrls.add(url)) {
            // URL에 http:// 또는 https://가 없으면 추가
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            // 새로운 행을 생성하고 데이터 입력
            Row row = sheet.createRow(rowNum.getAndIncrement());
            row.createCell(0).setCellValue(url);
            for (int i = 1; i <= 6; i++) {
                row.createCell(i).setCellValue("");
            }
            row.createCell(7).setCellValue(sourceCodeName);
            row.createCell(8).setCellValue(patternName);
        }
    }
}

package org.example;

import org.example.url.DataParser;
import org.example.url.FileComparator;
import org.example.url.file.FileCopyAndDelete;
import org.example.url.file.RecentFolderFinder;
import org.example.url.hash.SHA256Updater;
import org.example.validation.Validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) throws IOException{

        // newFilePath 설정 (실행하는 오늘 날짜 가져옴)
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //help arguments
        if(!(args[0] ==null) && args[0].equals("help")){
            List<String> helpLines = Files.readAllLines(Paths.get("help.txt"));
            for(String helpLine: helpLines){
                System.out.println(helpLine);
            }
        }

        // config.txt에 저장된 옵션값을 가져옵니다.
        OptionParser op = new OptionParser();
        String validationOp =  op.getOption();

        if(op.gui){
            SwingUtilities.invokeLater(() -> {
           mainGui mainGUI = new mainGui();
        });
        }else {
            // 오늘날짜의 경로 지정
            String newFolderPath = op.output + today +"/";

            // 최근 폴더 선언
            String RecentFolder;
            String ExcelFilename = op.ExcelFilename;

            // HASH값 검증
            boolean isSHA256Match = compareFileSHA256(op.input);
            System.out.println("해시 값 검증 완료");
            if (isSHA256Match) {
                System.out.println("SHA256 값이 일치합니다.");
                if (op.hashCheck){
                    System.out.println("hackCheck 값이 "+ op.hashCheck+ "이므로 프로그램을 종료합니다.");
                    System.exit(0);
                } else {
                    System.out.println("hackCheck 값이 "+ op.hashCheck+ "이므로 파싱 및 검증을 진행합니다.");
                }
            } else {
                System.out.println("SHA256 값이 다릅니다. 파싱을 진행합니다.");
            }

            // 파싱 진행
            DataParser.parseAndSaveData(op.input, op.output);
            System.out.println("파싱 완료: " + newFolderPath);

            RecentFolder= RecentFolderFinder.FolderFinder(op.output);
            FileComparator.compareExcelFiles(RecentFolder, newFolderPath);
            System.out.println("new 체크 완료.");

            System.out.println("검증을 시작합니다.");
            // 검증 진행
            Validation validation = new Validation(newFolderPath);
            validation.baseValidation(validationOp);
            //input 파일 이동
            FileCopyAndDelete.change_file_location(op.input, op.output);

            // SHA 파일 업데이트
            SHA256Updater.updateSHA256AndDate(op.input,"Default_Snort_out_SHA256.txt");
         }
    }

    // SHA256 비교 메소드
    private static boolean compareFileSHA256(String uploadedFilePath) {
        String existingSHA256 = readSHA256Info("SHA256");
        String uploadedFileSHA256 = SHA256Updater.updateSHA256AndDate(uploadedFilePath,"skip");
        return existingSHA256 != null && existingSHA256.equals(uploadedFileSHA256) && !(uploadedFileSHA256.equals("error"));
    }
    private static String readSHA256Info(String key) {
        File file = new File("Default_Snort_out_SHA256.txt"); // Assuming the file is in the project root directory
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(key)) {
                    return line.split(":")[1].trim();
                }
            }
        } catch (IOException e) {
             e.printStackTrace();
             return "Error reading file: " + e.getMessage();
        }
        return null;
    }
}
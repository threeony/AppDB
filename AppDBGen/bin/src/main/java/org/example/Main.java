package org.example;

import org.example.url.DataParser;
import org.example.url.FileComparator;
import org.example.validation.Validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) throws IOException{
        SwingUtilities.invokeLater(() -> {
           mainGui mainGUI = new mainGui();
           //mainGUI.setVisible(true);
       });

        // // oldFilePath 설정 (디폴트 MD5에서 값(DATE)을 가져옴)
        // String lastValidationDate = readMD5Info("Date");
        // String oldFilePath = "C:\\Temp\\Snort_Parsing\\" + lastValidationDate + "\\ParsedData.xlsx";
        // // newFilePath 설정 (실행하는 오늘 날짜 가져옴)
        // String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // String newFilePath = "C:\\Temp\\Snort_Parsing\\" + today + "\\ParsedData.xlsx";

        // //help arguments
        // if(args.length == 1 && args[0].equals("-help")){
        //     List<String> helpLines = Files.readAllLines(Paths.get("help.txt"));
        //     for(String helpLine: helpLines){
        //         System.out.println(helpLine);
        //     }
        // }

        // //parse options
        // OptionParser op = new OptionParser();
        // String validationOp =  op.getOption();

        // if(op.gui.equals("true")){
        //     mainGui mg = new mainGui();
        // }
        // else {
        //     //파싱 진행
        //     //1. 해시값 비교
        //     System.out.println("해시값을 비교합니다.\n");

        //     //해시값이 동일할 경우
        //     if (true) { //여기 true 바꿔주면 됨
        //         System.out.println("해시값이 동일합니다.");
        //         if (op.hashCheck.equals("false")) {
        //             System.out.println("ignoreHash가 true로 설정되어 있습니다.");
        //             System.out.println("시스템을 종료합니다.");
        //             System.exit(1);
        //         } else {
        //             System.out.println("ignoreHash가 false로 설정되어 있습니다.");
        //         }
        //     } else {
        //         System.out.println("해시값이 변경되었습니다.");
        //         System.out.println("파싱을 진행합니다.");

        //         DataParser dp = new DataParser();
        //         try {
        //             dp.parseAndSaveData(op.input); // 파싱 수행
        //             System.out.println("파싱 완료.");

        //             // 파싱이 완료된 후 MD5가 다를 경우 New 체크 진행
        //             if (true) { //여기도 바뀐 해시 비교로 ㄲ
        //                 System.out.println("새로운 파일이 생성되었습니다. New 체크를 진행합니다.\n");

        //                 // FileComparator 로직 추가
        //                 FileComparator.compareExcelFiles(oldFilePath, newFilePath);
        //                 System.out.println("파싱 완료: " + newFilePath + "\n");
        //                 System.out.println("New 체크 완료.\n");

        //             }
        //         } catch (IOException e) {
        //             System.out.println("파싱 중 에러 발생: " + e.getMessage() + "\n");
        //         }
        //     }
        //     System.out.println("\n검증을 시작합니다.\n");

        //     String newFolderPath = newFilePath.replace("\\ParsedData.xlsx", "");
        //     //검증
        //     Validation validation = new Validation(newFolderPath);
        //     validation.baseValidation(validationOp);

        //     MD5Updater.updateMD5AndDate(selectedFilePath, "Default_Snort_out_MD5.txt");
        //     System.out.println("최신 검증 날짜와 MD5 값이 갱신되었습니다.\n");
        //     // 최신 MD5 정보와 날짜를 라벨에 반영
        //     String newDate = readMD5Info("Date");
        //     String newMD5 = readMD5Info("MD5");

        //     System.out.println("최근 검증 날짜: " + newDate);
        //     System.out.println("기본 MD5: " + newMD5);
        // }
    }
}
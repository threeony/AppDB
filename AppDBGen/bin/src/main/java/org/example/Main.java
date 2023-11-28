package org.example;

import org.example.url.DataParser;
import org.example.url.FileComparator;
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
        if(args.length == 1 && args[0].equals("-help")){
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
//         }else {
//             // 오늘날짜의 경로 지정
//             String newFolderPath = op.output + today +"/";
//             // HASH값 검증
//             boolean isSHA256Match = compareFileSHA256(op.input);
//             System.out.println("sha 해시 값 실행 완료");
//             if (isSHA256Match) {
//                 System.out.println("SHA256 값이 일치합니다.\n");
//                 if (op.hashCheck){
//                     System.out.println("hackCheck 값이"+ op.hashCheck+ "으로 되어 있어 파싱을 생략 하였습니다.");
//                     // 검증 진행
//                     Validation validation = new Validation(newFolderPath);
//                     validation.baseValidation(validationOp);
//                 } else {
//                     // 파싱 진행
//                     DataParser.parseAndSaveData(op.input, op.output);
//                     System.out.println("파싱 완료!\n");
//                     // 검증 진행
//                     Validation validation = new Validation(newFolderPath);
//                     validation.baseValidation(validationOp);
//                 }
//             } else {
//                 System.out.println("SHA256 값이 다릅니다.\n");
//                 // 파싱 진행
//                 DataParser.parseAndSaveData(op.input, op.output);
//                 System.out.println("파싱 완료!\n");
//                 // 검증 진행
//                 Validation validation = new Validation(newFolderPath);
//                 validation.baseValidation(validationOp);
//             }
//             // SHA 파일 업데이트
//             SHA256Updater.updateSHA256AndDate(op.input,"Default_Snort_out_SHA256.txt");
//         }
//     }

//     // SHA256 비교 메소드
//     private static boolean compareFileSHA256(String uploadedFilePath) {
//         String existingSHA256 = readSHA256Info("SHA256");
//         String uploadedFileSHA256 = SHA256Updater.updateSHA256AndDate(uploadedFilePath,"skip");
//         return existingSHA256 != null && existingSHA256.equals(uploadedFileSHA256) && uploadedFileSHA256 != "error";
//     }
//     private static String readSHA256Info(String key) {
//         File file = new File("Default_Snort_out_SHA256.txt"); // Assuming the file is in the project root directory
//         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//             String line;
//             while ((line = br.readLine()) != null) {
//                 if (line.startsWith(key)) {
//                     return line.split(":")[1].trim();
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//             return "Error reading file: " + e.getMessage();
//         }
//         return null;
//     }
// }
        }
    }
}
package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OptionParser {
    String input;
    String output;
    String hashCheck;
    String opNew;
    String opSuccess;
    String opFail;
    String gui;

    public String getOption() throws IOException {
        List<String> configLines = Files.readAllLines(Paths.get("./config/config.txt"));
        for(String configLine: configLines){
            if(!configLine.startsWith("#")){
                String[] option = configLine.split(":");

                switch (option[0].strip().toLowerCase()){
                    case "input" -> input = option[1].substring(option[1].indexOf("\""), option[1].lastIndexOf("\""));
                    case "output" -> output = option[1].substring(option[1].indexOf("\""), option[1].lastIndexOf("\""));
                    case "ignorehash" -> hashCheck = option[1].strip();
                    case "new" -> opNew = option[1].strip();
                    case "success" -> opSuccess = option[1].strip();
                    case "fail" -> opFail = option[1].strip();
                    case "gui" -> gui = option[1].strip();
                }
            }
        }

        return getValidationOption();
    }

    private String getValidationOption() {
        StringBuilder optionBuilder = new StringBuilder();
        if (opNew.equals("true")) {
            optionBuilder.append("new+");
        }
        if (opSuccess.equals("true")) {
            optionBuilder.append("success+");
        }
        if (opFail.equals("true")) {
            optionBuilder.append("fail+");
        }

        if (optionBuilder.length() > 0) {
            // 마지막 '+' 제거
            optionBuilder.setLength(optionBuilder.length() - 1);
        }

        return optionBuilder.toString();
    }
}

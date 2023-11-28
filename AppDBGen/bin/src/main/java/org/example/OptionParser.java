package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

public class OptionParser {
    String input;
    String output;
    boolean hashCheck;
    boolean opNew;
    boolean opSuccess;
    boolean opFail;
    boolean gui;
    String ExcelFilename;

    public String getOption() throws IOException {
        List<String> configLines = Files.readAllLines(Paths.get("../config/config.txt"));
        for(String configLine: configLines){
            if(!configLine.startsWith("#")){
                String[] option = configLine.split("=");

                switch (option[0].strip().toLowerCase()){
                    case "input":
                        input = option[1].strip();
                        validatePath(input, "Input path is invalid or does not exist: " + input);
                        break;
                    case "output":
                        output = option[1].strip();
                        validatePath(output, "Output path is invalid or does not exist: " + output);
                        break;
                    case "hashcheck":
                        hashCheck = Boolean.parseBoolean(option[1].strip());
                        break;
                    case "new":
                        opNew = Boolean.parseBoolean(option[1].strip());
                        break;
                    case "success":
                        opSuccess = Boolean.parseBoolean(option[1].strip());
                        break;
                    case "fail":
                        opFail = Boolean.parseBoolean(option[1].strip());
                        break;
                    case "gui":
                        gui = Boolean.parseBoolean(option[1].strip());
                        break;
                    case "ExcelFilename":
                        ExcelFilename = option[1].strip();
                }
            }
        }

        return getValidationOption();
    }

    private void validatePath(String pathString, String errorMessage) throws IOException {
        Path path = Paths.get(pathString);
        if (!Files.exists(path)) {
            throw new IOException(errorMessage);
        }
    }

    private String getValidationOption() {
        StringBuilder optionBuilder = new StringBuilder();
        if (opNew) {
            optionBuilder.append("new+");
        }
        if (opSuccess) {
            optionBuilder.append("success+");
        }
        if (opFail) {
            optionBuilder.append("fail+");
        }

        if (optionBuilder.length() > 0) {
            optionBuilder.setLength(optionBuilder.length() - 1);
        }

        return optionBuilder.toString();
    }
}

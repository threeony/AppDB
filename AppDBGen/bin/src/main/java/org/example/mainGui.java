package org.example;

import javax.swing.*;
// import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.*;

public class mainGui {
    
    public mainGui(){
        // Initialize the GUI components
        initComponents();
    }

    private void initComponents() {
        // 기본으로 사용할 폰트 지정
        Font font = new Font("맑은 고딕", Font.BOLD, 12);
        // UIManager를 사용하여 JOptionPane의 폰트를 설정합니다.
        UIManager.put("OptionPane.messageFont", new Font("맑은 고딕", Font.PLAIN, 12));

        // 메인 프레임을 생성합니다.
        JFrame mainFrame = new JFrame("APP DB");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600); // 적절한 크기로 설정합니다.
        mainFrame.setTitle("Snort OpenAppID 파싱 및 검증 프로그램");      // 프로그램 타이틀 지정

        // 아이콘 추가
        ImageIcon image = new ImageIcon("icon/AppDB.png");
        mainFrame.setIconImage(image.getImage());

        // 상단 패널을 생성합니다.
        JPanel topPanel = new JPanel(new BorderLayout());

        // 제목 및 버전 정보를 포함하는 라벨을 생성합니다.
        JLabel titleLabel = new JLabel("<html>Title: APP DB<br/>버전 날짜: YYYYMMDD<br/>SHA-256:</html>", SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // 로그 출력을 위한 텍스트 영역을 생성합니다.
        JTextArea logTextArea = new JTextArea();
        logTextArea.setBorder(BorderFactory.createTitledBorder("LOG 출력"));
        logTextArea.setEditable(false);
        logTextArea.setFont(font);

        // 버튼 패널을 생성합니다.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5)); // 패널 여백 설정

        // 버튼들의 선호하는 크기와 최대 크기를 설정합니다.
        Dimension buttonSize = new Dimension(120, 25);

        // 버튼들을 생성하고 패널에 추가합니다.
        JButton helpButton = new JButton("도움말");
        JButton settingsButton = new JButton("설정");
        JButton saveButton = new JButton("설정 저장");
        JButton undoButton = new JButton("설정 취소");
        JButton executeButton = new JButton("실행");
        JButton resultButton = new JButton("결과 확인");
        JButton exitButton = new JButton("종료");

        // 모든 버튼을 리스트에 추가합니다.
        java.util.List<JButton> allButtons = new java.util.ArrayList<>(java.util.Arrays.asList(
            helpButton, settingsButton, executeButton, resultButton, saveButton, undoButton
        ));

        // "설정" 버튼과 연관된 버튼만을 리스트에 추가합니다.
        java.util.List<JButton> settingsButtons = new java.util.ArrayList<>(java.util.Arrays.asList(saveButton, undoButton));

        // 버튼들 사이의 일정한 간격을 위한 공백의 크기를 설정합니다.
        int verticalStrutHeight = 10;

        // 버튼들을 패널에 추가합니다.
        for (JButton button : allButtons) {
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setBackground(Color.white);
            button.setFont(font);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(button);
            buttonPanel.add(Box.createVerticalStrut(verticalStrutHeight));
        }

        // "설정 저장"과 "설정 취소" 버튼을 기본적으로 숨깁니다.
        saveButton.setVisible(false);
        undoButton.setVisible(false);

        // 버튼 패널에 가변 공간을 추가하여 exitButton을 맨 아래로 내립니다.
        buttonPanel.add(Box.createVerticalGlue());
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        exitButton.setBackground(Color.white);
        exitButton.setFont(font);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(exitButton);

        // "설정" 버튼의 이벤트 리스너를 추가합니다.
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.setText(""); // JTextArea를 클리어합니다.
                logTextArea.setEditable(true); // JTextArea를 편집 가능하게 설정합니다.
                // 모든 버튼을 숨깁니다.
                for (JButton button : allButtons) {
                    button.setVisible(false);
                }
                // "설정 저장"과 "설정 취소" 버튼만 보이게 합니다.
                for (JButton button : settingsButtons) {
                    button.setVisible(true);
                }
                buttonPanel.revalidate();
                buttonPanel.repaint();
                // config파일 읽어오기
                try {
                    File file = new File("../config/config.txt");
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), "UTF-8")
                    );
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logTextArea.append(line + "\n");
                    }
                    reader.close();
                } catch (IOException ex) {
                    logTextArea.setText("Error reading config.txt: " + ex.getMessage());
                }
            }
        });

        // "설정 저장" 버튼의 이벤트 리스너를 추가합니다.
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 모든 버튼을 다시 보이게 합니다.
                for (JButton button : allButtons) {
                    button.setVisible(true);
                }
                // "설정 저장"과 "설정 취소" 버튼을 숨깁니다.
                for (JButton button : settingsButtons) {
                    button.setVisible(false);
                }
                buttonPanel.revalidate();
                buttonPanel.repaint();

                try {
                    File file = new File("../config/config.txt");
                    BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file), "UTF-8")
                    );
                    writer.write(logTextArea.getText());
                    writer.close();
                    JOptionPane.showMessageDialog(mainFrame, "설정이 저장되었습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
                
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "설정 저장 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
                logTextArea.setText(""); // JTextArea를 클리어합니다.
                logTextArea.setEditable(false);     // JTextArea를 읽기 전용으로 설정합니다.
            }
        });

        // "설정 취소" 버튼의 이벤트 리스너를 추가합니다.
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.setText(""); // JTextArea를 클리어합니다.
                logTextArea.setEditable(false);     // JTextArea를 읽기 전용으로 설정합니다.
                // 모든 버튼을 다시 보이게 합니다.
                for (JButton button : allButtons) {
                    button.setVisible(true);
                }
                // "설정 저장"과 "설정 취소" 버튼을 숨깁니다.
                for (JButton button : settingsButtons) {
                    button.setVisible(false);
                }
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });


        // "도움말" 버튼의 이벤트 리스너를 추가합니다.
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.setText(""); // JTextArea를 클리어합니다.
                logTextArea.setEditable(false); // JTextArea를 읽기 전용으로 설정합니다.
                try {
                    File file = new File("help.txt");
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), "UTF-8")
                    );
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logTextArea.append(line + "\n");
                    }
                    reader.close();
                } catch (IOException ex) {
                    logTextArea.setText("Error reading help.txt: " + ex.getMessage());
                }
            }
        });


        // "종료" 버튼의 이벤트 리스너를 추가합니다.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose(); // GUI를 종료합니다.
            }
        });


        // 컨텐츠 패널을 생성하고 구성 요소들을 배치합니다.
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5)); // 여백 추가
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // 왼쪽, 하단, 오른쪽 여백 설정

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        contentPanel.add(new JScrollPane(logTextArea), BorderLayout.CENTER);

        // 메인 프레임에 컨텐츠 패널을 추가합니다.
        mainFrame.add(contentPanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}

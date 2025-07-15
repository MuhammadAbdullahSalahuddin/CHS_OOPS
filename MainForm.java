
import Stegno.*;
import javax.swing.*;
import java.awt.*;

import java.io.File;


public class MainForm {
    private final Main main;
    private final JFrame frame;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    public MainForm(Main main) {
        this.main = main;
        this.frame = new JFrame("Crypto, Hash, and Stegno Tool");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600, 400);
        this.frame.setLocationRelativeTo(null); // Center the window

        // Main panel with CardLayout to switch between options
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        // Create main menu panel
        JPanel menuPanel = createMainMenuPanel();
        mainPanel.add(menuPanel, "Menu");

        // Create panels for each option
        mainPanel.add(createCryptoPanel(), "Crypto");
        mainPanel.add(createHashPanel(), "Hash");
        mainPanel.add(createStegnoPanel(), "Stegno");

        frame.add(mainPanel);
    }

    // Display the GUI
    public void display() {
        frame.setVisible(true);
    }

    // Main menu panel with Crypto, Hash, Stegno buttons
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton cryptoButton = new JButton("Cryptography");
        cryptoButton.addActionListener(e -> cardLayout.show(mainPanel, "Crypto"));
        JButton hashButton = new JButton("Hashing");
        hashButton.addActionListener(e -> cardLayout.show(mainPanel, "Hash"));
        JButton stegnoButton = new JButton("Steganography");
        stegnoButton.addActionListener(e -> cardLayout.show(mainPanel, "Stegno"));

        panel.add(cryptoButton);
        panel.add(hashButton);
        panel.add(stegnoButton);
        return panel;
    }

    // Crypto panel with Caesar and XOR options
    private JPanel createCryptoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cipher selection
        JPanel cipherPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton caesarButton = new JButton("Caesar Cipher");
        JButton xorButton = new JButton("XOR Cipher");
        cipherPanel.add(caesarButton);
        cipherPanel.add(xorButton);

        // Sub-panel for crypto operations (CardLayout)
        JPanel cryptoSubPanel = new JPanel(new CardLayout());
        cryptoSubPanel.add(createCaesarCryptoPanel(), "Caesar");
        cryptoSubPanel.add(createXORCryptoPanel(), "XOR");

        caesarButton.addActionListener(e -> ((CardLayout) cryptoSubPanel.getLayout()).show(cryptoSubPanel, "Caesar"));
        xorButton.addActionListener(e -> ((CardLayout) cryptoSubPanel.getLayout()).show(cryptoSubPanel, "XOR"));

        // Back button
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        panel.add(cipherPanel, BorderLayout.NORTH);
        panel.add(cryptoSubPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    // Caesar Cipher panel
    private JPanel createCaesarCryptoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JRadioButton textRadio = new JRadioButton("Text", true);
        JRadioButton fileRadio = new JRadioButton("File");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadio);
        typeGroup.add(fileRadio);
        optionsPanel.add(textRadio);
        optionsPanel.add(fileRadio);

        JPanel cryptoPanel = new JPanel(new CardLayout());
        JPanel textPanel = createCaesarTextPanel();
        JPanel filePanel = createCaesarFilePanel();
        cryptoPanel.add(textPanel, "Text");
        cryptoPanel.add(filePanel, "File");

        textRadio.addActionListener(e -> ((CardLayout) cryptoPanel.getLayout()).show(cryptoPanel, "Text"));
        fileRadio.addActionListener(e -> ((CardLayout) cryptoPanel.getLayout()).show(cryptoPanel, "File"));

        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(cryptoPanel, BorderLayout.CENTER);
        return panel;
    }

    // Caesar Text Encryption/Decryption
    private JPanel createCaesarTextPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel keyLabel = new JLabel("Key (Integer):");
        JTextField keyField = new JTextField(10);
        JLabel inputLabel = new JLabel("Input Text:");
        JTextArea inputText = new JTextArea(5, 20);
        JLabel outputLabel = new JLabel("Output Text:");
        JTextArea outputText = new JTextArea(5, 20);
        outputText.setEditable(false);
        JRadioButton encryptRadio = new JRadioButton("Encrypt", true);
        JRadioButton decryptRadio = new JRadioButton("Decrypt");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encryptRadio);
        operationGroup.add(decryptRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(keyLabel, gbc);
        gbc.gridx = 1; panel.add(keyField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(inputText), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(outputText), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(encryptRadio, gbc);
        gbc.gridx = 1; panel.add(decryptRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(processButton, gbc);

        processButton.addActionListener(e -> {
            try {
                int key = Integer.parseInt(keyField.getText().trim());
                String input = inputText.getText();
                String result = encryptRadio.isSelected() ?
                        main.encryptTextCeaser(input, key) :
                        main.decryptTextCeaser(input, key);
                outputText.setText(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Key must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // Caesar File Encryption/Decryption
    private JPanel createCaesarFilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel keyLabel = new JLabel("Key (Integer):");
        JTextField keyField = new JTextField(10);
        JLabel inputLabel = new JLabel("Input File:");
        JTextField inputField = new JTextField(20);
        JButton inputBrowse = new JButton("Browse");
        JLabel outputLabel = new JLabel("Output File:");
        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");
        JRadioButton encryptRadio = new JRadioButton("Encrypt", true);
        JRadioButton decryptRadio = new JRadioButton("Decrypt");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encryptRadio);
        operationGroup.add(decryptRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(keyLabel, gbc);
        gbc.gridx = 1; panel.add(keyField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 2; panel.add(inputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(outputField, gbc);
        gbc.gridx = 2; panel.add(outputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(encryptRadio, gbc);
        gbc.gridx = 1; panel.add(decryptRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; panel.add(processButton, gbc);

        inputBrowse.addActionListener(e -> selectFile(inputField));
        outputBrowse.addActionListener(e -> selectFile(outputField));
        processButton.addActionListener(e -> {
            try {
                int key = Integer.parseInt(keyField.getText().trim());
                String inputPath = inputField.getText();
                String outputPath = outputField.getText();
                String result = encryptRadio.isSelected() ?
                        main.encryptFileCeaser(inputPath, outputPath, key) :
                        main.decryptFileCeaser(inputPath, outputPath, key);
                JOptionPane.showMessageDialog(frame, result, "Result", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Key must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // XOR Cipher panel
    private JPanel createXORCryptoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JRadioButton textRadio = new JRadioButton("Text", true);
        JRadioButton fileRadio = new JRadioButton("File");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadio);
        typeGroup.add(fileRadio);
        optionsPanel.add(textRadio);
        optionsPanel.add(fileRadio);

        JPanel cryptoPanel = new JPanel(new CardLayout());
        JPanel textPanel = createXORTextPanel();
        JPanel filePanel = createXORFilePanel();
        cryptoPanel.add(textPanel, "Text");
        cryptoPanel.add(filePanel, "File");

        textRadio.addActionListener(e -> ((CardLayout) cryptoPanel.getLayout()).show(cryptoPanel, "Text"));
        fileRadio.addActionListener(e -> ((CardLayout) cryptoPanel.getLayout()).show(cryptoPanel, "File"));

        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(cryptoPanel, BorderLayout.CENTER);
        return panel;
    }

    // XOR Text Encryption/Decryption
    private JPanel createXORTextPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel keyLabel = new JLabel("Key (String):");
        JTextField keyField = new JTextField(10);
        JLabel inputLabel = new JLabel("Input Text:");
        JTextArea inputText = new JTextArea(5, 20);
        JLabel outputLabel = new JLabel("Output Text:");
        JTextArea outputText = new JTextArea(5, 20);
        outputText.setEditable(false);
        JRadioButton encryptRadio = new JRadioButton("Encrypt", true);
        JRadioButton decryptRadio = new JRadioButton("Decrypt");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encryptRadio);
        operationGroup.add(decryptRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(keyLabel, gbc);
        gbc.gridx = 1; panel.add(keyField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(inputText), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(outputText), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(encryptRadio, gbc);
        gbc.gridx = 1; panel.add(decryptRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(processButton, gbc);

        processButton.addActionListener(e -> {
            String key = keyField.getText().trim();
            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String input = inputText.getText();
            String result = encryptRadio.isSelected() ?
                    main.encryptTextXOR(input, key) :
                    main.decryptTextXOR(input, key);
            outputText.setText(result);
        });

        return panel;
    }

    // XOR File Encryption/Decryption
    private JPanel createXORFilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel keyLabel = new JLabel("Key (String):");
        JTextField keyField = new JTextField(10);
        JLabel inputLabel = new JLabel("Input File:");
        JTextField inputField = new JTextField(20);
        JButton inputBrowse = new JButton("Browse");
        JLabel outputLabel = new JLabel("Output File:");
        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");
        JRadioButton encryptRadio = new JRadioButton("Encrypt", true);
        JRadioButton decryptRadio = new JRadioButton("Decrypt");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encryptRadio);
        operationGroup.add(decryptRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(keyLabel, gbc);
        gbc.gridx = 1; panel.add(keyField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 2; panel.add(inputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(outputField, gbc);
        gbc.gridx = 2; panel.add(outputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(encryptRadio, gbc);
        gbc.gridx = 1; panel.add(decryptRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; panel.add(processButton, gbc);

        inputBrowse.addActionListener(e -> selectFile(inputField));
        outputBrowse.addActionListener(e -> selectFile(outputField));
        processButton.addActionListener(e -> {
            String key = keyField.getText().trim();
            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String inputPath = inputField.getText();
            String outputPath = outputField.getText();
            String result = encryptRadio.isSelected() ?
                    main.encryptFileXOR(inputPath, outputPath, key) :
                    main.decryptFileXOR(inputPath, outputPath, key);
            JOptionPane.showMessageDialog(frame, result, "Result", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    // Hash panel
    private JPanel createHashPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel algoLabel = new JLabel("Select Algorithm:");
        JRadioButton md5Radio = new JRadioButton("MD5", true);
        JRadioButton sha1Radio = new JRadioButton("SHA1");
        JRadioButton sha256Radio = new JRadioButton("SHA256");
        ButtonGroup algoGroup = new ButtonGroup();
        algoGroup.add(md5Radio);
        algoGroup.add(sha1Radio);
        algoGroup.add(sha256Radio);
        JLabel inputLabel = new JLabel("Input Text:");
        JTextArea inputText = new JTextArea(5, 20);
        JLabel outputLabel = new JLabel("Hash Output:");
        JTextArea outputText = new JTextArea(10, 40);
        outputText.setEditable(false);
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        JButton processButton = new JButton("Compute Hash");
        JButton backButton = new JButton("Back to Menu");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(algoLabel, gbc);
        gbc.gridx = 1; panel.add(md5Radio, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(sha1Radio, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(sha256Radio, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(inputText), gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(outputText), gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; panel.add(processButton, gbc);
        gbc.gridy = 6; panel.add(backButton, gbc);

        processButton.addActionListener(e -> {
            String algorithm = md5Radio.isSelected() ? "Hashing.MD5" :
                    sha1Radio.isSelected() ? "Hashing.SHA1" : "Hashing.SHA256";
            String input = inputText.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Input cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String result = main.computeHash(algorithm, input);
            outputText.setText(result);
        });
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;
    }
    // Steganography panel
    private JPanel createStegnoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel mediaPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton imageButton = new JButton("Image");
        JButton audioButton = new JButton("Audio");
        mediaPanel.add(imageButton);
        mediaPanel.add(audioButton);

        JPanel stegnoSubPanel = new JPanel(new CardLayout());
        stegnoSubPanel.add(createImageStegnoPanel(), "Image");
        stegnoSubPanel.add(createAudioStegnoPanel(), "Audio");

        imageButton.addActionListener(e -> ((CardLayout) stegnoSubPanel.getLayout()).show(stegnoSubPanel, "Image"));
        audioButton.addActionListener(e -> ((CardLayout) stegnoSubPanel.getLayout()).show(stegnoSubPanel, "Audio"));

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        panel.add(mediaPanel, BorderLayout.NORTH);
        panel.add(stegnoSubPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    // Image Steganography panel
    private JPanel createImageStegnoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JRadioButton textRadio = new JRadioButton("Text", true);
        JRadioButton fileRadio = new JRadioButton("File");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadio);
        typeGroup.add(fileRadio);
        optionsPanel.add(textRadio);
        optionsPanel.add(fileRadio);

        JPanel stegnoPanel = new JPanel(new CardLayout());
        JPanel textPanel = createImageTextStegnoPanel();
        JPanel filePanel = createImageFileStegnoPanel();
        stegnoPanel.add(textPanel, "Text");
        stegnoPanel.add(filePanel, "File");

        textRadio.addActionListener(e -> ((CardLayout) stegnoPanel.getLayout()).show(stegnoPanel, "Text"));
        fileRadio.addActionListener(e -> ((CardLayout) stegnoPanel.getLayout()).show(stegnoPanel, "File"));

        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(stegnoPanel, BorderLayout.CENTER);
        return panel;
    }

    // Image Steganography Text
    private JPanel createImageTextStegnoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("Input Image:");
        JTextField inputField = new JTextField(20);
        JButton inputBrowse = new JButton("Browse");
        JLabel outputLabel = new JLabel("Output Image:");
        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");
        JLabel textLabel = new JLabel("Text to Encode:");
        JTextArea textArea = new JTextArea(5, 20);
        JRadioButton encodeRadio = new JRadioButton("Encode", true);
        JRadioButton decodeRadio = new JRadioButton("Decode");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encodeRadio);
        operationGroup.add(decodeRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 2; panel.add(inputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(outputField, gbc);
        gbc.gridx = 2; panel.add(outputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(textLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(textArea), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(encodeRadio, gbc);
        gbc.gridx = 1; panel.add(decodeRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; panel.add(processButton, gbc);

        inputBrowse.addActionListener(e -> selectFile(inputField, "*.png"));
        outputBrowse.addActionListener(e -> selectFile(outputField, "*.png"));
        processButton.addActionListener(e -> {
            String inputPath = inputField.getText();
            String outputPath = outputField.getText();
            if (encodeRadio.isSelected()) {
                String text = textArea.getText();
                String result = main.encodeStegno("image", inputPath, text, outputPath);
                JOptionPane.showMessageDialog(frame, result, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Object result = main.decodeStegno("image", inputPath, null);
                if (result instanceof Message) {
                    textArea.setText(result.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, result.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    // Image Steganography File
    private JPanel createImageFileStegnoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("Input Image:");
        JTextField inputField = new JTextField(20);
        JButton inputBrowse = new JButton("Browse");
        JLabel outputLabel = new JLabel("Output Image:");
        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");
        JLabel fileLabel = new JLabel("File to Encode:");
        JTextField fileField = new JTextField(20);
        JButton fileBrowse = new JButton("Browse");
        JLabel decodeLabel = new JLabel("Decode Output File:");
        JTextField decodeField = new JTextField(20);
        JButton decodeBrowse = new JButton("Browse");
        JRadioButton encodeRadio = new JRadioButton("Encode", true);
        JRadioButton decodeRadio = new JRadioButton("Decode");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encodeRadio);
        operationGroup.add(decodeRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 2; panel.add(inputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(outputField, gbc);
        gbc.gridx = 2; panel.add(outputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(fileLabel, gbc);
        gbc.gridx = 1; panel.add(fileField, gbc);
        gbc.gridx = 2; panel.add(fileBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(decodeLabel, gbc);
        gbc.gridx = 1; panel.add(decodeField, gbc);
        gbc.gridx = 2; panel.add(decodeBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(encodeRadio, gbc);
        gbc.gridx = 1; panel.add(decodeRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3; panel.add(processButton, gbc);

        inputBrowse.addActionListener(e -> selectFile(inputField, "*.png"));
        outputBrowse.addActionListener(e -> selectFile(outputField, "*.png"));
        fileBrowse.addActionListener(e -> selectFile(fileField));
        decodeBrowse.addActionListener(e -> selectFile(decodeField));
        processButton.addActionListener(e -> {
            String inputPath = inputField.getText();
            String outputPath = outputField.getText();
            if (encodeRadio.isSelected()) {
                File file = new File(fileField.getText());
                String result = main.encodeStegno("image", inputPath, file, outputPath);
                JOptionPane.showMessageDialog(frame, result, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Object result = main.decodeStegno("image", inputPath, decodeField.getText());
                if (result instanceof File) {
                    JOptionPane.showMessageDialog(frame, "File decoded to: " + decodeField.getText(), "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, result.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    // Audio Steganography panel
    private JPanel createAudioStegnoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JRadioButton textRadio = new JRadioButton("Text", true);
        JRadioButton fileRadio = new JRadioButton("File");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadio);
        typeGroup.add(fileRadio);
        optionsPanel.add(textRadio);
        optionsPanel.add(fileRadio);

        JPanel stegnoPanel = new JPanel(new CardLayout());
        JPanel textPanel = createAudioTextStegnoPanel();
        JPanel filePanel = createAudioFileStegnoPanel();
        stegnoPanel.add(textPanel, "Text");
        stegnoPanel.add(filePanel, "File");

        textRadio.addActionListener(e -> ((CardLayout) stegnoPanel.getLayout()).show(stegnoPanel, "Text"));
        fileRadio.addActionListener(e -> ((CardLayout) stegnoPanel.getLayout()).show(stegnoPanel, "File"));

        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(stegnoPanel, BorderLayout.CENTER);
        return panel;
    }

    // Audio Steganography Text
    private JPanel createAudioTextStegnoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("Input Audio:");
        JTextField inputField = new JTextField(20);
        JButton inputBrowse = new JButton("Browse");
        JLabel outputLabel = new JLabel("Output Audio:");
        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");
        JLabel textLabel = new JLabel("Text to Encode:");
        JTextArea textArea = new JTextArea(5, 20);
        JRadioButton encodeRadio = new JRadioButton("Encode", true);
        JRadioButton decodeRadio = new JRadioButton("Decode");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encodeRadio);
        operationGroup.add(decodeRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 2; panel.add(inputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(outputField, gbc);
        gbc.gridx = 2; panel.add(outputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(textLabel, gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(textArea), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(encodeRadio, gbc);
        gbc.gridx = 1; panel.add(decodeRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; panel.add(processButton, gbc);

        inputBrowse.addActionListener(e -> selectFile(inputField, "*.wav"));
        outputBrowse.addActionListener(e -> selectFile(outputField, "*.wav"));
        processButton.addActionListener(e -> {
            String inputPath = inputField.getText();
            String outputPath = outputField.getText();
            if (encodeRadio.isSelected()) {
                String text = textArea.getText();
                String result = main.encodeStegno("audio", inputPath, text, outputPath);
                JOptionPane.showMessageDialog(frame, result, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Object result = main.decodeStegno("audio", inputPath, null);
                if (result instanceof Message) {
                    textArea.setText(result.toString());
                } else {
                    JOptionPane.showMessageDialog(frame, result.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    // Audio Steganography File
    private JPanel createAudioFileStegnoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("Input Audio:");
        JTextField inputField = new JTextField(20);
        JButton inputBrowse = new JButton("Browse");
        JLabel outputLabel = new JLabel("Output Audio:");
        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");
        JLabel fileLabel = new JLabel("File to Encode:");
        JTextField fileField = new JTextField(20);
        JButton fileBrowse = new JButton("Browse");
        JLabel decodeLabel = new JLabel("Decode Output File:");
        JTextField decodeField = new JTextField(20);
        JButton decodeBrowse = new JButton("Browse");
        JRadioButton encodeRadio = new JRadioButton("Encode", true);
        JRadioButton decodeRadio = new JRadioButton("Decode");
        ButtonGroup operationGroup = new ButtonGroup();
        operationGroup.add(encodeRadio);
        operationGroup.add(decodeRadio);
        JButton processButton = new JButton("Process");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(inputLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 2; panel.add(inputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(outputLabel, gbc);
        gbc.gridx = 1; panel.add(outputField, gbc);
        gbc.gridx = 2; panel.add(outputBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(fileLabel, gbc);
        gbc.gridx = 1; panel.add(fileField, gbc);
        gbc.gridx = 2; panel.add(fileBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(decodeLabel, gbc);
        gbc.gridx = 1; panel.add(decodeField, gbc);
        gbc.gridx = 2; panel.add(decodeBrowse, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(encodeRadio, gbc);
        gbc.gridx = 1; panel.add(decodeRadio, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3; panel.add(processButton, gbc);

        inputBrowse.addActionListener(e -> selectFile(inputField, "*.wav"));
        outputBrowse.addActionListener(e -> selectFile(outputField, "*.wav"));
        fileBrowse.addActionListener(e -> selectFile(fileField));
        decodeBrowse.addActionListener(e -> selectFile(decodeField));
        processButton.addActionListener(e -> {
            String inputPath = inputField.getText();
            String outputPath = outputField.getText();
            if (encodeRadio.isSelected()) {
                File file = new File(fileField.getText());
                String result = main.encodeStegno("audio", inputPath, file, outputPath);
                JOptionPane.showMessageDialog(frame, result, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Object result = main.decodeStegno("audio", inputPath, decodeField.getText());
                if (result instanceof File) {
                    JOptionPane.showMessageDialog(frame, "File decoded to: " + decodeField.getText(), "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, result.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    // Helper method to select files
    private void selectFile(JTextField field, String... extensions) {
        JFileChooser fileChooser = new JFileChooser();
        if (extensions.length > 0) {
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Files (" + String.join(", ", extensions), extensions));
        }
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
}
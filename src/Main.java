
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Main {

    private static List<JCheckBox> checkBoxesUsername = new ArrayList<>();
    private static List<String> sentencesUsername = new ArrayList<>();
    private static List<JCheckBox> checkBoxesAvatar = new ArrayList<>();
    private static List<String> sentencesAvatar = new ArrayList<>();
    private static List<JCheckBox> checkBoxesDescription = new ArrayList<>();
    private static List<String> sentencesDescription = new ArrayList<>();
    private static List<JCheckBox> checkBoxesMiscellaneous = new ArrayList<>();
    private static List<String> sentencesMiscellaneous = new ArrayList<>();

    private static JTextArea resultArea = new JTextArea(5, 50); // Set initial height to 5 rows
    private static List<Erper> erpers = new ArrayList<>();
    private static int currentIndex = 0; // Tracks the current Erper being displayed

    public static void main(String[] args) {
        // Sample Erper objects
        erpers.add(new Erper("https://roblox.com/user1"));
        erpers.add(new Erper("https://roblox.com/user2"));
        erpers.add(new Erper("https://roblox.com/user3"));

        JFrame frame = new JFrame("Erper Reporting Tool");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create panels for checkboxes with updated titles and height
        // Create a JPanel to add to the JScrollPane
        // Adjusted height and layout for a more compact view

// Username panel
        JPanel panelUsernameContent = new JPanel();
        panelUsernameContent.setLayout(new BoxLayout(panelUsernameContent, BoxLayout.Y_AXIS)); // Set the layout of the panel
        JScrollPane panelUsername = new JScrollPane(panelUsernameContent);
        panelUsername.setBounds(50, 50, 250, 400); // Reduced height to 400px
        panelUsername.setBorder(BorderFactory.createTitledBorder("Username"));

// Avatar panel
        JPanel panelAvatarContent = new JPanel();
        panelAvatarContent.setLayout(new BoxLayout(panelAvatarContent, BoxLayout.Y_AXIS));
        JScrollPane panelAvatar = new JScrollPane(panelAvatarContent);
        panelAvatar.setBounds(320, 50, 250, 400); // Reduced height to 400px
        panelAvatar.setBorder(BorderFactory.createTitledBorder("Avatar"));

// Description panel
        JPanel panelDescriptionContent = new JPanel();
        panelDescriptionContent.setLayout(new BoxLayout(panelDescriptionContent, BoxLayout.Y_AXIS));
        JScrollPane panelDescription = new JScrollPane(panelDescriptionContent);
        panelDescription.setBounds(590, 50, 250, 400); // Reduced height to 400px
        panelDescription.setBorder(BorderFactory.createTitledBorder("Description"));

// Miscellaneous panel
        JPanel panelMiscellaneousContent = new JPanel();
        panelMiscellaneousContent.setLayout(new BoxLayout(panelMiscellaneousContent, BoxLayout.Y_AXIS));
        JScrollPane panelMiscellaneous = new JScrollPane(panelMiscellaneousContent);
        panelMiscellaneous.setBounds(860, 50, 250, 400); // Reduced height to 400px
        panelMiscellaneous.setBorder(BorderFactory.createTitledBorder("Miscellaneous"));



        // Adjust the result area to have more height and proper bounds
        resultArea.setBounds(50, 770, 1000, 50); // Adjusted height and position for single line display
        resultArea.setEditable(true); // Allow user editing
        resultArea.setLineWrap(true); // Disable line wrapping
        resultArea.setWrapStyleWord(true); // Disable wrapping by word

        // Add panels and result area to the frame
        frame.add(panelUsername);
        frame.add(panelAvatar);
        frame.add(panelDescription);
        frame.add(panelMiscellaneous);
        frame.add(resultArea);

        // Read CSVs and dynamically create checkboxes for each checklist
        readCSVAndCreateCheckboxes(panelUsernameContent, checkBoxesUsername, sentencesUsername, "username.csv");
        readCSVAndCreateCheckboxes(panelAvatarContent, checkBoxesAvatar, sentencesAvatar, "avatar.csv");
        readCSVAndCreateCheckboxes(panelDescriptionContent, checkBoxesDescription, sentencesDescription, "description.csv");
        readCSVAndCreateCheckboxes(panelMiscellaneousContent, checkBoxesMiscellaneous, sentencesMiscellaneous, "miscellaneous.csv");

        // Button to move to the next Erper
        // Adjust the result area position and size (above buttons)
        resultArea.setBounds(50, 470, 1000, 50); // Position moved up and width adjusted

// Adjust button positions to match new layout
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(400, 540, 100, 50); // Moved up to match the new compact layout
        frame.add(nextButton);

        JButton openBrowserButton = new JButton("Open URL");
        openBrowserButton.setBounds(200, 540, 150, 50); // Moved up
        frame.add(openBrowserButton);

        JButton importButton = new JButton("Import");
        importButton.setBounds(50, 540, 100, 50); // Moved up
        frame.add(importButton);

        JButton exportButton = new JButton("Export");
        exportButton.setBounds(550, 540, 100, 50); // Moved up
        frame.add(exportButton);

        // Update the UI with the first Erper
        updateUI();

        // ActionListener for the "Next" button
        nextButton.addActionListener(e -> {
            // Update the resultArea based on checkbox selections
            updateResultAreaWithCheckboxes(); // Populate resultArea first

            // Get the current user input before switching to the next Erper
            assignReportReasonToCurrentErper(); // Assign the current reason to the Erper

            if (currentIndex < erpers.size() - 1) {
                currentIndex++;
                updateUI(); // Update the UI to show the next Erper
            } else {
                JOptionPane.showMessageDialog(frame, "No more accounts to process.");
            }
        });

        // ActionListener for the "Open URL" button
        openBrowserButton.addActionListener(e -> conversions.openWebPage(erpers.get(currentIndex).getURL()));

        // ActionListener for the "Import" button
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                erpers = conversions.parseRobloxUrlsWithReasons(selectedFile.getAbsolutePath());
                currentIndex = 0; // Reset to the first account
                updateUI();
            }
        });

        // ActionListener for the "Export" button
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Export only the reasons for each Erper, including user input
                conversions.exportRobloxUrlsWithReasons(erpers, selectedFile.getAbsolutePath(), true);
            }
        });

        // Adding item listeners to update resultArea dynamically
        addCheckboxListeners(checkBoxesUsername, sentencesUsername);
        addCheckboxListeners(checkBoxesAvatar, sentencesAvatar);
        addCheckboxListeners(checkBoxesDescription, sentencesDescription);
        addCheckboxListeners(checkBoxesMiscellaneous, sentencesMiscellaneous);

        frame.setVisible(true);
    }

    // Helper method to add item listeners to checkboxes
    private static void addCheckboxListeners(List<JCheckBox> checkBoxes, List<String> sentences) {
        for (int i = 0; i < checkBoxes.size(); i++) {
            int index = i; // To use in the lambda
            checkBoxes.get(i).addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                    updateResultAreaWithCheckboxes(); // Update the text area on checkbox state change
                }
            });
        }
    }

    // New method to populate the resultArea based on the selected checkboxes
    private static void updateResultAreaWithCheckboxes() {
        StringBuilder reason = new StringBuilder();
        StringBuilder userReasons = new StringBuilder();
        StringBuilder avatarReasons = new StringBuilder();
        StringBuilder descriptionReasons = new StringBuilder();
        StringBuilder miscellaneousReasons = new StringBuilder();

        // Collect selected reasons from checkboxes
        appendSelectedReasons(userReasons, checkBoxesUsername, sentencesUsername);
        appendSelectedReasons(avatarReasons, checkBoxesAvatar, sentencesAvatar);
        appendSelectedReasons(descriptionReasons, checkBoxesDescription, sentencesDescription);
        appendSelectedReasons(miscellaneousReasons, checkBoxesMiscellaneous, sentencesMiscellaneous);

        // Construct the final string for the resultArea
        reason.append("ERP ACCOUNT: ");
        if (userReasons.length() > 0) {

            reason.append("In Username: ").append(userReasons.toString().trim()).append(". ");
        }
        if (avatarReasons.length() > 0) {
            reason.append("In Avatar: ").append(avatarReasons.toString().trim()).append(". ");
        }
        if (descriptionReasons.length() > 0) {
            reason.append("In Description: ").append(descriptionReasons.toString().trim()).append(". ");
        }
        if (miscellaneousReasons.length() > 0) {
            // Replace ', ' with '. '
            String formattedMiscellaneous = miscellaneousReasons.toString().replace(", ", ". ");
            reason.append(formattedMiscellaneous.trim()).append(".");
        }

        // Set the constructed reason in the text area
        resultArea.setText(reason.toString());
    }

    // Modified method to assign the user input to the current Erper
    private static void assignReportReasonToCurrentErper() {
        // Get the text from the result area and set it as the reason for the current Erper
        String userInputReason = resultArea.getText().trim();
        erpers.get(currentIndex).setReason(userInputReason); // Set the text area content as the reason for the current Erper
    }

    // Read CSV and create checkboxes
    private static void readCSVAndCreateCheckboxes(JPanel panel, List<JCheckBox> checkBoxes, List<String> sentences, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split by comma and trim whitespace
                String[] parts = line.split(",", 2); // Split into two parts: Title and Reason
                String title = parts[0].trim().replaceAll("^\"|\"$", ""); // Remove quotes from the title
                String reason = (parts.length > 1) ? parts[1].trim().replaceAll("^\"|\"$", "") : ""; // Remove quotes from the reason

                // Create the checkbox with the title and store the reason
                JCheckBox checkBox = new JCheckBox(title);
                checkBoxes.add(checkBox);
                sentences.add(reason); // Store reason separately for later use
                panel.add(checkBox);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "Error reading file: " + e.getMessage());
        }
    }

    // Update UI to display current Erper
    private static void updateUI() {
        // Reset all checkboxes
        resetCheckboxes();

        // Update the resultArea with current Erper's reason if available
        Erper currentErper = erpers.get(currentIndex);
        resultArea.setText(currentErper.getReason() != null ? currentErper.getReason() : "");

        // Set the title of the frame or any other updates for the current Erper
        // E.g., frame.setTitle("Processing: " + currentErper.getURL());
    }

    // Reset all checkboxes
    private static void resetCheckboxes() {
        for (JCheckBox checkBox : checkBoxesUsername) {
            checkBox.setSelected(false);
        }
        for (JCheckBox checkBox : checkBoxesAvatar) {
            checkBox.setSelected(false);
        }
        for (JCheckBox checkBox : checkBoxesDescription) {
            checkBox.setSelected(false);
        }
        for (JCheckBox checkBox : checkBoxesMiscellaneous) {
            checkBox.setSelected(false);
        }
    }

    // Helper method to append selected reasons from checkboxes
    private static void appendSelectedReasons(StringBuilder reasons, List<JCheckBox> checkBoxes, List<String> sentences) {
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                reasons.append(sentences.get(i)).append(", ");
            }
        }
        // Remove the trailing comma and space if reasons were added
        if (reasons.length() > 0) {
            reasons.setLength(reasons.length() - 2);
        }
    }

}

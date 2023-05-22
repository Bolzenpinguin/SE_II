import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The "Instructions" class extends JPanel and represents an instructions view.
 * It contains a text area for displaying instruction information and a back button
 * to return to the main menu.
 */
public class Instructions extends JPanel {

    /**
     * Constructor for the "Instructions" class.
     *
     * @param frame The main window (MainFrame) that contains the instructions view.
     */
    public Instructions(MainFrame frame) {
        setLayout(new BorderLayout());

        // Create the text area for instruction information
        JTextArea instructionText = new JTextArea();
        instructionText.setEditable(false);

        // Create a scroll pane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(instructionText);
        add(scrollPane, BorderLayout.CENTER);

        // Create the back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        add(backButton, BorderLayout.PAGE_END);

        try (BufferedReader reader = new BufferedReader(new FileReader("textContent/Instructions.txt"))) {
            // Read the instruction information from the text file and display it in the text area
            instructionText.read(reader, null);
        } catch (IOException e) {
            // If the file is not found or cannot be read, display an error message
            instructionText.setText("Instructions file not found!");
        }
    }
}
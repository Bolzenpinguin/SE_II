import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * The "Help" class extends JPanel and represents a help view.
 * It contains a text area for displaying help information and a back button
 * to return to the main menu.
 */
public class Help extends JPanel {

    /**
     * Constructor for the "Help" class.
     *
     * @param frame The main window (MainFrame) that contains the help view.
     */
    public Help(MainFrame frame) {
        setLayout(new BorderLayout());

        // Create the text area for help information
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);

        // Create a scroll pane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(helpText);
        add(scrollPane, BorderLayout.CENTER);

        // Create the back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        add(backButton, BorderLayout.PAGE_END);

        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Help.txt"))) {
            // Read the help information from the text file and display it in the text area
            helpText.read(reader, null);
        } catch (IOException e) {
            // If the file is not found or cannot be read, display an error message
            helpText.setText("Help file not found!");
        }
    }
}
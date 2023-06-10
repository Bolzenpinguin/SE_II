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
        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        // Attempt to read the about text from a file and load it into the text area
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Resource/textContent/Help.txt")))) {
            helpText.read(reader, null);
        } catch (IOException e) {
            helpText.setText("Help file not found!");
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * A JPanel class that creates an 'About' page for a GUI.
 * <p>
 * The 'About' page contains a text area that displays information about the program.
 * The About Page displays an excerpt of the company portrait.
 * There is also a "Back to Main Menu" button that allows the user 
 * to return to the main page of the application.
 * </p>
 */
public class About extends JPanel {

    /**
     * Constructs a new 'About' page.
     *
     * @param frame The main application window where this 'About' page will be displayed.
     */
    public About(MainFrame frame){
        setLayout(new BorderLayout());

        // Create the text area for about information
        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);

        // Create a scroll pane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(aboutText);
        add(scrollPane, BorderLayout.CENTER);

        // Create the back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        // Attempt to read the about text from a file and load it into the text area
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/resources/textContent/About.txt"))) {
            aboutText.read(reader, null);
        } catch (IOException e) {
            aboutText.setText("About text not found");
        }
    }
}

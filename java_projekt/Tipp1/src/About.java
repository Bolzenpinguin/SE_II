import javax.swing.*;
import java.awt.*;
import java.io.*;


public class About extends JPanel {
    public About(MainFrame frame){
        setLayout(new BorderLayout());

        // Create the text area for about informations
        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);

        // Create a scroll pane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(aboutText);
        add(scrollPane, BorderLayout.CENTER);

        // Create the back button
        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/About.txt"))) {
            aboutText.read(reader, null);
        } catch (IOException e) {
            aboutText.setText("About text not found");
        }
    }
}
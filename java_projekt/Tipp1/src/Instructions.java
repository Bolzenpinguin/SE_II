import javax.swing.*;

public class Instructions extends JPanel {
    public Instructions(MainFrame frame) {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));
        add(backButton);
        // Add other components here
    }
}
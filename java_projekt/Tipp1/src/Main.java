import javax.swing.*;

/**
 * The "Main" class contains the main method to start the application.
 * It creates an instance of the "MainFrame" class and sets up the main window.
 */
public class Main {
    /**
     * The main method of the application.
     * It creates an instance of "MainFrame", configures the main window, and makes it visible.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create an instance of the "MainFrame" class
            MainFrame frame = new MainFrame();

            // Set the title of the main window
            frame.setTitle("Tipp1");

            // Set the default close operation of the main window
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Pack the components in the main window
            frame.pack();

            // Center the main window on the screen
            frame.setLocationRelativeTo(null);

            // Set the size of the main window
            frame.setSize(1000, 500);

            // Make the main window visible
            frame.setVisible(true);
        });
    }
}
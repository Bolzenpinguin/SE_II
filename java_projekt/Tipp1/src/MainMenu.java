import javax.swing.*;
import java.awt.*;


/**
 * The "MainMenu" class extends JPanel and represents the main menu view of the application.
 * It displays buttons for different menu options, including play, help, scoreboard, instructions,
 * and quit.
 */
public class MainMenu extends JPanel {

    /**
     * Constructor for the "MainMenu" class.
     *
     * @param frame The main window (MainFrame) that contains the main menu view.
     */
    public MainMenu(MainFrame frame) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create buttons for menu options
        JButton playButton = new JButton("Play");
        JButton helpButton = new JButton("Help");
        JButton scoreButton = new JButton("Scoreboard");
        JButton instructionsButton = new JButton("Instructions");
        JButton quitButton = new JButton("Quit");

        // Align buttons horizontally at the center
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners to the buttons to switch to corresponding views or exit the application
        playButton.addActionListener(e -> frame.showPanel("Play"));
        helpButton.addActionListener(e -> frame.showPanel("Help"));
        scoreButton.addActionListener(e -> frame.showPanel("ScoreBoard"));
        instructionsButton.addActionListener(e -> frame.showPanel("Instructions"));
        quitButton.addActionListener(e -> System.exit(0));


        // Add vertical glue to push buttons to the top and bottom of the panel
        add(Box.createVerticalGlue());
        // Add buttons with rigid areas for spacing
        add(playButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scoreButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(instructionsButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(helpButton);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Add quit button with vertical glue
        add(quitButton);
        add(Box.createVerticalGlue());
    }
}
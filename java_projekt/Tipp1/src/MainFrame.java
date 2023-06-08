import javax.swing.*;
import java.awt.*;


/**
 * The "MainFrame" class extends JFrame and represents the main window of the application.
 * It manages the different panels/views using CardLayout, including the main menu, help,
 * play, scoreboard, and instructions.
 */
public class MainFrame extends JFrame {
    MainMenu mainMenu;
    Play play;
    ScoreBoard scoreBoard;
    Help help;
    Instructions instructions;
    CardLayout cardLayout;
    About about;

    /**
     * Constructor for the "MainFrame" class.
     * Initializes the CardLayout and creates instances of the different panels/views.
     */
    public MainFrame() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Create instances of the different panels/views
        mainMenu = new MainMenu(this);
        help = new Help(this);
        play = new Play(this,scoreBoard);
        scoreBoard = new ScoreBoard(this);
        instructions = new Instructions(this);
        about = new About(this);

        // Add the panels/views to the main frame with corresponding names
        add(mainMenu, "MainMenu");
        add(help, "Help");
        add(play, "Play");
        add(scoreBoard, "ScoreBoard");
        add(instructions, "Instructions");
        add(about, "About");
    }

    /**
     * Switches the currently displayed panel/view based on the provided name.
     *
     * @param name The name of the panel/view to show.
     */
    public void showPanel(String name) {
        cardLayout.show(getContentPane(), name);
    }
}
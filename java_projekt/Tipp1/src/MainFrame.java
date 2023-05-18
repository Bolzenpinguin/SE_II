import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    MainMenu mainMenu;
    Play play;
    ScoreBoard scoreBoard;
    Help help;
    Instructions instructions;
    CardLayout cardLayout;

    public MainFrame() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        mainMenu = new MainMenu(this);
        help = new Help(this);
        play = new Play(this);
        scoreBoard = new ScoreBoard(this);
        instructions = new Instructions(this);

        add(mainMenu, "MainMenu");
        add(help, "Help");
        add(play, "Play");
        add(scoreBoard, "ScoreBoard");
        add(instructions, "Instructions");
    }

    public void showPanel(String name) {
        cardLayout.show(getContentPane(), name);
    }
}

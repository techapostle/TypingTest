package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    // Labels
    @FXML
    private Label timeLabel;
    @FXML
    private Label displayUsername;

    // Texts
    @FXML
    private Text total;
    @FXML
    private Text wpm;
    @FXML
    private Text invalid;

    // initializer
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set username if any
        File userFile = new File("username.txt");
        if (userFile.length() != 0) {
            Scanner reader = null;
            try {
                reader = new Scanner(userFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String username = reader.nextLine();
            displayUsername.setText("Welcome, " + username);
        }

        // set day
        Date date = new Date();
        Locale locale = new Locale("en");
        DateFormat formatter = new SimpleDateFormat("EEEE", locale);
        String dayStr = formatter.format(date);
        timeLabel.setText("Today is " + dayStr);

        // TODO: display user stats

    }

    // Methods
    public void playGame(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        File userFile = new File("username.txt");
        if (userFile.length() == 0)
            m.changeScene("popup.fxml");
        else
            m.changeScene("game.fxml");
    }
}

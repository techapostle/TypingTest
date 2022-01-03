package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController implements Initializable {


    // FXML elements
    @FXML
    private Text seconds;
    @FXML
    private Text wordsPerMin;
    @FXML
    private Text accuracy;
    @FXML
    private Text programWord;
    @FXML
    private Text secondProgramWord;
    @FXML
    private TextField userWord;
    @FXML
    private ImageView correct;
    @FXML
    private ImageView wrong;
    @FXML
    private Button playAgain;
    @FXML
    public Text gameTip;

    // store array of words
    ArrayList<String> wordsList = new ArrayList<>();

    // file for save data
    private File saveData;

    private int wordCounter = 0;
    private int first = 1;
    private int countAll = 0;
    private int counter = 0;
    private int timer = 60;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * timer thread.
     * see startGame function for usage
     */
    Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {
            if (timer > -1) {
                seconds.setText(String.valueOf(timer));
                timer--;
            } else {
                if (timer == -1) {
                    userWord.setDisable(true);
                    userWord.setText("Game Over!");
                    try {
                        FileWriter fileWriter = new FileWriter(saveData);
                        fileWriter.write(countAll + ";");
                        fileWriter.write(counter + ";");
                        fileWriter.write(String.valueOf(countAll - counter));
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (timer == -4) {
                    playAgain.setVisible(true);
                    playAgain.setDisable(false);
                    executor.shutdown();
                }
                timer--;
            }
        }
    };

    /**
     * Runnable thread for correct animation.
     * Gets called when the input matches the desired value.
     */
    Runnable fadeCorrect = new Runnable() {
        @Override
        public void run() {
            animateAction(correct);
        }
    };

    /**
     * Runnable thread for wrong animation.
     * Gets called when the input does not match the desired value.
     */
    Runnable fadeWrong = new Runnable() {
        @Override
        public void run() {
            animateAction(wrong);
        }
    };

    private void animateAction(ImageView imageView) {
        int opacity = 0;
        for (int i = 0; i < 3; i++) {
            imageView.setOpacity(opacity);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            opacity = opacity >= 100 ? 0 : opacity+50;
        }
        imageView.setOpacity(opacity); // opacity should == 0
    }

    // function to extract line from wordList
    public void addToList() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("wordsList"));
            String line = reader.readLine();
            while (line != null) {
                this.wordsList.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function that gets called at start of game
    public void startGame(KeyEvent keyEvent) throws IOException {
        // only gets called once since first is initialized to 1
        if (first == 1) {
            first = 0;
            executor.scheduleAtFixedRate(runnableTimer, 0, 1, TimeUnit.SECONDS);
        }
        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            String s = userWord.getText();
            String real = programWord.getText();
            countAll++;

            if (s.trim().equals(real)) {
                counter++;
                wordsPerMin.setText(String.valueOf(counter));
                Thread t = new Thread(fadeCorrect);
                t.start();
            } else {
                Thread t = new Thread(fadeWrong);
                t.start();
            }
            userWord.setText("");
            accuracy.setText(String.valueOf(Math.round((counter*1.0/countAll)*100)));
            programWord.setText(wordsList.get(wordCounter));
            secondProgramWord.setText(wordsList.get(++wordCounter));
        }
    }

    // function to handle request to main menu
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("sample.fxml");
    }

    // initializer method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playAgain.setVisible(true);
        playAgain.setVisible(false);
        seconds.setText("60");
        accuracy.setText(" ");
        addToList();
        Collections.shuffle(wordsList);
        programWord.setText(wordsList.get(wordCounter));
        secondProgramWord.setText(wordsList.get(++wordCounter));

        Date date = new Date() ;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        saveData = new File("src/data/" +  formatter.format(date).strip() + ".txt");

        try {
            if (saveData.createNewFile())
                System.out.println("New file created: " + saveData.getName());
            else
                System.out.println("File already exists");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

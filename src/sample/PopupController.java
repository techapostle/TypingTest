package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;

public class PopupController {

    @FXML
    private TextField username;

    public void submit(ActionEvent actionEvent) throws IOException {
        // Write to file the value of username TextField
        FileWriter fileWriter = new FileWriter("username.txt");
        fileWriter.write(username.getText());
        fileWriter.close();

        Main m = new Main();
        m.changeScene("sample.fxml");
    }
}

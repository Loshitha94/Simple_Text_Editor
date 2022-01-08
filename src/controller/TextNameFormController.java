package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TextNameFormController {
     public TextField txtInput;
    public AnchorPane fileNameContext;
    public static String fileName;

    public void EnterOnAction(ActionEvent actionEvent) {
        fileName=txtInput.getText();
        Stage stage = (Stage) fileNameContext.getScene().getWindow();
        stage.close();
    }
}

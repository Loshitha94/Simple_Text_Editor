package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainFormController extends TextNameFormController{
    public TextArea txtArea;
    public AnchorPane mainFormContext;
    String PathForAll;

    public void mnuNewOnAction(ActionEvent actionEvent) throws IOException{
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select the destination");
            File file1 = directoryChooser.showDialog(null);
            String openPath = file1.getAbsolutePath();
            System.out.println(openPath);


            Stage nameStage=new Stage();
            nameStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/textNameForm.fxml"))));
            nameStage.showAndWait();
            System.out.println(fileName);
            PathForAll= openPath+"/"+fileName+".txt";
            System.out.println(PathForAll);

            if (fileName.trim().isEmpty()){
                new Alert(Alert.AlertType.ERROR,"Input a correct name and try again", ButtonType.OK).show();
            }else {
                File file = new File(openPath+"/"+fileName+".txt");
                file.createNewFile();
                txtArea.clear();
                Stage stage = (Stage) mainFormContext.getScene().getWindow();
                stage.setTitle(fileName+".txt");

            }
        }catch (Exception e){

        }

    }


    public void mnuOpenOnAction(ActionEvent actionEvent) throws IOException{

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a file");
            fileChooser.getExtensionFilters().
                    add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            String absolutePath = file.getAbsolutePath();
            PathForAll=absolutePath;
            System.out.println(PathForAll);
            Path path = Paths.get(absolutePath);
            SeekableByteChannel bc= Files.newByteChannel(path);
            ByteBuffer allocate = ByteBuffer.allocate((int) bc.size());
            bc.read(allocate);
            bc.close();
            byte[] array = allocate.array();
            String content = new String(array);
            txtArea.setText(content);
            Stage stage = (Stage) mainFormContext.getScene().getWindow();
            stage.setTitle(String.valueOf(path.getFileName()));


            System.out.println(stage.getTitle());
        } catch (Exception e){

        }


    }

    public void mnuSaveOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) mainFormContext.getScene().getWindow();
        /*Path path = Paths.get("/home/loshitha/Desktop/"+stage.getTitle());*/
        Path path = Paths.get(PathForAll);
        String fileContent = txtArea.getText();
        byte[] bytes = fileContent.getBytes();
        SeekableByteChannel byteChannel = Files.newByteChannel(path, StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byteChannel.write(buffer);
        byteChannel.close();
        Files.write(path, bytes);
    }

    public void mnuPrintOnAction(ActionEvent actionEvent) {

    }

    public void mnuExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) mainFormContext.getScene().getWindow();
        stage.close();
    }
    private String cutOrCopy;
    public void mnuCutOnAction(ActionEvent actionEvent) {
        byte[] bytes = txtArea.getSelectedText().getBytes(StandardCharsets.UTF_8);
        cutOrCopy = new String(bytes);
        int caretPosition = txtArea.getCaretPosition();
        txtArea.setText(txtArea.getText().replace(txtArea.getSelectedText(),""));
        txtArea.positionCaret(caretPosition);

    }

    public void mnuCopyOnAction(ActionEvent actionEvent) {
        byte[] bytes = txtArea.getSelectedText().getBytes(StandardCharsets.UTF_8);
        cutOrCopy = new String(bytes);
    }

    public void mnuPastOnAction(ActionEvent actionEvent) {
        int caretPosition = txtArea.getCaretPosition();
        txtArea.insertText(caretPosition,cutOrCopy);
    }

    public void mnuSelectAllOnAction(ActionEvent actionEvent) {
        txtArea.selectAll();
    }

    public void mnuAboutUsOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AboutUsForm.fxml"))));
        stage.setTitle("About Us");
        stage.show();
    }

    public void newOnAction(MouseEvent mouseEvent) throws IOException{
        mnuNewOnAction(new ActionEvent());
    }

    public void saveOnAction(MouseEvent mouseEvent) throws IOException {
        mnuSaveOnAction(new ActionEvent());
    }

    public void openOnAction(MouseEvent mouseEvent) throws IOException {
        mnuOpenOnAction(new ActionEvent());
    }

    public void cutOnAction(MouseEvent mouseEvent) {
       mnuCutOnAction(new ActionEvent());
    }

    public void copyOnAction(MouseEvent mouseEvent) {
        mnuCopyOnAction(new ActionEvent());
    }
}

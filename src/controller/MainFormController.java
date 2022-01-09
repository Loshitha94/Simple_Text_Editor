package controller;

import com.jfoenix.controls.JFXTextField;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFormController extends TextNameFormController{
    public TextArea txtArea;
    public AnchorPane mainFormContext;
    public TextField txtSearchText;
    public Button btnUp;
    public Button btnDown;
    public ToggleButton btnCaseSensitive;
    public ToggleButton btnRegex;
    public Button btnReplace;
    public Label lblFoundedWordCount;
    public Label lblSelectedWordCount;
    public Button btnReplaceAll;
    public JFXTextField txtReplace;
    String PathForAll;
    private boolean textChange;
    private Matcher matcher;

    public void initialize(){
        txtSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            textChange=true;
        });

        txtArea.textProperty().addListener((observable, oldValue, newValue) -> {
            textChange=true;
        });
    }

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

 // added methods------------------------------------------------------

    public void cutOnAction(MouseEvent mouseEvent) {
       mnuCutOnAction(new ActionEvent());
    }

    public void copyOnAction(MouseEvent mouseEvent) {
        mnuCopyOnAction(new ActionEvent());
    }

    public void upOnAction(ActionEvent actionEvent) {
    }

    public void downOnAction(ActionEvent actionEvent) {
        txtArea.deselect();
        if (textChange){
            int flags=0;
            if (!btnRegex.isSelected()){
                flags=flags | Pattern.LITERAL;
            }
            if (!btnCaseSensitive.isSelected()){
                flags=flags|Pattern.CASE_INSENSITIVE;
            }
            if (!btnRegex.isSelected()){
                flags=flags|Pattern.LITERAL;
            }
            matcher = Pattern.compile(txtSearchText.getText(),flags).matcher(txtArea.getText());
            textChange=false;
        }
        if (matcher.find()){
            txtArea.selectRange(matcher.start(),matcher.end());
        }else {
            matcher.reset();
        }
    }

    public void caseSensitiveOnAction(ActionEvent actionEvent) {
        textChange=true;
        btnDown.fire();
    }

    public void reGexOnAction(ActionEvent actionEvent) {
        textChange=true;
        btnDown.fire();
    }

    public void replaceOnAction(ActionEvent actionEvent) {

    }

    public void replaceAllOnAction(ActionEvent actionEvent) {
    }
}

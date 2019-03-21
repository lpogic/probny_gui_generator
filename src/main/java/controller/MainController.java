package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import model.FXMLWriter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MainController {
    public static URL url;

    @FXML
    private Button save;

    @FXML
    private Button exit;

    @FXML
    private Pane sandboxPane;

    @FXML
    public void initialize(){
        sandboxPane.setOnMousePressed((e)->{
            if(e.getButton() == MouseButton.SECONDARY){
                Button button = new Button("Button");
                button.setLayoutX(e.getSceneX());
                button.setLayoutY(e.getSceneY());
                sandboxPane.getChildren().add(button);
            }
        });
        save.setOnAction((e)->{
            FXMLWriter writer = new FXMLWriter("new.fxml");
            try{
                writer.write(sandboxPane);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            try {
                Files.copy(url.openStream(), Paths.get("template.jar"), StandardCopyOption.REPLACE_EXISTING);
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        });
        exit.setOnAction((e)->Platform.exit());
    }
}

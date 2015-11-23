/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotogallerie.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

/**
 *
 * @author Florian
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Pane imageGridPane;
    
    
    @FXML
    private void openFolder(ActionEvent event) {
        System.out.println("Open Folder");
        
        
    }
    @FXML
    private void openImage(ActionEvent event) {
        System.out.println("Open Image");
        Image myImage = new Image("file:src/fotogallerie/javafx/309.jpg");
        ImageView test = new ImageView(myImage);
        test.setFitHeight(150);
        test.setFitWidth(150);
        imageGridPane.getChildren().add(test);
        
    }
    @FXML
    private void startNew(ActionEvent event) {
        System.out.println("start new");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

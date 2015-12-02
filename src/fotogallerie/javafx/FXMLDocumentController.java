/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotogallerie.javafx;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Florian
 */
public class FXMLDocumentController implements Initializable {
    
    private final List<File> imageList = new ArrayList();
    
    @FXML
    private TilePane imageGridPane;
    @FXML
    private ScrollPane imageScrollPane;
    
    
    @FXML
    private void openFolder(ActionEvent event) {
        
        
        DirectoryChooser dicChooser = new DirectoryChooser();
        File file = dicChooser.showDialog(null);
        addFilesFromDir(file);
        
        for(int i =0 ; i <imageList.size();i++){
            System.out.println("Open Folder: "+ " "+ imageList.get(i));
            
            ImageView smallImageView = new ImageView();
            Image image = new Image(imageList.get(i).toURI().toString());
            smallImageView.setImage(image);
            System.out.println(image);
            smallImageView.setFitWidth(100);
            smallImageView.setFitHeight(100);
           
            imageGridPane.getChildren().add(smallImageView);
            
        }
        
    }
    
     private void addFilesFromDir(File dir) {
        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                imageList.add(file);
            }
        }
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

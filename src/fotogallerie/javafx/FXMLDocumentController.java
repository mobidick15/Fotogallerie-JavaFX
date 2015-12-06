/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotogallerie.javafx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Florian
 */
public class FXMLDocumentController extends FotogallerieJavaFX implements Initializable {

    private final List<File> imageList = new ArrayList();
    public boolean isDiashoow;
    private int imageIndex = 0;
    private int indexOfSlectedImage;

    @FXML
    private TilePane imageGridPane;
    @FXML
    private AnchorPane content;
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private Tab imageTab;
    @FXML
    private Tab diaShowTab;
    @FXML
    private ImageView largeImageView;
    @FXML
    private ImageView diashowImage;
    @FXML
    private TabPane myTabPane;
    @FXML
    private Pane largeImagePane;
    @FXML
    private Button diaShowButton;

    @FXML
    private void openFolder(ActionEvent event) {

        DirectoryChooser dicChooser = new DirectoryChooser();
        File file = dicChooser.showDialog(null);
        addFilesFromDir(file);

        for (imageIndex = 0; imageIndex<imageList.size();imageIndex++){

            ImageView smallImageView = new ImageView();
            Image image = new Image(imageList.get(imageIndex).toURI().toString());
            smallImageView.setImage(image);
            System.out.println(image);
            smallImageView.setFitWidth(100);
            smallImageView.setFitHeight(100);
            
            smallImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent event) {
                    openLageImage(image);
                   
                    
                    
                }
            });

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

    @FXML
    private void clearImageTab(ActionEvent event) {
        largeImageView.setImage(null);
    }

    @FXML
    private void onScroll(ZoomEvent event) {
        //System.out.println(event);
        largeImageView.setScaleX(largeImageView.getScaleX() * event.getZoomFactor());
        largeImageView.setScaleY(largeImageView.getScaleY() * event.getZoomFactor());

    }

    @FXML
    private void startFullScreen(MouseEvent event) {

        if (event.getClickCount() == 2) {
            Stage stage = (Stage) myTabPane.getScene().getWindow();
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
                diaShowTab.setDisable(false);
                diaShowButton.setVisible(true);

            } else {
                stage.setFullScreen(false);
                diaShowTab.setDisable(true);
                diaShowButton.setVisible(false);
            }
        }
    }

    @FXML
    private void openDiashow(ActionEvent event) throws InterruptedException {

        diashowImage.setImage(null);
        if (!isDiashoow) {
            myTabPane.getSelectionModel().select(diaShowTab);
            diashowImage.setImage(largeImageView.getImage());
            Stage stage = (Stage) myTabPane.getScene().getWindow();
            stage.setFullScreen(true);

            diashowImage.setFitHeight(stage.getHeight());
            diashowImage.setFitWidth(stage.getWidth());
            KeyCode kevent = KeyCode.SPACE;

            startDiashow(kevent);
        }
    }

    @FXML
    private void startDiashow(KeyCode event) throws InterruptedException {
       
        for(int i = 0; i<=imageList.size();i++){
           
        Image slideImage = new Image(imageList.get(i).toURI().toString());
        Image nextImage = new Image(imageList.get(i+1).toURI().toString());
        diashowImage.setImage(slideImage);
        

        RotateTransition rotate = new RotateTransition(Duration.millis(1000), diashowImage);
        rotate.setToAngle(360);
        FadeTransition slideOut = new FadeTransition(Duration.millis(1000), diashowImage);
        slideOut.setFromValue(1.0);
        slideOut.setToValue(0.0);

        ParallelTransition transition = new ParallelTransition(diashowImage, rotate, slideOut);
        transition.setCycleCount(1);
        transition.setAutoReverse(true);

        transition.play();

        diashowImage.setImage(nextImage);

        FadeTransition slideIn = new FadeTransition(Duration.millis(1500), diashowImage);
        slideIn.setFromValue(0.0);
        slideIn.setToValue(1.0);
        slideIn.setCycleCount(1);
        
        slideIn.play();
        Thread.sleep(1000);
        }

    }

    private void openLageImage(Image selectedSmallImage) {
        largeImageView.setImage(null);
        imageTab.setDisable(false);
        diaShowTab.setDisable(false);
        myTabPane.getSelectionModel().select(imageTab);
        largeImageView.setImage(selectedSmallImage);
    
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        isDiashoow = false;
        imageScrollPane.setFitToWidth(true);
        largeImagePane.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            largeImageView.setFitHeight((double) newValue);
        });

        largeImagePane.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            largeImageView.setFitWidth((double) newValue);
        });

    }

}

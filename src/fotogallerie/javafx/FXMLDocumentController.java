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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Florian
 */
public class FXMLDocumentController extends FotogallerieJavaFX implements Initializable {

    private final List<File> imageList = new ArrayList();
    private final List<Image> ListOfImage = new ArrayList();
    public boolean isDiashoow;
    private int imageIndex = 0;
    private int indexOfSlectedImage;
    public ImageView[] imagesToSlide;
    private int indexPrev = 0;
    private int indexNext = 0;
    private Timeline timeline;
    private AnimationTimer timer;
    private Stage diaShowStage;

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

        for (int i = 0; i < imageList.size(); i++) {

            ImageView smallImageView = new ImageView();
            Image image = new Image(imageList.get(i).toURI().toString());
            smallImageView.setImage(image);
            ListOfImage.add(image);
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
                
                diaShowButton.setVisible(true);

            } else {
                stage.setFullScreen(false);
                
                diaShowButton.setVisible(false);
            }
        }
    }

    @FXML
    private void openDiashow(ActionEvent event) throws InterruptedException {

            diashowImage = new ImageView();
            diashowImage.setImage(largeImageView.getImage());
            diashowImage.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());
            diashowImage.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
            
           diaShowStage = new Stage();
           diaShowStage.setFullScreen(true);
                        
            

            KeyCode kevent = KeyCode.SPACE;

            startDiashow(null);
            HBox root = new HBox();
            root.setAlignment(Pos.CENTER);
            root.getChildren().add(diashowImage);
            Scene scene = new Scene(root);
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.SPACE) {
                        if (timeline.getStatus() == Animation.Status.RUNNING) {
                            timeline.pause();
                        } else {
                            timeline.play();
                        }
                    } else if (event.getCode() == KeyCode.LEFT) {
                        imageIndex--;
                        diashowImage.setImage(ListOfImage.get(imageIndex));  
                    } else if (event.getCode() == KeyCode.RIGHT) {
                        imageIndex++;
                        diashowImage.setImage(ListOfImage.get(imageIndex));
                    }else if(event.getCode() == KeyCode.ESCAPE) {
                        diaShowStage.close();
                        timeline.stop();
                    }
                    }
                }
            );
            diaShowStage.setScene(scene);
            diaShowStage.show();
         
    }

    @FXML
    private void startDiashow(KeyCode event) throws InterruptedException {
        
        
        imageIndex = ListOfImage.indexOf(largeImageView.getImage()) +1;
        timeline = new Timeline();

        KeyValue transparent = new KeyValue(diashowImage.opacityProperty(), 0.0);
        KeyValue opaque = new KeyValue(diashowImage.opacityProperty(), 1.0);

        KeyFrame startFadeIn = new KeyFrame(Duration.ZERO, transparent);
        KeyFrame endFadeIn = new KeyFrame(Duration.millis(250), opaque);
        KeyFrame startFadeOut = new KeyFrame(Duration.millis(2000), opaque);
        KeyFrame endFadeOut = new KeyFrame(Duration.millis(2500), e -> {
            if (imageIndex < ListOfImage.size()) {
                diashowImage.setImage(ListOfImage.get(imageIndex));
                imageIndex++;
            }
        }, transparent);

        timeline.getKeyFrames().addAll(startFadeIn, endFadeIn, startFadeOut, endFadeOut);

        timeline.setCycleCount(ListOfImage.size() - imageIndex );
        timeline.play();
        
    }

    private void openLageImage(Image selectedSmallImage) {
        largeImageView.setImage(null);
        imageTab.setDisable(false);
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

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
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;

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

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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

    public ImageView[] imagesToSlide;
    private Timeline timeline;
    private Stage diaShowStage;

    @FXML
    private TilePane imageGridPane;
   
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private Tab imageTab;
  
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
        imageList.clear();
        DirectoryChooser dicChooser = new DirectoryChooser();
        File file = dicChooser.showDialog(null);
        addFilesFromDir(file);
        addImagesToGrid();
        

    }

    private void addFilesFromDir(File dir) {
        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    addFilesFromDir(file);
                } else if (file.getName().endsWith(".jpg") || (file.getName().endsWith(".png")) || (file.getName().endsWith(".jpeg"))) {
                    imageList.add(file);
                }
            }
        }
    }
    
    private void addImagesToGrid(){
        for (int i = 0; i < imageList.size(); i++) {

            ImageView smallImageView = new ImageView();
            Image image = new Image(imageList.get(i).toURI().toString());
            smallImageView.setImage(image);
            ListOfImage.add(image);
            smallImageView.setFitWidth(100);
            smallImageView.setFitHeight(100);

            smallImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent event) {
                    openLageImage(image);

                }
            });

            imageGridPane.getChildren().add(smallImageView);
            imageGridPane.setVisible(true);
        }
    }

    @FXML
    private void openImage(ActionEvent event) {
        imageList.clear();
        FileChooser fileChooser = new FileChooser();
        List<File> list = fileChooser.showOpenMultipleDialog(null);
        if (list != null) {
            for (File file : list) {
                String fileName = file.getName().toLowerCase();

                if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                    imageList.add(file);
                }

            }
            addImagesToGrid();
        }
    }

    @FXML
    private void startNew(ActionEvent event) throws IOException, Exception {
        
        imageList.clear();
        Stage stage = (Stage) myTabPane.getScene().getWindow();
        stage.close();

        FotogallerieJavaFX fotogallerie = new FotogallerieJavaFX();
        fotogallerie.start(stage);
    }

    @FXML
    private void clearImageTab(ActionEvent event
    ) {
        largeImageView.setImage(null);
    }

    @FXML
    private void onScroll(ZoomEvent event) {
        
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
                stage.setFullScreen(true);

                diaShowButton.setVisible(false);
            }
        }
    }

    @FXML
    private void openDiashow(ActionEvent event) throws InterruptedException {

        diashowImage = new ImageView();
        diashowImage.setImage(largeImageView.getImage());
        diaShowStage.setFullScreen(true);

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

                } else if (event.getCode() == KeyCode.RIGHT) {

                    imageIndex++;
                    diashowImage.setImage(ListOfImage.get(imageIndex));
                } else if (event.getCode() == KeyCode.ESCAPE) {
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

        imageIndex = ListOfImage.indexOf(largeImageView.getImage()) + 1;
        int cycleCount = ListOfImage.size() - imageIndex + 1;
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

        timeline.setCycleCount(cycleCount);
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
        diaShowStage = new Stage();
       
        imageScrollPane.setFitToWidth(true);
        largeImagePane.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            largeImageView.setFitHeight((double) newValue);
        });

        largeImagePane.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            largeImageView.setFitWidth((double) newValue);
        });
        
       diaShowStage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            diashowImage.setSmooth(true);
            diashowImage.setFitHeight((double) newValue);
        });

        diaShowStage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            diashowImage.setFitWidth((double) newValue);
        });

    }

}

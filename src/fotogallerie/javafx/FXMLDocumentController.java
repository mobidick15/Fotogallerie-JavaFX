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
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;

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
    private SequentialTransition diashowAnimation;
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
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;

    @FXML
    private void openFolder(ActionEvent event) {
        imageList.clear();
        DirectoryChooser dicChooser = new DirectoryChooser();
        File file = dicChooser.showDialog(null);
        if(file != null){
        addFilesFromDir(file);
        addImagesToGrid();
        }
    }
    
    @FXML
    private void openImage(ActionEvent event) {
        imageList.clear();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        FileChooser.ExtensionFilter combinedFilterFilter = new FileChooser.ExtensionFilter("Images ", "*.jpg", "*.png", "*.jpeg");

        fileChooser.getExtensionFilters().addAll(jpgFilter, jpegFilter, pngFilter, combinedFilterFilter);
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

    private void addImagesToGrid() {
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
    private void zoomIn() {
        largeImageView.setScaleX(largeImageView.getScaleX() * 2);
        largeImageView.setScaleY(largeImageView.getScaleY() * 2);

    }

    @FXML
    private void zoomOut() {
        largeImageView.setScaleX(largeImageView.getScaleX() * 0.5);
        largeImageView.setScaleY(largeImageView.getScaleY() * 0.5);
    }

    @FXML
    private void startFullScreen(MouseEvent event) {

        if (event.getClickCount() == 2) {
            Stage stage = (Stage) myTabPane.getScene().getWindow();
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);

                

            } else {
                stage.setFullScreen(true);

                
            }
        }
    }

    @FXML
    private void openDiashow(ActionEvent event) throws InterruptedException {

        imageIndex = ListOfImage.indexOf(largeImageView.getImage());
        diashowImage = new ImageView();
        diashowImage.setOpacity(0f);
        if (imageIndex == -1) {
            diashowImage.setImage(ListOfImage.get(0));
            imageIndex = 1;
        } else {
            diashowImage.setImage(ListOfImage.get(imageIndex));
            imageIndex++;
        }
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
                    if (diashowAnimation.getStatus() == Animation.Status.RUNNING) {
                        diashowAnimation.pause();
                    } else {
                        diashowAnimation.play();
                    }
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    diaShowStage.close();
                    diashowAnimation.stop();
                }
            }
        }
        );
        diaShowStage.setScene(scene);
        diaShowStage.show();

    }

    @FXML
    private void startDiashow(KeyCode event) throws InterruptedException {

        int cycleCount = ListOfImage.size() - imageIndex + 1;
        diashowAnimation = new SequentialTransition();

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(2000), diashowImage);
        fadeInTransition.setFromValue(0.0f);
        fadeInTransition.setToValue(1.0f);

        RotateTransition imageRotation = new RotateTransition(Duration.millis(750), diashowImage);
        imageRotation.setByAngle(720f);

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(3000));

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), diashowImage);
        scaleTransition.setToX(0f);
        scaleTransition.setToY(0f);

        KeyValue transparent = new KeyValue(diashowImage.opacityProperty(), 0.0);
        KeyFrame endFadeOut = new KeyFrame(Duration.millis(500), e -> {
            if (imageIndex < ListOfImage.size()) {
                diashowImage.setImage(ListOfImage.get(imageIndex));
                imageIndex++;
            }
        }, transparent);

        Timeline changeImage = new Timeline(endFadeOut);
        ParallelTransition showImage = new ParallelTransition();
        showImage.getChildren().addAll(imageRotation, fadeInTransition);

        diashowAnimation.getChildren().addAll(showImage, pauseTransition, scaleTransition, changeImage);

        diashowAnimation.setCycleCount(cycleCount);
        diashowAnimation.play();
        diashowAnimation.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                diaShowStage.close();
            }

        });

    }

    private void openLageImage(Image selectedSmallImage) {
        largeImageView.setImage(null);
        imageTab.setDisable(false);
        myTabPane.getSelectionModel().select(imageTab);
        largeImageView.setImage(selectedSmallImage);
        largeImageView.setFitHeight(largeImagePane.getHeight());
        largeImageView.setFitWidth(largeImagePane.getWidth());
        

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

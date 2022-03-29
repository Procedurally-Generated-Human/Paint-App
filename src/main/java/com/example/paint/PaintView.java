package com.example.paint;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class PaintView {
    public static double[] old_c = new double[2];
    public String tool = "draw";

    @FXML
    public Canvas canvas;

    @FXML
    public ColorPicker colorpicker;

    @FXML
    public ChoiceBox<String> sizepicker;
    @FXML
    public void initialize() {
        sizepicker.getItems().removeAll(sizepicker.getItems());
        sizepicker.getItems().addAll("2","4","6","8","10","12","24","50","100");
        sizepicker.getSelectionModel().select("50");
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(e -> {
            double brush_size = Double.parseDouble(sizepicker.getValue());
            double x = e.getX() - brush_size / 2;
            double y = e.getY() - brush_size / 2;
            if(Objects.equals(tool, "draw")) {
                gc.setFill(colorpicker.getValue());
                gc.fillOval(x, y, brush_size, brush_size);
            }
            else if(Objects.equals(tool, "eraser")){
                gc.clearRect(x, y, brush_size, brush_size);
            }
        });

        canvas.setOnMouseDragged(e -> {
            double brush_size = Double.parseDouble(sizepicker.getValue());
            double x = e.getX() - brush_size / 2;
            double y = e.getY() - brush_size / 2;
            if(Objects.equals(tool, "draw")){
                if (old_c[0] == 0 && old_c[1] == 0) {
                    old_c[0] = x;
                    old_c[1] = y;
                }
                gc.setFill(colorpicker.getValue());
                gc.fillOval(x, y, brush_size, brush_size);
                double x1 = x;
                double x2 = old_c[0];
                double y1 = y;
                double y2 = old_c[1];
                double m = (y1 - y2) / (x1 - x2);
                double b = y1 - (m * x1);
                double larger = Math.max(x2, x1);
                double smaller = Math.min(x2, x1);
                double larger_y = Math.max(y2, y1);
                double smaller_y = Math.min(y2, y1);
                if (Double.isFinite(m)) {
                    for (double i = smaller_y; i != larger_y; i++) {
                        double eq_x = (i - b) / m;
                        gc.fillOval(eq_x, i, brush_size, brush_size);
                    }
                }
                if (Double.isFinite(m)) {
                    for (double i = smaller; i != larger; i++) {
                        double eq_y = (m * i) + b;
                        gc.fillOval(i, eq_y, brush_size, brush_size);
                    }
                } else if (Double.isInfinite(m)) {
                    for (double i = smaller_y; i != larger_y; i++) {
                        gc.fillOval(x1, i, brush_size, brush_size);
                    }
                }
                old_c[0] = x;
                old_c[1] = y;
            }

            else if(Objects.equals(tool, "eraser")) {
                if (old_c[0] == 0 && old_c[1] == 0) {
                    old_c[0] = x;
                    old_c[1] = y;
                }
                gc.setFill(colorpicker.getValue());
                gc.clearRect(x, y, brush_size, brush_size);
                double x1 = x;
                double x2 = old_c[0];
                double y1 = y;
                double y2 = old_c[1];
                double m = (y1 - y2) / (x1 - x2);
                double b = y1 - (m * x1);
                double larger = Math.max(x2, x1);
                double smaller = Math.min(x2, x1);
                double larger_y = Math.max(y2, y1);
                double smaller_y = Math.min(y2, y1);
                if (Double.isFinite(m)) {
                    for (double i = smaller_y; i != larger_y; i++) {
                        double eq_x = (i - b) / m;
                        gc.clearRect(eq_x, i, brush_size, brush_size);
                    }
                }
                if (Double.isFinite(m)) {
                    for (double i = smaller; i != larger; i++) {
                        double eq_y = (m * i) + b;
                        gc.clearRect(i, eq_y, brush_size, brush_size);
                    }
                } else if (Double.isInfinite(m)) {
                    for (double i = smaller_y; i != larger_y; i++) {
                        gc.clearRect(x1, i, brush_size, brush_size);
                    }
                }
                old_c[0] = x;
                old_c[1] = y;
            }

        });

        canvas.setOnMouseReleased(e -> {
            old_c[0] = 0;
            old_c[1] = 0;
        });

    }

    @FXML
    public void change_eraser_state(){
        colorpicker.setDisable(!colorpicker.isDisabled());
        if(Objects.equals(tool, "draw")){
            tool = "eraser";
        }
        else if(Objects.equals(tool, "eraser")){
            tool = "draw";
        }
    }

    @FXML
    public void clear(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
    }


    @FXML
    public void save(){
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");

        File file = savefile.showSaveDialog(null);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error!");
            }
        }
    }


    @FXML
    public void save_transparent(){
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");

        File file = savefile.showSaveDialog(null);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);

                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);
                ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(sp, writableImage), null), "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error!");
            }
        }
    }

    @FXML
    public void about() throws IOException {
        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("about-view.fxml"));
        newWindow.setScene(new Scene(loader.load()));
        newWindow.show();
    }


}

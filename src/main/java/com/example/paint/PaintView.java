package com.example.paint;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class PaintView {

    @FXML
    public Canvas canvas;

    @FXML
    public ColorPicker colorpicker;

    @FXML
    public ChoiceBox<String> sizepicker;
    @FXML
    public void initialize() {
        sizepicker.getItems().removeAll(sizepicker.getItems());
        sizepicker.getItems().addAll("1","2","3","4","5","6","7","8","9","10","15","25","50","100");
        sizepicker.getSelectionModel().select("5");
    }


    @FXML
    public void startDraw(MouseEvent mouseEvent){
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        Color brush_color = colorpicker.getValue();
        double brush_size = Double.parseDouble(sizepicker.getValue());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(brush_color);
        gc.setLineWidth(brush_size);
        gc.setFill(brush_color);

        gc.beginPath();
        gc.stroke();
    }

    @FXML
    public void continueDraw(MouseEvent mouseEvent){
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        Color brush_color = colorpicker.getValue();
        double brush_size = Double.parseDouble(sizepicker.getValue());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(brush_color);
        gc.setLineWidth(brush_size);
        gc.setFill(brush_color);

        gc.lineTo(x, y);
        gc.stroke();
        gc.closePath();
        gc.beginPath();
        gc.moveTo(x, y);

    }

    @FXML
    public void endDraw(MouseEvent mouseEvent){
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        Color brush_color = colorpicker.getValue();
        double brush_size = Double.parseDouble(sizepicker.getValue());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(brush_color);
        gc.setLineWidth(brush_size);
        gc.setFill(brush_color);

        gc.lineTo(x, y);
        gc.stroke();
        gc.closePath();
    }

    @FXML
    public void save(){
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");

        File file = savefile.showSaveDialog(null);
        System.out.println("is file null ? "+ file);
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



}

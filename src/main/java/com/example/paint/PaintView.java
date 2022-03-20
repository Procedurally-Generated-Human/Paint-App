package com.example.paint;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

// by decreasing the y of linemove by 60 or something you can create a khose khat
public class PaintView {
    public static Circle c2 = new Circle(1,1,100,Color.GREEN);

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
        sizepicker.getSelectionModel().select("50");

        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            double brush_size = Double.parseDouble(sizepicker.getValue());
            double x = e.getX() - brush_size / 2;
            double y = e.getY() - brush_size / 2;
            gc.setFill(colorpicker.getValue());
            Circle c1 = new Circle(x, y, brush_size, colorpicker.getValue());
            gc.fillOval(c1.getCenterX(), c1.getCenterY(), c1.getRadius(), c1.getRadius());


            double x1 = c1.getCenterX();
            double x2 = c2.getCenterX();
            double y1 = c1.getCenterY();
            double y2 = c2.getCenterY();
            double m = (y1 - y2) / (x1 - x2);
            double b = y1 - (m*x1);

            double larger = Math.max(x2, x1);
            double smaller = Math.min(x2, x1);

            double larger_y = Math.max(y2, y1);
            double smaller_y = Math.min(y2, y1);


            if(Double.isFinite(m)){
                for(double i=smaller_y; i!=larger_y; i++){
                    double eq_x = (i-b)/m;
                    gc.fillOval(eq_x, i, c1.getRadius(), c1.getRadius());
                }
            }

            if(Double.isFinite(m)){
                for(double i=smaller; i!=larger; i++){
                    double eq_y = (m*i) + b;
                    gc.fillOval(i, eq_y, c1.getRadius(), c1.getRadius());
                }
            }

            else if(Double.isInfinite(m)){
                for(double i=smaller_y; i!=larger_y; i++){
                    gc.fillOval(x1, i, c1.getRadius(), c1.getRadius());
                }
            }

            c2 = c1;

        });
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


    @FXML
    public void save_transparent(){
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");

        File file = savefile.showSaveDialog(null);
        System.out.println("is file null ? "+ file);
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


}

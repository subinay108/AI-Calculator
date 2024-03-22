package calculator;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class HistoryLabel extends VBox{
    private Label inputLabel, outputLabel;
    private Line divider;

    // ******************** Methods *********************
    HistoryLabel(String inputString, String outputString, double length){
        // getStylesheets().add(HistoryLabel.class.getResource("historyLabel.css").toExternalForm());
        initGraphics(inputString, outputString, length);
    }

    // ******************** Initializations *********************
    private void initGraphics(String a, String b, double l){
        inputLabel = new Label(a);
        outputLabel = new Label("= " + b);
        outputLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        divider = new Line();
        // divider.setStartX(0.0f);
        // divider.setStartY(0.0f);
        divider.setEndX(l);
        divider.setStroke(Color.GRAY);
        // divider.setEndY(0f);
        setPrefWidth(l);
        setSpacing(0);
        getChildren().addAll(inputLabel, outputLabel, divider);
    }

}

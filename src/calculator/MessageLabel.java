package calculator;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageLabel extends VBox{
    private Label userTitle, aiTitle, userMessageLabel, aiMessageLabel;
    private HBox aiMessageBox;

    // ******************** Methods *********************
    MessageLabel(String userMessage, String aiMessage){
        getStylesheets().add(MessageLabel.class.getResource("../css/aimodal.css").toExternalForm());
        initGraphics(userMessage, aiMessage);
    }

    // ******************** Initializations *********************
    private void initGraphics(String a, String b){
        userTitle = new Label("You");
        aiTitle = new Label("You");
        userMessageLabel = new Label(a);
        aiMessageLabel = new Label(b);
        aiMessageBox = new HBox();
        aiMessageBox.getChildren().addAll(aiMessageBox);
        // outputLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        
        setPrefWidth(500);
        setSpacing(0);
        getChildren().addAll(userTitle, userMessageLabel, aiTitle, aiMessageLabel);
    }

}

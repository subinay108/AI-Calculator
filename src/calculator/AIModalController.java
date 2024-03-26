package calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AIModalController {
    @FXML
    private TextField promptInput;

    public void initialize(){

    }

    public void sendMesssage(ActionEvent e){
        String prompt = promptInput.getText();
        promptInput.clear();
        System.out.print(prompt);
    }

    public void closeModalWindow(MouseEvent e){
        // can be done by defining the close button in Controller.java 
        // System.out.println("close the window");
        // Platform.closeAIModal(e);
        // Platform.exit();
        // ImageView img = (ImageView) e.getTarget();
        // Parent p = img.getScene().getRoot();

    }
    
}

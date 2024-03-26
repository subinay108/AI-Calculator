package calculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AIModalController {
    @FXML
    private TextField promptInput;

    boolean isApiKeySet = false;

    public void initialize(){
        if(!isApiKeySet){
            showSettings();
        }
    }

    public void showSettings(){
        
    }

    public void sendMesssage(MouseEvent e){
        String prompt = promptInput.getText();
        if(prompt.isEmpty()){
            return;
        }
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

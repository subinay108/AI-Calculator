package calculator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Controller {

    @FXML
    private Label outputLabel, inputLabel;

    @FXML
    private Button sinBtn, cosBtn, tanBtn, lnBtn, exBtn, sinhBtn, coshBtn, tanhBtn, mrBtn, aiBtn;

    @FXML
    private VBox historyBox;

    @FXML
    private AnchorPane historySideBar;

    @FXML
    private ImageView hisImageView;

    private Vector<String> inputStringList = new Vector<String>();

    private String OperatorString = "+−×÷";
    private String numberString = "";
    private double memData = 0;
    Image backImage, hisImage;

    private boolean isNumber(String s){
        if("0123456789.".contains(s)){
            return true;
        }
        return false;
    }
    
    public void initialize() {
        // setting labels
        inputLabel.setText("");
        outputLabel.setText("");

        // load images
        File file = new File("src\\img\\back24.png");
        backImage = new Image(file.toURI().toString());
        file = new File("src\\img\\history32.png");
        hisImage = new Image(file.toURI().toString());

        // close historySidebar
        isHistoryBarOpen = true;
        System.out.println(historySideBar.getWidth());
        showHideHistoryBar();
        isHistoryBarOpen = false;

        // setting mr button state
        String d = "";
        try{
            d = Memory.getData();
            
        }catch(Exception e){
            System.out.println(e);
        }

        if(!d.isEmpty()){
            mrBtn.getStyleClass().remove("inputBtn, focus");
            mrBtn.getStyleClass().add("secondBtn");
        }
        else{
            mrBtn.getStyleClass().add("inputBtn, focus");
            mrBtn.getStyleClass().remove("secondBtn");
        }
    }

    private String getExpFromStringList(){
        String s = "";
        for(String x: inputStringList){
            s += x;
        }
        return s;
    }

    private void inputExpression(String nextInput){
        if(!numberString.isEmpty() && (nextInput.equals("10^") || nextInput.equals("2^"))){
            inputStringList.add("×");
        }

        if(isNumber(nextInput)){
            numberString += nextInput;
        }else{
            numberString = "";
        }

        if(!inputStringList.isEmpty() && 
        OperatorString.contains(inputStringList.lastElement()) && 
        OperatorString.contains(nextInput)){
            inputStringList.removeLast();
        }
        
        if(!nextInput.equals(" ")){
            inputStringList.add(nextInput);
        }

        String exp = getExpFromStringList();
        inputLabel.setText(exp);

        solveExpression();
    }

    private String getValidExpression(){
        String exp = "";

        for(String x: inputStringList){
            if(x.equals("−")){
                exp += Token.SUBTRACT;
            }else if(x.equals("×")){
                exp += Token.MULTIPLY;
            }else if(x.equals("÷")){
                exp += Token.DIVIDE;
            }else if(x.equals("π")){
                exp += Token.PI;
            }else if(x.equals("√")){
                exp += "2" + Token.RADICAL;
            }else if(x.equals("x√y")){
                exp += Token.RADICAL;
            }else if(x.equals("sin⁻¹(")){
                exp += "asin(";
            }else if(x.equals("cos⁻¹(")){
                exp += "acos(";
            }else if(x.equals("tan⁻¹(")){
                exp += "atan(";
            }else if(x.equals("sinh⁻¹(")){
                exp += "asinh(";
            }else if(x.equals("cosh⁻¹(")){
                exp += "acosh(";
            }else if(x.equals("tanh⁻¹(")){
                exp += "atanh(";
            }else if(x.equals("E")){
                if(exp.length() > 0 && isNumber(exp.substring(exp.length()-1))){
                    exp += "*";
                }
                exp += "10^";
            }else{
                exp += x;
            }
        }

        // if there is a operator in the last then remove it
        if(OperatorString.contains(inputStringList.lastElement())){
            exp = exp.substring(0, exp.length() - 1);
        }
        
        // counting the no. of parenthesis
        int openParenthesis = 0, closeParenthesis = 0;
        for(int i = 0; i < exp.length(); i++){
            if(exp.charAt(i) == '('){
                openParenthesis++;
            }else if(exp.charAt(i) == ')'){
                closeParenthesis++;
            }
        }

        // balancing the parenthesis
        if(openParenthesis < closeParenthesis){
            exp = "(".repeat(closeParenthesis - openParenthesis) + exp;
        }else{
            exp = exp + ")".repeat(openParenthesis - closeParenthesis);
        }

        return exp;
    }

    private void solveExpression(){
        if(inputStringList.isEmpty()){
            outputLabel.setText("");
            return;
        }
        String exp = getValidExpression();
        String result;
        try{
            Expression infix = new Expression(exp);
            infix.setDegreeAngle(isAngleDegree);
            result = infix.eval();
        }
        catch(Exception e){
            result = "Error";
            System.err.println(e);
        }
        if(result.equals("Infinity")){
            result = "Error";
        }
        outputLabel.setText(result);
    }

    public void inputNumber(ActionEvent e){
        Button button = (Button) e.getTarget();
        String opString = button.getText();
        if(opString.equals(".") && 
        !numberString.isEmpty() && 
        numberString.contains(".")){
            return;
        } 
        if(numberString.length() > 15){
            return;
        }
        if(numberString.equals("0")){
            if(opString.equals("0")){
                return;
            }
            inputStringList.removeLast();
        }
        inputExpression(opString);
    }

    public void inputOperator(ActionEvent e){
        Button button = (Button) e.getTarget();
        inputExpression(button.getText());
    }

    public void clearInput(ActionEvent e){ 
        inputStringList.clear();
        inputLabel.setText("");
        outputLabel.setText("");
        numberString = "";
    }

    public void inputEqual(ActionEvent e){
        if(outputLabel.getText().contains("E") || outputLabel.getText().equals("")){
            return;
        }
        inputStringList.clear();
        String outputString = outputLabel.getText();
        inputStringList.add(outputString);

        String iString = inputLabel.getText();
        HistoryLabel hl = new HistoryLabel(iString, outputString, 370);

        historyBox.getChildren().add(hl);

        inputLabel.setText(outputString);
        outputLabel.setText("");
    }

    public void backspace(ActionEvent e){
        if(!inputStringList.isEmpty()){
            inputStringList.removeLast();
        }
        if(!numberString.isEmpty()){
            numberString = numberString.substring(0, numberString.length() - 1);
        }
        inputExpression(" ");
    }

    public void inputFunction(ActionEvent e){
        Button button = (Button) e.getTarget();
        String fuString = button.getText();
        if(fuString.equals("lg")){
            inputExpression("lg(");
        }else if(fuString.equals("ln")){
            inputExpression("ln(");
        }else if(fuString.equals("eˣ")){
            inputExpression("e^");
        }else if(fuString.equals("Rand")){
            inputExpression("Rand");
        }else if(fuString.equals("10ˣ")){
            inputExpression("10^");
        }else if(fuString.equals("yˣ")){
            inputExpression("^");
        }else if(fuString.equals("x³")){
            inputExpression("^3");
        }else if(fuString.equals("x²")){
            inputExpression("^2");
        }else if(fuString.equals("x!")){
            inputExpression("!");
        }else if(fuString.equals("√")){
            inputExpression("");
        }else if(fuString.equals("sin")){
            inputExpression("sin(");
        }else if(fuString.equals("cos")){
            inputExpression("cos(");
        }else if(fuString.equals("tan")){
            inputExpression("tan(");
        }else if(fuString.equals("sinh")){
            inputExpression("sinh(");
        }else if(fuString.equals("cosh")){
            inputExpression("cosh(");
        }else if(fuString.equals("tanh")){
            inputExpression("tanh(");
        }else if(fuString.equals("sin⁻¹")){
            inputExpression("sin⁻¹(");
        }else if(fuString.equals("cos⁻¹")){
            inputExpression("cos⁻¹(");
        }else if(fuString.equals("tan⁻¹")){
            inputExpression("tan⁻¹(");
        }else if(fuString.equals("sinh⁻¹")){
            inputExpression("sinh⁻¹(");
        }else if(fuString.equals("cosh⁻¹")){
            inputExpression("cosh⁻¹(");
        }else if(fuString.equals("tanh⁻¹")){
            inputExpression("tanh⁻¹(");
        }else if(fuString.equals("1/x")){
            inputExpression("^(-1)");
        }else if(fuString.equals("x²")){
            inputExpression("x^2");
        }else if(fuString.equals("x³")){
            inputExpression("x^3");
        }else if(fuString.equals("EE")){
            inputExpression("E");
        }else if(fuString.equals("log2")){
            inputExpression("log2(");
        }else if(fuString.equals("2ˣ")){
            inputExpression("2^");
        }
    }

    boolean isAngleDegree = false;
    public void switchAngleMode(ActionEvent e){
        Button b = (Button) e.getTarget();
        isAngleDegree = !isAngleDegree;
        if(isAngleDegree){
            b.setText("Rad");
        }else{
            b.setText("Deg");
        }
        solveExpression();
    }

    boolean isSecondEnabled = false;
    public void secondOptions(ActionEvent e){
        isSecondEnabled = !isSecondEnabled;
        Button b = (Button) e.getTarget();
        if(isSecondEnabled){
            b.getStyleClass().remove("inputBtn, focus");
            b.getStyleClass().add("secondBtn");
            sinBtn.setText("sin⁻¹");
            cosBtn.setText("cos⁻¹");
            tanBtn.setText("tan⁻¹");
            sinhBtn.setText("sinh⁻¹");
            coshBtn.setText("cosh⁻¹");
            tanhBtn.setText("tanh⁻¹");
            lnBtn.setText("log2");
            exBtn.setText("2ˣ");


        }else{
            b.getStyleClass().add("inputBtn, focus");
            b.getStyleClass().remove("secondBtn");
            sinBtn.setText("sin");
            cosBtn.setText("cos");
            tanBtn.setText("tan");
            sinhBtn.setText("sinh");
            coshBtn.setText("cosh");
            tanhBtn.setText("tanh");
            lnBtn.setText("ln");
            exBtn.setText("eˣ");
        }
    }

    public void memoryClear(ActionEvent e){
        memData = 0;
        try{
            Memory.setData("");
        }catch(Exception ex){
            System.out.println(ex);
        }
        mrBtn.getStyleClass().add("inputBtn, focus");
        mrBtn.getStyleClass().removeAll("secondBtn");
    }

    public void memoryAddition(ActionEvent e){
        if(outputLabel.getText().contains("E")){
            return;
        }
        memData += Double.parseDouble(outputLabel.getText());
        
        mrBtn.getStyleClass().remove("inputBtn, focus");
        mrBtn.getStyleClass().add("secondBtn");

        DecimalFormat df = new DecimalFormat("#.###############");
        try{
            Memory.setData(String.valueOf(df.format(memData)));
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void memorySubtraction(ActionEvent e){
        if(outputLabel.getText().contains("E")){
            return;
        }
        if(memData == 0){
            mrBtn.getStyleClass().remove("inputBtn, focus");
            mrBtn.getStyleClass().add("secondBtn");
        }
        memData -= Double.parseDouble(outputLabel.getText());

        DecimalFormat df = new DecimalFormat("#.###############");
        try{
            Memory.setData(String.valueOf(df.format(memData)));
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void memoryRead(ActionEvent e){
        String d = "";
        try{
            d = Memory.getData();
        }catch(Exception ex){
            System.out.println(ex);
        }
        if(!d.isEmpty()){
            for(int i = 0; i < d.length(); i++){
                inputExpression(String.valueOf(d.charAt(i)));
            }
        }
    }

    public void plusminusBtnClicked(ActionEvent e){
        if(numberString.isEmpty()){
            return;
        }
        String tempString = new String(numberString);

        for(int i = 0; i < numberString.length(); i++){
            inputStringList.removeLast();
        }

        inputExpression("(");
        inputExpression("−");
        
        // numberString;
        // System.out.println(numberString.charAt(0));
        
        for(int i = 0; i < tempString.length(); i++){
            System.out.println(String.valueOf(tempString.charAt(i)));
            inputExpression(String.valueOf(tempString.charAt(i)));
        }

        inputExpression(")");
    }

    private boolean isHistoryBarOpen = true;
    public void showHideHistoryBar(){
        isHistoryBarOpen = !isHistoryBarOpen;
        if(isHistoryBarOpen){
            hisImageView.setImage(backImage);
            TranslateTransition openSideBar=new TranslateTransition(new Duration(350), historySideBar);
            openSideBar.setToX(0);
            openSideBar.play();
        }else{
            hisImageView.setImage(hisImage);
            TranslateTransition closeSideBar=new TranslateTransition(new Duration(350), historySideBar);
            double width = historySideBar.getWidth();
            if(width == 0){
                width = 400;
            }
            closeSideBar.setToX(-width-20);
            closeSideBar.play();
        }
    }

    public void clearHistory(){
        historyBox.getChildren().removeAll(historyBox.getChildren());
    }
    // private void clickShow(ActionEvent event) {
    //     Stage stage = new Stage();
    //     Parent root = FXMLLoader.load(Controller.class.getResource("YourClass.fxml"));
    //     stage.setScene(new Scene(root));
    //     stage.setTitle("My modal window");
    //     stage.initModality(Modality.WINDOW_MODAL);
    //     stage.initOwner(((Node)event.getSource()).getScene().getWindow() );
    //     stage.show();
    // }

    public void showAIModal(MouseEvent event) throws IOException{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Controller.class.getResource("aimodal.fxml"));
        stage.setScene(new Scene(root));
        // stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        stage.show();
        isAIModalOpen = true;
    }
    static boolean isAIModalOpen = false;
    public void closeAIModal(Stage modal){
        modal.close();
    }
}

// ⁻¹
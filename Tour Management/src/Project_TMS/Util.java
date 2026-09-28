package Project_TMS;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Util {
    
    public static void addScene(Stage stage, Scene scene){
        stage.setScene(scene);
    }
    
    public static String formatCurrency(double amount) {
        Locale philippines = new Locale("en", "PH");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(philippines);
        return currencyFormat.format(amount);
    }
    
    public static Button createButton(String buttonLabel, int width, String buttonStyle){
        
        Button button = new Button(buttonLabel);
            button.setPrefWidth(width);
            button.setStyle(buttonStyle);
            button.setCursor(Cursor.HAND);
        
        return button;
    }
    
    public static String setButtonStyle(String labelColor, String labelSize, String buttonColor, String backgroundRadius){
        
        String buttonStyle = "-fx-font-size: "+labelSize+";"
                + "-fx-text-fill: "+labelColor+";"
                + "-fx-background-color: "+buttonColor+";"
                + "-fx-background-radius: "+backgroundRadius+";"
                + "-fx-border-color: transparent;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Arial;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.3, -1, 3);";
        
        return buttonStyle;
    }
    
    //with shadow effect
    public static String setButtonStyle(String labelColor, String labelSize, String buttonColor, String backgroundRadius, String shadowEffect){
        
        String buttonStyle = "-fx-font-size: "+labelSize+";"
                + "-fx-text-fill: "+labelColor+";"
                + "-fx-background-color: "+buttonColor+";"
                + "-fx-background-radius: "+backgroundRadius+";"
                + "-fx-border-color: transparent;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Arial;"
                + "-fx-effect: "+shadowEffect+";";
        
        return buttonStyle;
    }
    
    public static Label createLabel(String content, double size, String fontColor, String weight){
        Label label = new Label(content);
        label.setStyle("-fx-font-size: "+size+";"
                + "-fx-text-fill: "+fontColor+";"
                + "-fx-font-weight:"+ weight+";"
                + "-fx-font-family: Arial;");
        return label;
    }
    
    public static void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    

    
    
}

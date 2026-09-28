package Project_TMS;

import database.TouristManager;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tourist.Tourist;

public class ForgotPassword {
    
    String formStyle = "-fx-background-color: rgba(255, 255, 255, 0.5);"
            + "-fx-background-radius: 15px;"
            + "-fx-border-radius: 15px;";
    
    String titleStyle = "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 35px;"
            + "-fx-font-family: Arial;";
    
    String labelStyle = "-fx-font-size: 12px;"
                + "-fx-text-fill: black;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Arial;";
    
    private TouristManager touristManager = new TouristManager();
    
    TextField phoneField,emailField, usernameField;
    PasswordField newpasswordField, confpasswordField;
    
    public Scene getForgotPasswordScene(Stage primaryStage){
        //parent container
        VBox parentContainer = new VBox();
            parentContainer.setStyle("-fx-background-color: rgba(57, 50, 50, 1);"); 
            parentContainer.setPadding(new Insets(50,0,50,0));
            parentContainer.setSpacing(5);
            parentContainer.setAlignment(Pos.CENTER);
        
        //title
        Label titleLabel = new Label("Forgot Password?");
            titleLabel.setStyle(titleStyle);
            titleLabel.setScaleX(1.1);
            
        HBox titleContainer = new HBox(titleLabel);
            titleContainer.setMaxWidth(375);
        
        //tag
        Label tagLabel = new Label("Don’t worry let’s recover your account!");
            tagLabel.setStyle(labelStyle+ "-fx-text-fill: yellow; -fx-font-size: 16px;");
        HBox tagContainer = new HBox(tagLabel);
            tagContainer.setMaxWidth(400);
        
            
        //form
        Label phoneLabel = new Label("Phone Number:");
            phoneLabel.setStyle(labelStyle);
        phoneField = new TextField();
            phoneField.setPromptText("Enter your account phone number");
        
        Label emailLabel = new Label("Email:");
            emailLabel.setStyle(labelStyle);
        emailField = new TextField();
            emailField.setPromptText("Enter your account email");
            
        Label usernameLabel = new Label("Username:");
            usernameLabel.setStyle(labelStyle);
        usernameField = new TextField();
            usernameField.setPromptText("Enter your username");
            
        Label newpasswordLabel = new Label("New Password:");
            newpasswordLabel.setStyle(labelStyle);
        newpasswordField = new PasswordField();
            newpasswordField.setPromptText("Enter your new password");
        
        Label confpasswordLabel = new Label("Confirm Password:");
            confpasswordLabel.setStyle(labelStyle);
        confpasswordField = new PasswordField();
            confpasswordField.setPromptText("Re-type your new password");
        
        
        VBox formContainer = new VBox(phoneLabel, phoneField, emailLabel, emailField, usernameLabel, usernameField, newpasswordLabel, newpasswordField, confpasswordLabel, confpasswordField);
            formContainer.setStyle(formStyle);
            formContainer.setMaxWidth(400);
            formContainer.setPadding(new Insets(10,20,15,20));
            formContainer.setSpacing(5);
            
        
        
            
        //buttons
        Button recoverButton = Util.createButton("Recover", 120, Util.setButtonStyle("black", "13px", "rgba(239, 247, 0, 1)", "5px"));
                 
        Button cancelButton = Util.createButton("Cancel", 75, Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px"));
            
        HBox buttonContainer = new HBox(cancelButton, recoverButton);
            buttonContainer.setAlignment(Pos.CENTER_RIGHT);
            buttonContainer.setSpacing(15);
            buttonContainer.setScaleX(1.038);
            buttonContainer.setMaxSize(387, 100);
            buttonContainer.setPadding(new Insets(5,0,0,0));
            
        
        //events
        recoverButton.setOnAction(e -> recoverAccount(primaryStage)); 
        recoverButton.setOnMouseEntered(e -> recoverButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(211, 215, 111, 1)", "5px")));
        recoverButton.setOnMouseExited(e -> recoverButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(239, 247, 0, 1)", "5px")));
            
        cancelButton.setOnAction(e -> cancel(primaryStage));
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(255, 255, 255, 0.7)", "5px")));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px")));

             
        parentContainer.getChildren().addAll(titleContainer,tagContainer,formContainer, buttonContainer);
        
        Scene scene = new Scene(parentContainer, 1000, 700);
        
        return scene;       
    }
    
    public void cancel(Stage primaryStage){
        Login login = new Login();
        Util.addScene(primaryStage, login.getLoginScene(primaryStage));
    }
    
    public void recoverAccount(Stage primaryStage) {
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String newPass = newpasswordField.getText();
        String confirmPass = confpasswordField.getText();

        if (phone.isEmpty() || email.isEmpty() || username.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Util.showAlert("Error", "All fields are required.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Util.showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }
;
        Tourist tourist = touristManager.findTouristByDetails(username, email, phone);

        if (tourist == null) {
            Util.showAlert("Error", "Account not found. Please check your details.", Alert.AlertType.ERROR);
            return;
        }

        boolean updated = touristManager.updatePassword(tourist.getTouristID(), newPass);

        if (updated) {
            Util.showAlert("Success", "Password updated successfully.", Alert.AlertType.INFORMATION);
            Login login = new Login();
            Util.addScene(primaryStage, login.getLoginScene(primaryStage));
        }
        else {
            Util.showAlert("Error", "Failed to update password. Try again later.", Alert.AlertType.ERROR);
        }
    }

    
}

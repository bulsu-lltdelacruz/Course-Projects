package Project_TMS;

import database.AdminManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.stage.Stage;

public class TravelMate_Admin {
    
    
    TextField usernameField;
    PasswordField passwordField;
    Button loginButton, normalLogInButton;   

    String borderStyle = "-fx-background-color: #393232;";
    
    String centerStyle = "-fx-background-color: rgba(255, 255, 255, 0.5);"
            + "-fx-border-radius: 20px;"
            + "-fx-border-weight: 5px;"
            + "-fx-background-radius: 20px;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0.5, -3, 3);";
    
    String titleStyle = "-fx-font-size: 25px;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Segoe UI;";
    
    String labelStyle = "-fx-font-size: 13px;"
                + "-fx-text-fill: black;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Arial;";
    
    String fieldStyle = "-fx-font-size: 13px;"
                + "-fx-text-fill: black;"
                + "-fx-font-family: Arial;";

    
    public Scene getAdminLoginScene(Stage primaryStage){
   
        Label titleLabel = new Label("TravelMate-Admin");
            titleLabel.setStyle(titleStyle);
        HBox titleContainer = new HBox(titleLabel);
            titleContainer.setAlignment(Pos.CENTER);
            
        Label usernameLabel = new Label("Username:");
            usernameLabel.setStyle(labelStyle);
        usernameField = new TextField();
            usernameField.setStyle(fieldStyle);
            usernameField.setPromptText("Enter your username");
            
        Label passwordLabel = new Label("Password:");
            passwordLabel.setStyle(labelStyle);  
        passwordField = new PasswordField();
            passwordField.setStyle(fieldStyle);
            passwordField.setPromptText("Enter your password");

        //buttons
        loginButton = Util.createButton("Log In", 100, Util.setButtonStyle("black", "14px", "rgba(239, 247, 0, 1)", "5px"));
        
        HBox loginButtonContainer = new HBox(loginButton);
            loginButtonContainer.setAlignment(Pos.CENTER);
                        
        normalLogInButton = Util.createButton("Log in as a User", 160, Util.setButtonStyle("white", "13px", "transparent", "5px", "none"));
            
        //layouts
        HBox top = new HBox();
        HBox left = new HBox();
        HBox bottom = new HBox(normalLogInButton);
            bottom.setAlignment(Pos.BOTTOM_LEFT);
            bottom.setPadding(new Insets(5,0,20, 0));
        HBox right = new HBox();
        
        ColumnConstraints col1 = new ColumnConstraints();
            col1.setMinWidth(100);
        ColumnConstraints col2 = new ColumnConstraints();
            col2.setMinWidth(250);
        
        GridPane gridCenter = new GridPane();
            gridCenter.getColumnConstraints().addAll(col1,col2);
            gridCenter.setVgap(10);
            gridCenter.setHgap(10);
            gridCenter.setPadding(new Insets(30));
            gridCenter.add(titleContainer, 0, 0,10,1);
            gridCenter.add(usernameLabel,0,2);
            gridCenter.add(usernameField,0,3,10,1);
            gridCenter.add(passwordLabel,0,4);
            gridCenter.add(passwordField,0,5,10,1);
            gridCenter.add(loginButtonContainer,0,7,10,1);
        
        
        top.setStyle(borderStyle);
        top.setPrefHeight(200);
        
        left.setStyle(borderStyle);
        left.setPrefWidth(250);
        
        bottom.setStyle(borderStyle);
        bottom.setPrefHeight(200);
        
        right.setStyle(borderStyle);
        right.setPrefWidth(250);
        
        gridCenter.setStyle(centerStyle);
        gridCenter.setPrefSize(300,300);
        gridCenter.setMaxSize(300,300);
        
        
        
        //events 
        loginButton.setOnAction(e -> logInAdmin(primaryStage));  

        normalLogInButton.setOnAction(e->logInAsUser(primaryStage));
        
        normalLogInButton.setOnMouseEntered(e -> normalLogInButton.setStyle(Util.setButtonStyle("white", "13px", "transparent", "5px", "none")+"-fx-underline: true;"));
        normalLogInButton.setOnMouseExited(e -> normalLogInButton.setStyle(Util.setButtonStyle("white", "13px", "transparent", "5px", "none")));
         
        BorderPane borderPane = new BorderPane();
            borderPane.setStyle(borderStyle);      
            borderPane.setTop(top);
            borderPane.setLeft(left);
            borderPane.setBottom(bottom);
            borderPane.setRight(right);
            borderPane.setCenter(gridCenter);
      
        Scene scene = new Scene(borderPane,1000,700);   
        return scene;
    }
    
    
    private void logInAdmin(Stage primaryStage) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        /*if (username.isEmpty() || password.isEmpty()) {
            Util.showAlert( "Input Error", "Username and password cannot be empty.",Alert.AlertType.ERROR);
            return;
        }*/

        //AdminManager adminManager = new AdminManager();
        //boolean exists = adminManager.adminExists(username, password);

        //if (exists) {
            AdminHome adminHome = new AdminHome();
            Util.addScene(primaryStage, adminHome.getAdminScene(primaryStage));
        //}
       /* else {
            Util.showAlert("Login Failed", "Invalid username or password.",Alert.AlertType.ERROR);
        }*/
    }

    
    private void logInAsUser(Stage primaryStage){
        Login home = new Login();
        Util.addScene(primaryStage, home.getLoginScene(primaryStage));
    }
    
    
    private void signUp(Stage primaryStage){
        AddAccount signup = new AddAccount();
        Util.addScene(primaryStage, signup.getSingupScene(primaryStage));
    }
    
    private void forgotPassword(Stage primaryStage){
        ForgotPassword forgotPassword = new ForgotPassword();
        Util.addScene(primaryStage, forgotPassword.getForgotPasswordScene(primaryStage));
    }
    
}

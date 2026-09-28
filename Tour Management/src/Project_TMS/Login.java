package Project_TMS;

import database.TouristManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Login {
    
    private TouristManager touristManager = new TouristManager();
    
    TextField usernameField;
    PasswordField passwordField;
    Button loginButton, signupButton, forgotButton;   

    String borderStyle = "-fx-background-color: rgba(82, 89, 68, 1);";
    
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

    
    public Scene getLoginScene(Stage primaryStage){
        
        Label titleLabel = new Label("TravelMate");
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
        
        signupButton = Util.createButton("Sign Up", 100, Util.setButtonStyle("white", "13px", "transparent", "5px", "none"));
            
        forgotButton = Util.createButton("Forgot Password?", 150, Util.setButtonStyle("firebrick", "13px", "transparent", "5px", "none"));

        HBox loginButtonContainer = new HBox(loginButton);
            loginButtonContainer.setAlignment(Pos.CENTER);
            
            
        HBox signupforgotContainer = new HBox(170,signupButton, forgotButton);
            signupforgotContainer.setAlignment(Pos.CENTER);
        
        
        Button adminButton = Util.createButton("Log in as Admin", 160, Util.setButtonStyle("white", "13px", "transparent", "5px", "none"));
            
        //layouts
        HBox top = new HBox();
        HBox left = new HBox();
        HBox bottom = new HBox(adminButton);
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
            gridCenter.add(signupforgotContainer,0,8,10,1);
        
        
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
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                logIn(primaryStage);
            }
        });
        
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                logIn(primaryStage);
            }
        });
        
        loginButton.setOnAction(e -> logIn(primaryStage));
        
        signupButton.setOnAction(e -> signUp(primaryStage));
        
        forgotButton.setOnAction(e -> forgotPassword(primaryStage));
        
        adminButton.setOnAction(e->logInAdmin(primaryStage));
        
        adminButton.setOnMouseEntered(e -> adminButton.setStyle(Util.setButtonStyle("white", "13px", "transparent", "5px", "none")+"-fx-underline: true;"));
        adminButton.setOnMouseExited(e -> adminButton.setStyle(Util.setButtonStyle("white", "13px", "transparent", "5px", "none")));
        
         
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
    
    private void logIn(Stage primaryStage){
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if(username.isEmpty() || password.isEmpty()){
            Util.showAlert("Error", "Please fill in all fields.",Alert.AlertType.ERROR);
            return;
        }

        if(touristManager.isValidLogin(username, password)){
            Home home = new Home();
            Util.addScene(primaryStage, home.getHomeScene(primaryStage));
        }
        else {
            Util.showAlert("Login Failed", "Invalid username or password.",Alert.AlertType.ERROR);
        }
    }

    private void logInAdmin(Stage primaryStage){
        TravelMate_Admin adminHome = new TravelMate_Admin();
        Util.addScene(primaryStage, adminHome.getAdminLoginScene(primaryStage));
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

package Project_TMS;

import tourist.Tourist;
import database.TouristManager;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddAccount {
    
    String titleStyle = "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 35px;"
            + "-fx-font-family: Arial;";
    
    String gridStyle = "-fx-background-color: rgba(255, 255, 255, 0.5);"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0.5, -3, 3);"
            + "-fx-border-radius: 15px;"
            + "-fx-background-radius: 15px;";
    
    String imageStyle = "-fx-background-color: rgba(82, 89, 68, 1);"
            + "-fx-border-radius: 15px;"
            + "-fx-background-radius: 15px;";
    
    String labelStyle = "-fx-font-size: 12px; "
                + "-fx-text-fill: black; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Arial; ";  
    
    String tagStyle = "-fx-font-size: 25px; "
                + "-fx-text-fill: white; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Serif; ";
    
    private TouristManager touristManager = new TouristManager();
    
    
    TextField fnameField, lnameField, midnameField, phoneField, emailField, usernameField;
    PasswordField passwordField, confpasswordField;
    RadioButton maleButton, femaleButton;
    DatePicker bdayPicker;
    
    public Scene getSingupScene(Stage primaryStage){
        //parent layout
        VBox vbox = new VBox();
            vbox.setStyle("-fx-background-color: rgba(82, 89, 68, 1);");
            vbox.setPadding(new Insets(50,0,50,0));
            vbox.setSpacing(10);
            vbox.setAlignment(Pos.CENTER);
        
            
        //title
        Label title = new Label("Sign Up");     
             title.setStyle(titleStyle);
             title.setScaleX(1.1);
        
        HBox titleContainer = new HBox(title);
             titleContainer.setPrefSize(100, 70);
             titleContainer.setAlignment(Pos.BOTTOM_LEFT);
             titleContainer.setMaxWidth(700);
        
             
        //signup form container
        ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(60);
        ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(40);
        RowConstraints row = new RowConstraints();
            row.setPercentHeight(100);
        GridPane grid = new GridPane();
             grid.setStyle(gridStyle);
             grid.setPrefSize(250, 450);
             grid.getColumnConstraints().addAll(col1,col2);
             grid.getRowConstraints().add(row);  
             
        Image image = new Image("/images/signupImage2.jpg");
        ImageView imageView = new ImageView(image);
            imageView.setFitWidth(285);
            imageView.setFitHeight(457);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            
        Rectangle clip = new Rectangle(285, 457); 
            clip.setArcWidth(30); 
            clip.setArcHeight(30);
            imageView.setClip(clip);
            
        Label tagLabel = new Label("Your journey starts here!");
            tagLabel.setPadding(new Insets(30,0,0,0));
            tagLabel.setStyle(tagStyle+ "-fx-text-fill: yellow;");
            
        Label tagLabel2 = new Label("Sign Up or register your account now.");
            tagLabel2.setPadding(new Insets(55,0,0,0));
            tagLabel2.setStyle(labelStyle+ "-fx-text-fill: white;");
            
        StackPane imageWithLabel = new StackPane(imageView, tagLabel, tagLabel2);
            imageWithLabel.setAlignment(Pos.TOP_CENTER);
       
        VBox imageContainer = new VBox(imageWithLabel);
             imageContainer.setStyle(imageStyle);
             
             
        //signup form fields and labels                     
        VBox namesContainer = new VBox();
            namesContainer.setSpacing(5);
            namesContainer.setPadding(new Insets(10,20,5,20));
            
            Label fnameLabel = new Label("First Name:");
                fnameLabel.setStyle(labelStyle);
            fnameField = new TextField();
                fnameField.setPromptText("Enter your first name");
            Label midnameLabel = new Label("Middle Name:");
                midnameLabel.setStyle(labelStyle);
            midnameField = new TextField();
                midnameField.setPromptText("Enter your middle name");
            Label lnameLabel = new Label("Last Name:");
                lnameLabel.setStyle(labelStyle);
            lnameField = new TextField();
                lnameField.setPromptText("Enter your last name");
            
        namesContainer.getChildren().addAll(fnameLabel, fnameField, midnameLabel, midnameField, lnameLabel, lnameField);
        
        HBox bdaygenderContainer = new HBox();
            bdaygenderContainer.setAlignment(Pos.CENTER_LEFT);
            bdaygenderContainer.setSpacing(10);
            bdaygenderContainer.setPrefHeight(70);
            bdaygenderContainer.setPadding(new Insets(0,15,5,20));
            
            Label bdayLabel = new Label("Birthday:");
                bdayLabel.setStyle(labelStyle);
            bdayPicker = new DatePicker();
                bdayPicker.setPrefWidth(110);
                bdayPicker.setPromptText("mm/dd/yyyy");
            Label genderLabel = new Label("Gender:");
                genderLabel.setStyle(labelStyle);
                genderLabel.setPadding(new Insets(0,0,0,20));
            ToggleGroup genderGroup = new ToggleGroup();
            maleButton = new RadioButton("Male");
                maleButton.setStyle(labelStyle);
                maleButton.setToggleGroup(genderGroup);
            femaleButton = new RadioButton("Female");
                femaleButton.setStyle(labelStyle);
                femaleButton.setToggleGroup(genderGroup);

        bdaygenderContainer.getChildren().addAll(bdayLabel, bdayPicker,genderLabel, maleButton, femaleButton);
             
        VBox accContainer = new VBox();
            accContainer.setPrefHeight(260);
            accContainer.setPadding(new Insets(0,20,15,20));
            accContainer.setSpacing(5);
            
            Label phoneLabel = new Label("Phone Number:");
                phoneLabel.setStyle(labelStyle);
            phoneField = new TextField();          
                phoneField.setPromptText("Enter your phone number");
              
            Label emailLabel = new Label("Email:");
                emailLabel.setStyle(labelStyle);
            emailField = new TextField();
                emailField.setPromptText("Enter your email");
                
            Label usernameLabel = new Label("Username:");
                usernameLabel.setStyle(labelStyle);
            usernameField = new TextField();
                usernameField.setPromptText("Enter your username");
                
            Label passwordLabel = new Label("Password:");
                passwordLabel.setStyle(labelStyle);
            passwordField = new PasswordField();
                passwordField.setPromptText("Enter your password");
                
            Label confpasswordLabel = new Label("Confirm Password:");
                confpasswordLabel.setStyle(labelStyle);
            confpasswordField = new PasswordField();
                confpasswordField.setPromptText("Re-type your password");
         
        accContainer.getChildren().addAll(phoneLabel,phoneField, emailLabel,emailField, usernameLabel, usernameField, passwordLabel, passwordField, confpasswordLabel, confpasswordField);
        
        VBox formContainer = new VBox(namesContainer, bdaygenderContainer, accContainer);
        
        //buttons
            HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.CENTER_RIGHT);
                buttonContainer.setSpacing(15);
                buttonContainer.setScaleX(1.038);
                buttonContainer.setMaxWidth(685);
                
            Button signupButton = Util.createButton("Sign Up", 120, Util.setButtonStyle("black", "13px", "rgba(25, 198, 36, 1)", "5px"));
                 
            Button cancelButton = Util.createButton("Cancel", 75, Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px"));
        
            buttonContainer.getChildren().addAll(cancelButton, signupButton);
        
        //events
            signupButton.setOnAction(e -> signUp(primaryStage));
            signupButton.setOnMouseEntered(e -> signupButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(119, 231, 126, 1)", "5px")));
            signupButton.setOnMouseExited(e -> signupButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(25, 198, 36, 1)", "5px")));
            
            cancelButton.setOnAction(e -> cancel(primaryStage));
            cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(255, 255, 255, 0.7)", "5px")));
            cancelButton.setOnMouseExited(e -> cancelButton.setStyle(Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px")));

             

        grid.add(formContainer, 0, 0);
        grid.add(imageContainer, 1, 0);
        grid.setMaxSize(685, 500);
        
        vbox.getChildren().addAll(titleContainer, grid, buttonContainer);
    
        
            
        Scene scene = new Scene(vbox,1000,700);
        return scene;
    }
    
    private void cancel(Stage primaryStage){
        Login login = new Login();
        Util.addScene(primaryStage, login.getLoginScene(primaryStage));
    }
    
    private void signUp(Stage primaryStage){
        if(addNewAccount()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Account created successfully!");
            alert.showAndWait();

            Login login = new Login();
            Util.addScene(primaryStage, login.getLoginScene(primaryStage));
        }
        
    }
    
    private Tourist getAllInputs() {
        String firstName = fnameField.getText().trim();
        String middleName = midnameField.getText().trim();
        String lastName = lnameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confpasswordField.getText().trim();
        String gender = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : "");
        LocalDate birthday = bdayPicker.getValue();
        

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() ||
            email.isEmpty() || username.isEmpty() || password.isEmpty() ||
            confirmPassword.isEmpty() || gender.isEmpty() || birthday == null) {
            showAlert("Missing Field", "Please fill out all required fields.");
            return null;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return null;
        }

        if (!phone.matches("^\\d{10,11}$")) {
            showAlert("Invalid Phone", "Phone number must be 10 to 11 digits.");
            return null;
        }

        if (!username.matches("^[a-zA-Z0-9]{5,15}$")) {
            showAlert("Invalid Username", "Username must be alphanumeric and 5–15 characters.");
            return null;
        }

        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")) {
            showAlert("Weak Password", "Password must be at least 6 characters, include letters and numbers.");
            return null;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return null;
        }

        return new Tourist(
                touristManager.generateTouristID() , firstName, 
                middleName, lastName,
                birthday, gender, phone,
                email, username, password
        );
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    
    private boolean addNewAccount(){
        Tourist tourist = getAllInputs();
        if(tourist != null){
            touristManager.addTourist(tourist);
            return true;
        }
        else{
            System.out.println("burat samar philippines ka");
            return false;
        }
    }
    
}
